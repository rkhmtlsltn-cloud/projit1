package com.comix.dao;

import com.comix.config.Database;
import com.comix.model.Chapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChapterDao {

    public ArrayList<Chapter> getByComic(int comicId) throws Exception {
        ArrayList<Chapter> list = new ArrayList<>();

        Connection con = Database.getInstance().getConnection();
        PreparedStatement st = con.prepareStatement(
                "select chapter_number, title, text from comic_chapters where comic_id=? order by chapter_number"
        );
        st.setInt(1, comicId);

        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            list.add(new Chapter(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3)
            ));
        }

        con.close();
        return list;
    }
}
