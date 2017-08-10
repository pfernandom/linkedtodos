package com.example.util;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.linkedin.parseq.Engine;
import com.linkedin.parseq.EngineBuilder;
import com.linkedin.parseq.Task;
import com.linkedin.parseq.batching.BatchingSupport;
import com.linkedin.r2.transport.common.Client;
import com.linkedin.r2.transport.common.bridge.client.TransportClientAdapter;
import com.linkedin.r2.transport.http.client.HttpClientFactory;
import com.linkedin.restli.client.ParSeqRestClient;
import com.linkedin.restli.client.ParSeqRestliClientBuilder;
import com.linkedin.restli.client.ParSeqRestliClientConfigBuilder;
import com.linkedin.restli.client.Response;
import com.linkedin.restli.client.RestClient;

public class RestUtils<T> {

	public CompletableFuture<T> toFuture(Task<Response<T>> task){
		CompletableFuture<T> completableFuture = new CompletableFuture<>();
		task.map((res) -> res.getEntity())
		.andThen("return", (res)->{
			completableFuture.complete(res);
		});
		return completableFuture;
	}
	
	public CompletableFuture<List<T>> toFutureList(Task<List<T>> task){
		CompletableFuture<List<T>> completableFuture = new CompletableFuture<>();
		task.withTimeout(3000, TimeUnit.MILLISECONDS)
		.andThen("print",list -> {list.stream().forEach(System.out::print);});
		task.andThen("return", (res)->{
			System.out.println("Resolving "+res);
			completableFuture.complete(res);
		});
		return completableFuture;
	}
	
	public static ParSeqRestClient getClient(String url) {

		// Create an HttpClient and wrap it in an abstraction layer
		HttpClientFactory http = new HttpClientFactory();

		final Client r2Client = new TransportClientAdapter(http.getClient(Collections.<String, String> emptyMap()));
		// Create a RestClient to talk to localhost:8080
		RestClient restClient = new RestClient(r2Client, url);

		ParSeqRestliClientConfigBuilder b = new ParSeqRestliClientConfigBuilder();
		return new ParSeqRestliClientBuilder().setRestClient(restClient).setBatchingSupport(new BatchingSupport())
				.setConfig(b.build()).build();
	}
	
	 public static Engine getEngine() {
			final int numCores = Runtime.getRuntime().availableProcessors();
			final ExecutorService taskScheduler = Executors.newFixedThreadPool(numCores + 1);
			final ScheduledExecutorService timerScheduler = Executors.newSingleThreadScheduledExecutor();

			return new EngineBuilder().setTaskExecutor(taskScheduler).setTimerScheduler(timerScheduler).build();
		}
}
