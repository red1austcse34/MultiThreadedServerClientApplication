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

public class ClientWriter extends Thread{
    private OutputStream stream;

    public ClientWriter(OutputStream stream) {
        this.stream = stream;
    }

    public OutputStream getStream() {
        return stream;
    }

    public void setStream(OutputStream stream) {
        this.stream = stream;
    }
    
    public void run(){
        Scanner sc = new Scanner(System.in);
        DataOutputStream out = new DataOutputStream(stream);
        
        try{
            while(true){
                String msg = sc.nextLine();
                out.writeBytes(msg + '\n');
                if(msg.equals("logout"))break;
            }
        }catch(Exception e){
            
        }
    }
}
