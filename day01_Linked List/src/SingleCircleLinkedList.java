public class SingleCircleLinkedList<E> extends AbstractList<E>{
    private Node<E> first;//指向第一个元素

    private static class Node<E>{
        E element;
        Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(element).append("_").append(next.element);
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

        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
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
        if (index == 0){
            Node<E> newFirst = new Node<>(element,first);//新添加的头结点
            //拿到最后一个节点
            Node<E> last = (size == 0 ) ? newFirst : node(size - 1);//若最初是空链表 现在我们加入第一个数据
            // 然后第一个数据就是最后一个数据 若size = 0 就代表最开始是空链表  现在我们加入第一个数据 first指向first
            last.next = newFirst;
            first = newFirst;
        }else {
            Node<E> prev = node(index - 1);
            prev.next = new Node<>(element,prev.next);
        }
        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        Node<E> node = first;//用来存储要删除的节点 最初默认为first 你也可以不默认为first
        if (index == 0){
            if (size == 1){
                first = null;
            }else {
                Node<E> last = node(size - 1);
                first = first.next;
                last.next = first;
            }
        }else {
            Node<E> prev = node(index - 1);
            node = prev.next;
            prev.next = node.next;
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
