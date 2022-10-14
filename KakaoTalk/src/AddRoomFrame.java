import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import SQL.DAO;
import SQL.DTO;

public class AddRoomFrame extends JFrame implements ActionListener, MouseListener {
	private JScrollPane scrollpane1, scrollpane2;
	ImageIcon Frame = new ImageIcon("Image/AddRoomFrame.png");
	ImageIcon DeleteText = new ImageIcon("Image/DeleteText.png");
	ImageIcon DeleteText_Mouse = new ImageIcon("Image/DeleteText_Mouse.png");
	ImageIcon Okay_Original = new ImageIcon("Image/Okay_Original.png");
	ImageIcon Okay = new ImageIcon("Image/Okay.png");
	ImageIcon Okay_Mouse = new ImageIcon("Image/Okay_Mouse.png");
	Font font2 = new Font("맑은 고딕", Font.PLAIN, 13);
	Font font3 = new Font("맑은 고딕", Font.PLAIN, 50);
	private DAO dao;
	private JButton okay_button;
	private JTextField id_field;
	private JTextField field;
	private String[][] data;
	private String[] headers;
	private String[] cell;
	private JTable table;

	public AddRoomFrame(String username, String ip_addr, String port_no, String id) {

		setTitle("KakaoTalk"); // 타이틀
		setSize(383, 605); // 창 사이즈
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 없으면 X를 눌러도 JVM 상에 메모리로 상주
		setLocationRelativeTo(null); // 창을 화면 상의 가운데에 정렬. null(기본값)은 가운데

		JPanel tablepanel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(Frame.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};
		tablepanel.setBounds(0, 0, 383, 467);
		tablepanel.setLayout(null);
		add(tablepanel);

		JTextField id_field = new JTextField() {
			@Override
			public void setBorder(Border border) {
			}
		};
		id_field.setBounds(56, 68, 265, 30);
		id_field.setFont(font2);
		tablepanel.add(id_field);

		JButton DeleteText_button = new JButton(DeleteText);
		DeleteText_button.setBounds(326, 71, 26, 24);
		DeleteText_button.setBorderPainted(false);
		DeleteText_button.setEnabled(false);
		DeleteText_button.setRolloverIcon(DeleteText_Mouse);
		tablepanel.add(DeleteText_button);
		DeleteText_button.setVisible(false);

		okay_button = new JButton(Okay_Original);
		okay_button.setBounds(270, 510, 81, 40);
		okay_button.setBorderPainted(false);
		okay_button.setEnabled(false);
		okay_button.setDisabledIcon(Okay_Original);
		tablepanel.add(okay_button);

		field = new JTextField();
		field.setBounds(8, 450, 350, 30);
		field.setEnabled(false);
		field.setDisabledTextColor(Color.BLACK);
		tablepanel.add(field);

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
		table.setRowHeight(30);
		table.setFont(font2);
		table.setAlignmentX(0);
		table.setSize(367, 320);
		table.setPreferredScrollableViewportSize(new Dimension(367, 320));
		table.addMouseListener(this);
		// table.addActionListener(this);

		scrollpane1 = new JScrollPane(table);
		scrollpane1.setBounds(0, 120, 367, 320);
		tablepanel.add(scrollpane1);
		scrollpane2 = new JScrollPane(tablepanel);
		setContentPane(scrollpane2);

		okay_button.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void actionPerformed(ActionEvent e) {
				int ran=1;
				String[][] roomnum_arr;
				while(true) {
					if(dao.checkRoom(String.valueOf(ran))!=true) {
						dao.addUserRoom(id, String.valueOf(ran));
						dao.addRoom(String.valueOf(ran));
						dao.createRoomnumTable(String.valueOf(ran));
						dao.addRoomnum(String.valueOf(ran), id);
						for(int i=0;i<cell.length;i++) {
							if(cell[i]!=null)
								dao.addRoomnum(String.valueOf(ran), cell[i]);
						}
						roomnum_arr = dao.getRoomnums(String.valueOf(ran));
						for(int i =0;i<roomnum_arr.length;i++) {
							dao.createUserRoomTable(roomnum_arr[i][0]);
							dao.addUserRoom(roomnum_arr[i][0], String.valueOf(ran));
						}
						break;
					}
					ran++;
				}
				SecondFrame sf = new SecondFrame(username, ip_addr, port_no, id);
				setVisible(false);
			}
		});

		DeleteText_button.addActionListener(new ActionListener() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void actionPerformed(ActionEvent e) {
				id_field.setText("");
				String val = id_field.getText();
				TableRowSorter<TableModel> trs = new TableRowSorter<>(table.getModel());
				table.setRowSorter(trs);
				trs.setRowFilter(RowFilter.regexFilter(val));
				DeleteText_button.setVisible(false);
			}
		});

		id_field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (id_field.getText().trim().length() != 0) {
					DeleteText_button.setVisible(true);
					DeleteText_button.setEnabled(true);
					String val = id_field.getText();
					TableRowSorter<TableModel> trs = new TableRowSorter<>(table.getModel());
					table.setRowSorter(trs);
					trs.setRowFilter(RowFilter.regexFilter(val));
				} else {
					DeleteText_button.setEnabled(false);
					DeleteText_button.setVisible(false);
					String val = id_field.getText();
					TableRowSorter<TableModel> trs = new TableRowSorter<>(table.getModel());
					table.setRowSorter(trs);
					trs.setRowFilter(RowFilter.regexFilter(val));
				}
			}
		});
		
		field.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (field.getText().trim().length() != 0) {
					okay_button.setEnabled(true);
					okay_button.setIcon(Okay);
					okay_button.setRolloverIcon(Okay_Mouse);
				} 
				else
					okay_button.setEnabled(false);
			}
		});

		setVisible(true);

		this.addWindowListener(new WindowAdapter() { // 버튼 클릭에 관한 메소드
			@Override // ActionListener메소드 사용을 위한 오버라이딩
			public void windowClosing(WindowEvent e) {
				SecondFrame sf = new SecondFrame(username, ip_addr, port_no, id);
				setVisible(false);
			}
		});
	}

	public static void main(String[] args) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = table.getSelectedRow();
		StringBuilder Cell = new StringBuilder();
		if (cell[row] == null)
			cell[row] = String.valueOf(table.getModel().getValueAt(row, 0));
		else
			cell[row] = null;
		for (int i = 0; i < data.length; i++) {
			if (cell[i] != null)
				Cell.append(String.valueOf(cell[i]) + " ");
		}
		field.setEnabled(true);
		field.setFont(font2);
		field.setText(String.valueOf(Cell));
		field.setEnabled(false);
		if (field.getText().trim().length() != 0) {
			okay_button.setEnabled(true);
			okay_button.setIcon(Okay);
			okay_button.setRolloverIcon(Okay_Mouse);
		} 
		else
			okay_button.setEnabled(false);
		for (int i = 0; i < data.length; i++) {
			System.out.println(cell[i]);
		}

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

	@Override
	public void actionPerformed(ActionEvent e) {

	}

}
