package inventory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in)) {

            InventoryManager manager = new InventoryManager();
            manager.loadData();

            while (true) {

                System.out.println("\nInventory Management System");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Search Product");
                System.out.println("4. Update Quantity");
                System.out.println("5. Delete Product");
                System.out.println("6. Save Data");
                System.out.println("7. Generate PDF");
                System.out.println("8. Exit");

                System.out.print("\nEnter Choice : ");

                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Enter number only.");
                    sc.next();
                    continue;
                }

                int choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        System.out.print("Enter ID : ");
                        int id = sc.nextInt();

                        sc.nextLine();

                        System.out.print("Enter Name : ");
                        String name = sc.nextLine();

                        System.out.print("Enter Quantity : ");
                        int qty = sc.nextInt();

                        System.out.print("Enter Price : ");
                        double price = sc.nextDouble();

                        manager.addProduct(new Product(id, name, qty, price));
                        break;

                    case 2:
                        manager.viewProducts();
                        break;

                    case 3:
                        System.out.print("Enter Product ID : ");
                        manager.searchProduct(sc.nextInt());
                        break;

                    case 4:
                        System.out.print("Enter Product ID : ");
                        int pid = sc.nextInt();

                        System.out.print("Enter New Quantity : ");
                        int q = sc.nextInt();

                        manager.updateQuantity(pid, q);
                        break;

                    case 5:
                        System.out.print("Enter Product ID : ");
                        manager.deleteProduct(sc.nextInt());
                        break;

                    case 6:
                        manager.saveData();
                        break;

                    case 7:
                        manager.generatePDF();
                        break;

                    case 8:
                        manager.saveData();
                        System.out.println("Thank You");
                        return;

                    default:
                        System.out.println("Invalid Choice");
                }
            }
        }
    }
}