//JavaObjServer.java ObjectStream ��� ä�� Server

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

	private ServerSocket socket; // ��������
	private Socket client_socket; // accept() ���� ������ client ����
	private Vector UserVec = new Vector(); // ����� ����ڸ� ������ ����
	private DAO dao;
	private static final int BUF_LEN = 128; // Windows ó�� BUF_LEN �� ����

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
				btnServerStart.setEnabled(false); // ������ ���̻� �����Ű�� �� �ϰ� ���´�
				txtPortNumber.setEnabled(false); // ���̻� ��Ʈ��ȣ ������ �ϰ� ���´�
				AcceptServer accept_server = new AcceptServer();
				accept_server.start();
			}
		});
		btnServerStart.setBounds(12, 356, 300, 35);
		contentPane.add(btnServerStart);
	}

	// ���ο� ������ accept() �ϰ� user thread�� ���� �����Ѵ�.
	class AcceptServer extends Thread {
		@SuppressWarnings("unchecked")
		public void run() {
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
				try {
					AppendText("Waiting new clients ...");
					client_socket = socket.accept(); // accept�� �Ͼ�� �������� ���� �����
					AppendText("���ο� ������ from " + client_socket);
					// User �� �ϳ��� Thread ����
					UserService new_user = new UserService(client_socket);
					UserVec.add(new_user); // ���ο� ������ �迭�� �߰�
					new_user.start(); // ���� ��ü�� ������ ����
					AppendText("���� ������ �� " + UserVec.size());
				} catch (IOException e) {
					AppendText("accept() error");
					// System.exit(0);
				}
			}
		}
	}

	public void AppendText(String str) {
		// textArea.append("����ڷκ��� ���� �޼��� : " + str+"\n");
		textArea.append(str + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	public void AppendObject(ChatMsg msg) {
		// textArea.append("����ڷκ��� ���� object : " + str+"\n");
		textArea.append("code = " + msg.code + "\n");
		textArea.append("id = " + msg.UserName + "\n");
		textArea.append("data = " + msg.data + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	// User �� �����Ǵ� Thread
	// Read One ���� ��� -> Write All
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
			// �Ű������� �Ѿ�� �ڷ� ����
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
			AppendText("���ο� ������ " + UserName + " ����.");
			// WriteOne("Welcome to Java chat server\n");
			// WriteOne(UserName + "�� ȯ���մϴ�.\n"); // ����� ����ڿ��� ���������� �˸�
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			WriteOthers(msg, room_id); // ���� user_vc�� ���� ������ user�� ���Ե��� �ʾҴ�.
		}

		public void Logout() { // ���̵� �ߺ��̸� ���� ���� ����
			String msg = "[" + UserName + "]���� ���� �Ͽ����ϴ�.\n";
			UserVec.removeElement(this); // Logout�� ���� ��ü�� ���Ϳ��� �����
			for (int i = 0; i < 100; i++) {
				if (ID[i].equals(id)) {
					ID[i] = null;
					break;
				}
			}
			WriteAll(msg, room_id); // ���� ������ �ٸ� User�鿡�� ����
			this.client_socket = null;
			AppendText("����� " + "[" + UserName + "] ����. ���� ������ �� " + UserVec.size());
		}

		// ��� User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
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

		// ��� User�鿡�� Object�� ���. ä�� message�� image object�� ���� �� �ִ�
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

		// ���� ������ User�鿡�� ���. ������ UserService Thread�� WriteONe() �� ȣ���Ѵ�.
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

		// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
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

		// UserService Thread�� ����ϴ� Client ���� 1:1 ����
		public void WriteOne(String msg, String room_id) {
			ChatMsg obcm = new ChatMsg("SERVER", "200", msg, "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		// �ӼӸ� ����
		public void WritePrivate(String msg, String room_id) {
			ChatMsg obcm = new ChatMsg("�ӼӸ�", "200", msg, "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		public void WriteExit(String room_id) {
			ChatMsg obcm = new ChatMsg("����", "400", "", "", room_id);
			WriteChatMsg(obcm, room_id);
		}

		public void WriteRoom(String room_id) {
			ChatMsg obcm = new ChatMsg("�� ����", "500", "", "", room_id);
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
			// Android�� ȣȯ���� ���� ������ Field�� ���ε��� �д´�.
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
			while (true) { // ����� ������ ����ؼ� �ޱ� ���� while��
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
					UserStatus = "O"; // Online ����
					Login();
				} else if (cm.code.matches("150")) {
					id = cm.id;
					int result = setID();
				} else if (cm.code.matches("200")) {
					String msg = String.format("[%s] %s", cm.UserName, cm.data);
					AppendText(msg); // server ȭ�鿡 ���
					String[] args = msg.split(" "); // �ܾ���� �и��Ѵ�.
					if (args.length == 1) { // Enter key �� ���� ��� Wakeup ó���� �Ѵ�.
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
					} else if (args[1].matches("/to")) { // �ӼӸ�
						for (int i = 0; i < user_vc.size(); i++) {
							UserService user = (UserService) user_vc.elementAt(i);
							if (user.UserName.matches(args[2]) && user.UserStatus.matches("O")) {
								String msg2 = "";
								for (int j = 3; j < args.length; j++) {// ���� message �κ�
									msg2 += args[j];
									if (j < args.length - 1)
										msg2 += " ";
								}
								// /to ����.. [�ӼӸ�] [user1] Hello user2..
								user.WritePrivate(args[0] + " " + msg2 + "\n", room_id);
								// user.WriteOne("[�ӼӸ�] " + args[0] + " " + msg2 + "\n");
								break;
							}
						}
					} else { // �Ϲ� ä�� �޽���
						UserStatus = "O";
						// WriteAll(msg + "\n"); // Write All
						WriteAllObject(cm, cm.room_id);
					}
				} else if (cm.code.matches("400")) { // logout message ó��
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
