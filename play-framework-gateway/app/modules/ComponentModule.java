package modules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.example.todo.api.TodoService;
import com.example.todo.impl.TodoServiceRest;
import com.google.inject.AbstractModule;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;

import play.Configuration;
import play.Environment;



public class ComponentModule extends AbstractModule {
	
	private final Configuration configuration;

	public ComponentModule(@SuppressWarnings("unused") Environment environment, Configuration configuration) {
        this.configuration = configuration;
    }
	
	@Override
    protected void configure() {
    	Engine engine = getEngine();
    	TodoServiceRest todoService = new TodoServiceRest(engine, configuration.getString("app.rest.host"));
    	System.out.println(todoService);
    	
        bind(TodoService.class).
                toInstance(todoService);
        bind(Engine.class).
        		toInstance(engine);
    }
    
    public static Engine getEngine() {
		final int numCores = Runtime.getRuntime().availableProcessors();
		final ExecutorService taskScheduler = Executors.newFixedThreadPool(numCores + 1);
		final ScheduledExecutorService timerScheduler = Executors.newSingleThreadScheduledExecutor();

		return new EngineBuilder().setTaskExecutor(taskScheduler).setTimerScheduler(timerScheduler).build();
	}
}