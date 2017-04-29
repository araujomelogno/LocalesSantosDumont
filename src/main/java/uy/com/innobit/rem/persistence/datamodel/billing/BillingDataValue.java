package uy.com.innobit.rem.persistence.datamodel.billing;

public class BillingDataValue {
	private String init;
	private String end;
	private String property;
	private String occupant;
	private String owner;

	private Integer amount;
	private Integer ownerCommission;
	private Integer occupantComission;
	private Integer ownerComissionCharged;
	private Integer occupantComissionCharged;
	private Integer amountPaied;

	public String getInit() {
		return init;
	}

	public void setInit(String init) {
		this.init = init;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getOccupant() {
		return occupant;
	}

	public void setOccupant(String occupant) {
		this.occupant = occupant;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getOwnerCommission() {
		return ownerCommission;
	}

	public void setOwnerCommission(Integer ownerCommission) {
		this.ownerCommission = ownerCommission;
	}

	public Integer getOccupantComission() {
		return occupantComission;
	}

	public void setOccupantComission(Integer occupantComission) {
		this.occupantComission = occupantComission;
	}

	public Integer getOwnerComissionCharged() {
		return ownerComissionCharged;
	}

	public void setOwnerComissionCharged(Integer ownerComissionCharged) {
		this.ownerComissionCharged = ownerComissionCharged;
	}

	public Integer getOccupantComissionCharged() {
		return occupantComissionCharged;
	}

	public void setOccupantComissionCharged(Integer occupantComissionCharged) {
		this.occupantComissionCharged = occupantComissionCharged;
	}

	public Integer getAmountPaied() {
		return amountPaied;
	}

	public void setAmountPaied(Integer amountPaied) {
		this.amountPaied = amountPaied;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

}
