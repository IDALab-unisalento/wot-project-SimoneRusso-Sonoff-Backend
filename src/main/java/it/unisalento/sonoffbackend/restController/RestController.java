package it.unisalento.sonoffbackend.restController;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import model.AccessToken;
import model.Credential;
import okio.BufferedSink;

@org.springframework.web.bind.annotation.RestController
public class RestController {
OkHttpClient client = new OkHttpClient();
	
	String host = "http://localhost:8081/";
	String authAddress = "http://192.168.1.100:8180/auth/realms/MyRealm/protocol/openid-connect/token";
	

	@RequestMapping(value = "changeStatusOFF/{clientId}/{token}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> changeStatusOFF(@PathVariable("clientId") String clientId, @PathVariable("token") String token) throws Exception {
		Request request = new Request.Builder().url(host+"changeStatusOFF/"+clientId+"/"+token)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		System.out.println("Something went wrong in changing status to OFF");
		throw new Exception();

	}

	@RequestMapping(value = "changeStatusON/{clientId}{token}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> changeStatusON(@PathVariable("clientId") String clientId, @PathVariable("token") String token) throws Exception {
		Request request = new Request.Builder().url(host+"changeStatusON/"+clientId+"/"+token)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		System.out.println("Something went wrong in changing status to ON");
		throw new Exception();

	}

	  @RequestMapping(value="getStatus/{clientId}/{token}", method = RequestMethod.GET) 
	  public String getStatus(@PathVariable("clientId") String clientId, @PathVariable("token") String token) throws Exception{
		   Request request = new Request.Builder().url(host+"getStatus/"+clientId+"/"+token)
					.get()
				   .build();
		   Response response = client.newCall(request).execute();
		   String status = response.body().string();
		   if(status==null) {
			   System.out.println("Something went wrong while getting status");
			   throw new Exception();
		   }
		   
		   return status;
	  }
	  
	  @RequestMapping(value="getAccessToken", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	  public String getAccessToken(@RequestBody Credential credential) {
		  String accessToken;
		  try {
		  Keycloak instance = Keycloak.getInstance("http://keycloak:8180/auth", "MyRealm", credential.getUsername(), credential.getPassword(), "gateway");                                                                                                      
		  TokenManager tokenmanager = instance.tokenManager();
		  accessToken = tokenmanager.getAccessTokenString();
		  }
		  catch (javax.ws.rs.NotAuthorizedException e) {
			return null;
		}
		return accessToken; 
	  }
}
