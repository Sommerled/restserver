package com.restserver.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.restserver.worker.Worker;
import com.restserver.worker.WorkerFactory;

public class Server {
	private ServerSocket ss;
	private WorkerFactory workerFactory;
	
	public Server() {
		
	}
	
	/**
	 * Starts the server listening on the <SocketServer>
	 * for requests and then hands a new request over 
	 * to a worker object.
	 * 
	 * @throws Exception if either the <SocketServer> is 
	 * 		   not set or is closed.
	 */
	public void startServer() throws Exception {
		
		if(ss == null) {
			throw new Exception("ServerSocket not set.");
		}
		
		if(ss.isClosed()) {
			throw new Exception("ServerSocket has been closed.");
		}
		
		Socket socket = null;
		while((socket = ss.accept()) != null) {
			Thread t = workerFactory.createWorker(socket);
			t.start();
		}
	}

	public void setSs(ServerSocket ss) {
		this.ss = ss;
	}

	public void setWorkerFactory(WorkerFactory workerFactory) {
		this.workerFactory = workerFactory;
	}
}
