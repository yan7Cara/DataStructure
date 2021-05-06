package 红黑树.tree;

import java.util.Comparator;

public class RedBlackTree<E> extends BBST<E> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public RedBlackTree(){
        this(null);
    }

    public RedBlackTree(Comparator<E> comparator){
        super(comparator);
    }

    private Node<E> color(Node<E> node, boolean color) {//染色
        if (node == null) return node;
        ((RBNode<E>)node).color = color;
        return node;
    }

    private Node<E> red(Node<E> node) {
        return color(node, RED);
    }

    private Node<E> black(Node<E> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<E> node) {
        return node == null ? BLACK : ((RBNode<E>)node).color;
    }

    private boolean isBlack(Node<E> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRed(Node<E> node) {
        return colorOf(node) == RED;
    }

    private static class RBNode<E> extends Node<E>{
        boolean color = RED;//默认新加的节点是红色 因为新加的节点是红色比是黑色更容易满足红黑树的性质
        //新加的节点是红色 只有性质4可能不满足
        public RBNode(E element, Node<E> parent) {
            super(element, parent);
        }

        @Override
        public String toString() {
            String str = "";
            if (color == RED) {
                str = "R_";
            }
            return str + element.toString();
        }
    }

    @Override
    protected Node<E> createNode(E element, Node<E> parent) {
        return new RBNode<>(element, parent);
    }

    @Override
    protected void afterAdd (Node<E> node){//添加node节点
        Node<E> parent = node.parent;
        //添加的是根节点
        if (parent == null){
            black(node);
            return;
        }
        /*
        添加的所有情况：
        1.若parent是black 不用操作 直接返回
        2.若parent是red 添加后有两种情况：（1）添加后不会溢出（2）添加后会溢出
        判定这两种情况的条件是：添加的节点的uncle节点是不是red 若不是则不溢出 若是则溢出
        （1）添加后不会溢出 即添加的节点的uncle节点不是red
        parent染成BLACK，grand染成red 此时若为LL：grand进行右旋 若为RR：grand进行左旋 若为LR：parent左旋 grand右旋
        若为RL：parent右旋 grand左旋
        （2）添加后会溢出 即添加的节点的uncle节点是red
        parent、uncle 染成BLACK grand染成RED，向上合并 当成新添加的节点进行处理
         */
         //如果父节点是黑色 直接返回
        if (isBlack(parent)) return;
        //以上两个if语句不能调换位置
        // 叔父节点
        Node<E> uncle = parent.sibling();
        // 祖父节点
        Node<E> grand = red(parent.parent);
        if (isRed(uncle)) { // 叔父节点是红色【B树节点上溢】
            black(parent);
            black(uncle);
            // 把祖父节点当做是新添加的节点
            afterAdd(grand);
            return;
        }

        // 叔父节点不是红色
        if (parent.isLeftChild()) { // L
            if (node.isLeftChild()) { // LL
                black(parent);
            } else { // LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else { // R
            if (node.isLeftChild()) { // RL
                black(node);
                rotateRight(parent);
            } else { // RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    /*
    删除的所有情况：
    1.若删除的节点是red 直接删除
    2.若删除的节点是black：
    （1）拥有2个RED子节点的BLACK节点：会找到他的子节点（前驱/后继节点）覆盖他 然后删除子节点（前驱/后继）
    子节点肯定是red 直接用第一种情况来处理
    （2）拥有1个RED子节点的BLACK节点：判定条件：用以替代的子节点是red。删除black节点 将替代的子节点染成black
    （3）black叶子节点：删除后可能会导致下溢 也可能不会导致下溢
    a.兄弟节点为black 且兄弟节点至少有一个RED子节点
    删出后进行旋转 若为LL：兄弟节点的子节点进行右旋 若为RR：兄弟节点的子节点进行左旋 若可能为RR或RL 则选择RR
    对兄弟节点的子节点进行左旋 若可能为LL或LR 则选择LL 对兄弟节点的子节点进行右旋
    旋转之后的中心节点继承他的父节点的颜色 旋转之后的左右子节点染为black
    b.兄弟节点为black 且兄弟节点没有RED子节点：删除后兄弟节点染red 父节点染black
    （2）兄弟节点为red：兄弟节点染black 父节点染red 进行旋转 于是又回到兄弟节点是black的情况 按上述处理

     */
//    @Override
//    protected void afterRemove(Node<E> node,Node<E> replacement) {
//        // 如果删除的节点是红色
//        // 或者 用以取代删除节点的子节点是红色
//        if (isRed(node)) {
//            black(node);
//            return;
//        }
//
//        Node<E> parent = node.parent;
//        // 删除的是根节点
//        if (parent == null) return;
//
//        // 删除的是黑色叶子节点【下溢】
//        // 判断被删除的node是左还是右
//        boolean left = parent.left == null || node.isLeftChild();
//        Node<E> sibling = left ? parent.right : parent.left;
//        if (left) { // 被删除的节点在左边，兄弟节点在右边
//            if (isRed(sibling)) { // 兄弟节点是红色
//                black(sibling);
//                red(parent);
//                rotateLeft(parent);
//                // 更换兄弟
//                sibling = parent.right;
//            }
//
//            // 兄弟节点必然是黑色
//            if (isBlack(sibling.left) && isBlack(sibling.right)) {
//                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
//                boolean parentBlack = isBlack(parent);
//                black(parent);
//                red(sibling);
//                if (parentBlack) {
//                    afterRemove(parent,null);
//                }
//            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
//                // 兄弟节点的左边是黑色，兄弟要先旋转
//                if (isBlack(sibling.right)) {
//                    rotateRight(sibling);
//                    sibling = parent.right;
//                }
//
//                color(sibling, colorOf(parent));
//                black(sibling.right);
//                black(parent);
//                rotateLeft(parent);
//            }
//        } else { // 被删除的节点在右边，兄弟节点在左边
//            if (isRed(sibling)) { // 兄弟节点是红色
//                black(sibling);
//                red(parent);
//                rotateRight(parent);
//                // 更换兄弟
//                sibling = parent.left;
//            }
//
//            // 兄弟节点必然是黑色
//            if (isBlack(sibling.left) && isBlack(sibling.right)) {
//                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
//                boolean parentBlack = isBlack(parent);
//                black(parent);
//                red(sibling);
//                if (parentBlack) {
//                    afterRemove(parent,null);
//                }
//            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
//                // 兄弟节点的左边是黑色，兄弟要先旋转
//                if (isBlack(sibling.left)) {
//                    rotateLeft(sibling);
//                    sibling = parent.left;
//                }
//
//                color(sibling, colorOf(parent));
//                black(sibling.left);
//                black(parent);
//                rotateRight(parent);
//            }
//        }
//    }

    @Override
    protected void afterRemove(Node<E> node) {
        // 如果删除的节点是红色
        // 或者 用以取代删除节点的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<E> parent = node.parent;
        // 删除的是根节点
        if (parent == null) return;

        // 删除的是黑色叶子节点【下溢】
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<E> sibling = left ? parent.right : parent.left;
        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    afterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }
}
