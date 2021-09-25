package com.db2rest.server.utils;

import java.util.ArrayList;
import java.util.List;

public class CommandLine {
  private static final String NULL_VALUE = "<NULL_VALUE>";
  private List<Argument> argsList;
  private String[] values;
  private String command;
  private String invalidName;

  public CommandLine(String command) {
    this.command = command;
    this.argsList = new ArrayList<Argument>();
  }

  public CommandLine append(String shortName, String longName, boolean isRequired, String description) {
    argsList.add(new Argument(shortName, longName, isRequired, description));
    return this;
  }

  public boolean accept(String[] args) {
    if (!parseArgs(args)) {
      return false;
    }

    return validRequired();
  }

  public boolean contains(String arg) {
    int index = findIndex(arg);
    if (index == -1) {
      return false;
    }

    String value = this.values[index];
    return value != null;
  }

  public String getValue(String arg) {
    int index = findIndex(arg);
    if (index == -1) {
      return null;
    }

    String value = this.values[index];
    return value == NULL_VALUE ? null : value;
  }

  public void print() {
    StringBuilder builder = new StringBuilder();
    builder.append("Invalid parameter ").append(this.invalidName).append(System.lineSeparator());;
    builder.append(command + " --usage").append(System.lineSeparator());
    for (Argument arg: this.argsList) {
      builder.append("    ").append("-").append(arg.ShortName).append("\t")
          .append("--").append(Utilities.fixedLengthString(arg.LongName, 12, true)).append("\t")
          .append(arg.Description).append(System.lineSeparator());
    }
    System.out.println(builder.toString());
  }

  private boolean parseArgs(String[] args) {
    this.values = new String[argsList.size()];
    String key = null;
    for (int i = 0; i < args.length; i++) {
      String str = args[i];
      if (str.startsWith("-")) {
        if (key != null) {
          if(!this.addValue(key, null)) {
            this.invalidName = key;
            return false;
          }
        }

        key = str.toLowerCase();
      } else {
        if (key != null) {
          if(!this.addValue(key, str)) {
            this.invalidName = key;
            return false;
          }
          key = null;
        }
      }
    }

    if (key != null) {
      if(!this.addValue(key, null)) {
        this.invalidName = key;
        return false;
      }
    }

    return true;
  }

  private boolean validRequired() {
    for (int i = 0; i < this.argsList.size(); i++) {
      Argument argument = this.argsList.get(i);
      if (argument.IsRequired) {
        if (this.values[i] == null) {
          this.invalidName = argument.LongName + " is required";
          return false;
        }
      }
    }

    return true;
  }

  private int findIndex(String name) {
    for (int i = 0; i < this.argsList.size(); i++) {
      Argument argument = this.argsList.get(i);
      if (Utilities.equalsIgnoreCase(name, argument.ShortName)) {
        return i;
      }
      if (Utilities.equalsIgnoreCase(name, argument.LongName)) {
        return i;
      }
    }

    return -1;
  }

  private boolean addValue(String nameWithPrefix, String value) {
    if (nameWithPrefix.startsWith("--")) {
      return this.addValue(null, nameWithPrefix.substring(2), value);
    } else if (nameWithPrefix.startsWith("-")){
      return this.addValue(nameWithPrefix.substring(1), null, value);
    } else {
      return false;
    }
  }

  private boolean addValue(String shortName, String longName, String value) {
    boolean isMatched = false;
    for (int i = 0; i < this.argsList.size(); i++) {
      Argument argument = this.argsList.get(i);
      if (shortName != null) {
        if (Utilities.equalsIgnoreCase(shortName, argument.ShortName)) {
          this.values[i] = value == null ? NULL_VALUE : value;
          isMatched = true;
        }
      } else if (longName != null) {
        if (Utilities.equalsIgnoreCase(longName, argument.LongName)) {
          this.values[i] = value == null ? NULL_VALUE : value;
          isMatched = true;
        }
      }
    }

    return isMatched;
  }
}

class Argument {
  public String ShortName;
  public String LongName;
  public String Description;
  public boolean IsRequired;

  public Argument(String shortName, String longName, boolean isRequired, String description) {
    this.ShortName = shortName;
    this.LongName = longName;
    this.IsRequired = isRequired;
    this.Description = description;
  }
}
