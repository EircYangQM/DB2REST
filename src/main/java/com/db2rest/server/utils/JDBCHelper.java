package com.db2rest.server.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCHelper {
  public static void executeNonQuery(Connection connection, String query, Object[] parameters) throws Exception {
    PreparedStatement statement = null;
    try {
      statement = connection.prepareStatement(query);
      if (parameters != null) {
        for (int i = 1; i <= parameters.length; i++) {
          statement.setObject(i, parameters[i - 1]);
        }
      }
      statement.execute();
    } finally {
      JDBCHelper.releaseStatement(statement);
    }
  }

  public static List<Object[]> executeMultiRow(Connection connection, String query, Object[] parameters, String[] columnName) throws Exception {
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    List<Object[]> list = new ArrayList<Object[]>();
    try {
      statement = connection.prepareStatement(query);
      if (parameters != null) {
        for (int i = 1; i <= parameters.length; i++) {
          statement.setObject(i, parameters[i - 1]);
        }
      }
      resultSet = statement.executeQuery();
      int[] indexes = new int[columnName.length];
      ResultSetMetaData metaData = resultSet.getMetaData();
      for (int i = 0; i < metaData.getColumnCount(); i++) {
        for (int j = 0; j < columnName.length; j++) {
          if (columnName[j].equalsIgnoreCase(metaData.getColumnLabel(i + 1))) {
            indexes[j] = i + 1;
          }
        }
      }
      while (resultSet.next()) {
        Object[] values = new Object[columnName.length];
        for (int i = 0; i < indexes.length; i++) {
          if (indexes[i] >= 1) {
            values[i] = resultSet.getObject(indexes[i]);
          }
        }

        list.add(values);
      }
    } finally {
      if (resultSet != null) {
        try {
          resultSet.close();
        } catch (Exception ex) {
          //DO nothing.
        }
      }

      if (statement != null) {
        try {
          statement.close();
        } catch (Exception e) {
          //DO nothing.
        }
      }
    }

    return list;
  }

  public static Object[] executeSingleRow(Connection connection, String query, Object[] parameters, String[] columnName) throws Exception {
    List<Object[]> list = executeMultiRow(connection, query, parameters, columnName);
    if (list.size() >= 1) {
      return list.get(0);
    } else {
      return new Object[columnName.length];
    }
  }

  public static void releaseStatement(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (Exception ex) {
        //Do nothing
      }
    }
  }

  public static void releaseResultSet(ResultSet resultSet) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (Exception ex) {
        //Do nothing
      }
    }
  }

  public static void releaseConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (Exception ex) {
        //Do nothing
      }
    }
  }

  public static String quotaIdentity(String identity, char startQuota, char endQuota) {
    StringBuilder builder = new StringBuilder();
    builder.append(startQuota);
    int length = identity.length();
    char ch;
    for (int i = 0; i < length; i++) {
      ch = identity.charAt(i);
      builder.append(ch);
      if (ch == endQuota) {
        builder.append(ch);
      }
    }

    builder.append(endQuota);
    return builder.toString();
  }
}
