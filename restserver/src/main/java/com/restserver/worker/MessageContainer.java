package com.restserver.worker;

import java.util.Map;

public class MessageContainer {
	private Map<String,String>headers;
	private byte[] message;
	
	public MessageContainer() {
		
	}

	public Map<String,String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String,String> headers) {
		this.headers = headers;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
	
}
