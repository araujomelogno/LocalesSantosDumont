package uy.com.innobit.rem.persistence.datamodel.clients;

import javax.persistence.Entity;

@Entity(name = "occupant")
public class Occupant extends Individual {
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
