/*
 * @(#)UserListUI.java    1.0 2015/12/08
 */

package src.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.io.*;

import src.*;

/**
 * provide the list of user(s) online
 *
 * @version 1.0 08 Dec 2015
 */
public class UserListUI extends JPanel {

    private ArrayList<JLabel> list;
    private HashMap<String, PrivateChatUI> privateChatList;
    // if there is a private-chat-window for a user
    private Box box;
    private Client client;

    public UserListUI(Client client_, ArrayList<String> userList) {
        client = client_;

        list = new ArrayList<JLabel>();
        privateChatList = new HashMap<String, PrivateChatUI>();

        box = Box.createVerticalBox();
        for (int i = 0; i < userList.size(); i++) {
            JLabel user = new JLabel(userList.get(i));
            list.add(user);
            box.add(user);
        }

        initUI();
    }

    private void initUI() {
        add(box);

        // add listener to each user label
        for (JLabel user : list) {
            user.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (!user.getText().equals(client.getClientname())
                        && (privateChatList.get(user.getText()) == null)) {
                            new PrivateChatUI(client, user.getText(),
                                privateChatList);
                    } else {
                        System.out.println(
                            "you have created a private window");
                    }
                }
            });
        }
    }

    public void closePrivateChatWindows() {
        for (PrivateChatUI ui : privateChatList.values()) {
            if (ui != null) {
                ui.dispose();
            }
        }
    }

    public void removeFromList(String chatWith) {
        privateChatList.put(chatWith, null);
    }

    // return null if there is no private-chat-window with this user
    public PrivateChatUI getPrivateChatUI(String chatWith) {
        if (privateChatList.get(chatWith) == null) {
            PrivateChatUI pcUI = new PrivateChatUI(client,
                chatWith, privateChatList);
            return pcUI;
        } else {
            return privateChatList.get(chatWith);
        }
    }
}
