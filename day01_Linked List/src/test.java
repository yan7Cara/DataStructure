public class test {
    static void testList(List<Integer> list) {
        list.add(11);
        list.add(22);
        list.add(33);
        list.add(44);

        list.add(0, 55); // [55, 11, 22, 33, 44]
        list.add(2, 66); // [55, 11, 66, 22, 33, 44]
        list.add(list.size(), 77); // [55, 11, 66, 22, 33, 44, 77]

        list.remove(0); // [11, 66, 22, 33, 44, 77]
        list.remove(2); // [11, 66, 33, 44, 77]
        list.remove(list.size() - 1); // [11, 66, 33, 44]

        Asserts.test(list.indexOf(44) == 3);
        Asserts.test(list.indexOf(22) == List.ELEMENT_NOT_FOUND);
//        Asserts.test(list.contains(33));
        Asserts.test(list.get(0) == 11);
        Asserts.test(list.get(1) == 66);
        Asserts.test(list.get(list.size() - 1) == 44);

        System.out.println(list);
    }

    static void josephus(){
        JosephusCircleLinkedList<Integer> list = new JosephusCircleLinkedList<>();
        for (int i = 1; i <= 8; i++) {
            list.add(i);
        }
        list.reset();
        while (!list.isEmpty()){
            list.next();
            list.next();
            System.out.println(list.remove());
        }
    }

    public static void main(String[] args) {
        //list是局部变量 是栈指针 new SingleLinkedList1<>()是堆空间的对象 list指向new SingleLinkedList1<>()所在地址
        //new SingleLinkedList1<>()被称为gc root对象 new SingleLinkedList1<>()中包含next value
        //        List<Integer> list = new SingleLinkedList1<>();
//        list.add(20);
//        list.add(0,10);
//        list.add(30);
//        list.add(list.size(),40);
//        [10,20,30,40]
//        list.remove(1);
        //[10,30,40]
//        System.out.println(list);

//        List<Integer> list = new SingleLinkedList2<>();
//        list.add(20);
//        list.add(0,10);
//        list.add(30);
//        list.add(list.size(),40);
//        [10,20,30,40]
//        list.remove(1);
        //[10,30,40]
//        System.out.println(list);

//       List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            list.add(i);
//        }
//        for (int i = 0; i < 50; i++) {
//            list.remove(0);
//        }
//        System.out.println(list);

//        List<Integer> list = new LinkedList<>();
//        list.add(20);
//        list.add(0,10);
//        list.add(30);
//        list.add(list.size(),40);
////        [10,20,30,40]
//        list.remove(1);
////        [10,30,40]
//        System.out.println(list);

//        List<Integer> list = new SingleCircleLinkedList<>();
//        list.add(20);
//        list.add(0,10);
//        list.add(30);
//        list.add(list.size(),40);
////        [10,20,30,40]
//        list.remove(1);
////        [10,30,40]
//        System.out.println(list);

//        testList(new SingleCircleLinkedList<>());

//        testList(new CircleLinkedList<>());
        josephus();

    }
}
