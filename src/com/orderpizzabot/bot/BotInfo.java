package com.orderpizzabot.bot;

import java.io.*;

/**
 * Created by Artoym on 07.04.2017.
 * The class represents information that is used by telegram bot to work,
 * such as token and username.
 */
public class BotInfo implements Serializable {
    private String token;
    private String username;
    private BotInfo botInfo;
    public static final long serialVersionUID = -3068493310143898707L;

    /**
     * When the object is being created, it deserializes information
     * about itself from the bot.bot file.
     */
    public BotInfo() {
        try {
            //TODO get rid of the hardcoded path
            FileInputStream fis = new FileInputStream("D:/IdeaProjects/PizzaBot/tmp/bot.bot");
            ObjectInputStream in = new ObjectInputStream(fis);
            this.botInfo = (BotInfo) in.readObject();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        username = botInfo.username;
        token = botInfo.token;
    }

    /**
     * Returns the token of the deserialized BotInfo object.
     * @return telegram bot token
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the username of the deserialized BotInfo object.
     * @return telegram bot username
     */
    public String getUsername() {
        return username;
    }
}
