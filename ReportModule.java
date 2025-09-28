import java.sql.*;
import java.util.Scanner;

public class ReportModule {
    private Connection conn;
    public ReportModule(Connection conn) { this.conn = conn; }
    public void menu(Scanner sc) {
        System.out.println("Reporting: 1.Books Borrowed 2.Users with Fines");
        int ch = sc.nextInt(); sc.nextLine();
        switch(ch) {
            case 1: borrowedBooksReport(); break;
            case 2: usersWithFinesReport(); break;
        }
    }
    private void borrowedBooksReport() {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(
                "SELECT users.name, books.title, borrows.borrow_date FROM borrows " +
                "JOIN users ON borrows.user_id = users.id " +
                "JOIN books ON borrows.book_id = books.id " +
                "WHERE borrows.return_date IS NULL"
            );
            while(rs.next())
                System.out.println("User: " + rs.getString("name") +
                    ", Book: " + rs.getString("title") +
                    ", Borrowed: " + rs.getString("borrow_date"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void usersWithFinesReport() {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(
                "SELECT users.name, SUM(fines.amount) AS total_fine " +
                "FROM fines " +
                "JOIN borrows ON fines.borrow_id = borrows.id " +
                "JOIN users ON borrows.user_id = users.id " +
                "GROUP BY users.id HAVING total_fine > 0"
            );
            while(rs.next())
                System.out.println("User: " + rs.getString("name") +
                    ", Total Fine: Rs " + rs.getLong("total_fine"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
