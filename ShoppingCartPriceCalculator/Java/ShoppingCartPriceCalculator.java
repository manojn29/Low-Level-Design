public class Product {
  private int id;
  private double price;
  private Category category;
  private String name;

  // Getters and setters
  
}

interface CartItemPromotion {
  public boolean isApplicable(CartItem cartItem);
  public void apply(CartItem cartItem);
}

public class BulkElectronicsPromotion extends CartItemPromotion {
  public boolean isApplicable(CartItem cartItem) {
    if (cartItem == null) return false;
    if (cartItem.getQuantity() < 1) return false;
    if (cartItem.getProduct().getCategory().equalsIgnoreCase("Electronics")) return true;
  }

  public void apply(CartItem cartItem) {
    FinalPrice final price = cartItem.getFinalPrice();
    double basicPrice = price.getBasicPrice();
    price.setBasicPrice(basicPrice * 0.5);
    return;
  }
}

interface CartPromotion {
  public boolean isApplicable(Cart cart);
  public void apply(Cart cart);
}

public class DeliveryPriceReduction extends CartPromotion {
  public boolean isApplicable(Cart cart){
    if (cart.getTotalPrice() >= 5000.00) return false;
    return false;
  }

  public void apply(Cart cart) {
    if (cart.getTotalPrice >= 5000.00) {
      FinalPrice finalPrice = cart.getFinalPrice();
      finalPrice.setDeliveryFee(0.0);
    }
  }
}

public class CartItem {
  private Product product;
  private int quantity;
  private FinalPrice finalPrice;

  // Getters and setters
}

public class FinalPrice {
  private double basicPrice;
  private double deliveryFee;
  private double totalPrice;

  public Double getTotal() {
    return this.totalPrice;
  }

  // Other getters and setters
}

public class Cart {
  private List<CartItem> cartItems;
  private FinalPrice totalPrice;
  
  // Getters and setters
}

class ShoppingCartPriceCalculator {
  List<CartItemPromotion> cartItemPromotion = new ArrayList<>();
  List<CartPromotion> cartPromotions = new ArrayList<>();
  public double finalCartPrice;
  
  public double calculatePrice(Cart cart) {
    List<CartItem> cartItems = cart.getCartItems();
    for (CartItem item: cartItems) {
      for (CartItemPromotion promotion: cartItemPromotion) {
        if (promotion.isApplicable(item)) promotion.apply(item);
        finalCartPrice += item.getTotalPrice().getTotal();
      }
    }

    for (CartPromotion promotion: cartPromotion) {
      if (promotion.isApplicable(cart)) promotion.apply(cart);
      finalPrice += cart.getTotalPrice().getTotal();
    }

    return finalCartPrice;
  }
}
