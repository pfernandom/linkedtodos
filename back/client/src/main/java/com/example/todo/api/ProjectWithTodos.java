package com.example.todo.api;

import java.util.List;

import com.example.todo.Project;
import com.example.todo.Todo;

public class ProjectWithTodos {
	Project project;
	List<Todo> todos;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

}
