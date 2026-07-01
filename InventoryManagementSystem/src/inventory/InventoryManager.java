package inventory;

import java.io.*;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class InventoryManager {

    private HashMap<Integer, Product> products = new HashMap<>();

    private static final int LOW_STOCK_LIMIT = 5;

    // ADD PRODUCT
    public void addProduct(Product p) {
        products.put(p.getId(), p);
        System.out.println("Product Added Successfully");
    }

    // VIEW PRODUCTS
    public void viewProducts() {
        if (products.isEmpty()) {
            System.out.println("Inventory Empty");
            return;
        }

        for (Product p : products.values()) {
            System.out.println(p);
        }
    }

    // SEARCH PRODUCT
    public void searchProduct(int id) {
        Product p = products.get(id);

        if (p != null)
            System.out.println(p);
        else
            System.out.println("Product Not Found");
    }

    // UPDATE QUANTITY
    public void updateQuantity(int id, int qty) {
        Product p = products.get(id);

        if (p != null) {
            p.setQuantity(qty);
            System.out.println("Quantity Updated");
        } else {
            System.out.println("Product Not Found");
        }
    }

    // DELETE PRODUCT
    public void deleteProduct(int id) {
        if (products.remove(id) != null)
            System.out.println("Product Deleted");
        else
            System.out.println("Product Not Found");
    }

    // SAVE DATA (SERIALIZATION)
    public void saveData() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("inventory.dat"))) {

            out.writeObject(products);
            System.out.println("Data Saved Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // LOAD DATA (DESERIALIZATION)
    @SuppressWarnings("unchecked")
    public void loadData() {
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream("inventory.dat"))) {

            products = (HashMap<Integer, Product>) in.readObject();

        } catch (Exception e) {
            // file may not exist initially
        }
    }

    // GENERATE PDF REPORT
    public void generatePDF() {

        try {
            File file = new File("InventoryReport.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));

            document.open();

            // Fonts
            Font companyFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Company Title
            Paragraph company = new Paragraph("Deepak  STORE PVT LTD", companyFont);
            company.setAlignment(Element.ALIGN_CENTER);
            document.add(company);

            // Report Title
            Paragraph report = new Paragraph("INVENTORY MANAGEMENT BILL", headingFont);
            report.setAlignment(Element.ALIGN_CENTER);
            document.add(report);

            document.add(new Paragraph("\n"));

            // Date
            String date = new SimpleDateFormat("dd-MMM-yyyy hh:mm a").format(new Date());
            document.add(new Paragraph("Generated On : " + date, normalFont));

            document.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);

            String[] columns = {
                    "ID", "Product Name", "Quantity",
                    "Price", "Total Value", "Stock Status"
            };

            for (String c : columns) {
                PdfPCell cell = new PdfPCell(new Phrase(c));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            double inventoryValue = 0;

            for (Product p : products.values()) {

                double total = p.getTotalValue();
                inventoryValue += total;

                String status = (p.getQuantity() < LOW_STOCK_LIMIT)
                        ? "Low Stock"
                        : "Available";

                table.addCell(String.valueOf(p.getId()));
                table.addCell(p.getName());
                table.addCell(String.valueOf(p.getQuantity()));
                table.addCell(String.format("%.2f", p.getPrice()));
                table.addCell(String.format("%.2f", total));
                table.addCell(status);
            }

            document.add(table);

            document.add(new Paragraph("\n"));

            document.add(new Paragraph(
                    "Total Products : " + products.size(),
                    headingFont));

            document.add(new Paragraph(
                    "Total Inventory Value : ₹" + String.format("%.2f", inventoryValue),
                    headingFont));

            document.add(new Paragraph("\n"));

            // Low Stock Section
            document.add(new Paragraph("Low Stock Products", headingFont));

            boolean found = false;

            for (Product p : products.values()) {
                if (p.getQuantity() < LOW_STOCK_LIMIT) {
                    found = true;

                    document.add(new Paragraph(
                            p.getName() + " (Qty : " + p.getQuantity() + ")"
                    ));
                }
            }

            if (!found) {
                document.add(new Paragraph("No Low Stock Products"));
            }

            document.add(new Paragraph("\n"));

            Paragraph footer = new Paragraph("***** END OF REPORT *****", headingFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            System.out.println("PDF Generated Successfully");
            System.out.println("PDF Saved At: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}