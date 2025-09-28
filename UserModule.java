import java.sql.*;
import java.util.Scanner;

public class UserModule {
    private Connection conn;
    public UserModule(Connection conn) { this.conn = conn; }
    
    public void menu(Scanner sc) {
        System.out.println("User Management: 1.Add 2.Remove 3.List");
        int ch = sc.nextInt(); sc.nextLine();
        switch(ch) {
            case 1: addUser(sc); break;
            case 2: removeUser(sc); break;
            case 3: listUsers(); break;
        }
    }
    private void addUser(Scanner sc) {
        System.out.print("Enter user name: "); String name = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO users(name) VALUES (?)")) {
            ps.setString(1, name); ps.executeUpdate();
            System.out.println("User added.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void removeUser(Scanner sc) {
        System.out.print("Enter user id: "); int id = sc.nextInt(); sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
            System.out.println("User removed.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void listUsers() {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM users");
            while(rs.next())
                System.out.println(rs.getInt("id") + ": " + rs.getString("name"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
