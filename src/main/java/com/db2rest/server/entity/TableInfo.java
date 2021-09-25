package com.db2rest.server.entity;

public class TableInfo {
  private String schema;
  private String catalog;
  private String tableName;

  public TableInfo() {}

  public TableInfo(String schema, String catalog, String tableName) {
    this.schema = schema;
    this.catalog = catalog;
    this.tableName = tableName;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getCatalog() {
    return catalog;
  }

  public void setCatalog(String catalog) {
    this.catalog = catalog;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}
