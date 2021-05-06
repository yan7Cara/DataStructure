import java.util.HashMap;
/*
trie：字典树 前缀树 单词查找树 主要用于查找某棵树（也可以是某数组 我们把该数组写成tree就形成trie）中是否包含某单词
        或是否以某单词/字母开头
trie搜索字符串的效率主要跟字符串的长度有关
思路：根节点为空 啥也不放 根节点到子节点的线上存放字母 节点存放：值 是否为最后一个单词 子节点
 */
public class Trie<V> {
    private int size;
    private Node<V> root = new Node<>();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
        root.getChildren().clear();//root本来就为空 所以清空root的所有子节点即可
    }

    //根据键获取值
    public V get(String key) {
        Node<V> node = node(key);
        return node == null ? null : node.value;
    }

    public boolean contains(String key) {
        return node(key) != null;
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

        Node<V> node = root;
        int len = key.length();
        for (int i = 0; i < len; i++) {
            char c = key.charAt(i);//c a t
            Node<V> childNode = node.getChildren().get(c);//节点的子节点中是否存在该字母对应的节点
            if (childNode == null) {//不存在
                childNode = new Node<>();
                node.getChildren().put(c, childNode);//为子节点创建该字母对应的（子）节点
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

    public V remove(String key) {
        return null;
    }

    public boolean startsWith(String prefix) {
        keyCheck(prefix);

        Node<V> node = root;
        int len = prefix.length();
        for (int i = 0; i < len; i++) {
            char c = prefix.charAt(i);
            node = node.getChildren().get(c);
            if (node == null) return false;
        }
        return true;
    }

    //根据键获取节点
    private Node<V> node(String key) {
        keyCheck(key);

        Node<V> node = root;
        int len = key.length();
        //假设trie中包含dog key="dog"
        for (int i = 0; i < len; i++) {//trie搜索字符串的效率主要跟字符串的长度有关
            char c = key.charAt(i);//遍历取出key中的每一个字符d o g
            //获取节点的子节点（node.getChildren()） 该子节点是HashMap
            //我们定义了子节点的键是字符 值是节点 HashMap的get方法是根据传入的可以获取value 所以此处获取到的是节点
            // 即传入键 获取子节点（HashMap）中该键对应的节点getChildren().get(c)
            //这一步操作其实就是获取子节点中某个字母对应的节点 若该节点为空 则表示该字母没有对应的节点 即不存在该字母
            node = node.getChildren().get(c);
            if (node == null) return null;
        }

        return node.word ? node : null;//node不为空 存在该字母 node的Word是否为true 是就是最后一个字母 返回node
    }

    private void keyCheck(String key) {
        if (key == null || key.length() == 0) {
            throw new IllegalArgumentException("key must not be empty");
        }
    }

    /*
    trie的每一个节点包括：
    boolean word：若为true 则代表该节点是单词的最后一个字母 该单词结尾了
    V value：用于存储该节点的值 在test中 cat的值是1 最后一个字母是t 那么t所在节点Word=true value=1
    HashMap<Character, Node<V>> children：用于存储子节点 键是字符 值是子节点
     */
    private static class Node<V> {
        HashMap<Character, Node<V>> children;
        V value;
        boolean word; // 是否为单词的结尾（是否为一个完整的单词）

        //获取节点的HashMap的Children
        public HashMap<Character, Node<V>> getChildren() {
            //若children为空 则创建HashMap 让children不为空 否则返回children
            //这样就可以保证上面的node.getChildren().get(c)不出现空指针异常
            return children == null ? (children = new HashMap<>()) : children;
        }
    }
}
