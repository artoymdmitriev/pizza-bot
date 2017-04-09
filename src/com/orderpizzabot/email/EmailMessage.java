package com.orderpizzabot.email;

import com.orderpizzabot.items.Complementary;
import com.orderpizzabot.items.Order;
import com.orderpizzabot.items.Pizza;

/**
 * Created by Artoym on 04.04.2017.
 */
public class EmailMessage {
    Order order;
    String subject;

    public EmailMessage(Order order) {
        this.order = order;
        subject = "Заказ #" + order.getOrderID();
    }

    public String getEmailTextForKitchen() {

        String result = "Заказаны следующие пиццы:\n";

        for(Pizza p : order.getOrderedPizzas()) {
            result = result + p.getName() + " Ингридиенты: " + p.getIngridients() + ". Вес: " + p.getWeight() + " грамм. Диаметр: " + p.getDiameter() + "\n";
        }

        result = result + "\n Помимо этого в заказ необходимо добавить следующие сопутствующие товары:\n";

        for(Complementary c : order.getOrderedComplementaries()) {
            result = result + c.getName() + "\n";
        }

        return result;
    }

    public String getEmailTextForManager() {
        //TODO add aditional info  (date, time, etc)

        String result = "Заказаны следующие товары:\n";

        for(Pizza p : order.getOrderedPizzas()) {
            result = result + p.getName() + " по цене: " + p.getPrice() + " руб.\n";
        }

        for(Complementary c : order.getOrderedComplementaries()) {
            result = result + c.getName() + " по цене: " + c.getPrice() + " руб.\n";
        }

        result = result + "---------------------------------\n";
        result = result + "Итоговая стоимость составила " + order.getFinalPrice() + " руб.";

        return result;
    }

    public String getEmailTextForCourier() {
        //TODO add aditional info  (date, time, etc)

        int amountOfPizzas = 0;
        int amountOfComplementaries = 0;

        String result = "Заказаны следующие товары:\n";

        for(Pizza p : order.getOrderedPizzas()) {
            result = result + p.getName() + " по цене: " + p.getPrice() + " руб.\n";
            amountOfPizzas++;
        }

        for(Complementary c : order.getOrderedComplementaries()) {
            result = result + c.getName() + " по цене: " + c.getPrice() + " руб.\n";
            amountOfComplementaries++;
        }

        result = result + "--------------------\n";
        result = result + "Заказ состоит из " + amountOfPizzas + " пицц и " + amountOfComplementaries + " дополнительных товаров\n";
        result = result + "Итоговая стоимость: " + order.getFinalPrice() + " руб.\n";
        result = result + "Адрес доставки: " + order.getAddress();

        return result;
    }

    public String getSubject() {
        return subject;
    }
}
