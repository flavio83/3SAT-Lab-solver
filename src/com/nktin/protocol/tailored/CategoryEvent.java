package com.nktin.protocol.tailored;

import java.util.ArrayList;
import java.util.List;


public class CategoryEvent {
	
	String name;
	String description;
	int categoryId;
	
	String xmlName;
	
	List<CategoryEventField> fields = null;
	
	public CategoryEvent() {
		fields = new ArrayList<CategoryEventField>();
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

	public void addField(CategoryEventField field) {
		fields.add(field);
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
		sBuff.append("\r\n");
		for(CategoryEventField field : fields) {
			sBuff.append(field);
			sBuff.append("\r\n");
		}
		return sBuff.toString();
	}

}
