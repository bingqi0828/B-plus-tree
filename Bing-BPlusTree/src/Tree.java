import java.util.ArrayList;
/*
 * Function:
 * 1) Insert
 * 2) Search
 * 3) Range search
 * 4) Remove
 */

public class Tree {
/*
 * The order of the tree
 */
    private int treeOrder;
    /*
     * The root of the tree
     */
    private Node root;

    /*
     * The constructor of the tree
     * And the maximum size of the node is the order of tree minus 1
     */

    public Tree(int treeOrder) {
        this.treeOrder = treeOrder;
        this.root = new LeafNode(treeOrder-1); 
    }
    
    /*
     * Insert function
     * return the root of this tree
     */

    public void insertIntoTree(float index, String value) {
            this.root = root.insert(index, value);
    }

    /*
     * Search function
     * return the value of this tree
     */

    public Key searchTree(double index) {
        Key result = root.search(index);	
        return result;
    }
    
    /*
     * Remove function
     * return the root of this tree
     */
    
    public void remove(float index){
    	this.root=root.remove(index);
    }
    
    /*
     * Range search function
     * return the value of this tree
     */
    
    public ArrayList<Key> searchTree(double index1, double index2) {
        ArrayList<Key> results = root.search(index1, index2);
        return results;
    }


}
