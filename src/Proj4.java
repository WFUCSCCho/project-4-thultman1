/****************************************************
 * @file: Proj4.java
 * @description:
 *      Main driver for Project 4: Hash Tables. This program loads movie data from a CSV file, applies sorted, shuffled, and reversed orderings
 *      to the dataset, and measures the running time of insert, search, and delete operations in a Separate Chaining Hash Table.
 *
 *      The program prints human-readable timing results to the console and also appends CSV-formatted timing data to analysis.txt for later graphing.
 * @acknowledgment: Portions of this code and documentation were developed with assistance from ChatGPT by OpenAI.
 * @author: Tim Hultman
 * @date: December 4, 2025
 ****************************************************/
import java.io.*;
import java.util.*;

public class Proj4 {

    /**
     * Main method.
     * Reads command line arguments, loads movie data,
     * and performs hash table timing tests on sorted,
     * shuffled, and reversed versions of the dataset.
     *
     * @param args command-line arguments:
     *             args[0] = dataset filename
     *             args[1] = number of lines to read
     * @throws IOException if file reading or writing fails
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Usage: java Proj4 <dataset file> <number of lines>");
            System.exit(1);
        }

        String inputFile = args[0];
        int N = Integer.parseInt(args[1]);

        // Load movies
        ArrayList<Movie> movies = Parser.readMovies(inputFile, N);

        // Prepare output file (append)
        FileWriter writer = new FileWriter("analysis.txt", true);

        // Run tests for:
        // 1. sorted
        // 2. shuffled
        // 3. reversed
        runTest("Sorted", movies, writer);
        Collections.shuffle(movies);
        runTest("Shuffled", movies, writer);
        Collections.sort(movies, Collections.reverseOrder());
        runTest("Reversed", movies, writer);

        writer.close();
    }

    /**
     * Runs a single timing test for a given dataset ordering.
     * Measures insert, search, and delete time for all movie titles
     * using a Separate Chaining Hash Table.
     *
     * Results are printed to the console and appended to analysis.txt.
     *
     * @param label  name of the dataset order ("Sorted", "Shuffled", etc.)
     * @param list   ArrayList of Movie objects to process
     * @param writer FileWriter for CSV output
     * @throws IOException if writing to analysis.txt fails
     */
    private static void runTest(String label, ArrayList<Movie> list, FileWriter writer) throws IOException {

        System.out.println("\n==== " + label + " Movies ====");

        SeparateChainingHashTable<String> table = new SeparateChainingHashTable<>();

        long insertStart = System.nanoTime();
        for (Movie m : list)
            table.insert(m.getName());
        long insertEnd = System.nanoTime();

        long searchStart = System.nanoTime();
        for (Movie m : list)
            table.contains(m.getName());
        long searchEnd = System.nanoTime();

        long deleteStart = System.nanoTime();
        for (Movie m : list)
            table.remove(m.getName());
        long deleteEnd = System.nanoTime();

        long insertTime = insertEnd - insertStart;
        long searchTime = searchEnd - searchStart;
        long deleteTime = deleteEnd - deleteStart;

        // Output to screen
        System.out.printf("Insert time: %.6f sec\n", insertTime / 1e9);
        System.out.printf("Search time: %.6f sec\n", searchTime / 1e9);
        System.out.printf("Delete time: %.6f sec\n", deleteTime / 1e9);

        // Output CSV
        writer.write(
                String.format("%s,%d,%.9f,%.9f,%.9f\n",
                        label,
                        list.size(),
                        insertTime / 1_000_000_000.0,
                        searchTime / 1_000_000_000.0,
                        deleteTime / 1_000_000_000.0
                )
        );


    }
}
