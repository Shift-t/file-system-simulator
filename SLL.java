// ***From Lab Files***
// **************************  SLL.java  *********************************
// A generic singly linked list class


public class SLL<T> {

    protected SLLNode<T> head, tail;

    public SLL() {
        head = tail = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void addToHead(T el) {
        head = new SLLNode<T>(el, head);
        if (tail == null)
            tail = head;
    }

    public void addToTail(T el) {
        if (!isEmpty()) {
            tail.next = new SLLNode<T>(el);
            tail = tail.next;
        } else {
            head = tail = new SLLNode<T>(el);
        }
    }

    public T deleteFromHead() {
        if (isEmpty())
            return null;

        T el = head.info;
        if (head == tail)
            head = tail = null;
        else
            head = head.next;

        return el;
    }

    public T deleteFromTail() {
        if (isEmpty())
            return null;

        T el = tail.info;

        if (head == tail)
            head = tail = null;
        else {
            SLLNode<T> tmp;
            for (tmp = head; tmp.next != tail; tmp = tmp.next);
            tail = tmp;
            tail.next = null;
        }

        return el;
    }

    public void delete(T el) {
        if (!isEmpty()) {
            if (head == tail && el.equals(head.info)) {
                head = tail = null;
            } else if (el.equals(head.info)) {
                head = head.next;
            } else {
                SLLNode<T> pred, tmp;
                for (pred = head, tmp = head.next;
                     tmp != null && !tmp.info.equals(el);
                     pred = pred.next, tmp = tmp.next);

                if (tmp != null) {
                    pred.next = tmp.next;
                    if (tmp == tail)
                        tail = pred;
                }
            }
        }
    }

    @Override
    public String toString() {
        if (head == null)
            return "[ ]";

        String str = "[ ";
        SLLNode<T> tmp = head;

        while (tmp != null) {
            str += tmp.info + " ";
            tmp = tmp.next;
        }

        return str + "]";
    }

    public boolean contains(T el) {
        if (head == null)
            return false;

        SLLNode<T> tmp = head;

        while (tmp != null) {
            if (tmp.info.equals(el))
                return true;
            tmp = tmp.next;
        }

        return false;
    }

    public int size() {
        if (head == null)
            return 0;

        int count = 0;
        SLLNode<T> p = head;

        while (p != null) {
            count++;
            p = p.next;
        }

        return count;
    }
}
