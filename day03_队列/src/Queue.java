import list.LinkedList;
import list.List;
/*
队列：只能在头尾两端操作 只能从队尾添加元素 即入队enQueue 只能从队头移除元素 即出队deQueue
双端队列：只能在队头 队尾删除 添加元素
循环队列：
 */
public class Queue<E> {
    private List<E> list = new LinkedList<>();
    public void clear(){
        list.clear();
    }
    public int size(){
        return list.size();
    }
    public boolean isEmpty(){
        return list.isEmpty();
    }
    public void enQueue(E element){
        list.add(element);
    }
    public E deQueue(){
        return list.remove(0);
    }
    public E front(){
        return list.get(0);
    }
}
