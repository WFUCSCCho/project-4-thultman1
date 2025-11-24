/****************************************************
 * @file: Parser.java
 * @description: Loads movies from a CSV file into an ArrayList<Movie>. Quote-aware CSV parsing handles commas inside quoted fields.
 *  Used by Proj3 to supply data for sorting algorithms and adapted from Proj2.
 * @author: Tim Hultman
 * @date: November 13, 2025
 ****************************************************/

import java.io.*;
import java.util.*;

public class Parser {

    /**
     * Reads up to N movies from a CSV file and returns them as an ArrayList.
     * Parameter String filename, int n
     * Returns ArrayList<Movie>
     */
    public static ArrayList<Movie> readMovies(String filename, int N) throws FileNotFoundException {
        ArrayList<Movie> list = new ArrayList<>();
        Scanner scan = new Scanner(new File(filename));
        if (scan.hasNextLine()) scan.nextLine(); // skip header
        int count = 0;
        while (scan.hasNextLine() && count < N) {
            String line = scan.nextLine();
            String[] parts = splitCSV(line, 8);
            list.add(convert(parts));
            count++;
        }
        scan.close();
        return list;
    }

    /**
     * Splits a CSV line into fields, aware of quoted commas.
     * Parameter String line, int expected
     * Return String[]
     */
    public static String[] splitCSV(String line, int expected) {
        String[] out = new String[expected];
        StringBuilder build = new StringBuilder();
        boolean inQuotes = false;
        int index = 0;
        for (char c : line.toCharArray()) {
            if (c == '"') inQuotes = !inQuotes;
            else if (c == ',' && !inQuotes) {
                out[index++] = build.toString();
                build.setLength(0);
            } else build.append(c);
        }
        out[index] = build.toString();
        return out;
    }

    /**
     * Converts a String[] of CSV fields into a Movie object.
     * Parameter String[]f
     * Return Movie
     */
    public static Movie convert(String[] f) {
        for (int i = 0; i < f.length; i++) f[i] = f[i].replace("\"", "");
        String name = f[0];
        int year = tryParseInt(f[1]);
        String duration = f[2];
        String genre = f[3];
        double rating = tryParseDouble(f[4]);
        String description = f[5];
        String director = f[6];
        String stars = f[7];
        return new Movie(name, year, duration, genre, rating, description, director, stars);
    }

    /**
     * Attempts to parse an integer from a string.
     * If parsing fails returns 0.
     *
     * Parameter String s
     * return int
     */
    private static int tryParseInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    /**
     * Attempts to parse a double from a string.
     * If parsing fails returns 0.0.
     *
     * Parameter String s
     * Return double
     */
    private static double tryParseDouble(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }
}
