import list.LinkedList;
import list.List;

public class ListSet<E> implements Set<E>{
    private List<E> list = new LinkedList<>();
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean contains(E element) {
        return list.contains(element);
    }

    @Override
    public void add(E element) {
//        if (list.contains(element)) return;//若包含这个元素 则直接返回
//        list.add(element);//若不包含 则将该元素添加进去

        int index = list.indexOf(element);//查找该元素的索引
        if (index != List.ELEMENT_NOT_FOUND){//能查到该元素的索引
            list.set(index,element);//用新的元素替代旧的元素
        }else {//没有找到该元素的索引
            list.add(element);//
        }
    }

    @Override
    public void remove(E element) {
        int index = list.indexOf(element);//查找该元素的索引
        if (index != List.ELEMENT_NOT_FOUND) {//能查到该元素的索引
            list.remove(index);//删除该元素
        }
    }

    @Override
    public void traversal(Visitor visitor) {//遍历
        if(visitor == null) return;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (visitor.visit(list.get(i))) return;
        }
    }
}
