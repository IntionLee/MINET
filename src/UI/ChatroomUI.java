/*
 * @(#)MainChatUI.java    1.0 2015/12/08
 */

package src.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.io.*;

import src.*;

/**
 * provide the GUI of chatroom window
 *
 * @version 1.0 08 Dec 2015
 */
public class ChatroomUI extends JFrame implements ComponentListener,
                WindowListener {

    private UserListUI userListWindow;
    private JScrollPane scrollUserListWindow;
    private JTextArea chatWindow;
    private JScrollPane scrollChatWindow;
    private JTextArea inputDialog;
    private JScrollPane scrollInputDialog;
    private JButton sendBtn;
    private JButton sendFileBtn;
    private Client client;

    public ChatroomUI(Client client_) {
        super("聊天室");

        client = client_;

        setLayout(new GridBagLayout());
        setBackground(new Color(182, 236, 234));

        requestName();
        requestList();

        initComponents();
        initUI();

        setSize(600, 600);
        setResizable(false);
        setVisible(true);
        getContentPane().setBackground(new Color(182, 236, 234));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addComponentListener(this);
        addWindowListener(this);
    }

    private void requestName() {
        client.requestClientname();
        client.input();
        System.setIn(new ByteArrayInputStream("".getBytes()));
    }

    private void requestList() {
        client.requestUserlist();
        client.input();
        System.setIn(new ByteArrayInputStream("".getBytes()));
    }

    private void initComponents() {
        // get the user list
        userListWindow = new UserListUI(client, client.getUserlist());
            // client.getUserlist());
        
        chatWindow = new JTextArea();
        inputDialog = new JTextArea();
        sendBtn = new JButton("发送");
        sendFileBtn = new JButton("选择文件");
    }

    private void addHeader() {
        GridBagConstraints cTop = new GridBagConstraints();
        cTop.weightx = 1;
        cTop.weighty = 0.1;
        cTop.insets = new Insets(0, 0, 0, 0);
        cTop.fill = GridBagConstraints.BOTH;
        cTop.anchor = GridBagConstraints.CENTER;

        // add header
        JLabel head = new JLabel();
        cTop.gridx = 0;
        cTop.gridy = 0;
        cTop.gridwidth = 6;
        cTop.gridheight = 1;
        head.setOpaque(true);
        head.setBackground(new Color(111, 201, 242));
        add(head, cTop);
    }

    private void addUserlistWindow() {
        GridBagConstraints cLeft = new GridBagConstraints();
        cLeft.weightx = 0;
        cLeft.weighty = 0.5;
        cLeft.insets = new Insets(10, 10, 10, 10);
        cLeft.fill = GridBagConstraints.VERTICAL;
        cLeft.anchor = GridBagConstraints.WEST;

        // add user list window
        cLeft.gridx = 0;
        cLeft.gridy = 1;
        cLeft.gridwidth = 2;
        cLeft.gridheight = 6;
        userListWindow.setOpaque(true);
        userListWindow.setBackground(new Color(239, 250, 252));
        scrollUserListWindow = new JScrollPane(userListWindow);
        scrollUserListWindow.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollUserListWindow.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollUserListWindow.setPreferredSize(new Dimension(180, 150));
        add(scrollUserListWindow, cLeft);
    }

    private void addChatWindow() {
        GridBagConstraints cUpRight = new GridBagConstraints();
        cUpRight.weightx = 0.8;
        cUpRight.weighty = 0.5;
        cUpRight.insets = new Insets(10, 10, 10, 10);
        cUpRight.fill = GridBagConstraints.BOTH;
        cUpRight.anchor = GridBagConstraints.CENTER;

        // add chat window
        cUpRight.gridx = 2;
        cUpRight.gridy = 1;
        cUpRight.gridwidth = 4;
        cUpRight.gridheight = 4;
        chatWindow.setEditable(false);
        chatWindow.setOpaque(true);
        chatWindow.setBackground(new Color(239, 250, 252));
        scrollChatWindow = new JScrollPane(chatWindow);
        scrollChatWindow.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollChatWindow.setPreferredSize(new Dimension(200, 150));
        add(scrollChatWindow, cUpRight);
    }

    private void addInputDialogWindow() {
        GridBagConstraints cDownRight = new GridBagConstraints();
        cDownRight.weightx = 0.8;
        cDownRight.weighty = 0.25;
        cDownRight.insets = new Insets(10, 10, 10, 10);
        cDownRight.fill = GridBagConstraints.BOTH;
        cDownRight.anchor = GridBagConstraints.CENTER;

        // add input dialog window
        cDownRight.gridx = 2;
        cDownRight.gridy = 5;
        cDownRight.gridwidth = 4;
        cDownRight.gridheight = 2;
        inputDialog.setOpaque(true);
        inputDialog.setBackground(new Color(239, 250, 252));
        scrollInputDialog = new JScrollPane(inputDialog);
        scrollInputDialog.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollInputDialog, cDownRight);
    }

    private void addSendButton() {
        GridBagConstraints cButton = new GridBagConstraints();
        cButton.weightx = 0.2;
        cButton.weighty = 0.025;
        cButton.insets = new Insets(10, 10, 10, 10);
        cButton.fill = GridBagConstraints.BOTH;
        cButton.anchor = GridBagConstraints.EAST;

        // add send button
        cButton.gridx = 4;
        cButton.gridy = 7;
        cButton.gridwidth = 1;
        cButton.gridheight = 1;
        add(sendBtn, cButton);

        // add send file button
        cButton.weightx = 0.2;
        cButton.gridx = 5;
        cButton.gridy = 7;
        cButton.gridwidth = 1;
        cButton.gridheight = 1;
        add(sendFileBtn, cButton);
    }

    private void initUI() {
        addHeader();
        addUserlistWindow();
        addChatWindow();
        addInputDialogWindow();
        addSendButton();

        // add listener
        sendBtn.addActionListener(new sendBtnListener());
        sendFileBtn.addActionListener(new sendFileBtnListener());
    }

    public void appendChatWindowText() {
        chatWindow.append(client.getMessage() + "\n");
    }

    public void refreshUserlist() {
        scrollUserListWindow.remove(userListWindow);
        // refresh the new user list
        userListWindow = new UserListUI(client, client.getUserlist());
        remove(scrollUserListWindow);
        scrollUserListWindow = new JScrollPane(userListWindow);
        addUserlistWindow();

        revalidate();
        repaint();
    }

    public void appendPrivateChatWindowText(String chatWith) {
        userListWindow.getPrivateChatUI(chatWith).appendChatWindowText();
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent arg0) {
            // TODO Auto-generated method stub

        userListWindow.closePrivateChatWindows();
        client.close();
        dispose();
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
            // TODO Auto-generated method stub
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
            // TODO Auto-generated method stub
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
            // TODO Auto-generated method stub
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void componentShown(ComponentEvent arg0) {
            // TODO Auto-generated method stub

    }

    private class sendBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // send the string to server
            client.output_to_chatroom(
                inputDialog.getText().toString());

            // clean the inputDialog window
            inputDialog.setText("");
        }
    }

    private class sendFileBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new FileChooserUI(client, 0, "");
        }
    }
}
