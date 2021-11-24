package it.unisalento.sonoffbackend.restController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@org.springframework.web.bind.annotation.RestController
public class RestController {
OkHttpClient client = new OkHttpClient();
	
	String host = "http://localhost:8081/";

	@RequestMapping(value = "changeStatusOFF/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> changeStatusOFF(@PathVariable("clientId") String clientId) throws Exception {
		Request request = new Request.Builder().url(host+"changeStatusOFF/"+clientId)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		System.out.println("Something went wrong in changing status to OFF");
		throw new Exception();

	}

	@RequestMapping(value = "changeStatusON/{clientId}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> changeStatusON(@PathVariable("clientId") String clientId) throws Exception {
		Request request = new Request.Builder().url(host+"changeStatusON/"+clientId)
				.get()
				.build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		System.out.println("Something went wrong in changing status to ON");
		throw new Exception();

	}

	  @RequestMapping(value="getStatus/{clientId}", method = RequestMethod.GET) 
	  public String getStatus(@PathVariable("clientId") String clientId) throws Exception{
		   Request request = new Request.Builder().url(host+"getStatus/"+clientId)
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

}
