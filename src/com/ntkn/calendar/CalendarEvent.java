/**
 * CalendarEvent.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ntkn.calendar;

public class CalendarEvent  implements java.io.Serializable {
    private java.lang.String addedBy;

    private int categoryId;

    private com.ntkn.calendar.CalendarEventDate date;

    private java.lang.String title;

    private com.ntkn.calendar.CalendarEventType type;

    private java.lang.String uid;

    public CalendarEvent() {
    }

    public CalendarEvent(
           java.lang.String addedBy,
           int categoryId,
           com.ntkn.calendar.CalendarEventDate date,
           java.lang.String title,
           com.ntkn.calendar.CalendarEventType type,
           java.lang.String uid) {
           this.addedBy = addedBy;
           this.categoryId = categoryId;
           this.date = date;
           this.title = title;
           this.type = type;
           this.uid = uid;
    }


    /**
     * Gets the addedBy value for this CalendarEvent.
     * 
     * @return addedBy
     */
    public java.lang.String getAddedBy() {
        return addedBy;
    }


    /**
     * Sets the addedBy value for this CalendarEvent.
     * 
     * @param addedBy
     */
    public void setAddedBy(java.lang.String addedBy) {
        this.addedBy = addedBy;
    }


    /**
     * Gets the categoryId value for this CalendarEvent.
     * 
     * @return categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }


    /**
     * Sets the categoryId value for this CalendarEvent.
     * 
     * @param categoryId
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }


    /**
     * Gets the date value for this CalendarEvent.
     * 
     * @return date
     */
    public com.ntkn.calendar.CalendarEventDate getDate() {
        return date;
    }


    /**
     * Sets the date value for this CalendarEvent.
     * 
     * @param date
     */
    public void setDate(com.ntkn.calendar.CalendarEventDate date) {
        this.date = date;
    }


    /**
     * Gets the title value for this CalendarEvent.
     * 
     * @return title
     */
    public java.lang.String getTitle() {
        return title;
    }


    /**
     * Sets the title value for this CalendarEvent.
     * 
     * @param title
     */
    public void setTitle(java.lang.String title) {
        this.title = title;
    }


    /**
     * Gets the type value for this CalendarEvent.
     * 
     * @return type
     */
    public com.ntkn.calendar.CalendarEventType getType() {
        return type;
    }


    /**
     * Sets the type value for this CalendarEvent.
     * 
     * @param type
     */
    public void setType(com.ntkn.calendar.CalendarEventType type) {
        this.type = type;
    }


    /**
     * Gets the uid value for this CalendarEvent.
     * 
     * @return uid
     */
    public java.lang.String getUid() {
        return uid;
    }


    /**
     * Sets the uid value for this CalendarEvent.
     * 
     * @param uid
     */
    public void setUid(java.lang.String uid) {
        this.uid = uid;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CalendarEvent)) return false;
        CalendarEvent other = (CalendarEvent) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.addedBy==null && other.getAddedBy()==null) || 
             (this.addedBy!=null &&
              this.addedBy.equals(other.getAddedBy()))) &&
            this.categoryId == other.getCategoryId() &&
            ((this.date==null && other.getDate()==null) || 
             (this.date!=null &&
              this.date.equals(other.getDate()))) &&
            ((this.title==null && other.getTitle()==null) || 
             (this.title!=null &&
              this.title.equals(other.getTitle()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.uid==null && other.getUid()==null) || 
             (this.uid!=null &&
              this.uid.equals(other.getUid())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAddedBy() != null) {
            _hashCode += getAddedBy().hashCode();
        }
        _hashCode += getCategoryId();
        if (getDate() != null) {
            _hashCode += getDate().hashCode();
        }
        if (getTitle() != null) {
            _hashCode += getTitle().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getUid() != null) {
            _hashCode += getUid().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CalendarEvent.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://calendar.ntkn.com/", "calendarEvent"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addedBy");
        elemField.setXmlName(new javax.xml.namespace.QName("", "addedBy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "categoryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("date");
        elemField.setXmlName(new javax.xml.namespace.QName("", "date"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://calendar.ntkn.com/", "calendarEventDate"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("title");
        elemField.setXmlName(new javax.xml.namespace.QName("", "title"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://calendar.ntkn.com/", "calendarEventType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("uid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "uid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
