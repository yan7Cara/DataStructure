package 二叉搜索树;
import 二叉搜索树.printer.BinaryTreeInfo;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTree<E> implements BinaryTreeInfo {
    private int size;
    private Node<E> root;
    private Comparator<E> comparator;//比较器 提供比较逻辑

    public BinarySearchTree(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public BinarySearchTree() {
        this(null);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public void add(E element) {
        elementNotNullCheck(element);
        //添加第一个节点 即添加根节点 原先是个空树
        if (root == null) {
            root = new Node<>(element, null);
            size++;
            return;
        }
        //添加的不是第一个节点
        //1.找到父节点
        Node<E> node = root;
        Node<E> parent = root;
        int cmp = 0;
        while (node != null) {
            cmp = compare(element, node.element);
            parent = node;
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {//找到的节点和打算添加的节点相等：1.直接返回 2.用新节点覆盖旧节点 即用打算添加的节点覆盖找到的节点
                return;
            }
        }
        //2.看看插入到父节点的哪个位置
        Node<E> newNode = new Node<>(element, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;
    }

    public void remove(E element) {
        remove(node(element));
    }

    /*
    删除节点：
    1.若节点为叶子节点：判断该叶子节点是其父节点的左子节点还是右子节点 然后让其父节点的左/右子节点置空
                        若该叶子节点的父节点为null 则表明该树只有这一个节点 删除节点就是将root置空
    2.若节点度为1 即只有一个子节点：判断该节点是其父节点的左子节点还是右子节点
    判断该节点的着唯一一个子节点是自己的左子节点还是右子节点
    然后用子节点代替原节点的位置 即该节点的父节点的左/右子节点=该节点的子节点的左/右子节点
    该节点的子节点的左/右子节点的父节点=该节点的父节点的左/右子节点 若该节点是根节点 即该节点的父节点为null
    那么root=该节点的左/右子节点 该节点的左/右子节点的父节点=null
    3.若节点度为2 即有左右两个子节点：先用该节点的前驱/后继节点的值覆盖该节点的值 然后删除相应的前驱/后继节点
    若一个节点的度为2 那么他的前驱/后继节点的度只能为1或0
     */
    private void remove(Node<E> node){
        if (node == null) return;
        size --;
        if (node.hasTwoChildren()){//node的度为2
            //找到要删除的节点的后继节点
            Node<E> s = successor(node);
            //用后继节点的值覆盖掉要删除的节点的值
            node.element = s.element;
            //后继节点赋值给node
            node = s;
        }

        //删除node节点（node的度一定是0或1）
        Node<E> replacement = node.left != null ? node.left : node.right;
        if (replacement != null){//node是度为1的节点
            //更改parent
            replacement.parent = node.parent;
            //更改parent的left right的指向
            if (node.parent == null){//node是度为1 的节点并且是根节点
                root = replacement;
            }else if (node == node.parent.left){
                node.parent.left = replacement;
            }else if (node == node.right){
                node.parent.right = replacement;
            }
        }else if (node.parent == null){//node是根节点
            root = null;
        }else {//node是叶子节点 但不是根节点
            if (node == node.parent.left){
                node.parent.left = null;
            }else {//node == node.parent.right
                node.parent.right = null;
            }
        }
    }

    //根据元素查找节点
    private Node<E> node(E element){
        Node<E> node = root;
        while (node != null){
            int cmp = compare(element,node.element);
            if (cmp == 0){
                return node;
            }else if(cmp > 0){
                node = node.right;
            }else {// cmp < 0
                node = node.left;
            }
        }
        return null;
    }

    public boolean contains(E element) {
        return node(element) != null;
    }

    //前序遍历的遍历顺序：根节点 前序遍历左子树 前序遍历右子树 即：根 左 右 或 根 右 左
    //遍历的重点是根节点 先遍历根节点就是前序遍历 第二遍历根节点就是中序遍历 最后遍历根节点就是后序遍历
    //应用：树状结构展示 注意左右子树的顺序
//   public void preorderTraversal(){
//        preorderTraversal(root);
//    }
//    private void preorderTraversal(Node<E> node){
//        if (node == null) return;
//        System.out.println(node.element);
//        preorderTraversal(node.left);
//        preorderTraversal(node.right);
//    }

    public void preorder(Visitor<E> visitor) {
        if (visitor == null) return;
        preorder(root, visitor);
    }

    private void preorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;
//        if (visitor.stop == true) return;
        visitor.stop = visitor.visit(node.element);//调用visit方法执行对节点的操作 获得返回值
        preorder(node.left, visitor);
        preorder(node.right, visitor);
    }

    //中序遍历的遍历顺序：中序遍历左子树 根节点 中序遍历右子树 即：左 根 右 或 右 根 左
    //二叉搜索树的中序遍历结果是升序或降序
    //中序遍历的应用：二叉搜索树的中序遍历按升序或降序处理结点
//    public void inorderTraversal(){
//        inorderTraversal(root);
//    }
//    private void inorderTraversal(Node<E> node){
//        if (node == null) return;
//        inorderTraversal(node.left);
//        System.out.println(node.element);
//        inorderTraversal(node.right);
//    }
    public void inorder(Visitor<E> visitor) {
        if (visitor == null) return;
        inorder(root, visitor);
    }

    /*
    递归思想:
    主函数调用inorder函数并传入visitor的匿名方法 由于visitor不为空 所以inorder方法调用inorder方法并把根节点和visitor方法传进去
    由于根节点和visitor.stop都不为空 所以调用inorder方法 并将根节点的左子节点和visitor方法传入 继续调用inorder方法
    并将根节点的左子节点的左子节点和visitor方法传入 如此循环调用inorder方法 传入left节点和visitor方法
    直到遍历到叶子节点 叶子节点的左节点为空 所以返回到叶子节点进行操作 此时还没有执行visit方法 所以visitor.stop为默认值false
    执行visit方法 对叶子节点进行操作 获得返回值 若visitor.stop == true 调用inorder方法 传入right节点和visitor方法
    此时由于visitor.stop == true 故返回到左叶子节点的父节点 由于visitor.stop == true 故一层层返回到根节点的左节点
     */
    private void inorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;
        inorder(node.left, visitor);
        if (visitor.stop == true) return;
        visitor.stop = visitor.visit(node.element);
        inorder(node.right, visitor);
    }

    //后序遍历的遍历顺序：后序遍历左子树 后序遍历右子树 根节点 即：左 右 根 或 右 左 根
    //后序遍历的应用：适用于一些先子后父的操作
//    public void postorderTraversal(){
//        postorderTraversal(root);
//    }
//    private void postorderTraversal(Node<E> node){
//        if (node == null) return;
//        postorderTraversal(node.left);
//        postorderTraversal(node.right);
//        System.out.println(node.element);
//    }
    public void postorder(Visitor<E> visitor) {
        if (visitor == null) return;
        postorder(root, visitor);
    }

    private void postorder(Node<E> node, Visitor<E> visitor) {
        if (node == null || visitor.stop) return;
        postorder(node.left, visitor);
        postorder(node.right, visitor);
        if (visitor.stop == true) return;
        visitor.stop = visitor.visit(node.element);
    }
    /*
    层序遍历的遍历顺序：从上到下 从左到右依次访问每一个节点
    层序遍历的应用：计算二叉树的高度 判断一棵树是否为完全二叉树
    实现思路：使用队列
    1.将根节点入队
    1.循环执行以下操作 直到队列为空
        （1）将队头节点A出队 进行访问
        （2）将A的左子节点入队
        （3）将A的右子节点入队
    按照上述步骤：最初root入队 此时队列只有root 不为空 将root出队进行操作 然后将root的左子节点A入队 将root的右子节点B入队
    此时队列不为空 队头A出队 进行操作 将A的左子节点C入队 将A的右子节点D入队 队列不为空 队头B出队 进行操作
    将B的左子节点E入队 将A的右子节点F入队 此时队列不为空 有CDEF 如此循环遍历
     */
//    public void levelOrderTraversal(){
//        if (root == null) return;
//        Queue<Node<E>> queue = new LinkedList<>();
//        queue.offer(root);
//        while(!queue.isEmpty()){
//            Node<E> node = queue.poll();
//            System.out.println(node.element);
//            if (node.left != null){
//                queue.offer(node.left);
//            }
//            if (node.right != null){
//                queue.offer(node.right);
//            }
//        }
//    }

    public void levelOrder(Visitor<E> visitor) {//层序遍历的代码和思想一定要背下来
        if (root == null || visitor == null) return;
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (visitor.visit(node.element)) return;//visitor返回true 则结束循环 不再遍历
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    /*
    在前面的levelOrderTraversal preOrderTraversal inOrderTraversal postOrderTraversal方法中 我们把获取到的结点直接打印
    但是在实际开发中 我们获取到了结点 不一定是把他直接打印出来 也许我们需要对结点进行一些操作再打印
    每次获取到结点 对结点的操作都不一样 所以我们不能把他写死了 我们定义一个visitor接口 用户可以实现该接口来定义对结点操作的方法
    重新定义levelOrder preOrder inOrder postOrder方法 在需要对结点进行操作时 直接调用visitor接口
     */
//    public static interface Visitor<E> {
    /*
    之前我们定义的是Visitor接口 但是由于接口只能存放方法 不能存放变量 所以我们把Visitor改为抽象类
    抽象类可以定义变量和抽象方法 并且继承抽象类的方法必须实现抽象类的抽象方法
     */
    public static abstract class Visitor<E> {
        //        void visit(E element);
        /*
        在遍历的时候 levelOrder preOrder inOrder postOrder方法都是从根节点一直遍历到叶子节点 那么假设我们现在有一个需求：
        遍历出树的前三个节点进行操作 别的都不遍历 那么我们可以定义visit方法返回一个布尔值
        在levelOrder preOrder inOrder postOrder方法调用visit方法时会获得返回值 我们根据返回的布尔值来决定是否继续遍历
         */
        boolean stop;

        abstract boolean visit(E element);//返回true 代表停止遍历
    }

    public int height1() {
        return height(root);
    }

    private int height(Node<E> node) {
        if (node == null) return 0;
        //一个节点的高度等于该节点左右子节点的高度的最大值+1 递归调用获取节点的高度
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int height() {
        if (root == null) return 0;

        // 树的高度
        int height = 0;
        // 存储着每一层的元素数量
        int levelSize = 1;
        Queue<Node<E>> queue = new LinkedList<>();
        /*
        根节点入队 队列不为空 队列弹出根节点 本层元素数量-1 队列空 根节点的左节点入队 根节点的右节点入队 由于此时levelSize=0
        意味着要访问下一层 将队列的长度赋值给levelSize 并且高度+1 因为队列不为空 所以弹出根节点的左节点 levelSize-1
        根节点的左节点的左节点入队 根节点的左节点的右节点入队 此时levelSize不为0且队列不为空 弹出根节点的右节点 levelSize-1 此时levelSize=0
        根节点的右节点的左节点入队 根节点的右节点的右节点入队 此时levelSize为0 意味着要访问下一层 将队列的长度赋值给levelSize
        并且高度+1 如此循环 最后返回height
         */
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();//弹出
            levelSize--;//队列弹出元素 元素数量-1

            if (node.left != null) {
                queue.offer(node.left);//入队
            }

            if (node.right != null) {
                queue.offer(node.right);
            }

            if (levelSize == 0) { // 意味着即将要访问下一层
                levelSize = queue.size();
                height++;
            }
        }
        return height;
    }

    /*
    完全二叉树：叶子节点只会出现在最后两层 且最后一层的叶子节点都靠左对齐
     */
//    public boolean isComplete() {//判断是否为完全二叉树
//        if (root == null) return false;
//        Queue<Node<E>> queue = new LinkedList<>();
//        queue.offer(root);
//        boolean leaf = false;
//        while (!queue.isEmpty()) {
//            Node<E> node = queue.poll();
//            if (leaf && !node.isLeaf()) return false;
//            if (node.hasTwoChildren()){
//                queue.offer(node.left);
//                queue.offer(node.right);
//            }else if (node.left == null && node.right != null){
//                return false;
//            }else {//后面遍历的节点都必须是叶子节点
//    // node.left != null && node.right == null或// node.left == null && node.right == null
//    // 由于可能出现node.left != null && node.right == null的情况 此时我们还需要将node.left入队
//                leaf = true;
//                if (node.left != null){
//                    queue.offer(node.left);
//                }
//            }
//        }
//        return true;
//    }
    //对上面的代码进行改善
    public boolean isComplete() {//判断是否为完全二叉树
        if (root == null) return false;//若为空树 肯定不是完全二叉树
        Queue<Node<E>> queue = new LinkedList<>();
        queue.offer(root);
        boolean leaf = false;
        /*
        根节点入队 队列不为空 leaf=false 若根节点的左子节点不为空 则入队 若根节点的左子节点为空 右子节点不为空
        则不是完全二叉树 若根节点的右子节点不为空 则入队：
        （1）若根节点的右子节点为空 则leaf=true 根节点的左子节点出队 因为完全二叉树的叶子节点只会出现在最后两层
        所以此时根节点的左子节点必须是叶子节点 才是完全二叉树 因为leaf=true就代表了上一个节点（此处为根节点）的右子节点为空
        即叶子节点 若此时根节点的左子节点不是叶子节点 则不是完全二叉树
        （2）若根节点的右子节点不为空 则继续循环遍历
         */
        while (!queue.isEmpty()) {
            Node<E> node = queue.poll();
            if (leaf && !node.isLeaf()) return false;

            if (node.left != null) {
                queue.offer(node.left);
            } else if (node.right != null) { // node.left == null && node.right != null
                return false;
            }

            if (node.right != null) {
                queue.offer(node.right);
            } else { // node.right == null 左边不确定 此时后面遍历的节点都必须是叶子节点
                leaf = true;
            }
        }
        return true;
    }

    /*
    前驱节点：中序遍历时的前一个节点
    如果是二叉搜索树 前驱节点就是前一个比他小的节点
    中序遍历：左 根 右
     */
    private Node<E> predecessor(Node<E> node){//找前驱节点
        if (node == null) return node;
        Node<E> p = node.left;
        if (p != null){//有左子节点 那么左子节点的最后一个右子节点（此时遍历完左子节点）就是该点的前驱节点
            //因为该点的左子节点遍历完了就该遍历该点了
            while (p.right != null){//找该点的左子节点的最后一个右子节点
                p = p.right;
            }
            return p;
        }
        /*
        没有左子节点：该节点的前驱节点一定是他的父节点或祖父节点 但是可能他在他的父节点的左边
        那他的父节点肯定不是他的前驱节点 因为要先遍历该节点再遍历该节点的根节点（这里也就是他的父节点）
        所以该节点的前驱节点可能是他的祖父节点 但是可能他的父节点还是他的祖父节点的左子节点
        由于要先遍历完该节点的左子节点才遍历该子节点 所以要找他的父节点是他的祖父节点的右子节点
        从父节点 祖父节点中寻找前驱节点
         */
        while (node.parent != null && node == node.parent.left){
            node = node.parent;
        }
        return node.parent;
    }

    private Node<E> successor(Node<E> node){//找后继节点
        if (node == null) return node;
        Node<E> p = node.right;
        if (p != null){
            while (p.left != null){
                p = p.left;
            }
            return p;
        }
        //从父节点 祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.right){
            node = node.parent;
        }
        return node.parent;
    }

    @Override
    public Object root() {
        return root;
    }

    @Override
    public Object left(Object node) {
        return ((Node<E>)node).left;
    }

    @Override
    public Object right(Object node) {
        return ((Node<E>)node).right;
    }

    @Override
    public Object string(Object node) {
        Node<E> myNode = (Node<E>)node;
        String parentString = null;
        if (myNode.parent != null){
            parentString = myNode.parent.element.toString();
        }
        return myNode.element + "_" + parentString;
    }
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        toString(root, sb, "");
//        return sb.toString();
//    }
//
//    private void toString(Node<E> node, StringBuilder sb, String prefix) {
//        if (node == null) return;
//
//        toString(node.left, sb, prefix + "L---");
//        sb.append(prefix).append(node.element).append("\n");
//        toString(node.right, sb, prefix + "R---");
//    }

    private static class Node<E>{
        E element;
        Node<E> left;
        Node<E> right;
        Node<E> parent;
        public Node(E element,Node<E> parent){
            this.element = element;
            this.parent = parent;
        }
        public boolean isLeaf() {//是否是叶子节点
            return left == null && right == null;
        }

        public boolean hasTwoChildren() {//是否有两个子节点
            return left != null && right != null;
        }
    }
    private void elementNotNullCheck(E element){
        if (element == null){
            throw new IllegalArgumentException("element must not be null!");
        }
    }
    //e1=e2 返回0 e1>e2 返回值大于0 e1<e2 返回值小于0
    private int compare(E e1,E e2){
        if (comparator != null){
            //传入了比较器 有自定义printer比较方法 那么就用比较器中的比较方法来比较
            return comparator.compare(e1,e2);
        }
        return ((Comparable<E>)e1).compareTo(e2);//没有传入比较器 强制将e1 e2转换为可比较的 然后按照默认方法进行比较
    }

}
