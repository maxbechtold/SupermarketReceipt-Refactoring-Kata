package dojo.supermarket.model;

import static dojo.supermarket.model.SpecialOfferType.FiveForAmount;
import static dojo.supermarket.model.SpecialOfferType.TenPercentDiscount;
import static dojo.supermarket.model.SpecialOfferType.ThreeForTwo;
import static dojo.supermarket.model.SpecialOfferType.TwoForAmount;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.BeforeEach;

import dojo.supermarket.ReceiptPrinter;
import maxbe.goldenmaster.junit.extension.GoldenMasterRun;
import maxbe.goldenmaster.junit.extension.GoldenMasterTest;

@GoldenMasterTest
public class SupermarketTest {
    
    private FileWriter fileWriter;
    private ReceiptPrinter receiptPrinter = new ReceiptPrinter(40);

    private FakeCatalog catalog;
    private Teller teller;
    private ShoppingCart theCart;
    private Product toothbrush;
    private Product rice;
    private Product apples;
    private Product cherryTomatoes;

    @BeforeEach
    void setUp() {
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

    interface SpecialOfferFactory extends BiConsumer<Teller, SupermarketCatalog> {
        default void enable(Teller t, SupermarketCatalog c) {
            this.accept(t, c);
        }
    };

    private List<SpecialOfferFactory> specialOffers;
    private Random random;

    @BeforeEach
    void setUp(File output, Integer index) throws Exception {
        fileWriter = new FileWriter(output);
        random = new Random(index);
        specialOffers = Arrays.asList(
                (teller, catalog) -> teller.addSpecialOffer(ThreeForTwo, toothbrush, catalog.getUnitPrice(toothbrush)),
                (teller, catalog) -> teller.addSpecialOffer(TenPercentDiscount, rice, 10.0),
                (teller, catalog) -> teller.addSpecialOffer(TwoForAmount, cherryTomatoes, .99),
                (teller, catalog) -> teller.addSpecialOffer(FiveForAmount, apples, 6.99)
        );
    }

    @GoldenMasterRun(repetitions = 20)
    void goldenmaster_oneSpecialOffer_oneCartItem_amount1() {
        int randomIndex = random.nextInt(specialOffers.size());
        specialOffers.get(randomIndex).enable(teller, catalog);
        
        Product product = catalog.getRandomProduct(random);

        theCart.addItem(product);
        
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        writeOut(receiptPrinter.printReceipt(receipt));
    }
    
    @GoldenMasterRun(repetitions = 20)
    void goldenmaster_oneSpecialOffer_oneCartItem_amount2() {
        int randomIndex = random.nextInt(specialOffers.size());
        specialOffers.get(randomIndex).enable(teller, catalog);
        
        Product product = catalog.getRandomProduct(random);

        int amount = 2;
        for (int i = 0; i < amount; i++) {
            theCart.addItem(product);
        }
        
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
