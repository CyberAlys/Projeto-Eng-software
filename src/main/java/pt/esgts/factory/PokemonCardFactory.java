package pt.esgts.factory;

import pt.esgts.model.Card;
import pt.esgts.model.PokemonCard;

public class PokemonCardFactory extends CardFactory {
    @Override
    public Card createCard(String name, String rarity, double price) {
        logCreation(name);
        return new PokemonCard(name, rarity, price);
    }
}
