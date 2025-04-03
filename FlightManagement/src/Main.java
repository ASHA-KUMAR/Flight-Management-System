import java.sql.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scan=new Scanner(System.in);
        String url="jdbc:mysql://localhost:3306/flightbooking";
        String user="root";
        String password="asha";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection(url,user,password);
            System.out.println("Drivaer is loaded");



        while (true) {
            System.out.println("Flight management system");
            System.out.println("1. Register the user");
            System.out.println("2. Search the flights");
            System.out.println("3. Book a flight");
            System.out.println("4. Cancel Bookings");
            System.out.println("5. View Bookings");
            System.out.println("6.Exit");

            System.out.println("Enter your choice");

           int choice=scan.nextInt();
            scan.nextLine();
           Statement stmt=con.createStatement();
           switch(choice){
               case 1:
                   registerUser(stmt,scan);
                   break;
               case 2:
                   SearchFlight(stmt,scan);
                   break;
               case 3:
                   BookFlight(stmt,scan);
                   break;
               case 4:
                   CancelFlight(stmt,scan);
                   break;
               case 5:
                   viewFligth(stmt,scan);
                   break;
               case 6:
                   System.out.println("Existing...");
                   con.close();
                   stmt.close();
           }
        }
        }
            catch(ClassNotFoundException | SQLException e){
                System.out.println(e.getMessage());
            }
        }

    private static void viewFligth(Statement stmt, Scanner scan) throws SQLException{
        System.out.println("Enter user id :");
        int user = scan.nextInt();

        String query = "SELECT * FROM bookings WHERE user_id = " + user;

        ResultSet rs = stmt.executeQuery(query);

        while(rs.next()){
            System.out.println("Booking ID: " + rs.getInt("booking_id")
                    + ", Flight ID: "+rs.getInt("flight_id")
                    + ", Seat Number : " + rs.getInt("seat_number")
                    + ", Status : "+rs.getString("status"));
        }

    }

    private static void CancelFlight(Statement stmt, Scanner scan) throws SQLException{
        System.out.println("Enter user id :");
        int userid=scan.nextInt();
        String cancelquery="update bookings set status='CANCELLED' where user_id="+userid;
        int res=stmt.executeUpdate(cancelquery);
        if(res>0){
            String updatequery="update flights set no_of_seats=no_of_seats+1 where flight_id="+
                    "(select flight_id from bookings where user_id="+userid+")";
        }
    }

    private static void BookFlight(Statement stmt, Scanner scan) throws SQLException {

        System.out.println("Enter the user_id: ");
        int user=scan.nextInt();
        System.out.println("Enter the fligth id: ");
        int flight_id=scan.nextInt();
        String checkfligth="select no_of_seats from flights where flight_id="+flight_id;
        ResultSet r=stmt.executeQuery(checkfligth);
        if(r.next() && r.getInt("no_of_seats")>0) {
            int seatnumber = r.getInt("no_of_seats");


            String bookquery = "insert into bookings(user_id,flight_id,seat_number)" +
                    " values(" + user + "," + flight_id + "," + seatnumber + ")";
            int n=stmt.executeUpdate(bookquery);
            String updatequery="update flights set no_of_seats=no_of_seats-1 where flight_id="+flight_id;
            int res=stmt.executeUpdate(updatequery);
            System.out.println("your flight is booked successfully!!");
        }else {
            System.out.println("Booking is not confirmed");
        }
    }

    private static void SearchFlight(Statement stmt, Scanner scan) throws SQLException{

        System.out.print("Enter your Departure: ");
        String departure=scan.nextLine();
        System.out.println("Enter the destination :");
        String destination=scan.nextLine();
        String query="select * from flights where departure='"+departure+"' and destination='"+destination+"' and no_of_seats>0";
         ResultSet r =stmt.executeQuery(query);
         while(r.next()){
             System.out.println("Flight Id :"+r.getInt("flight_id")+", "+
                     "Airline :"+r.getString("airline")+", "+
                     "Departure :"+r.getString("departure")+", "+
                      "Destination :"+r.getString("destination")+", "+
                     "Date :"+r.getDate("date")+", "+
                     "N0 of seats :"+r.getInt("no_of_seats")+", "+
                     "Price :"+r.getInt("price"));
         }
    }

    private static void registerUser(Statement stmt, Scanner scan) throws SQLException{

        System.out.print("Enter name: ");
        String name=scan.nextLine();
        System.out.print("Enter your email: ");
        String email=scan.nextLine();
        System.out.print("Enter phone number: ");
        long phone=scan.nextInt();
        String query="INSERT INTO users(name,email_id,number) values('" +name+ "','"+email+"','"+phone+"')";
           int rows=stmt.executeUpdate(query);
        System.out.println("user is registered successfully!!");
    }
}



