package com.comix.dao;

import com.comix.config.Database;
import com.comix.model.FullOrderDescription;
import com.comix.model.OrderLine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDao {

    public int buy(int customerId, int comicId, int qty) throws Exception {
        try (Connection con = Database.getInstance().getConnection()) {
            con.setAutoCommit(false);

            try {
                double price;

                try (PreparedStatement s1 = con.prepareStatement("select price from comics where id=?")) {
                    s1.setInt(1, comicId);
                    try (ResultSet r1 = s1.executeQuery()) {
                        if (!r1.next()) {
                            con.rollback();
                            return -2;
                        }
                        price = r1.getDouble(1);
                    }
                }

                int updated;
                try (PreparedStatement s2 = con.prepareStatement(
                        "update comics set stock=stock-? where id=? and stock>=?"
                )) {
                    s2.setInt(1, qty);
                    s2.setInt(2, comicId);
                    s2.setInt(3, qty);
                    updated = s2.executeUpdate();
                }

                if (updated == 0) {
                    con.rollback();
                    return -3;
                }

                int orderId;

                try (PreparedStatement s3 = con.prepareStatement(
                        "insert into orders(customer_id,total) values(?,?) returning id"
                )) {
                    s3.setInt(1, customerId);
                    s3.setDouble(2, price * qty);
                    try (ResultSet r3 = s3.executeQuery()) {
                        r3.next();
                        orderId = r3.getInt(1);
                    }
                }

                try (PreparedStatement s4 = con.prepareStatement(
                        "insert into order_items(order_id,comic_id,quantity,price_at_purchase) values(?,?,?,?)"
                )) {
                    s4.setInt(1, orderId);
                    s4.setInt(2, comicId);
                    s4.setInt(3, qty);
                    s4.setDouble(4, price);
                    s4.executeUpdate();
                }

                con.commit();
                return orderId;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public FullOrderDescription getFullOrderDescription(int orderId) throws Exception {
        FullOrderDescription full = null;

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(
                     "select o.id, c.id, c.name, o.total, oi.id, cm.id, cm.title, oi.quantity, oi.price_at_purchase " +
                             "from orders o " +
                             "join customers c on c.id=o.customer_id " +
                             "join order_items oi on oi.order_id=o.id " +
                             "join comics cm on cm.id=oi.comic_id " +
                             "where o.id=? " +
                             "order by oi.id"
             )) {

            st.setInt(1, orderId);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    if (full == null) {
                        full = new FullOrderDescription(
                                rs.getInt(1),
                                rs.getInt(2),
                                rs.getString(3),
                                rs.getDouble(4)
                        );
                    }

                    full.items.add(new OrderLine(
                            rs.getInt(5),
                            rs.getInt(6),
                            rs.getString(7),
                            rs.getInt(8),
                            rs.getDouble(9)
                    ));
                }
            }
        }

        return full;
    }
}
