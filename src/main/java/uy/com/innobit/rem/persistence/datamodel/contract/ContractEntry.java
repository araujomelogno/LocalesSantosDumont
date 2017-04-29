package uy.com.innobit.rem.persistence.datamodel.contract;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "contract_entry")
public class ContractEntry extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer yearIndex;
	private BigDecimal amount;
	private BigDecimal ownerComission;
	private BigDecimal clientComission;
	private BigDecimal ownerComissionCharged;
	private BigDecimal clientComissionCharged;
	private BigDecimal rentalCharged;
	private BigDecimal rentalPaid;
	private boolean active;
	private Date init;
	private Date end;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_entry_id")
	@Fetch(FetchMode.SELECT)
	private Set<ContractCharge> contractCharges = new HashSet<ContractCharge>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_entry_id")
	@Fetch(FetchMode.SELECT)
	private Set<ContractPayment> payments = new HashSet<ContractPayment>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYearIndex() {
		return yearIndex;
	}

	public void setYearIndex(Integer yearIndex) {
		this.yearIndex = yearIndex;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOwnerComission() {
		return ownerComission;
	}

	public void setOwnerComission(BigDecimal ownerComission) {
		this.ownerComission = ownerComission;
	}

	public BigDecimal getClientComission() {
		return clientComission;
	}

	public void setClientComission(BigDecimal clientComission) {
		this.clientComission = clientComission;
	}

	public BigDecimal getOwnerComissionCharged() {
		return ownerComissionCharged;
	}

	public void setOwnerComissionCharged(BigDecimal ownerComissionCharged) {
		this.ownerComissionCharged = ownerComissionCharged;
	}

	public BigDecimal getClientComissionCharged() {
		return clientComissionCharged;
	}

	public void setClientComissionCharged(BigDecimal clientComissionCharged) {
		this.clientComissionCharged = clientComissionCharged;
	}

	public BigDecimal getRentalCharged() {
		return rentalCharged;
	}

	public void setRentalCharged(BigDecimal rentalCharged) {
		this.rentalCharged = rentalCharged;
	}

	public BigDecimal getRentalPaid() {
		return rentalPaid;
	}

	public void setRentalPaid(BigDecimal rentalPaid) {
		this.rentalPaid = rentalPaid;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getInit() {
		return init;
	}

	public void setInit(Date init) {
		this.init = init;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Set<ContractPayment> getPayments() {
		return payments;
	}

	public void setPayments(Set<ContractPayment> payments) {
		this.payments = payments;
	}

	public Set<ContractCharge> getContractCharges() {
		return contractCharges;
	}

	public void setContractCharges(Set<ContractCharge> contractCharges) {
		this.contractCharges = contractCharges;
	}

	public Double getFinalPaymentToOwner() {
		return 0d;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
