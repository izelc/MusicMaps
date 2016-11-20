package extractors;
import org.junit.Test;

public class TestSpotifyConnector {

	

	@Test
	public void getUser() throws Exception {
		System.out.println(new SpotifyUserConnector().getUser("11149434144")
				.getDisplayName().replace(" ", "%20"));

	}
	
	
	
}
	