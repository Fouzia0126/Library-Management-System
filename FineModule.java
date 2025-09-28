import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineModule {
    private Connection conn;
    public FineModule(Connection conn) { this.conn = conn; }
    public void menu(Scanner sc) {
        System.out.println("Fine Management: 1.Calculate Fine 2.List Fines");
        int ch = sc.nextInt(); sc.nextLine();
        switch(ch) {
            case 1: calculateFine(sc); break;
            case 2: listFines(); break;
        }
    }
    private void calculateFine(Scanner sc) {
        System.out.print("Enter borrow id: "); int borrowId = sc.nextInt(); sc.nextLine();
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM borrows WHERE id=?")) {
            ps.setInt(1, borrowId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String borrowDate = rs.getString("borrow_date");
                String returnDate = rs.getString("return_date");
                if (returnDate == null) {
                    System.out.println("Book not yet returned.");
                    return;
                }
                LocalDate bDate = LocalDate.parse(borrowDate);
                LocalDate rDate = LocalDate.parse(returnDate);
                long days = ChronoUnit.DAYS.between(bDate, rDate);
                long fine = (days > 14) ? (days - 14) * 5 : 0; // Rs 5 per day after 14 days
                System.out.println("Fine: Rs " + fine);
                // Optionally write to fines table
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO fines(borrow_id, amount) VALUES (?, ?)");
                ps2.setInt(1, borrowId); ps2.setLong(2, fine);
                ps2.executeUpdate();
            } else {
                System.out.println("Borrow record not found.");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    private void listFines() {
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM fines");
            while(rs.next())
                System.out.println("Borrow ID: " + rs.getInt("borrow_id") + ", Amount: Rs " + rs.getLong("amount"));
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
