import java.util.UUID;

public class Product {
    private final Integer id;
    private final String name;
    String category;
    private final Double price;

    public Product(String name, String category, Double price) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public Double getPrice() {
        return this.price;
    }
}

public class CartItem {
    final Product product;
    int quantity;

    Double finalPricePerUnit;
    Double finalPrice;
}

public class Cart {
    String cartID;
    List<CartItem> items;
    User user;

    OrderEstimate currentEstimate;
}

public class OrderEstimate {
    Double totalPrice;
    Double totalPriceAfterDiscount;
    List<String> promotionsApplied;
    Double deliveryCost;
    Double finalPrice;
}

interface ItemPricingRule {
    boolean isApplicable(CartItem item);
    double apply(CartItem item);
}

interface CartPricingRule {
    boolean isApplicable(Cart cart);
    double apply(Cart cart); 
}

interface DeliveryStrategy {
    boolean isApplicable(Cart cart);
    double apply(Cart cart);
}

// Example Item Rule: "Buy 2 get 10% off" for Electronics
class BulkElectronicsRule implements ItemPricingRule {
    public boolean isApplicable(CartItem item) {
        return item.product.category == Category.ELECTRONICS && item.quantity >= 2;
    }

    public double apply(CartItem item) {
        return item.product.basePrice * 0.90; // 10% discount
    }
}

// Example Cart Rule: "10% off if total > $100"
class HighValueCartRule implements CartPricingRule {
    public boolean isApplicable(Cart cart) {
        double currentTotal = cart.items.stream().mapToDouble(i -> i.totalItemPrice).sum();
        return currentTotal > 100.00;
    }

    public double apply(Cart cart) {
         // Logic to calculate 10% of subtotal
         return subtotal * 0.10; 
    }
}

// Example Delivery: "Prime Members Free, else standard"
class AmazonDeliveryStrategy implements DeliveryStrategy {
    public double calculateCost(Cart cart) {
        if (cart.user.isPrimeMember()) {
            return 0.0;
        }
        // Fallback to weight-based calculation
        double totalWeight = cart.items.stream().mapToDouble(i -> i.product.weight * i.quantity).sum();
        return totalWeight * 0.5; // $0.50 per kg
    }
}

public class PricingService {
    List<ItemPricingRule> itemRules;
    List<CartPricingRule> cartRules;
    DeliveryStrategy deliveryStrategy;

    public OrderEstimate calculateOrder(Cart cart) {
        OrderEstimate estimate = new OrderEstimate();
        double subTotal = 0;
        
        // STEP 1: Apply Item-Level Discounts (The "Product Based Discount")
        for (CartItem item : cart.items) {
            double bestPrice = item.product.basePrice;
            
            // Find the BEST rule for this item (or stack them, depending on requirements)
            for (ItemPricingRule rule : itemRules) {
                if (rule.isApplicable(item)) {
                    bestPrice = Math.min(bestPrice, rule.apply(item));
                }
            }
            
            item.finalPricePerUnit = bestPrice;
            item.totalItemPrice = bestPrice * item.quantity;
            subTotal += item.totalItemPrice;
        }
        
        estimate.subTotal = subTotal;

        // STEP 2: Apply Cart-Level Discounts (Coupons, Seasonal Sales)
        double cartDiscount = 0;
        for (CartPricingRule rule : cartRules) {
            if (rule.isApplicable(cart)) {
                cartDiscount += rule.apply(cart);
            }
        }
        estimate.totalDiscount = cartDiscount;

        // STEP 3: Calculate Delivery
        // Logic changes based on Delivery Type selected by user
        estimate.deliveryCost = deliveryStrategy.calculateCost(cart);

        // STEP 4: Final Math
        estimate.finalAmount = (subTotal - cartDiscount) + estimate.deliveryCost;

        return estimate;
    }
}