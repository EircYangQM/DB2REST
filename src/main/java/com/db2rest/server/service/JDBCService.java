package com.db2rest.server.service;

import com.db2rest.server.entity.*;
import com.db2rest.server.service.jdbc.JDBCDb;
import com.db2rest.server.service.jdbc.MySQLDb;
import com.db2rest.server.utils.Utilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

public class JDBCService {
  private static Hashtable SUPPORTED_DBS;
  private static Hashtable<String, ConnectionInfo> CONNECTIONS;

  public List<String> listSupportDB() {
    List<String> dbs = new ArrayList<>();
    for (Object name: getSupportedDbs().keySet()) {
      dbs.add((String)name);
    }

    return dbs;
  }

  public ServerInfo connect(ConnectionInfo connectionInfo) throws Exception {
    JDBCDb db = null;
    ServerInfo serverInfo = null;
    try {
      db = connectInternal(connectionInfo);
      if (db == null) {
        throw new Exception(String.format("Connect db is failed."));
      }

      String connectionID = Utilities.toLowerCase(UUID.randomUUID().toString());
      this.saveConnectionInfo(connectionID, connectionInfo);
      serverInfo = db.getServerInfo();
      serverInfo.setId(connectionID);
    } finally {
      if (db != null) {
        db.disconnect();
      }
    }

    return serverInfo;
  }

  public List<TableInfo> getTables(String id, String catalog, String schema) throws Exception {
    JDBCDb db = null;
    List<TableInfo> tables;
    try {
      db = acquireConnection(id);
      if (db == null) {
        throw new Exception(String.format("Connect is null."));
      }

      tables = db.getTables(catalog, schema);
    } finally {
      db.disconnect();
    }

    return tables;
  }

  public List<ColumnInfo> getColumns(String id, String catalog, String schema, String tableName) throws Exception {
    JDBCDb db = null;
    List<ColumnInfo> columns;
    try {
      db = acquireConnection(id);
      if (db == null) {
        throw new Exception(String.format("Connect is null."));
      }

      columns = db.getColumns(catalog, schema, tableName);
    } finally {
      db.disconnect();
    }

    return columns;
  }

  public QueryResult executeQuery(String id, QueryInfo queryInfo) throws Exception {
    JDBCDb db = null;
    QueryResult resultSet;
    try {
      db = acquireConnection(id);
      if (db == null) {
        throw new Exception(String.format("Connect is null."));
      }

      resultSet = db.executeQuery(queryInfo);
    } finally {
      db.disconnect();
    }

    return resultSet;
  }

  private void saveConnectionInfo(String id, ConnectionInfo info) {
    if (CONNECTIONS == null) {
      CONNECTIONS = new Hashtable<>();
    }

    CONNECTIONS.put(id, info);
  }

  private JDBCDb acquireConnection(String id) throws Exception {
    JDBCDb db = null;
    if (CONNECTIONS == null) {
      CONNECTIONS = new Hashtable<>();
    } else {
      String key = Utilities.toLowerCase(id);
      if (CONNECTIONS.containsKey(key)) {
        ConnectionInfo info = (ConnectionInfo) CONNECTIONS.get(key);
        db = connectInternal(info);
      }
    }

    return db;
  }

  private JDBCDb connectInternal(ConnectionInfo connectionInfo) throws Exception {
    String key = connectionInfo.getType();
    if (key == null) {
      throw new Exception("The type is empty.");
    }

    key = key.toLowerCase();
    JDBCDb db = (JDBCDb) getSupportedDbs().get(key);
    if (db == null) {
      throw new Exception(String.format("Could not find the %s db.", key));
    }

    db.connect(connectionInfo);
    return db;
  }

  private Hashtable getSupportedDbs() {
    if (SUPPORTED_DBS == null) {
      SUPPORTED_DBS = new Hashtable();
      this.registerDb(MySQLDb.class);
    }

    return SUPPORTED_DBS;
  }

  private void registerDb(Class clazz) {
    try {
      JDBCDb instance = (JDBCDb)clazz.getDeclaredConstructor().newInstance();
      SUPPORTED_DBS.put(instance.getName(), instance);
    } catch (Exception ex) {

    }
  }
}
