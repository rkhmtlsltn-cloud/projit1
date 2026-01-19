package com.comix.dao;

import com.comix.config.Database;
import com.comix.model.Comic;

import java.sql.*;
import java.util.ArrayList;

public class ComicDao {

    public ArrayList<Comic> getAll() throws Exception {
        ArrayList<Comic> comics = new ArrayList<>();

        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement(
                "select id, title, price, stock from comics"
        );
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            comics.add(new Comic(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getInt(4)
            ));
        }

        con.close();
        return comics;
    }

    public Comic getById(int id) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement(
                "select id, title, price, stock from comics where id=?"
        );
        st.setInt(1, id);

        ResultSet rs = st.executeQuery();
        Comic comic = null;

        if (rs.next()) {
            comic = new Comic(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getDouble(3),
                    rs.getInt(4)
            );
        }

        con.close();
        return comic;
    }

    public void add(String title, double price, int stock) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement st = con.prepareStatement(
                "insert into comics(title, price, stock) values(?,?,?)"
        );

        st.setString(1, title);
        st.setDouble(2, price);
        st.setInt(3, stock);

        st.executeUpdate();
        con.close();
    }
}
