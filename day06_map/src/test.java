import file.FileInfo;

import java.nio.file.Files;
import java.util.Set;
import java.util.TreeSet;

public class test {
    static void test1() {
        Map<String, Integer> map = new TreeMap<>();
        map.put("c", 2);
        map.put("a", 5);
        map.put("b", 6);
        map.put("a", 8);

        map.traversal(new Map.Visitor<String, Integer>() {
            public boolean visit(String key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
    }

    static void test2() {
        FileInfo fileInfo = file.Files.read("D:\\BaiduNetdiskDownload\\java\\资料1\\day01-17【瑞客论坛 www.ruike1.com】\\代码\\09-集合",
                new String[]{"java"});

        System.out.println("文件数量：" + fileInfo.getFiles());
        System.out.println("代码行数：" + fileInfo.getLines());
        String[] words = fileInfo.words();
        System.out.println("单词数量：" + words.length);

        Map<String, Integer> map = new TreeMap<>();
        for (int i = 0; i < words.length; i++) {
            Integer count = map.get(words[i]);
            count = (count == null) ? 1 : (count + 1);
            map.put(words[i], count);
        }

        map.traversal(new Map.Visitor<String, Integer>() {
            public boolean visit(String key, Integer value) {
                System.out.println(key + "_" + value);
                return false;
            }
        });
    }

//    static void test3() {
//        Set<String> set = new TreeSet<>();
//        set.add("c");
//        set.add("b");
//        set.add("c");
//        set.add("c");
//        set.add("a");
//
//        set.traversal(new Set.Visitor<String>() {
//            public boolean visit(String element) {
//                System.out.println(element);
//                return false;
//            }
//        });
//    }

    public static void main(String[] args) {
        test1();
    }
}
