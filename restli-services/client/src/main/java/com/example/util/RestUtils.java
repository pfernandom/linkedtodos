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

/**
 * Utilities to generate ParSeq objects
 * 
 * @author pedro.f.marquez.soto
 *
 * @param <T>
 */
public class RestUtils<T> {

	/**
	 * Createe a ParSeqRestClient
	 * 
	 * @param url
	 * @return
	 */
	public static ParSeqRestClient getClient(String url) {

		// Create an HttpClient and wrap it in an abstraction layer
		HttpClientFactory http = new HttpClientFactory();

		final Client r2Client = new TransportClientAdapter(http.getClient(Collections.<String, String>emptyMap()));
		RestClient restClient = new RestClient(r2Client, url);

		ParSeqRestliClientConfigBuilder b = new ParSeqRestliClientConfigBuilder();
		return new ParSeqRestliClientBuilder().setRestClient(restClient).setBatchingSupport(new BatchingSupport())
				.setConfig(b.build()).build();
	}

}
