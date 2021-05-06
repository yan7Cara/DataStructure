import file.FileInfo;
import file.Files;
import map.Map;
import map.TreeMap;
import model.Key;
import model.Person;
import model.SubKey1;
import model.SubKey2;

public class test {
    static void test1(){//计算字符串的hash值
        String string = "jack";
//        System.out.println(string.hashCode());//以下代码等同于这一句
        int len = string.length();
        int hashCode = 0;
        for (int i = 0; i < len; i++) {
            char c = string.charAt(i);
//            hashCode = hashCode * 31 + c;//这句代码和下一句代码一样
            hashCode = (hashCode << 5) - hashCode + c;
        }
        System.out.println(hashCode);
    }

    static void test2(){
        Integer a =110;
        Float b = 10.6f;
        Long c = 156l;
        Double d = 10.9;
        String e = "rose";

        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(c.hashCode());
        System.out.println(d.hashCode());
        System.out.println(e.hashCode());
    }

    static void test3(){
        Person p1 = new Person(10,1.67f,"jack");
        Person p2 = new Person(10,1.67f,"jack");
        /*
        如果没有重写Person的hashCode方法 那么p1 p2就是两个不同的hash值 hash值不同对应在内存中的位置可能一样
        如果我们想要实现：当person的年龄 身高 体重值一样时 认定为同一个对象 p1 p2有同一个hash值
        在下面：
        map.put(p1,"abc");
        map.put(p2,"def");
        时 实际上是给同一个hash值put字符串 最后输出为1
        需要重写Person的hashCode方法
         */
        System.out.println(p1.hashCode());
        System.out.println(p2.hashCode());

        Map<Object,Object> map = new HashMap<>();
        map.put(p1,"abc");
        map.put(p2,"def");
        System.out.println(map.size());//import java.util.HashMap;
    }

    static void test4(){
        Person p1 = new Person(10,1.67f,"jack");
        Person p2 = new Person(10,1.67f,"jack");
        Map<Object,Integer> map = new HashMap<>();
        map.put(p1,1);
        map.put(p2,2);
        map.put("jack",3);
        map.put("rose",4);
        map.put("jack",5);
        map.put(null,6);

        System.out.println(map.size());
//        System.out.println(map.remove("jack"));
        System.out.println(map.get("jack"));
        System.out.println(map.get("rose"));
        System.out.println(map.get(p1));
        System.out.println(map.get(p2));
        System.out.println(map.get(null));
        System.out.println(map.size());

//        System.out.println(map.containsKey(p1));
//        System.out.println(map.containsKey(null));
//        System.out.println(map.containsValue(6));
//        System.out.println(map.containsValue(1));

//        map.traversal(new Map.Visitor<Object,Integer>(){
//            public boolean visit(Object key, Integer value) {
//                System.out.println(key + "_" + value);
//                return false;
//            }
//        });
    }

    static void test5(){
        Map<Object,Integer> map = new HashMap<>();
        for (int i = 0; i <= 19; i++) {
            map.put(new Key(i),i);
        }
        System.out.println(map.get(new Key(1)));
//        map.print();
    }

//    static void test6(){
//        HashMap<Object,Integer> map = new HashMap<>();
//        for (int i = 0; i < 19; i++) {
//            map.put(new Key(i),i);
//        }
//        map.put(new Key(4),100);
//        Asserts.test(map.size() == 29);
//        Asserts.test(map.get(new Key(4)) == 100);
//        Asserts.test(map.get(new Key(18)) == 18);
//        map.print();
//    }

    static void test1Map(Map<String, Integer> map, String[] words) {
        Times.test(map.getClass().getName(), new Times.Task() {
            @Override
            public void execute() {
                for (String word : words) {
                    Integer count = map.get(word);
                    count = count == null ? 0 : count;
                    map.put(word, count + 1);
                }
                System.out.println(map.size()); // 17188

                int count = 0;
                for (String word : words) {
                    Integer i = map.get(word);
                    count += i == null ? 0 : i;
                    map.remove(word);
                }
                Asserts.test(count == words.length);
                Asserts.test(map.size() == 0);
            }
        });
    }

    static void test7() {
        String filepath = "D:\\axis.log";
        FileInfo fileInfo = Files.read(filepath, null);
        String[] words = fileInfo.words();

        System.out.println("总行数：" + fileInfo.getLines());
        System.out.println("单词总数：" + words.length);
        System.out.println("-------------------------------------");

        test1Map(new TreeMap<>(), words);
        test1Map(new HashMap<>(), words);
    }

    static void test8(HashMap<Object, Integer> map) {
        for (int i = 1; i <= 20; i++) {
            map.put(new Key(i), i);
        }
        for (int i = 5; i <= 7; i++) {
            map.put(new Key(i), i + 5);
        }
        Asserts.test(map.size() == 20);
        Asserts.test(map.get(new Key(4)) == 4);
        Asserts.test(map.get(new Key(5)) == 10);
        Asserts.test(map.get(new Key(6)) == 11);
        Asserts.test(map.get(new Key(7)) == 12);
        Asserts.test(map.get(new Key(8)) == 8);
    }

    static void test9(HashMap<Object, Integer> map) {
        map.put(null, 1); // 1
        map.put(new Object(), 2); // 2
        map.put("jack", 3); // 3
        map.put(10, 4); // 4
        map.put(new Object(), 5); // 5
        map.put("jack", 6);
        map.put(10, 7);
        map.put(null, 8);
        map.put(10, null);
        Asserts.test(map.size() == 5);
        Asserts.test(map.get(null) == 8);
        Asserts.test(map.get("jack") == 6);
        Asserts.test(map.get(10) == null);
        // equals默认比内存地址
        Asserts.test(map.get(new Object()) == null);//new Object().equals(new Object()) = false
        Asserts.test(map.containsKey(10));
        Asserts.test(map.containsKey(null));
        Asserts.test(map.containsValue(null));
        Asserts.test(map.containsValue(1) == false);
    }

    static void test10(HashMap<Object, Integer> map) {
        map.put("jack", 1);
        map.put("rose", 2);
        map.put("jim", 3);
        map.put("jake", 4);
        for (int i = 1; i <= 10; i++) {
            map.put("test" + i, i);
            map.put(new Key(i), i);
        }
        for (int i = 5; i <= 7; i++) {
            Asserts.test(map.remove(new Key(i)) == i);
        }
        for (int i = 1; i <= 3; i++) {
            map.put(new Key(i), i + 5);
        }
        Asserts.test(map.size() == 21);
        Asserts.test(map.get(new Key(1)) == 6);
        Asserts.test(map.get(new Key(2)) == 7);
        Asserts.test(map.get(new Key(3)) == 8);
        Asserts.test(map.get(new Key(4)) == 4);
        Asserts.test(map.get(new Key(5)) == null);
        Asserts.test(map.get(new Key(6)) == null);
        Asserts.test(map.get(new Key(7)) == null);
        Asserts.test(map.get(new Key(8)) == 8);

        map.traversal(new Map.Visitor<Object,Integer>(){

            @Override
            public boolean visit(Object key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
    }

    static void test11(HashMap<Object, Integer> map) {
        for (int i = 1; i <= 20; i++) {
            map.put(new SubKey1(i), i);
        }
        map.put(new SubKey2(1), 5);
        Asserts.test(map.get(new SubKey1(1)) == 5);
        Asserts.test(map.get(new SubKey2(1)) == 5);
        Asserts.test(map.size() == 20);
    }

    public static void main(String[] args) {
//        test3();
//        test4();
//        test5();
//        test6();

//        test1Map(new HashMap<>());
//        test7();
//        test8(new HashMap<>());
//        test9(new HashMap<>());
//        test10(new HashMap<>());
//        test11(new HashMap<>());

        test8(new LinkedHashMap<>());
        test9(new LinkedHashMap<>());
        test10(new LinkedHashMap<>());
        test10(new LinkedHashMap<>());
        test11(new LinkedHashMap<>());
    }
}
