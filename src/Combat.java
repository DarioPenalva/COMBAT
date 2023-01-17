import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Combat {
    static final int NADVERSARIS = 10;
    static final int NCOMBATS = 5;
    static final int NTORNS = 5;

    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        /*
         Problema:
         Volem implementar un joc que serà del tipus CRPG (Computer Role Playing Game)
         Nom: COMBAT
         Tindrem Jugadors (Un contra l'altre, 1v1)
         Nom del jugador, tipus del jugador (Cavaller, Ogre, Mag, Orc, Assassí, ..)
         Nivell del jugador: indicador general de la capacitat de combat.
         Punts: Els que ha obtingut el jugador fins al moment.
         Punts de Vida: Energia del jugador.
         Màxim de punts de Vida: Màxim que es pot tenir de punts de vida.
         Capacitat d'Atac, Capacitat de Defensa.

         Joc de torns en el qual cada jugador té un torn.
         S'han de poder veure els atributs de cada jugador.
         Cada combat es divideix en un conjunt de rondes, cada ronda s'ha de triar quina estratègia es vol emprar.
         Al principi de cada ronda es mostra els atributs de cada jugador
         Llavors s'ha de triar entre quatre estratègies: Atac, Defensa, Engany, Maniobra.
         Cal veure cada jugador el grau d'èxit de cada estratègia.
         Atac o Engany, llença tantes monedes com capacitat d'atac.
         Defensa o Maniobra, llença tantes monedes com capacitat de defensa.

        Resolem el combat:
        - Res: No passa res.
        - Dany: el jugador perd una quantitat de punts de vida igual al grau d'èxit del contrincant.
        - Guarit: el jugador recupera uns punts de vida iguals a l'èxit d'aquest. (mai més que el màxim de pv)
        - Penalitzat: El jugador veu penalitzat el seu estat d'atac o defensa en tant de punts com el grau d'èxit del contrincant.(mai inferior a 1).

        L'atac o defensa perduts, es recuperen quan s'acaba el combat.

                                   J2
        ----------- | Atac          | Defensa         | Engany         | Maniobra
        Atac        | J1 & J2: Dany | J2: Guarit      | J2: Dany       | J2: Dany
     J  Defensa     | J1: Guarit    | J1 & J2: Guarit | J1: Dany x2    | J1: Penalitzat
     1  Engany      | J1: Dany      | J2: Dany x2     | J1 & J2: Dany  | J1: Penalitzat
        Maniobra    | J1: Dany      | J2: Penalitzat  | J2: Penalitzat | J1 & J2: Penalitzat
                */

        playSound("sounds/MortalKombat.wav");
        Border border = new Border();
        menuPrincipal(border);

    }

    // He trobat això a Stack Overflow i m'ha parescut molt guay, així que he afegit un toque extra quan obres el joc...
    public static void playSound(String soundFile) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        File f = new File("" + soundFile);
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }
    // Tot i que no m'agrada afegir codi que no entenc del tot bé, com a mínim he deduït com fer-ho servir, ja que no estava prou ben explicat.
    // ¡¡¡Apaga el volum si no vols que et molesti!!!

    private static Boolean nouCombat(Jugador jugador1, Jugador jugador2, Border border, int nCombat,Boolean nouPersonatge,Boolean dosJugadors) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Scanner scanner = new Scanner(System.in);
        boolean empatDosJugadors = true;
        boolean guanyes = false;
        boolean pujaNivell = false;
        int recordaNivell = jugador1.nivell;
        int[] victories = { 0, 0 };
        for (int ronda = 0; ronda < NCOMBATS; ronda++) {
            if (!dosJugadors) {
                if (recordaNivell != jugador1.nivell) {
                    pujaNivell = true;
                    recordaNivell = jugador1.nivell;
                }
            }
            Screen.clear();
            if (!dosJugadors) { // Mostra Informació 1 Jugador, amb detalls si escauen.
                mostraInfo(33,4,jugador1,border);
                mostraContinuar(25,21,"continuar",false, border);
                if (guanyes) mostraNovaPuntuacio(25,14,border,(jugador2.nivell+1)/2,pujaNivell,jugador1.nom);
                pujaNivell = false;
                guanyes = false;
                if (nouPersonatge) mostraNouPersonatge(24,14,border);
                nouPersonatge = false;
            } else { // Detalls mode 2 jugadors:
                mostraInfo(16,4,jugador1,border);
                mostraInfo(48,4,jugador2,border);
                mostraContinuar(25,21,"continuar",false, border);
                if (guanyes && !empatDosJugadors) {
                    mostraNovaPuntuacio(25,14,border,(jugador2.nivell+1)/2,pujaNivell,jugador1.nom);
                } else if (!guanyes && !empatDosJugadors) {
                    mostraNovaPuntuacio(25,14,border,(jugador1.nivell+1)/2,pujaNivell,jugador2.nom);
                }
            }
            Screen.show();
            scanner.nextLine();

            // Comença un dels NCOMBATS
            Screen.clear();
            mostraInfoNcombat(25,4,border,nCombat,ronda+1, dosJugadors);
            mostraContinuar(25,21,"combatre",false, border);
            Screen.show();
            scanner.nextLine();

            if (!dosJugadors) {
                guanyes = combat(jugador1,jugador2,border,victories,ronda+1,nCombat,false);
            } else {
                guanyes = combat(jugador1,jugador2,border,victories,ronda+1,nCombat,true);
                empatDosJugadors = jugador1.puntsDeVida == jugador2.puntsDeVida;
            }

            // Recupera els jugadors abans del següent combat
            jugador1.recuperacio();
            jugador2.recuperacio();
        }
        // Comprova el resultat per saber si es continuen les lluites
        Screen.clear();
        if (!dosJugadors) {
            if (victories[0] <= victories[1]) {
                missatgePerdreDefinitiu(18,4,border);
                mostraContinuar(25,21,"sortir",false, border);
                Screen.show();
                scanner.nextLine();
                return true;
            } else {
                mostraSeguentCombat(25,4,border);
                mostraContinuar(25,21,"continuar",false, border);
                Screen.show();
                scanner.nextLine();
                return false;
            }
        } else {
            String continuar = "";
            while (!continuar.equals("1") && !continuar.equals("2")) {
                Screen.clear();
                mostraContinuaDosJugadors(25,4,border);
                Screen.show();
                continuar = scanner.nextLine();
            }
            switch (continuar) {
                case "1":
                    return false;
                case "2":
                default:
            }
        }
        return true;
    }

    static void esborraPantalla() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    private static Boolean combat(Jugador jugador1, Jugador jugador2, Border border, int[] victories,int ronda,int nCombat, Boolean dosJugadors) {
        Scanner scanner = new Scanner(System.in);
        int torn = 1;
        while (!jugador1.haPerdut() && !jugador2.haPerdut()) {
            String estratJugador1 = "";
            String estratJugador2 = "";
            while (!estratJugador1.equals("A") && !estratJugador1.equals("D") && !estratJugador1.equals("E") && !estratJugador1.equals("M")) {
                // Mostra l'estat dels jugadors
                Screen.clear();
                mostraRondaNcombat(18,1,border,ronda,nCombat,torn);
                mostraInfo(18,4,jugador1,border);
                mostraInfo(48,4,jugador2,border);

                // Triar estratègia
                mostraTriaEstrategia(18,14,border,jugador1.nom);
                Screen.show();
                estratJugador1 = scanner.nextLine().toUpperCase();
            }
            if (!dosJugadors) {
                estratJugador2 = jugador2.triaEstrategiaAleatoria();
            } else {
                while (!estratJugador2.equals("A") && !estratJugador2.equals("D") && !estratJugador2.equals("E") && !estratJugador2.equals("M")) {
                    // Mostra l'estat dels jugadors
                    Screen.clear();
                    mostraRondaNcombat(18, 1, border, ronda, nCombat, torn);
                    mostraInfo(18, 4, jugador1, border);
                    mostraInfo(48, 4, jugador2, border);

                    // Triar estratègia
                    mostraTriaEstrategia(18, 14, border,jugador2.nom);
                    Screen.show();
                    estratJugador2 = scanner.nextLine().toUpperCase();
                }
            }

            // Tira monedes per punts d'èxit
            int exit1 = tiraSegonsEstrategia(jugador1, estratJugador1);
            int exit2 = tiraSegonsEstrategia(jugador2, estratJugador2);

            // Abans de Resolució
            Screen.clear();
            mostraRondaNcombat(18,1,border,ronda,nCombat,torn);
            mostraInfo(18,4,jugador1,border);
            mostraInfo(48,4,jugador2,border);
            mostraEstrategia(18,14,jugador1,jugador2,estratJugador1,estratJugador2,border);
            mostraExit(46,14,jugador1,jugador2,exit1,exit2,border);
            mostraContinuar(25,21,"resoldre",false,border);
            Screen.show();
            scanner.nextLine();

            // Resolució
            Screen.clear();
            resolCombat(jugador1, jugador2,estratJugador1,estratJugador2,exit1,exit2,border);

            mostraRondaNcombat(18,1,border,ronda,nCombat,torn);
            mostraInfo(18,4,jugador1,border);
            mostraInfo(48,4,jugador2,border);
            mostraContinuar(25,21,"continuar",false,border);
            Screen.show();
            scanner.nextLine();

            // Fi del torn
            if (torn >= NTORNS) { // Si s'acaba el màxim de torns:
                Screen.clear();
                mostraTempsAcabat(18,4,border);
                mostraContinuar(25,21,"conclusió",false,border);
                Screen.show();
                scanner.nextLine();
                if (jugador1.puntsDeVida > jugador2.puntsDeVida) { //Jug1 té més vida que Jug2. Guanya Jug1
                    jugador2.puntsDeVida = 0;
                } else if (jugador1.puntsDeVida < jugador2.puntsDeVida) { //Jug2 té més vida que Jug1. Guanya Jug2
                    jugador1.puntsDeVida = 0;
                } else { // Empat
                    jugador1.puntsDeVida = 0;
                    jugador2.puntsDeVida = 0;
                }
            }
            torn++;
        }
        Screen.clear();
        mostraContinuar(25,21,"continuar",false,border);
        if (jugador1.haPerdut() && !jugador2.haPerdut()) {
            victories[1]++;
            jugador2.guanyaPunts((jugador1.nivell+1)/2);
            missatgePerdre(18,4,border,jugador2,false,dosJugadors);
            Screen.show();
            scanner.nextLine();
            return false;
        }
        if (!jugador1.haPerdut() && jugador2.haPerdut()) {
            victories[0]++;
            jugador1.guanyaPunts((jugador2.nivell+1)/2);
            missatgePerdre(18,4,border,jugador1,true,dosJugadors);
            Screen.show();
            scanner.nextLine();
            return true;
        }
        if (jugador1.haPerdut() && jugador2.haPerdut()) {
            missatgeEmpat(18,4,border,jugador1,jugador2);
            Screen.show();
            scanner.nextLine();
            return false;
        }
        return false;
    }

    private static void resolCombat(Jugador jugador1, Jugador jugador2, String estratJugador1, String estratJugador2, int exit1, int exit2,Border border) {
        switch (estratJugador1) {
            case "A":
                switch (estratJugador2) {
                    case "A":
                        // 1A 2A
                        jugador1.dany(exit2);
                        jugador2.dany(exit1);
                        mostraDany(18, 14, jugador1, exit2, border);
                        mostraDany(18, 17, jugador2, exit1, border);
                        break;
                    case "D":
                        // 1A 2D
                        jugador2.guarit(exit2);
                        mostraGuarit(18, 14, jugador2, exit2, border);
                        break;
                    case "E":
                    case "M":
                        // 1A 2M
                        // 1A 2E
                        jugador2.dany(exit1);
                        mostraDany(18, 14, jugador2, exit1, border);
                        break;
                }
                break;
            case "D":
                switch (estratJugador2) {
                    case "A":
                        // 1D 2A
                        jugador1.guarit(exit1);
                        mostraGuarit(18, 14, jugador1, exit1, border);
                        break;
                    case "D":
                        // 1D 2D
                        jugador1.guarit(exit1);
                        jugador2.guarit(exit2);
                        mostraGuarit(18, 14, jugador1, exit1, border);
                        mostraGuarit(18, 17, jugador2, exit2, border);
                        break;
                    case "E":
                        // 1D 2E
                        jugador1.dany(exit2 * 2);
                        mostraDany(18, 14, jugador1, exit2 * 2, border);
                        break;
                    case "M":
                        // 1D 2M
                        jugador1.penalitza(18, 14, exit2, border);
                        break;
                }
                break;
            case "E":
                switch (estratJugador2) {
                    case "A":
                        // 1E 2A
                        jugador1.dany(exit2);
                        mostraDany(18, 14, jugador1, exit2, border);
                        break;
                    case "D":
                        // 1E 2D
                        jugador2.dany(exit1 * 2);
                        mostraDany(18, 14, jugador2, exit1 * 2, border);
                        break;
                    case "E":
                        // 1E 2E
                        jugador1.dany(exit2);
                        jugador2.dany(exit1);
                        mostraDany(18, 14, jugador1, exit2, border);
                        mostraDany(18, 17, jugador2, exit1, border);
                        break;
                    case "M":
                        // 1E 2M
                        jugador1.penalitza(18, 14, exit2, border);
                        break;
                }
                break;
            case "M":
                switch (estratJugador2) {
                    case "A":
                        // 1M 2A
                        jugador1.dany(exit2);
                        mostraDany(18, 14, jugador1, exit2, border);
                        break;
                    case "D":
                        // 1M 2D
                        jugador2.penalitza(18, 14, exit1, border);
                        break;
                    case "E":
                        // 1M 2E
                        jugador2.penalitza(18, 14, exit1, border);
                        break;
                    case "M":
                        // 1M 2M
                        jugador1.penalitza(18, 14, exit2, border);
                        jugador2.penalitza(18, 17, exit1, border);
                        break;
                }
                break;
        }
    }

     static void mostraPenalitzacio(int x, int y, String nom, int exit,Border border, String estatPenalitzat) {

         dibuixaQuadrat(x,y,55,2,false, border);

         Screen.print(4+x, 1+y, String.format("¡%s reb una penalització de -%d %s!",nom,exit,estatPenalitzat));
    }

    private static int tiraSegonsEstrategia(Jugador jugador, String estrategia) {
        if (estrategia.equals("A") || estrategia.equals("E")) {
            return Monedes.tira(jugador.capAtac);
        }
        return Monedes.tira(jugador.capDefensa);
    }

    private static void mostraDany(int x, int y, Jugador jugador, int exit,Border border) {

        dibuixaQuadrat(x,y,55,2,false, border);

        Screen.print(4+x, 1+y, String.format("¡%s reb %d de mal!",jugador.nom,exit));
    }
    private static void mostraGuarit(int x, int y, Jugador jugador, int exit,Border border) {

        dibuixaQuadrat(x,y,55,2,false, border);

        Screen.print(4+x, 1+y, String.format("¡%s recupera %d punts de vida!",jugador.nom,exit));
    }
    static void mostraNovaPuntuacio(int x, int y, Border border,int punts,Boolean pujaNivell,String nom) {
        dibuixaQuadrat(x,y,40,6,true, border);

        Screen.print(10+x, 1+y, "- AUGMENT DE PUNTS -",Screen.GREEN);
        Screen.print(13+x-(nom.length()/2), 3+y, String.format("¡Enhorabona %s!",nom));
        if (punts == 1) {
            Screen.print(4+x, 4+y, "¡Aquesta victòria t'atorga 1 punt!");
        } else {
            Screen.print(3+x, 4+y, String.format("¡Aquesta victoria t'atorga %d punts!",punts));
        }
        if (pujaNivell) Screen.print(2+x, 5+y, "¡Tens prou punts per pujar de nivell!");
    }

    private static void missatgeEmpat(int x, int y, Border border,Jugador jugador1, Jugador jugador2) {

        dibuixaQuadrat(x,y,54,5,true, border);

        Screen.print(21+x, 1+y, "- ¡EMPAT! -",Screen.RED);
        Screen.print(9+x, 3+y, "¡S'ha produït un resultat inesperat!");
        Screen.print(11+x-(((jugador1.nom.length() + jugador2.nom.length())/2)), 4+y, String.format("Tant %s, com %s perden aquesta ronda",jugador1.nom, jugador2.nom));
    }
    private static void missatgePerdre(int x, int y, Border border, Jugador jugador,Boolean victoria,Boolean dosJugadors) {

        dibuixaQuadrat(x,y,54,5,true, border);

        if (victoria && !dosJugadors) {
            Screen.print(20+x, 1+y, "- ¡VICTÒRIA! -",Screen.GREEN);
            Screen.print(10+x-(jugador.nom.length()/2), 3+y, String.format("¡%s ha guanyat aquest enfrontament!",jugador.nom));
        } else if (!dosJugadors) {
            Screen.print(20+x, 1+y, "- ..DERROTA.. -",Screen.RED);
            Screen.print(12+x-(jugador.nom.length()/2), 3+y, String.format("%s s'emporta aquest enfrontament",jugador.nom));
        } else {
            Screen.print(20+x, 1+y, "- ¡VICTÒRIA! -",Screen.GREEN);
            Screen.print(12+x-(jugador.nom.length()/2), 3+y, String.format("%s s'emporta aquest enfrontament",jugador.nom));
        }
    }
    private static void missatgePerdreDefinitiu(int x, int y, Border border) {

        dibuixaQuadrat(x,y,54,5,true, border);

        Screen.print(20+x, 1+y, "- ..FRACÀS.. -",Screen.RED);
        Screen.print(7+x, 3+y, "Has rebut una humiliació a nivell galàctic");
        Screen.print(8+x, 4+y, "És hora de que tornis al menú principal");
    }
    static void mostraRondaNcombat(int x, int y, Border border, int rondes, int nCombat, int torn) {

        dibuixaQuadrat(x,y,18,2,false, border);
        dibuixaQuadrat(x+19,y,17,2,false, border);
        dibuixaQuadrat(x+37,y,18,2,false, border);

        Screen.print(4+x, 1+y, String.format("COMBAT: %d ", nCombat));
        Screen.print(4+x+19, 1+y, String.format("RONDA: %d", rondes));
        Screen.print(3+x+37, 1+y, String.format("TORN: %d / %d", torn, NTORNS));
    }
    static void mostraTempsAcabat(int x, int y, Border border) {

        dibuixaQuadrat(x,y,54,5,true, border);

        Screen.print(18+x, 1+y, "- FI DE LA RONDA -",Screen.RED);
        Screen.print(16+x, 3+y, "¡S'ha acabat el temps!");
        Screen.print(9+x, 4+y, "Ara es decidirà qui és el guanyador");
    }
    static void mostraSeguentCombat(int x, int y, Border border) {

        dibuixaQuadrat(x,y,40,4,true, border);

        Screen.print(14+x, 1+y, "- VICTÒRIA -",Screen.GREEN);
        Screen.print(4+x, 3+y, "¡Has derrotat el teu adversari!");
    }
    static void mostraInfoNcombat(int x, int y, Border border,int combat,int ronda, Boolean dosJugadors) {

        dibuixaQuadrat(x,y,40,6,true, border);
        if (!dosJugadors) {
            if (ronda-1 == 0) {
                Screen.print(11+x, 1+y, "- NOU ADVERSARI -",Screen.RED);
                Screen.print(15+x, 3+y, String.format("Combat: %d",combat));
                Screen.print(15+x, 4+y, String.format("Ronda:  %d", ronda));
                Screen.print(5+x, 5+y, "¡Ha aparegut un nou adversari!");
            } else {
                Screen.print(10+x, 1+y, "- NOU ENFRONTAMENT -",Screen.RED);
                Screen.print(15+x, 3+y, String.format("Combat: %d",combat));
                Screen.print(15+x, 4+y, String.format("Ronda:  %d", ronda));
            }
        } else {
            if (ronda-1 == 0) {
                Screen.print(8+x, 1+y, "- PRIMER ENFRONTAMENT -",Screen.RED);
                Screen.print(15+x, 3+y, String.format("Combat: %d",combat));
                Screen.print(15+x, 4+y, String.format("Ronda:  %d", ronda));
                Screen.print(9+x, 5+y, "¡És hora de combatre!");
            } else {
                Screen.print(8+x, 1+y, "- PRÒXIM ENFRONTAMENT -",Screen.RED);
                Screen.print(15+x, 3+y, String.format("Combat: %d",combat));
                Screen.print(15+x, 4+y, String.format("Ronda:  %d", ronda));
            }
        }
    }
    static void mostraInfo(int x, int y,Jugador jugador, Border border) {

        dibuixaQuadrat(x,y,25,9,true, border);

        Screen.print(2+x, 1+y, "Nom:     ");
        Screen.print(2+x+9, 1+y, String.format("%s (%s)",jugador.nom, jugador.tipus));
        Screen.print(2+x, 3+y, "Nivell:  ");
        Screen.print(2+x+9, 3+y, String.format("%d",jugador.nivell));
        Screen.print(2+x, 4+y, "Punts:   ");
        Screen.print(2+x+9, 4+y, String.format("%d",jugador.punts));
        Screen.print(2+x, 5+y, "Vida:    ");
        Screen.print(2+x+9, 5+y,String.format("%s", dibuixaBarra(jugador.puntsDeVida,jugador.puntsDeVidaMaxims,'♥')),colorBarraVida(jugador.puntsDeVida,jugador.puntsDeVidaMaxims));
        Screen.print(2+x, 6+y, "Atac:    ");
        Screen.print(2+x+9, 6+y, String.format("%s",dibuixaBarra(jugador.capAtac,jugador.capAtacMaxima,'♦')),Screen.BLUE);
        Screen.print(2+x, 7+y, "Defensa: ");
        Screen.print(2+x+9, 7+y, String.format("%s",dibuixaBarra(jugador.capDefensa,jugador.capDefensaMaxima,'♠')),Screen.CYAN);
    }

    private static String colorBarraVida(int puntsActuals,int puntsMaxims) {
        int kokoros = (((puntsActuals * 120) / puntsMaxims) / 10);
        if (kokoros <= 8 && kokoros >= 4) return Screen.YELLOW;
        if (kokoros <= 3) return Screen.RED;
        return Screen.GREEN;
    }

    private static String dibuixaBarra(int puntsActuals,int puntsMaxims,char c) {
        int kokoros = (((puntsActuals * 120) / puntsMaxims) / 10);
        String barraVida = "";
        for (int i = 0; i < kokoros; i++) {
            barraVida += c;
        }
        return barraVida;
    }
    static void mostraTriaEstrategia(int x, int y, Border border,String nom) {

        dibuixaQuadrat(x,y,55,7,true, border);

        Screen.print(16+x-(nom.length()/2), 1+y, String.format("- %s: Tria una estratègia -",nom),Screen.RED);
        Screen.print(21+x, 3+y, "» (A)tac     «");
        Screen.print(18+x, 3+y, "⟅♦",Screen.BLUE);
        Screen.print(36+x, 3+y, "♦⟆",Screen.BLUE);
        Screen.print(21+x, 4+y, "» (D)efensa  «");
        Screen.print(18+x, 4+y, "⟅♠",Screen.CYAN);
        Screen.print(36+x, 4+y, "♠⟆",Screen.CYAN);
        Screen.print(21+x, 5+y, "» (E)ngany   «");
        Screen.print(18+x, 5+y, "⟅♦",Screen.BLUE);
        Screen.print(36+x, 5+y, "♦⟆",Screen.BLUE);
        Screen.print(21+x, 6+y, "» (M)aniobra «");
        Screen.print(18+x, 6+y, "⟅♠",Screen.CYAN);
        Screen.print(36+x, 6+y, "♠⟆",Screen.CYAN);
    }
    static void mostraEstrategia(int x, int y, Jugador jugador1, Jugador jugador2, String estratJugador1, String estratJugador2, Border border) {

        dibuixaQuadrat(x,y,27,5,true, border);
        String color = "";
        String color2 = "";
        if (estratJugador1.equals("A") || (estratJugador1.equals("E"))) {
            color = Screen.BLUE;
        } else {
            color = Screen.CYAN;
        }
        if (estratJugador2.equals("A") || (estratJugador2.equals("E"))) {
            color2 = Screen.BLUE;
        } else {
            color2 = Screen.CYAN;
        }
        if (estratJugador1.equals("A")) estratJugador1 = "Atac";
        if (estratJugador2.equals("A")) estratJugador2 = "Atac";
        if (estratJugador1.equals("D")) estratJugador1 = "Defensa";
        if (estratJugador2.equals("D")) estratJugador2 = "Defensa";
        if (estratJugador1.equals("E")) estratJugador1 = "Engany";
        if (estratJugador2.equals("E")) estratJugador2 = "Engany";
        if (estratJugador1.equals("M")) estratJugador1 = "Maniobra";
        if (estratJugador2.equals("M")) estratJugador2 = "Maniobra";
        Screen.print(8+x, 1+y, "Estratègia",Screen.YELLOW);
        Screen.print(2+x, 3+y, String.format("%s:",jugador1.nom));
        Screen.print(13+x, 3+y, String.format("%s", estratJugador1),color);
        Screen.print(2+x, 4+y, String.format("%s:",jugador2.nom));
        Screen.print(13+x, 4+y, String.format("%s", estratJugador2),color2);
    }



    static void mostraExit(int x, int y, Jugador jugador1, Jugador jugador2, int exit1, int exit2, Border border) {

        dibuixaQuadrat(x,y,27,5,true, border);

        Screen.print(11+x, 1+y, "Èxit",Screen.GREEN);
        Screen.print(2+x, 3+y, String.format("%s:",jugador1.nom));
        Screen.print(13+x, 3+y, String.format("%s", exit1));
        Screen.print(2+x, 4+y, String.format("%s:",jugador2.nom));
        Screen.print(13+x, 4+y, String.format("%s", exit2));
    }
    static void mostraContinuar(int x, int y, String continuar,boolean header, Border border) {
        dibuixaQuadrat(x,y,40,2,header, border);

        Screen.print(5+x, 1+y, String.format("»» Pitja ENTER per %s ««",continuar),Screen.BLUE);
    }
    static void mostraTitolMenuPrincipal(int x, int y, Border border) {
        dibuixaQuadrat(x,y,50,2,false, border);

        Screen.print(20+x, 1+y, "- COMBAT -",Screen.RED);
    }

    static void mostraMenuPrincipal(int x, int y, Border border) {
        dibuixaQuadrat(x,y,50,9,true, border);

        Screen.print(12+x, 1+y, "Computer Role Playing Game",Screen.PURPLE);
        Screen.print(9+x, 3+y, "» (1) Comença un joc nou       «");
        Screen.print(9+x, 4+y, "» (2) Continua un joc anterior «");
        Screen.print(9+x, 5+y, "» (3) Estadístiques            «");
        Screen.print(9+x, 6+y, "» (4) Opcions                  «");
        Screen.print(9+x, 7+y, "» (5) Com es juga              «");
        Screen.print(9+x, 8+y, "» (6) Sortir                   «");
    }
    static void mostraMenuOpcioInvalida(int x, int y, Border border) {

        dibuixaQuadrat(x,y,50,4,true, border);

        Screen.print(16+x, 1+y, "- OPCIÓ INVÀLIDA -",Screen.RED);
        Screen.print(11+x, 3+y, "Tria una opció d'entre (1-6)",Screen.RED);
    }
    static void mostraMenuJocNou(int x, int y, Border border) {
        dibuixaQuadrat(x,y,41,5,true, border);

        Screen.print(14+x, 1+y, "- JOC NOU -",Screen.RED);
        Screen.print(10+x, 3+y, "» (1) Un Jugador   «");
        Screen.print(10+x, 4+y, "» (2) Dos Jugadors «");
    }
    static void mostraMenuDosJugadors(int x, int y, Border border,int numeroJugador) {
        dibuixaQuadrat(x,y,41,5,true, border);

        Screen.print(9+x, 1+y, "- CREACIÓ PERSONATGE -",Screen.RED);
        Screen.print(16+x, 3+y, String.format("Jugador %d",numeroJugador));
    }
    static void mostraContinuaDosJugadors(int x, int y, Border border) {
        dibuixaQuadrat(x,y,51,7,true, border);

        Screen.print(15+x, 1+y, "- FI DEL COMBAT -",Screen.RED);
        Screen.print(4+x, 3+y, "L'enfrontament ha acabat, però mai es tard");
        Screen.print(14+x, 4+y, "per demanar la revenja");
        Screen.print(13+x, 5+y, "» (1) Torna a combatre «",Screen.GREEN);
        Screen.print(13+x, 6+y, "» (2) Ja n'hi ha prou  «",Screen.RED);
    }
    static void mostraTriaNom(int x, int y, Border border) {
        dibuixaQuadrat(x,y,41,4,true, border);

        Screen.print(11+x, 1+y, "- TRIA EL TEU NOM -",Screen.RED);
        Screen.print(13+x, 3+y, "¿Quin nom vols?");
    }
    static void mostraErrorNomCurt(int x, int y, Border border) {
        dibuixaQuadrat(x,y,47,5,true, border);

        Screen.print(16+x, 1+y, "- NOM INVALID -",Screen.RED);
        Screen.print(15+x, 3+y, "¡Nom massa curt!",Screen.RED);
        Screen.print(2+x, 4+y, "Has de triar un nom d'entre (3-12) caràcters",Screen.RED);
    }
    static void mostraErrorNomLlarg(int x, int y, Border border) {
        dibuixaQuadrat(x,y,47,5,true, border);

        Screen.print(16+x, 1+y, "- NOM INVALID -",Screen.RED);
        Screen.print(15+x, 3+y, "¡Nom massa llarg!",Screen.RED);
        Screen.print(2+x, 4+y, "Has de triar un nom d'entre (3-12) caràcters",Screen.RED);
    }
    static void mostraTriaTipus(int x, int y, Border border) {
        dibuixaQuadrat(x,y,51,8,true, border);

        Screen.print(15+x, 1+y, "- TRIA EL TEU TIPUS -",Screen.RED);
        Screen.print(5+x, 3+y, "» (A)prenent: Molt dèbil, millora aviat «");
        Screen.print(5+x, 4+y, "» (C)avaller: Equilibrat i versàtil     «");
        Screen.print(5+x, 5+y, "» (M)ag: Destaca amb la defensa         «");
        Screen.print(5+x, 6+y, "» (O)rc: Destaca amb l'atac             «");
        Screen.print(5+x, 7+y, "» (T)roll: Molt fort, millora poc       «");
    }
    static void mostraErrorTipus(int x, int y, Border border) {
        dibuixaQuadrat(x,y,41,4,true, border);

        Screen.print(11+x, 1+y, "- TIPUS INVALID -",Screen.RED);
        Screen.print(6+x, 3+y, "¡Tria un tipus de la llista!",Screen.RED);
    }
    static void mostraNouPersonatge(int x, int y, Border border) {
        dibuixaQuadrat(x,y,43,5,true, border);

        Screen.print(12+x, 1+y, "- NOU PERSONATGE -",Screen.RED);
        Screen.print(3+x, 3+y, "Aquests són els teus atributs inicials");
        Screen.print(7+x, 4+y, "¡Aviat serà hora de combatre!");
    }
    static void mostraMenuComEsJuga(int x, int y, Border border) {
        dibuixaQuadrat(x,y,99,15,true, border);

        Screen.print(42+x, 1+y, "- COM ES JUGA -",Screen.RED);
        Screen.print(3+x, 3+y, "COMBAT és un joc de lluites per torns on has d'enfrontar-te amb diferents adversaris:");
        Screen.print(3+x, 4+y, "Els lluitadors trien una estratègia, això determina el resultat de l'enfrontament.");
        Screen.print(3+x, 5+y, "Les estratègies són (A)tac, (D)efensa, (E)ngany o (M)aniobra");
        Screen.print(3+x, 6+y, "Les resolucions possibles d'un enfrontament són les següents:");
        Screen.print(5+x, 7+y, "Dany: Perdre punts de vida");
        Screen.print(5+x, 8+y, "Guarit: Recupera punts de vida");
        Screen.print(5+x, 9+y, "Penalitzat: Reducció temporal dels atributs");
        Screen.print(3+x, 10+y, "Quan un lluitador perd tots els punts de vida, s'acaba la ronda.");
        Screen.print(3+x, 11+y, String.format("Si una ronda supera els %d torns, guanya qui tengui més vida.", NTORNS));
        Screen.print(3+x, 12+y, "Guanya punts derrotant adversaris, amb prou punts pujaràs de nivell.");
        Screen.print(3+x, 13+y, String.format("El guanyador es decideix al millor de %d rondes, si perds s'acaba el joc.", (NCOMBATS)));
        Screen.print(3+x, 14+y, "Si guanyes, et seguiràs enfrontant amb diferents adversaris cada cop més forts.");
    }
    static void mostraMenuSortir(int x, int y, Border border) {
        dibuixaQuadrat(x,y,40,5,true, border);

        Screen.print(15+x, 1+y, "- SORTIR -",Screen.RED);
        Screen.print(6+x, 3+y, "Has pres la decisió correcta");
        Screen.print(14+x, 4+y, "¡Fins aviat!");
    }
    static void mostraMenuOpcions(int x, int y, Border border) {
        dibuixaQuadrat(x,y,40,5,true, border);

        Screen.print(14+x, 1+y, "- OPCIONS -",Screen.RED);
        Screen.print(3+x, 3+y, "» (1) Canvia l'estil de la vora «");
        Screen.print(3+x, 4+y, "» (2) Torna al menú             «");
    }

    static void mostraMenuBorder(int x, int y, Border border) {
        dibuixaQuadrat(x,y,40,11,true, border);

        Screen.print(11+x, 1+y, "- TRIA UNA VORA -",Screen.RED);
        Screen.print(9+x, 3+y, "» (1) Retro         «");
        Screen.print(9+x, 4+y, "» (2) Triangles     «");
        Screen.print(9+x, 5+y, "» (3) Simple        «");
        Screen.print(9+x, 6+y, "» (4) Doble H       «");
        Screen.print(9+x, 7+y, "» (5) Doble V       «");
        Screen.print(9+x, 8+y, "» (6) Doble H+V     «");
        Screen.print(9+x, 9+y, "» (7) Gruixat       «");
        Screen.print(9+x, 10+y, "» (8) Punts         «");
    }
    static void mostraConfirmaBorder(int x, int y, Border border) {
        dibuixaQuadrat(x,y,40,5,true, border);

        Screen.print(10+x, 1+y, "- CONFIRMACIÓ VORA -");
        Screen.print(5+x, 3+y, "» (1) M'agrada aquesta vora «");
        Screen.print(5+x, 4+y, "» (2) Tria un altre vora    «");
    }
    static void mostraMenuContinua(int x, int y, Border border) {
        dibuixaQuadrat(x,y,40,4,true, border);

        Screen.print(14+x, 1+y, "- CONTINUA -");
        Screen.print(3+x, 3+y, "¡Aquesta opció no està disponible!");
    }
    static void mostraMenuEstadistiques(int x, int y, Border border) {
        dibuixaQuadrat(x,y,40,4,true, border);

        Screen.print(10+x, 1+y, "- ESTADISTIQUES -");
        Screen.print(3+x, 3+y, "¡Aquesta opció no està disponible!");
    }
    private static void dibuixaQuadrat(int x, int y, int w, int h, boolean header, Border border) {
        String color = Screen.PURPLE;
        for (int i = 0; i < w; i++) {
            Screen.printChar(i+x,y,border.borderTop,color);
            Screen.printChar(i+x,y+h,border.borderBottom,color);
            if (!header) continue;
            Screen.printChar(i+x,y+2,border.borderMid,color);
        }
        for (int i = 0; i < h+1; i++) {
            Screen.printChar(x,i+y,border.borderLeft,color);

            Screen.printChar(w+x,i+y,border.borderRight,color);
        }
        Screen.printChar(x,y,border.borderTopLeft,color);
        Screen.printChar(x+w,y,border.borderTopRight,color);
        Screen.printChar(x,y+h,border.borderBottomLeft,color);
        Screen.printChar(x+w,y+h,border.borderBottomRight,color);
        if (!header) return;
        Screen.printChar(x+w,y+2,border.borderMidRight,color);
        Screen.printChar(x,y+2,border.borderMidLeft,color);
    }

    private static void menuPrincipal(Border border) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Scanner scanner = new Scanner(System.in);
        Boolean opcioInvalida = false;
        while (true) {
            Screen.clear();
            if (opcioInvalida) mostraMenuOpcioInvalida(24,17,border);
            opcioInvalida = false;
            mostraTitolMenuPrincipal(24,4,border);
            mostraMenuPrincipal(24,7,border);
            Screen.show();
            String menu = scanner.nextLine();
            switch (menu) {
                case "1":
                case "Joc Nou":
                    Screen.clear();
                    mostraMenuJocNou(25,5,border);
                    Screen.show();
                    String jocNou = scanner.nextLine();
                    if (jocNou.equals("1")) {
                        Boolean nouPersonatge = true;
                        // Mode 1 Jugador. El jugador humà crea el seu personatge
                        Jugador jugador1 = bestiari.triaJugador(border);
                        // Comencem el bucle de combats
                        for (int combats = 0; combats < NADVERSARIS; combats++) {
                            int nivellAdversari = -3 + combats + jugador1.nivell;
                            Jugador jugador2 = bestiari.jugadorAleatori(nivellAdversari);
                            Boolean derrota = nouCombat(jugador1,jugador2,border,combats+1,nouPersonatge,false);
                            nouPersonatge = false;
                            if (derrota) {
                                break;
                            }
                        }
                    } else if (jocNou.equals("2")) {
                        // Mode 2 Jugadors. jugador1 crea el seu personatge.
                        Screen.clear();
                        mostraMenuDosJugadors(25,5,border,1);
                        mostraContinuar(25,21,"continuar",false,border);
                        Screen.show();
                        scanner.nextLine();
                        Jugador jugador1 = bestiari.triaJugador(border);
                        //  jugador2 crea el seu personatge.
                        Screen.clear();
                        mostraMenuDosJugadors(25,5,border,2);
                        mostraContinuar(25,21,"continuar",false,border);
                        Screen.show();
                        scanner.nextLine();
                        Jugador jugador2 = bestiari.triaJugador(border);
                        // Comencem el bucle de combats
                        for (int combats = 0; combats < NADVERSARIS; combats++) {
                            Boolean derrota = nouCombat(jugador1,jugador2,border,combats+1,false, true);
                            if (derrota) {
                                break;
                            }
                        }
                    }
                    break;
                case "2":
                case "Continuar":
                    Screen.clear();
                    mostraMenuContinua(25,5,border);
                    mostraContinuar(25,21,"continuar",false,border);
                    Screen.show();
                    menu = scanner.nextLine();
                    break;
                case "3":
                case "Estadistiques":
                    Screen.clear();
                    mostraMenuEstadistiques(25,5,border);
                    mostraContinuar(25,21,"continuar",false,border);
                    Screen.show();
                    menu = scanner.nextLine();
                    break;
                case "4":
                case "Opcions":
                    Screen.clear();
                    mostraMenuOpcions(25,5,border);
                    Screen.show();
                    String triaOpcio = scanner.nextLine();
                    if (triaOpcio.equals("1")) {
                        while (true) {
                            Screen.clear();
                            mostraMenuBorder(25,5,border);
                            Screen.show();
                            triaOpcio = scanner.nextLine();
                            border.borderChange(triaOpcio);
                            if (!triaOpcio.equals("1") && !triaOpcio.equals("2") && !triaOpcio.equals("3") && !triaOpcio.equals("4") && !triaOpcio.equals("5") && !triaOpcio.equals("6") && !triaOpcio.equals("7") && !triaOpcio.equals("8")) {
                                break;
                            }

                            Screen.clear();
                            mostraConfirmaBorder(25,5,border);
                            Screen.show();
                            triaOpcio = scanner.nextLine();
                            if (triaOpcio.equals("1")) {
                                break;
                            }
                        }
                    }
                    break;
                case "5":
                case "help":
                case "ajuda":
                case "ayuda":
                    Screen.clear();
                    mostraMenuComEsJuga(2,3,border);
                    mostraContinuar(31,21,"continuar",false,border);
                    Screen.show();
                    menu = scanner.nextLine();
                    break;
                case "6":
                case "exit":
                case "Sortir":
                    Screen.clear();
                    mostraMenuSortir(25,5,border);
                    Screen.show();
                    return;
                default:
                    opcioInvalida = true;

            }
        }
    }

}
