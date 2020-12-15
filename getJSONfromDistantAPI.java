package automationPoleEmploi;

import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class getJSONfromDistantAPI {
	public static JsonNode grabJSON(String motcle) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create("grant_type=client_credentials&client_id=PAR_nuagesdemotsdynamiques_ade2949a6d4a0541b164994bcf27d418a80445c8066f02133bc8d0dbe86f9bb2&client_secret=311ca44a9ad43edaccff5e57a30294e236511ffd4c039794d990517c10198d4e&scope=api_offresdemploiv2 application_PAR_nuagesdemotsdynamiques_ade2949a6d4a0541b164994bcf27d418a80445c8066f02133bc8d0dbe86f9bb2 o2dsoffre", mediaType);

		Request request = new Request.Builder()
			.url("https://entreprise.pole-emploi.fr/connexion/oauth2/access_token?realm=%2Fpartenaire")
			.method("POST", body)
			.addHeader("Content-Type", "application/x-www-form-urlencoded")
			.build();
		Response response = client.newCall(request).execute();

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.body().string());
		
		String token = jsonNode.get("access_token").asText();
		
		OkHttpClient clientGET = new OkHttpClient().newBuilder()
				  .build();
				Request requestGET = new Request.Builder()
				  .url("https://api.emploi-store.fr/partenaire/offresdemploi/v2/offres/search?motsCles="+motcle+"&commune=51069,76322,46083,12172,28117&origineOffre=2")
				  .method("GET", null)
				  .addHeader("Content-Type", "application/json")
				  .addHeader("Accept", "application/json")
				  .addHeader("Authorization", "Bearer "+token)
				  .build();
				Response responseGET = clientGET.newCall(requestGET).execute();

				ObjectMapper mapJSON = new ObjectMapper();				
				JsonNode JSONtree = mapJSON.readTree(responseGET.body().string());
				
				return JSONtree;
	}
}