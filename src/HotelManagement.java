import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

//Create Hotel Management System using JDBC
public class HotelManagement {

    public static void main(String[] args) {

        try {

            Scanner scanner = new Scanner(System.in);
            JDBC_Connection connection = new JDBC_Connection();
            while (true) {

                System.out.println("1 New Reservation");
                System.out.println("2 Check Reservation");
                System.out.println("3 Get Room Number");
                System.out.println("4 Update Reservation");
                System.out.println("5 Delete Reservation");
                System.out.println("0 Exit");
                System.out.print("Choose an option : ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        newReservation(JDBC_Connection.jdbcConnect(), scanner);
                        break;
                    case 2:
                        checkReservation(JDBC_Connection.jdbcConnect(), scanner);
                        break;
                    case 3:
                        getRoomNumber(JDBC_Connection.jdbcConnect(), scanner);
                        break;
                    case 4:
                        updateReservation(JDBC_Connection.jdbcConnect(), scanner);
                        break;
                    case 5:
                        deleteReservation(JDBC_Connection.jdbcConnect(), scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice Try again");
                        break;
                }
            }


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public static void newReservation(Connection connection, Scanner scanner) {

        System.out.println("Enter guest name");
        String guestName = scanner.next();
        scanner.nextLine();

        System.out.println("Enter room number ");
        int roomNumber = scanner.nextInt();

        System.out.println("Enter guest contact number");
        String contactNumber = scanner.next();
        scanner.nextLine();

        String sql = "insert into reservation (guest_name,room_number,contact_number) values ( ?,?,?) ;";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, guestName);
            preparedStatement.setInt(2, roomNumber);
            preparedStatement.setString(3, contactNumber);

            int effactedRows = preparedStatement.executeUpdate();

            if (effactedRows > 0) {
                System.out.println("Reservation successfully");
            } else {
                System.out.println("Reservation failed");
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }

    }

    public static void checkReservation(Connection connection, Scanner scanner) {
        String sql = "select * from reservation";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest Name      | Room Number   | Contact Number       | " +
                     "Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

//                System.out.println("Reservation Id = " + reservationId);
//                System.out.println("Guest name = " + guestName);
//                System.out.println("Room number = " + roomNumber);
//                System.out.println("Contact number = " + contactNumber);
//                System.out.println("Reservation date = " + reservationDate);

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void getRoomNumber(Connection connection, Scanner scanner) {

        System.out.println("Enter guest reservation id");
        int reservationId = scanner.nextInt();

        String sql = "Select room_number,guest_name,contact_number from reservation where reservation_id = ? ";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int roomNumber = resultSet.getInt("room_number");
                String guestName = resultSet.getString("guest_name");
                String contactNumber = resultSet.getString("contact_number");

                System.out.println("Guest room number : " + roomNumber);
                System.out.println("Guest name : " + guestName);
                System.out.println("Guest contact number : " + contactNumber);

            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public static void updateReservation(Connection connection, Scanner scanner) {
        System.out.println("Enter guest reservation id which guest you change");
        int reservationId = scanner.nextInt();

        System.out.println("Enter guest new name if you want change otherwise enter same previous");
        String newGuestName = scanner.next();
//        scanner.nextLine();

        System.out.println("Enter guest new room number if you want change otherwise enter same previous");
        int newRoom = scanner.nextInt();

        System.out.println("Enter guest new contact number if you want change otherwise enter same previous");
        String newContact = scanner.next();
//        scanner.nextLine();

        String sql = "update reservation set guest_name = ? , room_number = ? , contact_number = ? , " +
                "reservation_date = CURRENT_TIMESTAMP where reservation_id = ?;";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newGuestName);
            preparedStatement.setInt(2, newRoom);
            preparedStatement.setString(3, newContact);
            preparedStatement.setInt(4, reservationId);

            int rowEfacted = preparedStatement.executeUpdate();
            if (rowEfacted > 0) {
                System.out.println("Update successfully");
            } else {
                System.out.println("Updation failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteReservation(Connection connection, Scanner scanner) {
        System.out.println("Enter reservation id");
        int reservationId = scanner.nextInt();
        String sql = "delete from reservation where reservation_id = ? ;";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, reservationId);
            int rowEfacted = preparedStatement.executeUpdate();

            if (rowEfacted > 0) {
                System.out.println("Delete Successfully");
            } else {
                System.out.println("Deletion failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i = 5;
        while (i >= 0) {
            System.out.print(".");
            Thread.sleep(350);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using hotel reservation system");
    }
}