package multi.server;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import multi.client.ClientThread;

public class ServerMain extends JFrame implements ActionListener, Runnable{

	JPanel p_north; 
	JTextField t_port;
	JButton bt_start;
	JTextArea area;
	JScrollPane scroll;
	int port=7777;
	
	// 서버 가동하는 기능이 메인쓰레드에 묶여있으면 X, 서버 가동용 쓰레드
	Thread thread;
	
	ServerSocket server;
	Socket socket;

	/*	
	BufferedReader buffr;
	BufferedWriter buffw;
	*/

	// 멀티캐스팅을 위해서는 현재 서버에 몇명이 들어오고 나가는지를 체크할 저장속 필요, 유연해야 함
	// -> collection framework 사용
	// 접속하는 모든 클라이언트의 정보 저장
	/*
	 * ArrayList는  특정 index에 누군가가 동시에 접근하면 접근 제한이 없어 데이터가 엉킴, 속도 빠름
	 * Vector는		특정 index에 동시에 접근하면 접근제한을 걸어 안정적, 속도 느림		
	 * 
	*/
	// 접속자 자체를 Generic type으로 지정
	Vector<ServerThread> list=new Vector<ServerThread>();
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port) , 10);
		bt_start = new JButton("가동");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		p_north.add(t_port);
		p_north.add(bt_start);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		bt_start.addActionListener(this);
		
		setBounds(600,100,300,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	// 서버 가동
	public void startServer(){
		try {
			// 즉, 서버는 죽으면 안됨
			// 또한, 서버는 대화를 나누는 주체가 되서는 안되고, 여러 사람을 받아들이는 접속 역할!!!
			// 대화는 클라이언트와 1:1 매칭되는 server의 avatar 객체가 수행 
			// -> 서로 독립적으로 수행해야 하기 때문에 avatar는 쓰레드

			port=Integer.parseInt(t_port.getText());
			server=new ServerSocket(port);
			area.append("서버 생성\n");
			
			while(true){
				// 클라이언트가 오길 기다림
				socket=server.accept();
				String ip=socket.getInetAddress().getHostAddress();
				area.append(ip+" 사용자 접속\n");
				
				// 접속자가 발견되면 쓰레드를 접속자에 하나씩 할당하여 대화 가능
				ServerThread st=new ServerThread(socket, this);
				st.start();
				
				// 접속자가 발견되면, 이 접속자와 대화를 나눌 쓰레드를 Vector에 담기
				// -> 멀티캐스팅
				list.add(st);
				area.append("현재 접속자의 수는 "+list.size()+"\n");

			}
			/*
			// 대화를 나누는 것은 server의 avatar 역할
			// 접속자 마다 따로 가져야 하므로 avatar 쓰레드로 넘기기
			// 대화를 나눌 스트림 생성			
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// 서버는 듣고 쓴다!
			String msg=null;
			
			msg=buffr.readLine();		// 클라이언트의 말 읽기
			buffw.write(msg+"\n");	// 클라이언트의 말 쓰기
			buffw.flush();					// 버퍼 비우기
			
			area.append(msg+"\n");
			*/
	
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// 가동 버튼을 누르면 쓰레드가 메모리에 올라감
		thread=new Thread(this);
		thread.start();
	}
	
	// 서버 가동용 쓰레드 실행 메소드
	public void run() {
		startServer();
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}

}
