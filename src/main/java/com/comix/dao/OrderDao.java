package com.comix.dao;

import com.comix.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDao {

    static int fail(Connection con, int code) throws Exception {
        con.rollback();
        con.close();
        return code;
    }

    public int buy(int customerId, int comicId, int qty) throws Exception {
        Connection con = Database.getConnection();
        con.setAutoCommit(false);

        try {
            PreparedStatement s1 = con.prepareStatement("select price, stock from comics where id=?");
            s1.setInt(1, comicId);
            ResultSet r1 = s1.executeQuery();
            if (!r1.next()) return fail(con, -2);

            double price = r1.getDouble(1);
            int stock = r1.getInt(2);
            if (stock < qty) return fail(con, -3);

            PreparedStatement s2 = con.prepareStatement("update comics set stock=stock-? where id=?");
            s2.setInt(1, qty);
            s2.setInt(2, comicId);
            s2.executeUpdate();

            PreparedStatement s3 = con.prepareStatement("insert into orders(customer_id,total) values(?,?) returning id");
            s3.setInt(1, customerId);
            s3.setDouble(2, price * qty);
            ResultSet r3 = s3.executeQuery();
            r3.next();
            int orderId = r3.getInt(1);

            PreparedStatement s4 = con.prepareStatement("insert into order_items(order_id,comic_id,quantity,price_at_purchase) values(?,?,?,?)");
            s4.setInt(1, orderId);
            s4.setInt(2, comicId);
            s4.setInt(3, qty);
            s4.setDouble(4, price);
            s4.executeUpdate();

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

