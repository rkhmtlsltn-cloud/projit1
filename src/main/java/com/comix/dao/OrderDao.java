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
         PreparedStatement s = con.prepareStatement("select price,stock from comics where id=?");
s.setInt(1, comicId);
ResultSet r = s.executeQuery();
if (!r.next()) return fail(con, -2);

double price = r.getDouble(1);
if (r.getInt(2) < qty) return fail(con, -3);

s = con.prepareStatement("update comics set stock=stock-? where id=?");
s.setInt(1, qty);
s.setInt(2, comicId);
s.executeUpdate();

s = con.prepareStatement("insert into orders(customer_id,total) values(?,?) returning id");
s.setInt(1, customerId);
s.setDouble(2, price * qty);
r = s.executeQuery();
r.next();
int orderId = r.getInt(1);

s = con.prepareStatement("insert into order_items(order_id,comic_id,quantity,price_at_purchase) values(?,?,?,?)");
s.setInt(1, orderId);
s.setInt(2, comicId);
s.setInt(3, qty);
s.setDouble(4, price);
s.executeUpdate();


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

