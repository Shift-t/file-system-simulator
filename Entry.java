//Class for Entries stored in the hashtable
public class Entry {
    private String key;
    private Node value;

    public Entry(String key, Node value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }
    public Node getValue() {
        return value;
    }
    public void setValue(Node value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }
}
