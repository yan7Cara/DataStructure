import heap.BinaryHeap;
import java.util.Comparator;
/*
优先级队列：
普通的队列是先进先出 优先级队列则是按照优先级高低进行出队 比如将优先级最高的元素作为队头优先出队

优先级队列的实现：二插堆 把优先级最高/最低的元素放在堆顶 然后直接remove堆顶元素即可实现出队
 */
public class PriorityQueue<E> {
    private BinaryHeap<E> heap;

    public PriorityQueue(Comparator<E> comparator) {
        heap = new BinaryHeap<>(comparator);
    }

    public PriorityQueue() {
        this(null);
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public void clear() {
        heap.clear();
    }

    public void enQueue(E element) {
        heap.add(element);
    }

    public E deQueue() {
        return heap.remove();
    }

    public E front() {
        return heap.get();
    }
}
