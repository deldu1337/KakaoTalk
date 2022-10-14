package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {
	public static void main(String[] args) {

	}
	
	public static Boolean getLogin(String ID, String PW) {
		Connection con = getConnection();
		try {
			String sql = "SELECT * FROM User WHERE ID = ? AND PW = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, ID);
			statement.setNString(2, PW);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				result.close();
				return true;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return false;
	}
	
	public static Boolean getAdminLogin(String ID, String PW) {
		Connection con = getConnection();
		try {
			String sql = "SELECT * FROM Admin WHERE ID = ? AND PW = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, ID);
			statement.setNString(2, PW);
			ResultSet result = statement.executeQuery();
			if(result.next())
				return true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return false;
	}
	
	public static Boolean getAdminJoin() {
		Connection con = getConnection();
		try {
			String sql = "SELECT EXISTS(SELECT * FROM Admin) AS cnt";
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			if(result.next())
				return true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return false;
	}
	
	public static String getName(int num) {
		Connection con = getConnection();
		try {
			String sql = "SELECT Name FROM User WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			ResultSet result = statement.executeQuery();
			if(result.next())
				return result.getNString("Name");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return null;
	}
	
	public static String getID(int num) {
		Connection con = getConnection();
		try {
			String sql = "SELECT ID FROM User WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			ResultSet result = statement.executeQuery();
			if(result.next())
				return result.getNString("ID");
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return null;
	}
	
	public static String getRemain(int num) {
		Connection con = getConnection();
		try {
			String sql = "SELECT Remain FROM User WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			ResultSet result = statement.executeQuery();
			if(result.next())
				return String.valueOf(result.getInt("Remain"));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return null;
	}
	
	public static void setRemain(int num, int coin) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET Remain = Remain + ? WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, coin);
			statement.setInt(2, num);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void ChangeRemain(String ID, int coin) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET Remain = ? WHERE ID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, coin);
			statement.setNString(2, ID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void MinusRemain(int num, int coin) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET Remain = Remain - ? WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, coin);
			statement.setInt(2, num);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static String getUsingCoin(int num) {
		Connection con = getConnection();
		try {
			String sql = "SELECT UsingCoin FROM User WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			ResultSet result = statement.executeQuery();
			if(result.next())
				return String.valueOf(result.getInt("UsingCoin"));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return null;
	}
	
	public static void setUsingCoin(int num, int coin) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET UsingCoin = UsingCoin + ? WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, coin);
			statement.setInt(2, num);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void resetUsingCoin(int num) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET UsingCoin = 0 WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void MinusUsingCoin(int num) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET UsingCoin = UsingCoin - 1 WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void MergeUsingCoin(String ID) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET Remain = Remain + UsingCoin, UsingCoin = 0 WHERE ID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, ID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void setRoom(String ID, int num) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET Room = ? WHERE ID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			statement.setNString(2, ID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Boolean getState(String State, int num) {
		Connection con = getConnection();
		try {
			String sql = "SELECT * FROM User WHERE Room = ? AND State = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setInt(1, num);
			statement.setNString(2, State);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				result.close();
				return true;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return false;
	}
	
	public static void setOnState(String ID) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET State = 'ONLINE' WHERE ID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, ID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void setOffState(String ID) {
		Connection con = getConnection();
		try {
			String sql = "UPDATE User SET State = 'OFFLINE' WHERE ID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, ID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void deleteFriend(String ID, String FID) {
		Connection con = getConnection();
		try {
			String sql = "DELETE FROM " + ID + " WHERE FID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, FID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void deleteRoom(String Room) {
		Connection con = getConnection();
		try {
			String sql = "DELETE FROM Room WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, Room);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void deleteUserRoom(String ID, String Room) {
		Connection con = getConnection();
		try {
			String sql = "DELETE FROM " + ID + "_Room WHERE Room = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, Room);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void deleteRoomnum(String ID, String Roomnum) {
		Connection con = getConnection();
		try {
			String sql = "DELETE FROM Room" + Roomnum + " WHERE ID = ?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, ID);
			int update = statement.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void addFriend(String ID, String FID) {
		try {
			Connection con = getConnection();
			PreparedStatement insert = con.prepareStatement(""
					+ "INSERT INTO " + ID
					+ "(FID) "
					+ "VALUES "
					+ "('" + FID + "')");
			insert.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void addRoom(String Room) {
		try {
			Connection con = getConnection();
			PreparedStatement insert = con.prepareStatement(""
					+ "INSERT INTO Room"
					+ "(Room) "
					+ "VALUES "
					+ "('" + Room + "')");
			insert.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void addUserRoom(String ID, String Room) {
		try {
			Connection con = getConnection();
			PreparedStatement insert = con.prepareStatement(""
					+ "INSERT INTO " + ID + "_Room"
					+ "(Room) "
					+ "VALUES "
					+ "('" + Room + "')");
			insert.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void addRoomnum(String Roomnum, String ID) {
		try {
			Connection con = getConnection();
			PreparedStatement insert = con.prepareStatement(""
					+ "INSERT INTO Room" + Roomnum
					+ "(ID) "
					+ "VALUES "
					+ "('" + ID + "')");
			insert.executeUpdate();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static Boolean checkRoom(String Room) {
		Connection con = getConnection();
		try {
			String sql = "SELECT COUNT(*) cnt FROM Room WHERE Room=?";
			PreparedStatement statement = con.prepareStatement(sql);
			statement.setNString(1, Room);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				int cnt = result.getInt("cnt");
				if (cnt>0)
					return true;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return false;
	}
	
	public static Boolean checkRoomnum(String Roomnum) {
		Connection con = getConnection();
		try {
			String sql = "SELECT COUNT(*) cnt FROM Room" + Roomnum;
			PreparedStatement statement = con.prepareStatement(sql);
			ResultSet result = statement.executeQuery();
			if(result.next()) {
				int cnt = result.getInt("cnt");
				if (cnt>1)
					return true;
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		} finally {
			con=null;
		}
		return false;
	}
	
	public static String[][] getFriends(String ID) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement(
					"SELECT * FROM " + ID);
			ResultSet results = statement.executeQuery();
			ArrayList<String[]> list = new ArrayList<String[]>();
			while(results.next()) {
				list.add(new String[] {
						results.getString("FID")
						});
			}
			System.out.println("The data has been fetched");
			String[][] arr = new String[list.size()][1];
			return list.toArray(arr);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static String[][] getRooms() {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement(
					"SELECT * FROM Room");
			ResultSet results = statement.executeQuery();
			ArrayList<String[]> list = new ArrayList<String[]>();
			while(results.next()) {
				list.add(new String[] {
						results.getString("Room")
						});
			}
			System.out.println("The data has been fetched");
			String[][] arr = new String[list.size()][1];
			return list.toArray(arr);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static String[][] getUserRooms(String ID) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement(
					"SELECT * FROM " + ID + "_Room");
			ResultSet results = statement.executeQuery();
			ArrayList<String[]> list = new ArrayList<String[]>();
			while(results.next()) {
				list.add(new String[] {
						results.getString("Room")
						});
			}
			System.out.println("The data has been fetched");
			String[][] arr = new String[list.size()][1];
			return list.toArray(arr);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static String[][] getRoomnums(String Roomnum) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con.prepareStatement(
					"SELECT * FROM Room" + Roomnum);
			ResultSet results = statement.executeQuery();
			ArrayList<String[]> list = new ArrayList<String[]>();
			while(results.next()) {
				list.add(new String[] {
						results.getString("ID")
						});
			}
			System.out.println("The data has been fetched");
			String[][] arr = new String[list.size()][1];
			return list.toArray(arr);
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public static void dropTable(String ID) {
		Connection con = getConnection();
		try {
			String sql = "DROP TABLE " + ID;
			PreparedStatement statement = con.prepareStatement(sql);
			statement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Table successfully deleted");
		}
	}
	
	public static void createUserTable(String ID) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE IF NOT EXISTS "
							+ ID + "("
							+ "FID varChar(20),"
							+ "PRIMARY KEY(FID))");
			statement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Table successfully created");
		}
	}
	
	public static void createRoomTable() {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE IF NOT EXISTS "
							+ "Room("
							+ "Room varChar(255),"
							+ "PRIMARY KEY(Room))");
			statement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Table successfully created");
		}
	}
	
	public static void createUserRoomTable(String ID) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE IF NOT EXISTS "
							+ ID + "_Room ("
							+ "Room varChar(255),"
							+ "PRIMARY KEY(Room))");
			statement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Table successfully created");
		}
	}
	
	public static void createRoomnumTable(String Roomnum) {
		try {
			Connection con = getConnection();
			PreparedStatement statement = con
					.prepareStatement("CREATE TABLE IF NOT EXISTS "
							+ "Room" + Roomnum + "("
							+ "ID varChar(255),"
							+ "PRIMARY KEY(ID))");
			statement.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			System.out.println("Table successfully created");
		}
	}

	public static Connection getConnection() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/kakao";
			String dbID = "root";
			String dbPW = "qwe123";
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(dbURL, dbID, dbPW);
			System.out.println("The Connection Successful");
			return con;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}