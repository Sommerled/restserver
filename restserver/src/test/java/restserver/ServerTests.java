package restserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

import com.restserver.server.Server;
import com.restserver.worker.WorkerFactory;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ServerTests {
	
	@Test
	public void StartServerNullAccept() throws Exception {
		ServerSocket mockServerSocket = mock(ServerSocket.class);
		
		try {
		    when(mockServerSocket.accept()).thenReturn(null);
		} catch (IOException e) {
		    fail(e.getMessage());
		}
		
		Server server = new Server();
		server.setSs(mockServerSocket);
		
		server.startServer();
	}
	
	@Test
	public void StartServerNullSocket() {
		Server server = new Server();
		server.setSs(null);
		
		try {
			server.startServer();
			fail("Should have failed");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "ServerSocket not set.");
		}
	}
	
	@Test
	public void StartServerClosedSocket() {
		ServerSocket mockServerSocket = mock(ServerSocket.class);
		
		when(mockServerSocket.isClosed()).thenReturn(true);
		
		Server server = new Server();
		server.setSs(mockServerSocket);
		
		try {
			server.startServer();
			fail("Should have failed");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "ServerSocket has been closed.");
		}
	}
	
	@Test
	public void StartServerAccept() {
		ServerSocket mockServerSocket = mock(ServerSocket.class);
		Socket mockTestClientSocket = mock(Socket.class);
		
		try {
		    when(mockServerSocket.accept()).thenReturn(mockTestClientSocket);
		} catch (IOException e) {
		    fail(e.getMessage());
		}

		Server server = new Server();
		server.setSs(mockServerSocket);
		WorkerFactory workerFactory = mock(WorkerFactory.class);
		Thread thread = mock(Thread.class);
		
		when(workerFactory.createWorker(mockTestClientSocket)).thenReturn(thread);
		doThrow(new IllegalThreadStateException ("Success")).when(thread).start();
		
		server.setWorkerFactory(workerFactory);
		
		try {
			server.startServer();
			fail("Should have failed");
		} catch (IllegalThreadStateException e) {
			assertEquals(e.getMessage(), "Success");
		} catch(Exception e2) {
			fail("Wrong exception");
		}
	}
}
