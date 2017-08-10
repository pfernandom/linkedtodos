package modules;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.example.todo.api.TodoService;
import com.example.todo.impl.TodoServiceRest;
import com.google.inject.AbstractModule;
import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;

public class ComponentModule extends AbstractModule {
	
    
	@Override
    protected void configure() {
    	Engine engine = getEngine();
    	TodoServiceRest todoService = new TodoServiceRest(engine, "http://localhost:8080/");
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