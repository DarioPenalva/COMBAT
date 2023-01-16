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
    // Per comprobar si hem perdut

    public boolean haPerdut() {
        return puntsDeVida <= 0;
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
        }
    }
    public void recuperacio() {
        puntsDeVida = puntsDeVidaMaxims;
        capDefensa = capDefensaMaxima;
        capAtac = capAtacMaxima;
    }



}
