package uy.com.innobit.rem.persistence.datamodel.clients;

import javax.persistence.Entity;

@Entity(name = "owner")
public class Owner extends Individual {
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
