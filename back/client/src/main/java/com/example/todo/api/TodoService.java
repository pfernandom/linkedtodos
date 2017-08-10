package com.example.todo.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.todo.Todo;

public interface TodoService {
	CompletableFuture<Todo> getTodo(long id);

	CompletableFuture<Todo> addTodo(Todo todo);
	
	void deleteTodo(Long id);
	
	CompletableFuture<Todo> updateTodo(Long id, Todo todo);
	
	CompletableFuture<List<Todo>> getAllTodos();
	
	CompletableFuture<ProjectWithTodos> getProjectWithTodos(Long idProject);
	
	CompletableFuture<List<ProjectWithTodos>> getAllProjectWithTodos();

}
