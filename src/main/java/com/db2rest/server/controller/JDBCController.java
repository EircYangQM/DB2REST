package com.db2rest.server.controller;

import com.db2rest.server.entity.*;
import com.db2rest.server.service.JDBCService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JDBCController {
  private JDBCService service;

  public JDBCController () {
    this.service = new JDBCService();
  }

  @GetMapping("/api/jdbc/types")
  public JsonResult<List<String>> getTypes() {
    return new JsonResult<List<String>>(this.service.listSupportDB());
  }

  @PostMapping("/api/jdbc/connect")
  public JsonResult<ServerInfo> connect(@RequestBody ConnectionInfo info) {
    try {
      return new JsonResult<ServerInfo>(this.service.connect(info));
    } catch (Exception ex) {
      return new JsonResult<>(false, "Connection is failed. Error:" + ex.getMessage());
    }
  }

  @GetMapping("/api/jdbc/{id}/tables")
  public JsonResult<List<TableInfo>> getTables(@PathVariable String id, @RequestParam(required = false, name = "catalog") String catalog, @RequestParam(required = false, name = "schema") String schema) {
    try {
      return new JsonResult<List<TableInfo>>(this.service.getTables(id, catalog, schema));
    } catch (Exception ex) {
      return new JsonResult<>(false, "Connection is failed. Error:" + ex.getMessage());
    }
  }

  @GetMapping("/api/jdbc/{id}/columns")
  public JsonResult<List<ColumnInfo>> getColumns(@PathVariable String id, @RequestParam String table, @RequestParam(required = false) String catalog, @RequestParam(required = false) String schema) {
    try {
      return new JsonResult<List<ColumnInfo>>(this.service.getColumns(id, catalog, schema, table));
    } catch (Exception ex) {
      return new JsonResult<>(false, "Connection is failed. Error:" + ex.getMessage());
    }
  }

  @PostMapping("/api/jdbc/{id}/query")
  public JsonResult<QueryResult> executeQuery(@PathVariable String id, @RequestBody QueryInfo queryInfo) {
    try {
      return new JsonResult<QueryResult>(this.service.executeQuery(id, queryInfo));
    } catch (Exception ex) {
      return new JsonResult<>(false, "Connection is failed. Error:" + ex.getMessage());
    }
  }
}
