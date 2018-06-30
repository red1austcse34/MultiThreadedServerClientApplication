
import java.io.*;
import java.net.*;
import java.util.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Asus
 */
public class Client {
    public static void main(String args[]){
        try{
            Scanner sc = new Scanner(System.in);
            boolean logged = false;
            Socket s = new Socket();
            Socket temp = new Socket("localhost", 7777);
            
            Login:
            while(!logged){
                System.out.print("Enter Username: ");
                String username = sc.nextLine();
                System.out.print("Enter Password: ");
                String password = sc.nextLine();
            
                DataOutputStream out = new DataOutputStream(temp.getOutputStream());
                InputStreamReader inStream = new InputStreamReader(temp.getInputStream());
                BufferedReader in = new BufferedReader(inStream);
                out.writeBytes("log:" + username + ":" + password + '\n');
                String response = in.readLine();
                if(response.equals("Logged in")){
                    System.out.println(response);
                    break;
                }
                else if(response.equals("User doesnt exist")){
                    out.writeBytes("reg:" + username + ":" + password + '\n');
                    System.out.println("Registered");
                }
                else System.out.println(response);
            }
            new ClientReader(temp.getInputStream()).start();
            new ClientWriter(temp.getOutputStream()).start();
        }catch(Exception e){  
            
        } 
    }
}
