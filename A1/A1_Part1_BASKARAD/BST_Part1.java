// CSE 522: Assignment 1, Part 1

class BST_Part1 {

	public static void main(String args[]) {
		 Tree tr;
		 tr = new Tree(100);
		 tr.insert(50);
		 tr.insert(125);
		 tr.insert(150);
		 tr.insert(20);
		 tr.insert(75);
		 tr.insert(20);	
		 tr.insert(90);
		 tr.delete(20);
		 tr.delete(20);
		 tr.delete(125);
		 tr.delete(150);
		 tr.delete(100);
		 tr.delete(50);
		 tr.delete(75);
		 tr.delete(25);
		 tr.delete(90);
	}
}

class Tree { // Defines one node of a binary search tree
	
	public Tree(int n) {
		value = n;
		left = null;
		right = null;
	}
	
	//Constructor to update the parent node while creation of new tree
	public Tree(int n, Tree p) {
		value = n;
		left = null;
		right = null;
		parent = p;
	}
	
	public void insert(int n) {
		if (value == n)
			return;
		if (value < n)
			if (right == null)
				right = new Tree(n, this);
			else
				right.insert(n);
		else if (left == null)
			left = new Tree(n,this);
		else
			left.insert(n);
	}

	
	public Tree min() {
		// returns the Tree node with the minimum value;
		if (left == null)
			return this;
		return left.min();
		
	}
	
	public Tree max() {
		// returns the Tree node with the maximum value;
		if (right == null)
			return this;
		return right.max();
	}
	
	
	public Tree find(int n) {
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
	
	public void delete(int n) {  
		//
		// *** do not modify this method ***
		//
		Tree t = find(n);
		if (t == null) { // n is not in the tree
			System.out.println("Unable to delete " + n + " -- not in the tree!");
			return;
		} else if (t.left == null && t.right == null) { // n is at a leaf position
			if (t != this) // if t is not the root of the tree
				case1(t);
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		} else if (t.left == null || t.right == null) {
			// t has one subtree only
			if (t != this) { // if t is not the root of the tree
				case2(t);
				return;
			} else { // t is the root of the tree with one subtree
				if (t.left != null)
					case3L(t);
				else
					case3R(t);
				return;
			}
		} else 
			// t has two subtrees; replace n with the smallest value in t's right subtree
			case3R(t);
	}
	
	private void case1(Tree t) {  
	// remove leaf node t;
		if (t.parent.right == t) // t is in the right of the parent tree
			t.parent.right = null;
		else // t is in the left of the parent tree
			t.parent.left = null;
		t.parent = null;
	}
	
	private void case2(Tree t) {  
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
	
	private void case3L(Tree t) {   
	// replace t.value with the largest value, v, in
	// t's left subtree; then delete value v from tree;
		Tree leftMax = t.left.max();
		t.value = leftMax.value;
		if (leftMax.left == null) // leftMax doesn't have any subtree
			case1(leftMax);
		else // leftMax have a subtree
			case2(leftMax);
 	}

	private void case3R(Tree t) {   
	// replace t.value with the smallest value, v, in
	// t's right subtree; then delete value v from tree;
		Tree rightMin = t.right.min();
		t.value = rightMin.value;
		if (rightMin.right == null) // rightMin doesn't have any subtree
			case1(rightMin);
		else // rightMin have a subtree
			case2(rightMin);
 	}

	protected int value;
	protected Tree left;
	protected Tree right;
      protected Tree parent;
}


