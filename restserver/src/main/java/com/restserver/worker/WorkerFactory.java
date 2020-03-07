package com.restserver.worker;

import java.net.Socket;

public class WorkerFactory {
	public WorkerFactory() {
		
	}
	
	public Thread createWorker(Socket socket) {
		Worker w = new Worker();
		w.setSocket(socket);
		return new Thread(w);
	}
}
