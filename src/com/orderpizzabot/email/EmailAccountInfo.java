package com.orderpizzabot.email;

import java.io.*;

/**
 * Created by Artoym on 07.04.2017.
 * The class represents information that is used by email sender to work,
 * such as login and password.
 */
public class EmailAccountInfo implements Serializable {
    private String login;
    private String password;
    private EmailAccountInfo emailAccountInfo;
    public static final long serialVersionUID = 3381006770889841535L;

    public EmailAccountInfo() {
        try {
            //TODO get rid of the hardcoded path
            FileInputStream fis = new FileInputStream("D:/IdeaProjects/PizzaBot/tmp/email.email");
            ObjectInputStream in = new ObjectInputStream(fis);
            this.emailAccountInfo = (EmailAccountInfo) in.readObject();
            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.login = emailAccountInfo.login;
        this.password = emailAccountInfo.password;

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
