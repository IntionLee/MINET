import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class chatServer extends JFrame {
	private static JTextArea jta = new JTextArea();
	private ServerSocket server;
	private static List<Socket> list = new ArrayList<Socket>();
    private ExecutorService exec;
    
    //´´½¨Ò»¸ö·þÎñÆ÷
	public chatServer(){
		setLayout(new BorderLayout());
		add(new JScrollPane(jta),BorderLayout.CENTER);
		setTitle("ÁÄÌìÊÒ·þÎñÆ÷");
		setSize(500,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		try{
 			server = new ServerSocket(54321);
 			exec = Executors.newCachedThreadPool();
 			jta.append("·þÎñÆ÷ÒÑ¾­Æô¶¯\n");
 			Socket client = null;
 			//Ò»Ö±¼àÌýÊÇ·ñÓÐÐÂµÄ¿Í»§¶ËÒªÇóÁ¬½Ó
 			while(true){
 				client = server.accept();
 				//Ìí¼Óµ½ÒÑÁ¬½Ó¿Í»§¶ËÁÐ±í
 				list.add(client);
 				//Îª¿Í»§¶Ë´´½¨Ò»¸öÏß³Ì
 				exec.execute(new Task(client));
 			}
		} catch(Exception e){
		} finally {
			// The connection is closed for one reason or another,
			// so have the server dealing with it
			try {
				for(Socket client:list){
				    client.close();
				}
			} catch (IOException ie) {
				System.out.println("Error closing");
				ie.printStackTrace();
			}		
		}
	}
	
	static class Task implements Runnable{
		private Socket client;
        private DataInputStream fromclient;
        private DataOutputStream toclient;
        String msg;
        String ipAddress;//¼ÇÂ¼¿Í»§¶ËµÄip
        int port;//¼ÇÂ¼¿Í»§¶ËµÄ¶Ë¿Ú
        
		public Task(Socket client) throws IOException{
  			this.client = client;
  			fromclient = new DataInputStream(client.getInputStream());
		}
		
		public void run() {
			try{
				//Ò»Ö±¼àÌý¿Í»§¶ËÊÇ·ñÓÐ·¢ËÍÏûÏ¢
				while((msg=fromclient.readUTF())!=null){
					ipAddress = client.getInetAddress().toString();
                    port =  client.getPort();
					jta.append(msg+"\n");
					//ÅÐ¶Ï¿Í»§¶Ë·¢ËÍµÄÏûÏ¢ÊÇÄÄÕÅÀàÐÍµÄÏûÏ¢
					judeMessage(msg);
				}
			}catch(Exception e){
			}
		}
		
		public void judeMessage(String message) throws IOException {
			if (message.startsWith("request:list")) {
				//ask for the user list
				sendClientList();
			} else if (message.startsWith("flag:1")) {
				//chat with all users
				sendMessageToAll();
			} else if (message.startsWith("flag:2")) {
				//chat with one user
				sendMessageToOne();
			} else if (message.startsWith("flag:3")) {
				//close the client
				removeConnection();
			} else if (message.startsWith("request:clientname")) {
				//ask for the client's name
				sendClientName();
			}
		}
		
		public void sendClientName() throws IOException {
			String message = "request:clientname" + "["+ipAddress+"]" + "["+port+"]";
			toclient= new DataOutputStream(client.getOutputStream());
			toclient.writeUTF(message);
		}
		public void sendMessageToOne() throws IOException {
			String message = msg;
			message = message.replace("flag:2", "");
			//·¢¸øP2PµÄÁíÒ»·½
			for (Socket client:list) {
				if (message.startsWith("["+client.getInetAddress()+"]" + "["+client.getPort()+"]")) {
					message = message.replace("["+client.getInetAddress()+"]" + "["+client.getPort()+"]", "");
					toclient= new DataOutputStream(client.getOutputStream());
					message = "flag:2" + "["+ipAddress+"]" + "["+port+"]" + message;
					toclient.writeUTF(message);
					jta.append("p2p:" + message+"\n");
				}
			}
			//·¢¸ø×Ô¼º£¬messageÒÑ¾­´¦ÀíºÃ£¬Ö±½Ó·¢ËÍ
			/*for (Socket client:list) {
			    if (client.getInetAddress().toString().equals(ipAddress) && client.getPort()== port) {
			    	toclient= new DataOutputStream(client.getOutputStream());
					toclient.writeUTF(message);
				}
			}*/
		}
		
		public void sendMessageToAll() throws IOException {
			//ÈºÁÄ Ã¿Ò»¸ö¶¼·¢ËÍ
			for(Socket client:list){
				toclient= new DataOutputStream(client.getOutputStream());
				String message = msg;
				message = message.replace("flag:1","");
				message = "flag:1" + "["+ipAddress+"]" + "["+port+"]" + ":" + message;
				toclient.writeUTF(message);
			}
		}
		
		public void sendClientList() throws IOException {
			//·µ»ØÔÚÏßÓÃ»§ÁÐ±í
			String sendList = "request:list";
			int num = list.size();
			sendList += num + "\n";
			toclient= new DataOutputStream(client.getOutputStream());
			for(Socket client:list){
				sendList += "["+client.getInetAddress()+"]" + "["+client.getPort()+"]" + "\n";				
			}
			toclient.writeUTF(sendList);	
		}
		
		public void removeConnection() {
			jta.append("remove client"+"\n");
			//remove the client from list
			for(int i = 0, len = list.size(); i< len; i++){  
				  if(list.get(i)== client){  
				       list.remove(i);  
				       --len;
				       --i;
				  }  
			}
			try {
				//¹Ø±ÕÁ¬½Ó
				client.close();
			} catch (IOException ie) {
				System.out.println("Error closing " + client);
				ie.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args){
		new chatServer();
	}

}
