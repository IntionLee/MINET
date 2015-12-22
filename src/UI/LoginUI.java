/*
 * @(#)LoginUI.java    1.0 2015/12/08
 */

package src.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

import src.*;

/**
 * provide the GUI of login window
 *
 * @version 1.0 08 Dec 2015
 */
public class LoginUI extends JFrame {

    private JLabel labelUsername;
    private JLabel labelPassword;
    private JTextField inputUsername;
    private JTextField inputPassword;
    private JButton loginBtn;
    private LoginPara loginPara;

    public LoginUI(LoginPara loginPara_) {
        super("登录");
        setLayout(new GridBagLayout());

        loginPara = loginPara_;

        labelUsername = new JLabel("ip");
        inputUsername = new JTextField();
        loginBtn = new JButton("登陆");

        initUI();
    }

    private void initUI() {
        GridBagConstraints cTop = new GridBagConstraints();
        cTop.weightx = 1;
        cTop.weighty = 0.4;
        cTop.insets = new Insets(0, 0, 0, 0);
        cTop.fill = GridBagConstraints.BOTH;
        cTop.anchor = GridBagConstraints.CENTER;

        // add header
        JLabel head = new JLabel();
        cTop.gridx = 0;
        cTop.gridy = 0;
        cTop.gridwidth = 5;
        cTop.gridheight = 1;
        head.setOpaque(true);
        head.setBackground(new Color(111, 201, 242));
        add(head, cTop);

        GridBagConstraints cUsrAndPwd = new GridBagConstraints();
        cUsrAndPwd.weightx = 0;
        cUsrAndPwd.weighty = 0;
        cUsrAndPwd.insets = new Insets(20, 10, 0, 20);
        cUsrAndPwd.fill = GridBagConstraints.BOTH;
        cUsrAndPwd.anchor = GridBagConstraints.CENTER;

        // add empty label
        cUsrAndPwd.gridx = 0;
        cUsrAndPwd.gridy = 1;
        cUsrAndPwd.gridwidth = 1;
        cUsrAndPwd.gridheight = 1;
        add(new JLabel(), cUsrAndPwd);

        // add username label
        cUsrAndPwd.gridx = 1;
        cUsrAndPwd.gridy = 1;
        cUsrAndPwd.gridwidth = 1;
        cUsrAndPwd.gridheight = 1;
        add(labelUsername, cUsrAndPwd);

        // add username textfield
        cUsrAndPwd.gridx = 2;
        cUsrAndPwd.gridy = 1;
        cUsrAndPwd.gridwidth = 3;
        cUsrAndPwd.gridheight = 1;
        inputUsername.setOpaque(true);
        inputUsername.setBackground(new Color(217, 247, 246));
        add(inputUsername, cUsrAndPwd);

        GridBagConstraints cLogin = new GridBagConstraints();
        cLogin.weightx = 0;
        cLogin.weighty = 0;
        cLogin.insets = new Insets(10, 0, 10, 0);
        cLogin.fill = GridBagConstraints.BOTH;
        cLogin.anchor = GridBagConstraints.CENTER;

        // add login button
        cLogin.gridx = 2;
        cLogin.gridy = 3;
        cLogin.gridwidth = 1;
        cLogin.gridheight = 1;
        add(loginBtn, cLogin);

        // add listener for login button
        loginBtn.addActionListener(new loginBtnListener());

        // set preferences for JFrame
        setSize(300, 200);
        setVisible(true);
        setResizable(false);
        getContentPane().setBackground(new Color(182, 236, 234));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class loginBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginPara.setFlag(false);
            loginPara.setIp(inputUsername.getText());
            dispose();
        }
    }
}
