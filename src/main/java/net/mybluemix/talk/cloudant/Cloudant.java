package net.mybluemix.talk.cloudant;

import java.util.List;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.FindByIndexOptions;
import com.cloudant.client.api.model.IndexField;
import com.cloudant.client.api.model.IndexField.SortOrder;
import com.cloudant.client.api.model.Response;
import com.google.gson.JsonObject;

public class Cloudant {
	private static Cloudant INSTANCE = null;

	private final String databaseTranslations = "translations";

	// FIXME
	private final String account = "account";
	private final String username = "username";
	private final String password = "pass";

	CloudantClient client;

	public Cloudant() {
		client = ClientBuilder.account(account).username(username).password(password).build();
	}

	public static Cloudant getInstance() {
		if (INSTANCE == null)
			synchronized (Cloudant.class) {
				if (INSTANCE == null)
					INSTANCE = new Cloudant();
			}
		return INSTANCE;
	}

	private Database getDatabase(String database) {
		return client.database(databaseTranslations, false);
	}

	/**
	 * @return true if the document was saved or false otherwise
	 */
	public boolean saveDocument(JsonObject json, String database) {
		Database db = getDatabase(database);
		Response couchDbResponse = db.save(json);

		return (couchDbResponse.getStatusCode() != 200);
	}

	/**
	 * @return true if the document was saved or false otherwise
	 */
	public boolean saveTranslationDocumentTranslation(JsonObject json) {
		return saveDocument(json, databaseTranslations);
	}

	public List<JsonObject> getTranslationsDocumentsHistory(int limit) {
		Database db = getDatabase(databaseTranslations);

		String selector = "\"selector\": { \"datetime\": { \"$gt\": 0 } }";
		FindByIndexOptions options = new FindByIndexOptions().limit(limit)
				.sort(new IndexField("datetime", SortOrder.desc));

		List<JsonObject> documents = db.findByIndex(selector, JsonObject.class, options);
		return documents;
	}
}
