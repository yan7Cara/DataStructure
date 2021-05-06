import java.util.HashMap;
/*
对trie的改进 重点还是看这个代码吧
 */
public class Trie2<V> {
    private int size;
    private Node<V> root;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        root = null;
    }

    //根据键获取值
    public V get(String key) {
        Node<V> node = node(key);
        return node != null && node.word ? node.value : null;
    }

    public boolean contains(String key) {
        Node<V> node = node(key);
        return node != null && node.word;
    }

    //将key value添加进trie
    /*
    思路：在trie中查找是否存在key：
    1.存在且t对应的节点的Word为true 表明存在cat单词 用传入的value覆盖原来的value
    2.存在且t对应的节点的Word为false 表明存在cat开头的单词 将t对应的节点的Word改为true
    3.存在c/ca开头的单词 但不存在cat 添加
    4.不存在c开头的单词 依次添加cat
     */
    public V add(String key, V value) {//"cat", 1
        keyCheck(key);

        //创建根节点
        if (root == null){
            root = new Node<>(null);
        }

        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);//c a t
            boolean emptyChildren = node.children == null;//node是不是叶子节点？node还有没有子节点
            Node<V> childNode = emptyChildren ? null : node.children.get(c);//节点node的子节点中是否存在该字母对应的节点
            if (childNode == null) {//不存在该字母对应的节点
                childNode = new Node<>(node);
                childNode.character = c;
                //用到子节点的时候再创建子节点
                node.children = emptyChildren ? new HashMap<>() : node.children;//子节点为空 创建子节点
                node.children.put(c, childNode);//为子节点创建该字母对应的（子）节点
                // 我们定义了子节点的键是字符 值是节点 所以put中应该传入字符 节点
            }
            node = childNode;//存在
        }

        if (node.word) { // 已经存在这个单词
            V oldValue = node.value;
            node.value = value;//传入的value覆盖原来的value
            return oldValue;//返回原来的value
        }

        // 存在以cat开头的单词 但是不存在cat单词 所以将Word改为true 表示存在cat单词即可
        node.word = true;
        node.value = value;
        size++;
        return null;
    }

    /*
    先遍历查找key 若不存在key 则直接返回null
    若存在key且key的最后一个字母所在节点有子节点 则将key的最后一个字母所在节点的Word变为false 清空value
    若存在key且key的最后一个字母所在节点没有子节点 先将最后一个字母所在节点删除 再判断该节点是否有兄弟节点
    若有 则结束 若没有 则判断该节点的父节点是否有兄弟节点 如此循环
     */
    public V remove(String key) {
        Node<V> node = node(key);//找到最后一个节点
        //如果不是单词结尾 不用做任何处理
        if (node == null || !node.word) return null;
        size--;
        V oldValue = node.value;

        if (node.children != null && !node.children.isEmpty()){//如果还有子节点
            node.word = false;
            node.value = null;
            return oldValue;
        }
        //如果没有子节点
        Node<V> parent = null;
        while ((parent = node.parent) != null){//只要节点的父节点不为空 则一直遍历 若节点的父节点为空 代表节点的父节点是root
            parent.children.remove(node.character);//移除字母
            if (parent.word || !parent.children.isEmpty()) break;//移除字母后 若父节点还有其他子节点 也就是删除的节点有兄弟节点 则退出
            node = parent;
        }

        return oldValue;
    }

    public boolean startsWith(String prefix) {
        return node(prefix) != null;
    }

    //根据键获取节点
    private Node<V> node(String key) {
        keyCheck(key);

        Node<V> node = root;
        int len = key.length();
        //假设trie中包含dog key="dog"
        for (int i = 0; i < len; i++) {//trie搜索字符串的效率主要跟字符串的长度有关
            if (node == null || node.children == null || node.children.isEmpty()) return null;
            char c = key.charAt(i);//遍历取出key中的每一个字符d o g
            //获取节点的子节点（node.getChildren()） 该子节点是HashMap
            //我们定义了子节点的键是字符 值是节点 HashMap的get方法是根据传入的可以获取value 所以此处获取到的是节点
            // 即传入键 获取子节点（HashMap）中该键对应的节点getChildren().get(c)
            //这一步操作其实就是获取子节点中某个字母对应的节点 若该节点为空 则表示该字母没有对应的节点 即不存在该字母
            node = node.children.get(c);

        }

        return node;
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty");
        }
    }

    /*
    trie的每一个节点包括：
    parent：父节点 方便删除
    boolean word：若为true 则代表该节点是单词的最后一个字母 该单词结尾了
    V value：用于存储该节点的值 在test中 cat的值是1 最后一个字母是t 那么t所在节点Word=true value=1
    HashMap<Character, Node<V>> children：用于存储子节点 键是字符 值是子节点
    character：每个节点对应的字符
     */
    private static class Node<V> {
        Node<V> parent;
        HashMap<Character, Node<V>> children;
        V value;
        boolean word; // 是否为单词的结尾（是否为一个完整的单词）
        Character character;

        public Node(Node<V> parent) {
            this.parent = parent;
        }
    }
}
