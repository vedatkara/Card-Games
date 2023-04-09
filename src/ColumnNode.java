public class ColumnNode {
    private int columnNumber;
    private ColumnNode right;
    private NumberNode down;

    public ColumnNode(int columnNum){
        columnNumber = columnNum;
        right = null;
        down = null;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public ColumnNode getRight() {
        return right;
    }

    public void setRight(ColumnNode right) {
        this.right = right;
    }

    public NumberNode getDown() {
        return down;
    }

    public void setDown(NumberNode down) {
        this.down = down;
    }
}
