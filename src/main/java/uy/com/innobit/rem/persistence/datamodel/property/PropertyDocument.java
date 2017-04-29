package uy.com.innobit.rem.persistence.datamodel.property;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import uy.com.innobit.rem.persistence.datamodel.Bean;

@Entity(name = "property_document")
public class PropertyDocument extends Bean {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private String mimeType;
	private Date uploaded;
	@Lob
	@Basic(fetch = FetchType.EAGER)
	private byte[] content;

	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getUploaded() {
		return uploaded;
	}

	public void setUploaded(Date uploaded) {
		this.uploaded = uploaded;
	}

	public String getDateSDF() {
		if (uploaded == null)
			return "";
		else
			return sdf.format(uploaded);
	}
}
