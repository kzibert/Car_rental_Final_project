package com.zibert.DAO.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Order implements Serializable {

    private int id;
    private Date rentStart;
    private Date rentEnd;
    private String passport;
    private int driver;
    private int userId;
    private int carId;
    private String cancelComments;
    private String status;
    private Car car;
    private OrderReceipt orderReceipt;
    private DamageReceipt damageReceipt;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", rentStart=" + rentStart +
                ", rentEnd=" + rentEnd +
                ", passport='" + passport + '\'' +
                ", driver=" + driver +
                ", userId=" + userId +
                ", carId=" + carId +
                ", cancelComments='" + cancelComments + '\'' +
                ", status='" + status + '\'' +
                ", car=" + car +
                ", orderReceipt=" + orderReceipt +
                ", damageReceipt=" + damageReceipt +
                '}';
    }

    public DamageReceipt getDamageReceipt() {
        return damageReceipt;
    }

    public void setDamageReceipt(DamageReceipt damageReceipt) {
        this.damageReceipt = damageReceipt;
    }

    public OrderReceipt getOrderReceipt() {
        return orderReceipt;
    }

    public void setOrderReceipt(OrderReceipt orderReceipt) {
        this.orderReceipt = orderReceipt;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRentStart() {
        return rentStart;
    }

    public void setRentStart(Date rentStart) {
        this.rentStart = rentStart;
    }

    public Date getRentEnd() {
        return rentEnd;
    }

    public void setRentEnd(Date rentEnd) {
        this.rentEnd = rentEnd;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public int getDriver() {
        return driver;
    }

    public void setDriver(int driver) {
        this.driver = driver;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCancelComments() {
        return cancelComments;
    }

    public void setCancelComments(String cancelComments) {
        this.cancelComments = cancelComments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
