package com.db2rest.server.service.jdbc;

import com.db2rest.server.entity.*;
import com.db2rest.server.utils.JDBCHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public abstract class JDBCDb {
  protected Connection connection;
  public abstract String getName();
  public abstract String getDriver();
  public abstract void connect(ConnectionInfo info) throws Exception;
  public void disconnect() throws Exception {
    if (this.connection != null) {
      this.connection.close();
    }
  }
  public ServerInfo getServerInfo() throws Exception {
    ServerInfo serverInfo = new ServerInfo();
    serverInfo.setProductVersion(this.connection.getMetaData().getDatabaseProductVersion());
    serverInfo.setDriverVersion(this.connection.getMetaData().getDriverVersion());
    serverInfo.setDriverName(this.connection.getMetaData().getDriverName());
    return serverInfo;
  }
  public List<TableInfo> getTables(String catalog, String schema) throws Exception {
    ResultSet resultSet = null;
    List<TableInfo> tables = new ArrayList<>();
    try {
      resultSet = this.connection.getMetaData().getTables(catalog, schema, null, null);
      while (resultSet.next()) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setCatalog(resultSet.getString("TABLE_CAT"));
        tableInfo.setSchema(resultSet.getString("TABLE_SCHEM"));
        tableInfo.setTableName(resultSet.getString("TABLE_NAME"));
        tables.add(tableInfo);
      }
    } finally {
      JDBCHelper.releaseResultSet(resultSet);
      JDBCHelper.releaseConnection(connection);
    }

    return tables;
  }

  public List<ColumnInfo> getColumns(String catalog, String schema, String tableName) throws Exception {
    ResultSet resultSet = null;
    List<ColumnInfo> columns = new ArrayList<>();
    try {
      resultSet = this.connection.getMetaData().getColumns(catalog, schema, tableName, null);
      while (resultSet.next()) {
        ColumnInfo column = new ColumnInfo();
        column.setCatalog(resultSet.getString("TABLE_CAT"));
        column.setSchema(resultSet.getString("TABLE_SCHEM"));
        column.setTableName(resultSet.getString("TABLE_NAME"));
        column.setColumnName(resultSet.getString("COLUMN_NAME"));
        column.setDataType(resultSet.getInt("DATA_TYPE"));
        column.setTypeName(resultSet.getString("TYPE_NAME"));
        column.setColumnSize(resultSet.getInt("COLUMN_SIZE"));
        column.setIsNull(resultSet.getInt("NULLABLE") != DatabaseMetaData.columnNullable);
        column.setPosition(resultSet.getInt("ORDINAL_POSITION"));
        column.setDescription(resultSet.getString("REMARKS"));
        columns.add(column);
      }
    } finally {
      JDBCHelper.releaseResultSet(resultSet);
      JDBCHelper.releaseConnection(connection);
    }

    return columns;
  }

  public QueryResult executeQuery(QueryInfo queryInfo) throws Exception {
    ResultSet resultSet = null;
    PreparedStatement statement = null;
    QueryResult result = new QueryResult();
    try {
      statement = this.connection.prepareStatement(queryInfo.getQuery());
      List<Param> params = queryInfo.getParams();
      if (params != null) {
        for (int i = 0; i < params.size(); i++) {
          Param param = params.get(i);
          statement.setObject(param.index, param.value);
        }
      }
      resultSet = statement.executeQuery();
      ResultSetMetaData metaData = resultSet.getMetaData();
      ColumnInfo[] columnInfos = new ColumnInfo[metaData.getColumnCount()];
      for (int i = 0; i < metaData.getColumnCount(); i++) {
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setCatalog(metaData.getCatalogName(i + 1));
        columnInfo.setSchema(metaData.getSchemaName(i + 1));
        columnInfo.setTableName(metaData.getTableName(i + 1));
        columnInfo.setDataType(metaData.getColumnType(i + 1));
        columnInfo.setTypeName(metaData.getTableName(i + 1));
        columnInfo.setColumnSize(metaData.getColumnDisplaySize(i + 1));
        columnInfo.setIsNull(metaData.isNullable(i + 1) != DatabaseMetaData.columnNoNulls);
        columnInfos[i] = columnInfo;
      }
      List<Object[]> values = new ArrayList<>();
      while (resultSet.next()) {
        Object[] row = new Object[columnInfos.length];
        for (int i = 0; i < columnInfos.length; i++) {
          row[i] = resultSet.getObject(i + 1);
        }
        values.add(row);
      }

      result.results = values;
      result.columns = columnInfos;
    } finally {
      JDBCHelper.releaseResultSet(resultSet);
      JDBCHelper.releaseConnection(connection);
    }

    return result;
  }

  protected abstract List<String> getRequiredProperties();

  protected void validateProperties(Hashtable properties) throws Exception{
    List<String> requiredProperties = getRequiredProperties();
    for (String property: requiredProperties) {
      if(!properties.containsKey(property)) {
        throw new Exception(String.format("The %s is required.", property));
      }
    }
  }
}
