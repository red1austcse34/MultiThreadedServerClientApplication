
import java.io.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Asus
 */
public class ClientReader extends Thread{
    private InputStream stream;

    public ClientReader(InputStream stream) {
        this.stream = stream;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }
    
    public void run(){
        InputStreamReader inStream = new InputStreamReader(stream);
        BufferedReader in = new BufferedReader(inStream);
        while(true){
            try{
                String full = in.readLine();
                if(full.equals("logout"))break;
                String details[] = new String[2];
                details = full.split(":");
                if(details.length==1){
                    System.out.println(details[0]);
                }
                else if(details[0].equals("rqst")){
                    System.out.println(details[1] + " wants to add you as a friend");
                }
                else System.out.println(details[0] + ": " + details[1]);
            }catch(Exception e){
                
            }
            
        }
    }
}
