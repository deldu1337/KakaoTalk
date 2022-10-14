import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import SQL.DAO;
import SQL.DTO;

public class AddFriendFrame extends JFrame{
	ImageIcon Frame = new ImageIcon("Image/AddFriendFrame.png");
	ImageIcon AddFriend_Original = new ImageIcon("Image/AddFriend_Original.png");
	ImageIcon AddFriend = new ImageIcon("Image/AddFriend.png");
	ImageIcon AddFriend_Mouse = new ImageIcon("Image/AddFriend_Mouse.png");
	Font font2 = new Font("���� ���", Font.PLAIN, 13);
	private DAO dao;
	
	public AddFriendFrame(String username, String ip_addr, String port_no, String id) {
		setTitle("KakaoTalk"); // Ÿ��Ʋ
		setSize(315, 467); // â ������
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ������ X�� ������ JVM �� �޸𸮷� ����
		setLocationRelativeTo(null); // â�� ȭ�� ���� ����� ����. null(�⺻��)�� ���
		
		JPanel panel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(Frame.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		panel.setBounds(0, 0, 315, 467);
		panel.setLayout(null);
		add(panel);
		
		JTextField id_field = new JTextField() {
			@Override
			public void setBorder(Border border) {

			}
		};
		id_field.setBounds(18, 117, 220, 30);
		id_field.setFont(font2);
		panel.add(id_field);
		
		JButton addFriends_button = new JButton(AddFriend_Original);
		addFriends_button.setBounds(195, 372, 85, 39);
		addFriends_button.setBorderPainted(false);
		addFriends_button.setEnabled(false);
		addFriends_button.setDisabledIcon(AddFriend_Original);
		panel.add(addFriends_button);
		
		id_field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (id_field.getText().trim().length() != 0) {
					addFriends_button.setEnabled(true);
					addFriends_button.setIcon(AddFriend);
					addFriends_button.setRolloverIcon(AddFriend_Mouse);
				} else {
					addFriends_button.setEnabled(false);
					addFriends_button.setDisabledIcon(AddFriend_Original);
					addFriends_button.setRolloverIcon(AddFriend_Original);
				}
			}
		});
		
		setVisible(true);
		
		addFriends_button.addActionListener(new ActionListener() { // ��ư Ŭ���� ���� �޼ҵ�
			@Override // ActionListener�޼ҵ� ����� ���� �������̵�
			public void actionPerformed(ActionEvent e) {
				dao.addFriend(id, id_field.getText()); // ������ ���� ģ�� �߰�
				dao.createUserTable(id_field.getText()); // ���� ���̺� ����
				dao.addFriend(id_field.getText(), id); // ���� ģ�� ��Ͽ� ���� �߰�
				SecondFrame sf = new SecondFrame(username, ip_addr, port_no, id);
				setVisible(false);
			}
		});
		
		this.addWindowListener(new WindowAdapter() { // ��ư Ŭ���� ���� �޼ҵ�
			@Override // ActionListener�޼ҵ� ����� ���� �������̵�
			public void windowClosing(WindowEvent e) {
				SecondFrame sf = new SecondFrame(username, ip_addr, port_no, id);
				setVisible(false);
			}
		});
	}
	
	public static void main(String[] args) {
	}

}
