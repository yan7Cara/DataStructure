import tree.BinaryTree;
import tree.RBTree;
import java.util.Comparator;

public class TreeSet<E> implements Set<E> {
    private RBTree<E> tree = new RBTree<>();

    public TreeSet(){
        this(null);
    }

    public TreeSet(Comparator<E> comparator){
        tree = new RBTree<>(comparator);
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(E element) {
        return tree.contains(element);
    }

    @Override
    public void add(E element) {
        tree.add(element);//红黑树的添加默认会查重 去除重复
    }

    @Override
    public void remove(E element) {
        tree.remove(element);
    }

    @Override
    public void traversal(Visitor<E> visitor) {
        //红黑树的inorder传入的visitor和traversal传入的visitor不一样
        tree.inorder(new BinaryTree.Visitor<E>() {//传入红黑树的inorder的visitor
            @Override
            public boolean visit(E element) {
                return visitor.visit(element);//在红黑树的inorder的visitor中调用traversal的visitor
            }
        });
    }
}
