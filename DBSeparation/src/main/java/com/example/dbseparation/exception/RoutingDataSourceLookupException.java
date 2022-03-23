package com.example.dbseparation.exception;

public class RoutingDataSourceLookupException extends CustomRuntimeException {

  public RoutingDataSourceLookupException(String message) {
    super(message);
  }

  public RoutingDataSourceLookupException(String message, Throwable cause) {
    super(message, cause);
  }

}
