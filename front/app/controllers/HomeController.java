package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.fortune.RestLiFortunesClient;
import com.example.todo.Todo;
import com.example.todo.api.TodoService;
import com.google.inject.Inject;
import com.typesafe.config.Config;

import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {

	final static Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@Inject
	private HttpExecutionContext ec;

	@Inject
	private Config configuration;

	@Inject
	private TodoService todoService;

	@Inject
	private FormFactory formFactory;

	/**
	 * An action that renders an HTML page with a welcome message. The
	 * configuration in the <code>routes</code> file means that this method will
	 * be called when the application receives a <code>GET</code> request with a
	 * path of <code>/</code>.
	 */
	public Result index() {
		return ok(views.html.index.render());
	}

	public CompletionStage<Result> test() throws Exception {
		RestLiFortunesClient client = new RestLiFortunesClient();

		return client.getFortune().thenApply(i -> ok("Got result: " + i));
	}

	public CompletionStage<Result> todos() throws Exception {
		return todoService.getAllTodos().thenApply(i -> {
			return ok(Json.toJson(i));
		});
	}

	public CompletionStage<Result> projects(Long id) throws Exception {
		System.out.println(configuration.getString("app.rest.host"));
		return todoService.getProjectWithTodos(id).thenApply(i -> {
			return ok(Json.toJson(i));
		});
	}

	public CompletionStage<Result> allProjects() throws Exception {
		System.out.println(configuration.getString("app.rest.host"));
		return todoService.getAllProjectWithTodos().thenApply(i -> {
			return ok(Json.toJson(i));
		});
	}

	public CompletionStage<Result> getTodo(Long id) throws Exception {

		try {
			return todoService.getTodo(id).thenApply(i -> {
				return ok(Json.toJson(i));
			});

		} catch (Exception e) {
			e.printStackTrace();
			return CompletableFuture.completedFuture(badRequest());

		}
	}

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

			LOG.info("New todo description: " + todo.getDescription());

			if (todo.hasDescription() && todo.hasProjectId()) {

				return todoService.addTodo(todo).thenApply(i -> {
					System.out.println("Result " + todo);
					return ok(Json.toJson(i));
				}).exceptionally(th -> {
					// will be executed when there is an exception.
					System.out.println(th);
					return internalServerError();
				});
			} else {
				throw new IllegalArgumentException("Form required fields are not included");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return CompletableFuture.completedFuture(badRequest());

		}
	}

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

			System.out.println(todo.getDescription());

			if (todo.hasDescription() && todo.hasProjectId()) {

				return todoService.updateTodo(id, todo).thenApply(i -> {
					System.out.println("Result " + todo);
					return ok(Json.toJson(i));
				}).exceptionally(th -> {
					// will be executed when there is an exception.
					System.out.println(th);
					return internalServerError();
				});
			} else {
				throw new IllegalArgumentException("Form required fields are not included");
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return CompletableFuture.completedFuture(badRequest());
		}
	}

	public Result deleteTodo(Long id) {
		try {
			todoService.deleteTodo(id);
			return play.mvc.Results.noContent();
		} catch (Exception e) {
			return internalServerError();
		}
	}
}
