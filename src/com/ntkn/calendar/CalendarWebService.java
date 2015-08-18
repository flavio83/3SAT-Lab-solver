/**
 * CalendarWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.ntkn.calendar;

public interface CalendarWebService extends java.rmi.Remote {
    public com.ntkn.calendar.CalendarEventExt[] getEvents(java.lang.String start, java.lang.String end, java.lang.String type) throws java.rmi.RemoteException;
}
