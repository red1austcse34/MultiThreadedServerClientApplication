
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
public class ClientHandler extends Thread{
    private String username, password;
    private ArrayList <ClientHandler> friends = new ArrayList<>();
    private ArrayList <String> groups = new ArrayList<>();
    private boolean isLoggedIn;
    private Socket socket;

    public ClientHandler(String username, String password, Socket socket) {
        this.username = username;
        this.password = password;
        this.socket = socket;
        isLoggedIn = false;
    }
    
    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    public ClientHandler findUser(String s){
        for(ClientHandler c : Server.clients){
            if(c.getUsername().equals(s)){
                return c;
            }
        }
        return null;
    }
    
    public void addFriend(String s){
        try{
            ClientHandler c = findUser(s);
            DataOutputStream out = new DataOutputStream(c.getSocket().getOutputStream());
            out.writeBytes("rqst:" + this.getUsername() + '\n');
        }catch(Exception e){
            
        }
    }
    
    public void addGroup(String s[]){
        try{
            ClientHandler c = this;
            c.groups.add(s[1]);
            int n = s.length;
            DataOutputStream out;
            for(int i=2; i<n; i++){
                c = findUser(s[i]);
                c.groups.add(s[1]);
                out = new DataOutputStream(c.getSocket().getOutputStream());
                out.writeBytes(this.getUsername() + " has added you in Group: " + s[1] +'\n');
            }
            
        }catch(Exception e){
            
        }
    }
    
    
    public void respondFrnd(String s[]){
        try{
            ClientHandler c = findUser(s[1]);
            DataOutputStream out = new DataOutputStream(c.getSocket().getOutputStream());
            if(s[0].equals("acc")){
                this.friends.add(c);
                c.friends.add(this);
                out.writeBytes("You and " + this.getUsername() + " are now friends." + '\n');
            }
            else{
                out.writeBytes(this.getUsername() + " denied your friend request." + '\n');
            }
        }catch(Exception e){
            
        }
    }
    
    public void broadcast(String s){
        try{
            DataOutputStream out;
            for(ClientHandler c : Server.clients){
                if(c.isLoggedIn && !c.equals(this)){
                    out = new DataOutputStream(c.getSocket().getOutputStream());
                    out.writeBytes(this.getUsername()+ ":" + s + '\n');
                }
            }
        }catch(Exception e){
            
        }
    }
    
    public void showList(){
        try{
            DataOutputStream out = new DataOutputStream(this.getSocket().getOutputStream());
            for(ClientHandler c : Server.clients){
                if(c.isLoggedIn){
                    out.writeBytes(c.getUsername() + '\n');
                }
            }
        }catch(Exception e){
            
        }
    }
    
    public void sendMessage(String s[]){
        int l = s.length;
        try{
            DataOutputStream out;
            for(int i=2; i<l; i++){
                ClientHandler c = findUser(s[i]);
                if(c==null)return;
                out = new DataOutputStream(c.getSocket().getOutputStream());
                out.writeBytes(this.getUsername()+ ":" + s[1] + '\n');
            }
        }catch(Exception e){
            
        }
        
    }
    public void sendGroupMessage(String s[]){
        try{
            DataOutputStream out;
            for(ClientHandler c : Server.clients){
                for(String g : c.groups){
                    if(g.equals(s[2])){
                        out = new DataOutputStream(c.getSocket().getOutputStream());
                        out.writeBytes(s[2]+ ":" + s[1] + '\n');
                    }
                }
            }
        }catch(Exception e){
            
        }
        
    }
    
    public void run(){
        try{
            System.out.println(username + " logged in");
            InputStreamReader inFromClient = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(inFromClient);
            do{
                String full = in.readLine();
                if(full.equals("logout")){
                    this.isLoggedIn = false;
                    DataOutputStream out = new DataOutputStream(this.getSocket().getOutputStream());
                    System.out.println(this.username + " logged out" + '\n');
                    out.writeBytes("logout" + '\n');
                    break;
                }
                String details[] = new String[100];
                details = full.split(":");
                if(details[0].equals("msg"))sendMessage(details);
                else if(details[0].equals("acc") || details[0].equals("deny"))respondFrnd(details);
                else if(details[0].equals("list"))showList();
                else if(details[0].equals("broad"))broadcast(details[1]);
                else if(details[0].equals("add"))addFriend(details[1]);
                else if(details[0].equals("addgrp"))addGroup(details);
                else if(details[0].equals("grpmsg"))sendGroupMessage(details);
            }while(true);
            socket.close();
        }catch(Exception e){
            
        }
    }
    
}
