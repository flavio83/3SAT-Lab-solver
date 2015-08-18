package com.ntkn.calendar;

public class CalendarWebServiceProxy implements com.ntkn.calendar.CalendarWebService {
  private String _endpoint = null;
  private com.ntkn.calendar.CalendarWebService calendarWebService = null;
  
  public CalendarWebServiceProxy() {
    _initCalendarWebServiceProxy();
  }
  
  public CalendarWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initCalendarWebServiceProxy();
  }
  
  private void _initCalendarWebServiceProxy() {
    try {
      calendarWebService = (new com.ntkn.calendar.CalendarWebServiceServiceLocator()).getCalendarWebServicePort();
      if (calendarWebService != null) {
        if (_endpoint != null) {
          ((javax.xml.rpc.Stub)calendarWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
          ((javax.xml.rpc.Stub)calendarWebService)._setProperty(javax.xml.rpc.Stub.USERNAME_PROPERTY, "marchifl");
          ((javax.xml.rpc.Stub)calendarWebService)._setProperty(javax.xml.rpc.Stub.PASSWORD_PROPERTY, "b*76*CH9ab");
        } else
          _endpoint = (String)((javax.xml.rpc.Stub)calendarWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (calendarWebService != null)
      ((javax.xml.rpc.Stub)calendarWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.ntkn.calendar.CalendarWebService getCalendarWebService() {
    if (calendarWebService == null)
      _initCalendarWebServiceProxy();
    return calendarWebService;
  }
  
  public com.ntkn.calendar.CalendarEventExt[] getEvents(java.lang.String start, java.lang.String end, java.lang.String type) throws java.rmi.RemoteException{
    if (calendarWebService == null)
      _initCalendarWebServiceProxy();
    return calendarWebService.getEvents(start, end, type);
  }
  
  
}