package com.comix.app;

import com.comix.dao.ComicDao;
import com.comix.dao.OrderDao;
import com.comix.model.Comic;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ComicDao comicDao = new ComicDao();
        OrderDao orderDao = new OrderDao();

        while (true) {
            System.out.println("\n1 Show all\n2 View by id\n3 Add manga\n4 Buy manga\n0 Exit");

            try {
                switch (sc.nextLine()) {
                    case "1" -> {
                        for (Comic x : comicDao.getAll())
                            System.out.println(x.id + " " + x.title + " " + x.price + " stock=" + x.stock);
                    }
                    case "2" -> {
                        System.out.print("Id: ");
                        Comic x = comicDao.getById(Integer.parseInt(sc.nextLine()));
                        System.out.println(x == null ? "Not found" :
                                x.id + "\n" + x.title + "\n" + x.price + "\n" + x.stock);
                    }
                    case "3" -> {
                        System.out.print("Title: ");
                        String t = sc.nextLine();
                        System.out.print("Price: ");
                        double p = Double.parseDouble(sc.nextLine());
                        System.out.print("Stock: ");
                        int s = Integer.parseInt(sc.nextLine());
                        comicDao.add(t, p, s);
                        System.out.println("Added");
                    }
                    case "4" -> {
                        System.out.print("Customer id: ");
                        int cid = Integer.parseInt(sc.nextLine());
                        System.out.print("Comic id: ");
                        int mid = Integer.parseInt(sc.nextLine());
                        System.out.print("Qty: ");
                        int q = Integer.parseInt(sc.nextLine());
                        int r = orderDao.buy(cid, mid, q);
                        System.out.println(r < 0 ? "Buy error" : "Order id = " + r);
                    }
                    case "0" -> {
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Wrong choice");
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
        }
    }
}


