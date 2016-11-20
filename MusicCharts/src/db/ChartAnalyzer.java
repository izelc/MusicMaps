package db;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.wrapper.spotify.Api;
import com.wrapper.spotify.methods.authentication.ClientCredentialsGrantRequest;
import com.wrapper.spotify.models.ClientCredentials;

public class ChartAnalyzer {

	String spotifyAccessToken;
	private final Logger logger = Logger.getLogger(ChartAnalyzer.class);

	private void authorize() {

		final String clientId = "06e9f3ff7da7472d9e4f46c8703e863d";
		final String clientSecret = "7afa0f3f48a24770a7a6c13bf99abfa5";

		final Api api = Api.builder().clientId(clientId)
				.clientSecret(clientSecret).build();

		final ClientCredentialsGrantRequest request = api
				.clientCredentialsGrant().build();

		final SettableFuture<ClientCredentials> responseFuture = request
				.getAsync();

		Futures.addCallback(responseFuture,
				new FutureCallback<ClientCredentials>() {

					public void onSuccess(ClientCredentials clientCredentials) {

						String accessToken = clientCredentials.getAccessToken();
						spotifyAccessToken = accessToken;
					}

					public void onFailure(Throwable throwable) {

					}
				});

	}

	public AudioFeatures getAudioFeatures(String ids) {
		authorize();
		String link = "https://api.spotify.com/v1/audio-features?ids=" + ids
				+ "&access_token=" + spotifyAccessToken;
		System.out.println(link);
		JsonObject page = new HttpRequestHandler().getPage(link);
		JsonArray audioFeatures = page.get("audio_features").getAsJsonArray();

		int i = 0;
		double sumEnergy = 0;
		double sumDanceability = 0;
		double sumLoudness = 0;
		double sumValence = 0;
		for (JsonElement audioFeature : audioFeatures) {
			i++;

			JsonElement energy = audioFeature.getAsJsonObject().get("energy");
			sumEnergy += energy.getAsDouble();

			JsonElement danceability = audioFeature.getAsJsonObject().get(
					"danceability");
			sumDanceability += danceability.getAsDouble();

			JsonElement loudness = audioFeature.getAsJsonObject().get(
					"loudness");
			sumLoudness += loudness.getAsDouble();

			JsonElement valence = audioFeature.getAsJsonObject().get("valence");
			sumValence += valence.getAsDouble();
		}

		AudioFeatures analyze = new AudioFeatures(sumDanceability / i,
				sumValence / i, sumEnergy / i, sumLoudness / i);
		logger.info("Analyzing process successfully finished: "
				+ analyze.toString());
		return analyze;

	}

}
