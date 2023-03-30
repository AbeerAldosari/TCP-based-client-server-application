/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network330;

import java.io.*;       //DataInputStream , DataOutputStream         
import java.net.*;      // Socket                                    
import java.util.*;     // InputMismatchException , Scanner 
import java.util.Base64;//Base64 
import javax.crypto.*;  // Cipher , KeyGenerator , SecretKey         
import javax.crypto.spec.SecretKeySpec;



public class client {

    DataOutputStream toServer;// data transfer to server        
    DataInputStream fromServer;//data received from server      
    Socket ClientSocket;

    public client(String IP, int portNum) throws Exception {

         

        
        try {
            
            ClientSocket = new Socket(IP, portNum);//Create a socket 
            System.out.println("###########################################################");
            System.out.println("#       Client contact with a IP server :"+ IP +"        #");
            
            while (!ClientSocket.isClosed()) { // while client is connected
                
                //create inner & outer of stream
                toServer = new DataOutputStream(ClientSocket.getOutputStream());
                fromServer = new DataInputStream(ClientSocket.getInputStream());

                //Client option 
                System.out.println("###########################################################");
                System.out.println("#                     please select:                      #");
                System.out.println("###########################################################");
                System.out.println("#   1.Open mode                                           #");
                System.out.println("#                 2.Secure mode                           #");
                System.out.println("#                                   3.Quit application    #");
                System.out.println("###########################################################\n");
                
                //Read from Client 
                Scanner scan = new Scanner(System.in);
                String s1 = Integer.toString(scan.nextInt());

                // if the client choose '1' it means open mode option 
                if (s1.equals("1")) {

                    toServer.writeUTF(s1);//send number '1' to server 
                    if (fromServer.readUTF().equals(s1)) { //Server checks is it in open mode
                        System.out.println("Please enter your massage...\n");
                        String msg = scan.next();//Massage that client will Write 
                        toServer.writeUTF(msg);//The massage that send to server  
                        System.out.println("Your message has arrived successfully <3 \n");
                        System.out.println("the massage return from server is : " + fromServer.readUTF() +"\n");

                    }
                }//End if 1
                
                // if the client choose '2' it means secure mode 
               else if (s1.equals("2")) {
                    toServer.writeUTF(s1);//send 2 to server
                    if (fromServer.readUTF().equals(s1)) {// //Server checks is it in secure mode
                        System.out.println("Please enter your massage...\n");
                        String msg = scan.next();//Massage that client will Write 
                        String secmsg = encryptWithAESKey(msg);// Encrypt massage
                        toServer.writeUTF(secmsg + '\n');//The massage that send to server 
                        System.out.println("Your message has arrived successfully <3 \n");
                        System.out.println("The message return from server encrypted and its: " + fromServer.readUTF() +"\n");
                    }
                }//End if 2

                //   if the client Enter 3 it means close the connection 
              else  if (s1.equals("3")) {
                    toServer.writeUTF(s1);//send 3 to server
                    System.out.println("The client's communication is over\n");
                    ClientSocket.close();// close client socket
                    break;
                }//End if 3
              else {// The client entered a number that isn't from the required list  
              	System.err.println("Input invalid!! \n");
              	
              }

            }// end while 
            
        } // end try block 
        catch (InputMismatchException w) {//The client entered a letter or string   
            System.err.println(w+"\n");

       } // end Catch 1 block
          catch (Exception e) {//The client tried to connect, but the server was not working 
            System.err.println("ERROR Connection Server!!\n");

        }// end Catch 2 block 
      

    }//end constructor 

    public static String encryptWithAESKey(String data) throws Exception {//encrypt message with shared key using AES algorithm  
		
		
		  KeyGenerator generator = KeyGenerator.getInstance("AES");//KeyGenerator to generate the AES secret key.
		  generator.init(128);//generating the AES key with the size 128
		  SecretKey key = generator.generateKey();
		  
		  byte[] symmetricKey =key.getEncoded();//Generate Symmetric key
		  
		  SecretKey secKey = new SecretKeySpec(symmetricKey,"AES");// create secret key to cipher information.
		  
		  //cipher instance using the init() method with a secret key and encryption mode.
		  Cipher cipher = Cipher.getInstance("AES");
		  cipher.init(Cipher.ENCRYPT_MODE, secKey);
		  
		  byte[] newData = cipher.doFinal(data.getBytes());	//encrypt the data string by invoking the doFinal() method. 

		  return Base64.getEncoder().encodeToString(newData);//mssage back encrypted.
		 }//End encryptWithAESKey

    public static void main(String[] args) throws Exception {//to run code 
        String IP = "127.0.0.1";//default IP address
        int portNum = 2233;// server port number
        client c = new client(IP, portNum);

    }//End main
}//End class client
