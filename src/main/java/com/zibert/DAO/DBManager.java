package com.zibert.DAO;

import com.zibert.DAO.entity.*;
import com.zibert.DAO.exceptions.BusyCarException;
import com.zibert.DAO.exceptions.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.JsonUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    static final String SQL_GET_ALL_CARS = "SELECT car.id, car.model, car.quality_class, car.price, car.car_status, car.brand_id, brand.name AS 'brand_name' FROM car INNER JOIN brand ON car.brand_id = brand.id";
    static final String SQL_GET_ALL_ORDERS = "SELECT orders.id, orders.rent_start, orders.rent_end, orders.passport, orders.driver, orders.user_id, orders.car_id, orders.cancel_comments, orders.status, car.model AS 'car_model', car.car_status AS 'car_status', car.brand_id AS 'brand_id', brand.name AS 'brand_name', order_receipt.payment_status AS 'payment_status', order_receipt.payment_date AS 'payment_date', order_receipt.cost AS 'cost', damage_receipt.payment_status AS 'damage_payment_status', damage_receipt.payment_date AS 'damage_payment_date', damage_receipt.cost AS 'damage_cost' FROM orders " +
            "INNER JOIN car ON orders.car_id = car.id " +
            "INNER JOIN brand ON brand_id=brand.id " +
            "LEFT JOIN damage_receipt ON orders.id=damage_receipt.order_id " +
            "LEFT JOIN order_receipt ON orders.id=order_receipt.order_id";
    static final String SQL_INSERT_USER = "INSERT INTO user VALUES (default, ?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_GET_USER_BY_EMAIL = "SELECT * FROM user WHERE email=?";
    static final String SQL_INSERT_BRAND = "INSERT INTO brand VALUES (default, ?)";
    static final String SQL_GET_ALL_BRANDS = "SELECT * FROM brand";
    static final String SQL_DELETE_BRAND = "DELETE FROM brand WHERE id = ?";
    static final String SQL_INSERT_CAR = "INSERT INTO car VALUES (default, ?, ?, ?, ?, ?)";
    static final String SQL_DELETE_CAR = "DELETE FROM car WHERE id = ?";
    static final String SQL_UPDATE_CAR = "UPDATE car SET brand_id=?, model=?, quality_class=?, price=?, car_status=? WHERE ID=?";
    static final String SQL_GET_CAR_BY_ID = SQL_GET_ALL_CARS + " WHERE car.id=?";
    static final String SQL_GET_ALL_USERS = "SELECT * FROM user";
    static final String SQL_UPDATE_USER_STATUS = "UPDATE user SET status=? WHERE ID=?";
    static final String SQL_GET_ALL_CAR_SORTED_BY_MODEL = SQL_GET_ALL_CARS + " ORDER BY model";
    static final String SQL_GET_ALL_CAR_SORTED_BY_PRICE = SQL_GET_ALL_CARS + " ORDER BY price";
    static final String SQL_GET_ALL_CAR_WITH_BRAND = SQL_GET_ALL_CARS + " WHERE brand_id=?";
    static final String SQL_GET_ALL_CAR_WITH_CLASS = SQL_GET_ALL_CARS + " WHERE quality_class=?";
    static final String SQL_GET_ALL_CARS_QUALITY_CLASSES = "SELECT DISTINCT car.quality_class FROM car ORDER BY quality_class";
    static final String SQL_INSERT_ORDER = "INSERT INTO orders VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
    static final String SQL_GET_ORDER_BY_ID = SQL_GET_ALL_ORDERS + " WHERE orders.id = ?";
    static final String SQL_INSERT_ORDER_RECEIPT = "INSERT INTO order_receipt VALUES (?, ?, ?, ?)";
    static final String SQL_UPDATE_ORDER_RECEIPT_PAYMENT_STATUS = "UPDATE order_receipt SET payment_status=?, payment_date=? WHERE order_id=?";
    static final String SQL_GET_ALL_ORDERS_BY_USERID = SQL_GET_ALL_ORDERS + " WHERE user_id=? ORDER BY id DESC limit ? offset ?";
    static final String SQL_GET_ALL_RECEIVED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE status='Order received' ORDER BY id DESC limit ? offset ?";
    static final String SQL_APPROVE_ORDER = "UPDATE orders SET status='Order approved' WHERE id=?";
    static final String SQL_CANCEL_ORDER = "UPDATE orders SET status='Order cancelled', cancel_comments=? WHERE id=?";
    static final String SQL_GET_ALL_APPROVED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE status='Order approved' ORDER BY id limit ? offset ?";
    static final String SQL_GET_ALL_NOT_WORKABLE_CARS = SQL_GET_ALL_CARS +
            " LEFT JOIN orders ON car.id=orders.car_id " +
            "WHERE (status='Order approved' and ? BETWEEN rent_start AND rent_end) " +
            "OR (status='Order approved' and ? BETWEEN rent_start AND rent_end) " +
            "OR (status='Order approved' and  rent_start > ? and rent_end < ?) " +
            "OR (status='Order received' and ? BETWEEN rent_start AND rent_end) " +
            "OR (status='Order received' and ? BETWEEN rent_start AND rent_end) " +
            "OR (status='Order received' and  rent_start > ? and rent_end < ?) " +
            "OR (car_status='Damaged')";
    static final String SQL_INSERT_DAMAGE_RECEIPT = "INSERT INTO damage_receipt VALUES (?, ?, ?, ?)";
    static final String SQL_UPDATE_CAR_STATUS_AS_DAMAGED = "UPDATE car SET car_status='Damaged' WHERE id=?";
    static final String SQL_SET_ORDER_AS_CLOSED = "UPDATE orders SET status='Order closed' WHERE id=?";
    static final String SQL_GET_ALL_CLOSED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE status='Order closed' ORDER BY id DESC limit ? offset ?";
    static final String SQL_CHECK_IF_ANY_ORDERS_FOR_GIVEN_DATES = SQL_GET_ALL_ORDERS +
            " WHERE (car_id=? AND (status='Order received' OR status='Order approved'))" +
            " AND (? BETWEEN rent_start AND rent_end OR ? BETWEEN rent_start AND rent_end OR (rent_start > ? and rent_end < ?))";
    static final String SQL_UPDATE_DAMAGE_RECEIPT_PAYMENT_STATUS = "UPDATE damage_receipt SET payment_status=?, payment_date=? WHERE order_id=?";
    static final String SQL_GET_ALL_CANCELLED_ORDERS = SQL_GET_ALL_ORDERS + " WHERE status='Order cancelled' ORDER BY id DESC limit ? offset ?";
    static final String SQL_COUNT_CLOSED_ORDERS = "SELECT COUNT(*) FROM orders WHERE status='Order closed'";
    static final String SQL_COUNT_CANCELLED_ORDERS = "SELECT COUNT(*) FROM orders WHERE status='Order cancelled'";
    static final String SQL_COUNT_RECEIVED_ORDERS = "SELECT COUNT(*) FROM orders WHERE status='Order received'";
    static final String SQL_COUNT_APPROVED_ORDERS = "SELECT COUNT(*) FROM orders WHERE status='Order approved'";
    static final String SQL_GET_ALL_CARS_WITH_PAGES = SQL_GET_ALL_CARS + " limit ? offset ?";
    static final String SQL_COUNT_ALL_CARS = "SELECT COUNT(*) FROM car";
    static final String SQL_COUNT_USER_ORDERS = "SELECT COUNT(*) FROM orders WHERE user_id=?";

    private static final Logger log = LogManager.getLogger(DBManager.class);

    private static DBManager instance;
    private DataSource ds;

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/CarRental");
        } catch (NamingException ex) {
            throw new IllegalStateException("Cannot obtain a data source", ex);
        }
    }

    ////////////////////

    public Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot obtain a connection", ex);
        }
        return con;
    }

    public void closeConnection(Connection con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            log.debug("Cannot close connection");
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setStatus(rs.getInt("status"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setRoleId(rs.getInt("roles_id"));
        user.setBlockDescr(rs.getString("block_comments"));
        return user;
    }

    private Brand extractBrand(ResultSet rs) throws SQLException {
        Brand brand = new Brand();
        brand.setId(rs.getInt("id"));
        brand.setName(rs.getString("name"));
        return brand;
    }

    private Car extractCar(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setId(rs.getInt("id"));
        car.setBrandId(rs.getInt("brand_id"));
        car.setModel(rs.getString("model"));
        car.setQualityClass(rs.getString("quality_class"));
        car.setPrice(rs.getInt("price"));
        car.setCarStatus(rs.getString("car_status"));
        Brand brand = new Brand();
        brand.setId(rs.getInt("brand_id"));
        brand.setName(rs.getString("brand_name"));
        car.setBrand(brand);
        return car;
    }

    private Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        java.util.Date rent_start = null;
        if (rs.getDate("rent_start") != null) {
            rent_start = new java.util.Date(rs.getDate("rent_start").getTime());
        }
        order.setRentStart(rent_start);
        java.util.Date rent_end = null;
        if (rs.getDate("rent_end") != null) {
            rent_end = new java.util.Date(rs.getDate("rent_end").getTime());
        }
        order.setRentEnd(rent_end);
        order.setPassport(rs.getString("passport"));
        order.setDriver(rs.getInt("driver"));
        order.setUserId(rs.getInt("user_id"));
        order.setCarId(rs.getInt("car_id"));
        order.setCancelComments(rs.getString("cancel_comments"));
        order.setStatus(rs.getString("status"));
        Car car = new Car();
        car.setId(rs.getInt("car_id"));
        car.setModel(rs.getString("car_model"));
        car.setCarStatus(rs.getString("car_status"));
        order.setCar(car);
        Brand brand = new Brand();
        brand.setId(rs.getInt("brand_id"));
        brand.setName(rs.getString("brand_name"));
        car.setBrand(brand);
        OrderReceipt orderReceipt = new OrderReceipt();
        orderReceipt.setCost(rs.getInt("cost"));
        orderReceipt.setPaymentStatus(rs.getInt("payment_status"));
        java.util.Date paymentDate = null;
        if (rs.getDate("payment_date") != null) {
            paymentDate = new java.util.Date((rs.getDate("payment_date").getTime()));
        }
        orderReceipt.setPaymentDate(paymentDate);
        order.setOrderReceipt(orderReceipt);
        DamageReceipt damageReceipt = new DamageReceipt();
        damageReceipt.setDamageCost(rs.getInt("damage_cost"));
        damageReceipt.setDamagePaymentStatus(rs.getInt("damage_payment_status"));
        java.util.Date damagePaymentDate = null;
        if (rs.getDate("damage_payment_date") != null) {
            damagePaymentDate = new java.util.Date((rs.getDate("damage_payment_date").getTime()));
        }
        damageReceipt.setDamagePaymentDate(damagePaymentDate);
        order.setDamageReceipt(damageReceipt);
        return order;
    }

    ////////////////////

    public void insertUser(User user) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
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
            closeConnection(con);
        }
    }

    public User getUser(String email) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_USER_BY_EMAIL);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain a user with e-mail: " + email);
        } finally {
            closeConnection(con);
        }
        return null;
    }

    public void insertBrand(Brand brand) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
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
            closeConnection(con);
        }
    }

    public List<Brand> getAllBrands() throws DBException {
        List<Brand> brands = new ArrayList<>();
        Connection con = null;
        try {
            con = getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_BRANDS);
            while (rs.next()) {
                brands.add(extractBrand(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain brands list");
            throw new DBException("Brand list is not available at the moment");
        } finally {
            closeConnection(con);
        }
        return brands;
    }

    public void deleteBrand(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_BRAND);
            int k = 1;
            pstmt.setInt(k++, id);
            pstmt.execute();
        } catch (SQLException ex) {
            log.debug("Cannot delete brand from list");
            throw new DBException("Brand hasn't been deleted");
        } finally {
            closeConnection(con);
        }
    }

    public void insertCar(Car car) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
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
            closeConnection(con);
        }
    }

    public List<Car> getAllCars(int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CARS_WITH_PAGES);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list");
            throw new DBException("The car list is not available");
        } finally {
            closeConnection(con);
        }
        return cars;
    }

    public int getAllCarsSize() {
        int size = 0;
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_ALL_CARS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            closeConnection(con);
        }
        return size;
    }

    public void deleteCar(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_DELETE_CAR);
            int k = 1;
            pstmt.setInt(k++, id);
            pstmt.execute();
        } catch (SQLException ex) {
            log.debug("Cannot delete car");
            throw new DBException("The car hasn't been deleted");
        } finally {
            closeConnection(con);
        }
    }

    public void updateCar(Car car, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
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
            throw new DBException("The car status hasn't been updated");
        } finally {
            closeConnection(con);
        }
    }

    public Car getCarById(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_CAR_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractCar(rs);
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain a car with id: " + id);
            throw new DBException("The car details are not available");
        } finally {
            closeConnection(con);
        }
        return null;
    }

    public List<User> getAllUsers() throws DBException {
        List<User> users = new ArrayList<>();
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_GET_ALL_USERS);
            while (rs.next()) {
                users.add(extractUser(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain users list");
            throw new DBException("The users list is not available");
        } finally {
            closeConnection(con);
        }
        return users;
    }

    public void updateUserStatus(User user, int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_UPDATE_USER_STATUS);
            int k = 1;
            pstmt.setInt(k++, user.getStatus());
            pstmt.setInt(k++, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot update user status");
            throw new DBException("The user status hasn't been updated");
        } finally {
            closeConnection(con);
        }
    }

    public List<Car> getAllCarsSortedByModel(String date1, String date2, int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        try {
            con = getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_CAR_SORTED_BY_MODEL);
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list sorted by model");
            throw new DBException("The list of cars isn't available");
        } finally {
            closeConnection(con);
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
            con = getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_CAR_SORTED_BY_PRICE);
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list sorted by price");
            throw new DBException("The list of cars isn't available");
        } finally {
            closeConnection(con);
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
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_BRAND);
            pstmt.setInt(1, brand_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by brand");
            throw new DBException("The list of cars isn't available");
        } finally {
            closeConnection(con);
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
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_BRAND);
            pstmt.setInt(1, brand_id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by brand");
            throw new DBException("The list of cars isn't available");
        } finally {
            closeConnection(con);
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        return cars.size();
    }

    public List<Car> getAllCarsWithClass(String quality_class, String date1, String date2, int offset, int limit) throws DBException {
        List<Car> cars = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_CLASS);
            pstmt.setString(1, quality_class);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by class");
            throw new DBException("The list of cars isn't available");
        } finally {
            closeConnection(con);
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
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CAR_WITH_CLASS);
            pstmt.setString(1, quality_class);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain car list filtered by class");
            throw new DBException("The list of cars isn't available");
        } finally {
            closeConnection(con);
        }
        cars.removeAll(getNotWorkableCarsForDates(date1, date2));
        return cars.size();
    }

    public List<String> getAllCarsQualityClasses() throws DBException {
        List<String> quality_classes = new ArrayList<>();
        Connection con = null;
        try {
            con = getConnection();
            ResultSet rs = con.createStatement().executeQuery(SQL_GET_ALL_CARS_QUALITY_CLASSES);
            while (rs.next()) {
                quality_classes.add(rs.getString("quality_class"));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain cars' quality classes list");
            throw new DBException("The quality classes list is not available");
        } finally {
            closeConnection(con);
        }
        return quality_classes;
    }

    public Order getOrderById(int orderId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ORDER_BY_ID);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractOrder(rs);
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain an order with id: " + orderId);
            throw new DBException("Order details are not available");
        } finally {
            closeConnection(con);
        }
        return null;
    }

    public void insertOrderReceipt(OrderReceipt orderReceipt) {
        Date paymentDate = null;
        Connection con = null;
        PreparedStatement pstmt = null;
        if (orderReceipt.getPaymentDate() != null) {
            paymentDate = new Date(orderReceipt.getPaymentDate().getTime());
        }
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_INSERT_ORDER_RECEIPT);
            int k = 1;
            pstmt.setInt(k++, orderReceipt.getCost());
            pstmt.setInt(k++, orderReceipt.getPaymentStatus());
            pstmt.setDate(k++, paymentDate);
            pstmt.setInt(k++, orderReceipt.getOrder_id());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot insert an order receipt: " + orderReceipt);
        } finally {
            closeConnection(con);
        }
    }

    public void updateOrderReceiptStatusAsPaidToday(Order order) throws DBException {
        Date today = new Date(System.currentTimeMillis());
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
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
            closeConnection(con);
        }
    }

    public List<Order> getAllOrdersByUserId(int userId, int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_ORDERS_BY_USERID);
            int k = 1;
            pstmt.setInt(k++, userId);
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain orders list for user with id: " + userId);
            throw new DBException("The list of orders isn't available");
        } finally {
            closeConnection(con);
        }
        return orders;
    }

    public int getUserOrdersSize(int userId) {
        int size = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
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
            closeConnection(con);
        }
        return size;
    }

    public List<Order> getAllReceivedOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_RECEIVED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain received orders list");
            throw new DBException("The list of received orders isn't available");
        } finally {
            closeConnection(con);
        }
        return orders;
    }

    public int getReceivedOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_RECEIVED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            closeConnection(con);
        }
        return size;
    }

    public void approveOrderById(int order_id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_APPROVE_ORDER);
            int k = 1;
            pstmt.setInt(k++, order_id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change status to 'Order approved' for order with id: " + order_id);
            throw new DBException("The order status hasn't been updated");
        } finally {
            closeConnection(con);
        }
    }

    public void cancelOrderById(String comment, int order_id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_CANCEL_ORDER);
            int k = 1;
            pstmt.setString(k++, comment);
            pstmt.setInt(k++, order_id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change status to 'Order cancelled' for order with id: " + order_id);
            throw new DBException("The order status hasn't been updated");
        } finally {
            closeConnection(con);
        }
    }

    public List<Order> getAllApprovedOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_APPROVED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain approved orders list");
            throw new DBException("The list of active orders isn't available");
        } finally {
            closeConnection(con);
        }
        return orders;
    }

    public int getApprovedOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_APPROVED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            closeConnection(con);
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
        return cars.size();
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
            con = getConnection();
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
                notWorkableCars.add(extractCar(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain list of not workable cars for dates");
        } finally {
            closeConnection(con);
        }
        return notWorkableCars;
    }

    public void updateOrderStatusAsClosed(int orderId) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_SET_ORDER_AS_CLOSED);
            int k = 1;
            pstmt.setInt(k++, orderId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            log.debug("Cannot change order status to 'Order closed'");
            throw new DBException("Order status hasn't been updated");
        } finally {
            closeConnection(con);
        }

    }

    public List<Order> getAllClosedOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CLOSED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain closed orders list");
            throw new DBException("The list of closed order isn't available");
        } finally {
            closeConnection(con);
        }
        return orders;
    }

    public int getClosedOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_CLOSED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            closeConnection(con);
        }
        return size;
    }

    public void insertDamageReceiptAndUpdateCarStatusAsDamaged(DamageReceipt damageReceipt, int orderId) throws DBException {
        Date paymentDate = null;
        Connection con = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        int carId;
        if (damageReceipt.getDamagePaymentDate() != null) {
            paymentDate = new Date(damageReceipt.getDamagePaymentDate().getTime());
        }
        try {
            con = getConnection();
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(SQL_INSERT_DAMAGE_RECEIPT);
            int k = 1;
            pstmt1.setInt(k++, damageReceipt.getDamageCost());
            pstmt1.setInt(k++, damageReceipt.getDamagePaymentStatus());
            pstmt1.setDate(k++, paymentDate);
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
            closeConnection(con);
        }
    }

    public void insertOrder(Order order) throws DBException, BusyCarException {
        Connection con = null;
        PreparedStatement pstmt1 = null;
        ResultSet rs1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs2 = null;
        Date rentStart = null;
        Date rentEnd = null;
        if (order.getRentStart() != null) {
            rentStart = new Date(order.getRentStart().getTime());
        }
        if (order.getRentEnd() != null) {
            rentEnd = new Date(order.getRentEnd().getTime());
        }
        try {
            con = getConnection();
            con.setAutoCommit(false);
            pstmt1 = con.prepareStatement(SQL_CHECK_IF_ANY_ORDERS_FOR_GIVEN_DATES);
            int k = 1;
            pstmt1.setInt(k++, order.getCarId());
            pstmt1.setDate(k++, rentStart);
            pstmt1.setDate(k++, rentEnd);
            pstmt1.setDate(k++, rentStart);
            pstmt1.setDate(k++, rentEnd);
            rs1 = pstmt1.executeQuery();
            if (rs1.next()) {
                con.rollback();
                con.close();
                throw new BusyCarException("The car is busy for these dates, please, try another dates");
            }
            pstmt2 = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            int p = 1;
            pstmt2.setDate(p++, rentStart);
            pstmt2.setDate(p++, rentEnd);
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
            closeConnection(con);
        }
    }

    public void updateDamageReceiptStatusAsPaidToday(Order order) throws DBException {
        Date today = new Date(System.currentTimeMillis());
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
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
            closeConnection(con);
        }
    }

    public List<Order> getAllCancelledOrders(int offset, int limit) throws DBException {
        List<Order> orders = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(SQL_GET_ALL_CANCELLED_ORDERS);
            int k = 1;
            pstmt.setInt(k++, limit);
            pstmt.setInt(k++, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(extractOrder(rs));
            }
        } catch (SQLException ex) {
            log.debug("Cannot obtain cancelled orders list");
            throw new DBException("The list of cancelled orders isn't available");
        } finally {
            closeConnection(con);
        }
        return orders;
    }

    public int getCancelledOrdersSize() {
        int size = 0;
        Connection con = null;
        try {
            con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_COUNT_CANCELLED_ORDERS);
            if (rs.next()) {
                size = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.debug("Cannot count closed orders");
        } finally {
            closeConnection(con);
        }
        return size;
    }
}
