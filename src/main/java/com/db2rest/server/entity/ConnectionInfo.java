package com.db2rest.server.entity;

import java.util.Hashtable;

public class ConnectionInfo {
  private String type;
  private Hashtable properties;

  public Hashtable getProperties() {
    return properties;
  }

  public void setProperties(Hashtable properties) {
    this.properties = properties;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
