// BookingBean.java
// Bean for interacting with the Booking table of the database
package DbClasses;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

@SessionScoped
@ManagedBean(name = "bookingBean")
public class BookingBean implements Serializable {

    // instance variables 
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
    private int bookingID;
    private String carID;
    private String firstName;
    private String lastName;
    private String email;
    private Date rentalDate;
    private int noOfDays;
    private int total;
    private ArrayList<BookingBean> bookings = new ArrayList<>();

    // allow the server to inject the DataSource
    @Resource(name = "jdbc/demo")
    DataSource dataSource;

    public BookingBean() {
    }

    public BookingBean(int bookingID, String carID, String firstName, String lastName, String email, Date rentalDate, int noOfDays, int total) {
        this.bookingID = bookingID;
        this.carID = carID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.rentalDate = rentalDate;
        this.noOfDays = noOfDays;
        this.total = total;
    }

    //getters
    public int getBookingID() {
        return bookingID;
    }

    public String getCarID() {
        return carID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getRentalDate() {
        return rentalDate;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public int getTotal() {
        return total;
    }

    public ArrayList<BookingBean> getBookings() {
        return bookings;
    }

    //setters
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRentalDate(Date rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setBookings(ArrayList<BookingBean> bookings) {
        this.bookings = bookings;
    }

    //List all Bookings
    public ArrayList<BookingBean> listAllBookings() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }
        try {
            // create a PreparedStatement to find all booking
            PreparedStatement bookinglist = connection.prepareStatement(" SELECT * FROM BOOKING ");
            //execute query
            ResultSet res = bookinglist.executeQuery();
            while (res.next()) {

                //store retrieved values in variable
                int ID = res.getInt("BOOKING_ID");
                String fname = res.getString("FIRST_NAME");
                String lname = res.getString("LAST_NAME");
                String mail = res.getString("EMAIL");
                Date rdate = res.getDate("RENTAL_DATE");
                int days = res.getInt("NO_OF_DAYS");
                String car = res.getString("CAR_ID");
                int tot = res.getInt("TOTAL");

                //create new object of BookingBean with retrieved values
                BookingBean booking = new BookingBean(ID, car, fname, lname, mail, rdate, days, tot);

                //add new BookingBean to the list
                bookings.add(booking);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return bookings;
    }
    
    
    //Call List all items function but return nothing.
    public void list() throws SQLException {
        clearList();
        listAllBookings();
    }

    //Clear the arraylist
    public void clearList() throws SQLException {
        bookings.removeAll(bookings);
    }

    //find booking by ID
    public String exists(int bID) {
        //search the arraylist 
        for (BookingBean b : bookings) {
            //if found return booking page
            if (bID == b.bookingID) {
                return "booking" + "?faces-redirect=true";
            }
        }
        //if not found return no booking page
        return "noBooking"+ "?faces-redirect=true";
    }

    // Create new entry in table BOOKING
    public String save() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to calculate the retrieve the daily rate of the car
            PreparedStatement getRate = connection.prepareStatement("SELECT DAILY_RATE FROM CAR  WHERE CAR_ID = ? ");

            //specify the PreparedStatemet arguments 
            getRate.setString(1, getCarID());

            //create a result set 
            ResultSet res = getRate.executeQuery();
            while (res.next()) {

                //assign the returned value of daily rate to an integer
                int rate = res.getInt("DAILY_RATE");

                //calculate the total based on the rate and the amount of days specified in the form
                int total = rate * getNoOfDays();

                //set total
                setTotal(total);
            }

            // create a PreparedStatement to insert a new booking entry
            PreparedStatement addEntry = connection.prepareStatement("INSERT INTO BOOKING "
                    + "(FIRST_NAME, LAST_NAME, EMAIL, RENTAL_DATE, NO_OF_DAYS, CAR_ID, TOTAL )"
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ? )");

            // specify the PreparedStatement's arguments
            addEntry.setString(1, getFirstName());
            addEntry.setString(2, getLastName());
            addEntry.setString(3, getEmail());
            addEntry.setString(4, formatter.format(getRentalDate()));
            addEntry.setString(5, String.valueOf(getNoOfDays()));
            addEntry.setString(6, getCarID());
            addEntry.setString(7, String.valueOf(getTotal()));
            addEntry.executeUpdate(); // execute the query, insert the entry to booking table

            // create a PreparedStatement to change availability of the car to 'N'
            PreparedStatement notAvailable = connection.prepareStatement("UPDATE CAR SET AVAILABLE = 'N' WHERE CAR_ID = ? ");

            notAvailable.setString(1, getCarID());
            notAvailable.executeUpdate(); // update Availability

            return "SuccessRent"; 
        } finally {
            connection.close();
        }
    }

    // Retrieve booking id by Car ID
    public void getID() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            PreparedStatement getID = connection.prepareStatement("SELECT BOOKING_ID FROM BOOKING  WHERE CAR_ID = ? ");

            //specify the PreparedStatemet arguments 
            getID.setString(1, getCarID());

            //create a result set 
            ResultSet result = getID.executeQuery();
            while (result.next()) {

                //assign the returned value of daily rate to an integer
                int ID = result.getInt("BOOKING_ID");

                setBookingID(ID);

            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            connection.close();
        }
    }

    //Delete booking from table BOOKING by booking id
    public String delete(int bID) throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to find the car ID of the booking with bID
            PreparedStatement getID = connection.prepareStatement("SELECT CAR_ID FROM BOOKING WHERE BOOKING_ID = ? ");

            getID.setString(1, String.valueOf(bID));
            //execute statement
            ResultSet rs = getID.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);

                // create a PreparedStatement to change availability of the car to 'Y'
                PreparedStatement Available = connection.prepareStatement("UPDATE CAR SET AVAILABLE = 'Y' WHERE CAR_ID = ? ");

                Available.setString(1, id);
                //execute statement
                Available.executeUpdate();
            }
            // create a PreparedStatement to delete the booking from BOOKING
            PreparedStatement delete = connection.prepareStatement("DELETE FROM BOOKING "
                    + "WHERE BOOKING_ID = ? ");
            delete.setString(1, String.valueOf(bID));
            //execute statement
            delete.executeUpdate();

            return "SuccessCancel";
        } finally {
            connection.close();
        }
    }

    //Retrieve all information about booking from table BOOKING by booking id
    public String getBooking(int bID) throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {

            // create a PreparedStatement to delete the booking from BOOKING
            PreparedStatement get = connection.prepareStatement("SELECT * FROM BOOKING "
                    + "WHERE BOOKING_ID = ? ");
            get.setString(1, String.valueOf(bID));
            //execute statement
            ResultSet res = get.executeQuery();
            while (res.next()) {
                String fname = res.getString("FIRST_NAME");
                String lname = res.getString("LAST_NAME");
                String email = res.getString("EMAIL");
                Date rdate = res.getDate("RENTAL_DATE");
                int days = res.getInt("NO_OF_DAYS");
                String car = res.getString("CAR_ID");
                int total = res.getInt("TOTAL");
                setFirstName(fname);
                setLastName(lname);
                setEmail(email);
                setRentalDate(rdate);
                setNoOfDays(days);
                setCarID(car);
                setTotal(total);
            }

            return "Edit";
        } finally {
            connection.close();
        }
    }

    //Update booking from table BOOKING by booking id
    public String update() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to calculate the retrieve the daily rate of the car
            PreparedStatement getRate = connection.prepareStatement("SELECT DAILY_RATE FROM CAR  WHERE CAR_ID = ? ");

            //specify the PreparedStatemet arguments 
            getRate.setString(1, getCarID());

            //create a result set 
            ResultSet res = getRate.executeQuery();
            while (res.next()) {

                //assign the returned value of daily rate to an integer
                int rate = res.getInt("DAILY_RATE");

                //calculate the total based on the rate and the amount of days specified in the form
                int total = rate * getNoOfDays();

                //set total
                setTotal(total);
            }

            // create a PreparedStatement to update the booking
            PreparedStatement update = connection.prepareStatement("UPDATE BOOKING"
                    + " SET FIRST_NAME = ?,  "
                    + "LAST_NAME = ?, "
                    + "EMAIL = ?, "
                    + "RENTAL_DATE = ?, "
                    + "NO_OF_DAYS = ?, "
                    + "CAR_ID =? , "
                    + "TOTAL= ?"
                    + " WHERE BOOKING_ID = ?");

            // specify the PreparedStatement's arguments
            update.setString(1, getFirstName());
            update.setString(2, getLastName());
            update.setString(3, getEmail());
            update.setString(4, formatter.format(getRentalDate()));
            update.setString(5, String.valueOf(getNoOfDays()));
            update.setString(6, getCarID());
            update.setString(7, String.valueOf(getTotal()));
            update.setString(8, String.valueOf(getBookingID()));
            //execute statement
            update.executeUpdate();
            return "SuccessUpdate";
        } finally {
            connection.close();
        }
    }

    //get all from table BOOKING
    public ResultSet getAllBookings() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to find all bookings
            PreparedStatement getAllBookings = connection.prepareStatement(
                    "SELECT BOOKING_ID, FIRST_NAME, LAST_NAME, EMAIL, BOOKING.CAR_ID, CAR.CAR_BRAND, CAR_MODEL, RENTAL_DATE, CAR.DAILY_RATE, NO_OF_DAYS , TOTAL "
                    + "FROM BOOKING, CAR WHERE CAR.CAR_ID = BOOKING.CAR_ID ");

            CachedRowSet rowSet = new com.sun.rowset.CachedRowSetImpl();
            rowSet.populate(getAllBookings.executeQuery());
            return rowSet;
        } finally {
            connection.close();
        }
    }

    //get specific booking from table BOOKING by booking id
    public ResultSet getOne() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }

        try {
            // create a PreparedStatement to find the booking by bID
            PreparedStatement getOne = connection.prepareStatement(
                    "SELECT BOOKING_ID, FIRST_NAME, LAST_NAME, EMAIL, RENTAL_DATE, BOOKING.CAR_ID, CAR_BRAND, CAR_MODEL, CAR_TYPE, NO_OF_DAYS , TOTAL "
                    + "FROM BOOKING, CAR " + " WHERE BOOKING_ID = ? AND  CAR.CAR_ID = BOOKING.CAR_ID "
            );

            getOne.setString(1, String.valueOf(getBookingID()));

            CachedRowSet rowSet = new com.sun.rowset.CachedRowSetImpl();
            rowSet.populate(getOne.executeQuery());
            return rowSet;
        } finally {
            connection.close();
        }
    }

    //Clear all information information about current booking
    public void clearAll() {
        setFirstName("");
        setLastName("");
        setEmail("");
        setRentalDate(null);
        setNoOfDays(0);
        setCarID("");
        setTotal(0);
    }
}