/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Asus
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    
    public static ArrayList <ClientHandler> clients = new ArrayList<>();
    
    public static ClientHandler findUser(String s){
        for(ClientHandler c : clients){
            if(c.getUsername().equals(s)){
                return c;
            }
        }
        return null;
    }
    
    public static String login(String userName, String password){
        ClientHandler c = findUser(userName);
        if(c==null)return "User doesnt exist";
        else if(password.equals(c.getPassword())){
            c.setIsLoggedIn(true);
            return "Logged in";
        }
        else return "Password doesn't match";
    }
    
    public static void main(String args[]){
        try{
            ServerSocket sSocket = new ServerSocket(7777);
            while(true){
                Socket cSocket = sSocket.accept();
                InputStreamReader inFromClient = new InputStreamReader(cSocket.getInputStream());
                BufferedReader in = new BufferedReader(inFromClient);
                DataOutputStream out = new DataOutputStream(cSocket.getOutputStream());
                String msg = in.readLine();
                System.out.println(msg);
                String s[] = msg.split(":");
                if(s[0].equals("log")){
                    String log = login(s[1], s[2]);
                    out.writeBytes(log + '\n');
                    while(!log.equals("Logged in")){
                        msg = in.readLine();
                        System.out.println(msg);
                        s = msg.split(":");
                        if(s[0].equals("log")){
                            log = login(s[1], s[2]);
                            out.writeBytes(log + '\n');
                        }
                        else if(s[0].equals("reg")){
                            clients.add(new ClientHandler(s[1], s[2], cSocket));
                            System.out.println("Registered " + s[1]);
                            log = "not logged";
                        }
                    }
                }
                
                    
                
                boolean found = false;
                while(!found){
                    for(ClientHandler c : Server.clients){
                        if(cSocket.equals(c.getSocket())){
                            c.start();
                            found = true;
                            break;
                        }
                    }
                }
            }
            
        }catch(Exception e){
            
        }  
    }
}
