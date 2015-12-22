/*
 * @(#)FileChooserUI.java    1.0 2015/12/08
 */

package src.UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

import src.*;

/**
 * provide the GUI of file choosing window
 *
 * @version 1.0 08 Dec 2015
 */
public class FileChooserUI {

    private JFileChooser fileChooser;

    public FileChooserUI(Client client, int mode, String receiver) {
        fileChooser = new JFileChooser();
        if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
            //用户点击了确定，取得路径选择
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            String name = fileChooser.getSelectedFile().getName();
            if (mode == 0) {
                // send file to chatroom
                client.output_file_to_chatroom(new File(path), name);
            } else {
                // send file to certain person
                client.output_file_to_p2p(receiver, new File(path), name);
            }
        }
    }
}
