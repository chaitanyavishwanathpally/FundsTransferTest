package com.rest.api.Dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rest.api.model.CustomerDtls;
import com.rest.api.model.TAccountDtls;
import com.rest.api.model.TAccountTransactionDtls;
import com.rest.api.model.TBankBookDtls;
import com.rest.api.persistance.GenericDAO;

import com.rest.api.util.AccountConstants;
import com.rest.api.vo.ApiVO;

@Component
public class ApiDaoImpl implements ApiDao {

	@Autowired
	private GenericDAO genericDao;
	
	@Transactional
	public String transferFunds(ApiVO vo) {
		String result="";
		try
		{
			CustomerDtls customerDtls = genericDao.findById(CustomerDtls.class, String.class, vo.getCustomerId());
			
			if(customerDtls!=null && AccountConstants.ACTIVE.equalsIgnoreCase(customerDtls.getActiveYn()))
			{
				TAccountDtls debitAccountDtls = genericDao.findById(TAccountDtls.class, String.class, vo.getDebitAccountNo());
				if(debitAccountDtls!=null && AccountConstants.ACTIVE.equalsIgnoreCase(debitAccountDtls.getActiveYn()))
				{
					TAccountDtls creditAccountDtls = genericDao.findById(TAccountDtls.class, String.class, vo.getCreditAccountNo());
					if(creditAccountDtls!=null && AccountConstants.ACTIVE.equalsIgnoreCase(creditAccountDtls.getActiveYn()))
					{
						if(debitAccountDtls.getBalanceAmt().compareTo(vo.getTransferAmt())==1 )
						{
							SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss aa");
							String formattedDate = dateFormat.format(new Date());
							Date today = dateFormat.parse(formattedDate);
							TAccountTransactionDtls dtls=new TAccountTransactionDtls();
							dtls.setTransactionId(getSequencePostgresql(AccountConstants.TRANSACTION_DTLS_SEQ));
							dtls.setDebitAccountNo(vo.getDebitAccountNo());
							dtls.setCreditAccountNo(vo.getCreditAccountNo());
							dtls.setDescription(vo.getDescription());
							dtls.setTransferMode(vo.getTransferMode());
							dtls.setTransferAmt(vo.getTransferAmt());
							dtls.setActiveYn(AccountConstants.ACTIVE);
							dtls.setCrtDt(today);
							dtls.setCrtUsr(vo.getCustomerId());
							dtls.setLstUpdDt(today);
							dtls.setLstUpdUsr(vo.getCustomerId());
							dtls=genericDao.save(dtls);
							if(dtls!=null)
							{
								System.out.println("debitAccountDtls.getBalanceAmt() : "+debitAccountDtls.getBalanceAmt());
								System.out.println("vo.getTransferAmt() : "+vo.getTransferAmt());
								BigDecimal debitBalAmt=debitAccountDtls.getBalanceAmt().subtract(vo.getTransferAmt());
								System.out.println("debitBalAmt : "+debitBalAmt);
								BigDecimal creditBalAmt=creditAccountDtls.getBalanceAmt().add(vo.getTransferAmt());
								
								//updating balance amount in debit account 
								debitAccountDtls.setBalanceAmt(debitBalAmt);
								genericDao.save(debitAccountDtls);
								
								//updating balance amount in credit account 
								creditAccountDtls.setBalanceAmt(creditBalAmt);
								genericDao.save(creditAccountDtls);
								
								//saving debit details
								TBankBookDtls debitDtls=new TBankBookDtls();
								debitDtls.setBankBookId(getSequencePostgresql(AccountConstants.BANK_BOOK_DTLS_SEQ));
								debitDtls.setAccountNo(vo.getDebitAccountNo());
								debitDtls.setTransactionId(dtls.getTransactionId());
								debitDtls.setTransactionType(AccountConstants.Debit);
								debitDtls.setTransferAmt(vo.getTransferAmt());
								debitDtls.setBalanceAmt(debitBalAmt);
								debitDtls.setActiveYn(AccountConstants.ACTIVE);
								debitDtls.setCrtDt(today);
								debitDtls.setCrtUsr(vo.getCustomerId());
								debitDtls.setLstUpdDt(today);
								debitDtls.setLstUpdUsr(vo.getCustomerId());
								genericDao.save(debitDtls);
								
								//saving credit details
								TBankBookDtls creditDtls=new TBankBookDtls();
								creditDtls.setBankBookId(getSequencePostgresql(AccountConstants.BANK_BOOK_DTLS_SEQ));
								creditDtls.setAccountNo(vo.getCreditAccountNo());
								creditDtls.setTransactionId(dtls.getTransactionId());
								creditDtls.setTransactionType(AccountConstants.Credit);
								creditDtls.setTransferAmt(vo.getTransferAmt());
								creditDtls.setBalanceAmt(creditBalAmt);
								creditDtls.setActiveYn(AccountConstants.ACTIVE);
								creditDtls.setCrtDt(today);
								creditDtls.setCrtUsr(vo.getCustomerId());
								creditDtls.setLstUpdDt(today);
								creditDtls.setLstUpdUsr(vo.getCustomerId());
								genericDao.save(creditDtls);
								
								result="success";
								
							}
							
						}
						else
						{
							result="Insufficient Funds";
						}
					}
					else
					{
						if(creditAccountDtls==null)
						{
							result="Invalid Credit Account";
						}
						else
						{
							result="Inactive Credit Account";
						}
					}
				}
				else
				{
					if(debitAccountDtls==null)
					{
						result="Invalid Debit Account";
					}
					else
					{
						result="Inactive Debit Account";
					}
					
				}
			}
			else
			{
				if(customerDtls==null)
				{
					result="Invalid Customer";
				}
				else
				{
					result="Inactive Customer";
				}
				
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			result="Unexpected error has occured.Please try again later";
		}
		return result;
	}
	
	
	public String getSequencePostgresql(String pStrSeqName) {
  		String lStrSeqRetVal = "";
  		try {
  			StringBuffer query = new StringBuffer();
  			String[] args = new String[1];
			args[0] = pStrSeqName;
  			query.append(" SELECT nextval(?0)||'' as seqval");
  			List<ApiVO> seqList = genericDao.executeSQLQueryList(ApiVO.class, query.toString(),args);
  			if (seqList != null && seqList.size() > 0) {
  				lStrSeqRetVal = String.valueOf(seqList.get(0).getSeqval());
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  		return lStrSeqRetVal;
  	}


	

}
