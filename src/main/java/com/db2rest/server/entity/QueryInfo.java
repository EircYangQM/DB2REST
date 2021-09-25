package com.db2rest.server.entity;

import java.util.List;

public class QueryInfo {
  private String query;
  private List<Param> params;

  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
  }
}
