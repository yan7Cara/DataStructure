package 二叉搜索树;

import 二叉搜索树.file.Files;
import 二叉搜索树.printer.BinaryTreeInfo;
import 二叉搜索树.printer.BinaryTrees;
import java.util.Comparator;

public class test {
    static void test1(){//添加节点到树中
        Integer data[] = new Integer[]{7,4,9,2,5,8,11,3};
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.print(bst, BinaryTrees.PrintStyle.INORDER);
        BinaryTrees.print(bst);
    }

    static void test2(){//自定义person类 进行比较
        Integer data[] = new Integer[] {
                7, 4, 9, 2, 5, 8, 11, 3, 12, 1
        };

        BinarySearchTree<Person> bst1 = new BinarySearchTree<>();//使用person默认比较器
        for (int i = 0; i < data.length; i++) {
            bst1.add(new Person(data[i]));//把person对象添加到树中
        }

        BinaryTrees.println(bst1);

        BinarySearchTree<Person> bst2 = new BinarySearchTree<>(new Comparator<Person>() {//自定义person对象的比较器
            public int compare(Person o1, Person o2) {
                return o2.getAge() - o1.getAge();
            }
        });
        for (int i = 0; i < data.length; i++) {
            bst2.add(new Person(data[i]));//把person对象添加到树中
        }
        BinaryTrees.println(bst2);
    }
    static void test3() {//生成随机树
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < 40; i++) {
            bst.add((int)(Math.random() * 100));
        }

//        String str = BinaryTrees.printString(bst);
//        str += "\n";
//        Files.writeToFile("D:/1.txt", str, true);//是否在文件末尾追加
        // 若追加 则为true 新生成的树追加在文件后面 若不追加 则为false 新生成的树覆盖原来的树

         BinaryTrees.println(bst);
    }
    static void test4() {
        BinaryTrees.println(new BinaryTreeInfo() {

            @Override
            public Object string(Object node) {
                return node.toString() + "_";
            }

            @Override
            public Object root() {
                return "A";//根节点root=A
            }

            @Override
            public Object right(Object node) {
                if (node.equals("A")) return "C";//A的右节点是C
                if (node.equals("C")) return "E";//C的右节点是E
                return null;
            }

            @Override
            public Object left(Object node) {
                if (node.equals("A")) return "B";//A的左节点是B
                if (node.equals("C")) return "D";//C的左节点第D
                return null;
            }
        });
    }
    static void test5() {
        BinarySearchTree<Person> bst = new BinarySearchTree<>();
        bst.add(new Person(10, "jack"));
        bst.add(new Person(12, "rose"));
        bst.add(new Person(6, "jim"));

        bst.add(new Person(10, "michael"));

        BinaryTrees.println(bst);
    }
    static void test6() {
        Integer data[] = new Integer[] {
                7, 4, 9, 2, 5
        };

//        BST<Integer> bst = new BST<>();
//        for (int i = 0; i < data.length; i++) {
//            bst.add(data[i]);
//        }

		BinarySearchTree<Integer> bst = new BinarySearchTree<>();
		for (int i = 0; i < 10; i++) {
			bst.add((int)(Math.random() * 100));
		}
        BinaryTrees.println(bst);
//        bst.preorderTraversal();
//        bst.inorderTraversal();
//        bst.postorderTraversal();
//        bst.levelOrderTraversal();

        System.out.println(bst.isComplete());

		/*
		 *       7
		 *    4    9
		    2   5
		 */

//		bst.levelOrder(new BST.Visitor<Integer>() {
//			public void visit(Integer element) {
//				System.out.print("_" + element + "_ ");
//			}
//		});

//		bst.inorder(new Visitor<Integer>() {
//			public void visit(Integer element) {
//				System.out.print("_" + (element + 3) + "_ ");
//			}
//		});

//         System.out.println(bst.height1());
//         System.out.println(bst.height2());
    }

    static void test7() {
        Integer data[] = new Integer[] {
                7, 4, 9, 2, 5, 8, 11, 3, 12, 1
        };

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }

        BinaryTrees.println(bst);

//        bst.remove(7);
		bst.remove(1);
		bst.remove(3);
		bst.remove(12);

        BinaryTrees.println(bst);
    }

    static void test9() {
        Integer data[] = new Integer[] {
                7, 4, 9, 2, 1
        };

        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        for (int i = 0; i < data.length; i++) {
            bst.add(data[i]);
        }
        BinaryTrees.println(bst);

        bst.preorder(new BinarySearchTree.Visitor<Integer>() {
            public boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 2 ? true : false;
            }
        });
        System.out.println();

        bst.inorder(new BinarySearchTree.Visitor<Integer>() {
            public boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 4 ? true : false;
            }
        });
        System.out.println();

        bst.postorder(new BinarySearchTree.Visitor<Integer>() {
            public boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 4 ? true : false;
            }
        });
        System.out.println();

        bst.levelOrder(new BinarySearchTree.Visitor<Integer>() {
            public boolean visit(Integer element) {
                System.out.print(element + " ");
                return element == 9 ? true : false;
            }
        });
        System.out.println();
    }
    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
        test7();
//        test9();
    }
}
