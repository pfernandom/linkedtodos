package com.example.todo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.todo.Project;
import com.example.todo.ProjectsBuilders;
import com.example.todo.ProjectsGetBuilder;
import com.example.todo.Todo;
import com.example.todo.TodosBuilders;
import com.example.todo.TodosCreateBuilder;
import com.example.todo.TodosGetAllBuilder;
import com.example.todo.TodosGetBuilder;
import com.example.todo.TodosUpdateBuilder;
import com.example.todo.api.ProjectWithTodos;
import com.example.todo.api.TodoService;
import com.example.util.RestUtils;
import com.google.common.collect.Lists;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.Task;
import com.linkedin.restli.client.ParSeqRestClient;
import com.linkedin.restli.client.Request;
import com.linkedin.restli.client.Response;
import com.linkedin.restli.common.CollectionResponse;
import com.linkedin.restli.common.EmptyRecord;

public class TodoServiceRest implements TodoService{
	
	private Engine engine;
	private ParSeqRestClient _parseqClient;
	private static final TodosBuilders _todosBuilders = new TodosBuilders();
	private static final ProjectsBuilders _projectsBuilders = new ProjectsBuilders();
	private RestUtils<Todo> todoUtils;
	private String url;
	
	public TodoServiceRest(){
		todoUtils = new RestUtils<Todo>();
	}
	
	public TodoServiceRest(Engine engine, String clientUrl){
		todoUtils = new RestUtils<Todo>();
		this.engine = engine;
		this.url = clientUrl;
		System.out.println("Connect to "+this.url);
		this._parseqClient = RestUtils.getClient(this.url);
		
	}

	@Override
	public CompletableFuture<Todo> getTodo(long id) {
		TodosGetBuilder getBuilder = _todosBuilders.get();
		Request<Todo> todoRequest = getBuilder.id(id).build();
		
		CompletableFuture<Todo> response =  new CompletableFuture<>();
		
		Task<Todo> todoResponseTask = _parseqClient.createTask(todoRequest)
				.map(todo -> todo.getEntity())
				.andThen("return", (res)->{
					response.complete(res);
				});
			
		engine.run(todoResponseTask);
		
		return response;
	}

	@Override
	public void addTodo(Todo todo) {
		TodosCreateBuilder getBuilder = _todosBuilders.create();
		Request<EmptyRecord> todoRequest = getBuilder.input(todo).build();
		
		Task<Response<EmptyRecord>> todoResponseTask = _parseqClient.createTask(todoRequest);
			
		engine.run(todoResponseTask);
		
	}

	@Override
	public void updateTodo(Long id, Todo todo) {
		TodosUpdateBuilder getBuilder = _todosBuilders.update();
		Request<EmptyRecord> todoRequest = getBuilder.id(id).input(todo).build();
		
		Task<Response<EmptyRecord>> todoResponseTask = _parseqClient.createTask(todoRequest);
			
		engine.run(todoResponseTask);
	}

	@Override
	public CompletableFuture<List<Todo>> getAllTodos() {
		TodosGetAllBuilder getBuilder = _todosBuilders.getAll();
		Request<CollectionResponse<Todo>> todoRequest = getBuilder.build();
		
		CompletableFuture<List<Todo>> response =  new CompletableFuture<>();
		
		Task<Response<CollectionResponse<Todo>>> todoResponseTask = _parseqClient.createTask(todoRequest)
				.andThen("return", (res)->{
					
					List<Todo> todos = Lists.newArrayList();
					todos.addAll(res.getEntity().getElements());
					
					response.complete(todos);
				});
	
		engine.run(todoResponseTask);
		return response;
	}
	
	@Override
	public CompletableFuture<ProjectWithTodos> getProjectWithTodos(Long idProject) {
		ProjectsGetBuilder getBuilder = _projectsBuilders.get();
		Request<Project> projectRequest = getBuilder.id(idProject).build();
		
		TodosGetAllBuilder getTodosBuilder = _todosBuilders.getAll();
		Request<CollectionResponse<Todo>> todoRequest = getTodosBuilder.build();
		
		CompletableFuture<ProjectWithTodos> response =  new CompletableFuture<>();
		
		Task<Response<Project>> projectResponse = _parseqClient.createTask(projectRequest);
		Task<Response<CollectionResponse<Todo>>> todosResponse = _parseqClient.createTask(todoRequest);
		
		Task<ProjectWithTodos> print = Task.par(projectResponse,todosResponse)
				.map("map", (tuple) -> {
					Project project = tuple._1().getEntity();
					CollectionResponse<Todo> todosList = tuple._2().getEntity();
					
					ProjectWithTodos pt = new ProjectWithTodos();
					pt.setProject(project);
					
					List<Todo> todos = new ArrayList<Todo>();
					
					todosList.getElements()
						.stream()
						.filter(t -> t.getProjectId() == project.getId()).forEach(todos::add);
					
					pt.setTodos(todos);
					
				return pt;
			})
			.andThen("print", (str)->{
				System.out.println(str);
				response.complete(str);
			});
		
		engine.run(print);
		
		return response;
	}

		
	@Override
	protected void finalize() throws Throwable {
		engine.shutdown();
		engine.awaitTermination(5, TimeUnit.SECONDS);
		super.finalize();
	}

}
