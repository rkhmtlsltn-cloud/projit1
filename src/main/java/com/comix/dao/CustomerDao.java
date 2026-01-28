package com.comix.dao;

import com.comix.config.Database;
import com.comix.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDao {

    public Customer getById(int id) throws Exception {
        Connection con = Database.getInstance().getConnection();

        PreparedStatement st = con.prepareStatement(
                "select id, name, role from customers where id=?"
        );
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();

        Customer c = null;
        if (rs.next()) {
            c = new Customer(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3)
            );
        }

        con.close();
        return c;
    }
}
