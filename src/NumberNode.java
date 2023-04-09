public class NumberNode {
    private int number;
    private NumberNode next;

    public NumberNode(int number){
        this.number = number;
        next = null;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public NumberNode getNext() {
        return next;
    }

    public void setNext(NumberNode next) {
        this.next = next;
    }
}
