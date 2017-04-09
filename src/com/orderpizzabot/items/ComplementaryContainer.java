package com.orderpizzabot.items;

import com.orderpizzabot.db.DBDriver;

import java.util.ArrayList;

/**
 * Created by Artoym on 31.03.2017.
 * The class represents a list of complementaries. Its constructor initializes a
 * connection with database via DBDRiver class and stores a list of
 * pizzas.
 */
public class ComplementaryContainer {
    private ArrayList<Complementary> complementaries = new ArrayList<>();

    /**
     * Adds only complementaries that are in stock, others will be ignored.
     */
    public ComplementaryContainer() {
        for(Complementary c : new DBDriver().getComplementariesList()) {
            if(c.isInStock()) {
                complementaries.add(c);
            }
        }
    }

    /**
     * Returns a list of complementaries.
     * @return a list of complementaries
     */
    public ArrayList<Complementary> getComplementaries() {
        return complementaries;
    }
}
