/*
删除该指针的下一个下一个下一个元素
 */
public class JosephusCircleLinkedList<E> extends AbstractList<E>{
    private Node<E> first;//指向第一个元素
    private Node<E> last;//指向最后一个元素
    private Node<E> current;//指向某个节点

    public void reset(){
        current = first;
    }

    public E next(){
        if (current == null) return null;
        current = current.next;
        return current.element;
    }

    public E remove(){
        if (current == null) return null;
        Node<E> next = current.next;
        E element = remove(current);
        if (size == 0){
            current = null;
        }else {
            current = next;
        }
        return element;
    }

    private static class Node<E>{
        E element;
        Node<E> next;
        Node<E> prev;

        public Node(Node<E> prev,E element, Node<E> next) {
            this.prev = prev;
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (prev != null) {
                sb.append(prev.element);
            } else {
                sb.append("null");
            }
            sb.append("_").append(element).append("_");
            if (next != null) {
                sb.append(next.element);
            } else {
                sb.append("null");
            }

            return sb.toString();
        }
    }

    @Override
    public String toString(){
        Node<E> node = first;
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(",[");
        for (int i = 0; i < size; i++) {
            if (i != 0){
                string.append(",");
            }
            string.append(node);
            node = node.next;
        }
        string.append("]");
        return string.toString();
    }

    //获取index位置对应结点对象
    private Node<E> node(int index){
        rangeCheck(index);
        Node<E> node;
        if (index <= (size >> 1)){
            node = first;
            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        }else {
            node = last;
            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }
        return node;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
    }

    @Override
    public E get(int index) {
        return node(index).element;
    }

    @Override
    public E set(int index, E element) {
        Node<E> node = node(index);
        E old = node.element;
        node.element = element;
        return old;
    }

    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        if (index == size){
            Node<E> oldLast = last;
            last = new Node<>(oldLast,element,first);
            if (oldLast == null ){//链表是空的 现在我们加入第一个数据
                first = last;
                first.next = first;
                first.prev = first;
            }else {
                oldLast.next = last;
                first.prev = last;
            }
        }else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode = new Node<E>(prev,element,next);
            next.prev = newNode;
            prev.next = newNode;
            if (index == 0 ){//或者index == first
                first = newNode;
            }
        }
        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        return remove(node(index));
    }

    private E remove(Node<E> node) {
        if(size == 1){
            first = null;
            last = null;
        }else {
            Node<E> prev = node.prev;
            Node<E> next = node.next;
            prev.next = next;
            next.prev = prev;
            if (node == first){//index = 0 删除第一个
                first = next;
            }
            if (node == last){//index = size - 1 删除最后一个
                last = prev;
            }
        }
        size--;
        return node.element;
    }

    //查看元素索引
    @Override
    public int indexOf(E element) {
        Node<E> node = first;
        if(element == null){
            for (int i = 0; i < size; i++) {
                if (node.element == null) return i;
                node = node.next;
            }
        }else {
            for (int i = 0; i < size; i++) {
                if (element.equals(node.element)) return i;
                node = node.next;
            }
        }
        return ELEMENT_NOT_FOUND;
    }
}
