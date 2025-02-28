package DesignPatterns;

public class DecoratorPattern {
    public static void main(String[] args) {
        BasePizza modifiedFarmhouse = new extraCheeze(new mashroom(new farmhouse()));
        BasePizza modifiedVegDelight = new extraCheeze(new extraCheeze(new extraCheeze(new vegDelight())));
        System.out.println(modifiedVegDelight.price());
        System.out.println(modifiedFarmhouse.price());
    }
}

abstract class BasePizza {
    abstract int price();
}

abstract class ToppingsDecorator extends BasePizza {

}

class vegDelight extends BasePizza {
    @Override
    int price() {
        return 150;
    }
}

class farmhouse extends BasePizza {
    @Override
    int price() {
        return 200;
    }
}

class extraCheeze extends ToppingsDecorator {

    BasePizza basePizza;

    extraCheeze(BasePizza basePizza) {
        this.basePizza = basePizza;
    }
    @Override
    int price() {
        return this.basePizza.price() + 50;
    }
}

class mashroom extends ToppingsDecorator {
    BasePizza basePizza;

    mashroom(BasePizza basePizza) {
        this.basePizza = basePizza;
    }
    @Override
    int price() {
        return this.basePizza.price() + 25;
    }
}
