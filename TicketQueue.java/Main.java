

import java.util.*;
import java.io.*;

class Ticket {
    String id, customer, issue, status;

    Ticket(String id, String customer, String issue) {
        this.id = id;
        this.customer = customer;
        this.issue = issue;
        this.status = "Waiting";
    }
}

public class Main {

    static LinkedList<Ticket> queue = new LinkedList<>();
    static ArrayList<Ticket> allTickets = new ArrayList<>();
    static int nextId = 101;

    static void loadData() {
        try {
            File file = new File("tickets.txt");

            if (!file.exists()) return;

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] data = line.split("\\|", -1);

                if (data.length == 4) {
                    Ticket t = new Ticket(
                        data[0], data[1], data[2]
                    );
                    t.status = data[3];

                    allTickets.add(t);

                    if (t.status.equals("Waiting")) {
                        queue.add(t);
                    }

                    int id = Integer.parseInt(
                        t.id.substring(1)
                    );
                    nextId = Math.max(nextId, id + 1);
                }
            }
            sc.close();

        } catch (Exception e) {
            System.out.println("Error loading file.");
        }
    }

    static void saveData() {
        try {
            PrintWriter pw = new PrintWriter(
                new FileWriter("tickets.txt")
            );

            for (Ticket t : allTickets) {
                pw.println(t.id + "|" + t.customer
                    + "|" + t.issue + "|" + t.status);
            }

            pw.close();

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    static void raiseTicket(Scanner sc) {
        System.out.print("Enter Customer Name: ");
        String name = sc.nextLine().replace("|", "/");

        System.out.print("Enter Issue: ");
        String issue = sc.nextLine().replace("|", "/");

        String id = "T" + nextId++;

        Ticket t = new Ticket(id, name, issue);

        queue.add(t);
        allTickets.add(t);

        saveData();

        System.out.println("\nTicket Raised Successfully!");
        System.out.println("Ticket ID: " + id);
        System.out.println("Customer: " + name);
        System.out.println("Issue: " + issue);
        System.out.println("Position: " + queue.size());
    }

    static void serveNext() {
        if (queue.isEmpty()) {
            System.out.println("No tickets in the queue.");
            return;
        }

        Ticket t = queue.removeFirst();
        t.status = "Served";

        saveData();

        System.out.println("Ticket " + t.id + " served.");
        System.out.println("Customer: " + t.customer);
        System.out.println("Tickets waiting: " + queue.size());
    }

    static void viewQueue() {
        if (queue.isEmpty()) {
            System.out.println("No tickets in the queue.");
            return;
        }

        System.out.println("\n--- WAITING QUEUE ---");

        int position = 1;

        for (Ticket t : queue) {
            System.out.println(
                t.id + " | " + t.customer
                + " | " + t.issue
                + " | Position: " + position++
            );
        }
    }

    static void searchTicket(Scanner sc) {
        System.out.print("Enter Ticket ID: ");
        String id = sc.nextLine().trim();

        for (Ticket t : allTickets) {
            if (t.id.equalsIgnoreCase(id)) {
                System.out.println("Ticket Found!");
                System.out.println("ID: " + t.id);
                System.out.println("Customer: " + t.customer);
                System.out.println("Issue: " + t.issue);
                System.out.println("Status: " + t.status);
                return;
            }
        }

        System.out.println("Ticket not found.");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        loadData();

        int choice;

        do {
            System.out.println("\n===== SIMPLE TICKET QUEUE SYSTEM =====");
            System.out.println("1. Raise Ticket");
            System.out.println("2. Serve Next Ticket");
            System.out.println("3. View Queue");
            System.out.println("4. Search Ticket by ID");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        raiseTicket(sc);
                        break;

                    case 2:
                        serveNext();
                        break;

                    case 3:
                        viewQueue();
                        break;

                    case 4:
                        searchTicket(sc);
                        break;

                    case 5:
                        saveData();
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (NumberFormatException e) {
                choice = 0;
                System.out.println("Please enter a valid number.");
            }

        } while (choice != 5);

        sc.close();
    }
}
