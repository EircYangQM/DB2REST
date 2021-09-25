package com.db2rest.server.entity;

import java.util.List;

public class QueryResult {
  public ColumnInfo[] columns;
  public List<Object[]> results;

  public ColumnInfo[] getColumns() {
    return columns;
  }

  public void setColumns(ColumnInfo[] columns) {
    this.columns = columns;
  }
}
