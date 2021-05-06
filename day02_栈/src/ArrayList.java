import java.util.Objects;

@SuppressWarnings("unchecked")
public class ArrayList<E> extends AbstractList<E> {
    private E[] elements;
    private static final int DEFAULT_CAPACITY = 10;

    public ArrayList(int capaticy){
        capaticy = (capaticy < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : capaticy;
        elements = (E[]) new Object[capaticy];
    }

    public ArrayList(){
        this(DEFAULT_CAPACITY);
    }

    //获取index位置的元素
    public E get(int index){
        rangeCheck(index);
        return elements[index];
    }

    //设置index位置的元素
    public E set(int index,E element){
        rangeCheck(index);
        E old = elements[index];
        elements[index] = element;
        return old;
    }

    //清除所有元素
    public void clear(){
        for(int i = 0;i< size;i++){
            elements[i] = null;
        }
        size = 0;
        //缩容
        if (elements != null && elements.length > DEFAULT_CAPACITY){
            elements = (E[])new Objects[DEFAULT_CAPACITY];
        }
    }

    //在index位置插入元素
    public void add(int index,E element){
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);

        for (int i = size;i > index;i--){
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    //删除index位置的元素
    public E remove(int index){
        rangeCheck(index);
        E old = elements[index];
        for (int i = index + 1; i < size; i++) {
            elements[i - 1] = elements[i];
        }
        elements[--size] = null;

        trim();
        return old;
    }

    //查看元素的索引
    public int indexOf(E element){
        if(element == null){
            for (int i = 0; i < size; i++) {
                if (elements[i] == null) return i;
            }
        }else {
            for (int i = 0; i < size; i++) {
                if (element.equals(elements[i])) return i;
            }
        }
        return ELEMENT_NOT_FOUND;
    }

    /*
    如果扩容倍数 缩容时机设计不当 有可能会导致复杂度震荡
    假设扩容时扩容为原来的2倍 当数据存储的容量为总容量的一半时缩容 假设最初的容量为20 且已存放20个数据 即容量满了
    当存放第21个数据时 需要扩容为40 此时复杂度由原来的o1变为on（存放正常数据时o1 扩容为on） 接下来 我们删除了第21个数据
    此时复杂度为o1 存放数据20个 容量40 此时需要缩容 复杂度由原来的o1变为on 我们又加入第21个数据 需要扩容为40
    此时复杂度由原来的o1变为on 接下来 我们删除了第21个数据 此时复杂度为o1 存放数据20个 容量40 此时需要缩容
    复杂度由原来的o1变为on …… 如此周而复始 复杂度震荡
    只要扩容的倍数乘以缩容的时机不为1 则可避免复杂度震荡
     */
    //保证要有capacity的容量
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

    //缩容
    private void trim(){
        int capacity = elements.length;
        int newCapacity = capacity >> 1;
        if (size >= (newCapacity) || capacity <= DEFAULT_CAPACITY) return;
        //剩余空间还很多
        E[] newElements = (E[])new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newElements[i] = elements[i];
        }
        elements = newElements;
        System.out.println(capacity + "缩容为" + newCapacity);
    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(",[");
        for (int i = 0; i < size; i++) {
            if (i != 0){
                string.append(",");
            }
            string.append(elements[i]);
        }
        string.append("]");
        return string.toString();
    }
}
