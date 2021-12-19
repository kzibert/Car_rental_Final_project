package com.zibert.DAO.entity;

import java.io.Serializable;
import java.util.Date;

public class DamageReceipt implements Serializable {

    private int damageCost;
    private int damagePaymentStatus;
    private Date damagePaymentDate;
    private int order_id;

    @Override
    public String toString() {
        return "DamageReceipt{" +
                "damageCost=" + damageCost +
                ", damagePaymentStatus=" + damagePaymentStatus +
                ", damagePaymentDate=" + damagePaymentDate +
                ", order_id=" + order_id +
                '}';
    }

    public int getDamageCost() {
        return damageCost;
    }

    public void setDamageCost(int damageCost) {
        this.damageCost = damageCost;
    }

    public int getDamagePaymentStatus() {
        return damagePaymentStatus;
    }

    public void setDamagePaymentStatus(int damagePaymentStatus) {
        this.damagePaymentStatus = damagePaymentStatus;
    }

    public Date getDamagePaymentDate() {
        return damagePaymentDate;
    }

    public void setDamagePaymentDate(Date damagePaymentDate) {
        this.damagePaymentDate = damagePaymentDate;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
