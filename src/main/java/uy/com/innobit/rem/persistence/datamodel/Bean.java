package uy.com.innobit.rem.persistence.datamodel;
import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Bean implements Serializable {

	  private static final long serialVersionUID = -2434964191410775775L;

	  /**
	   * Empty constructor.
	   */
	  public Bean() {
	  }

	  @Override
	  public boolean equals(Object obj) {
	    return EqualsBuilder.reflectionEquals(this, obj);
	  }

	  @Override
	  public int hashCode() {
	    return HashCodeBuilder.reflectionHashCode(this);
	  }

	  @Override
	public String toString() {
	    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	  }
	}
