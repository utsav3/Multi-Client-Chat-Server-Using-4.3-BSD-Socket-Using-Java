
import java.io.*;
import java.net.*;

public class chatServer{
  //Server Socket
  static ServerSocket serverSocket = null;
  //Client Socket
  static Socket clientSocket=null;
  //Allows Max 10 Client Connections
  static final clientThread[] threads = new clientThread[10];

  public static void main(String args[]) throws Exception {
    System.out.println(" Welcome to Multi Client Chat Server");
    System.out.println("Enter the port number:");
    BufferedReader portno = new BufferedReader(new InputStreamReader(System.in));

    try{
      String portNo = portno.readLine();
      //Opens Server Socket w PortNo
      serverSocket = new ServerSocket(Integer.parseInt(portNo));

    } catch(IOException e) {
      System.out.println(e);
    }

    //Client Socket for each client that wants to get connected
    while(true){
      try{
        clientSocket = serverSocket.accept();
        int i=0;
        for(i=0; i<10;i++){
          if(threads[i] == null){
            (threads[i] = new clientThread(clientSocket, threads)).start();
            break;
          }
        }

      } catch (IOException e){
        System.out.println(e);
      }
    }

  }
}

class clientThread extends Thread {
  DataInputStream is = null;
  Socket clientSocket = null;
  PrintStream os = null;
  clientThread[] threads;
  String clientName = null;


  public clientThread(Socket clientSocket, clientThread[] threads) {
    this.clientSocket = clientSocket;
    this.threads = threads;
  }

  public void run() {

    clientThread[] threads = this.threads;

    try {
      //Input and Output Streams
      is = new DataInputStream(clientSocket.getInputStream());
      os = new PrintStream(clientSocket.getOutputStream());
      os.println("Enter your name.");
      String name = is.readLine().trim();
      os.println("Welcome to Chat Room " + name + " to exit enter quit");

for (int i = 0; i < 10; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].os.println("User " + name + " entered the chat room !!!");
        }
      }

while (true) {
        String line = is.readLine();
        if (line.startsWith("quit")) {
          break;
        }

for (int i = 0; i < 10; i++) {
          if (threads[i] != null) {
            threads[i].os.println("" + name + ": " + line);
          }
        }
}

for (int i = 0; i < 10; i++) {
        if (threads[i] != null && threads[i] != this) {
          threads[i].os.println("The user " + name + " left the chat !!!");
        }
      }

os.println("Chat Session Ended" + name );

for (int i = 0; i < 10; i++) {
        if (threads[i] == this) {
          threads[i] = null;
        }
      }

      is.close();
      os.close();
      clientSocket.close();
    } catch (IOException e) {
    }
  }
}
