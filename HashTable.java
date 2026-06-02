import java.util.ArrayList;

//Hashtable implementation for directories to store children
//Uses separate chaining
public class HashTable {
    private SLL<Entry>[] table;
    private int capacity;

    //Constructor Method
    public HashTable(int capacity) {
        this.capacity = capacity;
        table = new SLL[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new SLL<>();
        }
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % capacity; //built in hashcode generator, might change to custom
    }

    //Inserts Entry into the Hash Table
    public void insert(String key, Node value){
        int index = hash(key);
        SLL<Entry> list = table[index];

        SLLNode<Entry> current = list.head;

        //searches to see if the key already exists
        while(current != null){
            if (current.info.getKey().equals(key)){
                current.info.setValue(value);  //if key exists update value
                return;
            }
            current = current.next;
        }

        list.addToTail(new Entry(key, value));
    }

    //Searches for a given key in the hash table
    public Node search(String key){
        int index = hash(key);
        SLL<Entry> list = table[index];

        //Search loop for SLL at the hash value of the key
        SLLNode<Entry> current = list.head;
        while(current != null){
            if (current.info.getKey().equals(key)){
                return current.info.getValue();
            }
            current = current.next;
        }
        return null;
    }

    //Removes a key from the hash table
    public void remove(String key){
        int index = hash(key);
        SLL<Entry> list = table[index];

        SLLNode<Entry> current = list.head;
        SLLNode<Entry> prev = null;

        //Loops through the SLL at hash value of the key
        while(current != null){
            if (current.info.getKey().equals(key)){
                if (prev == null){
                    list.head = current.next;
                    if (list.tail == current){
                        list.tail = null;
                    }
                }
                else{
                    prev.next = current.next;
                    if (list.tail == current){
                        list.tail = prev;
                    }
                }
                return;
            }
            prev = current;
            current = current.next;
        }
    }

    //Checks if a specific key is in the hashtable
    public boolean containsKey(String key){
        return search(key) != null;
    }

    //Returns a SLL of all the keys in the hashtable
    public SLL<String> keys(){
        SLL<String> keySet = new SLL<>();
        for (int i = 0; i < table.length; i++) {
            SLL<Entry> list = table[i];
            SLLNode<Entry> current = list.head;

            while(current != null) {
                keySet.addToTail(current.info.getKey());
                current = current.next;
            }
        }
        return keySet;
    }

    //Returns an array list containing all the nodes in the hashtable
    public ArrayList<Node> getNodes() {
        ArrayList<Node> valuesList = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            SLL<Entry> list = table[i];
            SLLNode<Entry> current = list.head;

            while(current != null) {
                valuesList.add(current.info.getValue());
                current = current.next;
            }
        }
        return valuesList;
    }

}
