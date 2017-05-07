package uy.com.innobit.rem.persistence.datamodel.contract;

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
	private Double amount = new Double(0);
	private Double ownerComission = new Double(0);;
	private Double clientComission = new Double(0);;
	private Double ownerComissionCharged = new Double(0);;
	private Double clientComissionCharged = new Double(0);;
	private Double rentalCharged = new Double(0);;
	private Double rentalPaid = new Double(0);;
	private boolean active;
	private Date init;
	private Date end;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ContractCharge> contractCharges = new HashSet<ContractCharge>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getOwnerComission() {
		return ownerComission;
	}

	public void setOwnerComission(Double ownerComission) {
		this.ownerComission = ownerComission;
	}

	public Double getClientComission() {
		return clientComission;
	}

	public void setClientComission(Double clientComission) {
		this.clientComission = clientComission;
	}

	public Double getOwnerComissionCharged() {
		return ownerComissionCharged;
	}

	public void setOwnerComissionCharged(Double ownerComissionCharged) {
		this.ownerComissionCharged = ownerComissionCharged;
	}

	public Double getClientComissionCharged() {
		return clientComissionCharged;
	}

	public void setClientComissionCharged(Double clientComissionCharged) {
		this.clientComissionCharged = clientComissionCharged;
	}

	public Double getRentalCharged() {
		return rentalCharged;
	}

	public void setRentalCharged(Double rentalCharged) {
		this.rentalCharged = rentalCharged;
	}

	public Double getRentalPaid() {
		return rentalPaid;
	}

	public void setRentalPaid(Double rentalPaid) {
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
