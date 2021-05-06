package 红黑树.tree;

import java.util.Comparator;

public class AVLTree<E> extends BBST<E> {
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
//    protected void afterRemove(Node<E> node,Node<E> replacement){
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
    protected void afterRotate(Node<E> grand, Node<E> parent, Node<E> child) {
        super.afterRotate(grand, parent, child);

        // 更新高度
        updateHeight(grand);
        updateHeight(parent);
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

    @Override
    protected void rotate(Node<E> r, Node<E> a,Node<E> b, Node<E> c, Node<E> d, Node<E> e, Node<E> f,Node<E> g) {
        super.rotate(r,a, b, c, d, e, f,g);

        // 更新高度
        updateHeight(b);
        updateHeight(f);
        updateHeight(d);
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
