package com.orderpizzabot.items;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Artoym on 01.04.2017.
 * The class represents an order that a client makes.
 */
public class Order {
    private ArrayList<Pizza> orderedPizzas = new ArrayList<>();
    private ArrayList<Complementary> orderedComplementaries = new ArrayList<>();
    private Client client;
    private double finalPrice;
    private int orderID;
    private String date;

    public Order() {
        orderID = createOrderID();
        date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        client = new Client();
    }

    /**
     * Adds a new pizza object to an arraylist of ordered
     * pizzas.
     * @param pizza
     */
    public void addPizzaToOrder(Pizza pizza) {
        orderedPizzas.add(pizza);
    }

    /**
     * Adds a new complementary object to an arraylist of ordered
     * complementaries.
     * @param complementary
     */
    public void addComplementaryToOrder(Complementary complementary) {
        orderedComplementaries.add(complementary);
    }

    /**
     * Sets an address where the order must be delivered.
     * @param address
     */
    public void setAddress(String address) {
        client.setAddress(address);
    }

    /**
     * Sets a mobile phone number that can be used by the
     * courier to contact the client.
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        client.setPhoneNumber(phoneNumber);
    }

    /**
     * Calculates the final price of the order. The result is a
     * sum of prices of each pizza and complementary item.
     */
    public void calculateFinalPrice() {
        for(Pizza p : orderedPizzas)
            finalPrice = finalPrice + p.getPrice();
        for(Complementary c : orderedComplementaries)
            finalPrice = finalPrice + c.getPrice();
    }

    /**
     * Returns the final price of the order.
     * @return the final price of the order
     */
    public double getFinalPrice() {
        return finalPrice;
    }

    /**
     * Returns the address where the order must be delivered.
     * @return the address where the order must be delivered
     */
    public String getAddress() {
        return client.getAddress();
    }

    /**
     * Returns the phone number of the client.
     * @return the phone number of the client
     */
    public String getPhoneNumber() {
        return client.getPhoneNumber();
    }

    /**
     * Returns the ID of the order.
     * @return the ID of the order
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     * Returns the date when the order was made.
     * @return the date when the order was made
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the list of pizzas that the client has ordered.
     * @return the list of pizzas that the client has ordered
     */
    public ArrayList<Pizza> getOrderedPizzas() {
        return orderedPizzas;
    }

    /**
     * Returns the list of complementaries that the client has ordered.
     * @return the list of complementaries that the client has ordered
     */
    public ArrayList<Complementary> getOrderedComplementaries() {
        return orderedComplementaries;
    }

    /**
     * Returns the string representation of the order object.
     * @return the string representation of the order object
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderedPizzas=" + orderedPizzas +
                ", orderedComplementaries=" + orderedComplementaries +
                ", client=" + client +
                ", finalPrice=" + finalPrice +
                ", orderID=" + orderID +
                ", date='" + date + '\'' +
                '}';
    }

    /**
     * Returns the ID for the new order object. It
     * consists of the time since 1970 casted to data type.
     * @return the ID for the new order object
     */
    private int createOrderID() {
        return (int) new Date().getTime();
    }
}
