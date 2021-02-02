package com.rest.api.fundsTransferTest.junit;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class FundsTransferUnitTest {

	@LocalServerPort
    int randomServerPort;
    
	
	@Test
	public void transferAPISuccess() throws Exception 
	{
	
		System.out.println("junit test Transfer Funds Success");

		JSONObject req=new JSONObject();
		 req.put("customerId","100000");
		 req.put("debitAccountNo","1000000000");
		 req.put("creditAccountNo","1000000001");
		 req.put("description","test");
		 req.put("transferMode","IMPS");
		 req.put("transferAmt",new BigDecimal(1000.00));
		
		 String reqBody=req.toString();	
		 
	     
	    String apiUrl = "http://localhost:"+randomServerPort+"/FundsTransferTest/api/funds-transfer";
	    System.out.println("apiUrl : "+apiUrl);
	    RestTemplate restTemplate = new RestTemplate();	
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Type","application/json");
    	headers.add("Accept","application/json");
		System.out.println("reqBody : "+reqBody);
		HttpEntity<String> apiReq= new HttpEntity<String>(reqBody,headers);
		ResponseEntity<String> apiResp=restTemplate.exchange(apiUrl,HttpMethod.POST,apiReq,String.class);
		System.out.println("apiResp : "+apiResp);
		System.out.println(apiResp.getStatusCodeValue());
		if(apiResp!=null && apiResp.getStatusCode().equals(HttpStatus.OK))
		{
			String apiResponse=apiResp.getBody().toString();
			if(apiResponse!=null && !"".equalsIgnoreCase(apiResponse)) 
			{
				JSONObject responseJson=new JSONObject(apiResponse);
				String status=responseJson.getString("status");
				assertEquals(true, status.equalsIgnoreCase("success"));
				
			}
			
			
		}   
	}
	
	@Test
	public void transferAPISourceAcctInvalid() throws Exception 
	{
	
		System.out.println("junit test Invalid Source Account");

		JSONObject req=new JSONObject();
		 req.put("customerId","100000");
		 req.put("debitAccountNo","100000");
		 req.put("creditAccountNo","1000000001");
		 req.put("description","test");
		 req.put("transferMode","IMPS");
		 req.put("transferAmt",new BigDecimal(1000.00));
		
		 String reqBody=req.toString();	
		 
	     
	    String apiUrl = "http://localhost:"+randomServerPort+"/FundsTransferTest/api/funds-transfer";
	    System.out.println("apiUrl : "+apiUrl);
	    RestTemplate restTemplate = new RestTemplate();	
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Type","application/json");
    	headers.add("Accept","application/json");
		System.out.println("reqBody : "+reqBody);
		HttpEntity<String> apiReq= new HttpEntity<String>(reqBody,headers);
		ResponseEntity<String> apiResp=restTemplate.exchange(apiUrl,HttpMethod.POST,apiReq,String.class);
		System.out.println("apiResp : "+apiResp);
		System.out.println(apiResp.getStatusCodeValue());
		if(apiResp!=null && apiResp.getStatusCode().equals(HttpStatus.OK))
		{
			String apiResponse=apiResp.getBody().toString();
			if(apiResponse!=null && !"".equalsIgnoreCase(apiResponse)) 
			{
				JSONObject responseJson=new JSONObject(apiResponse);
				String status=responseJson.getString("status");
				assertEquals(true, !status.equalsIgnoreCase("success"));
				
			}
			
			
		}    
	}
	
	@Test
	public void transferAPIDestAcctInvalid() throws Exception 
	{
	
		System.out.println("junit test Invalid Destination Account");

		JSONObject req=new JSONObject();
		 req.put("customerId","100000");
		 req.put("debitAccountNo","1000000000");
		 req.put("creditAccountNo","1000");
		 req.put("description","test");
		 req.put("transferMode","IMPS");
		 req.put("transferAmt",new BigDecimal(1000.00));
		
		 String reqBody=req.toString();	
		 
	     
	    String apiUrl = "http://localhost:"+randomServerPort+"/FundsTransferTest/api/funds-transfer";
	    System.out.println("apiUrl : "+apiUrl);
	    RestTemplate restTemplate = new RestTemplate();	
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Type","application/json");
    	headers.add("Accept","application/json");
		System.out.println("reqBody : "+reqBody);
		HttpEntity<String> apiReq= new HttpEntity<String>(reqBody,headers);
		ResponseEntity<String> apiResp=restTemplate.exchange(apiUrl,HttpMethod.POST,apiReq,String.class);
		System.out.println("apiResp : "+apiResp);
		System.out.println(apiResp.getStatusCodeValue());
		if(apiResp!=null && apiResp.getStatusCode().equals(HttpStatus.OK))
		{
			String apiResponse=apiResp.getBody().toString();
			if(apiResponse!=null && !"".equalsIgnoreCase(apiResponse)) 
			{
				JSONObject responseJson=new JSONObject(apiResponse);
				String status=responseJson.getString("status");
				assertEquals(true, !status.equalsIgnoreCase("success"));
				
			}
			
			
		}   
	}
	
	@Test
	public void transferAPIInsuffFunds() throws Exception 
	{
	
		System.out.println("junit test Insufficient funds");

		JSONObject req=new JSONObject();
		 req.put("customerId","100000");
		 req.put("debitAccountNo","1000000000");
		 req.put("creditAccountNo","1000000001");
		 req.put("description","test");
		 req.put("transferMode","IMPS");
		 req.put("transferAmt",new BigDecimal(52000.00));
		
		 String reqBody=req.toString();	
		 
	     
	    String apiUrl = "http://localhost:"+randomServerPort+"/FundsTransferTest/api/funds-transfer";
	    System.out.println("apiUrl : "+apiUrl);
	    RestTemplate restTemplate = new RestTemplate();	
		HttpHeaders headers=new HttpHeaders();
		headers.add("Content-Type","application/json");
    	headers.add("Accept","application/json");
		System.out.println("reqBody : "+reqBody);
		HttpEntity<String> apiReq= new HttpEntity<String>(reqBody,headers);
		ResponseEntity<String> apiResp=restTemplate.exchange(apiUrl,HttpMethod.POST,apiReq,String.class);
		System.out.println("apiResp : "+apiResp);
		System.out.println(apiResp.getStatusCodeValue());
		if(apiResp!=null && apiResp.getStatusCode().equals(HttpStatus.OK))
		{
			String apiResponse=apiResp.getBody().toString();
			if(apiResponse!=null && !"".equalsIgnoreCase(apiResponse)) 
			{
				JSONObject responseJson=new JSONObject(apiResponse);
				String status=responseJson.getString("status");
				assertEquals(true, !status.equalsIgnoreCase("success"));
				
			}
			
			
		}  
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
