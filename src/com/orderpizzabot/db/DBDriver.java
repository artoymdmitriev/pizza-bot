package com.orderpizzabot.db;

import com.orderpizzabot.items.Complementary;
import com.orderpizzabot.items.Order;
import com.orderpizzabot.items.Pizza;

import java.sql.*;
import java.util.*;

/**
 * Created by Artoym on 31.03.2017.
 * The class is a connector for the db.
 */

public class DBDriver {
    private static final String connectionPath = "jdbc:mysql://localhost:3306/pizza_db?useSSL=false";
    private ResultSet resultSet;
    private ArrayList<Pizza> pizzas;
    private ArrayList<Complementary> complementaries;

    /**
     * Returns the list of pizzas that are stored in database.
     * @return list of pizzas that are store in database
     */
    public ArrayList<Pizza> getPizzasList() {
        try {
            Connection connection = DriverManager.getConnection(connectionPath, "root", "root");
            Statement myStat = connection.createStatement();
            resultSet = myStat.executeQuery("SELECT * FROM pizza_db.pizzas");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pizzas = new ArrayList<>();
        createPizzas();

        return pizzas;
    }

    private void createPizzas() {
        try {
            while (resultSet.next()) {
                    pizzas.add(new Pizza(resultSet.getString("name"), resultSet.getDouble("price"),
                            resultSet.getString("ingridients"), resultSet.getDouble("weight"),
                            resultSet.getDouble("diameter"), resultSet.getBoolean("in_stock")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of complementaries that are stored in database.
     * @return list of complementaries that are store in database
     */
    public ArrayList<Complementary> getComplementariesList() {
        try {
            Connection connection = DriverManager.getConnection(connectionPath, "root", "root");
            Statement myStat = connection.createStatement();
            resultSet = myStat.executeQuery("SELECT * FROM pizza_db.complementaries");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        complementaries = new ArrayList<>();
        createComplementaries();

        return complementaries;
    }

    private void createComplementaries() {

        try {
            while (resultSet.next()) {
                    complementaries.add(new Complementary(resultSet.getString("name"), resultSet.getDouble("weight"), resultSet.getDouble("price"), resultSet.getBoolean("in_stock")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Puts a new order into database.
     * @param o
     */
    public void addOrder(Order o) {
        String statement = "INSERT INTO orders(id,price,address,date,phonenumber) VALUES(?,?,?,?,?)";
        try {
            Connection connection = DriverManager.getConnection(connectionPath, "root", "root");
            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, o.getOrderID());
            preparedStatement.setDouble(2, o.getFinalPrice());
            preparedStatement.setString(3, o.getAddress());
            preparedStatement.setString(4, o.getDate());
            preparedStatement.setString(5, o.getPhoneNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
