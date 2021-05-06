/*
    在写代码时 要注意边界测试 比如 index为0 index为size时
*/
public class LinkedList<E> extends AbstractList<E>{
    private Node<E> first;//指向第一个元素
    private Node<E> last;//指向最后一个元素

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
            last = new Node<>(oldLast,element,null);
            if (oldLast == null ){//链表是空的 现在我们加入第一个数据
                first = last;
            }else {
                oldLast.next = last;
            }
        }else {
            Node<E> next = node(index);
            Node<E> prev = next.prev;
            Node<E> newNode = new Node<E>(prev,element,next);
            next.prev = newNode;
            if (prev == null){//index == 0
                first = newNode;
            }else {
                prev.next = newNode;
            }
        }


        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
//        Node<E> node = first;
//        if (index == 0){
//            first = first.next;
//        }else {
//            Node<E> prev = node(index - 1);
//            node = prev.next;
//            prev.next = node.next;
//        }

            Node<E> prev = node(index).prev;
            Node<E> next = node(index).next;
            if (prev == null){//index = 0 删除第一个
                first = next;
            }else {
                prev.next = next;
            }
            if (next == null){//index = size - 1 删除最后一个
                last = prev;
            }else {
                next.prev = prev;
            }

        size--;
        return node(index).element;
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
