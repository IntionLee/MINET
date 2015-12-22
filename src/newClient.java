package src;
import src.UI.*;
import javax.swing.*;

/* this is a main function */
public class newClient {

    private Client client;

    public newClient() {
        // user login, input server's ip and port
        LoginPara loginPara = new LoginPara();
        LoginUI loginUI = new LoginUI(loginPara);
        while (loginPara.getFlag()) {
            System.out.print("");
        }

        // if login successfully, create a new client and chatroom UI
        client = new Client(loginPara.getIp(), 54321);
        ChatroomUI ui = new ChatroomUI(client);

        // keep detecting different kinds of messages
        while(true){
            int flag = client.input();
            if (flag == 1) {
                // someone sends a public message in the chatroom
                ui.appendChatWindowText();
            } else if (flag == 2) {
                // someone sends a private message to you
                ui.appendPrivateChatWindowText(client.getSender());
            } else if (flag == 3) {
                // refresh the user list when someone logins or logouts
                ui.refreshUserlist();
            } else if (flag == 5) {
                // someone sends you a file
                JOptionPane.showMessageDialog (null,
                    "you have received a new file",
                    "new information",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new newClient();
    }
}
