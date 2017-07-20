package controllers;

import java.util.concurrent.CompletionStage;

import com.example.fortune.RestLiFortunesClient;
import com.example.todo.api.TodoService;
import com.google.inject.Inject;
import com.typesafe.config.Config;

import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller contains an action to handle HTTP requests to the
 * application's home page.
 */
public class HomeController extends Controller {
	@Inject
	HttpExecutionContext ec;
	
	@Inject
	private Config configuration;
	
	@Inject
	TodoService todoService;

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
		return todoService
				.getAllTodos()
				.thenApply(i -> {
					return created(Json.toJson(i));
							});
	}
	
	
	public CompletionStage<Result> projects(Long id) throws Exception {
		System.out.println(configuration.getString("app.rest.host"));
		return todoService
				.getProjectWithTodos(id)
				.thenApply(i -> {
					return created(Json.toJson(i));
							});
	}

}
