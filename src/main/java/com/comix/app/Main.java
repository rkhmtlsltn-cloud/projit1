package com.comix.app;

import com.comix.dao.DaoFactory;
import com.comix.model.Comic;
import com.comix.model.Customer;
import com.comix.model.FullOrderDescription;
import com.comix.model.OrderLine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String line(int n) {
        String s = "";
        for (int i = 0; i < n; i++) s += "─";
        return s;
    }

    private static void boxTitle(String title) {
        int w = 64;
        System.out.println("┌" + line(w) + "┐");
        String t = " " + title + " ";
        int left = (w - t.length()) / 2;
        int right = w - t.length() - left;
        System.out.println("│" + " ".repeat(left) + t + " ".repeat(right) + "│");
        System.out.println("└" + line(w) + "┘");
    }

    private static void wrapPrint(String text, int width) {
        String s = text == null ? "" : text.trim();
        if (s.isEmpty()) {
            System.out.println("(no text)");
            return;
        }
        while (!s.isEmpty()) {
            if (s.length() <= width) {
                System.out.println(s);
                break;
            }
            int cut = width;
            while (cut > 0 && s.charAt(cut - 1) != ' ') cut--;
            if (cut == 0) cut = width;
            System.out.println(s.substring(0, cut).trim());
            s = s.substring(cut).trim();
        }
    }

    private static void pause(Scanner sc) {
        System.out.println();
        System.out.print("Press Enter...");
        sc.nextLine();
        System.out.println();
    }

    private static int askInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(sc.nextLine());
    }

    private static double askDouble(Scanner sc, String prompt) {
        System.out.print(prompt);
        return Double.parseDouble(sc.nextLine());
    }

    private static String askStr(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static boolean canAdd(Customer u) {
        return "ADMIN".equals(u.role) || "MANAGER".equals(u.role);
    }

    private static boolean canViewOrder(Customer u) {
        return "ADMIN".equals(u.role) || "MANAGER".equals(u.role);
    }

    private static void menu(Customer u) {
        boxTitle("COMIX Manga Shop");
        System.out.println("User: " + u.name + " | role=" + u.role);
        System.out.println();
        System.out.println("1  Show all comics");
        System.out.println("2  View comic by id");
        System.out.println("3  Add comic (ADMIN/MANAGER)");
        System.out.println("4  Buy comic");
        System.out.println("5  Show comics by category");
        System.out.println("6  Full order (JOIN) (ADMIN/MANAGER)");
        System.out.println("7  Read manga (text)");
        System.out.println("8  Read Chapters (text)");
        System.out.println("0  Exit");
        System.out.print("Choose: ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DaoFactory f = DaoFactory.getInstance();

        Customer current = null;

        while (current == null) {
            try {
                int id = askInt(sc, "Login customer id: ");
                current = f.customerDao().getById(id);
                if (current == null) System.out.println("Customer not found");
                else System.out.println("Logged in: " + current.name + " role=" + current.role);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        final Customer user = current;

        Map<String, Runnable> actions = new HashMap<>();

        actions.put("1", () -> {
            try {
                boxTitle("COMICS LIST");
                f.comicDao().getAll().forEach(c ->
                        System.out.println(String.format("%-3d | %-28s | %7.2f | stock=%-3d | %-10s",
                                c.id, c.title, c.price, c.stock, c.category))
                );
                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("2", () -> {
            try {
                int id = askInt(sc, "Comic id: ");
                Comic c = f.comicDao().getById(id);
                if (c == null) {
                    System.out.println("Not found");
                } else {
                    boxTitle("COMIC DETAILS");
                    System.out.println("Id: " + c.id);
                    System.out.println("Title: " + c.title);
                    System.out.println("Price: " + c.price);
                    System.out.println("Stock: " + c.stock);
                    System.out.println("Category: " + c.category);
                }
                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("3", () -> {
            try {
                if (!canAdd(user)) {
                    System.out.println("Access denied");
                    pause(sc);
                    return;
                }

                String t = askStr(sc, "Title: ");
                double p = askDouble(sc, "Price: ");
                int s = askInt(sc, "Stock: ");
                String cat = askStr(sc, "Category: ");
                String story = askStr(sc, "Story (text): ");

                if (isBlank(t) || isBlank(cat) || p <= 0 || s < 0) {
                    System.out.println("Validation error");
                    pause(sc);
                    return;
                }

                f.comicDao().add(t, p, s, cat, story == null ? "" : story);
                System.out.println("Comic added");
                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("4", () -> {
            try {
                int mid = askInt(sc, "Comic id: ");
                int q = askInt(sc, "Quantity: ");

                if (q <= 0) {
                    System.out.println("Validation error");
                    pause(sc);
                    return;
                }

                int r = f.orderDao().buy(user.id, mid, q);

                if (r == -2) System.out.println("Buy error: comic not found");
                else if (r == -3) System.out.println("Buy error: not enough stock");
                else if (r < 0) System.out.println("Buy error");
                else System.out.println("Order created, id=" + r);

                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("5", () -> {
            try {
                String cat = askStr(sc, "Category: ");
                if (isBlank(cat)) {
                    System.out.println("Validation error");
                    pause(sc);
                    return;
                }

                boxTitle("CATEGORY: " + cat);
                f.comicDao().getByCategory(cat).forEach(c ->
                        System.out.println(String.format("%-3d | %-28s | %7.2f | stock=%-3d",
                                c.id, c.title, c.price, c.stock))
                );
                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("6", () -> {
            try {
                if (!canViewOrder(user)) {
                    System.out.println("Access denied");
                    pause(sc);
                    return;
                }

                int oid = askInt(sc, "Order id: ");
                FullOrderDescription fo = f.orderDao().getFullOrderDescription(oid);

                if (fo == null) {
                    System.out.println("Order not found");
                    pause(sc);
                    return;
                }

                boxTitle("FULL ORDER (JOIN)");
                System.out.println("Order: " + fo.orderId);
                System.out.println("Customer: " + fo.customerId + " " + fo.customerName);
                System.out.println("Total: " + fo.total);
                System.out.println(line(64));

                for (OrderLine it : fo.items) {
                    System.out.println(String.format("Item %-3d | %-26s | qty=%-3d | price=%7.2f",
                            it.itemId, it.title, it.quantity, it.priceAtPurchase));
                }

                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("7", () -> {
            try {
                int id = askInt(sc, "Comic id to read: ");
                Comic c = f.comicDao().getById(id);

                if (c == null) {
                    System.out.println("Not found");
                    pause(sc);
                    return;
                }

                boxTitle("READING: " + c.title);
                System.out.println("Category: " + c.category);
                System.out.println("Price: " + c.price + " | Stock: " + c.stock);
                System.out.println();
                wrapPrint(c.story, 70);
                pause(sc);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });

        actions.put("8", () -> {
            try {
                int id = askInt(sc, "Comic id: ");
                var chapters = f.chapterDao().getByComic(id);

                if (chapters.isEmpty()) {
                    System.out.println("No chapters available");
                    pause(sc);
                    return;
                }

                for (var ch : chapters) {
                    boxTitle("Chapter " + ch.number + ": " + ch.title);
                    wrapPrint(ch.text, 70);
                    pause(sc);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                pause(sc);
            }
        });


        while (true) {
            menu(user);
            String choice = sc.nextLine();

            if ("0".equals(choice)) {
                sc.close();
                return;
            }

            Runnable action = actions.get(choice);
            if (action == null) {
                System.out.println("Wrong choice");
                pause(sc);
            } else {
                action.run();
            }
        }
    }
}
