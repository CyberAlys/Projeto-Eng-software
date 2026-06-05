package pt.esgts.service;

import pt.esgts.model.Card;
import pt.esgts.strategy.PricingStrategy;
import pt.esgts.strategy.StandardPricingStrategy;

import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private final List<Card> inventory = new ArrayList<>();
    private PricingStrategy pricingStrategy;

    public ShopService() {
        this.pricingStrategy = new StandardPricingStrategy();
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void addCard(Card card) {
        inventory.add(card);
    }

    public List<Card> getInventory() {
        return new ArrayList<>(inventory);
    }

    public double calculateTotalValue() {
        return inventory.stream()
                .mapToDouble(card -> pricingStrategy.calculatePrice(card))
                .sum();
    }
}
