package com.orderpizzabot.items;

import com.orderpizzabot.db.DBDriver;

import java.util.ArrayList;

/**
 * The class represents a list of pizzas. Its constructor initializes a
 * connection with database via DBDRiver class and stores an arraylist of
 * pizzas.
 */

public class PizzaContainer {
    private ArrayList<Pizza> pizzas = new ArrayList<>();

    /**
     * Adds only pizzas that are in stock, others will be ignored.
     */
    public PizzaContainer() {
        for(Pizza p : new DBDriver().getPizzasList()) {
            if(p.isInStock()) {
                pizzas.add(p);
            }
        }
    }

    /**
     * Returns a list of pizzas.
     * @return a list of pizzas
     */
    public ArrayList<Pizza> getPizzas() {
        return pizzas;
    }
}
