import java.util.Random;
import java.util.Scanner;

public class bestiari {
    static Jugador jugadorAleatori(int lvl) {
        Random r = new Random();
        if (lvl < 1) lvl = 1;
        String nom = nomAleatori(lvl).toUpperCase();
        String[] tipus = {"M","S","C","A","E"};
        int index = r.nextInt(tipus.length);
        String tip = tipus[index];
        return creaJugador(nom,tip,lvl);
    }

    private static String nomAleatori(int lvl) {
        Random r = new Random();
        String[] nom = new String[10];
        if (lvl == 1) {
            nom = new String[]{"Laureta", "Marcelí", "Tomaset", "Joanet", "Sioneta", "Raulet", "Jordiet", "Marteta", "Jaumet", "Miquelet"};
        } else if (lvl == 2) {
            nom = new String[]{"Carles", "Roxanne", "Geroni", "Tomás", "Òscar", "Lluís", "Margalida", "Isabel", "Joan", "Francesc"};
        }  else if (lvl == 3) {
            nom = new String[]{"Hera", "Hefesto", "Zeus", "Afrodita", "Ares", "Hestia", "Atenea", "Poseidon", "Apolo", "Artemisa"};
        } else if (lvl == 4) {
            nom = new String[]{"Kotal Kahn", "Kenshi", "Blaze", "Sareena", "Scorpion", "Sub-Zero", "Raiden", "Sonya Blade", "Chameleon", "Kollector"};
        } else {
            nom = new String[]{"DIO", "Pere", "Manu", "Marc", "Dani", "Pablo", "Santi", "InteliJ", "VSCode", "PacketTracer"};
        }
        int index = r.nextInt(nom.length);
        return nom[index];
    }

    static Jugador triaJugador(Border border) {
        Scanner scanner = new Scanner(System.in);
        String nom = "";
        String tip = "";
        while (nom.length() <= 2 || nom.length() >= 10) {
            Screen.clear();
            Combat.mostraTriaNom(25,5,border);
            Screen.show();
            nom = scanner.nextLine().toUpperCase();
            if (nom.length() <= 2) {
                Screen.clear();
                Combat.mostraErrorNomCurt(22,5,border);
                Combat.mostraContinuar(25,21,"continuar",false,border);
                Screen.show();
                scanner.nextLine();
            } else if (nom.length() >= 10) {
                Screen.clear();
                Combat.mostraErrorNomLlarg(22,5,border);
                Combat.mostraContinuar(25,21,"continuar",false,border);
                Screen.show();
                scanner.nextLine();
            }
        }
        while (tip.equals("")) {
            Screen.clear();
            Combat.mostraTriaTipus(20,5,border);
            Screen.show();
            tip = scanner.nextLine().toUpperCase();
            if (!tip.equals("M") && !tip.equals("S") && !tip.equals("C") && !tip.equals("E") && !tip.equals("A"))  {
                Screen.clear();
                Combat.mostraErrorTipus(25,5,border);
                Combat.mostraContinuar(25,21,"continuar",false,border);
                Screen.show();
                tip = "";
                scanner.nextLine();
            }
        }
        return creaJugador(nom,tip,1);
    }

    private static Jugador creaJugador(String nom, String tip,int nivell) {
        Jugador jugador = new Jugador();
        jugador.nom = nom;
        jugador.tipus = tip;
        if (tip.equals("M")) {
            // Mag
            jugador.puntsPujarNivell = 4;

            jugador.bonificacioAtac = 4;
            jugador.bonificacioDefensa = 1;
            jugador.bonificacioVida = 3;

            jugador.capAtacMaxima = 3 + (jugador.bonificacioAtac * (nivell - 1));
            jugador.capDefensaMaxima = 7 + (jugador.bonificacioDefensa * (nivell - 1));
            jugador.puntsDeVidaMaxims = 10 + (jugador.bonificacioVida * (nivell - 1));
        } else if (tip.equals("S")) {
            // Orc
            jugador.puntsPujarNivell = 4;

            jugador.bonificacioAtac = 1;
            jugador.bonificacioDefensa = 3;
            jugador.bonificacioVida = 3;

            jugador.capAtacMaxima = 7 + (jugador.bonificacioAtac * (nivell - 1));
            jugador.capDefensaMaxima = 3 + (jugador.bonificacioDefensa * (nivell - 1));
            jugador.puntsDeVidaMaxims = 10 + (jugador.bonificacioVida * (nivell - 1));
        } else if (tip.equals("C")) {
            // Cavaller
            jugador.puntsPujarNivell = 4;

            jugador.bonificacioAtac = 2;
            jugador.bonificacioDefensa = 2;
            jugador.bonificacioVida = 3;

            jugador.capAtacMaxima = 5 + (jugador.bonificacioAtac * (nivell - 1));
            jugador.capDefensaMaxima = 5 + (jugador.bonificacioDefensa * (nivell - 1));
            jugador.puntsDeVidaMaxims = 10 + (jugador.bonificacioVida * (nivell - 1));
        } else if (tip.equals("A")) {
            // Aprenent
            jugador.puntsPujarNivell = 4;

            jugador.bonificacioAtac = 4;
            jugador.bonificacioDefensa = 1;
            jugador.bonificacioVida = 3;

            jugador.capAtacMaxima = 7 + (jugador.bonificacioAtac * (nivell - 1));
            jugador.capDefensaMaxima = 3 + (jugador.bonificacioDefensa * (nivell - 1));
            jugador.puntsDeVidaMaxims = 10 + (jugador.bonificacioVida * (nivell - 1));
        } else if (tip.equals("E")) {
            // Escuder
            jugador.puntsPujarNivell = 4;

            jugador.bonificacioAtac = 1;
            jugador.bonificacioDefensa = 4;
            jugador.bonificacioVida = 3;

            jugador.capAtacMaxima = 3 + (jugador.bonificacioAtac * (nivell - 1));
            jugador.capDefensaMaxima = 7 + (jugador.bonificacioDefensa * (nivell - 1));
            jugador.puntsDeVidaMaxims = 10 + (jugador.bonificacioVida * (nivell - 1));
        } else {
            throw new RuntimeException("Tipus de jugador no reconegut");
        }
        jugador.nivell = nivell;
        jugador.punts = (nivell - 1) * jugador.puntsPujarNivell;
        jugador.puntsDeVida = jugador.puntsDeVidaMaxims;
        jugador.capAtac = jugador.capAtacMaxima;
        jugador.capDefensa = jugador.capDefensaMaxima;
        return jugador;
    }

}
