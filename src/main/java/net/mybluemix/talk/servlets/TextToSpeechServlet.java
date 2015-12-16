package net.mybluemix.talk.servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

/**
 * Servlet implementation class TextToSpeech
 */
@WebServlet("/TextToSpeech")
public class TextToSpeechServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// FIXME
	private final String username = "username";
	private final String password = "pass";

	private TextToSpeech service;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TextToSpeechServlet() {
		super();
		service = new TextToSpeech();
		service.setUsernameAndPassword(username, password);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String text = request.getParameter("text");
		if (text == null) {
			response.setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
			return;
		}

		try (InputStream in = service.synthesize(text, Voice.EN_LISA, "audio/ogg; codecs=opus")) {
			ServletOutputStream out = response.getOutputStream();
			IOUtils.copy(in, out);
			response.setStatus(HttpStatus.SC_OK);
			out.close();
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_BAD_GATEWAY);
		}
	}
}