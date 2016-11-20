package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import db.Analyzer;

/**
 * Servlet implementation class LanguageRatioServlet
 */
@WebServlet("/LanguageRatioServlet")
public class LanguageRatioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LanguageRatioServlet() {
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
		int turkishArtistsRate = new Analyzer()
				.getTurkishArtistsRate(schoolName);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", turkishArtistsRate);
		response.getWriter().write(jsonObject.toString());
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
