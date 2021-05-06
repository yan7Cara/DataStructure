public class Person {
    private int age;   // 10  20
    private float height; // 1.55 1.67
    private String name; // "jack" "rose"

    public Person(int age, float height, String name) {
        this.age = age;
        this.height = height;
        this.name = name;
    }

    /*
    用于计算hash值
     */
    @Override
    public int hashCode() {
        //上一个数据的hashcode*31+下一个数据的hashcode 拼接在一起形成最终的hashcode
        int hashCode = Integer.hashCode(age);
        hashCode = hashCode * 31 + Float.hashCode(height);
        hashCode = hashCode * 31 + (name != null ? name.hashCode() : 0);
        return hashCode;
    }

    @Override
    /*
     * 发生哈希冲突时 用来比较链表中2个对象是否相等
     * 默认的equals比较内存地址是否相等 而非比较内容
     * 一定要重写Person的equals方法 在出现hash冲突时 需要比较同一个hash值下的键 若发现相同的键 则用新值覆盖旧值
     * 如果两个数/对象equals后返回true
     *
     * equals用于判断两个key是否为同一个key
     * 需要遵守以下两个约定：
     * 1.对于任何非null的x x.equals(x)必须返回true
     * 2.对于任何非null的x y 如果x.equals(y)返回true 则y.equals(x)必须返回true
     * 3.对于任何非null的x y z 如果x.equals(y)返回true y.equals(z)返回true 则x.equals(z)必须返回true
     * 4.对于任何非null的x y 只要equals的比较操作在对象中所用的信息没有被修改 多次调用x.equals(y)就会一致的返回true/false
     * 5.对于任何非null的x x.equals(null)必须返回false
     */
    public boolean equals(Object obj) {
        // 比较内存地址
        if (this == obj) return true;
        /*
        下面两行代码的区别：
        obj如果是Person类型或Person的子类instanceof Person都返回true
        第一行代码则只有obj是Person类型才返回true
         */
        if (obj == null || obj.getClass() != getClass()) return false;//传入的是空 或者二者类型不同
        // if (obj == null || !(obj instanceof Person)) return false;

        // 比较成员变量
        Person person = (Person) obj;
        return person.age == age
                && person.height == height
                && (person.name == null ? name == null : person.name.equals(name));
    }
}
