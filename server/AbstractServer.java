package server;

public abstract class AbstractServer implements IServer {
  private static final KeyValueStore keyValueStore = new KeyValueStore();
  static final ServerLogger serverLogger = new ServerLogger();

  public String processRequest(String inputLine) {
    String[] tokens = inputLine.split("::");
    System.out.println(inputLine);
    if (tokens.length < 3) {
      return "Invalid request format";
    }

    String requestId = tokens[0];
    String operation = tokens[1];
    String key = tokens[2];
    String value = tokens.length > 3 ? tokens[3] : null;

    switch (operation.toUpperCase()) {
      case "PUT":
        if (value == null) {
          return requestId + ": PUT operation requires a value";
        }
        keyValueStore.put(key, value);
        return requestId + ": Key '" + key + "' stored with value '" + value + "'";
      case "GET":
        String storedValue = keyValueStore.get(key);
        return (storedValue != null) ? requestId + ": Value for key '" + key + "': " + storedValue : "Key '" + key + "' not found";
      case "DELETE":
        String removedValue = keyValueStore.delete(key);
        return (removedValue != null) ? requestId + ": Deleted key '" + key + "' with value '" + removedValue + "'" : "Key '" + key + "' not found";
      default:
        return requestId + ": Unsupported operation: " + operation;
    }
  }
}
