package worker;

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.restserver.worker.MessageContainer;

import static org.junit.Assert.*;

public class MessageContainerTests {
	
	@Test
	public void MessageContainerHeader() {
		MessageContainer container = new MessageContainer();
		Map<String, String> map = new HashMap<String, String>();
		
		container.setHeaders(map);
		
		assertEquals(map.equals(container.getHeaders()), true);
	}
	
	@Test
	public void MessageContainerMessage() {
		byte[] msg = new byte[100];
		MessageContainer container = new MessageContainer();
		
		container.setMessage(msg);
		
		assertEquals(msg.equals(container.getMessage()), true);
	}
}
