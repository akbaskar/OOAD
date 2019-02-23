
//CSE 522: Assignment 1, Part 2

  class BST_Part2 {

	  public static void main(String[] args) {
		
		  AbsTree tr = new DupTree(100);
			tr.insert(50);
			tr.insert(125);
			tr.insert(150);
			tr.insert(20);
			tr.insert(75);
			tr.insert(20);
			tr.insert(90);
			tr.insert(50);
			tr.insert(125);
			tr.insert(150);
			tr.insert(75);
			tr.insert(90);
			
			tr.delete(20);
			tr.delete(20);
			tr.delete(20);
			tr.delete(150);
			tr.delete(100);
			tr.delete(150);
			tr.delete(125);
			tr.delete(125);
			tr.delete(50);
			tr.delete(50);
			tr.delete(50);
			tr.delete(75);
			tr.delete(90);
			tr.delete(75);
			tr.delete(90);
		}
  }
  
  abstract class AbsTree {

	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}
	
	//Constructor to update the parent node while creation of new tree
	public AbsTree(int n, AbsTree p) {
		value = n;
		left = null;
		right = null;
		parent = p;
	}

	public void insert(int n) {
		if (value == n)
			count_duplicates();
		else if (value < n)
			if (right == null)
				right = add_node(n);
			else
				right.insert(n);
		else if (left == null)
			left = add_node(n);
		else
			left.insert(n);
	}
	
	public void delete(int n) {  		
	// adapt Part 1 solution and use here
		AbsTree t = find(n);	
		if (t == null) { // n is not in the tree
			System.out.println("Unable to delete " + n + " -- not in the tree!");
			return;
		} 
		else if (t.left == null && t.right == null) { // n is at a leaf position
			if (t != this) {// if t is not the root of the tree
				if(t.check_duplicates()) {
					case1(t);
				}
			}
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		} else if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // if t is not the root of the tree
				if(t.check_duplicates())
					case2(t);
			return;
			} else { // t is the root of the tree with one subtree
				if (t.left != null) {
					if(t.check_duplicates())
						case3L(t);
				}
				else
					if(t.check_duplicates())
						case3R(t);
				return;
			}
		} else 
			// t has two subtrees; replace n with the smallest value in t's right subtree
			if(t.check_duplicates())
				case3R(t);
		
	}
	
	protected void case1(AbsTree t) {  
		// remove leaf node t;
		if (t.parent.right == t) // t is in the right of the parent tree
			t.parent.right = null;
		else // t is in the left of the parent tree
			t.parent.left = null;
		t.parent = null;
	}
	
	protected void case2(AbsTree t) { 
		// remove internal node t;
		if (t.parent.right == t) // t is in the right of the parent tree
			if (t.right == null) { // one subtree is in left of t
				t.parent.right = t.left;
				t.left.parent = t.parent;
			}
			else {// one subtree in in right of t
				t.parent.right = t.right;
				t.right.parent = t.parent;
			}
		else // t is in the left of the parent tree
			if (t.right == null) { // one subtree is in left of t
				t.parent.left = t.left;
				t.left.parent = t.parent;
			}
			else {// one subtree in in right of t
				t.parent.left = t.right;
				t.right.parent = t.parent;
			}
		t.parent = null;
		t.right = null;
		t.left = null;
	}
	
	protected void case3L(AbsTree t) { 
		// replace t.value with the largest value, v, in
		// t's left subtree; then delete value v from tree;
		AbsTree leftMax = t.left.max();
		t.value = leftMax.value;
		//t.count  = leftmax.count
		t.copyCount(leftMax);
		if (leftMax.left == null) // leftMax doesn't have any subtree
			case1(leftMax);
		else // leftMax have a subtree
			case2(leftMax);
	}

	protected void case3R(AbsTree t) {  
		// replace t.value with the smallest value, v, in
		// t's right subtree; then delete value v from tree;
		AbsTree rightMin = t.right.min();
		t.value = rightMin.value;
		t.copyCount(rightMin);
		if (rightMin.right == null) // rightMin doesn't have any subtree
			case1(rightMin);
		else // rightMin have a subtree
			case2(rightMin);
	}

	private AbsTree find(int n) {
		// returns the Tree node with value n;
		// returns null if n is not present in the tree;
		if (value == n)
			return this;
		else if (value < n) {
			if (right != null)
				return right.find(n);
			else
				return null;
		}
		else if (value > n) {
			if (left != null)
				return left.find(n);
			else
				return null;
		}
		else
			return null;
	}
	
	public AbsTree min() {
		// returns the Tree node with the minimum value;
		if (left == null)
			return this;
		return left.min();	
	}

	public AbsTree max() {
		// returns the Tree node with the maximum value;
		if (right == null)
			return this;
		return right.max();
	}

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
	protected AbsTree parent;

	// Protected Abstract Methods
	
	protected abstract AbsTree add_node(int n);
	protected abstract void count_duplicates();

	// Additional protected abstract methods, as needed
	protected abstract boolean check_duplicates();
	protected abstract int getCount();
	protected abstract void copyCount(AbsTree temp);
}


class Tree extends AbsTree {

	public Tree(int n) {
		super(n);
	}
	
	public Tree(int n, AbsTree p) {
		super(n,p);
	}
	

	protected AbsTree add_node(int n) {
		return new Tree(n, this);
	}

	protected void count_duplicates() {
		;
	}
	
	// define additional protected methods here, as needed
	protected boolean check_duplicates() {
		return true;
	}
	protected int getCount() {
		return 0;
	}
	protected void copyCount(AbsTree temp) {
		;
	}

}

class DupTree extends AbsTree {

	public DupTree(int n) {
		super(n);
		count = 1;
	};
	
	public DupTree(int n, AbsTree p) {
		super(n,p);
		count = 1;
	};

	protected AbsTree add_node(int n) {
		return new DupTree(n, this);
	}

	protected void count_duplicates() {
		count++;
	}
	
	// define additional protected methods here, as needed
	protected boolean check_duplicates() {
		if (count > 1) {
			count --;
			return false;
		}
		else
			return true;
	}
	
	protected int getCount() {
		return count;
	}

	protected void copyCount(AbsTree temp) {
		count = temp.getCount();
	}

	protected int count;
}