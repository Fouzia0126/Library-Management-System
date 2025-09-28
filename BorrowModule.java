import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;

public class BorrowModule {
    private Connection conn;
    public BorrowModule(Connection conn) { this.conn = conn; }
    public void menu(Scanner sc) {
        System.out.println("Borrow/Return: 1.Borrow 2.Return 3.List Borrows");
        int ch = sc.nextInt(); sc.nextLine();
        switch(ch) {
            case 1: borrowBook(sc); break;
            case 2: returnBook(sc); break;
            case 3: listBorrows(); break;
        }
    }
    private void borrowBook(Scanner sc) {
        System.out.print("Enter user id: "); int userId = sc.nextInt(); sc.nextLine();
        System.out.print("Enter book id: "); int bookId = sc.nextInt(); sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO borrows(user_id, book_id, borrow_date) VALUES (?,?,?)")) {
            ps.setInt(1, userId); ps.setInt(2, bookId); ps.setString(3, LocalDate.now().toString());
            ps.executeUpdate();
            System.out.println("Book borrowed.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void returnBook(Scanner sc) {
        System.out.print("Enter borrow id: "); int borrowId = sc.nextInt(); sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement(
            "UPDATE borrows SET return_date=? WHERE id=?")) {
            ps.setString(1, LocalDate.now().toString()); ps.setInt(2, borrowId);
            ps.executeUpdate();
            System.out.println("Book returned.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void listBorrows() {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM borrows");
            while(rs.next())
                System.out.println("Borrow ID: " + rs.getInt("id") + ", User: " + rs.getInt("user_id") +
                    ", Book: " + rs.getInt("book_id") + ", Borrowed: " + rs.getString("borrow_date") +
                    ", Returned: " + rs.getString("return_date"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
