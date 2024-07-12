import java.io.Serializable;

public class Game implements Serializable {
    private String name;
    private double price;
    private double salePrice;

    public Game(String name, double price, double salePrice) {
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
    }

    public String toString() {
        String result = name + ": $" + salePrice;
        if (getDiscountPercentage() > 0) {
            result += " (";
            result += String.format("%.2f", getDiscountPercentage()) + "% off)";
        }
        return result;
    }
    
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public double getDiscountPercentage() {
        return ((price - salePrice) / price) * 100.00;
    }

    
}
