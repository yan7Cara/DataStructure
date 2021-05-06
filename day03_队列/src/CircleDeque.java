import list.LinkedList;
import list.List;

public class CircleDeque<E> {
    private int size;
    private E[] elements;
    private int front;

    public CircleDeque(){
        elements = (E[]) new Object[10];
    }

    private List<E> list = new LinkedList<>();
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size == 0;
    }

    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[index(size)] = null;
        }
        size = 0;
        front = 0;
    }

    //从尾部入队
    public void enQueueRear(E element) {
        ensureCapacity(size + 1);
        elements[index(size)] = element;
        size++;
    }

    //从头部出队
    public E deQueueFront() {
        E frontElement = elements[front];
        elements[front] = null;
        front = index(1);
        size--;
        return frontElement;
    }

    //从头部入队
    public void enQueueFront(E element) {
        ensureCapacity(size + 1);
        //不过front指针指向的index等于多少 我们都把他当成0 那么front指针前面的index就是-1
        //要从头部添加数据 先将front指针指向index(-1) 然后再将数据放入elements[front]的位置
        //此时我们要考虑index（-1）的特殊情况 见下面
        front = index(-1);
        elements[front] = element;
        size++;
    }

    //从尾部出队
    public E deQueueRear() {
        int rearIndex = index(size - 1);
        E rear = elements[rearIndex];//传入尾部数据的索引 获取尾部数据
        elements[rearIndex] = null;//尾部数据清空
        size--;
        return rear;
    }

    public E front() {
        return elements[front];
    }

    public E rear() {
//        return elements[front + size - 1];
        return elements[index(size - 1)];//index(size - 1)获取队列中最后一个数据的真实索引
    }
    /*
    假设front=0 index=-1 那么front+index=-1 假设队列为0~5的数组 那么-1%6=1
    而实际上我们应该往index=5的位置存放数据 我们需要解决这个bug：只需要index+数组长度就可以得到正确索引
    我们先对front+index进行判断 若为-1 即front=0 index=-1的情况 此时需要index+数组长度来获取正确索引
    若front不为0 则front+index>=0 则通过取模正常获取真实索引
     */
    private int index(int index) {
//        return (front + index) % elements.length;
        index += front;
        if(index < 0){
            return index + elements.length;
        }
//        return index % elements.length;
        return index - (index >= elements.length ? elements.length : 0);
    }
    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("capcacity=").append(elements.length)
                .append(" size=").append(size)
                .append(" front=").append(front)
                .append(", [");
        for (int i = 0; i < elements.length; i++) {
            if (i != 0) {
                string.append(", ");
            }

            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;

        // 新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];
        }
        elements = newElements;

        // 重置front
        front = 0;
    }
}
