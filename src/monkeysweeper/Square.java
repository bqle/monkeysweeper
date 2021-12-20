package monkeysweeper;

public class Square {
    // -1 indicates a bomb,
    // any other integer
    // indicates the number of bombs adjacent to this square
    private int value;

    // 0 is not shown; 1 is shown; 2 is flag
    private int visibility;

    private int i;
    private int j;

    public Square(int i, int j) {
        value = 0;
        visibility = 0;
        this.i = i;
        this.j = j;
    }

    public Square(int i, int j, int value) {
        this.value = value;
        visibility = 0;
        this.i = i;
        this.j = j;
    }

    public Square(int i, int j, int value, int visibility) {
        this.value = value;
        this.visibility = visibility;
        this.i = i;
        this.j = j;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void incrementValueIfNotBomb() {
        if (value != -1) {
            value += 1;
        }
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return i + "," + j + "," + value + "," + visibility;
    }
}
