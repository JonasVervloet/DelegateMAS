package Environment;

public class Entry {

    public int row;
    public int column;

    public Entry(int row, int column) {
        this.row = row;
        this.column = column;
    }


    public Entry getEntryAvove() {
        return new Entry(row + 1, column);
    }

    public Entry getEntryLeft() {
        return new Entry(row, column + 1);
    }

    public void out() {
        System.out.println("Entry: " + row + ", " + column);
    }

    @Override
    public int hashCode() {
        return row + 1000 * column;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        if (((Entry) other).row == row
                && ((Entry) other).column == column) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Entry: " + row + ", " + column;
    }
}
