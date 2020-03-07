package com.restserver.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.Properties;


public class ServerMain {

	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		String fileName = "/server-properties.prop";
		
		InputStream res = ServerMain.class.getResourceAsStream(fileName);
		
		if(res != null) {
			prop.load(res);
			
			if(prop == null || !prop.containsKey("port")) {
				throw new Exception("Properties not set.");
			}
			
			if(prop.get("port") == null ) {
				throw new Exception("Properties file nees a port property.");
			}

			String port = (String)prop.get("port");
			
			if("".equals(port)) {
				throw new Exception("Port property needs to be set.");
			}
			
			ServerSocket ss = new ServerSocket(Integer.valueOf(port));
			Server server = new Server();
			server.setSs(ss);
		}else {
			throw new FileNotFoundException("File " + fileName + " not found in classpath");
		}
		
		
	}

}
