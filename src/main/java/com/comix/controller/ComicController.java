package com.comix.controller;

import com.comix.dao.ComicDao;
import com.comix.model.Comic;

import java.util.ArrayList;

public class ComicController {
    private ComicDao dao = new ComicDao();

    public ArrayList<Comic> getAllComics() throws Exception {
        return dao.getAll();
    }
}
