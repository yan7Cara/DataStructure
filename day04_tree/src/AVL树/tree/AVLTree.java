package AVL树.tree;

import java.util.Comparator;

public class AVLTree<E> extends BST<E>{
    public AVLTree(){
        this(null);
    }
    public AVLTree(Comparator<E> comparator) {
        super(comparator);
    }
    @Override
    protected void afterAdd(Node<E> node){
        while((node = node.parent) != null){
            if (isBalanceFactory(node)){
                //更新高度
                updateHeight(node);
            }else {
                //恢复平衡
                rebalance(node);
                break;//整棵树恢复平衡
            }
        }
    }

    @Override
    protected void afterRemove(Node<E> node){
        while((node = node.parent) != null){
            if (isBalanceFactory(node)){
                //更新高度
                updateHeight(node);
            }else {
                //恢复平衡
                rebalance(node);
            }
        }
    }

    @Override
    protected Node<E> createNode(E element,Node<E> parent){//重写createNode方法 创建AVLNode节点
        return new AVLNode<>(element,parent);
    }

    private boolean isBalanceFactory(Node<E> node){
        return Math.abs(((AVLNode<E>)node).balanceFactor()) <= 1;//若平衡因子小于等于1 则平衡
    }

    //更新高度
    private void updateHeight(Node<E> node){
        ((AVLNode<E>)node).updateHeight();
    }

    //恢复平衡
    private void rebalance2(Node<E> grand){
        Node<E> parent = ((AVLNode<E>)grand).tallChild();
        Node<E> node = ((AVLNode<E>)parent).tallChild();
        if (parent.isLeftChild()){//L
            if (node.isLeftChild()){//LL
                rotateRight(grand);
            }else {//LR
                rotateLeft(parent);
                rotateRight(grand);
            }
        }else {//R
            if (node.isLeftChild()){//RL
                rotateRight(parent);
                rotateLeft(grand);
            }else {//RR
                rotateLeft(grand);
            }
        }
    }

    //恢复平衡
    private void rebalance(Node<E> grand) {
        Node<E> parent = ((AVLNode<E>)grand).tallChild();
        Node<E> node = ((AVLNode<E>)parent).tallChild();
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                rotate(grand,node.left,node,node.right,parent,parent.right,grand,grand.right);
            } else { // LR
                rotate(grand, parent.left,parent, node.left, node, node.right, grand,grand.right);
            }
        } else { // R
            if (node.isLeftChild()) { // RL
                rotate(grand, grand.left,grand, node.left, node, node.right, parent,parent.right);
            } else { // RR
                rotate(grand, grand.right,grand, parent.left, parent, node.left, node,node.right);
            }
        }
    }

    private void rotate(
            Node<E> r,
            Node<E> a,Node<E> b,Node<E> c,
            Node<E> d,
            Node<E> e,Node<E> f,Node<E> g
    ){
        //让d成为这棵子树的根节点
        d.parent = r.parent;
        if (r.isLeftChild()){
            r.parent.left = d;
        }else if (r.isRightChild()){
            r.parent.right = d;
        }else {
            root = d;
        }

        //a-b-c
        b.left = a;
        if (a != null){
            a.parent = b;
        }
        b.right = c;
        if (c != null){
            c.parent = b;
        }
        updateHeight(b);

        //e-f-g
        f.left = e;
        if (e != null){
            e.parent = f;
        }
        f.right = g;
        if (g != null){
            g.parent = f;
        }
        updateHeight(f);

        //b-d-f
        d.left = b;
        d.right = f;
        b.parent = d;
        f.parent = d;
        updateHeight(d);
    }

    private void rotateLeft(Node<E> grand){
        Node<E> parent = grand.right;
        Node<E> child = parent.left;//child没有别的含义 只是存放parent.left的一个变量 你也可以叫他aaa 他不是图中的n

        grand.right = child;
        parent.left = grand;

        afterRotate(grand,parent,child);
    }

    private void rotateRight(Node<E> grand){
        Node<E> parent = grand.left;
        Node<E> child = parent.right;

        grand.left = child;
        parent.right = grand;

        afterRotate(grand,parent,child);
    }

    private void afterRotate(Node<E> grand,Node<E> parent,Node<E> child){
        //让parent成为子树的根节点
        parent.parent = grand.parent;
        if(grand.isLeftChild()){
            grand.parent.left = parent;
        }else if(grand.isRightChild()){
            grand.parent.right = parent;
        }else {//grand是根节点
            root = parent;
        }

        //更新child的parent 要考虑child不存在的情况
        if (child != null){
            child.parent = grand;
        }

        //更新grand的parent
        grand.parent = parent;

        //更新高度
        updateHeight(grand);
        updateHeight(parent);
    }

    private static class AVLNode<E> extends Node<E>{
        int height = 1;//添加一个节点 该节点必然是叶子节点 所以高度为1
        public AVLNode(E element,Node<E> parent){
            super(element,parent);
        }
        public int balanceFactor(){//平衡因子 = 左子节点的高度 - 右子节点的高度
            /*
            节点的高度属性应该定义在AVLTree中 而不能定义在BinaryTree中 因为继承BinaryTree的方法不一定要用到height属性
            如果定义在BinaryTree中 那么BST不用height 但BinaryTree依旧会在内存中创建height 浪费了内存 所以将height定义在AVLTree中
            于是AVLTree中定义新的节点AVLNode 继承Node节点增加了height属性（因为AVLNode是在原来Node节点的基础上增加功能）
            在main函数中调用add方法添加节点都是创建Node节点 然而我们在添加节点时需要获得节点的平衡因子
            所以再添加节点时创建的节点应该是AVLNode节点 因此 我们需要在BinaryTree中定义createNode方法
             */
            //left right都是Node中的属性 没有
            int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            return leftHeight - rightHeight;
        }

        public void updateHeight(){
            int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            height = Math.max(leftHeight,rightHeight) + 1;
        }

        //失衡只会发生在height比较大的那一端 即节点的子节点高度比较高的那一端 所以我们需要找到height比较大的那一端
        public Node<E> tallChild(){
            //求节点的高度
            int leftHeight = left == null ? 0 : ((AVLNode<E>)left).height;
            int rightHeight = right == null ? 0 : ((AVLNode<E>)right).height;
            //比较节点的高度
            if (leftHeight > rightHeight) return left;
            if (leftHeight < rightHeight) return right;
            //如果两端一样高 若节点是其父节点的左/右节点 则返回左/右子节点
            return isLeftChild() ? left : right;
        }

        @Override
        public String toString() {
            String parentString = "null";
            if (parent != null) {
                parentString = parent.element.toString();
            }
            return element + "_p(" + parentString + ")_h(" + height + ")";
        }
    }
}
