package pt.esgts.model;

public class MagicCard extends AbstractCard {
    public MagicCard(String name, String rarity, double basePrice) {
        super(name, rarity, basePrice);
    }

    @Override
    public String getGameType() {
        return "Magic: The Gathering";
    }
}
