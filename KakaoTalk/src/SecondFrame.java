import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import SQL.DAO;
import SQL.DTO;

public class SecondFrame extends JFrame implements ActionListener, MouseListener { // JFrame을 상속받은 FirstFrame 클래스
	JScrollPane scrollpane1, scrollpane2, scrollpane3, scrollpane4; // 이미지 적용을 위한 틀
	ImageIcon menubar = new ImageIcon("Image/menubar.png"); // 버튼 기본 이미지
	ImageIcon OnMenu1 = new ImageIcon("Image/OnMenu1.png");
	ImageIcon OffMenu1 = new ImageIcon("Image/OffMenu1.png");
	ImageIcon OffMenu1_Mouse = new ImageIcon("Image/OffMenu1_Mouse.png");
	ImageIcon OnMenu2 = new ImageIcon("Image/OnMenu2.png");
	ImageIcon OffMenu2 = new ImageIcon("Image/OffMenu2.png");
	ImageIcon OffMenu2_Mouse = new ImageIcon("Image/OffMenu2_Mouse.png");
	ImageIcon setting = new ImageIcon("Image/setting.png");
	ImageIcon setting_Mouse = new ImageIcon("Image/setting_Mouse.png");
	ImageIcon OffMe = new ImageIcon("Image/OffMe.png");
	ImageIcon OffMe_Mouse = new ImageIcon("Image/OffMe_Mouse.png");
	ImageIcon OnMe = new ImageIcon("Image/OnMe.png");
	ImageIcon addFriends = new ImageIcon("Image/addFriends.png");
	ImageIcon addFriends_Mouse = new ImageIcon("Image/addFriends_Mouse.png");
	ImageIcon addRooms = new ImageIcon("Image/addRooms.png");
	ImageIcon addRooms_Mouse = new ImageIcon("Image/addRooms_Mouse.png");
	ImageIcon profile = new ImageIcon("Image/original.png");
	Font font1 = new Font("맑은 고딕", Font.BOLD, 11);
	Font font2 = new Font("맑은 고딕", Font.BOLD, 13);
	Font font3 = new Font("맑은 고딕", Font.BOLD, 19);
	Font font4 = new Font("맑은 고딕", Font.BOLD, 50);
	private Color Text_Mouse = new Color(248, 248, 248);
	private Color Text_Clicked = new Color(242, 242, 242);
	private String Username, IP_addr, Port_no, ID;
	private DAO dao;
	private String[][] arr;
	private String[][] Room_arr;
	private String[][] Roomnum_arr;
	private JTextField name;
	private JButton OffMe_button;
	private JButton profile_button;
	private JButton[] button;
	private int Cnt[];
	
	private String[][] data;
	private Object[][] obj;
	private ImageIcon [] Icon;
	private String[] headers;
	private String[] cell;
	private JTable table;
	
	private String[][] data2;
	private String[] headers2;
	private String[] cell2;
	private JTable table2;

	private int cnt = 0;

	SecondFrame(String username, String ip_addr, String port_no, String id) {
		Username = username;
		IP_addr = ip_addr;
		Port_no = port_no;
		ID = id;
		dao.createUserTable(id);
		arr = dao.getFriends(id);
		dao.createUserRoomTable(id);
		Room_arr = dao.getUserRooms(id);
		setTitle("KakaoTalk"); // 타이틀
		setSize(405, 650); // 창 사이즈
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 없으면 X를 눌러도 JVM 상에 메모리로 상주
		setLocationRelativeTo(null); // 창을 화면 상의 가운데에 정렬. null(기본값)은 가운데

		JPanel panel1 = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(menubar.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		panel1.setBounds(0, 0, 405, 650);
		panel1.setLayout(null);
		add(panel1);

		JButton OnMenu1_button = new JButton(OnMenu1);
		OnMenu1_button.setBounds(0, 22, 62, 59);
		OnMenu1_button.setBorderPainted(false);
		OnMenu1_button.setToolTipText("친구");
		OnMenu1_button.setHorizontalTextPosition(JButton.CENTER);
		panel1.add(OnMenu1_button);

		JButton OffMenu2_button = new JButton(OffMenu2);
		OffMenu2_button.setBounds(0, 80, 62, 59);
		OffMenu2_button.setBorderPainted(false);
		OffMenu2_button.setToolTipText("채팅");
		OffMenu2_button.setRolloverIcon(OffMenu2_Mouse);
		OffMenu2_button.setHorizontalTextPosition(JButton.CENTER);
		panel1.add(OffMenu2_button);
		
		JButton setting_button = new JButton(setting);
		setting_button.setBounds(10, 550, 50, 44);
		setting_button.setBorderPainted(false);
		setting_button.setToolTipText("설정");
		setting_button.setRolloverIcon(setting_Mouse);
		setting_button.setHorizontalTextPosition(JButton.CENTER);
		panel1.add(setting_button);
		
		PopupMenu popup = new PopupMenu();
		MenuItem logout_popup = new MenuItem("로그아웃");
		MenuItem drop_popup = new MenuItem("회원초기화");
		popup.add(logout_popup);
		popup.add(drop_popup);
		panel1.add(popup);
		
		setting_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseClicked(MouseEvent e) {
				popup.show(panel1, 60, 530);
				setting_button.setRolloverIcon(setting_Mouse);
				logout_popup.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
					@Override // ActionListener메소드 사용을 위한 오버라이딩
					public void actionPerformed(ActionEvent e) {
						JavaObjClientMain frame = new JavaObjClientMain();
						frame.setVisible(true);
						setVisible(false);
					}
				});
				drop_popup.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
					@Override // ActionListener메소드 사용을 위한 오버라이딩
					public void actionPerformed(ActionEvent e) {
						String [][] arr1 = dao.getFriends(id);
						String [][] arr2, arr3;
						for(int i=0;i<arr1.length;i++) {
							dao.deleteFriend(arr1[i][0], id);
						}
						dao.dropTable(id); // 친구 목록 삭제
						
						arr2 = dao.getUserRooms(id);
						for(int i=0;i<arr2.length;i++) {
							dao.deleteRoomnum(id,arr2[i][0]);
							if(dao.checkRoomnum(arr2[i][0])==false) {
								arr3 = dao.getRoomnums(arr2[i][0]);
								dao.deleteUserRoom(arr3[0][0], arr2[i][0]);
							}
						} 
						dao.dropTable(id+"_Room"); // 방 목록 삭제
						
						arr1 = dao.getRooms();
						for(int i=0;i<arr1.length;i++) {
							if(dao.checkRoomnum(arr1[i][0])==false) {
								dao.dropTable("Room"+arr1[i][0]);
								dao.deleteRoom(arr1[i][0]);
							}
						}
						JavaObjClientMain frame = new JavaObjClientMain();
						frame.setVisible(true);
						setVisible(false);
					}
				});
			}
		});

		profile_button = new JButton(profile);
		profile_button.setBounds(85, 75, 60, 60);
		profile_button.setBorderPainted(false);
		// profile_button.setRolloverIcon(profile);
		panel1.add(profile_button);

		name = new JTextField(username) {
			@Override
			public void setBorder(Border border) {

			}
		};
		name.setBounds(157, 90, 100, 30);
		name.setEnabled(false);
		name.setFont(font2);
		name.setDisabledTextColor(Color.BLACK);
		panel1.add(name);

		JButton addFriends_button = new JButton(addFriends);
		addFriends_button.setBounds(330, 24, 42, 40);
		addFriends_button.setBorderPainted(false);
		addFriends_button.setToolTipText("친구 추가");
		addFriends_button.setRolloverIcon(addFriends_Mouse);
		panel1.add(addFriends_button);

		addFriends_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseClicked(MouseEvent e) {
				AddFriendFrame aff = new AddFriendFrame(username, ip_addr, port_no, id);
				setVisible(false);
			}
		});

		JLabel label1 = new JLabel("친구");
		label1.setBounds(84, 24, 50, 50);
		label1.setFont(font3);
		panel1.add(label1);

		OffMe_button = new JButton(OffMe);
		OffMe_button.setBounds(63, 70, 310, 70);
		OffMe_button.setBorderPainted(false);
		panel1.add(OffMe_button);
		
		data = dao.getFriends(id);
		headers = new String[] { "친구 " + data.length };
		cell = new String[data.length];
		table = new JTable(data, headers);
		table.setModel(new DefaultTableModel(data, headers) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}); // 셀 값 수정 불가

		DefaultTableCellRenderer left = new DefaultTableCellRenderer();
		left.setHorizontalAlignment(SwingConstants.LEFT);

		TableColumnModel columnModels = table.getColumnModel();
		table.getTableHeader().setReorderingAllowed(false); // 테이블 컬럼 이동 방지
		columnModels.getColumn(0).setResizable(false); // 테이블 컬럼 크기 변경 불가
		// columnModels.getColumn(0).setPreferredWidth(120); // 테이블 컬럼 크기

		for (int i = 0; i < columnModels.getColumnCount(); i++) {
			columnModels.getColumn(i).setCellRenderer(left);
		} // 셀 값을 가운데 정렬 하기 위한 반복문

		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 셀 다중 선택 불가
		table.setRowHeight(70);
		table.setFont(font2);
		table.setAlignmentX(0);
		table.setSize(327, 585);
		table.setPreferredScrollableViewportSize(new Dimension(327, 585));
		//table.addMouseListener(this);
		// table.addActionListener(this);
		
		scrollpane1 = new JScrollPane(table);
		scrollpane1.setBounds(63, 150, 327, 585);
		panel1.add(scrollpane1);
		scrollpane2 = new JScrollPane(panel1);
		setContentPane(scrollpane2);

		JPanel panel2 = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(menubar.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		panel2.setBounds(0, 0, 405, 650);
		//panel2.setPreferredSize(new Dimension(405, 650));
		panel2.setLayout(null);
		add(panel2);

		data2 = dao.getUserRooms(id);
		headers2 = new String[] { "" };
		cell2 = new String[data.length];
		table2 = new JTable(data2, headers2);
		table2.setModel(new DefaultTableModel(data2, headers2) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}); // 셀 값 수정 불가
		DefaultTableCellRenderer left2 = new DefaultTableCellRenderer();
		left2.setHorizontalAlignment(SwingConstants.LEFT);

		TableColumnModel columnModels2 = table2.getColumnModel();
		table2.getTableHeader().setReorderingAllowed(false); // 테이블 컬럼 이동 방지
		columnModels2.getColumn(0).setResizable(false); // 테이블 컬럼 크기 변경 불가
		// columnModels.getColumn(0).setPreferredWidth(120); // 테이블 컬럼 크기

		for (int i = 0; i < columnModels2.getColumnCount(); i++) {
			columnModels2.getColumn(i).setCellRenderer(left2);
		} // 셀 값을 가운데 정렬 하기 위한 반복문

		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 셀 다중 선택 불가
		table2.setRowHeight(70);
		table2.setFont(font2);
		table2.setAlignmentX(0);
		table2.setSize(327, 585);
		table2.setPreferredScrollableViewportSize(new Dimension(327, 585));
		//table2.addMouseListener(this);
		// table.addActionListener(this);
		scrollpane3 = new JScrollPane(table2);
		scrollpane3.setBounds(63, 75, 327, 585);
		panel2.add(scrollpane3);
		scrollpane4 = new JScrollPane(panel2);
		
		JButton OffMenu1_button = new JButton(OffMenu1);
		OffMenu1_button.setBounds(0, 24, 62, 59);
		OffMenu1_button.setBorderPainted(false);
		OffMenu1_button.setToolTipText("친구");
		OffMenu1_button.setRolloverIcon(OffMenu1_Mouse);
		OffMenu1_button.setHorizontalTextPosition(JButton.CENTER);
		panel2.add(OffMenu1_button);

		JButton OnMenu2_button = new JButton(OnMenu2);
		OnMenu2_button.setBounds(0, 79, 62, 59);
		OnMenu2_button.setBorderPainted(false);
		OnMenu2_button.setToolTipText("채팅");
		OnMenu2_button.setHorizontalTextPosition(JButton.CENTER);
		panel2.add(OnMenu2_button);

		JLabel label2 = new JLabel("채팅");
		label2.setBounds(84, 24, 50, 50);
		label2.setFont(font3);
		panel2.add(label2);
		
		JButton setting_button2 = new JButton(setting);
		setting_button2.setBounds(10, 550, 50, 44);
		setting_button2.setBorderPainted(false);
		setting_button2.setToolTipText("설정");
		setting_button2.setRolloverIcon(setting_Mouse);
		setting_button2.setHorizontalTextPosition(JButton.CENTER);
		panel2.add(setting_button2);
		
		PopupMenu popup2 = new PopupMenu();
		MenuItem logout_popup2 = new MenuItem("로그아웃");
		MenuItem drop_popup2 = new MenuItem("회원초기화");
		popup2.add(logout_popup2);
		popup2.add(drop_popup2);
		panel2.add(popup2);
		
		setting_button2.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseClicked(MouseEvent e) {
				popup2.show(panel2, 60, 530);
				setting_button2.setRolloverIcon(setting_Mouse);
				logout_popup2.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
					@Override // ActionListener메소드 사용을 위한 오버라이딩
					public void actionPerformed(ActionEvent e) {
						JavaObjClientMain frame = new JavaObjClientMain();
						frame.setVisible(true);
						setVisible(false);
					}
				});
				drop_popup2.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
					@Override // ActionListener메소드 사용을 위한 오버라이딩
					public void actionPerformed(ActionEvent e) {
						String [][] arr1 = dao.getFriends(id);
						String [][] arr2, arr3;
						for(int i=0;i<arr1.length;i++) {
							dao.deleteFriend(arr1[i][0], id);
						}
						dao.dropTable(id); // 친구 목록 삭제
						
						arr2 = dao.getUserRooms(id);
						for(int i=0;i<arr2.length;i++) {
							dao.deleteRoomnum(id,arr2[i][0]);
							if(dao.checkRoomnum(arr2[i][0])==false) {
								arr3 = dao.getRoomnums(arr2[i][0]);
								dao.deleteUserRoom(arr3[0][0], arr2[i][0]);
							}
						} 
						dao.dropTable(id+"_Room"); // 방 목록 삭제
						
						arr1 = dao.getRooms();
						for(int i=0;i<arr1.length;i++) {
							if(dao.checkRoomnum(arr1[i][0])==false) {
								dao.dropTable("Room"+arr1[i][0]);
								dao.deleteRoom(arr1[i][0]);
							}
						}
						JavaObjClientMain frame = new JavaObjClientMain();
						frame.setVisible(true);
						setVisible(false);
					}
				});
			}
		});
		
		JButton addRooms_button = new JButton(addRooms);
		addRooms_button.setBounds(333, 24, 42, 40);
		addRooms_button.setBorderPainted(false);
		addRooms_button.setToolTipText("새로운 채팅");
		addRooms_button.setRolloverIcon(addRooms_Mouse);
		panel2.add(addRooms_button);

		addRooms_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseClicked(MouseEvent e) {
				AddRoomFrame arf = new AddRoomFrame(username, ip_addr, port_no, id);
				setVisible(false);
			}
		});

		panel1.setVisible(true); // 화면에 프레임 출력
		panel2.setVisible(false);

		setVisible(true);

		OffMenu2_button.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void actionPerformed(ActionEvent e) {
				panel1.setVisible(false);
				data2 = dao.getUserRooms(id);
				headers2 = new String[] { "" };
				cell2 = new String[data.length];
				table2 = new JTable(data2, headers2);
				table2.setModel(new DefaultTableModel(data2, headers2) {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				}); // 셀 값 수정 불가

				DefaultTableCellRenderer left2 = new DefaultTableCellRenderer();
				left2.setHorizontalAlignment(SwingConstants.LEFT);

				TableColumnModel columnModels2 = table2.getColumnModel();
				table2.getTableHeader().setReorderingAllowed(false); // 테이블 컬럼 이동 방지
				columnModels2.getColumn(0).setResizable(false); // 테이블 컬럼 크기 변경 불가
				// columnModels.getColumn(0).setPreferredWidth(120); // 테이블 컬럼 크기

				for (int i = 0; i < columnModels2.getColumnCount(); i++) {
					columnModels2.getColumn(i).setCellRenderer(left2);
				} // 셀 값을 가운데 정렬 하기 위한 반복문

				// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				// 셀 다중 선택 불가
				table2.setRowHeight(70);
				table2.setFont(font2);
				table2.setAlignmentX(0);
				table2.setSize(327, 585);
				table2.setPreferredScrollableViewportSize(new Dimension(327, 585));
				//table2.addMouseListener(this);
				// table.addActionListener(this);

				scrollpane3 = new JScrollPane(table2);
				scrollpane3.setBounds(63, 75, 327, 585);
				panel2.add(scrollpane3);
				scrollpane4 = new JScrollPane(panel2);
				setContentPane(scrollpane4);
				panel2.setVisible(true);
				setVisible(true);
				repaint();
			}
		});

		OffMenu1_button.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void actionPerformed(ActionEvent e) {
				panel2.setVisible(false);
				data = dao.getFriends(id);
				headers = new String[] { "친구 " + data.length };
				cell = new String[data.length];
				table = new JTable(data, headers);
				table.setModel(new DefaultTableModel(data, headers) {
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				}); // 셀 값 수정 불가

				DefaultTableCellRenderer left = new DefaultTableCellRenderer();
				left.setHorizontalAlignment(SwingConstants.LEFT);

				TableColumnModel columnModels = table.getColumnModel();
				table.getTableHeader().setReorderingAllowed(false); // 테이블 컬럼 이동 방지
				columnModels.getColumn(0).setResizable(false); // 테이블 컬럼 크기 변경 불가
				// columnModels.getColumn(0).setPreferredWidth(120); // 테이블 컬럼 크기

				for (int i = 0; i < columnModels.getColumnCount(); i++) {
					columnModels.getColumn(i).setCellRenderer(left);
				} // 셀 값을 가운데 정렬 하기 위한 반복문

				// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				// 셀 다중 선택 불가
				table.setRowHeight(70);
				table.setFont(font2);
				table.setAlignmentX(0);
				table.setSize(327, 585);
				table.setPreferredScrollableViewportSize(new Dimension(327, 585));
				//table.addMouseListener(this);
				// table.addActionListener(this);

				scrollpane1 = new JScrollPane(table);
				scrollpane1.setBounds(63, 150, 327, 585);
				panel1.add(scrollpane1);
				scrollpane2 = new JScrollPane(panel1);
				setContentPane(scrollpane2);
				panel1.setVisible(true);
				setVisible(true);
				repaint();
			}
		});

		profile_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseEntered(MouseEvent e) {
				if (cnt != 1) {
					OffMe_button.setIcon(OffMe_Mouse);
					name.setBackground(Text_Mouse);
					profile_button.setVisible(true);
					name.setVisible(true);
					repaint();
				}
				repaint();
			}
		});

		profile_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseExited(MouseEvent e) {
				if (cnt != 1) {
					OffMe_button.setIcon(OffMe);
					name.setBackground(Color.WHITE);
					profile_button.setVisible(true);
					name.setVisible(true);
					repaint();
				}
				repaint();
			}
		});

		name.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseEntered(MouseEvent e) {
				if (cnt != 1) {
					OffMe_button.setIcon(OffMe_Mouse);
					name.setBackground(Text_Mouse);
					profile_button.setVisible(true);
					name.setVisible(true);
					repaint();
				}
				repaint();
			}
		});

		name.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseExited(MouseEvent e) {
				if (cnt != 1) {
					OffMe_button.setIcon(OffMe);
					name.setBackground(Color.WHITE);
					profile_button.setVisible(true);
					name.setVisible(true);
					repaint();
				}
				repaint();
			}
		});

		name.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseClicked(MouseEvent e) {
				cnt = 1;
				OffMe_button.setIcon(OnMe);
				// OffMe_button.setRolloverIcon(OnMe);
				name.setBackground(Text_Clicked);
				profile_button.setVisible(true);
				name.setVisible(true);
				repaint();
			}
		});

		OffMe_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseEntered(MouseEvent e) {
				if (cnt != 1) {
					OffMe_button.setIcon(OffMe_Mouse);
					name.setBackground(Text_Mouse);
					profile_button.setVisible(true);
					name.setVisible(true);
					repaint();
				}
				repaint();
			}
		});

		OffMe_button.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseExited(MouseEvent e) {
				if (cnt != 1) {
					OffMe_button.setIcon(OffMe);
					name.setBackground(Color.WHITE);
					profile_button.setVisible(true);
					name.setVisible(true);
					repaint();
				}
				repaint();
			}
		});

		OffMe_button.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void actionPerformed(ActionEvent e) {
				cnt = 1;
				OffMe_button.setIcon(OnMe);
				name.setBackground(Text_Clicked);
				profile_button.setVisible(true);
				name.setVisible(true);
				repaint();
			}
		});
		
		table2.addMouseListener(new MouseAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					JTable table2 = (JTable)e.getSource();
					int row = table2.getSelectedRow();
					String roomid = String.valueOf(table2.getModel().getValueAt(row, 0));
					StringBuilder Cell = new StringBuilder();
					Roomnum_arr = dao.getRoomnums(roomid);
					System.out.println(row);
					JavaObjClientView chat = new JavaObjClientView(Username, IP_addr, Port_no, ID,roomid);
				}
			}
		});

		this.addWindowListener(new WindowAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void windowClosing(WindowEvent e) {
				//dao.dropTable(id);
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}