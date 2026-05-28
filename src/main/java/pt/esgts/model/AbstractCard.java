package pt.esgts.model;

public abstract class AbstractCard implements Card {
    private final String name;
    private final String rarity;
    private final double basePrice;

    protected AbstractCard(String name, String rarity, double basePrice) {
        this.name = name;
        this.rarity = rarity;
        this.basePrice = basePrice;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getRarity() {
        return rarity;
    }

    @Override
    public double getPrice() {
        return basePrice;
    }
}
