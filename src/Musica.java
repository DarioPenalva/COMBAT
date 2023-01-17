import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Musica {
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
}
