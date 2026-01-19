package com.comix.dao;

import com.comix.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDao {

    public int buy(int customerId, int comicId, int qty) throws Exception {
        Connection con = Database.getConnection();
        con.setAutoCommit(false);

        try {
            PreparedStatement st1 = con.prepareStatement("select price, stock from comics where id=?");
            st1.setInt(1, comicId);
            ResultSet rs1 = st1.executeQuery();

            if (!rs1.next()) {
                con.rollback();
                con.close();
                return -2;
            }

            double price = rs1.getDouble(1);
            int stock = rs1.getInt(2);

            if (stock < qty) {
                con.rollback();
                con.close();
                return -3;
            }

            PreparedStatement st2 = con.prepareStatement("update comics set stock=stock-? where id=?");
            st2.setInt(1, qty);
            st2.setInt(2, comicId);
            st2.executeUpdate();

            PreparedStatement st3 = con.prepareStatement("insert into orders(customer_id,total) values(?,?) returning id");
            st3.setInt(1, customerId);
            st3.setDouble(2, price * qty);
            ResultSet rs3 = st3.executeQuery();
            rs3.next();
            int orderId = rs3.getInt(1);

            PreparedStatement st4 = con.prepareStatement("insert into order_items(order_id,comic_id,quantity,price_at_purchase) values(?,?,?,?)");
            st4.setInt(1, orderId);
            st4.setInt(2, comicId);
            st4.setInt(3, qty);
            st4.setDouble(4, price);
            st4.executeUpdate();

            con.commit();
            con.close();
            return orderId;
        } catch (Exception e) {
            con.rollback();
            con.close();
            throw e;
        }
    }
}
