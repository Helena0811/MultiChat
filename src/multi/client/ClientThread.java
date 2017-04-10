/*
 * 키 입력을 해야 서버의 메시지를 받는 현재의 기능을 보완
 * 무한루프를 돌면서 서버의 메시지를 받을 존재가 필요하며, 무한루프를 실행해야 하므로 쓰레드로 정의
 * */
package multi.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientThread extends Thread{
	Socket socket;
	ClientMain main;		// 클라이언트 프레임 자체를 받아오기(JTextArea만 받아와도 되지만 새로운 방법으로)
	BufferedReader buffr;
	BufferedWriter buffw;
	
	public ClientThread(Socket socket, ClientMain main) {
		this.socket=socket;
		this.main=main;
		
		try {
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 서버로부터 메시지 받기(듣기)
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();		// 듣기
			main.area.append(msg+"\n");
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 서버에 메시지 보내기
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(true){
			listen();
		}
	}
}
