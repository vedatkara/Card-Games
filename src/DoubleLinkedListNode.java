public class DoubleLinkedListNode {

    private Object data;
    private DoubleLinkedListNode prev;
    private DoubleLinkedListNode next;

    public DoubleLinkedListNode(Object dataToAdd) {
        data = dataToAdd;
        prev = null;
        next = null;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public DoubleLinkedListNode getPrev() {
        return prev;
    }
    public void setPrev(DoubleLinkedListNode prev) {
        this.prev = prev;
    }
    public DoubleLinkedListNode getNext() {
        return next;
    }
    public void setNext(DoubleLinkedListNode next) {
        this.next = next;
    }

}

