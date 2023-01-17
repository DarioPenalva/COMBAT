public class Screen {
    // Reset
    public static final String RESET = "\033[0m";     // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";  // BLACK
    public static final String RED = "\033[0;31m";    // RED
    public static final String GREEN = "\033[0;32m";  // GREEN
    public static final String YELLOW = "\033[0;33m"; // YELLOW
    public static final String BLUE = "\033[0;34m";   // BLUE
    public static final String PURPLE = "\033[0;35m"; // PURPLE
    public static final String CYAN = "\033[0;36m";   // CYAN
    public static final String WHITE = "\033[0;37m";  // WHITE
    static String[][] pixels = new String[26][140];

    static void clear() {
        Combat.esborraPantalla();
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = " ";
            }
        }
    }

    static void show() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                System.out.print(pixels[i][j]);
            }
            System.out.println();
        }
    }

    public static void printChar(int x, int y, char c, String color) {
        pixels[y][x] = color + c + RESET;
    }

    public static void print(int x, int y, String s) {
        for (int i = 0; i < s.length(); i++) {
            printChar(x, y, s.charAt(i), WHITE);
            x++;
        }
    }

    public static void print(int x, int y, String s, String color) {
        for (int i = 0; i < s.length(); i++) {
            printChar(x, y, s.charAt(i), color);
            x++;
        }
    }
}
