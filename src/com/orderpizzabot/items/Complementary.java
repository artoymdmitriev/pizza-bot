package com.orderpizzabot.items;

/**
 * Created by Artoym on 31.03.2017.
 * The class represents a single complementary object.
 */
public class Complementary {
    private String name;
    private double weight;
    private double price;
    private boolean inStock;

    public Complementary(String name, double weight, double price, boolean inStock) {
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.inStock = inStock;
    }

    /**
     * Returns the name of the complementary.
     * @return the name of the complementary
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the weight of the complementary.
     * @return the weight of the complementary
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the price of the complementary.
     * @return the price of the complementary
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns true if the item is in stock.
     * @return true if the item is in stock
     */
    public boolean isInStock() {
        return inStock;
    }
}
