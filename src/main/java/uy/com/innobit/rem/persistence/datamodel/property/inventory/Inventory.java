package uy.com.innobit.rem.persistence.datamodel.property.inventory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "inventory")
public class Inventory extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private Date created;

	@OneToMany(mappedBy = "inventory")
	private Set<InventoryItem> items = new HashSet<InventoryItem>();
 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<InventoryItem> getItems() {
		return items;
	}

	public void setItems(Set<InventoryItem> items) {
		this.items = items;
	}

 
}
