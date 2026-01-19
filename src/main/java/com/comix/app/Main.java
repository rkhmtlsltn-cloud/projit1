package com.comix.app;

import com.comix.dao.ComicDao;
import com.comix.dao.OrderDao;
import com.comix.model.Comic;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ComicDao comicDao = new ComicDao();
        OrderDao orderDao = new OrderDao();

        while (true) {
            printMenu();
            String choice = sc.nextLine();

            try {
                if (choice.equals("1")) {
                    showAll(comicDao);
                } else if (choice.equals("2")) {
                    showById(sc, comicDao);
                } else if (choice.equals("3")) {
                    addComic(sc, comicDao);
                } else if (choice.equals("4")) {
                    buyComic(sc, orderDao);
                } else if (choice.equals("0")) {
                    break;
                } else {
                    System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
        }

        sc.close();
    }

    static void printMenu() {
        System.out.println();
        System.out.println("=== COMIX MANGA SHOP ===");
        System.out.println("1) Show all manga");
        System.out.println("2) View manga by id");
        System.out.println("3) Add manga");
        System.out.println("4) Buy manga");
        System.out.println("0) Exit");
        System.out.print("> ");
    }

    static void showAll(ComicDao comicDao) throws Exception {
        ArrayList<Comic> list = comicDao.getAll();
        for (Comic c : list) {
            System.out.println(c.id + " | " + c.title + " | " + c.price + " | stock=" + c.stock);
        }
    }

    static void showById(Scanner sc, ComicDao comicDao) throws Exception {
        System.out.print("Manga id: ");
        int id = Integer.parseInt(sc.nextLine());

        Comic c = comicDao.getById(id);
        if (c == null) {
            System.out.println("Not found");
        } else {
            System.out.println(c.id);
            System.out.println(c.title);
            System.out.println(c.price);
            System.out.println(c.stock);
        }
    }

    static void addComic(Scanner sc, ComicDao comicDao) throws Exception {
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(sc.nextLine());

        comicDao.add(title, price, stock);
        System.out.println("Added");
    }

    static void buyComic(Scanner sc, OrderDao orderDao) throws Exception {
        System.out.print("Customer id: ");
        int customerId = Integer.parseInt(sc.nextLine());
        System.out.print("Manga id: ");
        int comicId = Integer.parseInt(sc.nextLine());
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());

        int res = orderDao.buy(customerId, comicId, qty);

        if (res == -1) System.out.println("Customer not found");
        else if (res == -2) System.out.println("Manga not found");
        else if (res == -3) System.out.println("Not enough stock");
        else System.out.println("Order created. ID = " + res);
    }
}
