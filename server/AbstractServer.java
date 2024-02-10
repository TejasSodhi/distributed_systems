package server;

public abstract class AbstractServer implements IServer {
  private static final KeyValueStore keyValueStore = new KeyValueStore();

  public String processRequest(String inputLine) {
    String[] tokens = inputLine.split(" ");
    if (tokens.length < 2) {
      return "Invalid request format";
    }

    String operation = tokens[0];
    String key = tokens[1];
    String value = tokens.length > 2 ? tokens[2] : null;

    switch (operation.toUpperCase()) {
      case "PUT":
        if (value == null) {
          return "PUT operation requires a value";
        }
        keyValueStore.put(key, value);
        return "Key '" + key + "' stored with value '" + value + "'";
      case "GET":
        String storedValue = keyValueStore.get(key);
        return (storedValue != null) ? "Value for key '" + key + "': " + storedValue : "Key '" + key + "' not found";
      case "DELETE":
        String removedValue = keyValueStore.delete(key);
        return (removedValue != null) ? "Deleted key '" + key + "' with value '" + removedValue + "'" : "Key '" + key + "' not found";
      default:
        return "Unsupported operation: " + operation;
    }
  }
}
