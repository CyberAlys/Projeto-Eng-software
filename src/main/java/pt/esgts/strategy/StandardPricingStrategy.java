package pt.esgts.strategy;

import pt.esgts.model.Card;

public class StandardPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Card card) {
        return card.getPrice();
    }
}
