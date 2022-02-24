package it.unisalento.sonoffbackend.restController;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.admin.client.token.TokenManager;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.unisalento.sonoffbackend.model.Credential;
import it.unisalento.sonoffbackend.model.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestBody;


@org.springframework.web.bind.annotation.RestController
public class RestController {
	OkHttpClient client = new OkHttpClient();
	
	@Value("${keycloak.resource}")
	private String keycloakClient;

	@Value("${keycloak.auth-server-url}")
	private String keycloakUrl;

	@Value("${keycloak.realm}")
	private String keycloakRealm;

	@Value("${is.keycloak.admin.user}")
	private String keycloakAdminUser;

	@Value("${is.keycloak.admin.password}")
	private String keycloakAdminPassword;
	
	private String keycloakClientSecret = "eLFYzBFFDlJrA9dTmNPnkTwhiipyB8x8";
	
	private Keycloak getAdminKeycloakInstance() {
		return Keycloak.getInstance(
				keycloakUrl,
				"master",
				keycloakAdminUser,
				keycloakAdminPassword,
				"admin-cli");
	}

	private String ip = "10.3.141.130";
	//private String ip = "192.168.1.100";
	private final String host = "http://localhost:8081/";
	private final String authAddress = "http://"+ip+":8180/auth/realms/MyRealm/protocol/openid-connect/userinfo";
	private final String refreshAddress="http://"+ip+":8180/auth/realms/MyRealm/protocol/openid-connect/token";
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "changeStatusOFF/{clientId}", method = RequestMethod.POST)
	public ResponseEntity<User> changeStatusOFF(@PathVariable("clientId") String clientId, @RequestBody User user){
		
		com.squareup.okhttp.MediaType JSON = com.squareup.okhttp.MediaType.parse("application/json; charset=utf-8");
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("username", user.getUsername());
		jsonObj.put("role", user.getRole());
		jsonObj.put("token", user.getToken());
		jsonObj.put("refreshToken", user.getRefreshToken());
		
		com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(JSON, jsonObj.toString());
		
		Request request = new Request.Builder().url(host+"changeStatusOFF/"+clientId)
				.post(body)
				.build();
		
		Response response;
		try {
			response = client.newCall(request).execute();
			if(response.isSuccessful()) {
				JSONParser parser = new JSONParser();  
				String stringBody = response.body().string();
				if(!stringBody.isEmpty()) {
					JSONObject json = (JSONObject) parser.parse(stringBody);  
					user.setToken(json.get("token").toString());
					user.setRefreshToken(json.get("refreshToken").toString());
					return new ResponseEntity<>(user, HttpStatus.valueOf(response.code()));
				}
				return new ResponseEntity<>(new User(), HttpStatus.valueOf(response.code()));

			}
			return new ResponseEntity<>(HttpStatus.valueOf(response.code()));
			
		} 
		catch (IOException e) {
			e.getStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "changeStatusON/{clientId}", method = RequestMethod.POST)
	public ResponseEntity<User> changeStatusON(@PathVariable("clientId") String clientId, @RequestBody User user) throws Exception {
		com.squareup.okhttp.MediaType JSON = com.squareup.okhttp.MediaType.parse("application/json; charset=utf-8");
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("username", user.getUsername());
		jsonObj.put("role", user.getRole());
		jsonObj.put("token", user.getToken());
		jsonObj.put("refreshToken", user.getRefreshToken());
		
		com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(JSON, jsonObj.toString());
		
		Request request = new Request.Builder().url(host+"changeStatusON/"+clientId)
				.post(body)
				.build();
		
		Response response;
		try {
			response = client.newCall(request).execute();
			if(response.isSuccessful()) {
				JSONParser parser = new JSONParser();  
				String stringBody = response.body().string();
				if(!stringBody.isEmpty()) {
					JSONObject json = (JSONObject) parser.parse(stringBody);  
					user.setToken(json.get("token").toString());
					user.setRefreshToken(json.get("refreshToken").toString());
					return new ResponseEntity<>(user, HttpStatus.valueOf(response.code()));
				}
				return new ResponseEntity<>(new User(), HttpStatus.valueOf(response.code()));

			}
			return new ResponseEntity<>(HttpStatus.valueOf(response.code()));
			
		} 
		catch (IOException e) {
			e.getStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="getStatus1/{clientId}", method = RequestMethod.POST) 
	public ResponseEntity<String> getStatus1(@PathVariable("clientId") String clientId, @RequestBody User user){
		com.squareup.okhttp.MediaType JSON = com.squareup.okhttp.MediaType.parse("application/json; charset=utf-8");
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("username", user.getUsername());
		jsonObj.put("role", user.getRole());
		jsonObj.put("token", user.getToken());
		jsonObj.put("refreshToken", user.getRefreshToken());
		
		com.squareup.okhttp.RequestBody body = com.squareup.okhttp.RequestBody.create(JSON, jsonObj.toString());
		
		Request request = new Request.Builder().url(host+"getStatus1/"+clientId)
				.post(body)
				.build();
		
		Response response;
		try {
			response = client.newCall(request).execute();
			if(response.isSuccessful()) {
				JSONParser parser = new JSONParser();
				JSONObject jsonResp = (JSONObject) parser.parse(response.body().string());
				return new ResponseEntity<>(jsonResp.toString(), HttpStatus.valueOf(response.code()));
			}
			return new ResponseEntity<>(HttpStatus.valueOf(response.code()));

		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	@RequestMapping(value="auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getAccessToken(@RequestBody Credential credential) {
		String accessToken = null;
		String refreshToken = null;
		try {
			Keycloak instance = Keycloak.getInstance(keycloakUrl, keycloakRealm, credential.getUsername(), credential.getPassword(), keycloakClient, keycloakClientSecret);                                                                                                      
			TokenManager tokenmanager = instance.tokenManager();
			accessToken = tokenmanager.getAccessTokenString();
			refreshToken = tokenmanager.getAccessToken().getRefreshToken();		
			User user = new User();
			
			String role = getRolesByUser(credential.getUsername());
			if(role!=null) {
				user.setUsername(credential.getUsername());
				user.setToken(accessToken);
				user.setRole(role);
				user.setRefreshToken(refreshToken);
				return new ResponseEntity<User>(user, HttpStatus.OK);
			}
			return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		catch (javax.ws.rs.NotAuthorizedException e) {
			return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
		}
		
	}

	@PostMapping("createUser/{username}/{password}/{userRole}")
	public ResponseEntity<User> createUser(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("userRole") String userRole, @RequestBody User user) {
			try {
				user = checkToken(user);
				CredentialRepresentation credentials = new CredentialRepresentation();
				credentials.setType(CredentialRepresentation.PASSWORD);
				credentials.setValue(password);
				credentials.setTemporary(false);
				UserRepresentation userRepresentation = new UserRepresentation();
				userRepresentation.setUsername(username);
				userRepresentation.setCredentials(Arrays.asList(credentials));
				userRepresentation.setEnabled(true);
				Map<String, List<String>> attributes = new HashMap<>();
				//attributes.put("description", Arrays.asList("A test user"));
				userRepresentation.setAttributes(attributes);
				Keycloak keycloak = getAdminKeycloakInstance();
				javax.ws.rs.core.Response response = keycloak.realm(keycloakRealm).users().create(userRepresentation);
				
				if(HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
					List<UserRepresentation> userList = keycloak.realm(keycloakRealm).users().search(username).stream()
			                .filter(userRep -> userRep.getUsername().equals(username)).collect(Collectors.toList());
					
			        userRepresentation = userList.get(0);
			        assignRoleToUser(userRepresentation.getId(), userRole, username);
					return new ResponseEntity<>(user, HttpStatus.valueOf(response.getStatus()));
				}
				else {
					return new ResponseEntity<>(user, HttpStatus.valueOf(response.getStatus()));
				}
				
			} 
			catch (Exception e) {
				if(e.getMessage().equals("invalid token")) {
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				else {
					e.printStackTrace();
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
			}	
	}
	
	private String getRolesByUser(String username){
		Keycloak keycloak = getAdminKeycloakInstance();
		Optional<UserRepresentation> user = keycloak.realm(keycloakRealm).users().search(username).stream()
				.filter(u -> u.getUsername().equals(username)).findFirst();
		if (user.isPresent()) {
			UserRepresentation userRepresentation = user.get();
			UserResource userResource = keycloak.realm(keycloakRealm).users().get(userRepresentation.getId());
			ClientRepresentation clientRepresentation = keycloak.realm(keycloakRealm).clients().findByClientId(keycloakClient).get(0);
			List<RoleRepresentation> roles = userResource.roles().clientLevel(clientRepresentation.getId()).listAll();
			return roles.get(0).getName();

		} else {
			return null;
		}
	}
	
	private void assignRoleToUser(String userId, String role, String username) throws Exception {
        Keycloak keycloak = getAdminKeycloakInstance();
        UsersResource usersResource = keycloak.realm(keycloakRealm).users();
        UserResource userResource = usersResource.get(userId);

        //getting client
        ClientRepresentation clientRepresentation = keycloak.realm(keycloakRealm)
        		.clients()
        		.findAll()
        		.stream()
        		.filter(client -> 
        			client.getClientId().equals(keycloakClient)
        		)
        		.collect(Collectors.toList())
        		.get(0);
        
        ClientResource clientResource = keycloak.realm(keycloakRealm).clients().get(clientRepresentation.getId());
        
        //getting role
        RoleRepresentation roleRepresentation = clientResource.roles().list().stream().filter(element -> element.getName().equals(role)).collect(Collectors.toList()).get(0);
        
        //assigning to user
        userResource.roles()
        	.clientLevel(clientRepresentation
        			.getId())
        	.add(Collections.singletonList(roleRepresentation));
        
        if(getRolesByUser(username) == null) {
        	throw new Exception("errore durante l'assegnazione del ruolo");
        }
    }
	
	//@PostMapping("check")
	//private ResponseEntity<User> checkToken(User user) throws Exception{
	private User checkToken(User user) throws Exception {
		Request request = new Request.Builder()
			      .url(authAddress)
			      .header("Content-Type", "application/json")
			      .header("Authorization","Bearer "+ user.getToken())
			      .get()
			      .build();
		 Response response;
		 try {
				response = client.newCall(request).execute();
				if(response.isSuccessful()) {
					return new User();
				}
				else {
					return executeRefresh(user);
				}
			} 
		 catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
	}
		
		
		/*Keycloak instance = Keycloak.getInstance(keycloakUrl, keycloakRealm, keycloakClient, user.getToken()); //CAPIRE BENE QUESTA
		TokenManager tokenManager = instance.tokenManager();
		String accessToken = null;
		//String accessToken = tokenManager.getAccessTokenString();*/
		
		
		/*METODO 1 DA TESTARE
		if(accessToken == null) {
			return executeRefresh(user);
		}
		return null;
		*/
		/* METODO 2 DA TESTARE 
		try {
			 accessToken = tokenManager.getAccessTokenString();
			 return new ResponseEntity<>(user, HttpStatus.OK);
		}
		catch (javax.ws.rs.NotAuthorizedException e) { //CAPIRE BENE QUALE ECCEZIONE GESTIRE
			try {
				e.printStackTrace();
				accessToken = tokenManager.refreshToken().getToken();
				user.setToken(accessToken);
				user.setRefreshToken(tokenManager.getAccessToken().getRefreshToken());
				return new ResponseEntity<User>(user, HttpStatus.OK);			}
			catch (java.lang.NullPointerException e1) {
				e1.printStackTrace();
				return new ResponseEntity<User>(HttpStatus.UNAUTHORIZED);
			}
		}*/
	
	private User executeRefresh(User user) throws Exception {
		com.squareup.okhttp.RequestBody requestBody = new FormEncodingBuilder()
	    	     .add("grant_type", "refresh_token")
	    	     .add("refresh_token", user.getRefreshToken())
	    	     .add("client_id", "backend")
	    	     .add("client_secret", "eLFYzBFFDlJrA9dTmNPnkTwhiipyB8x8")
	    	     .build();
	    
	    Request request = new Request.Builder()
	    		.url(refreshAddress)
	    		.post(requestBody)
	            .build();
	    Response response;
	    
	    try {
	    	response = client.newCall(request).execute();
	    	if(response.isSuccessful()) {
	    		JSONParser parser = new JSONParser();  
	    		JSONObject json = (JSONObject) parser.parse(response.body().string());  
	    		user.setToken(json.get("access_token").toString());
	    		user.setRefreshToken(json.get("refresh_token").toString());  
		    	return user;
	    	}
	    	else {
	    		throw new Exception("Invalid token");	  
	    	}
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
