package uy.com.innobit.rem.persistence.datamodel.contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "contract_expiration")
public class ContractExpiration extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;

	private Date expectedDate;
	private Double amount;
	private String currency;
	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contract_entry_id", nullable = true)
	private ContractEntry entry;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ContractCharge> contractCharges = new HashSet<ContractCharge>();

	public ContractExpiration() {

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContractExpiration) {
			ContractExpiration new_name = (ContractExpiration) obj;
			return this.id == (new_name.getId());
		}
		return false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public ContractEntry getEntry() {
		return entry;
	}

	public void setEntry(ContractEntry entry) {
		this.entry = entry;
	}

	public Date getExpectedDate() {
		return expectedDate;
	}

	public String getExpectedDateSdf() {
		if (expectedDate == null)
			return null;
		return sdf.format(expectedDate);
	}

	public void setExpectedDate(Date expectedDate) {
		this.expectedDate = expectedDate;
	}

	@Override
	public String toString() {
		return getExpectedDateSdf() + "-" + getCurrency() + getAmount();
	}

	public Set<ContractCharge> getContractCharges() {
		if (contractCharges == null)
			contractCharges = new HashSet<ContractCharge>();
		return contractCharges;
	}

	public void setContractCharges(Set<ContractCharge> contractCharges) {
		this.contractCharges = contractCharges;
	}

}
