package pt.esgts;

import pt.esgts.factory.CardFactory;
import pt.esgts.factory.MagicCardFactory;
import pt.esgts.factory.PokemonCardFactory;
import pt.esgts.model.Card;
import pt.esgts.service.ShopService;
import pt.esgts.strategy.RareCardPromotionStrategy;
import pt.esgts.strategy.StandardPricingStrategy;

import java.util.Scanner;

public class Main {

    static final String RESET  = "\u001B[0m";
    static final String YELLOW = "\u001B[33m";
    static final String RED    = "\u001B[31m";
    static final String BLUE   = "\u001B[34m";
    static final String CYAN   = "\u001B[36m";
    static final String GREEN  = "\u001B[32m";
    static final String BOLD   = "\u001B[1m";
    static final String WHITE  = "\u001B[37m";

    static void cls() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
        } catch (Exception e) {
            // fallback: imprime várias linhas em branco
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static void main(String[] args) {
        ShopService shop = new ShopService();
        CardFactory pokemonFactory = new PokemonCardFactory();
        CardFactory magicFactory   = new MagicCardFactory();
        Scanner scanner = new Scanner(System.in);

        // Stock inicial
        shop.addCard(pokemonFactory.createCard("Pikachu",      "Common",  5.0));
        shop.addCard(pokemonFactory.createCard("Charizard",    "Rare",  150.0));
        shop.addCard(pokemonFactory.createCard("Mewtwo",       "Rare",  200.0));
        shop.addCard(magicFactory.createCard("Black Lotus",    "Rare", 2000.0));
        shop.addCard(magicFactory.createCard("Lightning Bolt", "Common",  3.5));

        boolean running = true;
        while (running) {
            cls();
            printHeader();
            printMainMenu();
            System.out.print(YELLOW + "  Pokédex ► " + RESET);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> { cls(); menuInventory(shop);                          pausar(scanner); }
                case "2" -> { cls(); menuAddCard(shop, pokemonFactory, magicFactory, scanner); pausar(scanner); }
                case "3" -> { cls(); menuPricing(shop, scanner);                   pausar(scanner); }
                case "4" -> { cls(); menuTotals(shop);                             pausar(scanner); }
                case "0" -> { cls(); printGoodbye(); running = false; }
                default  -> { cls(); printError("Opção inválida! Tenta outra vez..."); pausar(scanner); }
            }
        }
        scanner.close();
    }

    // ─────────────────────────────────────────────
    //  SUB-MENUS
    // ─────────────────────────────────────────────

    static void menuInventory(ShopService shop) {
        printSectionHeader("INVENTÁRIO DA LOJA");
        var inv = shop.getInventory();
        if (inv.isEmpty()) {
            System.out.println(RED + "  Sem cartas em stock!" + RESET);
            return;
        }
        System.out.printf(BOLD + "  %-4s %-22s %-10s %-22s %s%n" + RESET,
                "#", "Nome", "Raridade", "Jogo", "Preço");
        System.out.println("  " + "─".repeat(70));
        int i = 1;
        for (Card c : inv) {
            String rarColor = c.getRarity().equalsIgnoreCase("Rare") ? YELLOW : WHITE;
            System.out.printf("  %-4d " + rarColor + "%-22s" + RESET + " %-10s %-22s " + GREEN + "$%-8.2f" + RESET + "%n",
                    i++, c.getName(), c.getRarity(), c.getGameType(), c.getPrice());
        }
        System.out.println("  " + "─".repeat(70));
        System.out.println(CYAN + "  Total de cartas: " + inv.size() + RESET);
    }

    static void menuAddCard(ShopService shop,
                            CardFactory pokemonFactory,
                            CardFactory magicFactory,
                            Scanner scanner) {
        printSectionHeader("ADICIONAR CARTA");
        System.out.println(CYAN + "  Tipo de carta:" + RESET);
        System.out.println("  [1] Pokémon");
        System.out.println("  [2] Magic: The Gathering");
        System.out.println("  [0] Voltar");
        System.out.print(YELLOW + "  ► " + RESET);
        String tipo = scanner.nextLine().trim();

        if (tipo.equals("0")) return;
        if (!tipo.equals("1") && !tipo.equals("2")) {
            printError("Tipo inválido!");
            return;
        }

        System.out.print(CYAN + "\n  Nome da carta: " + RESET);
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) { printError("Nome não pode ser vazio!"); return; }

        System.out.println(CYAN + "\n  Raridade:" + RESET);
        System.out.println("  [1] Common");
        System.out.println("  [2] Rare");
        System.out.print(YELLOW + "  ► " + RESET);
        String rarOpt = scanner.nextLine().trim();
        String raridade = rarOpt.equals("2") ? "Rare" : "Common";

        System.out.print(CYAN + "\n  Preço ($): " + RESET);
        double preco;
        try {
            preco = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            if (preco < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            printError("Preço inválido!");
            return;
        }

        Card nova = tipo.equals("1")
                ? pokemonFactory.createCard(nome, raridade, preco)
                : magicFactory.createCard(nome, raridade, preco);
        shop.addCard(nova);

        System.out.println(GREEN + "\n  ✔ Carta \"" + nova.getName() + "\" adicionada com sucesso!" + RESET);
    }

    static void menuPricing(ShopService shop, Scanner scanner) {
        printSectionHeader("ESTRATÉGIA DE PREÇOS");
        System.out.println(CYAN + "  Escolhe uma estratégia:" + RESET);
        System.out.println("  [1] Preço Padrão (sem desconto)");
        System.out.println("  [2] Promoção Raras – 10%");
        System.out.println("  [3] Promoção Raras – 20%");
        System.out.println("  [4] Promoção Raras – 50%");
        System.out.println("  [0] Voltar");
        System.out.print(YELLOW + "  ► " + RESET);
        String opt = scanner.nextLine().trim();

        switch (opt) {
            case "1" -> { shop.setPricingStrategy(new StandardPricingStrategy());
                          System.out.println(GREEN + "\n  ✔ Estratégia: Preço Padrão ativada." + RESET); }
            case "2" -> { shop.setPricingStrategy(new RareCardPromotionStrategy(0.10));
                          System.out.println(GREEN + "\n  ✔ Promoção 10% nas Rares ativada." + RESET); }
            case "3" -> { shop.setPricingStrategy(new RareCardPromotionStrategy(0.20));
                          System.out.println(GREEN + "\n  ✔ Promoção 20% nas Rares ativada." + RESET); }
            case "4" -> { shop.setPricingStrategy(new RareCardPromotionStrategy(0.50));
                          System.out.println(GREEN + "\n  ✔ Promoção 50% nas Rares ativada." + RESET); }
            case "0" -> { return; }
            default  -> printError("Opção inválida!");
        }
    }

    static void menuTotals(ShopService shop) {
        printSectionHeader("VALOR TOTAL DO INVENTÁRIO");
        double total = shop.calculateTotalValue();
        System.out.println();
        System.out.println("  " + "═".repeat(40));
        System.out.printf("  " + BOLD + YELLOW + "  TOTAL: $%.2f%n" + RESET, total);
        System.out.println("  " + "═".repeat(40));
    }

    // ─────────────────────────────────────────────
    //  RENDER HELPERS
    // ─────────────────────────────────────────────

    static void pausar(Scanner scanner) {
        System.out.print(CYAN + "\n  [ Prima ENTER para voltar ao menu... ]" + RESET);
        scanner.nextLine();
    }

    static void printHeader() {
        System.out.println(YELLOW + "  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║                                                  ║");
        System.out.println("  ║   ____  ____  __  __ _____  _    _  _   _       ║");
        System.out.println("  ║  |  _ \\/ __ \\|  \\/  | ____|| \\  / || \\ | |      ║");
        System.out.println("  ║  | |_) | |  | | \\/ | |___ | \\\\// |  \\| |      ║");
        System.out.println("  ║  |  __/| |  | | |\\/| |  __|| /\\\\ | .   |      ║");
        System.out.println("  ║  |_|   \\____/|_|  |_|_____||_/  \\_|_|\\_|      ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ║" + RED + "        ░░  T C G  S H O P  ░░" + YELLOW + "                   ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ║" + WHITE + "        ⚡  Gotta trade 'em all!  ⚡" + YELLOW + "               ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void printMainMenu() {
        System.out.println(CYAN + "  ┌─────────────────────────────────┐");
        System.out.println("  │         MENU PRINCIPAL          │");
        System.out.println("  ├─────────────────────────────────┤");
        System.out.println("  │  [1]  Ver Inventario            │");
        System.out.println("  │  [2]  Adicionar Carta           │");
        System.out.println("  │  [3]  Estrategia de Precos      │");
        System.out.println("  │  [4]  Ver Valor Total           │");
        System.out.println("  │  [0]  Sair                      │");
        System.out.println("  └─────────────────────────────────┘" + RESET);
        System.out.println();
    }

    static void printSectionHeader(String title) {
        System.out.println(BOLD + BLUE + "\n  ═══════════════════════════════════════");
        System.out.printf("     %s%n", title);
        System.out.println("  ═══════════════════════════════════════" + RESET);
        System.out.println();
    }

    static void printGoodbye() {
        System.out.println(YELLOW + "\n  ╔══════════════════════════════════╗");
        System.out.println("  ║   Ate a proxima, Treinador!    ║");
        System.out.println("  ║                                ║");
        System.out.println("  ║      ~~  Ash Ketchum  ~~       ║");
        System.out.println("  ╚══════════════════════════════════╝" + RESET);
        System.out.println();
    }

    static void printError(String msg) {
        System.out.println(RED + "\n  [!] " + msg + RESET);
    }
}
