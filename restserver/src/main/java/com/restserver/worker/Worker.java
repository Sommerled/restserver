package com.restserver.worker;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Worker implements Runnable{
	private Socket socket;
	
	public Worker() {
		
	}

	/**
	 * Processes an HTTP request
	 */
	public void run() {
		if(this.socket != null) {
			try {
				InputStream is = this.socket.getInputStream();
				SocketReader socketReader = new SocketReader();
				socketReader.setIs(is);
				MessageContainer messageContainer = socketReader.parseMessage();
				
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
