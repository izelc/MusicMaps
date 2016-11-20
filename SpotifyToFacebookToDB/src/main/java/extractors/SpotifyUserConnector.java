package extractors;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.models.User;

public class SpotifyUserConnector {

	final static Logger logger = Logger.getLogger(SpotifyUserConnector.class);
	Api api = Api.DEFAULT_API;


	public SpotifyUser getUser(String id) throws IOException, WebApiException {
		User user = api.getUser(id).build().get();
		String displayName = user.getDisplayName();	
		String image;
		try {
			image = user.getImages().get(0).getUrl();
		} catch (Exception e) {
			logger.info("No image has been found for user :" + id);
			return null;
		}
		logger.info("User id:" + id + " name:" + displayName + " image:"
				+ image);
		return new SpotifyUser(displayName, editImageUrl(image));
	}

	private String editImageUrl(String imageUrl) {
		return imageUrl.split("_n.jpg")[0].split("200x200/")[1];
	}

}
