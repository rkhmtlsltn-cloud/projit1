package com.comix.controller;

import com.comix.dao.DaoFactory;
import com.comix.model.Comic;

import java.util.ArrayList;

public class ComicController {

    public ArrayList<Comic> getAllComics() throws Exception {
        return DaoFactory.getInstance().comicDao().getAll();
    }
}
