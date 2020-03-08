package worker;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import com.restserver.worker.MessageContainer;
import com.restserver.worker.SocketReader;
import static org.junit.Assert.*;

public class SocketReaderTests {
	@Test
	public void SocketReaderSocket() {
		Socket socket = mock(Socket.class);
		try {
	        PipedOutputStream oStream = new PipedOutputStream();
	        when(socket.getOutputStream()).thenReturn(oStream);

	        PipedInputStream iStream = new PipedInputStream(oStream);
	        when(socket.getInputStream()).thenReturn(iStream);

	        when(socket.isClosed()).thenReturn(false);
	    } catch (IOException e) {
	        fail(e.getMessage());
	    }
		
		SocketReader socketReader = new SocketReader();
		
		try {
			socketReader.setIs(socket.getInputStream());
			assertEquals(socketReader.getIs().equals(socket.getInputStream()), true);
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void SocketReaderInputStreamNull() {
		SocketReader socketReader = new SocketReader();
		socketReader.setIs(null);
		
		try {
			socketReader.parseMessage();
			fail("Exception not fired");
		}catch(Exception e) {
			assertEquals(e.getMessage(), "InputStream is not set");
		}
	}
	
	@Test
	public void SocketReaderHeaderNoMsg() {
		String msg = "GET / HTTP/1.1\r\n"
				     + "Host: localhost:8080\r\n"
				     + "Connection: keep-alive\r\n"
				     + "Cache-Control: max-age=0\r\n"
				     + "Upgrade-Insecure-Requests: 1\r\n"
				     + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36\r\n"
				     + "Sec-Fetch-Dest: document\r\n"
				     + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n"
				     + "Sec-Fetch-Site: none\r\n"
				     + "Sec-Fetch-Mode: navigate\r\n"
				     + "Sec-Fetch-User: ?1\r\n"
				     + "Accept-Encoding: gzip, deflate, br\r\n"
				     + "Accept-Language: en-US,en;q=0.9\r\n\r\n";
		
		InputStream is = new ByteArrayInputStream(msg.getBytes());
		SocketReader sr = new SocketReader();
		sr.setIs(is);
		
		try {
			MessageContainer mc = sr.parseMessage();
			
			assertEquals(mc.getHeaders().get("Http Method"), "GET");
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void SocketReaderHeaderMsg() {
		String postMsg = "Hello World!";
		String msg = "POST / HTTP/1.1\r\n"
				     + "Host: localhost:8080\r\n"
				     + "Connection: keep-alive\r\n"
				     + "Cache-Control: max-age=0\r\n"
				     + "Upgrade-Insecure-Requests: 1\r\n"
				     + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36\r\n"
				     + "Sec-Fetch-Dest: document\r\n"
				     + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\r\n"
				     + "Sec-Fetch-Site: none\r\n"
				     + "Sec-Fetch-Mode: navigate\r\n"
				     + "Sec-Fetch-User: ?1\r\n"
				     + "Content-Length: " + postMsg.length() + "\r\n"
				     + "Accept-Encoding: gzip, deflate, br\r\n"
				     + "Accept-Language: en-US,en;q=0.9\r\n\r\n"
				     + postMsg;
		
		InputStream is = new ByteArrayInputStream(msg.getBytes());
		SocketReader sr = new SocketReader();
		sr.setIs(is);
		
		try {
			MessageContainer mc = sr.parseMessage();
			assertEquals(mc.getHeaders().get("Http Method"), "POST");
			assertEquals(Integer.valueOf(mc.getHeaders().get("Content-Length")).intValue(), postMsg.length());
			String msg1 = new String(mc.getMessage());
			String msg2 = new String(postMsg.getBytes());
			assertEquals(msg1.equals(msg2), true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
}
