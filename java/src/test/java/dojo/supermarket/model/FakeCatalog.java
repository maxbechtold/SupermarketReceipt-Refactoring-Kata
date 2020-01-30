package dojo.supermarket.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class FakeCatalog implements SupermarketCatalog {
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Double> prices = new HashMap<>();

    @Override
    public void addProduct(Product product, double price) {
        this.products.put(product.getName(), product);
        this.prices.put(product.getName(), price);
    }

    @Override
    public double getUnitPrice(Product p) {
        return this.prices.get(p.getName());
    }
    
    public Product getRandomProduct(Random random ) {
        Collection<Product> products = this.products.values();
        Stream<Product> selectedStream = products.stream().skip(random.nextInt(products.size()));
        return selectedStream.findFirst().get();
    }
}
