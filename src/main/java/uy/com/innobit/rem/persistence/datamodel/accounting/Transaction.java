package uy.com.innobit.rem.persistence.datamodel.accounting;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "transaction")
public class Transaction extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String concept;
	private String detail;
	private BigDecimal amount;
	private String type;
	private String bank;
	private String checknumber;
	
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private byte[] chekscan;
	private String mimetype;

	private Date expirationDate;
	private Date paymentDate;
	private boolean comission;

	@ManyToOne(optional = false)
	@JoinColumn(name = "from_account_id", nullable = false)
	private CurrentAccount fromAccount;
	@ManyToOne(optional = false)
	@JoinColumn(name = "to_account_id", nullable = false)
	private CurrentAccount toAccount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public CurrentAccount getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(CurrentAccount fromAccount) {
		this.fromAccount = fromAccount;
	}

	public CurrentAccount getToAccount() {
		return toAccount;
	}

	public void setToAccount(CurrentAccount toAccount) {
		this.toAccount = toAccount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getChecknumber() {
		return checknumber;
	}

	public void setChecknumber(String checknumber) {
		this.checknumber = checknumber;
	}

	public byte[] getChekscan() {
		return chekscan;
	}

	public void setChekscan(byte[] chekscan) {
		this.chekscan = chekscan;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public boolean isComission() {
		return comission;
	}

	public void setComission(boolean comission) {
		this.comission = comission;
	}

}
