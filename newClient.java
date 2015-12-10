import java.io.BufferedReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.Scanner;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


class Client {
    private Socket socket;
    private DataOutputStream output_;
    private DataInputStream input_;
    private String message;
    private String sender;
    private ArrayList<String> userlist;

    /*constructor of Client
      connect to  Server*/
    public Client(String hostip, int port) {
        try {
            socket=new Socket(hostip, port);
            output_ = new DataOutputStream(socket.getOutputStream());
            input_ = new DataInputStream(socket.getInputStream());
            message = "";
            sender = "";
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*output message to chatroom(all user)*/
    public void output_to_chatroom(String str) {
        try {
            output_.writeUTF("flag:1"+str);
            output_.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*send a userlist request to Server*/
    public void requestUserlist() {
        try {
            output_.writeUTF("request:list");
            output_.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*output to the other user, this is a p2p output
      when call this function, you have to give who you want to send
      and what you want to send*/
    public void output_to_p2p(String user, String str) {
        try {
            output_.writeUTF("flag:2"+user+str);
            output_.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*The function just return flag value to tell UI the type of this input
     If UI wants to get the input message or sender or userlist
     it need to use function getmessage() and getSender() and getUserlist()
     if return 1, the Server send a chatroom message
     if return 2, the Server send a p2p message
     if return 3, the Server send a online user list*/
    //public String input() {
    public int input() {
        int flag = 0;
        String str = "";
        try {
            str = input_.readUTF();
            if (str.startsWith("request:list")) {
                /*get userlist from Server, store the string which server sent
                  and store it in userlist*/
                userlist = new ArrayList<String>();
                str = str.replace("request:list","");
                char[] str1 = str.toCharArray();
                String num = "";
                for(int i = 0; str1[i] != '\n';i++) {
                    num = num+str1[i];
                }
                str = str.replace(num+'\n',"");
                str1 = str.toCharArray();
                int count = Integer.parseInt(num);
                String user_ = "";
                for(int i = 0, rem = 0; rem < count; i++) {
                    if(str1[i] == '\n') {
                        userlist.add(user_);
                        user_ = "";
                        rem++;
                    } else {
                        user_ = user_+str1[i];
                    }
                }
                flag = 3;
            } else if (str.startsWith("flag:1")) {
                /*get chatroom message, store it as message*/
                message = str.replace("flag:1","");
                flag = 1;
            } else if (str.startsWith("flag:2")) {
                /*get message from other user, store the sender as sender
                  and store message as massage*/
                str = str.replace("flag:2","");
                sender = "";
                char[] str1 = str.toCharArray();
                for(int i = 0, count = 0; count != 2; i++) {
                    if(str1[i] == ']') {
                        sender = sender+str1[i];
                        count++;
                    } else {
                        sender = sender+str1[i];
                    }
                }
                message = str.replace(sender,"");
                flag = 2;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
        //return str;
    }

    /*return the massage which send to Client*/
    public String getMessage() {
        return message;
    }

    /*return the sender which send massage to Client*/
    public String getSender() {
        return sender;
    }

    /*return the list of online user*/
    public ArrayList<String> getUserlist() {
        return userlist;
    }

    /*close connection*/
    public void close() {
        try {
            output_.writeUTF("flag:3");
            output_.flush();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }
}

/*this is a test UI, it can be replaced*/
public class newClient extends JFrame{
    private JPanel p= new JPanel();
    private JTextField jtf1 = new JTextField("content");
    private JTextField jtf2 = new JTextField("to");
    private JTextArea jta1 = new JTextArea();
    private JButton button  = new JButton("发送");
    private JButton button2  = new JButton("发送P2P");
    private JButton button3  = new JButton("UserList");
    private Client client;
    public newClient(){
        p.setLayout(new BorderLayout());  
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());   
        p2.add(jtf1,BorderLayout.CENTER);
	p2.add(jtf2,BorderLayout.NORTH);
        p2.add((new JLabel("chatroom")),BorderLayout.SOUTH);
        p2.add(button,BorderLayout.EAST);
	p2.add(button2,BorderLayout.WEST);
        p2.add(button3,BorderLayout.SOUTH);
        p.add(p2,BorderLayout.SOUTH);
        p.add(jta1,BorderLayout.CENTER);
        this.setTitle("最简单的聊天室-----antking");
        this.add(p);
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        button.addActionListener(new buttonListener());
	button2.addActionListener(new buttonListener2());
        button3.addActionListener(new buttonListener3());
   
        client = new Client("localhost",54321);
        while(true){
	   int flag = client.input();
	   if (flag == 1) {
		jta1.append("chatroom:"+client.getMessage()+"\n");
	   } else if (flag == 2) {
		//System.out.println(client.getSender()+client.getmessage());
		jta1.append("p2p:"+client.getSender()+client.getMessage()+"\n");
	   } else if (flag == 3) {
                ArrayList<String> userlist = client.getUserlist();
                for(String str:userlist){
                   jta1.append("userlist:"+str+"\n");
                }
           }
           //jta1.append(client.input()+"\n");
        }
    }

     private class buttonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = jtf1.getText().toString();
            client.output_to_chatroom(str);
        }
     
    }

    private class buttonListener2 implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String user = jtf2.getText().toString();
	    String str = jtf1.getText().toString();
            client.output_to_p2p(user, str);
            jta1.append("p2p:me:"+str+"\n");
        }
     
    }
    private class buttonListener3 implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            client.requestUserlist();
        }
     
    }
    public static void main(String[] args) {
        new newClient();
    }
}
