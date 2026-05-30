package pt.esgts.factory;

import pt.esgts.model.Card;

public abstract class CardFactory {
    public abstract Card createCard(String name, String rarity, double price);

    public void logCreation(String name) {
        System.out.println("Criando carta: " + name);
    }
}
