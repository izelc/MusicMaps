package extractors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.exceptions.BadRequestException;
import com.wrapper.spotify.exceptions.WebApiException;
import com.wrapper.spotify.methods.UserPlaylistsRequest;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;
import com.wrapper.spotify.models.Page;
import com.wrapper.spotify.models.PlaylistTrack;
import com.wrapper.spotify.models.SimpleArtist;
import com.wrapper.spotify.models.SimplePlaylist;

public class SongFinder {

	private String AccessToken = "";

	final static Logger logger = Logger.getLogger(SongFinder.class);
	Api api;

	ArrayList<String> echonestApiKeys;

	public SongFinder() {
		this.api = Api.DEFAULT_API;

	}

	public void authorize() {

		final String clientId = "06e9f3ff7da7472d9e4f46c8703e863d";
		final String clientSecret = "7afa0f3f48a24770a7a6c13bf99abfa5";

		final Api api = Api.builder().clientId(clientId)
				.clientSecret(clientSecret).build();

		/* Create a request object. */
		final ClientCredentialsGrantRequest request = api
				.clientCredentialsGrant().build();

		/*
		 * Use the request object to make the request, either asynchronously
		 * (getAsync) or synchronously (get)
		 */
		final SettableFuture<ClientCredentials> responseFuture = request
				.getAsync();

		/* Add callbacks to handle success and failure */
		Futures.addCallback(responseFuture,
				new FutureCallback<ClientCredentials>() {
					public void onSuccess(ClientCredentials clientCredentials) {

						/* The tokens were retrieved successfully! */

						logger.info("Successfully retrieved an access token! "
								+ clientCredentials.getAccessToken());
						logger.info("The access token expires in "
								+ clientCredentials.getExpiresIn() + " seconds");
						AccessToken = clientCredentials.getAccessToken();

					}

					public void onFailure(Throwable throwable) {
						/*
						 * An error occurred while getting the access token.
						 * This is probably caused by the client id or client
						 * secret is invalid.
						 */
					}
				});
	}

	public ArrayList<Track> getPlaylist(String userId) throws IOException,
			WebApiException {

		ArrayList<Track> tracklist = new ArrayList<Track>();
		Page<SimplePlaylist> playlistsPage;
		try {
			api = Api.builder().accessToken(AccessToken).build();
			final UserPlaylistsRequest request = api
					.getPlaylistsForUser(userId).build();

			playlistsPage = request.get();
		} catch (BadRequestException e) {
			authorize();
			api = Api.builder().accessToken(AccessToken).build();
			final UserPlaylistsRequest request = api
					.getPlaylistsForUser(userId).build();

			playlistsPage = request.get();
		}

		List<SimplePlaylist> simplePlaylists = playlistsPage.getItems();

		for (SimplePlaylist simplePlaylist : simplePlaylists) {
			try {
				String id = simplePlaylist.getOwner().getId();
				if (id.equals(userId)) {

					List<PlaylistTrack> playlistTracks = api
							.getPlaylistTracks(id, simplePlaylist.getId())
							.build().get().getItems();

					for (PlaylistTrack playlistTrack : playlistTracks) {

						com.wrapper.spotify.models.Track track = playlistTrack
								.getTrack();

						SimpleArtist artist = track.getArtists().get(0);
						String artistId = artist.getId();

						String genre = findGenre(artist, artistId);

						tracklist.add(new Track(artist.getName(), artistId,
								track.getName(), track.getId(), genre));
						logger.info("Saved track:  " + artist.getName() + " - "
								+ track.getName() + " - " + genre);

					}

				}
			} catch (Exception e) {
				logger.error("Error occured while trying to get user playlists. userId:"+userId);
				e.printStackTrace();
			}
		}
		return tracklist;

	}

	public String findGenre(SimpleArtist artist, String artistId)
			throws IOException, WebApiException {
		List<String> genres = api.getArtist(artistId).build().get().getGenres();

		String genre = "";
		if (!genres.isEmpty()) {
			genre = genres.get(0);
		}

			return genre;
	}

}
