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

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(
                     "select id, title, price, stock, category, story from comics order by id"
             );
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                list.add(new Comic(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getString(6)
                ));
            }
        }
        return list;
    }

    public Comic getById(int id) throws Exception {
        Comic c = null;

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(
                     "select id, title, price, stock, category, story from comics where id=?"
             )) {

            st.setInt(1, id);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    c = new Comic(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getDouble(3),
                            rs.getInt(4),
                            rs.getString(5),
                            rs.getString(6)
                    );
                }
            }
        }
        return c;
    }

    public ArrayList<Comic> getByCategory(String category) throws Exception {
        ArrayList<Comic> list = new ArrayList<>();

        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(
                     "select id, title, price, stock, category, story from comics where category=?"
             )) {

            st.setString(1, category);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(new Comic(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getDouble(3),
                            rs.getInt(4),
                            rs.getString(5),
                            rs.getString(6)
                    ));
                }
            }
        }
        return list;
    }

    public void add(String title, double price, int stock, String category, String story) throws Exception {
        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(
                     "insert into comics(title, price, stock, category, story) values(?,?,?,?,?)"
             )) {

            st.setString(1, title);
            st.setDouble(2, price);
            st.setInt(3, stock);
            st.setString(4, category);
            st.setString(5, story);

            st.executeUpdate();
        }
    }

    public boolean deleteById(int id) throws Exception {
        try (Connection con = Database.getInstance().getConnection();
             PreparedStatement st = con.prepareStatement(
                     "delete from comics where id=?"
             )) {

            st.setInt(1, id);
            return st.executeUpdate() > 0;
        }
    }
}



        st.executeUpdate();
        con.close();
    }
}
