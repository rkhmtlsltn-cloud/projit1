package com.comix.dao;

import com.comix.config.Database;
import com.comix.model.Comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ComicDao {

    public ArrayList<Comic> getAll() throws Exception {
        ArrayList<Comic> list = new ArrayList<>();

        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement("select id, title, price, stock from comics order by id");
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            list.add(new Comic(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
            ));
        }

        rs.close();
        st.close();
        con.close();

        return list;
    }

    public Comic getById(int id) throws Exception {
        Comic comic = null;

        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement("select id, title, price, stock from comics where id = ?");
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            comic = new Comic(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
            );
        }

        rs.close();
        st.close();
        con.close();

        return comic;
    }

    public void add(String title, double price, int stock) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement("insert into comics(title, price, stock) values(?,?,?)");
        st.setString(1, title);
        st.setDouble(2, price);
        st.setInt(3, stock);

        st.executeUpdate();

        st.close();
        con.close();
    }
}
