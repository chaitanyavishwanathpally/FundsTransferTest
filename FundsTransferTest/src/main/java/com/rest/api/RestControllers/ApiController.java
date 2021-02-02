package com.rest.api.RestControllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.api.service.ApiService;
import com.rest.api.util.AccountConstants;
import com.rest.api.vo.ApiVO;



@RestController
@CrossOrigin
@RequestMapping(value = { "/api" })
public class ApiController {
	
	@Autowired 
	private ApiService apiService;
	 

	@PostMapping(value = "funds-transfer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getListFacilities(@RequestBody ApiVO vo) {
		System.out.println(" in api controller : funds-transfer");
			
			if (vo.getCustomerId() == null || "".equalsIgnoreCase(vo.getCustomerId()))
			{
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Please send Customer Id");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);
			}
			if (vo.getDebitAccountNo() == null || "".equalsIgnoreCase(vo.getDebitAccountNo()))
			{
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Please send Debit Account No");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);
			}
			else if (vo.getCreditAccountNo() == null || "".equalsIgnoreCase(vo.getCreditAccountNo()))
			{
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Please send Credit Account No");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);
			}
			else if (vo.getDebitAccountNo() != null && !"".equalsIgnoreCase(vo.getDebitAccountNo()) && vo.getCreditAccountNo() != null && !"".equalsIgnoreCase(vo.getCreditAccountNo()) && vo.getDebitAccountNo().equalsIgnoreCase(vo.getCreditAccountNo()))
			{
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Debit and Credit Account No");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);	
			}
			else if (vo.getTransferMode() == null || "".equalsIgnoreCase(vo.getTransferMode()))
			{				
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Please send Transfer Mode");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);
			}
			else if (vo.getTransferMode() != null && !AccountConstants.NEFT.equalsIgnoreCase(vo.getTransferMode()) && !AccountConstants.IMPS.equalsIgnoreCase(vo.getTransferMode()) && !AccountConstants.RTGS.equalsIgnoreCase(vo.getTransferMode()))
			{				
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Please send valid Transfer Mode");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);
			}
			else if (vo.getTransferAmt() == null)
			{
				JSONObject errJson=new JSONObject();
				errJson.put("status", "error");	
				errJson.put("message", "Please send Transfer Amount");	
				return new ResponseEntity<String>(errJson.toString(), HttpStatus.BAD_REQUEST);
			}
			else
			{
				String transferResult=apiService.transferFunds(vo);
				JSONObject resJson=new JSONObject();
				if(transferResult!=null && "success".equalsIgnoreCase(transferResult))
				{

					resJson.put("status", "success");
					resJson.put("message", "Funds Transfer Successful");
					
				}
				else
				{
					resJson.put("status", "error");	
					resJson.put("message", transferResult);
					
				}	
				
				return new ResponseEntity<String>(resJson.toString(), HttpStatus.OK);
			}	
	}
}
