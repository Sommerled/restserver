package com.restserver.worker;

import java.io.IOException;
import java.net.Socket;

public class Worker implements Runnable{
	private Socket socket;
	
	public Worker() {
		
	}

	public void run() {
		if(this.socket != null) {
			try {
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				if(!this.socket.isClosed()) {
					try {
						this.socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
