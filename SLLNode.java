// ***From Lab Files***
// **************************  SLLNode.java  *********************************
// A generic node for the singly linked list


public class SLLNode<T> {
    T info;
    SLLNode<T> next;

    public SLLNode() {
        this(null, null);
    }

    public SLLNode(T el) {
        this(el, null);
    }

    public SLLNode(T el, SLLNode<T> ptr) {
        this.info = el;
        this.next = ptr;
    }
}
