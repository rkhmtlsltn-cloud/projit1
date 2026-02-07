package com.comix.app;

import com.comix.dao.DaoFactory;
import com.comix.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static int askInt(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter a number");
            }
        }
    }

    private static double askDouble(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Enter a number");
            }
        }
    }

    private static String askStr(Scanner sc, String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    private static void pause(Scanner sc) {
        System.out.print("Press Enter...");
        sc.nextLine();
    }

    private static boolean isAdmin(Customer u) {
        return u.role != null && u.role.equalsIgnoreCase("ADMIN");
    }

    private static boolean canManage(Customer u) {
        return u.role != null && (u.role.equalsIgnoreCase("ADMIN") || u.role.equalsIgnoreCase("MANAGER"));
    }

    private static void menu(Customer u) {
        System.out.println("\n=== COMIX SHOP ===");
        System.out.println("User: " + u.name + " | " + u.role);
        System.out.println("1  Show all comics");
        System.out.println("2  View comic by id");
        System.out.println("3  Add comic (ADMIN/MANAGER)");
        System.out.println("4  Buy comic");
        System.out.println("5  Comics by category");
        System.out.println("6  Full order (ADMIN/MANAGER)");
        System.out.println("7  Read manga");
        System.out.println("8  Read chapters");
        System.out.println("9  Delete comic (ADMIN)");
        System.out.println("0  Exit");
        System.out.print("Choose: ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DaoFactory f = DaoFactory.getInstance();

        Customer user = null;
        while (user == null) {
            int id = askInt(sc, "Login customer id: ");
            try {
                user = f.customerDao().getById(id);
                if (user == null) System.out.println("Not found");
            } catch (Exception e) {
                System.out.println("DB error");
            }
        }

        final Customer currentUser = user;

        Map<String, Runnable> actions = new HashMap<>();

        actions.put("1", () -> {
            try {
                for (Comic c : f.comicDao().getAll()) {
                    System.out.println(c.id + " | " + c.title + " | " + c.price + " | stock=" + c.stock);
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("2", () -> {
            int id = askInt(sc, "Comic id: ");
            try {
                Comic c = f.comicDao().getById(id);
                if (c == null) System.out.println("Not found");
                else {
                    System.out.println("Title: " + c.title);
                    System.out.println("Category: " + c.category);
                    System.out.println("Price: " + c.price);
                    System.out.println("Stock: " + c.stock);
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("3", () -> {
            if (!canManage(currentUser)) {
                System.out.println("Access denied");
                pause(sc);
                return;
            }

            String t = askStr(sc, "Title: ");
            double p = askDouble(sc, "Price: ");
            int s = askInt(sc, "Stock: ");
            String cat = askStr(sc, "Category: ");
            String story = askStr(sc, "Story: ");

            if (isBlank(t) || isBlank(cat) || p <= 0 || s < 0) {
                System.out.println("Validation error");
                pause(sc);
                return;
            }

            try {
                f.comicDao().add(t, p, s, cat, story);
                System.out.println("Added");
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("4", () -> {
            int cid = askInt(sc, "Comic id: ");
            int q = askInt(sc, "Qty: ");
            try {
                int r = f.orderDao().buy(currentUser.id, cid, q);
                if (r > 0) System.out.println("Order id=" + r);
                else if (r == -2) System.out.println("Comic not found");
                else if (r == -3) System.out.println("Not enough stock");
                else System.out.println("Buy error");
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("5", () -> {
            String cat = askStr(sc, "Category: ");
            if (isBlank(cat)) {
                System.out.println("Validation error");
                pause(sc);
                return;
            }
            try {
                for (Comic c : f.comicDao().getByCategory(cat)) {
                    System.out.println(c.id + " | " + c.title);
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("6", () -> {
            if (!canManage(currentUser)) {
                System.out.println("Access denied");
                pause(sc);
                return;
            }

            int oid = askInt(sc, "Order id: ");
            try {
                FullOrderDescription fo = f.orderDao().getFullOrderDescription(oid);
                if (fo == null) System.out.println("Not found");
                else {
                    System.out.println("Order " + fo.orderId + " Total=" + fo.total);
                    System.out.println("Customer: " + fo.customerId + " " + fo.customerName);
                    for (OrderLine i : fo.items) {
                        System.out.println(i.title + " x" + i.quantity + " | " + i.priceAtPurchase);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("7", () -> {
            int id = askInt(sc, "Comic id: ");
            try {
                Comic c = f.comicDao().getById(id);
                if (c == null) System.out.println("Not found");
                else System.out.println(c.story);
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("8", () -> {
            int id = askInt(sc, "Comic id: ");
            try {
                var chapters = f.chapterDao().getByComic(id);
                if (chapters.isEmpty()) System.out.println("No chapters");
                for (Chapter ch : chapters) {
                    System.out.println("\nChapter " + ch.number + " - " + ch.title);
                    System.out.println(ch.text);
                }
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        actions.put("9", () -> {
            if (!isAdmin(currentUser)) {
                System.out.println("Access denied");
                pause(sc);
                return;
            }

            int id = askInt(sc, "Comic id to delete: ");
            try {
                boolean ok = f.comicDao().deleteById(id);
                if (ok) System.out.println("Deleted");
                else System.out.println("Not found");
            } catch (Exception e) {
                System.out.println("Error");
            }
            pause(sc);
        });

        while (true) {
            menu(currentUser);
            String c = sc.nextLine().trim();
            if (c.equals("0")) break;
            actions.getOrDefault(c, () -> {
                System.out.println("Wrong choice");
                pause(sc);
            }).run();
        }
    }
}

