package src;
import src.UI.*;

/*this is a main function*/
public class newClient {

    private Client client;

    public newClient() {
        client = new Client("localhost",54321);
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
