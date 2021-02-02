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
@Table(name="t_account_dtls")
public class TAccountDtls implements java.io.Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private String accountNo;
	private String customerId;
	private String accountType;
	private BigDecimal balanceAmt;
	private BigDecimal unclearAmt;
	private String activeYn;
	private Date crtDt;
	private String crtUsr;
	private Date lstUpdDt;
	private String lstUpdUsr;
	
	@Id
	@Column ( name = "ACCOUNT_NO", length = 100 , nullable = false )
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	@Column(name="CUSTOMER_ID")
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Column(name="ACCOUNT_TYPE")
	public String getAccountType() {
		return accountType;
	}
	
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	@Column(name="BALANCE_AMT")
	public BigDecimal getBalanceAmt() {
		return balanceAmt;
	}
	public void setBalanceAmt(BigDecimal balanceAmt) {
		this.balanceAmt = balanceAmt;
	}
	
	@Column(name="UNCLEAR_AMT")
	public BigDecimal getUnclearAmt() {
		return unclearAmt;
	}
	public void setUnclearAmt(BigDecimal unclearAmt) {
		this.unclearAmt = unclearAmt;
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
