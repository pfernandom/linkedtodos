package com.example.fortune.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.todo.Todo;
import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateKVResponse;
import com.linkedin.restli.server.PagingContext;
import com.linkedin.restli.server.UpdateResponse;
import com.linkedin.restli.server.annotations.Context;
import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.annotations.RestMethod;
import com.linkedin.restli.server.annotations.ReturnEntity;
import com.linkedin.restli.server.resources.CollectionResourceTemplate;

/**
 * Rest.li resource to manage Todos.
 * 
 * This implementation uses an in-memory HashMap to store the Todos. It should
 * only be used for demostrative purposes, and should be replaced with an
 * implementation which uses a better datasource to persist data.
 * 
 * @author pedro.f.marquez.soto
 *
 */
@RestLiCollection(name = "todos", namespace = "com.example.todo")
public class TodosResource extends CollectionResourceTemplate<Long, Todo> {

	final static Logger LOG = LoggerFactory.getLogger(TodosResource.class);

	static Map<Long, Todo> todos = new HashMap<Long, Todo>();
	static Long inc = 4l;
	static {

		Todo todo = new Todo();
		todo.setId(1L);
		todo.setCompleted(false);
		todo.setDescription("Learn Rest.li");
		todo.setProjectId(1L);
		todo.setDeleted(false);
		todos.put(1L, todo);

		todo = new Todo();
		todo.setId(2L);
		todo.setCompleted(false);
		todo.setDescription("Learn Play Framework");
		todo.setProjectId(1L);
		todo.setDeleted(false);
		todos.put(2L, todo);

		todo = new Todo();
		todo.setId(3L);
		todo.setCompleted(false);
		todo.setDescription("Learn EmberJS");
		todo.setProjectId(1L);
		todo.setDeleted(false);
		todos.put(3L, todo);

		todo = new Todo();
		todo.setId(4L);
		todo.setCompleted(false);
		todo.setDescription("Have a beer");
		todo.setProjectId(2L);
		todo.setDeleted(false);
		todos.put(4L, todo);

	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#getAll(com.linkedin.restli.server.PagingContext)
	 */
	@Override
	public List<Todo> getAll(@Context PagingContext pagingContext) {
		return todos.values().stream().filter(t -> {
			LOG.info("Filter todo: " + t.isDeleted());

			return !t.isDeleted();
		}).collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#get(java.lang.Object)
	 */
	@Override
	public Todo get(Long key) {
		return todos.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#create(com.linkedin.data.template.RecordTemplate)
	 */
	@Override
	@ReturnEntity
	public CreateKVResponse<Long, Todo> create(Todo entity) {
		entity.setId(++inc);
		todos.put(inc, entity);
		return new CreateKVResponse<Long, Todo>(inc, entity);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#update(java.lang.Object, com.linkedin.data.template.RecordTemplate)
	 */
	@Override
	@ReturnEntity
	public UpdateResponse update(Long key, Todo entity) {
		entity.setId(key);
		todos.put(key, entity);
		return new UpdateResponse(HttpStatus.S_204_NO_CONTENT);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#delete(java.lang.Object)
	 */
	@Override
	public UpdateResponse delete(Long key) {
		todos.get(key).setDeleted(true);
		return new UpdateResponse(HttpStatus.S_204_NO_CONTENT);
	}

}
