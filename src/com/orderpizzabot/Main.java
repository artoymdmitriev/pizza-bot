package com.orderpizzabot;

import com.orderpizzabot.bot.Companion;
import com.orderpizzabot.email.EmailAccountInfo;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * Created by Artoym on 31.03.2017.
 */
public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Companion());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
