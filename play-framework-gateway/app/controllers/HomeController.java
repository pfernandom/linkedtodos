package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.todo.Todo;
import com.example.todo.api.TodoService;
import com.google.inject.Inject;

import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's main API.
 */
public class HomeController extends Controller {

	final static Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Inject
	private TodoService todoService;

	@Inject
	private FormFactory formFactory;

	/**
	 * Welcome message view.
	 */
	public Result index() {
		return ok(views.html.index.render());
	}

	/**
	 * Retrieves all the Todos
	 * 
	 * @return
	 * @throws Exception
	 */
	public CompletionStage<Result> todos() throws Exception {
		return todoService.getAllTodos().thenApply(i -> {
			return ok(Json.toJson(i));
		});
	}

	/**
	 * Retrieves one project by its ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CompletionStage<Result> projects(Long id) throws Exception {
		return todoService.getProjectWithTodos(id).thenApply(i -> {
			return ok(Json.toJson(i));
		});
	}

	/**
	 * Retrieves all projects
	 * 
	 * @return
	 * @throws Exception
	 */
	public CompletionStage<Result> allProjects() throws Exception {
		return todoService.getAllProjectWithTodos().thenApply(i -> {
			return ok(Json.toJson(i));
		});
	}

	/**
	 * Retrieves one of the TodoS by its ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CompletionStage<Result> getTodo(Long id) throws Exception {

		try {
			return todoService.getTodo(id).thenApply(i -> {
				return ok(Json.toJson(i));
			});

		} catch (Exception e) {
			LOG.error("Error retrieving the Todo", e);
			return CompletableFuture.completedFuture(badRequest());

		}
	}

	/**
	 * Creates a new Todo
	 * 
	 * @return
	 * @throws Exception
	 */
	public CompletionStage<Result> createTodo() throws Exception {
		Form<TodoBean> form = formFactory.form(TodoBean.class).bindFromRequest();

		try {

			if (form.hasErrors()) {
				throw new IllegalArgumentException("Form has errors");
			}

			TodoBean bean = form.get();
			Todo todo = new Todo();

			todo.setCompleted(bean.isCompleted());
			todo.setDescription(bean.getDescription());
			todo.setProjectId(Long.parseLong(bean.getProjectId()));
			todo.setDeleted(false);

			if (todo.hasDescription() && todo.hasProjectId()) {

				return todoService.addTodo(todo).thenApply(i -> {
					return ok(Json.toJson(i));
				});
			} else {
				throw new IllegalArgumentException("Form required fields are not included");
			}

		} catch (Exception e) {
			LOG.error("Error creating the Todo", e);
			return CompletableFuture.completedFuture(badRequest());

		}
	}

	/**
	 * Updates a Todo
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CompletionStage<Result> updateTodo(Long id) throws Exception {
		Form<TodoBean> form = formFactory.form(TodoBean.class).bindFromRequest();

		try {

			if (form.hasErrors()) {
				throw new IllegalArgumentException("Form has errors");
			}

			TodoBean bean = form.get();
			Todo todo = new Todo();

			todo.setCompleted(bean.isCompleted());
			todo.setDescription(bean.getDescription());
			todo.setProjectId(Long.parseLong(bean.getProjectId()));
			todo.setId(id);
			todo.setDeleted(false);

			if (todo.hasDescription() && todo.hasProjectId()) {

				return todoService.updateTodo(id, todo).thenApply(i -> {
					return ok(Json.toJson(i));
				});
			} else {
				throw new IllegalArgumentException("Form required fields are not included");
			}

		} catch (IllegalArgumentException e) {
			LOG.error("Error updating the Todo", e);
			return CompletableFuture.completedFuture(badRequest());
		}
	}

	/**
	 * Deletes a Todo
	 * 
	 * @param id
	 * @return
	 */
	public Result deleteTodo(Long id) {
		try {
			todoService.deleteTodo(id);
			return play.mvc.Results.noContent();
		} catch (Exception e) {
			return internalServerError();
		}
	}
}
