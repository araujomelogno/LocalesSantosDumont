package uy.com.innobit.rem.persistence.datamodel.contract;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "contract_document")
public class ContractDocument extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id = 0;
	private String name;
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private byte[] content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ContractDocument) {
			ContractDocument new_name = (ContractDocument) obj;
			return id.equals(new_name.getId());
		}
		return false;
	}

}
