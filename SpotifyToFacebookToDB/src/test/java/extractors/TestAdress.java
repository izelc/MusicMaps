package extractors;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestAdress {
	@Test
	public void testName() throws Exception {
		assertEquals("ozyegin university", new Location("").formatUniversity("Özyeğin Üniversitesi"));
		assertEquals("kocaeli university", new Location("").formatUniversity("Kocaeli üniversitesi tıp fakültesi"));
		
		
	}
	


}
