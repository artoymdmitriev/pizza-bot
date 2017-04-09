package com.orderpizzabot.items;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Created by Artoym on 09.04.2017.
 */
public class ClientTest {
    @Test
    public void testGetAddress() {
        Client client = new Client();
        client.setAddress("test");

        assertEquals("test", client.getAddress());
    }

    @Test
    public void testGetPhoneNumber() {
        Client client = new Client();
        client.setPhoneNumber("111-11-11");

        assertEquals("111-11-11", client.getPhoneNumber());
    }
}
