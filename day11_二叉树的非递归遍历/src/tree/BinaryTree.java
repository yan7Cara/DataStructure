package tree;
import printer.BinaryTreeInfo;
import sun.reflect.generics.visitor.Visitor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class BinaryTree<E> implements BinaryTreeInfo {
	protected int size;
	protected Node<E> root;
	
	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public void clear() {
		root = null;
		size = 0;
	}
	/*
	前序遍历：利用栈实现 根左右
	前序遍历的顺序：先遍历根节点 再遍历左子节点 遍历到根节点的最后一个左子节点后
	遍历根节点的最后一个左子节点所在的树的右子节点 若该右子节点有左子节点 则继续遍历他的左子节点
	直到最后一个左子节点所在的树遍历完毕 然后遍历根节点的倒数第二个左子节点所在的树的右子节点
	方法同上 由此可见 左子节点的遍历是从上往下 右子节点的遍历是从下往上
	也就是根节点的右子节点最后遍历 所以我们可以先拿到根节点 将根节点的右子节点入栈
	然后拿到根节点的左子节点 将该左子节点的右子节点入栈 如此循环拿到左子节点 将右子节点入栈
	当遍历到最后一个左子节点时 将栈顶元素弹出 弹出的元素看做root 传给node 进行类似于上面的循环
	因为你并不知道最后一个左子节点所在的树的右子节点是否有左子节点 若没有 则弹出栈中第二个元素
	若有 则遍历该元素的左子节点 把该元素看成root
	 */
	public void preorder(Visitor<E> visitor) {
		if (visitor == null || root == null) return;
		Node<E> node = root;
		Stack<Node<E>> stack = new Stack<>();
		while (true){
			if (node != null){
				//访问node节点
				if (visitor.visit(node.element)) return;//若为true表明停止遍历
				//将右子节点入栈
				if (node.right != null){
					stack.push(node.right);//右子节点入栈
				}
				//一直向左走 把右子节点入栈
				node = node.left;
			}else if (stack.isEmpty()){//没有右子节点或栈中所有元素都弹出来了 即前序遍历结束
				return;
			}else {
				node = stack.pop();
			}
		}
	}
	/*
	前序遍历方法2：访问根节点 然后将根节点的右子节点 左字节点入队
	然后弹出栈顶元素（根节点的左子节点）访问 然后将元素的右子节点 左字节点入队
	然后弹出栈顶元素访问 如此循环
	 */
	public void preorder2(Visitor<E> visitor) {
		if (visitor == null || root == null) return;
		Stack<Node<E>> stack = new Stack<>();
		stack.push(root);
		while (!stack.isEmpty()){
			Node<E> node = stack.pop();
			if (visitor.visit(node.element)) return;//访问节点
			if (node.right != null){
				stack.push(node.right);
			}
			if (node.left != null){
				stack.push(node.left);
			}
		}
	}
	/*
	中序遍历：利用栈实现 左根右
	中序遍历的顺序：先遍历根节点最左边的节点 然后遍历最左边的节点的根 最左边的节点的右子节点
	然后遍历根节点的左子节点中倒数第二棵数的根节点 倒数第二棵数的右子节点 由此可见 左右子节点
	根节点的遍历都是由下而上 那么我们可以从上往下遍历左子节点时 把根节点root 子树的根节点
	最后一个左子节点入栈 其实子树的根节点其实全部都是左子节点 当遍历到最后一个左子节点时
	弹出栈顶元素 也就是最后一个左子节点 若该元素有右子节点 那么将右子节点看做root传入node
	重复以上操作 由此遍历右子节点的子树 若没有 则弹出栈顶元素 也就是倒数第二个左子节点
	然后遍历该元素的右子节点 如此循环
	 */
	public void inorder(Visitor<E> visitor) {
		if (visitor == null || root == null) return;
		Node<E> node = root;
		Stack<Node<E>> stack = new Stack<>();
		while(true){
			if (node != null){
				stack.push(node);
				node = node.left;//一直向左走 当遍历到最后一个左子节点时 该左子节点的left为空
//				则node为空
			}else if (stack.isEmpty()){
				return;
			}else {//node为空
				node = stack.pop();//弹出栈顶元素
				//访问node节点
				if (visitor.visit(node.element)) return;//若为true表明停止遍历
				node = node.right;//遍历栈顶元素的右子节点
			}
		}
	}

	/*
	后序遍历：左右根
	后序遍历的顺序：将root入栈 然后root的右子节点入栈 左子节点入栈 然后查看栈顶 若栈顶不是叶子节点
	则将栈顶看做root 将栈顶的右子节点入栈 左子节点入栈 如此循环 若栈顶是叶子节点
	则表明已经遍历到最左边的左子节点了 弹出栈顶元素访问 即弹出最左边的左子节点访问
	然后查看栈顶元素 此栈顶元素为最左边的左子节点所在的子树的右子节点 若不为叶子节点
	则表明右子节点有左右子节点 将此右左子节点入栈 如此循环 若为叶子节点 则弹出访问
	要考虑一个bug：假设最左边的左子节点所在的子树有左右子节点 那么当遍历到最左边的左子节点时
	弹出左子节点访问后 弹出他的兄弟节点 即最左边的左子节点所在的子树的右子节点访问
	然后会弹出最左边的左子节点所在的子树的根节点 由于该根节点不是叶子节点
	所以会将该根节点的左右子节点入栈 也就是我们刚弹出访问的两个元素又入栈了 如此 程序陷入死循环
	所以我们除了判别栈顶元素是否是叶子节点外还要判别栈顶是否是上一次弹出访问的节点的父节点
	 */
	public void postorder(Visitor<E> visitor) {
		if (visitor == null || root == null) return;
		Node<E> prev = null;//记录上一次弹出访问的节点
		Stack<Node<E>> stack = new Stack<>();
		stack.push(root);
		while(!stack.isEmpty()) {
			Node<E> top = stack.peek();
			if (top.isLeaf() || (prev != null && prev.parent == top)){
				prev = stack.pop();
				if (visitor.visit(prev.element)) return;//访问节点
			}else {
				if (top.right != null){
					stack.push(top.right);
				}
				if (top.left != null){
					stack.push(top.left);
				}
			}
		}
	}
	
	public void levelOrder(Visitor<E> visitor) {
		if (root == null || visitor == null) return;
		
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			if (visitor.visit(node.element)) return;
			
			if (node.left != null) {
				queue.offer(node.left);
			}
			
			if (node.right != null) {
				queue.offer(node.right);
			}
		}
	}
	
	public boolean isComplete() {
		if (root == null) return false;
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		
		boolean leaf = false;
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			if (leaf && !node.isLeaf()) return false;

			if (node.left != null) {
				queue.offer(node.left);
			} else if (node.right != null) {
				return false;
			}
			
			if (node.right != null) {
				queue.offer(node.right);
			} else { // 后面遍历的节点都必须是叶子节点
				leaf = true;
			}
		}
		
		return true;
	}
	
	public int height() {
		if (root == null) return 0;
		
		// 树的高度
		int height = 0;
		// 存储着每一层的元素数量
		int levelSize = 1;
		Queue<Node<E>> queue = new LinkedList<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			Node<E> node = queue.poll();
			levelSize--;
			
			if (node.left != null) {
				queue.offer(node.left);
			}
			
			if (node.right != null) {
				queue.offer(node.right);
			}

			if (levelSize == 0) { // 意味着即将要访问下一层
				levelSize = queue.size();
				height++;
			}
		}
		
		return height;
	}
	
	public int height1() {
		return height(root);
	}
	
	private int height(Node<E> node) {
		if (node == null) return 0;
		return 1 + Math.max(height(node.left), height(node.right));
	}

	protected Node<E> predecessor(Node<E> node) {
		if (node == null) return null;
		
		// 前驱节点在左子树当中（left.right.right.right....）
		Node<E> p = node.left;
		if (p != null) {
			while (p.right != null) {
				p = p.right;
			}
			return p;
		}
		
		// 从父节点、祖父节点中寻找前驱节点
		while (node.parent != null && node == node.parent.left) {
			node = node.parent;
		}

		// node.parent == null
		// node == node.parent.right
		return node.parent;
	}
	
	protected Node<E> successor(Node<E> node) {
		if (node == null) return null;
		
		// 前驱节点在左子树当中（right.left.left.left....）
		Node<E> p = node.right;
		if (p != null) {
			while (p.left != null) {
				p = p.left;
			}
			return p;
		}
		
		// 从父节点、祖父节点中寻找前驱节点
		while (node.parent != null && node == node.parent.right) {
			node = node.parent;
		}

		return node.parent;
	}

	public static abstract class Visitor<E> {
		/**
		 * @return 如果返回true，就代表停止遍历
		 */
		public abstract boolean visit(E element);
	}
	
	protected static class Node<E> {
		E element;
		Node<E> left;//左子节点
		Node<E> right;
		Node<E> parent;

		public Node(E element, Node<E> parent) {
			this.element = element;
			this.parent = parent;
		}
		
		public boolean isLeaf() {
			return left == null && right == null;
		}
		
		public boolean hasTwoChildren() {
			return left != null && right != null;
		}

		public boolean isLeftChild(){
			return parent != null && this == parent.left;
		}

		public boolean isRightChild(){
			return parent != null && this == parent.right;
		}
	}

	protected Node<E> createNode(E element,Node<E> parent){
		return new Node<>(element,parent);//默认返回Node节点
	}

	@Override
	public Object root() {
		return root;
	}

	@Override
	public Object left(Object node) {
		return ((Node<E>)node).left;
	}

	@Override
	public Object right(Object node) {
		return ((Node<E>)node).right;
	}

	@Override
	public Object string(Object node) {
		Node<E> myNode = (Node<E>)node;
		String parentString = "null";
		if (myNode.parent != null) {
			parentString = myNode.parent.element.toString();
		}
		return myNode.element + "_p(" + parentString + ")";
	}
}
