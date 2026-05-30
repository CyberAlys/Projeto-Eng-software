package pt.esgts.factory;

import pt.esgts.model.Card;
import pt.esgts.model.MagicCard;

public class MagicCardFactory extends CardFactory {
    @Override
    public Card createCard(String name, String rarity, double price) {
        logCreation(name);
        return new MagicCard(name, rarity, price);
    }
}
