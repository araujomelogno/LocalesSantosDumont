package uy.com.innobit.rem.persistence.datamodel.contract;

import java.text.SimpleDateFormat;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "contract_entry")
public class ContractEntry extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;

	private Integer yearIndex;
	private Double amount = new Double(0);
	private Double ownerComission = new Double(0);;
	private Double clientComission = new Double(0);;

	private String currency;
	private boolean active = true;
	private Date init;
	private Date end;
	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@ManyToOne(optional = true)
	@JoinColumn(name = "contract_id", nullable = true)
	private Contract contract;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ContractCharge> contractCharges = new HashSet<ContractCharge>();
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ContractPayment> payments = new HashSet<ContractPayment>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ContractExpiration> expirations = new HashSet<ContractExpiration>();

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
		Double result = 0d;
		for (ContractCharge c : contractCharges)
			if (c.getCommission() && c.getSource() != null && c.getSource().equalsIgnoreCase("propietario"))
				if (c.getCurrency().equalsIgnoreCase("$"))
					result = result + c.getAmount();
				else
					result = result + c.getAmount() * c.getDollarCotization();
		return result;
	}

	public Double getClientComissionCharged() {
		Double result = 0d;
		for (ContractCharge c : contractCharges)
			if (c.getCommission() && c.getSource() != null && c.getSource().equalsIgnoreCase("inquilino"))
				if (c.getCurrency().equalsIgnoreCase("$"))
					result = result + c.getAmount();
				else
					result = result + c.getAmount() * c.getDollarCotization();
		return result;
	}

	public Double getRentalCharged() {
		Double result = 0d;
		for (ContractCharge c : contractCharges)
			if (!c.getCommission())
				if (c.getCurrency().equalsIgnoreCase("$"))
					result = result + c.getAmount();
				else
					result = result + c.getAmount() * c.getDollarCotization();
		return result;
	}

	public Double getRentalPaid() {
		Double result = 0d;
		for (ContractPayment c : payments)
			if (c.getCurrency().equalsIgnoreCase("$"))
				result = result + c.getAmount();
			else
				result = result + c.getAmount() * c.getDollarCotization();
		return result;
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
		return (amount - amount * 0.105 - ownerComission);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		if (id != null)
			return id;
		return super.hashCode();

	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContractEntry) {
			if (id == null)
				return false;
			ContractEntry new_name = (ContractEntry) obj;
			return new_name.getId().equals(id);

		}
		return false;
	}

	public String getProperty() {
		String result = "";
		if (getContract() != null)
			result = getContract().getPropertyName();
		return result;
	}

	public String getOccupant() {
		String result = "";
		if (getContract() != null && getContract().getOccupant() != null)
			result = getContract().getOccupant().getName();
		return result;
	}

	public String getOwner() {
		String result = "";
		if (getContract() != null)
			result = getContract().getOwnerName();
		return result;
	}

	public Double rentalCharged() {
		Double result = 0d;
		for (ContractCharge c : contractCharges) {
			if (!c.getCommission())
				result = result + c.getAmount();
		}
		return result;
	}

	public Double rentalPaid() {
		Double result = 0d;
		for (ContractPayment p : payments) {
			result = result + p.getAmount();
		}
		return result;
	}

	public Double ownerCommissionCharged() {
		Double result = 0d;
		for (ContractCharge c : contractCharges) {
			if (c.getCommission() && c.getSource().equalsIgnoreCase("propietario"))
				result = result + c.getAmount();
		}
		return result;
	}

	public Double clientCommissionCharged() {
		Double result = 0d;
		for (ContractCharge c : contractCharges) {
			if (c.getCommission() && c.getSource().equalsIgnoreCase("inquilino"))
				result = result + c.getAmount();
		}
		return result;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public String getInitSDF() {
		if (init == null)
			return "";
		return sdf.format(init);
	}

	public String getEndSDF() {
		if (end == null)
			return "";
		return sdf.format(end);
	}

	public Set<ContractExpiration> getExpirations() {
		return expirations;
	}

	public void setExpirations(Set<ContractExpiration> expirations) {
		this.expirations = expirations;
	}

	public Double getNextEntryAmount() {
		if (contract != null) {
			int index = yearIndex + 1;
			for (ContractEntry ce : contract.getEntries())
				if (ce.getYearIndex() == index)
					return ce.getAmount();
		}
		return 0d;
	}

	public Double getNextEntryOwnerCommision() {
		if (contract != null) {
			int index = yearIndex + 1;
			for (ContractEntry ce : contract.getEntries())
				if (ce.getYearIndex() == index)
					return ce.getOwnerComission();
		}
		return 0d;
	}

	public Double getNextEntryClientCommision() {
		if (contract != null) {
			int index = yearIndex + 1;
			for (ContractEntry ce : contract.getEntries())
				if (ce.getYearIndex() == index)
					return ce.getClientComission();
		}
		return 0d;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
