package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import maxbe.goldenmaster.junit.extension.GoldenMasterRun;
import maxbe.goldenmaster.junit.extension.GoldenMasterTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

@GoldenMasterTest
public class SupermarketTest {

    private FileWriter fileWriter;
    private ReceiptPrinter receiptPrinter = new ReceiptPrinter(40);

    private SupermarketCatalog catalog;
    private Teller teller;
    private ShoppingCart theCart;
    private Product toothbrush;
    private Product rice;
    private Product apples;
    private Product cherryTomatoes;

    @BeforeEach
    void setUp(File output, Integer index) throws IOException {
        fileWriter = new FileWriter(output);
        
        catalog = new FakeCatalog();
        teller = new Teller(catalog);
        theCart = new ShoppingCart();

        toothbrush = new Product("toothbrush", ProductUnit.Each);
        catalog.addProduct(toothbrush, 0.99);
        rice = new Product("rice", ProductUnit.Each);
        catalog.addProduct(rice, 2.99);
        apples = new Product("apples", ProductUnit.Kilo);
        catalog.addProduct(apples, 1.99);
        cherryTomatoes = new Product("cherry tomato box", ProductUnit.Each);
        catalog.addProduct(cherryTomatoes, 0.69);
    }

    @GoldenMasterRun(repetitions = 1)
    void an_empty_shopping_cart_should_cost_nothing() {
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void one_normal_item() {
        theCart.addItem(toothbrush);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void two_normal_items() {
        theCart.addItem(toothbrush);
        theCart.addItem(rice);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);

        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void buy_two_get_one_free() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void buy_two_get_one_free_but_insufficient_in_basket() {
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }
    @GoldenMasterRun(repetitions = 1)
    void buy_five_get_one_free() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(SpecialOfferType.ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void loose_weight_product() {
        theCart.addItemQuantity(apples, .5);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void percent_discount() {
        theCart.addItem(rice);
        teller.addSpecialOffer(SpecialOfferType.TenPercentDiscount, rice, 10.0);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void xForY_discount() {
        theCart.addItem(cherryTomatoes);
        theCart.addItem(cherryTomatoes);
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, cherryTomatoes,.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void xForY_discount_with_insufficient_in_basket() {
        theCart.addItem(cherryTomatoes);
        teller.addSpecialOffer(SpecialOfferType.TwoForAmount, cherryTomatoes,.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void FiveForY_discount() {
        theCart.addItemQuantity(apples, 5);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples,6.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void FiveForY_discount_withSix() {
        theCart.addItemQuantity(apples, 6);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples,5.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void FiveForY_discount_withSixteen() {
        theCart.addItemQuantity(apples, 16);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples,7.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
        writeOut(receiptPrinter.printReceipt(receipt));
    }

    @GoldenMasterRun(repetitions = 1)
    void FiveForY_discount_withFour() {
        theCart.addItemQuantity(apples, 4);
        teller.addSpecialOffer(SpecialOfferType.FiveForAmount, apples,8.99);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        
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
