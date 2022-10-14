// JavaObjClient.java
// ObjecStream 사용하는 채팅 Client

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

import SQL.DAO;
import SQL.DTO;

public class JavaObjClientMain extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DAO dao;
	private JPanel contentPane;
	private JTextField txtID;
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;
	
	private ImageIcon logo1 = new ImageIcon("Image/logo1.png"); // 버튼 기본 이미지
	private ImageIcon Offlogin = new ImageIcon("Image/Offlogin.png");
	private ImageIcon Onlogin = new ImageIcon("Image/Onlogin.png");
	private ImageIcon Onlogin_Mouse = new ImageIcon("Image/Onlogin_Mouse.png");
	private ImageIcon TextFrame = new ImageIcon("Image/TextFrame.png");
	
	private Font font1 = new Font("맑은 고딕", Font.BOLD, 13);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JavaObjClientMain frame = new JavaObjClientMain();
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
	public JavaObjClientMain() {
		dao.createRoomTable();
		Color background = new Color(245,225,4);
		Color defaulttext = new Color(191,191,191);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 375, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(background);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUserName = new JTextField("이름") {
            @Override
            public void setBorder(Border border) {  
            }
        };
		txtUserName.setBounds(70, 206, 225, 33);
		txtUserName.setForeground(defaulttext);
		txtUserName.setEditable(false);
		txtUserName.setBackground(Color.WHITE);
		contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		txtID = new JTextField("아이디") {
            @Override
            public void setBorder(Border border) {  
            }
        };
		txtID.setBounds(70, 242, 225, 33);
		txtID.setForeground(defaulttext);
		txtID.setEditable(false);
		txtID.setBackground(Color.WHITE);
		contentPane.add(txtID);
		txtID.setColumns(10);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setBounds(20, 476, 82, 35);
		contentPane.add(lblIpAddress);
		
		txtIpAddress = new JTextField() {
			@Override
            public void setBorder(Border border) {  
            }
        };
        txtIpAddress.setBounds(105, 476, 225, 33);
        txtIpAddress.setText("127.0.0.1");
		txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(txtIpAddress);
		txtIpAddress.setColumns(10);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setBounds(20, 512, 82, 35);
		contentPane.add(lblPortNumber);
		
		txtPortNumber = new JTextField() {
			@Override
            public void setBorder(Border border) {  
            }
        };
        txtPortNumber.setBounds(105, 512, 225, 33);
        txtPortNumber.setText("30000");
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(txtPortNumber);
		txtPortNumber.setColumns(10);
		
		JButton btnLogo = new JButton(logo1); // 버튼 변수 선언 및 생성
		btnLogo.setBounds(125, 65, 110, 110); // 버튼 시작점 위치(550,400)와 크기(100,50)
		btnLogo.setBorderPainted(false); // 버튼 테두리 안보이게 하기
		btnLogo.setDisabledIcon(logo1);
		btnLogo.setEnabled(false);
		btnLogo.setHorizontalTextPosition(JButton.CENTER); // 버튼을 수평 기준 가운데 정렬
		contentPane.add(btnLogo); // 패널에 버튼 적용
		
		JButton btnConnect = new JButton(Offlogin);
		btnConnect.setBounds(65, 285, 235, 40);
		btnConnect.setBorderPainted(false);
		btnConnect.setDisabledIcon(Offlogin);
		btnConnect.setEnabled(false);
		contentPane.add(btnConnect);
		
		Myaction action = new Myaction();
		btnConnect.addActionListener(action);
		txtUserName.addActionListener(action);
		txtIpAddress.addActionListener(action);
		txtPortNumber.addActionListener(action);
		
		txtUserName.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(txtUserName.getText().equals("이름")) {
					txtUserName.setText("");
					txtUserName.setEditable(true);
					txtUserName.setForeground(Color.BLACK);
					btnConnect.setIcon(Offlogin);
					btnConnect.setRolloverIcon(Offlogin);
					btnConnect.setEnabled(false);
					if(txtID.getText().trim().length()==0) {
						txtID.setText("아이디");
						txtID.setForeground(defaulttext);
						txtID.setEditable(false);
					}
				}
				if(txtID.getText().trim().length()==0) {
					txtID.setText("아이디");
					txtID.setForeground(defaulttext);
					txtID.setEditable(false);
				}
			}
		});
		
		txtID.addMouseListener(new MouseAdapter() { 
			@Override
			public void mousePressed(MouseEvent e) {
				if(txtID.getText().equals("아이디")) {
					txtID.setText("");
					txtID.setEditable(true);
					txtID.setForeground(Color.BLACK);
					btnConnect.setIcon(Offlogin);
					btnConnect.setRolloverIcon(Offlogin);
					btnConnect.setEnabled(false);
					if(txtUserName.getText().trim().length()==0) {
						txtUserName.setText("이름");
						txtUserName.setForeground(defaulttext);
						txtUserName.setEditable(false);
					}
				}
				if(txtUserName.getText().trim().length()==0) {
					txtUserName.setText("이름");
					txtUserName.setForeground(defaulttext);
					txtUserName.setEditable(false);
				}
			}
		});
		
		txtUserName.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				if(txtUserName.getText().trim().length()!=0 && txtID.getText().trim().length()!=0) {
					if(!txtID.getText().equals("아이디")) {
						btnConnect.setIcon(Onlogin);
						btnConnect.setRolloverIcon(Onlogin_Mouse);
						btnConnect.setEnabled(true);
					}
				}
				else {
					btnConnect.setIcon(Offlogin);
					btnConnect.setRolloverIcon(Offlogin);
					btnConnect.setEnabled(false);
				}
			}
		});
		
		txtID.addKeyListener(new KeyAdapter() { 
			@Override
			public void keyReleased(KeyEvent e) {
				if(txtUserName.getText().trim().length()!=0 && txtID.getText().trim().length()!=0) {
					if(!txtUserName.getText().equals("이름")) {
						btnConnect.setIcon(Onlogin);
						btnConnect.setRolloverIcon(Onlogin_Mouse);
						btnConnect.setEnabled(true);
					}
				}
				else {
					btnConnect.setIcon(Offlogin);
					btnConnect.setRolloverIcon(Offlogin);
					btnConnect.setEnabled(false);
				}
			}
		});
		
		JButton btnText = new JButton(TextFrame);
		btnText.setBounds(65, 200, 233, 80);
		btnText.setBorderPainted(false);
		btnText.setDisabledIcon(TextFrame);
		btnText.setEnabled(false);
		contentPane.add(btnText);

		JButton btnText2 = new JButton(TextFrame);
		btnText2.setBounds(100, 470, 233, 80);
		btnText2.setBorderPainted(false);
		btnText2.setDisabledIcon(TextFrame);
		btnText2.setEnabled(false);
		contentPane.add(btnText2);
	}
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = txtUserName.getText().trim();
			String ip_addr = txtIpAddress.getText().trim();
			String port_no = txtPortNumber.getText().trim();
			String id = txtID.getText().trim();
			SecondFrame second = new SecondFrame(username, ip_addr, port_no, id);
			setVisible(false);
		}
	}
}


