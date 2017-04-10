/*
 * 서버 대신 대화를 나눌 Avatar Thread
 * 접속한 클라이언트와 1:1로 대화를 나눌 쓰레드
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
	Socket socket;		// 대화를 나누는 데 필요한 스트림에 socket 필요
	ServerMain main;
	Vector<ServerThread>list;
	
	boolean flag=true;
	
	// 접속하는 모든 클라이언트의 정보 저장
	/*
	 * ArrayList는  특정 index에 누군가가 동시에 접근하면 접근 제한이 없어 데이터가 엉킴, 속도 빠름
	 * Vector는		특정 index에 동시에 접근하면 접근제한을 걸어 안정적, 속도 느림		
	 * 
	*/
	
	// 이 때 사용되는 소켓은 새로 만드는 것이 아니고, 서버 측에서 socket=server.accept();으로 얻어온 소켓 사용
	public ServerThread(Socket socket, ServerMain main) {
		this.socket=socket;
		this.main=main;
		
		try {
			// 대화를 나누기 위한 스트림 메모리에 올리기
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 서버 측은 먼저 듣고 보내야 하므로 listen() 메소드 내에 send()추가
	// 클라이언트로부터 메시지를 받아 듣기
	public void listen(){
		String msg=null;
		try {
			msg=buffr.readLine();		// 듣기
			main.area.append(msg+"\n");
			
			send(msg);					// 다시 보내기
		} catch (IOException e) {
			// 사용자가 마음대로 나가면 IOException 에러 발생
			// 존재하지 않은 사용자에 대한 stream을 읽을 수 없기 때문에!
			//System.out.println("읽기 불가");
			flag=false;		// 쓰레드 동작을 위한 while문 flag
			
			// vector에서 이 쓰레드 제거
			main.list.removeElement(this);
			main.area.append("1명 퇴장 후 현재 접속자 수 "+main.list.size()+"\n");
			e.printStackTrace();

		}
	}
	
	// 클라이언트 측에 메시지 보내기
	public void send(String msg){
		try {
			
			// 현재 접속한 클라이언트 모두에게 메시지 보내기
			// 접속한 클라이언트는 메모리 상에서만 저장해놓고 사용(collection framework 사용)
			// -> oracle과 같은 하드에 저장하면 쉴새없이 클라이언트가 접속했다 종료하기 때문에 비효율
			for(int i=0; i<main.list.size(); i++){
				// 현재 list에 저장되어 있는 클라이언트에게 보내기
				// 벡터 내 쓰레드의.buffw.write();
				// 벡터 내 쓰레드의.buffw.flush();
				
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
