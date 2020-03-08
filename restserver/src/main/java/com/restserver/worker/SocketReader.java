package com.restserver.worker;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketReader {
	private InputStream is;
	private Map<String,String>headers;
	
	public SocketReader() {
		
	}
	
	/**
	 * Parses the message coming in on the <InputStream>
	 * into the individual header values and the 
	 * message to be processed and then returns it all
	 * in a <MessageContainer>
	 * 
	 * @return
	 * @throws Exception if the <InputStream> is not set
	 */
	public MessageContainer parseMessage() throws Exception {
		MessageContainer messageContainer = new MessageContainer();
		this.headers = new HashMap<String,String>();
		
		if(this.is == null) {
			throw new Exception("InputStream is not set");
		}
		
		byte[] bArr = new byte[1024];
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		boolean endOfLine = false;
		boolean readingMsg = false;
		int len = -2;
		int msgLen = 0;
		int i = 0;
		
		/*
		 * len == 0 - no bytes read
		 * len == -1 - end of file
		 */
		while(len != 0 || len != -1) {
			if(i >= len) {
				len = this.is.read(bArr);
				if(len <= 0) {
					break;
				}
			}
			
			if(!readingMsg) {
				for(; i < len; i++) {
					if(bArr[i] == 13 && !readingMsg) {
						if(i + 1 < len && bArr[i + 1] == 10 && !endOfLine) {
							i++;
							String headerLine = new String(bs.toByteArray());
							System.out.println(headerLine);
							parseHeaderLine(headerLine);
							bs.reset();
							endOfLine = true;
						}else if(i + 1 < len && bArr[i + 1] == 10 && endOfLine) {
							i++;
							i++;
							readingMsg = true;
							break;
						}
					} else {
						bs.write(bArr[i]);
						endOfLine = false;
					}
				}
			}else if(readingMsg) {
				if(headers.containsKey("Content-Length")) {
					if(msgLen == 0 && Integer.valueOf(headers.get("Content-Length")) > 0) {
						msgLen = Integer.valueOf(headers.get("Content-Length"));
					}
					
					for(; i < len; i++) {
						bs.write(bArr[i]);
						msgLen--;
					}
					
					if(msgLen == 0) {
						break;
					}
				}else {
					break;
				}
			}
			
		}
		
		messageContainer.setHeaders(headers);
		
		if(bs.size() > 0) {
			messageContainer.setMessage(bs.toByteArray());
			
			System.out.println(new String(messageContainer.getMessage()));
		}
		
		System.out.println("\n");
		return messageContainer;
	}
	
	/**
	 * Parses an HTTP header line into its
	 * various elements.
	 * 
	 * @param line
	 */
	private void parseHeaderLine(String line) {
		 Pattern p = Pattern.compile("(GET|POST|PUT|DELETE|PATCH)\\s+(.+)\\s+(HTTP\\/[0-9]+\\.[0-9]+)");
		 Matcher m = p.matcher(line);
		 
		 if(m.find()) {
			 String httpMethod = m.group(1);
			 String service = m.group(2);
			 String httpVersion = m.group(3);
			 this.headers.put("Http Method", httpMethod);
			 this.headers.put("Service", service);
			 this.headers.put("Method", httpVersion);
		 }else {
			 String[] entry = line.split(": ");
			 if(entry.length == 2) {
				 this.headers.put(entry[0], entry[1]);
			 }
		 }
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

}
