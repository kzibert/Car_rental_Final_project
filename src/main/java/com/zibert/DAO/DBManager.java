package com.zibert.DAO;

import com.zibert.DAO.entity.*;
import com.zibert.DAO.exceptions.BusyCarException;
import com.zibert.DAO.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final String SQL_GET_ALL_CARS = "SELECT car.id, car.model, car.quality_class, car.price, car.car_status, car.brand_id, brand.name AS 'brand_name' FROM car INNER JOIN brand on car.brand_id = brand.id";
    private static final String SQL_GET_ALL_ORDERS = "SELECT ORDERS.ID, ORDERS.RENT_START, ORDERS.RENT_END, ORDERS.PASSPORT, ORDERS.DRIVER, ORDERS.USER_ID, ORDERS.CAR_ID, ORDERS.CANCEL_COMMENTS, ORDERS.STATUS, CAR.MODEL AS 'CAR_MODEL', CAR.CAR_STATUS AS 'CAR_STATUS', CAR.BRAND_ID AS 'BRAND_ID', BRAND.NAME AS 'BRAND_NAME', ORDER_RECEIPT.PAYMENT_STATUS AS 'PAYMENT_STATUS', ORDER_RECEIPT.PAYMENT_DATE AS 'PAYMENT_DATE', ORDER_RECEIPT.COST AS 'COST', DAMAGE_RECEIPT.PAYMENT_STATUS AS 'DAMAGE_PAYMENT_STATUS', DAMAGE_RECEIPT.PAYMENT_DATE AS 'DAMAGE_PAYMENT_DATE', DAMAGE_RECEIPT.COST AS 'DAMAGE_COST' FROM ORDERS " +
            "INNER JOIN CAR ON ORDERS.CAR_ID = CAR.ID " +
            "INNER JOIN BRAND ON BRAND_ID=BRAND.ID " +
            "LEFT JOIN DAMAGE_RECEIPT ON ORDERS.ID=DAMAGE_RECEIPT.ORDER_ID " +
            "LEFT JOIN ORDER_RECEIPT ON ORDERS.ID=ORDER_RECEIPT.ORDER_ID";
    private static final String SQL_INSERT_USER = "INSERT INTO USER VALUES (default, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_USER_BY_EMAIL = "SELECT * FROM USER WHERE email=?";
    private static final String SQL_INSERT_BRAND = "INSERT INTO BRAND VALUES (default, ?)";
    private static final String SQL_GET_ALL_BRANDS = "SELECT * FROM BRAND";
    private static final String SQL_DELETE_BRAND = "DELETE FROM BRAND WHERE id = ?";
    private static final String SQL_INSERT_CAR = "INSERT INTO CAR VALUES (default, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_CAR = "DELETE FROM CAR WHERE id = ?";
    private static final String SQL_UPDATE_CAR = "UPDATE CAR SET BRAND_ID=?, MODEL=?, QUALITY_CLASS=?, PRICE=?, CAR_STATUS=? WHERE ID=?";
    private static final String SQL_GET_CAR_BY_ID = SQL_GET_ALL_CARS + " WHERE CAR.ID=?";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM USER";
    private static final String SQL_UPDATE_USER_STATUS = "UPDATE USER SET STATUS=? WHERE ID=?";
    private static final String SQL_GET_ALL_CAR_SORTED_BY_MODEL = SQL_GET_ALL_CARS + " ORDER BY MODEL";
    private static final String SQL_GET_ALL_CAR_SORTED_BY_PRICE = SQL_GET_ALL_CARS + " ORDER BY PRICE";
    private static final String SQL_GET_ALL_CAR_WITH_BRAND = SQL_GET_ALL_CARS + " WHERE BRAND_ID=?";
    private static final String SQL_GET_ALL_CAR_WITH_CLASS = SQL_GET_ALL_CARS + " WHERE QUALITY_CLASS=?";
    private static final String SQL_GET_ALL_CARS_QUALITY_CLASSES = "SELECT DISTINCT CAR.QUALITY_CLASS FROM CAR ORDER BY QUALITY_CLASS";
    private static final String SQL_INSERT_ORDER = "INSERT INTO ORDERS VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_GET_ORDER_BY_ID = SQL_GET_ALL_ORDERS + " WHERE ORDERS.ID = ?";
    private static final String SQL_INSERT_ORDER_RECEIPT = "INSERT INTO ORDER_RECEIPT VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_ORDER_RECEIPT_PAYMENT_STATUS = "UPDATE ORDER_RECEIPT SET PAYMENT_STATUS=?, PAYMENT_DATE=? WHERE ORDER_ID=?";
    private static final String SQL_GET_ALL_ORDERS_BY_USERID = SQL_GET_ALL_ORDERS + " WHERE USER_ID=? ORDER BY ID DESC limit ? offset ?";
    private static final String SQL_GET_ALL_RECEIVED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE STATUS='Order received' ORDER BY ID DESC limit ? offset ?";
    private static final String SQL_APPROVE_ORDER = "UPDATE ORDERS SET STATUS='Order approved' WHERE ID=?";
    private static final String SQL_CANCEL_ORDER = "UPDATE ORDERS SET STATUS='Order cancelled', CANCEL_COMMENTS=? WHERE ID=?";
    private static final String SQL_GET_ALL_APPROVED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE STATUS='Order approved' ORDER BY ID limit ? offset ?";
    private static final String SQL_GET_ALL_CARS_BUT_DAMAGED = SQL_GET_ALL_CARS + " WHERE CAR_STATUS='Available' limit ? offset ?";
    private static final String SQL_GET_ALL_NOT_WORKABLE_CARS = SQL_GET_ALL_CARS +
            " LEFT JOIN ORDERS ON CAR.ID=ORDERS.CAR_ID " +
            "where (status='Order approved' and ? BETWEEN rent_start AND rent_end) " +
            "or (status='Order approved' and ? BETWEEN rent_start AND rent_end) " +
            "or (status='Order approved' and  rent_start > ? and rent_end < ?) " +
            "or (status='Order received' and ? BETWEEN rent_start AND rent_end) " +
            "or (status='Order received' and ? BETWEEN rent_start AND rent_end) " +
            "or (status='Order received' and  rent_start > ? and rent_end < ?) " +
            "or (car_status='Damaged')";
    private static final String SQL_INSERT_DAMAGE_RECEIPT = "INSERT INTO DAMAGE_RECEIPT VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_CAR_STATUS_AS_DAMAGED = "update car set car_status='Damaged' where id=?";
    private static final String SQL_SET_ORDER_AS_CLOSED = "update orders set status='Order closed' where id=?";
    private static final String SQL_GET_ALL_CLOSED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE STATUS='Order closed' ORDER BY ID DESC limit ? offset ?";
    private static final String SQL_CHECK_IF_ANY_ORDERS_FOR_GIVEN_DATES = SQL_GET_ALL_ORDERS +
            " where (car_id=? and (status='Order received' or status='Order approved'))" +
            " and (? BETWEEN rent_start AND rent_end or ? BETWEEN rent_start AND rent_end or (rent_start > ? and rent_end < ?))";
    private static final String SQL_UPDATE_DAMAGE_RECEIPT_PAYMENT_STATUS = "UPDATE DAMAGE_RECEIPT SET PAYMENT_STATUS=?, PAYMENT_DATE=? WHERE ORDER_ID=?";
    private static final String SQL_GET_ALL_CANCELLED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE STATUS='Order cancelled' ORDER BY ID DESC limit ? offset ?";
    private static final String SQL_COUNT_CLOSED_ORDERS = "SELECT COUNT(*) FROM ORDERS WHERE STATUS='Order closed'";
    private static final String SQL_COUNT_CANCELLED_ORDERS = "SELECT COUNT(*) FROM ORDERS WHERE STATUS='Order cancelled'";
    private static final String SQL_COUNT_RECEIVED_ORDERS = "SELECT COUNT(*) FROM ORDERS WHERE STATUS='Order received'";
    private static final String SQL_COUNT_APPROVED_ORDERS = "SELECT COUNT(*) FROM ORDERS WHERE STATUS='Order approved'";
    private static final String SQL_GET_ALL_CARS_WITH_PAGES = SQL_GET_ALL_CARS + " limit ? offset ?";
    private static final String SQL_COUNT_ALL_CARS = "SELECT COUNT(*) FROM CAR";
    private static final String SQL_COUNT_USER_ORDERS = "SELECT COUNT(*) FROM ORDERS WHERE USER_ID=?";

    private static final Logger log = LogManager.getLogger(DBManager.class);

    private static DBManager instance;

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {

    }

    ////////////////////

    public void insertUser(User user) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            pstmt.setString(k++, user.getEmail());
            pstmt.setString(k++, user.getPassword());
            pstmt.setInt(k++, user.getStatus());
            pstmt.setString(k++, user.getName());
            pstmt.setString(k++, user.getSurname());
            pstmt.setInt(k++, user.getRoleId());
            pstmt.setString(k++, user.getBlockDescr());

            if (pstmt.executeUpdate() > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    user.setId(id);
                }
            }
        } catch (SQLException ex) {
            log.debug("Cannot insert a user: " + user);
            throw new DBException("The user hasn't been registered");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug(e.getMessage());
            }
        }
    }

    public User getUser(String email) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_USER_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return DBUtils.getInstance().extractUser(rs);
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain a user with e-mail: " + email);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return null;
    }

    public void insertBrand(Brand brand) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_BRAND, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            pstmt.setString(k++, brand.getName());

            if (pstmt.executeUpdate() > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    brand.setId(id);
                }
            }
        } catch (SQLException ex) {
            log.debug("Cannot insert car brand: " + brand);
            throw new DBException("Brand hasn't been added");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public List<Brand> getAllBrands() throws DBException {
        List<Brand> brands = new ArrayList<>();
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_BRANDS);
            while (rs.next()) {
                brands.add(DBUtils.getInstance().extractBrand(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain brands list");
            throw new DBException("Brand list is not available at the moment");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return brands;
    }

    public void deleteBrand(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_BRAND);
            int k = 1;
            pstmt.setInt(k++, id);
            pstmt.execute();
        } catch (SQLException ex) {
            log.debug("Cannot delete brand from list");
            throw new DBException("Brand hasn't been deleted");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public void insertCar(Car car) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_CAR, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            pstmt.setInt(k++, car.getBrandId());
            pstmt.setString(k++, car.getModel());
            pstmt.setString(k++, car.getQualityClass());
            pstmt.setDouble(k++, car.getPrice());
            pstmt.setString(k++, car.getCarStatus());

            if (pstmt.executeUpdate() > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    car.setId(id);
                }
            }
        } catch (SQLException ex) {
            log.debug("Cannot insert a car: " + car);
            throw new DBException("The car hasn't been added");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public List<Car> getAllCars(int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CARS_WITH_PAGES);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list");
            throw new DBException("The car list is not available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return cars;
    }

    public int getAllCarsSize() {
        int size = 0;
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_ALL_CARS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return size;
    }

    public void deleteCar(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_CAR);
            int k = 1;
            pstmt.setInt(k++, id);
            pstmt.execute();
        } catch (SQLException ex) {
            log.debug("Cannot delete car");
            throw new DBException("The car hasn't been deleted");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public void updateCar(Car car, int id) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_CAR);
            int k = 1;
            pstmt.setInt(k++, car.getBrandId());
            pstmt.setString(k++, car.getModel());
            pstmt.setString(k++, car.getQualityClass());
            pstmt.setDouble(k++, car.getPrice());
            pstmt.setString(k++, car.getCarStatus());
            pstmt.setInt(k++, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot update car");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public Car getCarById(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_CAR_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return DBUtils.getInstance().extractCar(rs);
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain a car with id: " + id);
            throw new DBException("The car details are not available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return null;
    }

    public List<User> getAllUsers() throws DBException {
        List<User> users = new ArrayList<>();
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_USERS);
            while (rs.next()) {
                users.add(DBUtils.getInstance().extractUser(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain users list");
            throw new DBException("The users list is not available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return users;
    }

    public void updateUserStatus(User user, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_STATUS);
            int k = 1;
            pstmt.setInt(k++, user.getStatus());
            pstmt.setInt(k++, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot update user status");
            throw new DBException("The user status hasn't been updated");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public List<Car> getAllCarsSortedByModel(String date1, String date2, int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_CAR_SORTED_BY_MODEL);
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list sorted by model");
            throw new DBException("The list of cars isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        List<Car> result = new ArrayList<>();
        for (int i = offset; i < (Math.min((offset + limit), cars.size())); i++) {
            result.add(cars.get(i));
        }
        return result;
    }

    public List<Car> getAllCarsSortedByPrice(String date1, String date2, int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_CAR_SORTED_BY_PRICE);
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list sorted by price");
            throw new DBException("The list of cars isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        List<Car> result = new ArrayList<>();
        for (int i = offset; i < (Math.min((offset + limit), cars.size())); i++) {
            result.add(cars.get(i));
        }
        return result;
    }

    public List<Car> getAllCarsWithBrand(int brand_id, String date1, String date2, int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_BRAND);
            pstmt.setInt(1, brand_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by brand");
            throw new DBException("The list of cars isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        List<Car> result = new ArrayList<>();
        for (int i = offset; i < (Math.min((offset + limit), cars.size())); i++) {
            result.add(cars.get(i));
        }
        return result;
    }

    public int countAllCarsWithBrand(int brand_id, String date1, String date2) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_BRAND);
            pstmt.setInt(1, brand_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by brand");
            throw new DBException("The list of cars isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        int size = cars.size();
        return size;
    }

    public List<Car> getAllCarsWithClass(String quality_class, String date1, String date2, int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_CLASS);
            pstmt.setString(1, quality_class);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by class");
            throw new DBException("The list of cars isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        List<Car> result = new ArrayList<>();
        for (int i = offset; i < (Math.min((offset + limit), cars.size())); i++) {
            result.add(cars.get(i));
        }
        return result;
    }

    public int countAllCarsWithClass(String quality_class, String date1, String date2) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_CLASS);
            pstmt.setString(1, quality_class);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by class");
            throw new DBException("The list of cars isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        int size = cars.size();
        return size;
    }

    public List<String> getAllCarsQualityClasses() throws DBException {
        List<String> quality_classes = new ArrayList<>();
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_CARS_QUALITY_CLASSES);
            while (rs.next()) {
                quality_classes.add(rs.getString("quality_class"));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain cars' quality classes list");
            throw new DBException("The quality classes list is not available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return quality_classes;
    }

    public Order getOrderById(int orderId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ORDER_BY_ID);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return DBUtils.getInstance().extractOrder(rs);
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain an order with id: " + orderId);
            throw new DBException("Order details are not available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return null;
    }

    public void insertOrderReceipt(OrderReceipt orderReceipt) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_ORDER_RECEIPT);
            int k = 1;
            pstmt.setInt(k++, orderReceipt.getCost());
            pstmt.setInt(k++, orderReceipt.getPaymentStatus());
            pstmt.setDate(k++, orderReceipt.getPaymentDate());
            pstmt.setInt(k++, orderReceipt.getOrder_id());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot insert an order receipt: " + orderReceipt);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public void updateOrderReceiptStatusAsPaidToday(Order order) throws DBException {
        Date today = new Date(System.currentTimeMillis());
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_ORDER_RECEIPT_PAYMENT_STATUS);
            int k = 1;
            pstmt.setInt(k++, 1);
            pstmt.setDate(k++, today);
            pstmt.setInt(k++, order.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change order receipt status to 'Paid'");
            throw new DBException("The order payment details haven't been updated. Please, contact the manager.");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public List<Order> getAllOrdersByUserId(int userId, int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_ORDERS_BY_USERID);
            int k = 1;
            pstmt.setInt(k++, userId);
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(DBUtils.getInstance().extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain orders list for user with id: " + userId);
            throw new DBException("The list of orders isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return orders;
    }

    public int getUserOrdersSize(int userId) {
        int size = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_COUNT_USER_ORDERS);
            int k = 1;
            pstmt.setInt(k++, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count user's orders");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return size;
    }

    public List<Order> getAllReceivedOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_RECEIVED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(DBUtils.getInstance().extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain received orders list");
            throw new DBException("The list of received orders isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return orders;
    }

    public int getReceivedOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_RECEIVED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return size;
    }

    public void approveOrderById(int order_id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_APPROVE_ORDER);
            int k = 1;
            pstmt.setInt(k++, order_id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change status to 'Order approved' for order with id: " + order_id);
            throw new DBException("The order status hasn't been updated");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public void cancelOrderById(String comment, int order_id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_CANCEL_ORDER);
            int k = 1;
            pstmt.setString(k++, comment);
            pstmt.setInt(k++, order_id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change status to 'Order cancelled' for order with id: " + order_id);
            throw new DBException("The order status hasn't been updated");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public List<Order> getAllApprovedOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_APPROVED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(DBUtils.getInstance().extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain approved orders list");
            throw new DBException("The list of active orders isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return orders;
    }

    public int getApprovedOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_APPROVED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return size;
    }

    public List<Car> getWorkableCarsForDates(String rentBeg, String rentFin, int offset, int limit) throws DBException {
        List<Car> notWorkableCars = getNotWorkableCarsForDates(rentBeg, rentFin);
        List<Car> cars = getAllCars(0, Integer.MAX_VALUE);
        cars.removeAll(notWorkableCars);
        List<Car> result = new ArrayList<>();
        for (int i = offset; i < (Math.min((offset + limit), cars.size())); i++) {
            result.add(cars.get(i));
        }
        return result;
    }

    public int countWorkableCarsForDates(String rentBeg, String rentFin) throws DBException {
        List<Car> notWorkableCars = getNotWorkableCarsForDates(rentBeg, rentFin);
        List<Car> cars = getAllCars(0, Integer.MAX_VALUE);
        cars.removeAll(notWorkableCars);
        int size = cars.size();
        return size;
    }

    public List<Car> getNotWorkableCarsForDates(String rentBeg, String rentFin) {
        List<Car> notWorkableCars = new ArrayList<>();
        Date rentStart = null;
        Date rentEnd = null;
        try {
            java.util.Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(rentBeg);
            rentStart = new Date(date1.getTime());

            java.util.Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(rentFin);
            rentEnd = new Date(date2.getTime());
        } catch (ParseException e) {
            log.debug("Cannot parse dates: " + e.getMessage());
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_NOT_WORKABLE_CARS);
            int k = 1;
            pstmt.setDate(k++, rentStart);
            pstmt.setDate(k++, rentEnd);
            pstmt.setDate(k++, rentStart);
            pstmt.setDate(k++, rentEnd);
            pstmt.setDate(k++, rentStart);
            pstmt.setDate(k++, rentEnd);
            pstmt.setDate(k++, rentStart);
            pstmt.setDate(k++, rentEnd);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                notWorkableCars.add(DBUtils.getInstance().extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain list of not workable cars for dates");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return notWorkableCars;
    }

    public void updateOrderStatusAsClosed(int orderId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_SET_ORDER_AS_CLOSED);
            int k = 1;
            pstmt.setInt(k++, orderId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change order status to 'Order closed'");
            throw new DBException("Order status hasn't been updated");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }

    }

    public List<Order> getAllClosedOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CLOSED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(DBUtils.getInstance().extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain closed orders list");
            throw new DBException("The list of closed order isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return orders;
    }

    public int getClosedOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_CLOSED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return size;
    }

    public void insertDamageReceiptAndUpdateCarStatusAsDamaged(DamageReceipt damageReceipt, int orderId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        int carId;
        try {
            con = DBUtils.getInstance().getConnection();
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(SQL_INSERT_DAMAGE_RECEIPT);
            int k = 1;
            pstmt1.setInt(k++, damageReceipt.getDamageCost());
            pstmt1.setInt(k++, damageReceipt.getDamagePaymentStatus());
            pstmt1.setDate(k++, damageReceipt.getDamagePaymentDate());
            pstmt1.setInt(k++, damageReceipt.getOrder_id());
            pstmt1.executeUpdate();

            Order order = getOrderById(orderId);
            carId = order.getCarId();

            pstmt2 = con.prepareStatement(SQL_UPDATE_CAR_STATUS_AS_DAMAGED);
            int p = 1;
            pstmt2.setInt(p++, carId);
            pstmt2.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            log.debug("Cannot insert damage receipt and update car status to 'Damaged'");
            try {
                con.rollback();
            } catch (SQLException exception) {
                log.debug("Cannot rollback connection with DB");
            }
            throw new DBException("The damage receipt hasn't been created, please, try again");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public void insertOrder(Order order) throws DBException, BusyCarException {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs2 = null;
        try {
            con = DBUtils.getInstance().getConnection();
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(SQL_CHECK_IF_ANY_ORDERS_FOR_GIVEN_DATES);
            int k = 1;
            pstmt1.setInt(k++, order.getCarId());
            pstmt1.setDate(k++, order.getRentStart());
            pstmt1.setDate(k++, order.getRentEnd());
            pstmt1.setDate(k++, order.getRentStart());
            pstmt1.setDate(k++, order.getRentEnd());
            rs1 = pstmt1.executeQuery();
            if (rs1.next()) {
                con.rollback();
                con.close();
                throw new BusyCarException("The car is busy for these dates, please, try another dates");
            }
            pstmt2 = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            int p = 1;
            pstmt2.setDate(p++, order.getRentStart());
            pstmt2.setDate(p++, order.getRentEnd());
            pstmt2.setString(p++, order.getPassport());
            pstmt2.setInt(p++, order.getDriver());
            pstmt2.setInt(p++, order.getUserId());
            pstmt2.setInt(p++, order.getCarId());
            pstmt2.setString(p++, order.getCancelComments());
            pstmt2.setString(p++, order.getStatus());
            if (pstmt2.executeUpdate() > 0) {
                rs2 = pstmt2.getGeneratedKeys();
                if (rs2.next()) {
                    int id = rs2.getInt(1);
                    order.setId(id);
                }
                con.commit();
            } else {
                con.rollback();
                throw new DBException("The order hasn't been added, please, try again");
            }
        } catch (SQLException ex) {
            log.debug("Cannot insert an order: " + order);
            try {
                con.rollback();
            } catch (SQLException e) {
                log.debug("Cannot rollback connection with DB");
            }
            throw new DBException("The order hasn't been added, please, try again");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public void updateDamageReceiptStatusAsPaidToday(Order order) throws DBException {
        Date today = new Date(System.currentTimeMillis());
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_DAMAGE_RECEIPT_PAYMENT_STATUS);
            int k = 1;
            pstmt.setInt(k++, 1);
            pstmt.setDate(k++, today);
            pstmt.setInt(k++, order.getId());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change damage receipt status to 'Paid' and insert payment date");
            throw new DBException("The damage payment details haven't been updated. Please, contact the manager.");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
    }

    public List<Order> getAllCancelledOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DBUtils.getInstance().getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CANCELLED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(DBUtils.getInstance().extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain cancelled orders list");
            throw new DBException("The list of cancelled orders isn't available");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return orders;
    }

    public int getCancelledOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = DBUtils.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_CANCELLED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.debug("Cannot close connection");
            }
        }
        return size;
    }
}
