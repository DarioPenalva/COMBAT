import java.util.Random;

public class Jugador {
    String nom;
    String tipus;
    int nivell;
    int punts;
    int puntsDeVida;
    int puntsDeVidaMaxims;
    int capAtac;
    int capAtacMaxima;
    int capDefensa;
    int capDefensaMaxima;
    // El següent es fa servir per determinar quan puja de nivell cada tipus.
    int puntsExperiencia;
    int puntsPujarNivell;
    // Els següents serveixen per determinar quins augments té cada tipus en pujar de nivell.
    int bonificacioAtac;
    int bonificacioDefensa;
    int bonificacioVida;
    // Atributs per la IA:
    int memoriaEstrategiesJug1 = 0;
    String darreraStratJug1 = "";

    // Per comprobar si hem perdut
    public boolean haPerdut() {
        return puntsDeVida <= 0;
    }

    // Això és la IA:
    public String decisioIA(String estratJug1) {
        if (estratJug1.equals(darreraStratJug1)) {
            memoriaEstrategiesJug1++;
        } else {
            darreraStratJug1 = estratJug1;
            memoriaEstrategiesJug1 = 0;
        }
        if (nivell == 1) {
            return triaEstrategiaAleatoria(); // Les IA de lvl1 només fan jugades aleatòries.
        } else if (nivell >= 2) {
            if (memoriaEstrategiesJug1 >= 3) { // Si fas spam d'una estratègia, a partir de lvl2 la IA fa trampes per guanyar-te.
                memoriaEstrategiesJug1 -= 1;
                return triaEstrategiaGuanyadora(estratJug1);
            }
        } else if (nivell >= 3) { // Les IA a partir de lvl3 comproven els seus atributs per triar el que més efecte pot tenir.
            if (nivell >= 5 && ((puntsDeVida*100)/puntsDeVidaMaxims) <= 30) { // Les IA a parit de lvl5, quan els queda un 30% de vida, sempre fan trampes.
                return triaEstrategiaGuanyadora(estratJug1);
            }
            return triaEstrategiaMesFavorable(capAtac,capDefensa);
        }
        return triaEstrategiaAleatoria();
    }

    private String triaEstrategiaMesFavorable(int capAtac, int capDefensa) {
        Random r = new Random();
        if (capAtac == capDefensa) {
            return triaEstrategiaAleatoria();
        } else if (capAtac > capDefensa) {
            String[] estrategies = {"A","A","A","E","E","E","D","M"};
            int index = r.nextInt(estrategies.length);
            return estrategies[index];
        } else {
            String[] estrategies = {"D","D","D","M","M","M","A","E"};
            int index = r.nextInt(estrategies.length);
            return estrategies[index];
        }
    }

    private String triaEstrategiaGuanyadora(String estratJug1) {
        Random r = new Random();
        if (estratJug1.equals("A")) return "D";
        if (estratJug1.equals("D")) {
            String[] estrategies = {"E","M"};
            int index = r.nextInt(estrategies.length);
            return estrategies[index];
        }
        if (estratJug1.equals("E")) {
            String[] estrategies = {"A","M"};
            int index = r.nextInt(estrategies.length);
            return estrategies[index];
        }
        if (estratJug1.equals("M")) return "A";
        return "A";
    }

    public String triaEstrategiaAleatoria() {
        Random r = new Random();
        String[] estrategies = {"A","D","E","M"};
        int index = r.nextInt(estrategies.length);
        return estrategies[index];
    }

    public void dany(int exit) {
        puntsDeVida -= exit;
        if (puntsDeVida < 0) {
            puntsDeVida = 0;
        }
    }


    public void guarit(int exit) {
        puntsDeVida += exit;
        if (puntsDeVida >= puntsDeVidaMaxims) {
            puntsDeVida = puntsDeVidaMaxims;
        }

    }

    public void penalitza(int x, int y, int exit, Border border) {
        if (Math.random() < 0.5) {
            capDefensa -= exit;
            Combat.mostraPenalitzacio(x,y,nom,exit,border,"Defensa");
        } else {
            capAtac -= exit;
            Combat.mostraPenalitzacio(x,y,nom,exit,border,"Atac");
        }
        if (capDefensa < 1) capDefensa = 1;
        if (capAtac < 1) capAtac = 1;
    }
    public void guanyaPunts(int augment) {
        punts += augment;
        puntsExperiencia += augment;
        while (puntsExperiencia >= puntsPujarNivell) { // Puja de nivell es calcula aquí
            puntsExperiencia -= puntsPujarNivell;
            nivell++;
            capAtacMaxima += bonificacioAtac;
            capDefensaMaxima += bonificacioDefensa;
            puntsDeVidaMaxims += bonificacioVida;
            puntsPujarNivell = puntsPujarNivell + puntsPujarNivell / 2 ; // Pujar el pròxim nivell costa més
        }
    }
    public void recuperacio() {
        puntsDeVida = puntsDeVidaMaxims;
        capDefensa = capDefensaMaxima;
        capAtac = capAtacMaxima;
    }



}
