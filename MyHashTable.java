/**
 * class MyHashTable. A simple HashTable. Handle collision by chain
 */
import java.util.ArrayList;

public class MyHashTable<K extends Comparable<K>, V>
{
    private ArrayList<MyHashEntry<K, V>> table;
    private int count; // how many elements in table

    /**
     * Constructor. Constructor an empty MyHashTable with given number of Buckets
     * @param tableSize The number of Buckets of the table
     */
    public MyHashTable(int tableSize){
        if(tableSize <= 0)
            tableSize = 10;          // guard against an invalid size
        table = new ArrayList<MyHashEntry<K, V>>(tableSize);
        for(int i = 0; i < tableSize; i++)
            table.add(null);         // every bucket starts as an empty chain
        count = 0;
    }

    /**
     * constructor. Construct an empty MyHashTable with capacity 10 buckets
     */
    public MyHashTable(){
        this(10);
    }

    /**
     * Map a key to a bucket index. Masking the sign bit keeps the
     * result non-negative even when hashCode() is negative.
     */
    private int hash(K key){
        return (key.hashCode() & 0x7fffffff) % table.size();
    }

    /**
     * get the number of elements in the table
     * @return the number of elements in the table
     */
    public int size(){
        return count;
    }

    /**
     * clear the table
     */
    public void clear(){
        for(int i = 0; i < table.size(); i++)
            table.set(i, null);
        count = 0;
    }

    /**
     * get the value with given key.
     * @param key The given key
     * @return the value that have the key matches the given key. If no such a value, return null
     */
    public V get(K key){
        MyHashEntry<K, V> current = table.get(hash(key));
        while(current != null){
            if(current.getKey().compareTo(key) == 0)
                return current.getValue();
            current = current.getNext();
        }
        return null;
    }

    /**
     * insert (key, value) pair into the table
     * If the key already exists, its value is replaced.
     * @param key The key of the pair
     * @param value The value of the pair
     */
    public void insert(K key, V value){
        int index = hash(key);
        MyHashEntry<K, V> current = table.get(index);
        while(current != null){
            if(current.getKey().compareTo(key) == 0){
                current.setValue(value);   // duplicate key: update, don't add
                return;
            }
            current = current.getNext();
        }
        MyHashEntry<K, V> entry = new MyHashEntry<K, V>(key, value);
        entry.setNext(table.get(index));   // new entry goes to the head of the chain
        table.set(index, entry);
        count++;
    }

    /**
     * remove the value with given key from the table
     * @param key The given key
     * @return the value whose key matches the given key. If no such value, null is returned
     */
    public V remove(K key){
        int index = hash(key);
        MyHashEntry<K, V> current = table.get(index);
        MyHashEntry<K, V> previous = null;
        while(current != null){
            if(current.getKey().compareTo(key) == 0){
                if(previous == null)
                    table.set(index, current.getNext());   // removing the head
                else
                    previous.setNext(current.getNext());   // bypass the node
                count--;
                return current.getValue();
            }
            previous = current;
            current = current.getNext();
        }
        return null;
    }

    /**
     * check if the table is empty,i.e. no entry
     * @return true if the table holds no elements; false otherwise
     */
    public boolean isEmpty(){
        return count == 0;
    }

    /**
     * return a String representation of the table
     * @return a String representation of the table
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < table.size(); i++){
            sb.append("[").append(i).append("]: ");
            MyHashEntry<K, V> current = table.get(i);
            while(current != null){
                sb.append("(").append(current.getKey()).append(", ")
                  .append(current.getValue()).append(")");
                if(current.getNext() != null)
                    sb.append(" -> ");
                current = current.getNext();
            }
            if(i < table.size() - 1)
                sb.append("\n");
        }
        return sb.toString();
    }
}
