package com.ntkn.util.apps;

import org.apache.commons.lang3.builder.ToStringStyle;




public class CustomToStringEasyStyle extends ToStringStyle {
	
	private static final long serialVersionUID = 1L;

	public CustomToStringEasyStyle() {
		setUseClassName(false);
		setUseIdentityHashCode(false);
		setUseShortClassName(false);
		setUseFieldNames(false);
	}

	protected void appendDetail(StringBuffer buffer, String fieldName, Object value) {
		buffer.append(value);
		buffer.append(" ");
	}
	
}