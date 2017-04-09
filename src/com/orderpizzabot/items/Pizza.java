package com.orderpizzabot.items;

/**
 * The class represents a single pizza object.
 */

public class Pizza {

    private String name;
    private double price;
    private String ingridients;
    private double weight;
    private double diameter;
    private boolean inStock;


    public Pizza(String name, double price, String ingridients, double weight, double diameter, boolean inStock) {
        this.name = name;
        this.price = price;
        this.ingridients = ingridients;
        this.weight = weight;
        this.diameter = diameter;
        this.inStock = inStock;
    }

    /**
     * Returns the name of the pizza.
     * @return the name of the pizza
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the pizza.
     * @return the price of the pizza
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the ingridients of the pizza.
     * @return the ingridients of the pizza
     */
    public String getIngridients() {
        return ingridients;
    }

    /**
     * Returns the weight of the pizza.
     * @return the weight of the pizza
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the diameter of pizza.
     * @return the diameter of pizza
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Returns true if the item is in stock.
     * @return true if the item is in stock
     */
    public boolean isInStock() {
        return inStock;
    }

    /**
     * Returns the string representation of pizza object.
     * @return the string representation of pizza object
     */
    @Override
    public String toString() {
        return "Pizza{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", ingridients='" + ingridients + '\'' +
                ", weight=" + weight +
                ", diameter=" + diameter +
                ", inStock=" + inStock +
                '}';
    }
}
