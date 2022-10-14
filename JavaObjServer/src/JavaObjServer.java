//JavaObjServer.java ObjectStream 기반 채팅 Server

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import SQL.DAO;

public class JavaObjServer extends JFrame {

	/*
	 Exception in thread "Thread-1" java.lang.ArrayIndexOutOfBoundsException: 1 > 0
	at java.base/java.util.Vector.insertElementAt(Vector.java:589)
	at java.base/java.util.Vector.add(Vector.java:827)
	at JavaObjServer$UserService.ChatRoom(JavaObjServer.java:342)
	at JavaObjServer$UserService.run(JavaObjServer.java:398)
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JTextArea textArea;
	private JTextField txtPortNumber;

	private ServerSocket socket; // 서버소켓
	private Socket client_socket; // accept() 에서 생성된 client 소켓
	private Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터
	private DAO dao;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의

	private String[] ID = new String[100];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjServer frame = new JavaObjServer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JavaObjServer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 338, 440);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 300, 298);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		JLabel lblNewLabel = new JLabel("Port Number");
		lblNewLabel.setBounds(13, 318, 87, 26);
		contentPane.add(lblNewLabel);

		txtPortNumber = new JTextField();
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setText("30000");
		txtPortNumber.setBounds(112, 318, 199, 26);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);

		JButton btnServerStart = new JButton("Server Start");
		btnServerStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
				} catch (NumberFormatException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				AppendText("Chat Server Running..");
				btnServerStart.setText("Chat Server Running..");
				btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
				txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
					AppendText("새로운 참가자 from " + client_socket);
					// User 당 하나씩 Thread 생성
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // 새로운 참가자 배열에 추가
					new_user.start(); // 만든 객체의 스레드 실행
					AppendText("현재 참가자 수 " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("사용자로부터 들어온 메세지 : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("사용자로부터 들어온 object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User 당 생성되는 Thread
	// Read One 에서 대기 -> Write All
	class UserService extends Thread {
		private InputStream is;
		private OutputStream os;
		private DataInputStream dis;
		private DataOutputStream dos;

		private ObjectInputStream ois;
		private ObjectOutputStream oos;

		private Socket client_socket;
		private Vector user_vc;
		public String UserName = "";
		public String id = "";
		public String room_id = "";
		public String UserStatus;

		public UserService(Socket client_socket) {
			// TODO Auto-generated constructor stub
			// 매개변수로 넘어온 자료 저장
			this.client_socket = client_socket;
			this.user_vc = UserVec;
			try {
				oos = new ObjectOutputStream(client_socket.getOutputStream());
				oos.flush();
				ois = new ObjectInputStream(client_socket.getInputStream());
			} catch (Exception e) {
				AppendText("userService error");
			}
		}

		public int setID() {
			for (int i = 0; i < 100; i++) {
				if (ID[i] == null) {
					ID[i] = "";
				}
			}
			for (int i = 0; i < 100; i++) {
				if (ID[i].equals(id))
					return 0;
				if (ID[i].equals("")) {
					ID[i] = id;
					return 1;
				}
			}
			return 1;
		}

		public void Login() {
			AppendText("새로운 참가자 " + UserName + " 입장.");
			// WriteOne("Welcome to Java chat server\n");
			// WriteOne(UserName + "님 환영합니다.\n"); // 연결된 사용자에게 정상접속을 알림
			String msg = "[" + UserName + "]님이 입장 하였습니다.\n";
			WriteOthers(msg, room_id); // 아직 user_vc에 새로 입장한 user는 포함되지 않았다.
		}

		public void Logout() { // 아이디 중복이면 퇴장 문구 없앰
			String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
			UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
			for (int i = 0; i < 100; i++) {
				if (ID[i].equals(id)) {
					ID[i] = null;
					break;
				}
			}
			WriteAll(msg, room_id); // 나를 제외한 다른 User들에게 전송
			this.client_socket = null;
			AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
		}

		// 모든 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteAll(String str, String room_id) {
			String[][] list = dao.getRoomnums(room_id);
			for(int i=0;i<list.length;i++) {
				for (int j = 0; j < user_vc.size(); j++) {
					UserService user = (UserService) user_vc.elementAt(j);
					if (list[i][0].equals(String.valueOf(user.id))) {
						if (user.UserStatus == "O")
							user.WriteOne(str, room_id);
					}
				}
			}
		}

		// 모든 User들에게 Object를 방송. 채팅 message와 image object를 보낼 수 있다
		public void WriteAllObject(ChatMsg obj, String room_id) {
			String[][] list = dao.getRoomnums(room_id);
			for(int i=0;i<list.length;i++) {
				for (int j = 0; j < user_vc.size(); j++) {
					UserService user = (UserService) user_vc.elementAt(j);
					if (list[i][0].equals(String.valueOf(user.id))) {
						if (user.UserStatus == "O")
							user.WriteChatMsg(obj, room_id);
					}
				}
			}
		}

		// 나를 제외한 User들에게 방송. 각각의 UserService Thread의 WriteONe() 을 호출한다.
		public void WriteOthers(String str, String room_id) {
			String[][] list = dao.getRoomnums(room_id);
			for(int i=0;i<list.length;i++) {
				for (int j = 0; j < user_vc.size(); j++) {
					UserService user = (UserService) user_vc.elementAt(j);
					if (list[i][0].equals(String.valueOf(user.id))) {
						if (user != this && user.UserStatus == "O")
							user.WriteOne(str, room_id);
					}
				}
			}
		}

		// Windows 처럼 message 제외한 나머지 부분은 NULL 로 만들기 위한 함수
		public byte[] MakePacket(String msg) {
			byte[] packet = new byte[BUF_LEN];
			byte[] bb = null;
			int i;
			for (i = 0; i < BUF_LEN; i++)
				packet[i] = 0;
			try {
				bb = msg.getBytes("euc-kr");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (i = 0; i < bb.length; i++)
				packet[i] = bb[i];
			return packet;
		}

		// UserService Thread가 담당하는 Client 에게 1:1 전송
		public void WriteOne(String msg, String room_id) {
			ChatMsg obcm = new ChatMsg("SERVER", "200", msg, "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		// 귓속말 전송
		public void WritePrivate(String msg, String room_id) {
			ChatMsg obcm = new ChatMsg("귓속말", "200", msg, "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		public void WriteExit(String room_id) {
			ChatMsg obcm = new ChatMsg("퇴장", "400", "", "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		public void WriteRoom(String room_id) {
			ChatMsg obcm = new ChatMsg("방 입장", "500", "", "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		//
		public void WriteChatMsg(ChatMsg obj, String room_id) {
			try {
				oos.writeObject(obj.code);
				oos.writeObject(obj.UserName);
				oos.writeObject(obj.data);
				if (obj.code.equals("300")) {
					oos.writeObject(obj.imgbytes);
					// oos.writeObject(obj.bimg);
				}
				oos.writeObject(obj.id);
				oos.writeObject(obj.room_id);
			} catch (IOException e) {
				AppendText("oos.writeObject(ob) error");
				try {
					ois.close();
					oos.close();
					client_socket.close();
					client_socket = null;
					ois = null;
					oos = null;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Logout();

			}
		}

		public ChatMsg ReadChatMsg() {
			Object obj = null;
			String msg = null;
			ChatMsg cm = new ChatMsg("", "", "", "", "");
			// Android와 호환성을 위해 각각의 Field를 따로따로 읽는다.
			try {
				obj = ois.readObject();
				cm.code = (String) obj;
				obj = ois.readObject();
				cm.UserName = (String) obj;
				obj = ois.readObject();
				cm.data = (String) obj;
				if (cm.code.equals("300")) {
					obj = ois.readObject();
					cm.imgbytes = (byte[]) obj;
//					obj = ois.readObject();
//					cm.bimg = (BufferedImage) obj;
				}
				obj = ois.readObject();
				cm.id = (String) obj;
				obj = ois.readObject();
				cm.room_id = (String) obj;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				Logout();
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Logout();
				return null;
			}
			return cm;
		}

		public void run() {
			while (true) { // 사용자 접속을 계속해서 받기 위해 while문
				ChatMsg cm = null;
				if (client_socket == null)
					break;
				cm = ReadChatMsg();
				if (cm == null)
					break;
				if (cm.code.length() == 0)
					break;
				AppendObject(cm);
				if (cm.code.matches("100")) {
					UserName = cm.UserName;
					room_id = cm.room_id;
					UserStatus = "O"; // Online 상태
					Login();
				} else if (cm.code.matches("150")) {
					id = cm.id;
					int result = setID();
				} else if (cm.code.matches("200")) {
					String msg = String.format("[%s] %s", cm.UserName, cm.data);
					AppendText(msg); // server 화면에 출력
					String[] args = msg.split(" "); // 단어들을 분리한다.
					if (args.length == 1) { // Enter key 만 들어온 경우 Wakeup 처리만 한다.
						UserStatus = "O";
					} else if (args[1].matches("/exit")) {
						Logout();
						break;
					} else if (args[1].matches("/list")) {
						WriteOne("User list\n", cm.room_id);
						WriteOne("Name\tStatus\n", cm.room_id);
						WriteOne("-----------------------------\n", cm.room_id);
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							WriteOne(user.UserName + "\t" + user.UserStatus + "\n", cm.room_id);
						}
						WriteOne("-----------------------------\n", cm.room_id);
					} else if (args[1].matches("/sleep")) {
						UserStatus = "S";
					} else if (args[1].matches("/wakeup")) {
						UserStatus = "O";
					} else if (args[1].matches("/to")) { // 귓속말
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
								String msg2 = "";
								for (int j = 3; j < args.length; j++) {// 실제 message 부분
									msg2 += args[j];
									if (j < args.length - 1)
										msg2 += " ";
								}
								// /to 빼고.. [귓속말] [user1] Hello user2..
								user.WritePrivate(args[0] + " " + msg2 + "\n", room_id);
								// user.WriteOne("[귓속말] " + args[0] + " " + msg2 + "\n");
								break;
							}
						}
					} else { // 일반 채팅 메시지
						UserStatus = "O";
						// WriteAll(msg + "\n"); // Write All
						WriteAllObject(cm, cm.room_id);
					}
				} else if (cm.code.matches("400")) { // logout message 처리
					Logout();
					break;
				} else if (cm.code.matches("500")) {
					room_id = cm.room_id;
					WriteRoom(room_id);
				} else if (cm.code.matches("300")) {
					WriteAllObject(cm, cm.room_id);
				}
			} // while
		} // run
	}

}
