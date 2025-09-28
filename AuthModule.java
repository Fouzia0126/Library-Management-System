import java.sql.*;
import java.util.Scanner;

public class AuthModule {
    private Connection conn;
    public AuthModule(Connection conn) { this.conn = conn; }
    public boolean login(Scanner sc) {
        System.out.print("Enter username: "); String username = sc.nextLine();
        System.out.print("Enter password: "); String password = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement(
            "SELECT * FROM admins WHERE username=? AND password=?")) {
            ps.setString(1, username); ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
