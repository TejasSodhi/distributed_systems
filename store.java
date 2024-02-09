import java.util.HashMap;

public class KeyValueStore {
    private HashMap<String, String> store;

    public KeyValueStore() {
        this.store = new HashMap<>();
    }

    public synchronized void put(String key, String value) {
        store.put(key, value);
    }

    public synchronized String get(String key) {
        return store.get(key);
    }

    public synchronized String delete(String key) {
        return store.remove(key);
    }
}
