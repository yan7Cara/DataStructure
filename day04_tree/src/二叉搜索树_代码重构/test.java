package 二叉搜索树_代码重构;

import 二叉搜索树.Person;
import 二叉搜索树_代码重构.printer.BinaryTreeInfo;
import 二叉搜索树_代码重构.printer.BinaryTrees;
import 二叉搜索树_代码重构.tree.BST;

import java.util.Comparator;

public class test {
    static void test1(){//添加节点到树中
        Integer data[] = new Integer[]{7,4,9,2,5,8,11,3};
        BST<Integer> bst = new BST<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.print(bst, BinaryTrees.PrintStyle.INORDER);
        BinaryTrees.print(bst);
    }

    public static void main(String[] args) {
        test1();
    }
}
