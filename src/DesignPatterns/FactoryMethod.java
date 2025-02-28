package DesignPatterns;

public class FactoryMethod {
    public static void main(String[] args) {
        Phone phone = PhoneStore.getPhone("Apple", "iphone 14");
        System.out.println(phone.getDescription());
        Phone phone1 = PhoneStore.getPhone("Samsung", "s23");
        System.out.println(phone1.getDescription());
    }
}

class PhoneStore {
    public static Phone getPhone(String brand, String model) {
        if(brand.equals("Apple")) {
            return new Apple().getPhone(model);
        } else if(brand.equals("Samsung")) {
            return new Samsung().getPhone(model);
        } else return new NullPhone();
    }
}

class Phone {
    private final String description;

    Phone(String model) {
        this.description = model;
    }

    public String getDescription() {
        return this.description;
    }
}

class Apple {
    public Phone getPhone(String model) {
        if(model.equals("iphone 14")) return new Phone(model);
        else if(model.equals("iphone 14 pro max")) return new Phone(model);
        return new NullPhone();
    }
}

class Samsung {
    public Phone getPhone(String model) {
        if(model.equals("s23")) return new Phone(model);
        return new NullPhone();
    }
}

class NullPhone extends Phone {
    public NullPhone() {
        super("invalid model");
    }

}