public class Border {
    char borderLeft = '║';
    char borderRight = '║';
    char borderTop = '═';
    char borderMid = '═';
    char borderBottom = '═';
    char borderBottomRight = '╝';
    char borderBottomLeft = '╚';
    char borderTopRight = '╗';
    char borderTopLeft = '╔';
    char borderMidLeft = '╠';
    char borderMidRight = '╣';

    public void borderChange(String option) {
        switch (option) {
            case "1":
                borderLeft = '|';
                borderRight = '|';
                borderTop = '-';
                borderMid = '~';
                borderBottom = '_';
                borderBottomRight = '/';
                borderBottomLeft = 92;
                borderTopRight = 92;
                borderTopLeft = '/';
                borderMidLeft = '[';
                borderMidRight = ']';
                break;
            case "2":
                borderLeft = '▏';
                borderRight = '▕';
                borderTop = '▾';
                borderMid = '▻';
                borderBottom = '▴';
                borderBottomRight = '◢';
                borderBottomLeft = '◣';
                borderTopRight = '◥';
                borderTopLeft = '◤';
                borderMidLeft = '▶';
                borderMidRight = '◀';
                break;
            case "3":
                borderLeft = '│';
                borderRight = '│';
                borderTop = '─';
                borderMid = '─';
                borderBottom = '─';
                borderBottomRight = '┘';
                borderBottomLeft = '└';
                borderTopRight = '┐';
                borderTopLeft = '┌';
                borderMidLeft = '├';
                borderMidRight = '┤';
                break;
            case "4":
                borderLeft = '│';
                borderRight = '│';
                borderTop = '═';
                borderMid = '═';
                borderBottom = '═';
                borderBottomRight = '╛';
                borderBottomLeft = '╘';
                borderTopRight = '╕';
                borderTopLeft = '╒';
                borderMidLeft = '╞';
                borderMidRight = '╡';
                break;
            case "5":
                borderLeft = '║';
                borderRight = '║';
                borderTop = '─';
                borderMid = '─';
                borderBottom = '─';
                borderBottomRight = '╜';
                borderBottomLeft = '╙';
                borderTopRight = '╖';
                borderTopLeft = '╓';
                borderMidLeft = '╟';
                borderMidRight = '╢';
                break;
            case "6":
                borderLeft = '║';
                borderRight = '║';
                borderTop = '═';
                borderMid = '═';
                borderBottom = '═';
                borderBottomRight = '╝';
                borderBottomLeft = '╚';
                borderTopRight = '╗';
                borderTopLeft = '╔';
                borderMidLeft = '╠';
                borderMidRight = '╣';
                break;
            case "7":
                borderLeft = '█';
                borderRight = '█';
                borderTop = '▀';
                borderMid = '▬';
                borderBottom = '▄';
                borderBottomRight = '█';
                borderBottomLeft = '█';
                borderTopRight = '█';
                borderTopLeft = '█';
                borderMidLeft = '█';
                borderMidRight = '█';
                break;
            case "8":
                borderLeft = '┋';
                borderRight = '┋';
                borderTop = '┅';
                borderMid = '┅';
                borderBottom = '┅';
                borderBottomRight = '┛';
                borderBottomLeft = '┗';
                borderTopRight = '┓';
                borderTopLeft = '┏';
                borderMidLeft = '┋';
                borderMidRight = '┋';
                break;
            default:
        }
    }
}
