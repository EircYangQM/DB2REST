# DB2REST
The DB2REST uses to expose the database with REST API. 

# Install

Execute the following code.

```shell
git clone https://github.com/EircYangQM/DB2REST.git
cd DB2REST
mvn clean
mvn package
mvn spring-boot:run
```

# Usage

## list support database.

```shell
curl -XGET http://localhost:8080/api/jdbc/types

#result
{
  "data": [
    "mysql"
  ],
  "msg": "",
  "success": true
}
```

## Open connection.

```shell
curl -XPOST -H "Content-type: application/json" -d '{
  "type": "mysql",
  "properties":
  {
    "server": "localhost",
    "database": "sakila",
    "user": "root",
    "password": "abc"
  }
}' 'http://localhost:8080/api/jdbc/connect'

# Results
{
    "data": {
        "driverName": "MySQL Connector/J",
        "driverVersion": "mysql-connector-java-8.0.26 (Revision: 9aae1e450989d62c06616c1dcda3e404ef84df70)",
        "id": "51fba6ec-94e9-4af0-8761-1a967d66587b",
        "productVersion": "8.0.26"
    },
    "msg": "",
    "success": true
}
```

## List Tables

```shell
curl -XGET http://localhost:8080/api/jdbc/51fba6ec-94e9-4af0-8761-1a967d66587b/tables?catalog=sakila

# Result
{
    "data": [
        {
            "catalog": "sakila",
            "schema": "",
            "tableName": "actor"
        },
...
        {
            "catalog": "sakila",
            "schema": "",
            "tableName": "staff_list"
        }
    ],
    "msg": "",
    "success": true
}
```

## List Columns

```shell
curl -XGET http://localhost:8080/api/jdbc/51fba6ec-94e9-4af0-8761-1a967d66587b/columns -G -d catalog=sakila -d table=actor

# Result
{
    "data": [
        {
            "catalog": "sakila",
            "columnName": "actor_id",
            "columnSize": 5,
            "dataType": 5,
            "description": "",
            "isNull": true,
            "position": 1,
            "schema": "",
            "tableName": "actor",
            "typeName": "SMALLINT UNSIGNED"
        },
        ...
    ],
    "msg": "",
    "success": true
}
```

## Query

```shell
curl -XPOST -H "Content-type: application/json" -d '{
  "query": "select * from sakila.actor where actor_id=?",
  "params": [
      {
          "index": 1,
          "name": "actor_id",
          "value": 1
      }
  ]
}' 'http://localhost:8080/api/jdbc/bcf6167a-439f-4cb5-b91a-722938292b49/query'

# Result
{
    "data": {
        "columns": [
            {
                "catalog": "sakila",
                "columnName": "",
                "columnSize": 5,
                "dataType": 5,
                "description": "",
                "isNull": false,
                "position": 0,
                "schema": "",
                "tableName": "actor",
                "typeName": "actor"
            },
...
        ],
        "results": [
            [
                1,
                "PENELOPE",
                "GUINESS",
                1139949273000
            ]
        ]
    },
    "msg": "",
    "success": true
}
```