package uy.com.innobit.rem.persistence.datamodel.contract;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "contract_payment")
public class ContractPayment extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
 

	private Date paymentDate;
	private String type;
	private BigDecimal amount;
	private Boolean check;
	private Date checkDate;
	private String checkNumber;
	private String bank;
	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

 

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public String getCheckDateSDF() {
		if (checkDate != null)
			return sdf.format(checkDate);
		return "";
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPaymentDateSDF() {
		if (paymentDate != null)
			return sdf.format(paymentDate);
		return "";
	}
}
