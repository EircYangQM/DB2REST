package com.db2rest.server.service.jdbc;

import com.db2rest.server.entity.ConnectionInfo;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MySQLDb extends JDBCDb{
  @Override
  public String getName() {
    return "mysql";
  }

  @Override
  public String getDriver() {
    return "com.mysql.cj.jdbc.Driver";
  }

  @Override
  public void connect(ConnectionInfo info) throws Exception {
    Hashtable hashtable = (Hashtable) info.getProperties().clone();
    this.validateProperties(hashtable);
    StringBuilder builder = new StringBuilder();
    builder.append("jdbc:mysql://");
    String server = (String) hashtable.get("server");
    hashtable.remove("server");
    builder.append(server);
    String port = (String) hashtable.get("port");
    if (port != null && !"".equals(port)){
      builder.append(port);
      hashtable.remove("port");
    }
    String database = (String) hashtable.get("database");
    hashtable.remove("database");
    builder.append("/").append(database);
    builder.append("?");
    boolean isFirst = true;
    for (Object key: hashtable.keySet()) {
      if (isFirst) {
        isFirst = false;
      } else {
        builder.append("&");
      }
      builder.append(key).append("=").append(hashtable.get(key));
    }

    Class.forName(this.getDriver());
    this.connection = DriverManager.getConnection(builder.toString());
  }

  @Override
  protected List<String> getRequiredProperties() {
    List<String> properties = new ArrayList<>();
    properties.add("server");
    properties.add("database");
    properties.add("user");
    properties.add("password");
    return properties;
  }
}
