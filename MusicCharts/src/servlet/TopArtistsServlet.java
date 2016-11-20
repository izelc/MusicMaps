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

import db.Analyzer;

/**
 * Servlet implementation class TopArtistsServlet
 */
@WebServlet("/TopArtistsServlet")
public class TopArtistsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TopArtistsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JsonArray jsonArray = new JsonArray();
		String parameter = request.getParameter("schoolName");
		ArrayList<Map> topArtists = new Analyzer().getTopArtists(parameter);
		for (Map map : topArtists) {
			String string = map.get("a.name").toString();
			jsonArray.add(string);

		}
		response.getWriter().write(jsonArray.toString());
		response.setCharacterEncoding("UTF-8");
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
