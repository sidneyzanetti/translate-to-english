package net.mybluemix.talk.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.google.gson.JsonObject;

import net.mybluemix.talk.cloudant.Cloudant;

/**
 * Servlet implementation class TranslationHistoryServlet
 */
@WebServlet("/TranslationHistory")
public class TranslationHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Cloudant client = Cloudant.getInstance();

		List<JsonObject> documents = client.getTranslationsDocumentsHistory(5);

		response.setStatus(HttpStatus.SC_OK);
		response.getWriter().print(documents);
	}

}
