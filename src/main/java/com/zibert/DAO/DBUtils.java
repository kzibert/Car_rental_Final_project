package com.zibert.DAO;

import com.zibert.DAO.entity.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtils {

    private static DBUtils instance;

    public static synchronized DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    private DataSource ds;

    private DBUtils() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("jdbc/CarRental");
        } catch (NamingException ex) {
            throw new IllegalStateException("Cannot obtain a data source", ex);
        }
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            throw new IllegalStateException("Cannot obtain a connection", ex);
        }
        return con;
    }

    public User extractUser(ResultSet rs) throws SQLException {
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

    public Brand extractBrand(ResultSet rs) throws SQLException {
        Brand brand = new Brand();
        brand.setId(rs.getInt("id"));
        brand.setName(rs.getString("name"));
        return brand;
    }

    public Car extractCar(ResultSet rs) throws SQLException {
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

    public Order extractOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setRentStart(rs.getDate("rent_start"));
        order.setRentEnd(rs.getDate("rent_end"));
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
        order.setBrand(brand);
        OrderReceipt orderReceipt = new OrderReceipt();
        orderReceipt.setCost(rs.getInt("cost"));
        orderReceipt.setPaymentStatus(rs.getInt("payment_status"));
        orderReceipt.setPaymentDate(rs.getDate("payment_date"));
        order.setOrderReceipt(orderReceipt);
        DamageReceipt damageReceipt = new DamageReceipt();
        damageReceipt.setDamageCost(rs.getInt("damage_cost"));
        damageReceipt.setDamagePaymentStatus(rs.getInt("damage_payment_status"));
        damageReceipt.setDamagePaymentDate(rs.getDate("damage_payment_date"));
        order.setDamageReceipt(damageReceipt);
        return order;
    }
}
