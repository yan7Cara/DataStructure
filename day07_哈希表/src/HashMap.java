import map.Map;
import printer.BinaryTreeInfo;
import printer.BinaryTrees;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

//当发生hash冲突时 hash值对应的区域可能存储的是链表或红黑树 此处我们统一为红黑树 并把红黑树的根节点放在hash表中
public class HashMap<K,V> implements Map<K,V> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private int size;
    private Node<K, V>[] table;//table是hash表 里面存放的是红黑树的根节点
    private static final int DEFAULT_CAPACITY = 1 << 4;//2的4次方
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;//装填因子=节点总数量/哈希表桶数组长度 也叫负载因子
    //JDK要求若装填因子超过0.75就扩容为原来的两倍

    public HashMap() {
        table = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        if (size == 0) return;
        size = 0;
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
    }

    @Override
    public V put(K key, V value) {
        resize();
        //根据key计算出哈希表中的索引index 该索引的位置为空 没有数据 则将传入的键值对放入此桶
        int index = index(key);
        Node<K,V> root = table[index];// 取出index位置的红黑树根节点
        if (root == null){
            root = createNode(key,value,null);
            table[index] = root;
            size ++;
            afterPut(root);
            return null;
        }

//        根据key计算出哈希表中的索引index 该索引的位置有数据 假设数据都存放在红黑树上面
        //添加新的节点到红黑树上面
        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        Node<K,V> result = null;
        K k1 = key;
        int h1 = hash(k1);
        boolean searched = false;//是否搜索过一遍
        /*
        假设传入的键值对在hash表中没有相同的 即需要将其添加到hash表中：程序一运行 发现h1>h2 h1<h2 Objects.equals(k1,k2)均不成立
        就开始扫描 扫描结束后发现没有相同的键值对 需要新添加 程序需要判断添加到节点的哪个位置 即哪个节点的左/右子节点
        若没有searched 由于需要新添加 所以需要比较 所以程序执行：
        cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
        假设cmp>0 node = node.right;此时node不为空 继续循环 h1>h2 h1<h2 Objects.equals(k1,k2)依旧不成立 程序又要扫描一遍
        可是我们之前已经扫描过一次了 发现没有相同的键值对 所以没必要再扫描 所以我们引入searched 用于记录程序是否已经扫描过了
         */
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2){//传入的key对应的hash值大于节点的hash值
                cmp = 1;
            }else if (h1 < h2){//小于
                cmp = -1;
            }else if (Objects.equals(k1,k2)){//hash值相等 equals相等 找到了相同的键值对
                cmp = 0;
            }else if(k1 != null && k2 != null && k1.getClass() == k2.getClass() && k1 instanceof Comparable
                        && (cmp = ((Comparable)k1).compareTo(k2)) != 0){//hash值相等 equals不相等 两者可比较
                // 但是不相等 根据cmp的正负确定后续操作 先扫描 如果存在相同的键值对 则覆盖
            }else if (searched){//哈希表中所有的红黑树都已经扫描过了 没有找到相同的 以后每次递归调用直接比较 不用再扫描
                //已经扫描过一次了 直接比较
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);
            }else {
                if ((node.left != null && (result = node(node.left,k1)) != null) ||
                        (node.right != null && (result = node(node.right,k1)) !=null)){//在节点的左边或者右边找到了 直接覆盖
                    node = result;
                    cmp = 0;
                }else {//不存在 添加
                    searched = true;//扫描结束 不存在
                    cmp = System.identityHashCode(k1) - System.identityHashCode(k2);//添加到节点左边还是右边
                }
            }
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else { // 相等
                V oldValue = node.value;
                node.key = key;
                node.value = value;
                node.hash = h1;//这一句可以不用写 因为执行else里的代码时 传入的键值对和节点的键值对肯定相等
                //既然相等 那么相同键计算出来的hash值一定一样 所以可以不用写这句代码
                return oldValue;
            }
        } while (node != null);

        // 看看插入到父节点的哪个位置
        Node<K, V> newNode = createNode(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;

        // 新添加节点之后的处理
        afterPut(newNode);
        return null;
    }

    @Override
    public V get(K key) {//根据键获得节点
        Node<K,V> node = node(key);
        return node != null ? node.value : null;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    @Override
    public boolean containsValue(V value) {
        if (size == 0) return false;
        Queue<Node<K,V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;//根节点为空 没有红黑树
            queue.offer(table[i]);
            while (!queue.isEmpty()){
                Node<K,V> node = queue.poll();
                if (Objects.equals(value,node.value)) return true;
                if (node.left != null){
                    queue.offer(node.left);
                }
                if (node.right != null){
                    queue.offer(node.right);
                }
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || visitor == null) return;
        Queue<Node<K,V>> queue = new LinkedList<>();
        for (int i = 0; i < table.length; i++) {
            if (table[i] == null) continue;//根节点为空 没有红黑树
            queue.offer(table[i]);
            while (!queue.isEmpty()){
                Node<K,V> node = queue.poll();
                if (visitor.visit(node.key,node.value)) return;//为true即停止遍历
                if (node.left != null){
                    queue.offer(node.left);
                }
                if (node.right != null){
                    queue.offer(node.right);
                }
            }
        }
    }

    protected static class Node<K, V> {
        int hash;
        K key;
        V value;
        boolean color = RED;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;
        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
            this.value = value;
            this.parent = parent;
        }

        public boolean hasTwoChildren() {
            return left != null && right != null;
        }

        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }
    }

    //根据key获取对应的hash值
    private int index(K key){
//        if (key == null) return 0;
//        int hash = key.hashCode();
//        // 先将高低16位进行混合运算再和数组的大小进行运算 计算索引 用户自定义的hashcode计算方法可能有问题
//        // 所以JDK自己再运算下
//        return (hash ^ (hash >>> 16)) & (table.length - 1);
        //以上代码可改为：
        return hash(key) & (table.length - 1);
    }
    //根据节点获得索引
    private int index(Node<K,V> node){
//        return (node.hash ^ (node.hash >>> 16)) & (table.length - 1);
        //以上代码可改为：
        return node.hash & (table.length - 1);
    }

    private int hash(K key) {
        if (key == null) return 0;
        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    private void fixAfterRemove(Node<K, V> node) {
        // 如果删除的节点是红色
        // 或者 用以取代删除节点的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<K, V> parent = node.parent;
        if (parent == null) return;

        // 删除的是黑色叶子节点【下溢】
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;
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
                    fixAfterRemove(parent);
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
                    fixAfterRemove(parent);
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

    private void afterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        // 添加的是根节点 或者 上溢到达了根节点
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色，直接返回
        if (isBlack(parent)) return;

        // 叔父节点
        Node<K, V> uncle = parent.sibling();
        // 祖父节点
        Node<K, V> grand = red(parent.parent);
        if (isRed(uncle)) { // 叔父节点是红色【B树节点上溢】
            black(parent);
            black(uncle);
            // 把祖父节点当做是新添加的节点
            afterPut(grand);
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

    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) return node;
        node.color = color;
        return node;
    }

    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    private void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;
        grand.right = child;
        parent.left = grand;
        afterRotate(grand, parent, child);
    }

    private void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;
        grand.left = child;
        parent.right = grand;
        afterRotate(grand, parent, child);
    }

    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
        // 让parent称为子树的根节点
        parent.parent = grand.parent;
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else { // grand是root节点
            table[index(grand)] = parent;
        }

        // 更新child的parent
        if (child != null) {
            child.parent = grand;
        }

        // 更新grand的parent
        grand.parent = parent;
    }

    /*
    发生hash冲突时 用于比较两个节点（键值对）是否相等 其中h1是k1的hash值 h2是k2的hash值
     */
//    private int compare(K k1,K k2,int h1,int h2){
//        //比较两个键值对的hash值来确定他们是否相等
//        int result = h1 - h2;
//        if (result != 0) return result;
//        //比较equals
//        if (Objects.equals(h1,h2)) return 0;
//        //hash值相等但是equals不等
//        //比较类名
//        if (k1 != null && k2 != null){
//            String k1Class = k1.getClass().getName();
//            String k2Class = k2.getClass().getName();
//            result = k1Class.compareTo(k2Class);
//            if (result != 0) return result;
//            //同一种类型 并且具有可比较性
//            if (k1 instanceof Comparable){//k1是Comparable的 那么可以直接比较k1 k2
//                return ((Comparable)k1).compareTo(k2);
//            }
//        }
//        // 同一种类型，哈希值相等，但是不equals，但是不具备可比较性
//		// k1不为null，k2为null
//		// k1为null，k2不为null
//        //identityHashCode根据内存地址生成hash值 存在bug
//		return System.identityHashCode(k1) - System.identityHashCode(k2);
//    }

    //根据key找到对应的节点
//    private Node<K,V> node(K key){
//        Node<K,V> node = table[index(key)];
//        int h1 = key == null ? 0 :key.hashCode();
//        while (node != null){
//            int cmp = compare(key,node.key,h1,node.hash);
//            if (cmp == 0) return node;
//            if (cmp > 0){
//                node = node.right;
//            }else if (cmp < 0){
//                node = node.left;
//            }
//        }
//        return null;
//    }
    private Node<K,V> node(K key){
        Node<K,V> root = table[index(key)];
        return root == null ? null :node(root,key);
    }
    private Node<K,V> node(Node<K,V> node,K k1){
        Node<K,V> result = null;
        int cmp = 0;
        int h1 = hash(k1);
        while (node != null){
            K k2 = node.key;
            int h2 = node.hash;
            // 先比较哈希值
            //若传入的k1的哈希值大于节点的哈希值 则去节点的右边找 若小于则去左边
            if(h1 > h2){//hash值可能是负的 所以不能用h1 - h2来比较大小
                node = node.right;
            }else if(h1 < h2){
                node = node.left;
            }else if (Objects.equals(k1,k2)){//hash值相等 找到了
                return node;
            } else if (k1 != null && k2 != null && k1.getClass() == k2.getClass() && k1 instanceof Comparable
                        && (cmp = ((Comparable) k1).compareTo(k2)) != 0){//hash值相等 equals不相等 两者可比较 但是不等
                //根据比较结果进一步确定接下来是往节点的左边找还是右边找
                node = cmp > 0 ? node.right : node.left;
            }else if (node.right != null && (result = node(node.right,k1)) != null) {//hash值不相等 equals不相等 在右边找到了
                return result;
            }else {
                node = node.left;//此时node若不为空 继续循环 若为空 则结束循环 以下代码优化为这一句
//            }else if (node.left != null && (result = node(node.left,k1)) != null){//在左边找到了
//                return result;
//            }else {
//                return null;
            }
        }
        return null;
    }

    protected V remove(Node<K,V> node){
        if (node == null) return null;
        Node<K,V> willNode = node;
        size--;

        V oldValue = node.value;

        if (node.hasTwoChildren()) { // 度为2的节点
            // 找到后继节点
            Node<K, V> s = successor(node);
            // 用后继节点的值覆盖度为2的节点的值
            node.key = s.key;
            node.value = s.value;
            node.hash = s.hash;
            // 删除后继节点
            node = s;

            replaceNode();
        }

        // 删除node节点（node的度必然是1或者0）
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        int index = index(node);
        if (replacement != null) { // node是度为1的节点
            // 更改parent
            replacement.parent = node.parent;
            // 更改parent的left、right的指向
            if (node.parent == null) { // node是度为1的节点并且是根节点
                table[index] = replacement;
            } else if (node == node.parent.left) {
                node.parent.left = replacement;
            } else { // node == node.parent.right
                node.parent.right = replacement;
            }

            // 删除节点之后的处理
            fixAfterRemove(replacement);
        } else if (node.parent == null) { // node是叶子节点并且是根节点
            table[index] = null;
        } else { // node是叶子节点，但不是根节点
            if (node == node.parent.left) {
                node.parent.left = null;
            } else { // node == node.parent.right
                node.parent.right = null;
            }

            // 删除节点之后的处理
            fixAfterRemove(node);
        }

        //交给子类去处理
        afterRemove(willNode,node);
        return oldValue;
    }

    private Node<K, V> successor(Node<K, V> node) {
        if (node == null) return null;

        // 前驱节点在左子树当中（right.left.left.left....）
        Node<K, V> p = node.right;
        if (p != null) {
            while (p.left != null) {
                p = p.left;
            }
            return p;
        }

        // 从父节点、祖父节点中寻找前驱节点
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }

        return node.parent;
    }

    public void print() {
        if (size == 0) return;
        for (int i = 0; i < table.length; i++) {
            final Node<K, V> root = table[i];
            System.out.println("【index = " + i + "】");
            BinaryTrees.println(new BinaryTreeInfo() {
                @Override
                public Object string(Object node) {
                    return node;
                }

                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object right(Object node) {
                    return ((Node<K, V>)node).right;
                }

                @Override
                public Object left(Object node) {
                    return ((Node<K, V>)node).left;
                }
            });
            System.out.println("---------------------------------------------------");
        }
    }

    /*
    当扩容为原来容量的2倍时 节点的索引有两种情况：
    1.保持不变
    2.index = index + 旧容量
    因为：首先 计算索引公式：hash(key) & (table.length - 1);
    假设哈希值为1110 原来table容量为2的平方 即4 table.length - 1 = 4 - 1 = 3 = 11
    原来的index = 1110 & 11 = 10 扩容为原来的2倍 即2的3次方 即8 table.length - 1 = 8 - 1 = 7 = 111
    则index = 1110 & 111 = 110 即 index = 10 + 100 = index + 原来的容量2的平方 即4 即100
    假设哈希值为1010 原来的index = 1010 & 11 = 10 扩容为原来的2倍 即111 则index = 1010 & 111 = 10 不变
    所以 扩容后 hash值的索引可能会改变 可能不变
     */
    private void resize(){
        //填充因子小于等于0.75 不扩容
        if (size / table.length <= DEFAULT_LOAD_FACTOR) return;

        Node<K,V>[] oldTable = table;
        table = new Node[oldTable.length << 1];//扩容 table指向新数组 新数组容量是原来的两倍

        //把原来table中的所有节点 包括所有红黑树上的节点 全部遍历挪到新的table中
        Queue<Node<K,V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTable.length; i++) {
            if (oldTable[i] == null) continue;//根节点为空 没有红黑树
            queue.offer(oldTable[i]);
            while (!queue.isEmpty()){
                Node<K,V> node = queue.poll();

                if (node.left != null){
                    queue.offer(node.left);
                }
                if (node.right != null){
                    queue.offer(node.right);
                }
                moveNode(node);//最后再移动节点
            }
        }
    }
    //移动节点
    private void moveNode(Node<K,V> newNode) {
        //重置 把之前的节点移动到新的哈希表时 之前的节点的父节点 左右子节点肯定都要清空
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;//移动到新的哈希表的节点可以看做是新的节点 默认为红色

        int index = index(newNode);
        Node<K, V> root = table[index];// 取出index位置的红黑树根节点
        if (root == null) {
            root = newNode;//桶为空 放入移动过来的节点
            table[index] = root;
            afterPut(root);
            return;
        }

        Node<K, V> parent = root;
        Node<K, V> node = root;
        int cmp = 0;
        K k1 = newNode.key;
        int h1 = hash(k1);
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;
            if (h1 > h2) {//传入的key对应的hash值大于节点的hash值
                cmp = 1;
            } else if (h1 < h2) {//小于
                cmp = -1;
            } else if (k1 != null && k2 != null && k1.getClass() == k2.getClass() && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {//hash值相等 equals不相等 两者可比较 但是不相等 根据cmp的正负确定后续操作
                // 先扫描 如果存在相同的键值对 则覆盖
            } else {//不存在 添加
                cmp = System.identityHashCode(k1) - System.identityHashCode(k2);//添加到节点左边还是右边
            }

            if (cmp > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        } while (node != null);

        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        newNode.parent = parent;
        // 新添加节点之后的处理
        afterPut(newNode);
    }

    protected Node<K,V> createNode(K key,V value,Node<K,V> parent){
        return new Node<>(key,value,parent);
    }

    protected void afterRemove(Node<K,V> willNode,Node<K,V> removedNode){
    }

    protected void replaceNode(){

    }
}
