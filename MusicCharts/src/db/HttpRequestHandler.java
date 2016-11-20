package db;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class HttpRequestHandler {

	private HttpClient client;

	public HttpRequestHandler() {
		client = new HttpClient(new SslContextFactory(true));
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JsonObject getPage(String uri) throws JsonSyntaxException {
		String result = null;
		try {
			Request newRequest = client.newRequest(uri);
			ContentResponse send = newRequest.send();
			result = send.getContentAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Gson().fromJson(result, JsonObject.class);
	}

}