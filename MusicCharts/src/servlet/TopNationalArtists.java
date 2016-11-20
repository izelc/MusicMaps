package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import db.Analyzer;

/**
 * Servlet implementation class TopNationalArtists
 */
@WebServlet("/TopNationalArtists")
public class TopNationalArtists extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TopNationalArtists() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String schoolName = request.getParameter("schoolName");
		JsonArray jsonArray = new JsonArray();
		ArrayList<Map> topTurkishArtist = new Analyzer()
				.getTopTurkishArtist(schoolName);
		for (Map map : topTurkishArtist) {
			String artist = map.get("a.name").toString();
			jsonArray.add(artist);
		}
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonArray.toString());
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
