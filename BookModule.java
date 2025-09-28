import java.sql.*;
import java.util.Scanner;

public class BookModule {
    private Connection conn;
    public BookModule(Connection conn) { this.conn = conn; }
    
    public void menu(Scanner sc) {
        System.out.println("Book Management: 1.Add 2.Remove 3.List");
        int ch = sc.nextInt(); sc.nextLine();
        switch(ch) {
            case 1: addBook(sc); break;
            case 2: removeBook(sc); break;
            case 3: listBooks(); break;
        }
    }
    private void addBook(Scanner sc) {
        System.out.print("Enter book title: "); String title = sc.nextLine();
        System.out.print("Enter author: "); String author = sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO books(title, author) VALUES (?,?)")) {
            ps.setString(1, title); ps.setString(2, author); ps.executeUpdate();
            System.out.println("Book added.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void removeBook(Scanner sc) {
        System.out.print("Enter book id: "); int id = sc.nextInt(); sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setInt(1, id); ps.executeUpdate();
            System.out.println("Book removed.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void listBooks() {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM books");
            while(rs.next())
                System.out.println(rs.getInt("id") + ": " + rs.getString("title") + " by " + rs.getString("author"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
