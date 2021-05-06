import list.LinkedList;
import list.List;

public class CircleQueue<E> {
    private int size;
    private E[] elements;
    private int front;

    public CircleQueue(){
        elements = (E[]) new Object[10];
    }

    private List<E> list = new LinkedList<>();

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void enQueue(E element){
        ensureCapacity(size + 1);
        elements[index(size)] = element;
        size++;
    }

    public E deQueue(){
        E frontElement = elements[front];
        elements[front] = null;
        front = index(1);
        size--;
        return frontElement;
    }

    public E front(){
        return elements[front];
    }

    public void clear(){
        for (int i = 0; i < size; i++) {
            elements[index(size)] = null;
        }
        size = 0;
        front = 0;
    }
    //循环队列：队列的底层是动态数组 假设我们有一个数组index=0~5 有一个指针front始终指向队头数据
    //最初front=0 若出队一个 即index=0的数据删除 front指向index=1 若此时我们需要往队列里面添加数据
    //当然不能往index=6的位置添加 应该加在index=0的位置 因为数组大小为0~5且index=0位置空着
    // 那么指针怎么知道应该添加到index=0位置呢 所以我们需要做一个映射来获取真实索引：size为数组中存放的元素个数
    // (size + front) % elements.length elements.length为数组长度
    //只要是循环队列 在获取索引时一定要这样写
    private int index(int index) {//获取真实索引
//        return (front + index) % elements.length;
        /*
        代码优化：
        因为运算符耗时 所以尽量不要使用* / %运算
        数学思想：首先我们考虑front+size不可能大于等于数组长度的两倍 即front+size<2*elements.length
        首先 front不可能等于elements.length的值 例如：假设有0~5的数组 那么elements.length=6 front最大为5
        其次 size的取值不可能等于elements.length的值 例如：假设有0~5的数组 size只能为0到5中的某个值
        当size=elements.length=6时表明0~5的数组已经满了 此时还要往数组里加数据 加在size=6的位置
        系统早就自动扩容了 elements.length早就大于6了 size肯定小于elements.length
        所以front+size<2*elements.length 由于size最小为-1 单独考虑 所以size>=0 front>0 所以front+index>0
        那么在这种情况下 当front+size=elements.length时 front+size%elements.length=0
        当front+size>elements.length时 front+size%elements.length=front+size-elements.length
        以上两种情况可以合为一种：当front+size>=elements.length时 front+size%elements.length=front+size-elements.length
        当front+size<elements.length时 front+size%elements.length=front+size
        由于size=front+size 所以
        size >= elements.length ? size-elements.length : size
        所以 size - （size >= elements.length ? elements.length : 0）
         */
        index += front;//index=front+index
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
    /**
     * 保证要有capacity的容量
     * @param capacity
     */
    //扩容：假设现在容量为3 front指向index=1 即队头在1 队尾在0 此时我们需要扩容 新的容量为5
    //那么我们新申请一个容量为5的数组 把front指向的数据（index=1的数据）放在新数组index=0的位置
    //然后依次把数据存放在新数组 把index=2的数据放在新数组index=1的位置 把index=0的数据放在新数组index=3的位置
    //然后front=0 front置零
    private void ensureCapacity(int capacity) {
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;//不用扩容 直接退出

        // 新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[index(i)];//(i + front) % elements.length获取真实索引
            //比如说假设现在容量为3 front指向index=1 即队头在1 队尾在0 那么（0+1）%3=1 那么elements[1]的数据
            //即front指向的数据 放在newElements[0]的位置
        }
        elements = newElements;

        // 重置front
        front = 0;
    }
}
