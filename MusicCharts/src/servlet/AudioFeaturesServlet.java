package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import db.Analyzer;
import db.AudioFeatures;

/**
 * Servlet implementation class AudioFeaturesServlet
 */
@WebServlet("/AudioFeaturesServlet")
public class AudioFeaturesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AudioFeaturesServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JsonObject jsonObject = new JsonObject();
		String schoolName = request.getParameter("schoolName");
		AudioFeatures audioFeatures = new Analyzer()
				.getTopTracksAnalyze(schoolName);
		int danceability = (int) (audioFeatures.getDanceability() * 100);
		int valence = (int) (audioFeatures.getValence() * 100);
		int energy = (int) (audioFeatures.getEnergy() * 100);

		jsonObject.addProperty("valence", valence);
		jsonObject.addProperty("danceability", danceability);
		jsonObject.addProperty("energy", energy);

		
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append(jsonObject.toString());
		response.getWriter().flush();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
