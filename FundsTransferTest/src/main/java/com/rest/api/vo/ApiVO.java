package com.rest.api.vo;

import java.math.BigDecimal;

public class ApiVO {
	
	private String seqval;
	private String nextval;
	private String customerId;
	private String debitAccountNo;
	private String creditAccountNo;
	private String description;
	private String transferMode;
	private BigDecimal transferAmt;
	
	public String getSeqval() {
		return seqval;
	}
	public void setSeqval(String seqval) {
		this.seqval = seqval;
	}
	public String getNextval() {
		return nextval;
	}
	public void setNextval(String nextval) {
		this.nextval = nextval;
	}
	public String getDebitAccountNo() {
		return debitAccountNo;
	}
	public void setDebitAccountNo(String debitAccountNo) {
		this.debitAccountNo = debitAccountNo;
	}
	public String getCreditAccountNo() {
		return creditAccountNo;
	}
	public void setCreditAccountNo(String creditAccountNo) {
		this.creditAccountNo = creditAccountNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransferMode() {
		return transferMode;
	}
	public void setTransferMode(String transferMode) {
		this.transferMode = transferMode;
	}
	public BigDecimal getTransferAmt() {
		return transferAmt;
	}
	public void setTransferAmt(BigDecimal transferAmt) {
		this.transferAmt = transferAmt;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
    
	
}
