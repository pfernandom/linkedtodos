package com.example.todo.api;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.todo.Todo;

public interface TodoService {
	CompletableFuture<Todo> getTodo(long id);

	void addTodo(Todo todo);

	void updateTodo(Long id, Todo todo);
	
	CompletableFuture<List<Todo>> getAllTodos();
	
	
	CompletableFuture<ProjectWithTodos> getProjectWithTodos(Long idProject);

}