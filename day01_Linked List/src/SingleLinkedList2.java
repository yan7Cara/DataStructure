/*
加入虚拟头结点的做法
有时候为了代码更精简 统一所有节点的处理逻辑 可以在最前面增加一个虚拟头结点（不存储数据）
 */
public class SingleLinkedList2<E> extends AbstractList<E>{
    private Node<E> first;//指向第一个元素

    public SingleLinkedList2() {
        first = new Node<>(null,null);
    }

    private static class Node<E>{
        E element;
        Node<E> next;

        public Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }
    }

    @Override
    public String toString(){
        Node<E> node = first.next;
        StringBuilder string = new StringBuilder();
        string.append("size=").append(size).append(",[");
        for (int i = 0; i < size; i++) {
            if (i != 0){
                string.append(",");
            }
            string.append(node.element);
            node = node.next;
        }
        string.append("]");
        return string.toString();
    }

    //获取index位置对应结点对象
    private Node<E> node(int index){
        rangeCheck(index);

        Node<E> node = first.next;
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

        Node<E> prev = index == 0 ? first : node(index - 1);//若index=0 那么拿到的就是first节点 否则就是前驱节点
        prev.next = new Node<>(element,prev.next);
        size++;
    }

    @Override
    public E remove(int index) {
        rangeCheck(index);
        Node<E> node = first;
        Node<E> prev = index == 0 ? first :node(index - 1);
        node = prev.next;
        prev.next = node.next;

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
