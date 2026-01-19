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
            PreparedStatement st = con.prepareStatement(
                    "select price, stock from comics where id = ?"
            );
            st.setInt(1, comicId);
            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                con.rollback();
                con.close();
                return -2;
            }

            double price = rs.getDouble(1);
            int stock = rs.getInt(2);

            if (stock < qty) {
                con.rollback();
                con.close();
                return -3;
            }

            PreparedStatement updateStock = con.prepareStatement(
                    "update comics set stock = stock - ? where id = ?"
            );
            updateStock.setInt(1, qty);
            updateStock.setInt(2, comicId);
            updateStock.executeUpdate();

            PreparedStatement createOrder = con.prepareStatement(
                    "insert into orders(customer_id, total) values(?, ?) returning id"
            );
            createOrder.setInt(1, customerId);
            createOrder.setDouble(2, price * qty);
            ResultSet orderRs = createOrder.executeQuery();
            orderRs.next();
            int orderId = orderRs.getInt(1);

            PreparedStatement addItem = con.prepareStatement(
                    "insert into order_items(order_id, comic_id, quantity, price_at_purchase) values(?,?,?,?)"
            );
            addItem.setInt(1, orderId);
            addItem.setInt(2, comicId);
            addItem.setInt(3, qty);
            addItem.setDouble(4, price);
            addItem.executeUpdate();

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
