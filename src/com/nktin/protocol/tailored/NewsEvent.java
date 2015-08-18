package com.nktin.protocol.tailored;


public class NewsEvent {
	
	String name;
	String description;
	int categoryId;
	
	String xmlName;

	public NewsEvent() {
		// TODO Auto-generated constructor stub
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public void addField(int id, String description, String unit) {
		
	}

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}
	
	public String toString() {
		StringBuilder sBuff = new StringBuilder();
		sBuff.append(categoryId);
		sBuff.append(" ");
		sBuff.append(name);
		return sBuff.toString();
	}

}
