package com.zibert.DAO.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderReceipt implements Serializable {

    private int cost;
    private int paymentStatus;
    private Date paymentDate;
    private int order_id;

    @Override
    public String toString() {
        return "OrderReceipt{" +
                "cost=" + cost +
                ", paymentStatus=" + paymentStatus +
                ", paymentDate=" + paymentDate +
                ", order_id=" + order_id +
                '}';
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
