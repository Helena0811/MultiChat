/*
 * Ű �Է��� �ؾ� ������ �޽����� �޴� ������ ����� ����
 * ���ѷ����� ���鼭 ������ �޽����� ���� ���簡 �ʿ��ϸ�, ���ѷ����� �����ؾ� �ϹǷ� ������� ����
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
	ClientMain main;		// Ŭ���̾�Ʈ ������ ��ü�� �޾ƿ���(JTextArea�� �޾ƿ͵� ������ ���ο� �������)
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
	
	// �����κ��� �޽��� �ޱ�(���)
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();		// ���
			main.area.append(msg+"\n");
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ������ �޽��� ������
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
