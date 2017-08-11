package com.example.todo.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.todo.Todo;

/**
 * A service to manage TODOS. It uses CompletableFuture to enable non-blocking
 * requests.
 * 
 * @author pedro.f.marquez.soto
 *
 */
public interface TodoService {
	/**
	 * Retrieve a Todo
	 * 
	 * @param id
	 * @return a Todo
	 */
	CompletableFuture<Todo> getTodo(long id);

	/**
	 * Create a Todo
	 * 
	 * @param todo
	 * @return a Todo
	 */
	CompletableFuture<Todo> addTodo(Todo todo);

	/**
	 * Delete a Todo
	 * 
	 * @param id
	 */
	void deleteTodo(Long id);

	/**
	 * Update a Todo
	 * 
	 * @param id
	 * @param todo
	 * @return a Todo
	 */
	CompletableFuture<Todo> updateTodo(Long id, Todo todo);

	/**
	 * Retrieve all Todos
	 * 
	 * @return a list of Todos
	 */
	CompletableFuture<List<Todo>> getAllTodos();

	/**
	 * Retrieve a project with its Todos embedded
	 * 
	 * @param idProject
	 * @return A project with it's Todos
	 */
	CompletableFuture<ProjectWithTodos> getProjectWithTodos(Long idProject);

	/**
	 * Retrieve all projects with their Todos embedded
	 * 
	 * @return A list of projects with their Todos
	 */
	CompletableFuture<List<ProjectWithTodos>> getAllProjectWithTodos();

}
