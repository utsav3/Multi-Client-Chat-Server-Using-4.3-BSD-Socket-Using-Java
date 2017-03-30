import java.io.*;
import java.net.*;

public class chatClient implements Runnable{
  //Client socket
  static Socket clientSocket=null;
  //Output stream
  static PrintStream os = null;
  //Input stream
  static DataInputStream is = null;

  static BufferedReader inputLine = null;
  static boolean closed = false;

  public static void main(String args[])throws Exception{

    System.out.println("Welcome to Chat Server");

    try{
        inputLine = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the Client IP Address:");
        String ipAdd = inputLine.readLine();

        System.out.println("Enter the Server Port Number:");
        String portno = inputLine.readLine();

        InetAddress address = InetAddress.getByName(ipAdd);

        clientSocket = new Socket(address,Integer.parseInt(portno));

        os = new PrintStream(clientSocket.getOutputStream());
        is = new DataInputStream(clientSocket.getInputStream());

    } catch (UnknownHostException e) {
    } catch (IOException e) {
    }


    if (clientSocket != null && os != null && is != null) {
       try {

             /* Create a thread to read from the server. */
             new Thread(new chatClient()).start();
             while (!closed) {
               os.println(inputLine.readLine().trim());
             }

             os.close();
             is.close();
             clientSocket.close();
       } catch (IOException e) {
         System.err.println("IOException:  " + e);
       }
    }

 }

 public void run() {
    //Keeps Reading from the server
    String responseLine;
    try {
      while ((responseLine = is.readLine()) != null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("Exited") != -1)
          break;
      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }

}
