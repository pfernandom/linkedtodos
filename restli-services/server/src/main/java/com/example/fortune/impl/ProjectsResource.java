package com.example.fortune.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.todo.Project;
import com.example.todo.Todo;
import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateResponse;
import com.linkedin.restli.server.PagingContext;
import com.linkedin.restli.server.UpdateResponse;
import com.linkedin.restli.server.annotations.Context;
import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.resources.CollectionResourceTemplate;

/**
 * Rest.li resource to manage Projects.
 * 
 * This implementation uses an in-memory HashMap to store Projects.It should
 * only be used for demostrative purposes, and should be replaced with an
 * implementation which uses a better datasource to persist data.
 * 
 * @author pedro.f.marquez.soto
 *
 */
@RestLiCollection(name = "projects", namespace = "com.example.todo")
public class ProjectsResource extends CollectionResourceTemplate<Long, Project> {

	static Map<Long, Project> projects = new HashMap<Long, Project>();
	static Long inc = 3l;
	static {

		Project project = new Project();
		project.setId(1L);
		project.setName("Interview");
		project.setDescription("Things to do before the interview");
		projects.put(1L, project);

		project = new Project();
		project.setId(2L);
		project.setName("Post-Interview");
		project.setDescription("Things to do after the interview");
		projects.put(2L, project);

	}

	/* (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#getAll(com.linkedin.restli.server.PagingContext)
	 */
	@Override
	public List<Project> getAll(@Context PagingContext pagingContext) {
		return projects.values().stream().collect(Collectors.toList());
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#get(java.lang.Object)
	 */
	@Override
	public Project get(Long key) {
		return projects.get(key);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#create(com.linkedin.data.template.RecordTemplate)
	 */
	@Override
	public CreateResponse create(Project entity) {
		entity.setId(++inc);
		projects.put(inc, entity);
		return new CreateResponse(inc);
	}

	/*
	 * (non-Javadoc)
	 * @see com.linkedin.restli.server.resources.CollectionResourceTemplate#update(java.lang.Object, com.linkedin.data.template.RecordTemplate)
	 */
	@Override
	public UpdateResponse update(Long key, Project entity) {
		entity.setId(key);
		projects.put(key, entity);
		return new UpdateResponse(HttpStatus.S_200_OK);
	}

}
