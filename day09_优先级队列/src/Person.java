public class Person implements Comparable<Person> {
	private String name;
	private int boneBreak;
	public Person(String name, int boneBreak) {
		this.name = name;
		this.boneBreak = boneBreak;
	}

	/*
	compare(this,person)
	大堆顶 把优先级最高的数（最大的数）放在堆顶：this.boneBreak - person.boneBreak;

	要想实现比较 可以传入comparator 也可以重写自定义类的compareTo方法 前者在heap中实现了 此处展示后者方法
	 */
	@Override
	public int compareTo(Person person) {
		return this.boneBreak - person.boneBreak;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", boneBreak=" + boneBreak + "]";
	}
}
