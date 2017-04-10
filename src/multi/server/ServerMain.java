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
	
	// ���� �����ϴ� ����� ���ξ����忡 ���������� X, ���� ������ ������
	Thread thread;
	
	ServerSocket server;
	Socket socket;

	/*	
	BufferedReader buffr;
	BufferedWriter buffw;
	*/

	// ��Ƽĳ������ ���ؼ��� ���� ������ ����� ������ ���������� üũ�� ����� �ʿ�, �����ؾ� ��
	// -> collection framework ���
	// �����ϴ� ��� Ŭ���̾�Ʈ�� ���� ����
	/*
	 * ArrayList��  Ư�� index�� �������� ���ÿ� �����ϸ� ���� ������ ���� �����Ͱ� ��Ŵ, �ӵ� ����
	 * Vector��		Ư�� index�� ���ÿ� �����ϸ� ���������� �ɾ� ������, �ӵ� ����		
	 * 
	*/
	// ������ ��ü�� Generic type���� ����
	Vector<ServerThread> list=new Vector<ServerThread>();
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port) , 10);
		bt_start = new JButton("����");
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
	
	// ���� ����
	public void startServer(){
		try {
			// ��, ������ ������ �ȵ�
			// ����, ������ ��ȭ�� ������ ��ü�� �Ǽ��� �ȵǰ�, ���� ����� �޾Ƶ��̴� ���� ����!!!
			// ��ȭ�� Ŭ���̾�Ʈ�� 1:1 ��Ī�Ǵ� server�� avatar ��ü�� ���� 
			// -> ���� ���������� �����ؾ� �ϱ� ������ avatar�� ������

			port=Integer.parseInt(t_port.getText());
			server=new ServerSocket(port);
			area.append("���� ����\n");
			
			while(true){
				// Ŭ���̾�Ʈ�� ���� ��ٸ�
				socket=server.accept();
				String ip=socket.getInetAddress().getHostAddress();
				area.append(ip+" ����� ����\n");
				
				// �����ڰ� �߰ߵǸ� �����带 �����ڿ� �ϳ��� �Ҵ��Ͽ� ��ȭ ����
				ServerThread st=new ServerThread(socket, this);
				st.start();
				
				// �����ڰ� �߰ߵǸ�, �� �����ڿ� ��ȭ�� ���� �����带 Vector�� ���
				// -> ��Ƽĳ����
				list.add(st);
				area.append("���� �������� ���� "+list.size()+"\n");

			}
			/*
			// ��ȭ�� ������ ���� server�� avatar ����
			// ������ ���� ���� ������ �ϹǷ� avatar ������� �ѱ��
			// ��ȭ�� ���� ��Ʈ�� ����			
			buffr=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			// ������ ��� ����!
			String msg=null;
			
			msg=buffr.readLine();		// Ŭ���̾�Ʈ�� �� �б�
			buffw.write(msg+"\n");	// Ŭ���̾�Ʈ�� �� ����
			buffw.flush();					// ���� ����
			
			area.append(msg+"\n");
			*/
	
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		// ���� ��ư�� ������ �����尡 �޸𸮿� �ö�
		thread=new Thread(this);
		thread.start();
	}
	
	// ���� ������ ������ ���� �޼ҵ�
	public void run() {
		startServer();
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}

}
