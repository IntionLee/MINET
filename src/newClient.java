package src;

import src.UI.*;

/*this is a test UI, it can be replaced*/
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
                //System.out.println(client.getSender()+client.getmessage());
                // jta1.append("p2p:"+client.getSender()+client.getMessage()+"\n");
                ui.appendPrivateChatWindowText(client.getSender());
            } else if (flag == 3) {
                ui.refreshUserlist();
            }
            // jta1.append(client.input()+"\n");
        }
    }

    // private class buttonListener implements ActionListener{
    //     @Override
    //     public void actionPerformed(ActionEvent e) {
    //         String str = jtf1.getText().toString();
    //         client.output_to_chatroom(str);
    //     }
    // }

    // private class buttonListener2 implements ActionListener{
    //     @Override
    //     public void actionPerformed(ActionEvent e) {
    //         String user = jtf2.getText().toString();
    //         String str = jtf1.getText().toString();
    //         client.output_to_p2p(user, str);
    //         jta1.append("p2p:me:"+str+"\n");
    //     }
    // }

    // private class buttonListener3 implements ActionListener{
    //     @Override
    //     public void actionPerformed(ActionEvent e) {
    //         client.requestUserlist();
    //     }
    // }

    public static void main(String[] args) {
        new newClient();
    }
}
