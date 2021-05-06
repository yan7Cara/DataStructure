import printer.BinaryTrees;

import java.util.Comparator;

public class test {
    static void test1() {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(68);
        heap.add(72);
        heap.add(43);
        heap.add(50);
        heap.add(38);
        heap.add(10);
        heap.add(90);
        heap.add(65);
        BinaryTrees.println(heap);
        heap.remove();
        BinaryTrees.println(heap);

		System.out.println(heap.replace(70));
        BinaryTrees.println(heap);
    }

    static void test2() {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        BinaryHeap<Integer> heap = new BinaryHeap<>(data);
        BinaryTrees.println(heap);

        data[0] = 10;
        data[1] = 20;
        BinaryTrees.println(heap);
    }

    /*
    程序员在使用compare时 默认情况下 若返回值大于0 则认为o1>o2 此时compare内部是o1 - o2
    假设我们希望返回值大于0时o1<o2 那么我们应该重写compare：o2 - o1
    在计算大堆顶时 运用比较 找到o1 o2中大的那个数放在堆顶 此时使用默认的compare
    计算小堆顶时 我们要将o1 o2中小的那个数放在堆顶 那么此时就可以重写compare为o2 - o1
    若o2 - o1 > 0 则o2大 但是o2 - o1 > 0返回的是正数 外面程序却认为o1大
    所以程序把实际小的那个数当做大的数进行运算
    */
    static void test3() {
        Integer[] data = {88, 44, 53, 41, 16, 6, 70, 18, 85, 98, 81, 23, 36, 43, 37};
        BinaryHeap<Integer> heap = new BinaryHeap<>(data, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;//大堆顶
//                return o2 - o1;//小堆顶
            }
        });
        BinaryTrees.println(heap);
    }

    /*
    题目：从n个整数中 找出最大的前k个数（k远小于n）
    思路：用小堆顶
    先将数组中前k个数放入堆中形成小堆顶 然后从k+1开始遍历数组 数组中的每一个数都和堆顶比较 若堆顶小
    则用新数替代堆顶 即新数覆盖堆顶 删除小堆顶中最小的数（堆顶） 然后再进行调整形成小堆顶
    再从数组中取出一个数和堆顶比较 若堆顶大 则取出数组中的下一个数和堆顶进行比较 如此遍历
    这样 每次都能删除小堆顶中最小的数 添加大的数 遍历结束后 小堆顶中剩下的数就是数组中最大的前k个数
     */
    static void test4() {
        // 新建一个小顶堆
        BinaryHeap<Integer> heap = new BinaryHeap<>(new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

        // 找出最大的前k个数
        int k = 3;
        Integer[] data = {51, 30, 39, 92, 74, 25, 16, 93,
                91, 19, 54, 47, 73, 62, 76, 63, 35, 18,
                90, 6, 65, 49, 3, 26, 61, 21, 48};
        for (int i = 0; i < data.length; i++) {
            if (heap.size() < k) { // 前k个数添加到小顶堆
                heap.add(data[i]); // logk
            } else if (data[i] > heap.get()) { // 如果是第k + 1个数，并且大于堆顶元素
                heap.replace(data[i]); // logk
            }
        }
        // O(nlogk)
        BinaryTrees.println(heap);
    }
    public static void main(String[] args) {
//        test1();
//        test2();
        test3();
//        test4();
    }
}
