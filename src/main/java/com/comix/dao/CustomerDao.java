package com.comix.dao;

import com.comix.config.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDao {

    public boolean exists(int id) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement(
                "select id from customers where id = ?"
        );
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();
        boolean ok = rs.next();

        rs.close();
        st.close();
        con.close();

        return ok;
    }
}
