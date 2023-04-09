public class DoubleLinkedList {

    private DoubleLinkedListNode head;
    private DoubleLinkedListNode tail;

    public DoubleLinkedList() {
        head = null;
        tail = null;
    }

    public void add (Player dataToAdd) { // adds the inserted elements in order from largest to smallest

        if((head == null) && (tail == null)) {
            DoubleLinkedListNode newNode = new DoubleLinkedListNode(dataToAdd);
            head = newNode;
            tail = newNode;
        }
        else if((Double)dataToAdd.getScore() > (Double)((Player)head.getData()).getScore()) {
            DoubleLinkedListNode newNode = new DoubleLinkedListNode(dataToAdd);
            newNode.setNext(head);
            head.setPrev(newNode);
            head = newNode;
        }
        else {
            DoubleLinkedListNode newNode = new DoubleLinkedListNode(dataToAdd);
            DoubleLinkedListNode temp = head;
            while(temp.getNext()!= null && ((Double)dataToAdd.getScore() < (Double)((Player)temp.getNext().getData()).getScore())) {
                temp = temp.getNext();
            }
            newNode.setPrev(temp);
            newNode.setNext(temp.getNext());
            if(temp.getNext() != null) {
                temp.getNext().setPrev(newNode);
            }
            else
                tail = newNode;
            temp.setNext(newNode);
        }
    }

    public void removeLastElement() {
        if (head == null) {
            System.out.println("linked list is empty");
        } else if (size() == 1) {
            head = null;
        } else {

            DoubleLinkedListNode temp = head;
            while(temp.getNext() != null) {
                temp = temp.getNext();
            }
            temp.getPrev().setNext(null);
            temp.setPrev(null);
            temp = null;
        }
    }

    public int size() {
        if (head == null) {
            return 0;
        } else {
            int count = 0;
            DoubleLinkedListNode temp = head;

            while (temp != null) {
                temp = temp.getNext();
                count++;
            }

            return count;
        }
    }

    public void display(enigma.console.Console obj) {
        if(head == null) {
            System.out.println("List is empty!");
        }
        else {
            int rank = 1;
            DoubleLinkedListNode temp = head;
            while(temp != null) {
                obj.getTextWindow().setCursorPosition(26,rank + 4);
                System.out.println(rank + ". " + ((Player)temp.getData()).getName() + " " + String.format ("%.2f",((Player)temp.getData()).getScore()));
                temp = temp.getNext();
                rank++;
            }
        }
    }

    public Object getElement(int x) { // returns the item in the desired index
        if(head == null)
        {
            System.out.println("List is empty");
            return null;
        }
        else if(x > size() || x < 0 ){
            System.out.println("Index is out of range");
            return null;
        }
        else {
            DoubleLinkedListNode temp = head;
            int count = 1;
            while(temp != null) {
                if(x == count)
                    return temp.getData();
                temp = temp.getNext();
                count++;
            }
            return null;
        }
    }
}