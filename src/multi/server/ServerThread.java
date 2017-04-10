/*
 * ���� ��� ��ȭ�� ���� Avatar Thread
 * ������ Ŭ���̾�Ʈ�� 1:1�� ��ȭ�� ���� ������
 * */
package multi.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JTextArea;

public class ServerThread extends Thread{
	BufferedReader buffr;
	BufferedWriter buffw;
	Socket socket;		// ��ȭ�� ������ �� �ʿ��� ��Ʈ���� socket �ʿ�
	ServerMain main;
	Vector<ServerThread>list;
	
	boolean flag=true;
	
	// �����ϴ� ��� Ŭ���̾�Ʈ�� ���� ����
	/*
	 * ArrayList��  Ư�� index�� �������� ���ÿ� �����ϸ� ���� ������ ���� �����Ͱ� ��Ŵ, �ӵ� ����
	 * Vector��		Ư�� index�� ���ÿ� �����ϸ� ���������� �ɾ� ������, �ӵ� ����		
	 * 
	*/
	
	// �� �� ���Ǵ� ������ ���� ����� ���� �ƴϰ�, ���� ������ socket=server.accept();���� ���� ���� ���
	public ServerThread(Socket socket, ServerMain main) {
		this.socket=socket;
		this.main=main;
		
		try {
			// ��ȭ�� ������ ���� ��Ʈ�� �޸𸮿� �ø���
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ���� ���� ���� ��� ������ �ϹǷ� listen() �޼ҵ� ���� send()�߰�
	// Ŭ���̾�Ʈ�κ��� �޽����� �޾� ���
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();		// ���
			main.area.append(msg+"\n");
			
			send(msg);					// �ٽ� ������
		} catch (IOException e) {
			// ����ڰ� ������� ������ IOException ���� �߻�
			// �������� ���� ����ڿ� ���� stream�� ���� �� ���� ������!
			//System.out.println("�б� �Ұ�");
			flag=false;		// ������ ������ ���� while�� flag
			
			// vector���� �� ������ ����
			main.list.removeElement(this);
			main.area.append("1�� ���� �� ���� ������ �� "+main.list.size()+"\n");
			e.printStackTrace();

		}
	}
	
	// Ŭ���̾�Ʈ ���� �޽��� ������
	public void send(String msg){
		try {
			
			// ���� ������ Ŭ���̾�Ʈ ��ο��� �޽��� ������
			// ������ Ŭ���̾�Ʈ�� �޸� �󿡼��� �����س��� ���(collection framework ���)
			// -> oracle�� ���� �ϵ忡 �����ϸ� �������� Ŭ���̾�Ʈ�� �����ߴ� �����ϱ� ������ ��ȿ��
			for(int i=0; i<main.list.size(); i++){
				// ���� list�� ����Ǿ� �ִ� Ŭ���̾�Ʈ���� ������
				// ���� �� ��������.buffw.write();
				// ���� �� ��������.buffw.flush();
				
				/*
				ServerThread st=main.list.elementAt(i);
				st.buffw.write(msg+"\n");
				st.buffw.flush();
				*/
				
				main.list.get(i).buffw.write(msg+"\n");
				main.list.get(i).buffw.flush();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(flag){
			listen();
		}
	}
}
