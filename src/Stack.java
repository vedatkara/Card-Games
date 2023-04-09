import java.util.Random;

public class Stack {
    private int top;
    private Object[] elements;

    Stack(int size) {
        elements = new Object[size];
        top = -1;
    }

    public void push(Object element) {
        if(!isFull()) {
            top++;
            elements[top] = element;
        }
        else {
            System.out.println("Stack overflow");
        }
    }

    public Object pop() {
        if(!isEmpty()) {
            Object temp = elements[top];
            elements[top] = null;
            top--;
            return temp;
        }
        else {
            System.out.println("There is no element in stack");
            return null;
        }

    }

    public Object peek() {
        if(!isEmpty()) {
            return elements[top];
        }
        else {
            //System.out.println("There is no element in stack");
            return new Card(" ", " ");
        }
    }

    public int size() {
        return top + 1;
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public boolean isFull() {
        return (top == elements.length - 1);
    }

    public void shuffle() {
        Random rnd = new Random();

        for (int i = 0; i < elements.length; i++) {
            int randomIndexToSwap = rnd.nextInt(elements.length);
            Object temp = elements[randomIndexToSwap];
            elements[randomIndexToSwap] = elements[i];
            elements[i] = temp;
        }
    }
}
