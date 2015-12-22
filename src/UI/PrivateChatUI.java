/*
 * @(#)PrivateChatUI.java    1.0 2015/12/08
 */

package src.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

import src.*;

/**
 * provide the GUI of private chat window(p2p)
 *
 * @version 1.0 08 Dec 2015
 */
public class PrivateChatUI extends JFrame implements ComponentListener,
                WindowListener {

    private JTextArea chatWindow;
    private JScrollPane scrollChatWindow;
    private JTextArea inputDialog;
    private JScrollPane scrollInputDialog;
    private JButton sendBtn;
    private JButton sendFileBtn;
    private Client client;
    private String chatWith;
    private HashMap<String, PrivateChatUI> privateChatList;

    public PrivateChatUI(Client client_, String name,
            HashMap<String, PrivateChatUI> chatWindowList) {
        super(name);
        setLayout(new GridBagLayout());

        addComponentListener(this);
        addWindowListener(this);

        client = client_;
        chatWith = name;
        privateChatList = chatWindowList;
        privateChatList.put(chatWith, this);

        initComponents();
        initUI();

        setSize(600, 600);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
        chatWindow = new JTextArea();
        scrollChatWindow = new JScrollPane(chatWindow);
        scrollChatWindow.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        inputDialog = new JTextArea();
        scrollInputDialog = new JScrollPane(inputDialog);
        scrollInputDialog.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        sendBtn = new JButton("发送");
        sendFileBtn = new JButton("选择文件");
    }

    private void initUI() {
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

        GridBagConstraints cUp = new GridBagConstraints();
        cUp.weightx = 1;
        cUp.weighty = 0.5;
        cUp.insets = new Insets(10, 10, 10, 10);
        cUp.fill = GridBagConstraints.BOTH;
        cUp.anchor = GridBagConstraints.CENTER;

        // add chat window
        cUp.gridx = 2;
        cUp.gridy = 1;
        cUp.gridwidth = 4;
        cUp.gridheight = 4;
        chatWindow.setEditable(false);
        chatWindow.setOpaque(true);
        chatWindow.setBackground(new Color(239, 250, 252));
        add(scrollChatWindow, cUp);

        GridBagConstraints cDown = new GridBagConstraints();
        cDown.weightx = 1;
        cDown.weighty = 0.25;
        cDown.insets = new Insets(10, 10, 10, 10);
        cDown.fill = GridBagConstraints.BOTH;
        cDown.anchor = GridBagConstraints.CENTER;

        // add input dialog window
        cDown.gridx = 2;
        cDown.gridy = 5;
        cDown.gridwidth = 4;
        cDown.gridheight = 2;
        inputDialog.setOpaque(true);
        inputDialog.setBackground(new Color(239, 250, 252));
        add(scrollInputDialog, cDown);

        GridBagConstraints cButton = new GridBagConstraints();
        cButton.weightx = 0.2;
        cButton.weighty = 0.025;
        cButton.insets = new Insets(10, 10, 10, 10);
        cButton.fill = GridBagConstraints.BOTH;
        cButton.anchor = GridBagConstraints.CENTER;

        // add send button
        cButton.gridx = 3;
        cButton.gridy = 7;
        cButton.gridwidth = 2;
        cButton.gridheight = 1;
        add(sendBtn, cButton);

        // add send file button
        cButton.weightx = 0.2;
        cButton.gridx = 5;
        cButton.gridy = 7;
        cButton.gridwidth = 1;
        cButton.gridheight = 1;
        add(sendFileBtn, cButton);

        // set preferences for JFrame
        setSize(600, 600);
        setVisible(true);
        getContentPane().setBackground(new Color(182, 236, 234));

        // add listener
        sendBtn.addActionListener(new sendBtnListener());
        sendFileBtn.addActionListener(new sendFileBtnListener());
    }

    public void appendChatWindowText() {
        chatWindow.append(chatWith + ": " + client.getMessage() + "\n");
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
            // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
            // TODO Auto-generated method stub

        System.out.println("Windows Closed!");
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
            // TODO Auto-generated method stub

        // remove this private chat window from list
        privateChatList.put(chatWith, null);
        dispose();
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
            // send private message to person you are chatting with
            String str = inputDialog.getText().toString();
            client.output_to_p2p(chatWith, str);

            chatWindow.append("me: " + str + "\n");

            inputDialog.setText("");
        }
    }

    private class sendFileBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new FileChooserUI(client, 1, chatWith);
        }
    }
}
