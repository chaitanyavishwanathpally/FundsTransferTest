package com.rest.api.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="t_bank_book_dtls")
public class TBankBookDtls implements java.io.Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private String bankBookId;
	private String accountNo;
	private String transactionId;
	private String transactionType;
	private BigDecimal transferAmt;
	private BigDecimal balanceAmt;
	private String activeYn;
	private Date crtDt;
	private String crtUsr;
	private Date lstUpdDt;
	private String lstUpdUsr;
	

	@Id
	@Column ( name = "BANK_BOOK_ID", length = 100 , nullable = false )
	public String getBankBookId() {
		return bankBookId;
	}
	public void setBankBookId(String bankBookId) {
		this.bankBookId = bankBookId;
	}
	
	@Column(name="ACCOUNT_NO")
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	@Column(name="TRANSACTION_ID")
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@Column(name="TRANSACTION_TYPE")
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	@Column(name="TRANSFER_AMT")
	public BigDecimal getTransferAmt() {
		return transferAmt;
	}
	public void setTransferAmt(BigDecimal transferAmt) {
		this.transferAmt = transferAmt;
	}
	
	@Column(name="BALANCE_AMT")
	public BigDecimal getBalanceAmt() {
		return balanceAmt;
	}
	public void setBalanceAmt(BigDecimal balanceAmt) {
		this.balanceAmt = balanceAmt;
	}
	
	@Column(name="ACTIVE_YN")
	public String getActiveYn() {
		return activeYn;
	}
	public void setActiveYn(String activeYn) {
		this.activeYn = activeYn;
	}
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="CRT_DATE")
	public Date getCrtDt() {
		return crtDt;
	}
	public void setCrtDt(Date crtDt) {
		this.crtDt = crtDt;
	}
	
	@Column(name="CRT_USER")
	public String getCrtUsr() {
		return crtUsr;
	}
	public void setCrtUsr(String crtUsr) {
		this.crtUsr = crtUsr;
	}
	
	@Temporal( TemporalType.TIMESTAMP)
	@Column(name="LST_UPD_DATE")
	public Date getLstUpdDt() {
		return lstUpdDt;
	}
	public void setLstUpdDt(Date lstUpdDt) {
		this.lstUpdDt = lstUpdDt;
	}
	
	@Column(name="LST_UPD_USER")
	public String getLstUpdUsr() {
		return lstUpdUsr;
	}
	public void setLstUpdUsr(String lstUpdUsr) {
		this.lstUpdUsr = lstUpdUsr;
	}
	
}
