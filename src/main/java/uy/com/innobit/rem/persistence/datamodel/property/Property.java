package uy.com.innobit.rem.persistence.datamodel.property;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import uy.com.innobit.rem.persistence.datamodel.clients.Owner;
import uy.com.innobit.rem.persistence.datamodel.contract.Contract;
import uy.com.innobit.rem.persistence.datamodel.contract.ContractEntry;

@Entity(name = "property")
public class Property extends Bean {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;
	@ManyToOne(optional = true)
	@JoinColumn(name = "owner_id")
	private Owner owner;
	private String address;
	private String nro;
	private String padron;
	private String block;
	private String refUte;
	private String refAgua;
	private String size;
	private Boolean payExpenses;
	private Integer expenses;
	private String expensesFreq;
	private String admin;
	private String obs;
	private String tel;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "property_id")
	@Fetch(FetchMode.SELECT)
	private Set<PropertyDocument> documents = new HashSet<PropertyDocument>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "property_id")
	@Fetch(FetchMode.SELECT)
	private Set<PropertyNotification> notifications = new HashSet<PropertyNotification>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "property_id")
	@Fetch(FetchMode.SELECT)
	private Set<PropertyPicture> pictures = new HashSet<PropertyPicture>();

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "property_id")
	@Fetch(FetchMode.SELECT)
	private Set<Contract> contracts = new HashSet<Contract>();

	private double lat = 0d;
	private double lng = 0d;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPadron() {
		return padron;
	}

	public void setPadron(String padron) {
		this.padron = padron;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getRefUte() {
		return refUte;
	}

	public void setRefUte(String refUte) {
		this.refUte = refUte;
	}

	public String getRefAgua() {
		return refAgua;
	}

	public void setRefAgua(String refAgua) {
		this.refAgua = refAgua;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getExpensesFreq() {
		return expensesFreq;
	}

	public void setExpensesFreq(String expensesFreq) {
		this.expensesFreq = expensesFreq;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<PropertyDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(Set<PropertyDocument> documents) {
		this.documents = documents;
	}

	public Set<PropertyNotification> getNotifications() {
		return notifications;
	}

	public void setNotifications(Set<PropertyNotification> notifications) {
		this.notifications = notifications;
	}

	public Set<PropertyPicture> getPictures() {
		return pictures;
	}

	public void setPictures(Set<PropertyPicture> pictures) {
		this.pictures = pictures;
	}

	public boolean isEmpty() {
		Date now = new Date();
		for (Contract p : contracts)
			if (p.getInit() != null && p.getInit().before(now) && p.getEnd() != null && p.getEnd().after(now)) {
				for (ContractEntry entry : p.getEntries()) {
					if (entry.getInit() != null && entry.getInit().before(now) && entry.getEnd() != null
							&& entry.getEnd().after(now) && entry.isActive())
						return false;
				}
			}
		return true;
	}

	public Integer getExpenses() {
		return expenses;
	}

	public void setExpenses(Integer expenses) {
		this.expenses = expenses;
	}

	public Set<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}

	public String getNro() {
		return nro;
	}

	public void setNro(String nro) {
		this.nro = nro;
	}

	public Boolean getPayExpenses() {
		return payExpenses;
	}

	public void setPayExpenses(Boolean payExpenses) {
		this.payExpenses = payExpenses;
	}

	public Contract getActualContract() {
		Date now = new Date();
		for (Contract c : contracts)
			if (c.getInit() != null && c.getEnd() != null && !now.before(c.getInit()) && !now.after(c.getEnd()))
				return c;
		return null;
	}

}
