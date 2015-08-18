/**
 * CalendarWebServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ntkn.calendar;

public class CalendarWebServiceServiceLocator extends org.apache.axis.client.Service implements com.ntkn.calendar.CalendarWebServiceService {

    public CalendarWebServiceServiceLocator() {
    }


    public CalendarWebServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CalendarWebServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CalendarWebServicePort
    private java.lang.String CalendarWebServicePort_address = "http://www.alphaflash.com/calendarservice/soap";

    public java.lang.String getCalendarWebServicePortAddress() {
        return CalendarWebServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CalendarWebServicePortWSDDServiceName = "CalendarWebServicePort";

    public java.lang.String getCalendarWebServicePortWSDDServiceName() {
        return CalendarWebServicePortWSDDServiceName;
    }

    public void setCalendarWebServicePortWSDDServiceName(java.lang.String name) {
        CalendarWebServicePortWSDDServiceName = name;
    }

    public com.ntkn.calendar.CalendarWebService getCalendarWebServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CalendarWebServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCalendarWebServicePort(endpoint);
    }

    public com.ntkn.calendar.CalendarWebService getCalendarWebServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.ntkn.calendar.CalendarWebServiceServiceSoapBindingStub _stub = new com.ntkn.calendar.CalendarWebServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getCalendarWebServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCalendarWebServicePortEndpointAddress(java.lang.String address) {
        CalendarWebServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.ntkn.calendar.CalendarWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.ntkn.calendar.CalendarWebServiceServiceSoapBindingStub _stub = new com.ntkn.calendar.CalendarWebServiceServiceSoapBindingStub(new java.net.URL(CalendarWebServicePort_address), this);
                _stub.setPortName(getCalendarWebServicePortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CalendarWebServicePort".equals(inputPortName)) {
            return getCalendarWebServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://calendar.ntkn.com/", "CalendarWebServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://calendar.ntkn.com/", "CalendarWebServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CalendarWebServicePort".equals(portName)) {
            setCalendarWebServicePortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
