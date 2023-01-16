public class Screen {
    static char[][] pixels = new char[24][140];

    static void clear() {
        Combat.esborraPantalla();
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = ' ';
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

    public static void printChar(int x, int y, char c) {
        pixels[y][x] = c;
    }

    public static void print(int x, int y, String s) {
        for (int i = 0; i < s.length(); i++) {
            pixels[y][x] = s.charAt(i);
            x++;
        }
    }
}
