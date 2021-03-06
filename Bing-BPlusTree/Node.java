import java.util.ArrayList;
import java.util.LinkedList;
/*
 * Node is a abstract class
 * It include some functions that will be used by indexnode and leafnode
 * Index and leafnode will both extends the functions of Node
 * Also, they will include some functions by themselves
 */

public abstract class Node {
	/*
	 * Initial the paremeters of the node
	 */
	//the keys of each node
    protected LinkedList<Key> keys;
    
    //parent of each node
    protected IndexNode parent;

    //the maximum size of each node
    private int maxSize;
    
    //the children list of each node
    protected LinkedList<Node> children;
    
    //construct the abstract methods here to be extends by index nodes and leaf nodes
    public abstract Node insert(double index, String value);
    
    public abstract Node remove(double index);

    public abstract Key search(double index);

    public abstract ArrayList<Key> search(double index1, double index2);
    
    
	/*
	 * Some basic methods to be applied to call the internal parameters
	 */
    public void setParent(IndexNode parent) {
        this.parent = parent;
    }

    public LinkedList<Key> getKeys() {
        return this.keys;
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setKeys(LinkedList<Key> keys) {
        this.keys = keys;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public LinkedList<Node> getChildren() {
        return children;
    }
	/*
	 * This method is applied when the nodes has been splitted
	 * During inserting process when the nodes has been splitted into midkey and the right node
	 * The midkey will be merged with the parent
	 * The parent node will be updated
	 * This is used by index node and leaf node
	 */

    protected void insertToParent(Key midKey, Node rightNode) {

        if (parent == null) {
        	
        	//when it is the root node
        	//just build a new index node and take it as the root node

            IndexNode newParent = new IndexNode(maxSize);
            newParent.getKeys().add(midKey);
            newParent.getChildren().add(this);
            newParent.getChildren().add(rightNode);

            this.setParent(newParent);
            rightNode.setParent(newParent);

        } else {
        	//the parent is not full, just insert the midkey into the parent
            if (!isFull(parent)) {
                int i = findPosition(midKey.getIndex(), parent);
                parent.getKeys().add(i, midKey);
                parent.getChildren().add(i + 1, rightNode);
                rightNode.setParent(this.parent);
            }

            // the parent is full
            else {
                parent.split(midKey,  rightNode);
            }
        }
    }

    
	/*
	 * Some useful functions that will be used to make the process convenient
	 */
     
    //determine whether a node is full
    protected boolean isFull(Node node) {

        return (node.getKeys().size() >= node.getMaxSize());
    }
    
    //find the position in a node that will be used to put the index
    //return the the first key value that larger than the index
    protected int findPosition(double index, Node node) {
    	
        for (int i = 0; i < node.getKeys().size(); i++) {

            if (index < node.getKeys().get(i).getIndex()) {
                return i;
            }
        }
        return node.getKeys().size();
    }
    
    //return to the root of the current node
    protected Node findRoot(Node node) {

        while (node.parent != null) {
            node = node.parent;
        }
        return node;
    }
    
    //return to the leaf where the index is located
    protected LeafNode findLeaf(double index, Node root) {
        Node currentNode = root;
        while (currentNode instanceof IndexNode) {
            int i = findPosition(index, currentNode);
            currentNode = ((IndexNode) currentNode).getChildren().get(i);
        }
        return ((LeafNode) currentNode);
    }
    
    //delete the indicated keys in the node 
    //return a double parameter
    protected double deletePosition (double index, Node node){
    	int low=0;
    	int high=node.getKeys().size()-1;
    	int mid=0;
    	double comp;
    	double result=0;
    	while (low<=high){
    		mid=(low+high)/2;
    		comp=node.getKeys().get(mid).getIndex()-index;
    		if (comp==0){
    			result=node.getKeys().get(mid).getIndex();
    			node.getKeys().remove(mid);
    			return result;
    			
    		}else if (comp<0){
    			low=mid+1;
    		}else {
    			high=mid-1;
    		}
    	}
    	return result;
		
    }
    
    //determine whether the node contains the index
    //return a true or false
    protected boolean contains(double index, Node node){
    	int low=0;
    	int high=node.getKeys().size()-1;
    	int mid=0;
    	double comp;
    	while (low<=high){
    		mid=(low+high)/2;
    		comp=node.getKeys().get(mid).getIndex()-index;
    		if (comp==0){
    			return true;
    		}else if (comp<0){
    			low=mid+1;
    		}else {
    			high=mid-1;
    		}
    	}
    	return false;
    }
    
}
