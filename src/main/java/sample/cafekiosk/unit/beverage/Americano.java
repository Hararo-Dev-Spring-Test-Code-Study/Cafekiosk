package sample.cafekiosk.unit.beverage;

public class Americano implements Beverage {

    // 아메리카노, 4000원
    @Override
    public String getName() {
        return "Americano";
    }

    @Override
    public int getPrice() {
        return 4000;
    }

}
