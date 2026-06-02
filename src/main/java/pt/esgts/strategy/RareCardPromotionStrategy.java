package pt.esgts.strategy;

import pt.esgts.model.Card;

public class RareCardPromotionStrategy implements PricingStrategy {
    private final double discountRate;

    public RareCardPromotionStrategy(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double calculatePrice(Card card) {
        if ("Rare".equalsIgnoreCase(card.getRarity())) {
            return card.getPrice() * (1 - discountRate);
        }
        return card.getPrice();
    }
}
