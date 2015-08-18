package com.nktin.protocol.tailored;




public class CategoryEventField {
	
	int fieldId;
	String short_description;
	String description;
	String unit;

	public CategoryEventField(int fieldId, String short_description, String description, String unit) {
		this.fieldId = fieldId;
		this.description = description;
		this.short_description = short_description;
		this.description = description;
		this.unit = unit;
	}

	public int getFieldId() {
		return fieldId;
	}

	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("\tdatum_id: ");
		s.append(fieldId);
		s.append("\r\n\tshort_description: ");
		s.append(short_description);
		s.append("\r\n\tdescription: ");
		s.append(description);
		s.append("\r\n\tdatum_type: ");
		s.append(unit);
		return s.toString();
	}

}
