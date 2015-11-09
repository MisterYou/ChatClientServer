import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient extends Frame{
	
	Socket s = null;	
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnectd = false;
	
	TextField tftxt = new TextField();
	
	TextArea taContant = new TextArea();
	Thread tRecv = new Thread(new RecvThread());
	
	public static void main(String[] args){
		new ChatClient().launchFram();
	}
	
	public void launchFram(){
		setLocation(400,300);
		this.setSize(300, 300);
		add(tftxt,BorderLayout.SOUTH);//加在南边
		add(taContant,BorderLayout.NORTH);//加在北边
		pack();//调整此窗口的大小，以适合其子组件的首选大小和布局。
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
				disconnect();
				
			}
		});
		tftxt.addActionListener(new TFListener());
		setVisible(true);
		connect();
		
		tRecv.start();
		
	}
	
	public void connect(){
		try {
			s = new Socket("127.0.0.1",9999);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			bConnectd = true;
System.out.println("Connected");
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		/*
		try {
			bConnectd = false;
			
		   tRecv.join();
		
			
		}catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				dos.close();
				dis.close();
				s.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		*/
		
	}
	
	private class TFListener implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
			String str = tftxt.getText().trim();//trim去掉两边空格
			//taContant.setText(str);
			tftxt.setText("");
			try {
				dos.writeUTF(str);
				dos.flush();
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	private class RecvThread implements Runnable{
		public void run(){
			try{
				while(bConnectd){
					String str = dis.readUTF();
					//System.out.println(str);
					taContant.setText(taContant.getText() + str + "\n");
				}
			}catch(SocketException e){
				System.out.println("退出了");
			}
			catch(IOException e ){
				e.printStackTrace();
			}
		}
	}
	
}







