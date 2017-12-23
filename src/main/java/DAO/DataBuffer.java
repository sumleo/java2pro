package DAO;

import Model.Earchquake;

import java.util.ArrayList;

public class DataBuffer {
    private static ArrayList<Earchquake> earchquakes;

    /**
     * Initial the arraylist
     */
    public static void setAllData() {
        if (earchquakes == null) earchquakes = new ArrayList<Earchquake>();
    }

    public static ArrayList<Earchquake> getData() {
        return earchquakes;
    }

    /**
     * Clear the data
     */
    public static void clear() {
        earchquakes = null;
    }
}
