package uy.com.innobit.rem.persistence.datamodel.contract;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "contract_charge")
public class ContractCharge extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Date paymentDate;
	private String type;

	private BigDecimal amount;
	private String currency;
	private Boolean check;
	private Date checkDate;
	private String checkNumber;
	private String bank;
	private Boolean commission;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	private byte[] checkImage;

	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	@Transient
	private String SOURCE;

	public ContractCharge() {

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContractCharge) {
			ContractCharge new_name = (ContractCharge) obj;
			this.id.equals(new_name.getId());
		}
		return false;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public String getPaymentDateSDF() {
		if (paymentDate != null)
			return sdf.format(paymentDate);
		return "";
	}

	public String getCheckDateSDF() {
		if (checkDate != null)
			return sdf.format(checkDate);
		return "";
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

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}

	public Boolean getCommission() {
		return commission;
	}

	public void setCommission(Boolean commission) {
		this.commission = commission;
	}

	public byte[] getCheckImage() {
		return checkImage;
	}

	public void setCheckImage(byte[] checkImage) {
		this.checkImage = checkImage;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}