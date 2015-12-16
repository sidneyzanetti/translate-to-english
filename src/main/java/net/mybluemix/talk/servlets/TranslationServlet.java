package net.mybluemix.talk.servlets;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;

import com.google.gson.JsonObject;
import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.IdentifiedLanguage;
import com.ibm.watson.developer_cloud.language_translation.v2.model.Translation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;

import net.mybluemix.talk.cloudant.Cloudant;

/**
 * Servlet implementation class TranslationServlet
 */
@WebServlet("/Translation")
public class TranslationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// FIXME
	private final String username = "username";
	private final String password = "pass";

	private LanguageTranslation service;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TranslationServlet() {
		super();
		service = new LanguageTranslation();
		service.setUsernameAndPassword(username, password);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		IdentifiedLanguage identifiedLanguage;
		TranslationResult translationResult;

		String text = request.getParameter("text");
		if (text == null) {
			response.setStatus(HttpStatus.SC_NO_CONTENT);
			return;
		}

		try {
			identifiedLanguage = identifyLanguage(text);
			translationResult = translateToEnglish(identifiedLanguage.getLanguage(), text);
		} catch (Exception e) {
			response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR); // if API down
			return;
		}

		String translationString = translationResultToString(translationResult);
		JsonObject jsonResult = createJsonObject(text, identifiedLanguage, translationString);

		//saveJsonResult(jsonResult); //FIXME

		response.setStatus(HttpStatus.SC_OK);
		response.getWriter().print(jsonResult);
	}

	private IdentifiedLanguage identifyLanguage(String text) {
		List<IdentifiedLanguage> langs = service.identify(text);
		if (langs.isEmpty())
			return null;
		return langs.get(0);
	}

	private TranslationResult translateToEnglish(String identifiedLanguage, String text) {
		return service.translate(text, identifiedLanguage, "en");
	}

	private JsonObject createJsonObject(String textInput, IdentifiedLanguage identifiedLanguage,
			String translationString) {
		JsonObject contentJson = new JsonObject();
		contentJson.addProperty("textInput", textInput);
		contentJson.addProperty("textTranslated", translationString);
		contentJson.addProperty("detectedLanguage", new Locale(identifiedLanguage.getLanguage()).getDisplayLanguage());
		contentJson.addProperty("confidenceDetectedLanguage",
				new DecimalFormat("#.##").format(identifiedLanguage.getConfidence() * 100));
		contentJson.addProperty("datetime", getCurrentDateTime());
		return contentJson;
	}

	private void saveJsonResult(JsonObject resultJson) {
		Cloudant.getInstance().saveTranslationDocumentTranslation(resultJson);
	}

	public String getCurrentDateTime() {
		return DATEFORMAT.format(new Date());
	}

	private String translationResultToString(TranslationResult result) {
		StringJoiner joiner = new StringJoiner(" ");
		for (Translation t : result.getTranslations())
			joiner.add(t.getTranslation());
		return joiner.toString();
	}

	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
}