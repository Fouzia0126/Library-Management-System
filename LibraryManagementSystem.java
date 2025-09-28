import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static Connection connection;
    private static UserModule userModule;
    private static BookModule bookModule;
    private static BorrowModule borrowModule;
    private static SearchModule searchModule;
    private static FineModule fineModule;
    private static ReportModule reportModule;
    private static AuthModule authModule;

    public static void main(String[] args) {
        try {
            // SQL connection
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db", "root", "password"
            );
            userModule = new UserModule(connection);
            bookModule = new BookModule(connection);
            borrowModule = new BorrowModule(connection);
            searchModule = new SearchModule(connection);
            fineModule = new FineModule(connection);
            reportModule = new ReportModule(connection);
            authModule = new AuthModule(connection);

            Scanner sc = new Scanner(System.in);
            System.out.println("Welcome to Library Management System");
            // Basic login
            if (!authModule.login(sc)) {
                System.out.println("Invalid login. Exiting.");
                return;
            }
            while (true) {
                System.out.println("\nSelect module:");
                System.out.println("1. User Management");
                System.out.println("2. Book Management");
                System.out.println("3. Borrow/Return");
                System.out.println("4. Search");
                System.out.println("5. Fine Management");
                System.out.println("6. Reporting");
                System.out.println("7. Exit");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1: userModule.menu(sc); break;
                    case 2: bookModule.menu(sc); break;
                    case 3: borrowModule.menu(sc); break;
                    case 4: searchModule.menu(sc); break;
                    case 5: fineModule.menu(sc); break;
                    case 6: reportModule.menu(sc); break;
                    case 7: 
                        System.out.println("Exiting...");
                        connection.close();
                        return;
                    default: System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
