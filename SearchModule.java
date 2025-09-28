import java.sql.*;
import java.util.Scanner;

public class SearchModule {
    private Connection conn;
    public SearchModule(Connection conn) { this.conn = conn; }
    public void menu(Scanner sc) {
        System.out.println("Search: 1.Book by Title 2.Book by Author");
        int ch = sc.nextInt(); sc.nextLine();
        switch(ch) {
            case 1: searchByTitle(sc); break;
            case 2: searchByAuthor(sc); break;
        }
    }
    private void searchByTitle(Scanner sc) {
        System.out.print("Enter title: "); String title = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE title LIKE ?")) {
            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                System.out.println(rs.getInt("id") + ": " + rs.getString("title") + " by " + rs.getString("author"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void searchByAuthor(Scanner sc) {
        System.out.print("Enter author: "); String author = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE author LIKE ?")) {
            ps.setString(1, "%" + author + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                System.out.println(rs.getInt("id") + ": " + rs.getString("title") + " by " + rs.getString("author"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
