package src;

import src.UI.*;

/*this is a test UI, it can be replaced*/
public class newClient {

    private Client client;

    public newClient() {
        LoginPara loginPara = new LoginPara();

        LoginUI loginUI = new LoginUI(loginPara);
        while (loginPara.getFlag()) {
            System.out.print("");
        }
        
        client = new Client(loginPara.getIp(),
            loginPara.getPortNum());
        ChatroomUI ui = new ChatroomUI(client);

        while(true){
            int flag = client.input();
            if (flag == 1) {
                ui.appendChatWindowText();
            } else if (flag == 2) {
                ui.appendPrivateChatWindowText(client.getSender());
            } else if (flag == 3) {
                ui.refreshUserlist();
            }
        }
    }

    public static void main(String[] args) {
        new newClient();
    }
}
