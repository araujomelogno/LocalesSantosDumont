package uy.com.innobit.rem.persistence.datamodel.contract;

import java.math.BigDecimal;
import java.security.acl.Owner;
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import uy.com.innobit.rem.persistence.datamodel.Bean;
import uy.com.innobit.rem.persistence.datamodel.clients.Occupant;
import uy.com.innobit.rem.persistence.datamodel.property.Property;
import uy.com.innobit.rem.persistence.datamodel.property.inventory.Inventory;

@Entity(name = "contract")
public class Contract extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;
	private Date init;
	private Date end;
	private boolean cede;
	private boolean warranty;
	private String warrantyType;
	private int notdays;
	private String obs;
	@ManyToOne(optional = false)
	@JoinColumn(name = "property_id", nullable = false)
	private Property property;
	@ManyToOne(optional = false)
	@JoinColumn(name = "occupant_id", nullable = false)
	private Occupant occupant;

	private BigDecimal totalPayments;
	private BigDecimal totalPrice;
	private BigDecimal totalCharges;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_id")
	@Fetch(FetchMode.SELECT)
	private Set<ContractEntry> entries = new HashSet<ContractEntry>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_id")
	@Fetch(FetchMode.SELECT)
	private Set<ContractDocument> documents = new HashSet<ContractDocument>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_id")
	@Fetch(FetchMode.SELECT)
	private Set<ContractNotification> notifications = new HashSet<ContractNotification>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_id")
	@Fetch(FetchMode.SELECT)
	private Set<ContractExpense> expenses = new HashSet<ContractExpense>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "contract_id")
	@Fetch(FetchMode.SELECT)
	private Set<Inventory> inventories = new HashSet<Inventory>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public boolean isCede() {
		return cede;
	}

	public void setCede(boolean cede) {
		this.cede = cede;
	}

	public boolean isWarranty() {
		return warranty;
	}

	public void setWarranty(boolean warranty) {
		this.warranty = warranty;
	}

	public String getWarrantyType() {
		return warrantyType;
	}

	public void setWarrantyType(String warrantyType) {
		this.warrantyType = warrantyType;
	}

	public int getNotdays() {
		return notdays;
	}

	public void setNotdays(int notdays) {
		this.notdays = notdays;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Occupant getOccupant() {
		return occupant;
	}

	public void setOccupant(Occupant occupant) {
		this.occupant = occupant;
	}

	public void setEntries(Set<ContractEntry> entries) {
		this.entries = entries;
	}

	public Set<ContractEntry> getEntries() {
		return entries;
	}

	public BigDecimal getTotalPayments() {
		return totalPayments;
	}

	public void setTotalPayments(BigDecimal totalPayments) {
		this.totalPayments = totalPayments;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getTotalCharges() {
		return totalCharges;
	}

	public void setTotalCharges(BigDecimal totalCharges) {
		this.totalCharges = totalCharges;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Contract) {
			Contract new_name = (Contract) obj;
			return id.equals(new_name.getId());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public Set<ContractDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<ContractDocument> documents) {
		this.documents = documents;
	}

	public Set<ContractNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<ContractNotification> notifications) {
		this.notifications = notifications;
	}

	public Set<ContractExpense> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<ContractExpense> expenses) {
		this.expenses = expenses;
	}

	public Set<Inventory> getInventories() {
		return inventories;
	}

	public void setInventories(Set<Inventory> inventories) {
		this.inventories = inventories;
	}

	public String getPropertyName() {
		if (property != null)
			return property.getName();
		return "";
	}

	public String getOwnerName() {
		if (property != null)
			return property.getOwner().getName();
		return "";
	}
}
