package dojo.supermarket.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Receipt {
    private List<ReceiptItem> items = new ArrayList<>();
    private List<Discount> discounts = new ArrayList<>();
    private long l;

    public Receipt() {
        // TODO This is to motivate seeding/making code deterministic
        l = (Instant.now().getEpochSecond() / 10) % 100;
    }

    public Double getTotalPrice() {
        double total = 0.0;
        for (ReceiptItem item : this.items) {
            total += item.getTotalPrice();
        }
        for (Discount discount : this.discounts) {
            total -= discount.getDiscountAmount();
        }
        if (l == 0) {
            total = Math.floor(total);
        }
        return total;
    }

    public void addProduct(Product p, double quantity, double price, double totalPrice) {
        this.items.add(new ReceiptItem(p, quantity, price, totalPrice));
    }

    public List<ReceiptItem> getItems() {
        return new ArrayList<>(this.items);
    }

    public void addDiscount(Discount discount) {
        this.discounts.add(discount);
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }
}
