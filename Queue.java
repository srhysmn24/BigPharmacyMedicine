/***
Coder: Roslan S, UiTM Pahang, roslancs@uitm.edu.my
Year: 2012
***/

public class Queue extends LinkedList 
{
    public Queue() {}
    
    public void enqueue(Object element) {
        addLast(element);
    }
    
    public Object dequeue() {
        return removeFirst();
    }
    
    public Object getFront() {
        return getFirst();
    }
}
