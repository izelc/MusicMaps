package servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.graphdb.Result;

import com.google.gson.JsonArray;

import db.GraphDbSingleton;

/**
 * Servlet implementation class SchoolServlet
 */
@WebServlet("/SchoolServlet")
public class SchoolServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SchoolServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JsonArray jsonArray = new JsonArray();

		Result execute = GraphDbSingleton
				.getGraphDB()
				.execute(
						"MATCH (s:User)-[STUDIED_AT]->(u:University) with  count(distinct s )as ct,u Where ct>7 RETURN  u.universityName order by ct desc");
		while (execute.hasNext()) {
			Map<String, Object> next = execute.next();
			String schoolName = next.get("u.universityName").toString();
			jsonArray.add(schoolName);

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
