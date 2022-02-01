package it.unisalento.sonoffbackend.restController;

/*
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Value;
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
 */
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.unisalento.sonoffbackend.model.Credential;
import it.unisalento.sonoffbackend.model.User;

import java.io.IOException;
//import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

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

	private Keycloak getKeycloakInstance() {
		return Keycloak.getInstance(
				keycloakUrl,
				"master",
				keycloakAdminUser,
				keycloakAdminPassword,
				"admin-cli");
	}



	String host = "http://localhost:8081/";
	String authAddress = "http://192.168.1.100:8180/auth/realms/master/protocol/openid-connect/token";


	@RequestMapping(value = "changeStatusOFF/{clientId}/{token}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> changeStatusOFF(@PathVariable("clientId") String clientId, @PathVariable("token") String token) throws Exception {
		Request request = new Request.Builder().url(host+"changeStatusOFF/"+clientId+"/"+token)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		return new ResponseEntity<>(HttpStatus.valueOf(response.code()));

	}

	@RequestMapping(value = "changeStatusON/{clientId}{token}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> changeStatusON(@PathVariable("clientId") String clientId, @PathVariable("token") String token) throws Exception {
		Request request = new Request.Builder().url(host+"changeStatusON/"+clientId+"/"+token)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		return new ResponseEntity<>(HttpStatus.valueOf(response.code()));
	}

	@RequestMapping(value="getStatus/{clientId}/{token}", method = RequestMethod.GET) 
	public ResponseEntity<String> getStatus(@PathVariable("clientId") String clientId, @PathVariable("token") String token) throws Exception{
		Request request = new Request.Builder().url(host+"getStatus/"+clientId+"/"+token)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		String status = response.body().string();
		if(status==null) {
			System.out.println("Something went wrong while getting status");
			return new ResponseEntity<String>(HttpStatus.valueOf(response.code()));
		}
		return new ResponseEntity<String>(status, HttpStatus.valueOf(response.code()));
	}

	@RequestMapping(value="auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getAccessToken(@RequestBody Credential credential) {
		String accessToken = null;
		try {
			Keycloak instance = Keycloak.getInstance("http://keycloak:8180/auth", "MyRealm", credential.getUsername(), credential.getPassword(), "backend", "eLFYzBFFDlJrA9dTmNPnkTwhiipyB8x8");                                                                                                      
			TokenManager tokenmanager = instance.tokenManager();
			accessToken = tokenmanager.getAccessTokenString();
			if(accessToken == null) {
				return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
			}
		}
		catch (javax.ws.rs.NotAuthorizedException e) {
		}
		User user = new User();
		
		String role = getRolesByUser(credential.getUsername());
		if(role!=null) {
			user.setUsername(credential.getUsername());
			user.setToken(accessToken);
			user.setRole(role);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR); 
	}

	public String getRolesByUser(String username){ 
		Keycloak keycloak = getKeycloakInstance();
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

	@PostMapping("/createUser/{username}/{password}/{userRole}")
	public ResponseEntity<String> createUser(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("userRole") String userRole, @RequestBody User user) {
		
		if(checkToken(user.getToken()) && user.getRole().equals("admin")) { //checking token and role
			if (username.isBlank() || password.isBlank()) {
				return ResponseEntity.badRequest().body("Empty username or password");
			}
			CredentialRepresentation credentials = new CredentialRepresentation();
			credentials.setType(CredentialRepresentation.PASSWORD);
			credentials.setValue(password);
			credentials.setTemporary(false);
			UserRepresentation userRepresentation = new UserRepresentation();
			userRepresentation.setUsername(username);
			userRepresentation.setCredentials(Arrays.asList(credentials));
			userRepresentation.setEnabled(true);
			Map<String, List<String>> attributes = new HashMap<>();
			attributes.put("description", Arrays.asList("A test user"));
			userRepresentation.setAttributes(attributes);
			Keycloak keycloak = getKeycloakInstance();
			javax.ws.rs.core.Response result = keycloak.realm(keycloakRealm).users().create(userRepresentation);
			
			
			List<UserRepresentation> userList = keycloak.realm(keycloakRealm).users().search(username).stream()
	                .filter(userRep -> userRep.getUsername().equals(username)).collect(Collectors.toList());
			
	        userRepresentation = userList.get(0);
	        
			assignRoleToUser(userRepresentation.getId(), userRole);
			
			return new ResponseEntity<>(HttpStatus.valueOf(result.getStatus()));
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

	}
	
	private void assignRoleToUser(String userId, String role) {
        Keycloak keycloak = getKeycloakInstance();
        UsersResource usersResource = keycloak.realm(keycloakRealm).users();
        UserResource userResource = usersResource.get(userId);

        //getting client
        ClientRepresentation clientRepresentation = keycloak.realm(keycloakRealm).clients().findAll().stream().filter(client -> client.getClientId().equals(keycloakClient)).collect(Collectors.toList()).get(0);
        ClientResource clientResource = keycloak.realm(keycloakRealm).clients().get(clientRepresentation.getId());
        //getting role
        RoleRepresentation roleRepresentation = clientResource.roles().list().stream().filter(element -> element.getName().equals(role)).collect(Collectors.toList()).get(0);
        //assigning to user
        userResource.roles().clientLevel(clientRepresentation.getId()).add(Collections.singletonList(roleRepresentation));
    }

	private Boolean checkToken(String token) {
		Request request = new Request.Builder()
				.url(authAddress)
				.header("Content-Type", "application/json")
				.header("Authorization","Bearer "+ token)
				.get()
				.build();
		Response response;
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				return true;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
