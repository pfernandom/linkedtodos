package com.example.fortune.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.todo.Todo;
import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateResponse;
import com.linkedin.restli.server.PagingContext;
import com.linkedin.restli.server.UpdateResponse;
import com.linkedin.restli.server.annotations.Context;
import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.resources.CollectionResourceTemplate;

@RestLiCollection(name = "todos", namespace = "com.example.todo")
public class TodosResource extends CollectionResourceTemplate<Long, Todo> {

	static Map<Long, Todo> todos = new HashMap<Long, Todo>();
	static Long inc = 4l;
	static {
		
		Todo todo = new Todo();
		todo.setId(1L);
		todo.setCompleted(false);
		todo.setDescription("Learn Rest.li");
		todo.setProjectId(1L);
		todos.put(1L, todo); 
		
		todo = new Todo();
		todo.setId(2L);
		todo.setCompleted(false);
		todo.setDescription("Learn Play Framework");
		todo.setProjectId(1L);
		todos.put(2L, todo); 
		
		todo = new Todo();
		todo.setId(3L);
		todo.setCompleted(false);
		todo.setDescription("Learn EmberJS");
		todo.setProjectId(1L);
		todos.put(3L, todo); 
		
		
		todo = new Todo();
		todo.setId(4L);
		todo.setCompleted(false);
		todo.setDescription("Have a beer");
		todo.setProjectId(2L);
		todos.put(4L, todo); 
		
	}

	@Override
	public List<Todo> getAll(@Context PagingContext pagingContext) {
		return todos.values().stream().collect(Collectors.toList());
	}

	@Override
	public Todo get(Long key) {
		return todos.get(key);
	}
	
	@Override
	public CreateResponse create(Todo entity) {
		entity.setId(++inc);
		todos.put(inc, entity);
		return new CreateResponse(inc);
	}
	
	@Override
	public UpdateResponse update(Long key, Todo entity) {
		entity.setId(key);
		todos.put(key, entity);
		return new UpdateResponse(HttpStatus.S_200_OK);
	}
	

}
