package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import db.Analyzer;

/**
 * Servlet implementation class GenresServlet
 */
@WebServlet("/GenresServlet")
public class GenresServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GenresServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String parameter = request.getParameter("schoolName");
		JsonArray jsonArray = new JsonArray();
		ArrayList<Map> topGenres = new Analyzer().getTopGenres(parameter);
		for (Map map : topGenres) {
			String genre = map.get("g.genre").toString();
			String count = map.get("ct").toString();
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("label", genre);
			jsonObject.addProperty("value", Integer.parseInt(count));
			jsonObject.addProperty("color", getRandomColor());
			jsonArray.add(jsonObject);
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

	private String getRandomColor() {
		Random r = new Random();
		final char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char[] s = new char[7];
		int n = r.nextInt(0x1000000);

		s[0] = '#';
		for (int i = 1; i < 7; i++) {
			s[i] = hex[n & 0xf];
			n >>= 4;
		}
		return new String(s);
	}

}
