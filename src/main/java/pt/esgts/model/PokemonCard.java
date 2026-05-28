package pt.esgts.model;

public class PokemonCard extends AbstractCard {
    public PokemonCard(String name, String rarity, double basePrice) {
        super(name, rarity, basePrice);
    }

    @Override
    public String getGameType() {
        return "Pokemon";
    }
}
