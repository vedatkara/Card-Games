public class Node {
    // The node that we learned from lesson
    private Node link;
    private Object data;


    public Node(Object dataToAdd) {
        data = dataToAdd;
        link = null;
    }

    public Node getLink() {
        return link;
    }
    public void setLink(Node link) {
        this.link = link;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
}