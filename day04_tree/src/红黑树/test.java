package 红黑树;

import 红黑树.printer.BinaryTrees;
import 红黑树.tree.AVLTree;
import 红黑树.tree.RedBlackTree;

public class test {
    static void test1() {
//        Integer data[] = new Integer[] {
//                67, 52, 92, 96, 53, 95, 13, 63, 34, 82, 76, 54, 9, 68, 39
//        };

        Integer data2[] = new Integer[] {
                85,19,69,3,7,99,95
        };

        AVLTree<Integer> avl = new AVLTree<>();
//        for (int i = 0; i < data.length; i++) {
//            avl.add(data[i]);
//			System.out.println("【" + data1[i] + "】");
//			BinaryTrees.println(avl);
//			System.out.println("---------------------------------------");
//        }

		for (int i = 0; i < data2.length; i++) {
            avl.add(data2[i]);
//			avl.remove(data2[i]);
            avl.remove(99);
            avl.remove(85);
            avl.remove(95);
			System.out.println("【" + data2[i] + "】");
			BinaryTrees.println(avl);
			System.out.println("---------------------------------------");
		}
//        avl.remove(99);
//        avl.remove(85);
//        avl.remove(95);

        BinaryTrees.println(avl);
    }

//    static void test2() {
//        List<Integer> data = new ArrayList<>();
//        for (int i = 0; i < 100_0000; i++) {
//            data.add((int)(Math.random() * 100_0000));
//        }
//
//        BST<Integer> bst = new BST<>();
//        for (int i = 0; i < data.size(); i++) {
//            bst.add(data.get(i));
//        }
//        for (int i = 0; i < data.size(); i++) {
//            bst.contains(data.get(i));
//        }
//        for (int i = 0; i < data.size(); i++) {
//            bst.remove(data.get(i));
//        }
//
//        AVLTree<Integer> avl = new AVLTree<>();
//        for (int i = 0; i < data.size(); i++) {
//            avl.add(data.get(i));
//        }
//        for (int i = 0; i < data.size(); i++) {
//            avl.contains(data.get(i));
//        }
//        for (int i = 0; i < data.size(); i++) {
//            avl.remove(data.get(i));
//        }
//    }

    static void test3() {
        Integer data[] = new Integer[] {
                55, 87, 56, 74, 96, 22, 62, 20, 70, 68, 90, 50
        };

        RedBlackTree<Integer> rb = new RedBlackTree<>();
        for (int i = 0; i < data.length; i++) {
            rb.add(data[i]);
            System.out.println("【" + data[i] + "】");
            BinaryTrees.println(rb);
            System.out.println("---------------------------------------");
        }
    }

    public static void main(String[] args) {
//        test1();
        test3();
    }
}
