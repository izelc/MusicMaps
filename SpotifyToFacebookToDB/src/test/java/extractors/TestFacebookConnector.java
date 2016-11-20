package extractors;
import java.util.ArrayList;

import org.junit.Test;



public class TestFacebookConnector {
	

	 @Test
	 public void findFacebookPage() throws Exception {
	FacebookUserConnector facebookUserConnector = new FacebookUserConnector();
	 SpotifyUser user = new SpotifyUserConnector().getUser("11127004733");
	 System.out.println(user.getDisplayName());
	 facebookUserConnector.getLocation(user);
	 }

	

}
