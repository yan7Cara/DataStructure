import printer.BinaryTreeInfo;

import java.util.Comparator;

//二叉堆:逻辑结构是完全二叉树 也叫完全二叉堆
//此处默认为最大堆
@SuppressWarnings("unchecked")
public class BinaryHeap<E> extends AbstractHeap<E> implements BinaryTreeInfo {
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public BinaryHeap(Comparator<E> comparator){
//        super(comparator);
//        this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        this(null,comparator);
    }
    public BinaryHeap(){
        this(null,null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    @Override
    public void add(E element) {
        elementNotNullCheck(element);
        ensureCapacity(size + 1);
//        添加时 直接将新元素添加到数组的最后 然后调用上滤函数将新添加的元素放在合适的位置
        elements[size++] = element;
        siftUP(size - 1);
    }

    @Override
    public E get() {
        emptyCheck();
        return elements[0];
    }

    /*
    堆的删除其实是删除根元素 思路：
    最后一个元素覆盖根元素 删除最后一个元素 循环执行以下步骤：最后一个元素和子节点中最大的元素进行比较
    若最后一个元素大 则结束 若最后一个元素小 则二者交换位置 循环 最后一个元素和自己子节点中最大的元素进行比较……
    这个过程叫下滤 可以像添加操作那样优化
     */
    @Override
    public E remove() {
        emptyCheck();
        E root = elements[0];

//        elements[0] = elements[size - 1];
//        elements[size - 1] = null;
//        size --;
        //上述代码优化为：
        int lastIndex = --size;
        elements[0] = elements[lastIndex];
        elements[lastIndex] = null;

        siftDown(0);
        return root;
    }

    /*
    删除堆顶元素的同时插入一个新元素
    思路：直接将新添加的元素放在堆顶 覆盖堆顶原来的元素 然后再进行下滤
     */
    @Override
    public E replace(E element) {
        elementNotNullCheck(element);
        E root = null;
        if (size == 0){
            elements[0] = element;
            size++;
        }else {
            root = elements[0];
            elements[0] = element;
            siftDown(0);
        }
        return root;
    }

    @Override
    public Object root() {
        return 0;
    }

    @Override
    public Object left(Object node) {
        int index = ((int)node << 1) + 1;
        return index >= size ? null : index;
    }

    @Override
    public Object right(Object node) {
        int index = ((int)node << 1) + 2;
        return index >= size ? null : index;
    }

    @Override
    public Object string(Object node) {
        return elements[(int)node];
    }

    private void emptyCheck(){
        if (size == 0){
            throw new IndexOutOfBoundsException("Heap is empty!");
        }
    }
    private void elementNotNullCheck(E element){
        if (element == null){
            throw new IllegalArgumentException("element must not be null!");
        }
    }

    private void ensureCapacity(int capacity){
        int oldCapacity = elements.length;
        if (oldCapacity >= capacity) return;
        //新容量为旧容量的1.5倍
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        E[] newElements = (E[])new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;

        System.out.println(oldCapacity + "扩容为" + newCapacity);
    }

    //让index位置的元素上滤
//    private void siftUP(int index){
//        E e = elements[index];
//        while (index > 0){//有父节点才能上滤
//            int pindex = (index - 1) >> 1;
//            E p = elements[pindex];
//            if (compare(e,p) <= 0) return;
//
//            //交换pindex index位置的内容
//            E tmp = elements[index];
//            elements[index] = elements[pindex];
//            elements[pindex] = tmp;
//            //重新赋值index
//            index = pindex;//交换内容之后 新添加的节点就在他原来父节点的位置 那么 新添加节点的索引应该为原来他父节点的索引
//
//        }
//    }
    /*
    代码优化
    上滤函数设计思路：
    先取出新添加的元素e 获取e的父元素p 若p<e 则p放在e的位置 e的index变为p的pindex
    再取出pindex的父元素pp 若pp<e 则pp放在p的位置 e的index变为pp的ppindex
    如此循环 一直拿e和父元素一级一级比较 若父元素小于e 则父元素下来（放在e的位置）
    若父元素大于e 再将e的元素放在该位置上
     */
    private void siftUP(int index){
        E e = elements[index];
        while (index > 0){//有父节点才能上滤
            int pindex = (index - 1) >> 1;//index元素的父节点
            E p = elements[pindex];//父节点元素
            if (compare(e,p) <= 0) break;//若新添加的元素小于父节点的元素 则退出此次循环
            //新添加的元素大于父节点的元素 执行以下代码
            //将父元素的存储在index的位置 即发现新添加元素的父元素小于新添加的元素 则父元素放在新添加的元素的位置
            elements[index] = p;
            //重新赋值index
            index = pindex;
        }
        elements[index] = e;
    }

    //下滤
    private void siftDown(int index) {
        E element = elements[index];
        int self = size >> 1;//除以2
        //第一个叶子节点的索引==非叶子节点的数量
        //index小于第一个叶子节点的索引
        while (index < self){//必须保证index的位置是非叶子节点 否则不能下滤
            //index的节点有两种情况：1只有左节点 2同时有左右子节点
            int childIndex = (index << 1) + 1;//左子节点索引
            E child = elements[childIndex];//左子节点元素
            int rightIndex = childIndex + 1;//右子节点索引
            //选出左右子节点最大的那个
            //如果右子节点的索引小于数组的长度 即存在右子节点
            if(rightIndex < size && compare(elements[rightIndex],child) > 0){//若右子节点大
                //默认使用左子节点child进行比较 若右子节点比左子节点大 则把右子节点的元素放在child里用于比较
                child = elements[childIndex = rightIndex];
            }

            if (compare(element,child) >= 0) break;
            //element<child
            //将子节点存放到index位置
            elements[index] = child;
            //重新设置index
            index = childIndex;
        }
        elements[index] = element;
    }

    //给你一个数组 数组里面的元素无序 要求根据这个数组建立一个堆：自上而下的上滤：自下而上的下滤：
    public BinaryHeap(E[] elements,Comparator comparator){
        super(comparator);

        if (elements == null || elements.length == 0){
            this.elements = (E[]) new Object[DEFAULT_CAPACITY];
        }else {
            size = elements.length;
            int capacity = Math.max(elements.length,DEFAULT_CAPACITY);
            this.elements = (E[]) new Object[capacity];
            for (int i = 0; i < elements.length; i++) {
                this.elements[i] = elements[i];
            }
        }
        heapify();
    }
    public BinaryHeap(E[] elements){
        this(elements,null);
    }
    /*
    批量建堆
    1.自上而下的上滤：从数组为索引1的位置开始依次遍历 每个遍历的结果进行自上而下的上滤
    2.自下而上的下滤：从最后一个非叶子节点（叶子节点前面一个 即索引(size >> 1) - 1的位置）开始遍历
                        每个遍历的结果进行自下而上的下滤 推荐
     */
    private void heapify(){
        //1.自上而下的上滤
//        for (int i = 1; i < size; i++) {
//            siftUP(i);
//        }

        //2.自下而上的下滤
        for (int i = ((size >> 1) - 1); i >= 0; i--) {
            siftDown(i);
        }
    }
}
