package com.db2rest.server.entity;

public class ColumnInfo {
  private String schema;
  private String catalog;
  private String tableName;
  private String columnName;
  private int dataType;
  private String typeName;
  private int columnSize;
  private boolean isNull;
  private int position;
  private String description;

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

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public int getDataType() {
    return dataType;
  }

  public void setDataType(int dataType) {
    this.dataType = dataType;
  }

  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  public int getColumnSize() {
    return columnSize;
  }

  public void setColumnSize(int columnSize) {
    this.columnSize = columnSize;
  }

  public boolean getIsNull() {
    return isNull;
  }

  public void setIsNull(boolean aNull) {
    isNull = aNull;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
