/****************************************************
 * @file: SeparateChainingHashTable.java
 * @description:
 *      Implements a generic Separate Chaining Hash Table. Uses an array of LinkedLists to store elements and
 *      resolves collisions by chaining. Supports core hash table operations including insert, remove,
 *      search (contains), and rehashing when the load factor exceeds 1.0. Table size is maintained as a
 *      prime number to reduce clustering.
 * @acknowledgment: Portions of this code and documentation were developed with assistance from ChatGPT by OpenAI.
 * @author: Tim Hultman
 * @date: December 4, 2025
 ****************************************************/
import java.util.LinkedList;
import java.util.List;
public class SeparateChainingHashTable<AnyType> {
    /**
     * Default constructor.
     * Creates a hash table of default size (101).
     */
    public SeparateChainingHashTable() {
        this(DEFAULT_TABLE_SIZE);
    }
    /**
     * Constructs the hash table with a given initial size.
     * The actual table size is the next prime number ≥ size.
     *
     * @param size approximate initial number of buckets
     */
    public SeparateChainingHashTable(int size) {
        theLists = new LinkedList[nextPrime(size)];
        for (int i = 0; i < theLists.length; i++)
            theLists[i] = new LinkedList<>();
        currentSize = 0;
    }

    /**
     * Inserts an item into the hash table.
     * Duplicate elements are ignored.
     *
     * If load factor exceeds 1.0, the table rehashes
     * to double its size (next prime).
     *
     * @param x the item to insert
     */
    public void insert(AnyType x) {
        List<AnyType> whichList = theLists[myhash(x)];

        if (!whichList.contains(x)) {
            whichList.add(x);

            // rehash if load factor > 1
            if (++currentSize > theLists.length)
                rehash();
        }
    }

    /**
     * Removes an item from the hash table if it exists.
     *
     * @param x the item to remove
     */
    public void remove(AnyType x) {
        List<AnyType> whichList = theLists[myhash(x)];

        if (whichList.contains(x)) {
            whichList.remove(x);
            currentSize--;
        }
    }

    /**
     * Determines whether an item exists in the hash table.
     *
     * @param x the item to search for
     * @return true if found, false otherwise
     */
    public boolean contains(AnyType x) {
        List<AnyType> whichList = theLists[myhash(x)];
        return whichList.contains(x);
    }

    /**
     * Makes the table logically empty by clearing all chains.
     * Does not resize the table.
     */
    public void makeEmpty() {
        for (List<AnyType> list : theLists)
            list.clear();
        currentSize = 0;
    }

    /**
     * Rehashes the table.
     * Creates a new table of double the size (next prime),
     * and reinserts all existing elements into the new table.
     */
    private void rehash() {
        List<AnyType>[] oldLists = theLists;

        // create new table of double size
        theLists = new LinkedList[nextPrime(2 * oldLists.length)];
        for (int i = 0; i < theLists.length; i++)
            theLists[i] = new LinkedList<>();

        currentSize = 0;

        // reinsert everything
        for (List<AnyType> list : oldLists)
            for (AnyType x : list)
                insert(x);
    }
    /**
     * Internal hash function.
     * Computes the hash code and maps it into the table range.
     *
     * @param x the key to hash
     * @return bucket index for the key
     */
    private int myhash(AnyType x) {
        int hashVal = x.hashCode();
        hashVal %= theLists.length;
        if (hashVal < 0) hashVal += theLists.length;
        return hashVal;
    }

    private static final int DEFAULT_TABLE_SIZE = 101;
    /** Underlying array of chains */
    private List<AnyType>[] theLists;
    /** Number of items currently stored */
    private int currentSize;
    /**
     * Returns the next prime number ≥ n.
     *
     * @param n starting value
     * @return the next prime ≥ n
     */
    private static int nextPrime(int n) {
        if (n % 2 == 0)
            n++;

        for (; !isPrime(n); n += 2);
        return n;
    }
    /**
     * Tests whether a number is prime.
     * Used for sizing the hash table.
     *
     * @param n number to test
     * @return true if prime
     */
    private static boolean isPrime(int n) {
        if (n == 2 || n == 3)
            return true;

        if (n == 1 || n % 2 == 0)
            return false;

        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;

        return true;
    }
}
