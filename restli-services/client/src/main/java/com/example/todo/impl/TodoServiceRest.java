package com.example.todo.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.example.todo.Project;
import com.example.todo.ProjectsBuilders;
import com.example.todo.ProjectsGetAllBuilder;
import com.example.todo.ProjectsGetBuilder;
import com.example.todo.Todo;
import com.example.todo.TodosBuilders;
import com.example.todo.TodosCreateBuilder;
import com.example.todo.TodosDeleteBuilder;
import com.example.todo.TodosGetAllBuilder;
import com.example.todo.TodosGetBuilder;
import com.example.todo.TodosUpdateBuilder;
import com.example.todo.api.ProjectWithTodos;
import com.example.todo.api.TodoService;
import com.example.util.RestUtils;
import com.google.common.collect.Lists;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.Task;
import com.linkedin.restli.client.CreateRequest;
import com.linkedin.restli.client.ParSeqRestClient;
import com.linkedin.restli.client.Request;
import com.linkedin.restli.client.Response;
import com.linkedin.restli.common.CollectionResponse;
import com.linkedin.restli.common.EmptyRecord;

/**
 * Implementation of the TodoService which uses Rest.li to retrieve Projects and
 * Todos
 * 
 * @author pedro.f.marquez.soto
 *
 */
public class TodoServiceRest implements TodoService {

	private Engine engine;
	private ParSeqRestClient _parseqClient;
	private static final TodosBuilders _todosBuilders = new TodosBuilders();
	private static final ProjectsBuilders _projectsBuilders = new ProjectsBuilders();
	private RestUtils<Todo> todoUtils;
	private String url;

	/**
	 * Constructor
	 */
	@Deprecated
	public TodoServiceRest() {
		todoUtils = new RestUtils<Todo>();
	}

	/**
	 * Constructor
	 * @param engine - ParSeq engine
	 * @param clientUrl - URL of the Rest.li service
	 */
	public TodoServiceRest(Engine engine, String clientUrl) {
		todoUtils = new RestUtils<Todo>();
		this.engine = engine;
		this.url = clientUrl;
		this._parseqClient = RestUtils.getClient(this.url);

	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#getTodo(long)
	 */
	@Override
	public CompletableFuture<Todo> getTodo(long id) {
		TodosGetBuilder getBuilder = _todosBuilders.get();
		Request<Todo> todoRequest = getBuilder.id(id).build();

		CompletableFuture<Todo> response = new CompletableFuture<>();

		Task<Todo> todoResponseTask = _parseqClient.createTask(todoRequest).map(todo -> todo.getEntity())
				.andThen("return", (res) -> {
					response.complete(res);
				});

		engine.run(todoResponseTask);

		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#addTodo(com.example.todo.Todo)
	 */
	@Override
	public CompletableFuture<Todo> addTodo(Todo todo) {
		TodosCreateBuilder getBuilder = _todosBuilders.create();

		CreateRequest<Todo> todoRequest = getBuilder.input(todo).build();

		CompletableFuture<Todo> response = new CompletableFuture<>();

		Task<Response<EmptyRecord>> todoResponseTask = _parseqClient.createTask(todoRequest).andThen("return",
				(res) -> {
					todo.setId(Long.parseLong(res.getId()));
					response.complete(todo);

				});

		engine.run(todoResponseTask);
		return response;

	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#updateTodo(java.lang.Long, com.example.todo.Todo)
	 */
	@Override
	public CompletableFuture<Todo> updateTodo(Long id, Todo todo) {
		TodosUpdateBuilder getBuilder = _todosBuilders.update();
		Request<EmptyRecord> todoRequest = getBuilder.id(id).input(todo).build();

		CompletableFuture<Todo> response = new CompletableFuture<>();

		Task<Response<EmptyRecord>> todoResponseTask = _parseqClient.createTask(todoRequest).andThen("return",
				(res) -> {
					response.complete(todo);

				});

		engine.run(todoResponseTask);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#getAllTodos()
	 */
	@Override
	public CompletableFuture<List<Todo>> getAllTodos() {
		TodosGetAllBuilder getBuilder = _todosBuilders.getAll();
		Request<CollectionResponse<Todo>> todoRequest = getBuilder.build();

		CompletableFuture<List<Todo>> response = new CompletableFuture<>();

		Task<Response<CollectionResponse<Todo>>> todoResponseTask = _parseqClient.createTask(todoRequest)
				.andThen("return", (res) -> {

					List<Todo> todos = Lists.newArrayList();
					todos.addAll(res.getEntity().getElements());

					response.complete(todos);
				});

		engine.run(todoResponseTask);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#getProjectWithTodos(java.lang.Long)
	 */
	@Override
	public CompletableFuture<ProjectWithTodos> getProjectWithTodos(Long idProject) {
		ProjectsGetBuilder getBuilder = _projectsBuilders.get();
		Request<Project> projectRequest = getBuilder.id(idProject).build();

		TodosGetAllBuilder getTodosBuilder = _todosBuilders.getAll();
		Request<CollectionResponse<Todo>> todoRequest = getTodosBuilder.build();

		CompletableFuture<ProjectWithTodos> response = new CompletableFuture<>();

		Task<Response<Project>> projectResponse = _parseqClient.createTask(projectRequest);
		Task<Response<CollectionResponse<Todo>>> todosResponse = _parseqClient.createTask(todoRequest);

		Task<ProjectWithTodos> print = Task.par(projectResponse, todosResponse).map("map", (tuple) -> {
			Project project = tuple._1().getEntity();
			CollectionResponse<Todo> todosList = tuple._2().getEntity();

			ProjectWithTodos pt = new ProjectWithTodos();
			pt.setId(Long.toString(project.getId()));
			pt.setName(project.getName());
			pt.setDescription(project.getDescription());

			List<Todo> todos = new ArrayList<Todo>();

			todosList.getElements().stream().filter(t -> t.getProjectId() == project.getId()).forEach(todos::add);

			pt.setTodos(todos);

			return pt;
		}).andThen("print", (str) -> {
			response.complete(str);
		});

		engine.run(print);

		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		engine.shutdown();
		engine.awaitTermination(5, TimeUnit.SECONDS);
		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#getAllProjectWithTodos()
	 */
	@Override
	public CompletableFuture<List<ProjectWithTodos>> getAllProjectWithTodos() {
		ProjectsGetAllBuilder getBuilder = _projectsBuilders.getAll();
		Request<CollectionResponse<Project>> projectRequest = getBuilder.build();

		TodosGetAllBuilder getTodosBuilder = _todosBuilders.getAll();
		Request<CollectionResponse<Todo>> todoRequest = getTodosBuilder.build();

		CompletableFuture<List<ProjectWithTodos>> response = new CompletableFuture<>();

		Task<Response<CollectionResponse<Project>>> projectResponse = _parseqClient.createTask(projectRequest);
		Task<Response<CollectionResponse<Todo>>> todosResponse = _parseqClient.createTask(todoRequest);

		Task<List<ProjectWithTodos>> print = Task.par(projectResponse, todosResponse).map("map", (tuple) -> {
			CollectionResponse<Project> projectList = tuple._1().getEntity();
			CollectionResponse<Todo> todosList = tuple._2().getEntity();

			List<ProjectWithTodos> result = new ArrayList<ProjectWithTodos>();

			projectList.getElements().stream().forEach(p -> {
				ProjectWithTodos pt = new ProjectWithTodos();
				pt.setId(Long.toString(p.getId()));
				pt.setName(p.getName());
				pt.setDescription(p.getDescription());

				List<Todo> todos = new ArrayList<Todo>();

				todosList.getElements().stream().filter(t -> t.getProjectId() == p.getId()).forEach(todos::add);

				pt.setTodos(todos);
				result.add(pt);

			});

			return result;
		}).andThen("print", (str) -> {
			response.complete(str);
		});

		engine.run(print);

		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see com.example.todo.api.TodoService#deleteTodo(java.lang.Long)
	 */
	@Override
	public void deleteTodo(Long id) {
		TodosDeleteBuilder getBuilder = _todosBuilders.delete();
		Request<EmptyRecord> todoRequest = getBuilder.id(id).build();

		Task<Response<EmptyRecord>> todoResponseTask = _parseqClient.createTask(todoRequest);

		engine.run(todoResponseTask);
	}

}
