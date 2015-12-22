package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream; 

import java.util.Scanner;
import java.util.ArrayList;


public class Client {
    /*constructor of Client
      connect to  Server*/
    private Socket socket;
    private DataOutputStream output_;
    private DataInputStream input_;
    private FileInputStream input_file;
    private FileOutputStream output_file;
    private byte[] sendBytes;
    private byte[] inputBytes;
    private String message;
    private String sender;
    private String filename_;
    private String clientname;
    private ArrayList<String> userlist;

    /*constructor of Client
      connect to  Server*/
    public Client(String hostip, int port) {
        try {
            socket=new Socket(hostip, port);
            output_ = new DataOutputStream(socket.getOutputStream());
            input_ = new DataInputStream(socket.getInputStream());
            input_file = null;
            output_file = null;
            sendBytes = null;
            inputBytes = null;
            message = "";
            sender = "";
            clientname = "";
            filename_ = "";
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

    /*output a file to chatroom (all users)*/
    public void output_file_to_chatroom(File file, String filename) {
        try {
            long l = file.length();
            String l_ = String.valueOf(l);
            output_.writeUTF("file_flag:1"+filename+'\n'+l_);
            output_.flush();

            int length = 0;
            double sumL = 0;
            input_file = new FileInputStream(file);
            sendBytes = new byte[1024];
            while ((length = input_file.read(sendBytes, 0, sendBytes.length)) > 0) {  
                sumL += length;    
                System.out.println("Transmit"+((sumL/l)*100)+"%");  
                output_.write(sendBytes, 0, length);  
                output_.flush();  
            }
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

    /*send a clientname request to Server*/
    public void requestClientname() {
        try {
            output_.writeUTF("request:clientname");
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
    public void output_to_p2p(String receiver, String str) {
        try {
            output_.writeUTF("flag:2"+receiver+str);
            output_.flush();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*output a file to the other user, this is a p2p output*/
    public void output_file_to_p2p(String receiver, File file, String filename) {
        try {
            long l = file.length();
            String l_ = String.valueOf(l);
            output_.writeUTF("file_flag:2"+receiver+filename+"\n"+l_);
            output_.flush();

            int length = 0;
            double sumL = 0;
            input_file = new FileInputStream(file);
            sendBytes = new byte[1024];
            while ((length = input_file.read(sendBytes, 0, sendBytes.length)) > 0) {  
                sumL += length;    
                System.out.println("Transmit"+((sumL/l)*100)+"%");  
                output_.write(sendBytes, 0, length);  
                output_.flush();  
            }
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
     if return 3, the Server send a online user list
     if return 4, the Server send a clientname of this client
     if return 5, the Server send a file from others*/
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
            } else if (str.startsWith("request:clientname")) {
                /*get clientname, store it as clientname*/
                clientname = str.replace("request:clientname","");
                flag = 4;
            }  else if (str.startsWith("file_flag:1")) {
                /*get chatroom file,  store the sender as sender
                    and store file at folder files */
                str = str.replace("file_flag:1","");
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
                filename_ = "";
                str = str.replace(sender,"");
                str1 = str.toCharArray();
                for(int i = 0; str1[i] != '\n'; i++) {
                    filename_ = filename_+str1[i];
                }
                str = str.replace(filename_+"\n","");

                int length = 0;
                long l = Long.parseLong(str); 

                File directory = new File("..");
                String path = directory.getCanonicalPath() + "/files";
                File f = new File(path);

                if(!f.exists()){  
                    f.mkdir();    
                }

                long cur = 0;
                output_file = new FileOutputStream(new File(
                    path + "/" +filename_));
                inputBytes = new byte[1024];
                while (true) {
                    length = input_.read(inputBytes, 0, inputBytes.length);
                    cur += length;
                    output_file.write(inputBytes, 0, length);  
                    output_file.flush(); 
                    if (cur >= l) {
                        output_file.close();
                        break;
                    }   
                }
                flag = 5;
            } else if (str.startsWith("file_flag:2")) {
                /*get chatroom file,  store the sender as sender
                    and store file at folder files */
                str = str.replace("file_flag:2","");
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
                filename_ = "";
                str = str.replace(sender,"");
                str1 = str.toCharArray();
                for(int i = 0; str1[i] != '\n'; i++) {
                    filename_ = filename_+str1[i];
                }
                str = str.replace(filename_+"\n","");

                int length = 0;
                long l = Long.parseLong(str); 
                
                File directory = new File("..");
                String path = directory.getCanonicalPath() + "/files";
                File f = new File(path); 

                if(!f.exists()){  
                    f.mkdir();    
                }

                long cur = 0;
                output_file = new FileOutputStream(new File(
                    path + "/" +filename_));
                inputBytes = new byte[1024];
                while (true) {
                    length = input_.read(inputBytes, 0, inputBytes.length);
                    cur += length;
                    output_file.write(inputBytes, 0, length);  
                    output_file.flush(); 
                    if (cur >= l) {
                        output_file.close();
                        break;
                    }   
                }
                flag = 5;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
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

    /*return the clientname of this Client*/
    public String getClientname() {
        return clientname;
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
