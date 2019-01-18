package tester;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Serv{
    
    int servPort = 10000;
    int fileCount;
    int maxByteSize = 1024;
    int byteCount;
    int id;
    File fileFolder = new File("D:/TORrent_$");
    File plikTxt = new File("D:/TORrent_$/plik.txt");
    File[] myFileList = fileFolder.listFiles();
    ServerSocket servSock = null;
    Socket clientSock = null;
    InputStream inputS = null;
    OutputStream outputS = null;
      
    public Serv(int id) throws IOException{
        log("server_start",1);
        this.id = id;
        servPort = servPort + id;
        
        
 /*               
        for (File f : fileFolder.listFiles()){
            byte [] byteArr = new byte[(int)f.length()];
            DataInputStream dataInput = new DataInputStream(new FileInputStream(f));
            dataInput.readFully(byteArr); 
            System.out.println(byteArr);
*/
       }
        
    
    
    public void fileList() throws FileNotFoundException, IOException{
        log("file list",1);
//file list drucken  
        for (int i = 0; i < myFileList.length; i++) {
            if (myFileList[i].isFile()) {
                myFileList[i].getName();
                System.out.println(myFileList[i]);
                fileCount++;
            }    
        }
        log("Number of FILES is " + fileCount);
    }
        
      
    
 /*   
    MessageDigest md = MessageDigest.getInstance("MD5");
try (InputStream is = Files.newInputStream(Paths.get("file.txt"));
     DigestInputStream dis = new DigestInputStream(is, md)) 
{
  // Read decorated stream (dis) to EOF as normal... 
}
byte[] digest = md.digest();
    
  */  
    public static void log(String s){
        DateFormat dF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date d = new Date();
	System.out.println("  #server: " + s + ", " + dF.format(d));
        wait(3);
    }
    
    public static void log(String s, int i){
        DateFormat dF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date d = new Date();
        System.out.println("  #server: " + s + ", " + dF.format(d));
        wait(i);
    }

    public static void wait (int i) {
        try { 
            Thread.sleep(i*1000);
        } catch (InterruptedException e1) {e1.printStackTrace();}	
    }

    public void listen() {
    	try {
        ServerSocket servSock = new ServerSocket(servPort);
        clientSock = servSock.accept();
        log("socket jest");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
        PrintWriter out = new PrintWriter(clientSock.getOutputStream(), true);
		out.println("OK");
		out.flush();
			String command = in.readLine();
			String params[] = command.split(" ");
			byte [] bytes;
	    	switch (params[0]) {
	    		case "SEND": 
					bytes  = new byte[Integer.parseInt(params[2])];
		    	    InputStream is = clientSock.getInputStream();
		    	    FileOutputStream fos = new FileOutputStream("D://TORrent_" + id + "/" + params[1]);
		    	    BufferedOutputStream bos = new BufferedOutputStream(fos);
					is.read(bytes, 0, bytes.length);
		    	    bos.write(bytes,0,bytes.length);
		    	    bos.flush();
		    	    fos.close();
		    	    bos.close();
	    			break;
	    		case "DOWNLOAD": 
					File file = new File("D://TORrent_" + id + "/" + params[1]);
					bytes  = new byte [(int)file.length()];
					out.println(file.length());
					out.flush();
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(bytes,0,bytes.length);
					clientSock.getOutputStream().write(bytes,0,bytes.length);
					clientSock.getOutputStream().flush();
					bis.close();
	    			break;
	    	}
		
        servSock.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args) throws IOException {
    	
        //log("start");
    	Serv s;
        s = new Serv(Integer.parseInt(args[0]));
    	
        while(true){
            s.listen();

        }

    }
    
}
