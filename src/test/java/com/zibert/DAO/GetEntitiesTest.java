package com.zibert.DAO;

import com.zibert.DAO.entity.*;
import com.zibert.DAO.exceptions.DBException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class GetEntitiesTest {

    DBManager dbManager;
    ResultSet rs;
    Statement stmt;
    Connection con;
    PreparedStatement pstmt;

    User user1 = new User();
    User user2 = new User();
    Brand brand1 = new Brand();
    Brand brand2 = new Brand();
    Car car1 = new Car();
    Car car2 = new Car();
    OrderReceipt orderReceipt1 = new OrderReceipt();
    DamageReceipt damageReceipt1 = new DamageReceipt();
    Order order1 = new Order();

    @Before
    public void setUp() throws ParseException {
        String path = "C:\\WEB-INF\\log4j2.log";
        System.setProperty("logFile", path);

        user1.setId(1);
        user1.setEmail("user1.email@gmail.com");
        user1.setPassword("user1pass");
        user1.setStatus(0);
        user1.setName("John");
        user1.setSurname("Brown");
        user1.setRoleId(1);
        user1.setBlockDescr(null);
        user2.setId(4);
        user2.setEmail("user2.email@gmail.com");
        user2.setPassword("user2pass");
        user2.setStatus(1);
        user2.setName("Fred");
        user2.setSurname("Silver");
        user2.setRoleId(3);
        user2.setBlockDescr("Bad behavior");

        brand1.setId(23);
        brand1.setName("Porsche");
        brand2.setId(65);
        brand2.setName("Ford");

        car1.setId(4);
        car1.setBrandId(23);
        car1.setModel("Cayenne");
        car1.setQualityClass("C");
        car1.setPrice(750);
        car1.setCarStatus("Damaged");
        car1.setBrand(brand1);
        car2.setId(7);
        car2.setBrandId(65);
        car2.setModel("Focus");
        car2.setQualityClass("B");
        car2.setPrice(320);
        car2.setCarStatus("Available");
        car2.setBrand(brand2);

        orderReceipt1.setOrder_id(5);
        orderReceipt1.setCost(1500);
        orderReceipt1.setPaymentStatus(0);
        orderReceipt1.setPaymentDate(null);

        damageReceipt1.setOrder_id(2);
        damageReceipt1.setDamageCost(450);
        damageReceipt1.setDamagePaymentStatus(0);
        damageReceipt1.setDamagePaymentDate(null);

        order1.setId(5);
        order1.setRentStart(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-01"));
        order1.setRentEnd((new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-05")));
        order1.setPassport("KF456123");
        order1.setDriver(0);
        order1.setUserId(4);
        order1.setCarId(7);
        order1.setCancelComments(null);
        order1.setStatus("Order closed");
        order1.setCar(car2);
        order1.setOrderReceipt(orderReceipt1);
        order1.setDamageReceipt(damageReceipt1);
    }

    @Test
    public void getAllUsersTest() throws SQLException, DBException {

        rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("id"))
                .thenReturn(1)
                .thenReturn(4);

        when(rs.getString("email"))
                .thenReturn("user1.email@gmail.com")
                .thenReturn("user2.email@gmail.com");

        when(rs.getString("password"))
                .thenReturn("user1pass")
                .thenReturn("user2pass");

        when(rs.getInt("status"))
                .thenReturn(0)
                .thenReturn(1);

        when(rs.getString("name"))
                .thenReturn("John")
                .thenReturn("Fred");

        when(rs.getString("surname"))
                .thenReturn("Brown")
                .thenReturn("Silver");

        when(rs.getInt("roles_id"))
                .thenReturn(1)
                .thenReturn(3);

        when(rs.getString("block_comments"))
                .thenReturn(null)
                .thenReturn("Bad behavior");

        ///////////////

        stmt = mock(Statement.class);
        when(stmt.executeQuery(DBManager.SQL_GET_ALL_USERS))
                .thenReturn(rs);

        /////////////

        con = mock(Connection.class);
        when(con.createStatement()).thenReturn(stmt);

        ///////////////

        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(con);

        when(dbManager.getAllUsers()).thenCallRealMethod();

        List<User> actual = dbManager.getAllUsers();
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllBrandsTest() throws SQLException, DBException {

        rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("id"))
                .thenReturn(23)
                .thenReturn(65);

        when(rs.getString("name"))
                .thenReturn("Porsche")
                .thenReturn("Ford");

        ///////////////

        stmt = mock(Statement.class);
        when(stmt.executeQuery(DBManager.SQL_GET_ALL_BRANDS))
                .thenReturn(rs);

        /////////////

        con = mock(Connection.class);
        when(con.createStatement()).thenReturn(stmt);

        ///////////////

        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(con);

        when(dbManager.getAllBrands()).thenCallRealMethod();

        List<Brand> actual = dbManager.getAllBrands();
        List<Brand> expected = new ArrayList<>();
        expected.add(brand1);
        expected.add(brand2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getAllCarsTest() throws SQLException, DBException {

        rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(rs.getInt("id"))
                .thenReturn(4)
                .thenReturn(7);

        when(rs.getInt("brand_id"))
                .thenReturn(23)
                .thenReturn(23)
                .thenReturn(65)
                .thenReturn(65);

        when(rs.getString("brand_name"))
                .thenReturn("Porsche")
                .thenReturn("Ford");

        when(rs.getString("model"))
                .thenReturn("Cayenne")
                .thenReturn("Focus");

        when(rs.getString("quality_class"))
                .thenReturn("C")
                .thenReturn("B");

        when(rs.getInt("price"))
                .thenReturn(750)
                .thenReturn(320);

        when(rs.getString("car_status"))
                .thenReturn("Damaged")
                .thenReturn("Available");

        ///////////////

        stmt = mock(Statement.class);
        when(stmt.executeQuery(DBManager.SQL_GET_ALL_CARS))
                .thenReturn(rs);

        /////////////

        pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        ///////////////

        con = mock(Connection.class);
        when(con.createStatement()).thenReturn(stmt);
        when(con.prepareStatement(DBManager.SQL_GET_ALL_CARS_WITH_PAGES))
                .thenReturn(pstmt);

        ///////////////

        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(con);

        when(dbManager.getAllCars(0, Integer.MAX_VALUE)).thenCallRealMethod();

        List<Car> actual = dbManager.getAllCars(0, Integer.MAX_VALUE);
        List<Car> expected = new ArrayList<>();
        expected.add(car1);
        expected.add(car2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getOrderByIdTest() throws SQLException, DBException, ParseException {

        rs = mock(ResultSet.class);

        when(rs.next())
                .thenReturn(true);

        when(rs.getInt("id"))
                .thenReturn(5);

        when(rs.getDate("rent_start"))
                .thenReturn(new Date (new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-01").getTime()));

        when(rs.getDate("rent_end"))
                .thenReturn(new Date (new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-05").getTime()));

        when(rs.getString(rs.getString("passport")))
                .thenReturn("KF456123");

        when(rs.getInt("driver"))
                .thenReturn(0);

        when(rs.getInt("user_id"))
                .thenReturn(4);

        when(rs.getInt("car_id"))
                .thenReturn(7);

        when(rs.getString("cancel_comments"))
                .thenReturn(null);

        when(rs.getString("status"))
                .thenReturn("Order closed");

        when(rs.getString("car_model"))
                .thenReturn("Focus");

        when(rs.getString("car_status"))
                .thenReturn("Available");

        when(rs.getInt("brand_id"))
                .thenReturn(65);

        when(rs.getString("brand_name"))
                .thenReturn("Ford");

        when(rs.getInt("cost"))
                .thenReturn(1500);

        when(rs.getInt("payment_status"))
                .thenReturn(0);

        when(rs.getDate("payment_date"))
                .thenReturn(null);

        when(rs.getInt("damage_cost"))
                .thenReturn(450);

        when(rs.getInt("damage_payment_status"))
                .thenReturn(0);

        when(rs.getDate("damage_payment_date"))
                .thenReturn(null);

        ///////////////

        pstmt = mock(PreparedStatement.class);
        when(pstmt.executeQuery())
                .thenReturn(rs);

        ///////////////

        con = mock(Connection.class);
        when(con.createStatement()).thenReturn(stmt);
        when(con.prepareStatement(DBManager.SQL_GET_ORDER_BY_ID))
                .thenReturn(pstmt);

        ///////////////

        dbManager = mock(DBManager.class);
        when(dbManager.getConnection()).thenReturn(con);

        when(dbManager.getOrderById(5)).thenCallRealMethod();

        Order actual = dbManager.getOrderById(5);
        Order expected = order1;
        Assert.assertEquals(expected, actual);
    }
}
