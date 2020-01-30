package dojo.supermarket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import dojo.supermarket.model.Discount;
import dojo.supermarket.model.Product;
import dojo.supermarket.model.ProductUnit;
import dojo.supermarket.model.Receipt;
import maxbe.goldenmaster.junit.extension.GoldenMasterRun;
import maxbe.goldenmaster.junit.extension.GoldenMasterTest;

@GoldenMasterTest
public class ReceiptPrinterTest {

    private Product toothbrush = new Product("toothbrush", ProductUnit.Each);
    private Product apples = new Product("apples", ProductUnit.Kilo);
    private Receipt receipt = new Receipt();

    private FileWriter fileWriter;
    private ReceiptPrinter receiptPrinter;

    @BeforeEach
    void setUp(File outputFile, Integer index) throws Exception {
        fileWriter = new FileWriter(outputFile);
        receiptPrinter = new ReceiptPrinter(40);
    }

    @GoldenMasterRun(repetitions = 1)
    public void oneLineItem() {
        receipt.addProduct(toothbrush, 1, 0.99, 0.99);

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    public void quantityTwo() {
        receipt.addProduct(toothbrush, 2, 0.99, 0.99 * 2);

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    public void looseWeight() {
        receipt.addProduct(apples, 2.3, 1.99, 1.99 * 2.3);

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    public void total() {
        receipt.addProduct(toothbrush, 1, 0.99, 2 * 0.99);
        receipt.addProduct(apples, 0.75, 1.99, 1.99 * 0.75);

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    public void discounts() {
        receipt.addDiscount(new Discount(apples, "3 for 2", 0.99));

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    public void printWholeReceipt() {
        receipt.addProduct(toothbrush, 1, 0.99, 0.99);
        receipt.addProduct(toothbrush, 2, 0.99, 2 * 0.99);
        receipt.addProduct(apples, 0.75, 1.99, 1.99 * 0.75);
        receipt.addDiscount(new Discount(toothbrush, "3 for 2", 0.99));

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    private void writeOut(String receiptPrint) {
        try {
            this.fileWriter.append(receiptPrint);
            this.fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
