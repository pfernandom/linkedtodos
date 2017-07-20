package main;

import java.io.IOException;

import com.linkedin.restli.server.NettyStandaloneLauncher;

public class Application {
	public static void main(String args[]) throws IOException {
		NettyStandaloneLauncher launcher = new NettyStandaloneLauncher(8080 /* port */,
				"com.example.fortune.impl" /* resource package(s) */
		);
		launcher.start();
	}
}
