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
 * provide the GUI of regist window
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
                    Boolean flag = false;
                    // client.requestUserlist();
                    // System.out.println("finding1...");
                    // client.input();
                    // System.setIn(new ByteArrayInputStream("".getBytes()));
                    // System.out.println("finding2...");
                    for (String user_ : client.getUserlist()) {
                        if (user_.equals(user.getText())) {
                            System.out.println("has this user");
                            flag = true;
                            break;
                        }
                    }

                    if (!user.getText().equals(client.getClientname()) && flag
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

    public void removeFromList(String chatWith) {
        privateChatList.put(chatWith, null);
    }

    // return null if there is no private-chat-window with this user
    public PrivateChatUI getPrivateChatUI(String chatWith) {
        if (privateChatList.get(chatWith) == null) {
            PrivateChatUI pcUI = new PrivateChatUI(client,
                chatWith, privateChatList);
            // privateChatList.put(chatWith, pcUI);
            return pcUI;
        } else {
            return privateChatList.get(chatWith);
        }
    }
}
