package com.orderpizzabot.bot;

import com.orderpizzabot.db.DBDriver;
import com.orderpizzabot.email.EmailMessage;
import com.orderpizzabot.email.EmailSender;
import com.orderpizzabot.items.*;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Artoym on 01.04.2017.
 * The class represents a bot that contacts with clients and
 * helps them to order something based on their needs.
 */
public class Companion extends TelegramLongPollingBot {

    public enum Keys {
        EMPTY, WANNA_PIZZA, CHOOSE_PIZZA,
        CHOOSECOMPLEMENTARIES, ENTERADDRESS,
        ENTERPHONENUMBER, CONFIRMORDER, WANNAANOTHERPIZZA,
        WANNAANOTHERCOMPLEMENTARY
    }

    Order order = new Order();
    BotInfo botInfo = new BotInfo();
    Update update = null;
    Keys key = Keys.EMPTY;
    private long chatID = 0;

    @Override
    public String getBotToken() {
        return botInfo.getToken();
    }

    @Override
    public String getBotUsername() {
        return botInfo.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.update = update;

        //PREVIOUS ACTION -> THE ACTION THAT WILL HAPPEN IN THE CURRENT BLOCK OF CODE
        if(update.hasMessage() && key.equals(Keys.EMPTY) && update.getMessage().getText().equals("/start")) {
            /*
            * EMPTY -> WANNA_PIZZA
            * Asking if the user wants to buy a pizza.
            * */

            askIfClientWantsToMakeAnOrder();
            key = Keys.WANNA_PIZZA;

        } else if(update.hasMessage() && (key.equals(Keys.WANNA_PIZZA) || key.equals(Keys.WANNAANOTHERPIZZA)) && update.getMessage().getText().equals("Да")) {
            /*
            * WANNA_PIZZA -> CHOOSE_PIZZA
            * or
            * WANNAANOTHERPIZZA -> CHOOSE_PIZZA
            *
            * Showing the list of available pizzas.
            * */

            makeClientChoosePizza();
            key = Keys.CHOOSE_PIZZA;
        } else if(update.hasMessage() && key.equals(Keys.CHOOSE_PIZZA) && isNum(update.getMessage().getText())) {
            /*
            * CHOOSE_PIZZA -> WANNAANOTHERPIZZA
            * Asking if the user wants another pizza.
            * */

            askIfClientWantsAnotherPizza();
            key = Keys.WANNAANOTHERPIZZA;
        } else if(update.hasMessage() && key.equals(Keys.WANNA_PIZZA) && update.getMessage().getText().equals("Нет")) {
            /*
            * WANNA_PIZZA -> EMPTY
            * Showing the links to website and contacts info because the user
            * refused to order anything.
            * */

            showInfoBecauseClientRefusedToMakeAnOrder();
            key = Keys.EMPTY;
        } else if(update.hasMessage() && (key.equals(Keys.WANNAANOTHERPIZZA) && update.getMessage().getText().equals("Нет")) ||
                    (key.equals(Keys.WANNAANOTHERCOMPLEMENTARY) && update.getMessage().getText().equals("Да"))) {
            /*
            * WANNAANOTHERPIZZA -> CHOOSECOMPLEMENTARIES
            * Showing the list of available complementaries.
            * */

            makeClientChooseComplementary();
            key = Keys.CHOOSECOMPLEMENTARIES;
        } else if (update.hasMessage() && key.equals(Keys.CHOOSECOMPLEMENTARIES) && isNum(update.getMessage().getText())) {
            /*
            * CHOOSECOMPLEMENTARIES -> WANNAANOTHERCOMPLEMENTARY
            * */

            askIfClientWantsAnotherComplementary();
            key = Keys.WANNAANOTHERCOMPLEMENTARY;
        } else if(update.hasMessage() && key.equals(Keys.WANNAANOTHERCOMPLEMENTARY) && update.getMessage().getText().equals("Нет")) {
            /*
            * CHOOSECOMPLEMENTARIES -> ENTERADDRESS
            * Asking the user to enter the delivery address.
            * */

            askClientToEnterAddress();
            key = Keys.ENTERADDRESS;
        } else if (update.hasMessage() && key.equals(Keys.ENTERADDRESS)) {
            /*
            * ENTERADDRESS -> ENTERPHONENUMBER
            * Asking the user to enter the phone number.
            * */

            askClientToEnterPhoneNumber();
            key = Keys.ENTERPHONENUMBER;

        } else if (update.hasMessage() && key.equals(Keys.ENTERPHONENUMBER)){
            /*
            * ENTERPHONENUMBER -> CONFIRMORDER
            * Asking the user to tap on button to confirm the order.
            * */

            askClientToConfirmOrder();
            key = Keys.CONFIRMORDER;
        } else if (update.hasMessage() && key.equals(Keys.CONFIRMORDER) && update.getMessage().getText().equals("Да")) {
            /*
            * CONFIRMORDER -> EMPTY
            * Adding the order in database, sending the message to user with information
            * about the order. The action is complete.
            * */

            saveCreatedOrderAndSendConfirmation();
            key = Keys.EMPTY;
        } else if (!update.hasMessage() && update.getCallbackQuery().getData().equals("Наши контакты.")) {
            /*
            * Sending the contact info when the user has tapped on
            * 'Our contacts' inline keyboard button.
            * */

            sendContactsInfoAboutShop();

        } else {
            /*
            * Sending an error message because the user has typed an
            * unexpected message.
            * */
            sendErrorMessage();
        }
    }

    /**
     * Asks if the client wants to order something.
     */
    private void askIfClientWantsToMakeAnOrder() {
        /* EMPTY -> WANNA_PIZZA
        * Asking if the user wants to buy a pizza.
        * */

        Message msg = update.getMessage();
        chatID = msg.getChatId();
        SendMessage message = new SendMessage();
        message.setChatId(msg.getChatId());
        message.setText("Привет! Хочешь, чтобы я помог тебе заказать пиццу? \uD83C\uDF55");
        message.setReplyMarkup(yesOrNoKeyboard());

        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the list of available pizzas to the client.
     */
    private void makeClientChoosePizza() {
        Message msg = update.getMessage();
        String reply = "";
        PizzaContainer pizzaContainer = new PizzaContainer();
        if(key.equals(Keys.WANNA_PIZZA)) {
            int i = 1;
            reply = "Вот список наших пицц, выбирай:\n";
            for(Pizza p : pizzaContainer.getPizzas()) {
                reply = reply + i + ". \"" + p.getName() + "\", ингридиенты: " + p.getIngridients()
                        + ", вес:  " + p.getWeight() + " г., диаметр: " + p.getDiameter() + " см., цена: " + p.getPrice() + " руб.\n";
                i++;
            }
        } else if(key.equals(Keys.WANNAANOTHERPIZZA)) {
            reply = "Вводи номер пиццы";
        }

        ReplyKeyboardMarkup rkm = getKeyboardWithNumbersOfItems(pizzaContainer.getPizzas().size());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setText(reply);
        sendMessage.setReplyMarkup(rkm);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks if the client wants to add another pizza to order.
     */
    private void askIfClientWantsAnotherPizza() {
        Message msg = update.getMessage();
        PizzaContainer pizzaContainer = new PizzaContainer();
        ArrayList<Pizza> pizzas = pizzaContainer.getPizzas();

        for(int i = 0; i < pizzas.size(); i++) {
            if(i + 1 == Integer.parseInt(msg.getText())) {
                order.addPizzaToOrder(pizzas.get(i));
            }
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("Хочешь еще одну пиццу?");
        sendMessage.setReplyMarkup(yesOrNoKeyboard());
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a sad message because the client refused to
     * order anything.
     */
    private void showInfoBecauseClientRefusedToMakeAnOrder() {
        Message msg = update.getMessage();
        SendMessage message = new SendMessage();
        message.setChatId(msg.getChatId());
        message.setText(":(\n Пиши /start, если я тебе понадоблюсь!");

        InlineKeyboardMarkup replyKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboardRowss = new ArrayList<>();

        List<InlineKeyboardButton> keyboardRowsTop = new ArrayList<>();

        InlineKeyboardButton keyboardButtonWebsite = new InlineKeyboardButton();
        keyboardButtonWebsite.setText("Наш сайт.");
        keyboardButtonWebsite.setUrl("http://pizza.com/");
        keyboardRowsTop.add(keyboardButtonWebsite);

        List<InlineKeyboardButton> keyboardRowsBottom = new ArrayList<>();
        InlineKeyboardButton keyboardButtonInfo = new InlineKeyboardButton();
        keyboardButtonInfo.setText("Наши контакты.");
        keyboardButtonInfo.setCallbackData("Наши контакты.");
        keyboardRowsBottom.add(keyboardButtonInfo);

        keyboardRowss.add(keyboardRowsTop);
        keyboardRowss.add(keyboardRowsBottom);

        replyKeyboardMarkup.setKeyboard(keyboardRowss);
        message.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the list of complementary items to the client.
     */
    private void makeClientChooseComplementary() {
        //TODO add action if the user doesn't want to order complementaries
        Message msg = update.getMessage();
        String replyComp = "";
        ComplementaryContainer complementaryContainer = new ComplementaryContainer();

        if(key.equals(Keys.WANNAANOTHERPIZZA)) {
            int f = 1;
            replyComp = "Вот список наших дополнительных товаров, выбирай:\n";
            for(Complementary c : complementaryContainer.getComplementaries()) {
                if(c.getWeight() == 0) {
                    replyComp = replyComp + f + ". \"" + c.getName() + "\", г., цена: " + c.getPrice() + " руб.\n";
                } else {
                    replyComp = replyComp + f + ". \"" + c.getName() + "\", вес: " + c.getWeight() + " г., цена: " + c.getPrice() + " руб.\n";
                }
                f++;
            }
        } else if(key.equals(Keys.WANNAANOTHERCOMPLEMENTARY)) {
            replyComp = "Вводи номер товара";
        }

        ReplyKeyboardMarkup rkm = getKeyboardWithNumbersOfItems(complementaryContainer.getComplementaries().size());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setText(replyComp);
        sendMessage.setReplyMarkup(rkm);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks client if he wants to order another complementary
     * item.
     */
    private void askIfClientWantsAnotherComplementary() {
        Message msg = update.getMessage();
        ComplementaryContainer complementaryContainer = new ComplementaryContainer();
        ArrayList<Complementary> complementaries = complementaryContainer.getComplementaries();

        for(int i = 0; i < complementaries.size(); i++) {
            if(i + 1 == Integer.parseInt(msg.getText())) {
                order.addComplementaryToOrder(complementaries.get(i));
            }
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("Хочешь еще один дополнительный товар?");
        sendMessage.setReplyMarkup(yesOrNoKeyboard());
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks client to enter the address where the order will be
     * delivered.
     */
    private void askClientToEnterAddress() {
        Message msg = update.getMessage();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("По какому адресу выполнить доставку?");
        sendMessage.setChatId(msg.getChatId());
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks client to enter his phone number.
     */
    private void askClientToEnterPhoneNumber() {
        Message msg = update.getMessage();
        order.setAddress(msg.getText());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("По какому номеру телефона курьер сможет связаться с тобой?");
        sendMessage.setChatId(msg.getChatId());
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks client to confirm the order by pressing
     * the button.
     */
    private void askClientToConfirmOrder() {
        Message msg = update.getMessage();
        order.setPhoneNumber(msg.getText());
        order.calculateFinalPrice();

        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Да");
        keyboardRows.add(keyboardRow);
        rkm.setKeyboard(keyboardRows);
        rkm.setOneTimeKeyboad(true);
        rkm.setResizeKeyboard(true);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Подтверди заказ, нажав на кнопку \"Да\"");
        sendMessage.setChatId(msg.getChatId());
        sendMessage.setReplyMarkup(rkm);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes Order object into database. After that it
     * sends a success message to the client.
     */
    private void saveCreatedOrderAndSendConfirmation() {
        Message msg = update.getMessage();
        DBDriver dbDriver = new DBDriver();
        dbDriver.addOrder(order);

        //TODO replace with observer pattern
        EmailMessage emailMessage = new EmailMessage(order);
        EmailSender emailSender = new EmailSender(emailMessage);
        emailSender.sendEmailToManager();
        emailSender.sendEmailToCourier();
        emailSender.sendEmailToKitchen();

        //TODO add more info to the message
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Заказ принят в обработку!\nОкончательная стоимость: " + order.getFinalPrice() + " руб."
                + "\nИдентификационный номер заказа: #" + order.getOrderID()
                + "\n\nЧтобы совершить новый заказ, введи /start");
        sendMessage.setChatId(msg.getChatId());
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends contact information about company.
     */
    private void sendContactsInfoAboutShop() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText(contactsMessage());
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an error message to the client because
     * he has typed an unexpected message.
     */
    private void sendErrorMessage() {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.setText("Прости, я не понял что ты имеешь ввиду.");
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns true if the given string is a number.
     * @param strNum
     * @return
     */
    private boolean isNum(String strNum) {
        boolean ret = true;
        try {
            Double.parseDouble(strNum);
        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    /**
     * Returs the string that contains the contact information
     * about pizza shop.
     * @return
     */
    private String contactsMessage() {
        String result = "vk.com/orderpizzabot\ntwitter.com/orderpizzabot\n+375-29-444-44-44";
        return result;
    }

    /**
     * Returns a keyboard with yes/no buttons.
     * @return a keyboard with yes/no buttons
     */
    private ReplyKeyboardMarkup yesOrNoKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Да");
        row.add("Нет");
        keyboardRows.add(row);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        return replyKeyboardMarkup;
    }

    /**
     * Returns a keyboard with buttons. Each button contains the
     * position of each item in the list of available goods.
     * @param amountOfItems
     * @return
     */
    private ReplyKeyboardMarkup getKeyboardWithNumbersOfItems(int amountOfItems) {
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow;
        int numOnButton = 1;

        for(int rows = 1; rows <= Math.round(amountOfItems / 4) + 1; rows++) {
            keyboardRow = new KeyboardRow();
            for(int buttons = 1; buttons <= 4; buttons++) {
                if(numOnButton <= amountOfItems) {
                    keyboardRow.add(String.valueOf(numOnButton));
                    numOnButton++;
                }
            }
            keyboardRows.add(keyboardRow);
        }


        rkm.setKeyboard(keyboardRows);
        rkm.setResizeKeyboard(true);
        rkm.setOneTimeKeyboad(true);
        return rkm;
    }
}