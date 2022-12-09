import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static Connection connect() {
        String url = "jdbc:sqlite:C:/Users/phtef/Documents/DBLabb3StefanKarlssonJAVA22.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printMenu() {
        System.out.println("\nChoose:\n");
        System.out.println("0  - Exit program\n" +
                "1  - Show all animals\n" +
                "2  - Add an animal\n" +
                "3  - Update seen animal\n" +
                "4  - Remove an animal\n" +
                "5  - Search for an animal\n" +
                "6  - Show all seen animals");
    }

    private static void selectAll() {
        String sql = "SELECT animalId, animalName, categoryName, haveSeenAnimal FROM animalsInMyArea INNER JOIN category c ON animalsInMyArea.animalCategoryId = c.categoryId";

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt("animalId") + "\t" +
                        rs.getString("animalName") + "\t" +
                        rs.getString("categoryName") + "\t" +
                        rs.getInt("haveSeenAnimal"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertAnimal() {
        System.out.println("Enter animal Name: ");
        String inputanimalName = scanner.nextLine();
        System.out.println("Have you seen it?(0 or 1): ");
        int inputhaveSeenAnimal = scanner.nextInt();
        System.out.println("What category does it belong too? (1.Bird, 2.Amphibian, 3.Mammal)");
        int inputanimalCategoryId = scanner.nextInt();
        insert(inputanimalName, inputhaveSeenAnimal, inputanimalCategoryId);
        scanner.nextLine();
    }

    private static void insert(String name, int haveSeenAnimal, int animalCategoryId) {
        String sql = "INSERT INTO animalsInMyArea(animalName, haveSeenAnimal, animalCategoryId) VALUES(?,?,?)";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, haveSeenAnimal);
            pstmt.setInt(3, animalCategoryId);
            pstmt.executeUpdate();
            System.out.println("You have added an animal.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void update(String name, int haveSeenAnimal, int animalCategoryId, int id) {
        String sql = "UPDATE animalsInMyArea SET animalName = ? , "
                + "haveSeenAnimal = ? , "
                + "animalCategoryId = ?  "
                + "WHERE animalId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, haveSeenAnimal);
            pstmt.setInt(3, animalCategoryId);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("You have updated an animal");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateAnimal() {
        System.out.println("Enter the id of the animal you want to update: ");
        int inputId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter animal Name: ");
        String inputanimalName = scanner.nextLine();
        System.out.println("Have you seen it?(0 or 1): ");
        int inputhaveSeenAnimal = scanner.nextInt();
        System.out.println("What category does it belong too? (1.Bird, 2.Amphibian, 3.Mammal)");
        int inputanimalCategoryId = scanner.nextInt();
        update(inputanimalName, inputhaveSeenAnimal, inputanimalCategoryId, inputId);
        scanner.nextLine();
    }


    private static void delete(int id) {
        String sql = "DELETE FROM animalsInMyArea WHERE animalId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("You have removed this animal.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteAnimalFromTable() {
        System.out.println("Input the ID of the animal you want to remove: ");
        int inputId = scanner.nextInt();
        delete(inputId);
        scanner.nextLine();
    }

    private static void searchAnimal() {
        String sql = "SELECT animalName, categoryName, haveSeenAnimal FROM animalsInMyArea INNER JOIN category c ON animalsInMyArea.animalCategoryId = c.categoryId WHERE animalName = ? ";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            System.out.println("Enter animal name: ");
            String inputName = scanner.nextLine();

            pstmt.setString(1, inputName);
            //
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString("animalName") + "\t" +
                        rs.getString("categoryName") + "\t" +
                        rs.getInt("haveSeenAnimal"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showSeenAnimals() {
        String sql = "SELECT animalName FROM animalsInMyArea WHERE haveSeenAnimal = 1 ORDER BY animalId";

        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString("animalName") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    public static void main(String[] args) {

        boolean quit = false;

        while (!quit) {
            printMenu();
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0:
                    System.out.println("\nShutting down.");
                    quit = true;
                    break;

                case 1:
                    selectAll();
                    break;

                case 2:
                    insertAnimal();
                    break;

                case 3:
                    updateAnimal();
                    break;

                case 4:
                    deleteAnimalFromTable();
                    break;

                case 5:
                    searchAnimal();
                    break;

                case 6:
                    showSeenAnimals();
                    break;
            }
        }

    }

}
