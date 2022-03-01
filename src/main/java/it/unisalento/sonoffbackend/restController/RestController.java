package it.unisalento.sonoffbackend.restController;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.JSONArray;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.underscore.U;
import com.github.underscore.U.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import it.unisalento.sonoffbackend.domain.Event;
import it.unisalento.sonoffbackend.domain.User;
import it.unisalento.sonoffbackend.dto.EventDTO;
import it.unisalento.sonoffbackend.dto.UserDTO;
import it.unisalento.sonoffbackend.exception.InvalidTokenEx;
import it.unisalento.sonoffbackend.iService.IEventService;
import it.unisalento.sonoffbackend.model.Credential;
import it.unisalento.sonoffbackend.model.LoggedUser;
import it.unisalento.sonoffbackend.wrapper.LogApiWrapper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestBody;


@org.springframework.web.bind.annotation.RestController
public class RestController {
	
	@Autowired
	IEventService eventService;
	
	OkHttpClient client = new OkHttpClient();
	private final String INVALID_TOKEN = "Invalid token";
	private String ip = "10.3.141.130";


	
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
	
	/*
	
	private String keycloakClient = "backend";

	private String keycloakUrl = "http://"+ip+":8180/auth" ;
	
	private String keycloakRealm= "MyRealm";

	private String keycloakAdminUser= "admin";

	private String keycloakAdminPassword= "admin";
	*/
	
	private Keycloak getAdminKeycloakInstance() {
		return Keycloak.getInstance(
				keycloakUrl,
				"master",
				keycloakAdminUser,
				keycloakAdminPassword,
				"admin-cli");
	}

	//private String ip = "192.168.1.100";
	private final String authAddress = "http://"+ip+":8180/auth/realms/MyRealm/protocol/openid-connect/userinfo";
	private final String refreshAddress="http://"+ip+":8180/auth/realms/MyRealm/protocol/openid-connect/token";
	private final String cmdTopic1 = "cmnd/tasmota_8231A8/POWER1";
	private final String reqTopic = "cmnd/tasmota_8231A8/STATUS11";
	private final String statTopic = "stat/tasmota_8231A8/STATUS11";
	private final String broker = "tcp://localhost:1883";		
	private String status="";

	@RequestMapping(value = "changeStatusON/{clientId}", method = RequestMethod.POST)
	public ResponseEntity<LoggedUser> changeStatusON(@PathVariable("clientId") String clientId, @RequestBody LoggedUser user) throws Exception {
		
		try {
			user = checkToken(user); //LANCIA UN ECCEZIONE SE IL TOKEN NON E' PIU' VALIDO E NON PUO' ESSERE REFRESHATO
			MqttClient client = connectToBroker(cmdTopic1, clientId);
			MqttMessage message = new MqttMessage("ON".getBytes());
			System.out.println("Trying to change status to ON...");
			client.publish(cmdTopic1, message);
			client.disconnect(100);
			System.out.println("Client " + client.getClientId() + " disconnected succesfully");
			client.close();	
			return new ResponseEntity<>(user, HttpStatus.OK);
			
		}catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InvalidTokenEx e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (MqttException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value = "changeStatusOFF/{clientId}", method = RequestMethod.POST)
	public ResponseEntity<LoggedUser> changeStatusOFF(@PathVariable("clientId") String clientId, @RequestBody LoggedUser user) throws Exception {
		
		try {
			user = checkToken(user); //LANCIA UN ECCEZIONE SE IL TOKEN NON E' PIU' VALIDO E NON PUO' ESSERE REFRESHATO
			MqttClient client = connectToBroker(cmdTopic1, clientId);
			MqttMessage message = new MqttMessage("OFF".getBytes());
			System.out.println("Trying to change status to ON...");
			client.publish(cmdTopic1, message);
			client.disconnect(100);
			System.out.println("Client " + client.getClientId() + " disconnected succesfully");
			client.close();	
			return new ResponseEntity<>(user, HttpStatus.OK);
			
		}catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InvalidTokenEx e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (MqttException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value="getStatus/{clientId}", method = RequestMethod.POST) 
	public ResponseEntity<String> getStatus(@PathVariable("clientId") String clientId, @RequestBody LoggedUser user){	
		try {
			user = checkToken(user);
			//status1 = "";
			MqttClient client = connectToBroker(statTopic, clientId);;
			System.out.println("Trying to subscribe to "+statTopic);
			client.subscribe(statTopic, new IMqttMessageListener() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					status = new String(message.getPayload());
					System.out.println("Getted status: " + status);
					client.disconnect();
				}
			});
			MqttMessage message = new MqttMessage();
			message.setPayload("0".getBytes());
			System.out.println("Trying to get status...");
			client.publish(reqTopic, message);	//BLOCKING
			while(client.isConnected());
			System.out.println("Client " + client.getClientId() + " disconnected succesfully");
			client.close();
			System.out.println("status: "+status.split(",")[8]);
			
			String status1 = status.split(",")[8].split(":")[1].replaceAll("\"", "");
			String pir = status.split(",")[9].split(":")[1].replaceAll("\"", "");
			String touch = status.split(",")[10].split(":")[1].replaceAll("\"", "");
			Builder builder = U.objectBuilder()
					.add("status", status1)
					.add("pirSensor", pir)
					.add("touchSensor", touch);
			
			if(user!=null) {
				builder
					.add("user", U.objectBuilder()
					.add("username", user.getUsername())
					.add("role", user.getRole())
					.add("token", user.getToken())
					.add("refreshToken", user.getRefreshToken()));
			}
			String retValue = builder.toJson();
			return new ResponseEntity<>(retValue, HttpStatus.OK); //OTTENGO LO STATO DI POWER1, POWER2, POWER3*/
		}
		catch (MqttException e) {
			System.out.println("Something went wrong while getting status!\n" + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InvalidTokenEx e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
	}
	
	@RequestMapping(value="getTouchSensorState/{clientId}", method = RequestMethod.POST) 
	public ResponseEntity<String> getTouchSensorState(@PathVariable("clientId") String clientId, @RequestBody LoggedUser user){
		try {
			user = checkToken(user);
			MqttClient client = connectToBroker(statTopic, clientId);;
			System.out.println("Trying to subscribe to "+statTopic);
			client.subscribe(statTopic, new IMqttMessageListener() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					status = new String(message.getPayload());
					System.out.println("Getted status: " + status);
					client.disconnect();
				}
			});
			MqttMessage message = new MqttMessage();
			//message.setPayload("".getBytes());
			message.setPayload("0".getBytes());
			System.out.println("Trying to get status...");
			client.publish(reqTopic, message);	//BLOCKING
			while(client.isConnected());
			System.out.println("Client " + client.getClientId() + " disconnected succesfully");
			client.close();
			
			String touch = status.split(",")[10].split(":")[1].replaceAll("\"", "");
			Builder builder = U.objectBuilder()
					.add("touchSensor", touch);
			
			if(user!=null) {
				builder
					.add("user", U.objectBuilder()
					.add("username", user.getUsername())
					.add("role", user.getRole())
					.add("token", user.getToken())
					.add("refreshToken", user.getRefreshToken()));
			}
			String retValue = builder.toJson();
			return new ResponseEntity<>(retValue, HttpStatus.OK); //OTTENGO LO STATO DI POWER1*/
		}
		catch (MqttException e) {
			System.out.println("Something went wrong while getting status!\n" + e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (InvalidTokenEx e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	

	@RequestMapping(value="auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoggedUser> authentication(@RequestBody Credential credential) {
		String accessToken = null;
		String refreshToken = null;
		try {
			Keycloak instance = Keycloak.getInstance(keycloakUrl, keycloakRealm, credential.getUsername(), credential.getPassword(), keycloakClient, keycloakClientSecret);                                                                                                      
			TokenManager tokenmanager = instance.tokenManager();
			accessToken = tokenmanager.getAccessTokenString();
			refreshToken = tokenmanager.getAccessToken().getRefreshToken();		
			LoggedUser user = new LoggedUser();
			
			String role = getRolesByUser(credential.getUsername());
			if(role!=null) {
				user.setUsername(credential.getUsername());
				user.setToken(accessToken);
				user.setRole(role);
				user.setRefreshToken(refreshToken);
				return new ResponseEntity<LoggedUser>(user, HttpStatus.OK);
			}
			return new ResponseEntity<LoggedUser>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		catch (javax.ws.rs.NotAuthorizedException e) {
			return new ResponseEntity<LoggedUser>(HttpStatus.UNAUTHORIZED);
		}
		
	}

	@PostMapping("createUser/{username}/{password}/{userRole}")
	public ResponseEntity<LoggedUser> createUser(@PathVariable("username") String username, @PathVariable("password") String password, @PathVariable("userRole") String userRole, @RequestBody LoggedUser user) {
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
	
	
	//TODO: verificare che funzioni
		@PostMapping("getEventLog")
		public ResponseEntity<LogApiWrapper> getEventLog(@RequestBody LoggedUser loggedUser){
			try {
				loggedUser = checkToken(loggedUser);
				List<Event> eventList = eventService.findAll();		
				List<EventDTO> eventDtoList = new ArrayList<>();
				EventDTO eventDTO;
				UserDTO userDTO;
				User user;
				
				for(Event event : eventList) {
					eventDTO = new EventDTO();
					eventDTO.setId(event.getId());
					eventDTO.setDate(event.getDate());
					eventDTO.setEvent_type(event.getEvent_type());
					userDTO = new UserDTO();
					user = event.getUser();
					if(user!=null) {
						userDTO = new UserDTO();
						userDTO.setId(user.getId());
						userDTO.setName(user.getName());
						userDTO.setSurname(user.getSurname());
						userDTO.setUsername(user.getUsername());
						eventDTO.setUserDTO(userDTO);
					}
					eventDtoList.add(eventDTO);
				}
				
				LogApiWrapper law = new LogApiWrapper();
				law.setLoggedUser(loggedUser);
				law.setEventDtoList(eventDtoList);
				return new ResponseEntity<>(law, HttpStatus.OK);
			}catch (ParseException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (InvalidTokenEx e) {
				return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

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
	
	private LoggedUser checkToken(LoggedUser user) throws ParseException, InvalidTokenEx, IOException{
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
					return null;
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
	
	private LoggedUser executeRefresh(LoggedUser user) throws ParseException, IOException, InvalidTokenEx {
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
	    		throw new InvalidTokenEx(INVALID_TOKEN);	  
	    	}
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		} catch (InvalidTokenEx e) {
			throw e;
		}
	}
	
	private MqttClient connectToBroker(String topic, String clientId) throws MqttException {
		MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
		client.setCallback(new MqttCallbackExtended() {
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
			}
			
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				if(topic.equals(cmdTopic1)) {
					System.out.println("State changed!");
				}
				else {
					System.out.println("Getting status...");
				}
			}
			
			@Override
			public void connectionLost(Throwable cause) {
			}
			
			@Override
			public void connectComplete(boolean reconnect, String serverURI) {
				System.out.println("Connected succesfully!");
			}
		});
		MqttConnectOptions opt = new MqttConnectOptions();
		opt.setCleanSession(true);
		System.out.println("Connceting to broker at: " + broker);
		client.connect(opt);
		return client;
	}

}
