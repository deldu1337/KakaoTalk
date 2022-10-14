
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.JToggleButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import SQL.DAO;

public class JavaObjClientView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private String ID;
	private String room_ID;
	private DAO dao;
	private JButton btnSend;
	private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private JLabel lblUserName;
	private JLabel lblID;
	// private JTextArea textArea;
	private JTextPane textArea;

	private Frame frame;
	private FileDialog fd;
	private JButton imgBtn;

	private SimpleAttributeSet attribs = new SimpleAttributeSet();
	// StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
	// output.setParagraphAttributes(attribs, true);

	private ImageIcon BottomImg = new ImageIcon("Image/BottomImg.png");
	private ImageIcon OffSend = new ImageIcon("Image/OffSend.png");
	private ImageIcon OnSend = new ImageIcon("Image/OnSend.png");
	private ImageIcon OnSend_Mouse = new ImageIcon("Image/OnSend_Mouse.png");
	private ImageIcon clip = new ImageIcon("Image/clip.png");
	private ImageIcon clip_Mouse = new ImageIcon("Image/clip_Mouse.png");

	private Font font1 = new Font("맑은 고딕", Font.PLAIN, 13);
	private Color background = new Color(178, 199, 217);

	/**
	 * Create the frame.
	 */
	public JavaObjClientView(String username, String ip_addr, String port_no, String id, String room_id) {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 365, 607);
		contentPane = new JPanel(); // {
		// public void paintComponent(Graphics g) {
		// g.drawImage(BottomImg.getImage(), 0, 0, null);
		// setOpaque(false);
		// super.paintComponent(g);
		// }
		// };
		contentPane.setBackground(background);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 60, 325, 400);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(scrollPane);

		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setBackground(background);
		textArea.setFont(font1);
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField() {
			@Override
			public void setBorder(Border border) {

			}
		};
		txtInput.setBounds(15, 467, 245, 30);
		txtInput.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton(OffSend);
		btnSend.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		btnSend.setBorderPainted(false);
		btnSend.setBounds(290, 470, 50, 34);
		contentPane.add(btnSend);

		txtInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtInput.getText().trim().length() != 0) {
					btnSend.setIcon(OnSend);
					btnSend.setRolloverIcon(OnSend_Mouse);
				} else {
					btnSend.setIcon(OffSend);
					btnSend.setRolloverIcon(OffSend);
				}
			}
		});

		lblUserName = new JLabel("Name");
		lblUserName.setBackground(Color.WHITE);
		lblUserName.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblUserName.setBounds(72, 5, 100, 30);
		contentPane.add(lblUserName);
		setVisible(true);

		lblID = new JLabel("ID");
		lblID.setBackground(Color.WHITE);
		lblID.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		lblID.setBounds(72, 20, 100, 30);
		contentPane.add(lblID);
		setVisible(true);

		AppendText("User " + username + " connecting " + ip_addr + " " + port_no, 0);
		textArea.setEditable(false);
		UserName = username;
		ID = id;
		room_ID = room_id;
		lblUserName.setText(username);
		lblID.setText(id);

		imgBtn = new JButton(clip);
		imgBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		imgBtn.setBorderPainted(false);
		imgBtn.setRolloverIcon(clip_Mouse);
		imgBtn.setToolTipText("이미지첨부");
		imgBtn.setBounds(10, 530, 28, 28);
		contentPane.add(imgBtn);

		// JButton btnNewButton = new JButton("종 료");
		// btnNewButton.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		// btnNewButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// ChatMsg msg = new ChatMsg(UserName, "400", "Bye");
		// SendChatMsg(msg);
		// System.exit(0);
		// }
		// });
		// btnNewButton.setBounds(295, 539, 69, 40);
		// contentPane.add(btnNewButton);

		JButton Bottom_button = new JButton(BottomImg);
		Bottom_button.setBounds(0, 461, 350, 110);
		Bottom_button.setDisabledIcon(BottomImg);
		Bottom_button.setEnabled(false);
		contentPane.add(Bottom_button);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			ChatMsg obcm = new ChatMsg(UserName, "100", "Hello", ID, room_ID);
			ChatMsg obcmID = new ChatMsg(UserName, "150", "Hello", ID, "");
			ChatMsg obcmRoom = new ChatMsg(UserName, "500", "Hello", ID, room_ID);
			SendChatMsg(obcm);
			SendChatMsg(obcmID);

			ListenNetwork net = new ListenNetwork();
			net.start();
			TextSendAction action = new TextSendAction();
			btnSend.addActionListener(action);
			txtInput.addActionListener(action);
			txtInput.requestFocus();
			ImageSendAction action2 = new ImageSendAction();
			imgBtn.addActionListener(action2);

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textArea.setEditable(true);
			AppendText("connect error", 0);
			textArea.setEditable(false);
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
			}
			obj = ois.readObject();
			cm.id = (String) obj;
			obj = ois.readObject();
			cm.room_id = (String) obj;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			AppendText("ReadChatMsg Error", 0);
			e.printStackTrace();
			try {
				oos.close();
				socket.close();
				ois.close();
				socket = null;
				return null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				try {
					oos.close();
					socket.close();
					ois.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				socket = null;
				return null;
			}

			// textArea.append("메세지 송신 에러!!\n");
			// System.exit(0);
		}

		return cm;
	}

	// Server Message를 수신해서 화면에 표시
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				ChatMsg cm = ReadChatMsg();
				if (cm == null)
					break;
				if (socket == null)
					break;
				String msg;
				String Mymsg;
				msg = String.format("[%s] %s", cm.UserName, cm.data);
				Mymsg = String.format("%s", cm.data);
				switch (cm.code) {
				case "150":
					break;
				case "200": // chat message
					// AppendText(msg);
					if (room_ID.equals(cm.room_id)) {
						if (lblID.getText().equals(cm.id)) { // 본인 message 우측 정렬
							textArea.setEditable(true);
							AppendText(Mymsg, 1);
							StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
							textArea.setParagraphAttributes(attribs, true);
							textArea.setEditable(false);
						} else { // 상대방 message 좌측 정렬
							textArea.setEditable(true);
							AppendText(msg, 0);
							StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_LEFT);
							textArea.setParagraphAttributes(attribs, true);
							textArea.setEditable(false);
						}
					}
					break;
				case "300": // Image 첨부
					// AppendText("[" + cm.UserName + "]" + " " + cm.data);
					// AppendImage(cm.img);
					// AppendImageBytes(cm.imgbytes);
					if (room_ID.equals(cm.room_id)) {
						if (lblID.getText().equals(cm.id)) { // 본인 message 우측 정렬
							textArea.setEditable(true);
							//AppendText("[" + cm.UserName + "]", 1);
							StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
							textArea.setParagraphAttributes(attribs, true);
							AppendImageBytes(cm.imgbytes);
							StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_RIGHT);
							textArea.setParagraphAttributes(attribs, true);
							textArea.setEditable(false);
						} else { // 상대방 message 좌측 정렬
							textArea.setEditable(true);
							AppendText("[" + cm.UserName + "]", 0);
							AppendImageBytes(cm.imgbytes);
							StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_LEFT);
							textArea.setParagraphAttributes(attribs, true);
							textArea.setEditable(false);
						}
					}
					break;
				case "400":
					ChatMsg msg1 = new ChatMsg(UserName, "400", "Bye", "", "");
					JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.", "아이디 중복", JOptionPane.WARNING_MESSAGE);
					System.exit(0);
					break;
				case "500":

					break;
				}

			}
		}
	}

	// keyboard enter key 치면 서버로 전송
	class TextSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				// msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				msg = txtInput.getText();
				SendMessage(msg);
				txtInput.setText(""); // 메세지를 보내고 나면 메세지 쓰는창을 비운다.
				txtInput.requestFocus(); // 메세지를 보내고 커서를 다시 텍스트 필드로 위치시킨다
				btnSend.setIcon(OffSend);
				btnSend.setRolloverIcon(OffSend);
				if (msg.contains("/exit")) // 종료 처리
					System.exit(0);
			}
		}
	}

	class ImageSendAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// 액션 이벤트가 sendBtn일때 또는 textField 에세 Enter key 치면
			if (e.getSource() == imgBtn) {
				frame = new Frame("이미지첨부");
				fd = new FileDialog(frame, "이미지 선택", FileDialog.LOAD);
				fd.setVisible(true);
				// System.out.println(fd.getDirectory() + fd.getFile());
				if (fd.getDirectory().length() > 0 && fd.getFile().length() > 0) {
					ChatMsg obcm = new ChatMsg(UserName, "300", fd.getFile(), ID, room_ID);
//					ImageIcon img = new ImageIcon(fd.getDirectory() + fd.getFile());
//					obcm.img = img;
//					SendChatMsg(obcm);

					BufferedImage bImage = null;
					String filename = fd.getDirectory() + fd.getFile();
					try {
						bImage = ImageIO.read(new File(filename));
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					try {
						textArea.setEditable(true);
						ImageIO.write(bImage, "jpg", bos);
						byte[] data = bos.toByteArray();
						bos.close();
						obcm.imgbytes = data;
						// AppendImageBytes(data);
						textArea.setEditable(false);

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					SendChatMsg(obcm);
				}
			}
		}
	}

	ImageIcon icon1 = new ImageIcon("original.png");

	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		// 끝으로 이동
		textArea.setCaretPosition(len);
		textArea.insertIcon(icon);
	}

	// 화면에 출력
	public void AppendText(String msg, int num) {
		// textArea.append(msg + "\n");
		if (num == 1) {
			msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
			int len = textArea.getDocument().getLength();
			// 끝으로 이동
			textArea.setCaretPosition(len);
			textArea.replaceSelection(msg + "\n");
		} else {
			AppendIcon(icon1);
			msg = msg.trim(); // 앞뒤 blank와 \n을 제거한다.
			int len = textArea.getDocument().getLength();
			// 끝으로 이동
			textArea.setCaretPosition(len);
			textArea.replaceSelection(msg + "\n");
		}
	}

	public void AppendImage(ImageIcon ori_icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image가 너무 크면 최대 가로 또는 세로 200 기준으로 축소시킨다.
		if (width > 200 || height > 200) {
			if (width > height) { // 가로 사진
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // 세로 사진
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
			textArea.insertIcon(new_icon);

		} else
			textArea.insertIcon(ori_icon);
		len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len);
		textArea.replaceSelection("\n");
	}

	public void AppendImageBytes(byte[] imgbytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(imgbytes);
		BufferedImage ori_img = null;
		try {
			ori_img = ImageIO.read(bis);
			bis.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ImageIcon new_icon = new ImageIcon(ori_img);
		AppendImage(new_icon);
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
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server에게 network으로 전송
	public void SendMessage(String msg) {
		ChatMsg obcm = new ChatMsg(UserName, "200", msg, ID, room_ID);
		SendChatMsg(obcm);
	}

	// 하나의 Message 보내는 함수
	// Android와 호환성을 위해 code, UserName, data 모드 각각 전송한다.
	public void SendChatMsg(ChatMsg obj) {
		try {
			oos.writeObject(obj.code);
			oos.writeObject(obj.UserName);
			oos.writeObject(obj.data);
			if (obj.code.equals("300")) { // 이미지 첨부 있는 경우
				oos.writeObject(obj.imgbytes);
			}
			oos.writeObject(obj.id);
			oos.writeObject(obj.room_id);
			oos.flush();
		} catch (IOException e) {
			AppendText("SendChatMsg Error", 0);
			e.printStackTrace();
			try {
				oos.close();
				socket.close();
				ois.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// textArea.append("메세지 송신 에러!!\n");
			// System.exit(0);
		}
	}
}
