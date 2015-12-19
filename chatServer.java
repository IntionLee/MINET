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
    
    //创建一个服务器
	public chatServer(){
		setLayout(new BorderLayout());
		add(new JScrollPane(jta),BorderLayout.CENTER);
		setTitle("聊天室服务器");
		setSize(500,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		try{
 			server = new ServerSocket(54321);
 			exec = Executors.newCachedThreadPool();
 			jta.append("服务器已经启动\n");
 			Socket client = null;
 			//一直监听是否有新的客户端要求连接
 			while(true){
 				client = server.accept();
 				//添加到已连接客户端列表
 				list.add(client);
 				//为客户端创建一个线程
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
        String ipAddress;//记录客户端的ip
        int port;//记录客户端的端口
        
		public Task(Socket client) throws IOException{
  			this.client = client;
  			fromclient = new DataInputStream(client.getInputStream());
		}
		
		public void run() {
			try{
				//一直监听客户端是否有发送消息
				while((msg=fromclient.readUTF())!=null){
					ipAddress = client.getInetAddress().toString();
                    port =  client.getPort();
					jta.append(msg+"\n");
					//判断客户端发送的消息是哪张类型的消息
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
			//发给P2P的另一方
			for (Socket client:list) {
				if (message.startsWith("["+client.getInetAddress()+"]" + "["+client.getPort()+"]")) {
					message = message.replace("["+client.getInetAddress()+"]" + "["+client.getPort()+"]", "");
					toclient= new DataOutputStream(client.getOutputStream());
					message = "flag:2" + "["+ipAddress+"]" + "["+port+"]" + message;
					toclient.writeUTF(message);
					jta.append("p2p:" + message+"\n");
				}
			}
			//发给自己，message已经处理好，直接发送
			/*for (Socket client:list) {
			    if (client.getInetAddress().toString().equals(ipAddress) && client.getPort()== port) {
			    	toclient= new DataOutputStream(client.getOutputStream());
					toclient.writeUTF(message);
				}
			}*/
		}
		
		public void sendMessageToAll() throws IOException {
			//群聊 每一个都发送
			for(Socket client:list){
				toclient= new DataOutputStream(client.getOutputStream());
				String message = msg;
				message = message.replace("flag:1","");
				message = "flag:1" + "["+ipAddress+"]" + "["+port+"]" + ":" + message;
				toclient.writeUTF(message);
			}
		}
		
		public void sendClientList() throws IOException {
			//返回在线用户列表
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
			sendClientList();
			try {
				//关闭连接
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
