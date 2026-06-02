package pt.esgts.strategy;

import pt.esgts.model.Card;

public interface PricingStrategy {
    double calculatePrice(Card card);
}
