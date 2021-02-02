package com.rest.api.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rest.api.Dao.ApiDao;
import com.rest.api.vo.ApiVO;
@Component
public class ApiServiceImpl implements ApiService {

	@Autowired
	private ApiDao apiDao;

	@Override
	public String transferFunds(ApiVO vo) {
		String result="";
		try {
			result=apiDao.transferFunds(vo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
