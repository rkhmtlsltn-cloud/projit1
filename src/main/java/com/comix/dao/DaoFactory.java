package com.comix.dao;

public class DaoFactory {

    private static final DaoFactory instance = new DaoFactory();

    private final ComicDao comicDao = new ComicDao();
    private final OrderDao orderDao = new OrderDao();
    private final CustomerDao customerDao = new CustomerDao();
    private final ChapterDao chapterDao = new ChapterDao();

    private DaoFactory() {
    }

    public static DaoFactory getInstance() {
        return instance;
    }

    public ComicDao comicDao() {
        return comicDao;
    }

    public OrderDao orderDao() {
        return orderDao;
    }

    public CustomerDao customerDao() {
        return customerDao;
    }

    public ChapterDao chapterDao() {
        return chapterDao;
    }
}
