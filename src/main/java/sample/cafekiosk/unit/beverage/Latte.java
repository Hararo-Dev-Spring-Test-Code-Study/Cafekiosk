package sample.cafekiosk.unit.beverage;

public class Latte implements Beverage {

    // 라떼, 4500원

    @Override
    public String getName() {
        return "라떼";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
