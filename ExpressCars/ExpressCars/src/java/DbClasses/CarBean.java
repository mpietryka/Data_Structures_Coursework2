// CarBean.java
// Bean for interacting with the Car table of the database
package DbClasses;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.sql.DataSource;

@ManagedBean(name = "CarBean")
public class CarBean implements Serializable {

    private String carID;
    private String carBrand;
    private String carModel;
    private String carType;
    private int dailyRate;
    private char available;
    private ArrayList<CarBean> cars = new ArrayList<>();

// allow the server to inject the DataSource
    @Resource(name = "jdbc/demo")
    DataSource dataSource;

//constructors
    public CarBean(String carID, String carBrand, String carModel, String carType, int dailyRate, char available) {
        this.carID = carID;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carType = carType;
        this.dailyRate = dailyRate;
        this.available = available;
    }

    public CarBean() {
    }

//getters
    public String getCarID() {
        return carID;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarType() {
        return carType;
    }

    public int getDailyRate() {
        return dailyRate;
    }

    public String getAvailable() {
        String Available = String.valueOf(available);
        return Available;
    }

    public ArrayList<CarBean> getCars() {
        return cars;
    }

//setters
    public void setCarID(String carID) {
        this.carID = carID;
    }

    public void setCarModel(String CarModel) {
        this.carModel = CarModel;
    }

    public void setCarBrand(String CarBrand) {
        this.carBrand = CarBrand;
    }

    public void getCarType(String CarType) {
        this.carType = CarType;
    }

    public void setDailyRate(int DailyRate) {
        this.dailyRate = DailyRate;
    }

    public void setAvailable(char available) {
        this.available = available;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public void setCars(ArrayList<CarBean> cars) {
        this.cars = cars;
    }

    // Retrieve all from CAR by car id from booking table
    public void getCar(String id) throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }
        try {
            // create a PreparedStatement to find car by id
            PreparedStatement get = connection.prepareStatement(" SELECT * FROM CAR WHERE CAR_ID = ? ");
            get.setString(1, id);
            ResultSet res = get.executeQuery();
            cars.removeAll(cars);
            while (res.next()) {

                String ID = res.getString("CAR_ID");
                String brand = res.getString("CAR_BRAND");
                String model = res.getString("CAR_MODEL");
                String type = res.getString("CAR_TYPE");
                int rate = res.getInt("DAILY_RATE");
                String avail = res.getString("AVAILABLE");
                char a = avail.charAt(0);

                setCarID(ID);
                setCarBrand(brand);
                setCarModel(model);
                setCarType(type);
                setDailyRate(rate);
                setAvailable(a);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // List all available cars
    public ArrayList<CarBean> getAvailableCars() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Unable to obtain DataSource");
        }
        Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to connect to DataSource");
        }
        try {
            // create a PreparedStatement to find available cars
            PreparedStatement carlist = connection.prepareStatement(" SELECT * FROM CAR WHERE AVAILABLE = 'Y' ");
            //execute query
            ResultSet rs = carlist.executeQuery();
            while (rs.next()) {

                //store retrieved values in variable
                String ID = rs.getString("CAR_ID");
                String brand = rs.getString("CAR_BRAND");
                String model = rs.getString("CAR_MODEL");
                String type = rs.getString("CAR_TYPE");
                int rate = rs.getInt("DAILY_RATE");
                String avail = rs.getString("AVAILABLE");
                char available = avail.charAt(0);

                //create new object of CarBean with retrieved values
                CarBean car = new CarBean(ID, brand, model, type, rate, available);

                //add new CarBean to the list
                cars.add(car);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return cars;
    }

    public void clearList() throws SQLException {
        cars.removeAll(cars);
    }

    // Return a description of a car
    public String description() {
        String desc = carBrand + " " + carModel + " £" + dailyRate + " per day";
        return desc;
    }

    // Return a shortened description of a car 
    public String description2() {
        String desc = carBrand + " " + carModel;
        return desc;
    }
}