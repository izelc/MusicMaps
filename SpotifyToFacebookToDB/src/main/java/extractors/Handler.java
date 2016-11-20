package extractors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wrapper.spotify.exceptions.WebApiException;

import db.Inserter;

public class Handler {

	final static Logger logger = Logger.getLogger(Handler.class);

	public static void main(String[] args) {
		new Handler().createSampleUserGroup();
	}

	public void createSampleUserGroup() {

		SpotifyUserConnector spotifyUserConnector = new SpotifyUserConnector();
		ArrayList<String> spotifyUserIdsFromFile = removeDuplicatesFromFile(getSpotifyUsersFromFile());
		FacebookUserConnector facebookUserConnector = new FacebookUserConnector();

		for (String userId : spotifyUserIdsFromFile) {
			try {
				SpotifyUser spotifyUser = spotifyUserConnector.getUser(userId);
				if (spotifyUser != null) {

					Location location = facebookUserConnector
							.getLocation(spotifyUser);
					if (location != null) {
						logger.info("Adress found with Spotifyuser id:"
								+ userId + " facebokPage:");
						logger.info("addressInfo" + location.toString());

						ArrayList<Track> playlist = new SongFinder()
								.getPlaylist(userId);
						if (!playlist.isEmpty()) {
							new Inserter().insertUser(userId,
									spotifyUser.getDisplayName(),
									location.getUniversity(), playlist);

						} else
							logger.info("Music doesnt found");

					} else {
						logger.info("Location doesnt found");
					}
				}

			} catch (IOException e) {
				logger.error("Spotify api error" + e);
			} catch (WebApiException e) {
				logger.error("Spotify api error" + e);
			}
		}

	}

	public static ArrayList<String> removeDuplicatesFromFile(ArrayList<String> al) {
		Set<String> hs = new HashSet<String>();
		hs.addAll(al);
		al.clear();
		al.addAll(hs);
		return al;

	}

	public static ArrayList<String> getSpotifyUsersFromFile() {
		ArrayList<String> spotifyIds = new ArrayList<String>();
		File file = new File(
				"/home/izel/workspace/SpotifyToFacebookToDB/spotifyUris.txt");
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			try {
				for (String line; (line = bufferedReader.readLine()) != null;) {
					spotifyIds.add(line.split(":user:")[1]);

				}
			} catch (IOException e) {

				logger.error("Cant read line");
			}
		} catch (FileNotFoundException e) {
			logger.error("File not found");
		}
		try {
			bufferedReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spotifyIds;
	}

}
