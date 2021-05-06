/*
在stack中定义动态数组list stack中的方法的实现可以在直接调用list方法
 */
public class Stack<E>{
    private List<E> list = new ArrayList<>();

    public void clear() {
        list.clear();
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void push(E element) {
        list.add(element);
    }


    public E pop() {
        return list.remove(list.size() - 1);
    }


    public E top() {
        return list.get(list.size() - 1);
    }
}
