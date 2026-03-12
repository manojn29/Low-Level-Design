import java.util.UUID;

public enum Category {
    ELECTRONIC,
    CLOTHING,
    FOOTWEAR
}

class Product {
    private String id;
    private String name;
    private double baserice;
    private Category category;

    public Product(String name, Category category) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(String name) {
        return this.name;
    }
}

public class CartItem {
    private Product product;
    private int quantity;
}

public class Price {
    double subtotal;
    double discount;
    double deliveryFee;
    double totalPrice;
}

public class Cart {
    private List<CartItem> items;
    private Price price;
}

interface IItemPromotions {
    public double calculateDiscount(CartItem item);
}

interface ICartPromotions {
    public double calculateDiscount(double price);
}

public class ElectronicsItemDiscount implements IItemPromotions {
    @Override
    public double calculateDiscount(CartItem item) {
        if (item.getProduct().getCategory() == Category.ELECTRONIC && item.getQuantity() >= 1) {
            double price = item.getProduct().getBasePrice();
            return price * item.getQuantity() * 0.50;
        }
    }
}

public class NoDeliveryFee implements ICartPromotions {
    @Override
    private double THRESHOLD;

    public NoDeliveryFee(double threshold) {
        this.THRESHOLD = threshold;
    }

    public double calculateDiscount(double price) {
        if (price >= this.THRESHOLD) {
            return 0.0;
        }
    }
}

class ShoppingCartPractice4 {
    List<IItemPromotions> itemPromotions = new ArrayList<>();
    List<ICartPromotions> cartPromotions = new ArrayList<>();
    
    public void addItemPromotions(List<IItemPromotions> promotions) {
        this.itemPromotions.extend(promotions);
    }

    public void addCartPromotions(List<IItemPromotions> promotions) {
        this.cartPromotions.extend(promotions);
    }

    public double calculatePrice(Cart cart) {
        
    }

}