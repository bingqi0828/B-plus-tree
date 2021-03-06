import java.util.ArrayList;
import java.util.LinkedList;
/*
 * This is the leaf node 
 * Function:
 * 1) Insert
 * 2) Split
 * 3) Remove
 * 4) Building the sibling connections
 */

public class LeafNode extends Node {

    //the left leaf node of this node
    private LeafNode rightsibling;     
    //the right leaf node of this node
    private LeafNode leftsibling;
    
    //constructor for the leaf node
    public LeafNode(int maxSize) {
        this.setMaxSize(maxSize);
        this.setKeys(new LinkedList<>());
        this.children=new LinkedList<>();
        rightsibling = null;
        leftsibling=null;
    }
    
    
    /*
     * Some basic methods to call or assignment the parameters of the leaf node
     */
    public LeafNode getLeftSibling(){
    	return leftsibling;
    }
    
    public void setLeftSibling(LeafNode leftSibling){
    	this.leftsibling=leftSibling;
    }
    
    public LeafNode getRightSibling() {
        return rightsibling;
    }

    public void setRightSibling(LeafNode rightSibling) {
        this.rightsibling = rightSibling;
    }
    
    //insert the key into the leaf node
    @Override
    public Node insert(double index, String value) {
    	//this is empty, just insert

        if (this.getKeys().isEmpty()) {
            this.getKeys().add(new Key(index, value));
        } else {
        	
        	//when the index exists, add the value into the existing value lists
            if (search(index) != null) {
                search(index).getValues().add(value);
            } else {
            	//when the node is not full, just insert the key into the node

                if (!isFull(this)) {
                    int i = findPosition(index, this);
                    this.getKeys().add(i, new Key(index, value));
                }
                
              //when the node is full, insert the node into the tree and split the node and update the parent
                else {
                    this.split(new Key(index, value));
                }
            }
        }

        // return the root of the tree
        return findRoot(this);
    }



    public void split(Key newKey) {

        int i = findPosition(newKey.getIndex(), this);
        this.getKeys().add(i, newKey);
        LeafNode right = new LeafNode(getMaxSize());
        
        //create the right node
        for (int j = getMaxSize(); j >= ((getMaxSize() + 1) / 2); j--) {
            right.getKeys().addFirst(this.getKeys().remove(j));
        }
        //add left sibling
        if (this.rightsibling!=null){
        	this.rightsibling.setLeftSibling(right);
        }
        //add right sibling
        right.setRightSibling(this.getRightSibling());
        this.setRightSibling(right);
      
        //update left sibling
        right.setLeftSibling(this);
        
        //update the parent
        Key midKey = new Key(right.getKeys().get(0).getIndex());
        this.insertToParent(midKey, right);
    }

    //The search function of the node
    @Override
    public Key search(double index) {
    	//traverse the keys in the node and search the index
        int i = findPosition(index, this);
        if (i > 0) {
            if (this.getKeys().get(i - 1).getIndex() == index) {
                return this.getKeys().get(i - 1);
            } else {
                return null;
            }
        } else if (i == 0) {
            if (this.getKeys().get(i).getIndex() == index) {
                return this.getKeys().get(i);
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Key> search(double index1, double index2) {
        int i = findPosition(index1, this);

        ArrayList<Key> results = new ArrayList<Key>();
        LeafNode searchNode = this;
        //the initial key position
        if (i > 0) {
            if (this.getKeys().get(i - 1).getIndex() == index1) {
                i = i - 1;
            }
        }
        //if this node's largest key index is still smaller than the index
        //go to this nodes' rightsibling and find out other index that within the range

        if (i > (searchNode.getKeys().size())-1) {
            if (searchNode.getRightSibling() != null) {
                searchNode = searchNode.getRightSibling();
                i = 0;
            } else {
                return results;
            }
        }

        while (searchNode.getKeys().get(i).getIndex() <= index2) {
            results.add(searchNode.getKeys().get(i));
            i++;
            if (i > (searchNode.getKeys().size())-1) {
                if (searchNode.getRightSibling() != null) {
                    searchNode = searchNode.getRightSibling();
                    i = 0;
                } else {
                    break;
                }
            }
        }
        return results;
    }
    
   //Remove function of the Node  
    public Node remove (double index){
    	double result=0;
    	//if this leaf node does not contain index, just out of the index
    	if (contains(index,this)==false){
    		//System.out.println("Leaf1");
    		return findRoot(this);
    	}
    	
    	//if this node is the root, just delete the index
    	if (this.parent==null){
    		deletePosition(index, this);
    		//System.out.println("Leaf2");
    		return findRoot(this);
    	}
    	
    	//if this node size is larger than a threshold, just delete the node
    	if (this.getKeys().size()>this.getMaxSize()/2 ){
    		deletePosition(index, this);
    		//System.out.println("Leaf3");
    		return findRoot(this);
    	}
    	//if this node's left sibling's size is larger than a threshold, borrow the leftmost key from leftsibling and update the parent
    	if ((this.leftsibling!=null)&&(this.leftsibling.parent==this.parent)&&(this.leftsibling.getKeys().size()>this.getMaxSize()/2)&&(this.leftsibling.getKeys().size()>=2)){
    		int size=this.leftsibling.getKeys().size();
    		this.getKeys().add(0,this.leftsibling.getKeys().remove(size-1));
    		int index1=this.parent.getChildren().indexOf(this.leftsibling);
    		this.parent.getKeys().set(index1, this.getKeys().get(0));
    		deletePosition(index, this);
    		//System.out.println("Leaf4");
    		return findRoot(this);
    	}
    	//if this node's right sibling's size is larger than a threshold, borrow the leftmost key from rightsibling and update the parent
    	if (this.rightsibling!=null&&this.rightsibling.parent==parent&&this.rightsibling.getKeys().size()>this.getMaxSize()/2&&this.rightsibling.getKeys().size()>=2){
    		this.getKeys().add(this.rightsibling.getKeys().remove(0));
    		int index2=this.parent.getChildren().indexOf(this);
    		this.parent.getKeys().set(index2, this.rightsibling.getKeys().get(0));
    		deletePosition(index, this);
    		//System.out.println("Leaf5");
    		return findRoot(this);
    	}
    	
    	//combine with the previous node if its left sibling is insufficient
    	if (this.leftsibling!=null&&this.leftsibling.parent==this.parent&& (this.leftsibling.getKeys().size()<=this.getMaxSize()/2||this.leftsibling.getKeys().size()<2) ){
    		deletePosition(index, this);
    		
    		for (int i=0;i<this.getKeys().size();i++){
    			this.leftsibling.getKeys().add(this.getKeys().get(i));
    		}
    		this.setKeys(this.leftsibling.getKeys());
    		this.parent.getChildren().remove(this.leftsibling);
    		this.leftsibling.parent=null;
    		this.leftsibling.setKeys(null);
    		
    		//update the sibling relationship
    		if (this.leftsibling.leftsibling!=null){
    			LeafNode temp=this.leftsibling;
    			temp.leftsibling.rightsibling=this;
    			this.leftsibling=temp.leftsibling;
    			temp.leftsibling=null;
    			temp.rightsibling=null;
    		}else{
    			this.leftsibling.rightsibling=null;
    			this.leftsibling=null;
    		}
    		
    		this.parent.getKeys().remove(this.parent.getChildren().indexOf(this)); ///mark
    		//if the parent's parent has enough size
    		if (( (this.parent.parent!=null) && (this.parent.getChildren().size()>=this.getMaxSize()/2 &&this.parent.getChildren().size()>=2) )||(this.parent.parent==null&&this.parent.getChildren().size()>=2)){
    			//System.out.println("Leaf6");
    			return findRoot(this);
    		}
    		//System.out.println("Leaf7");
    		
    		this.parent.updateRemove();
    		
    		return findRoot(this);		

    	}
    	
    	//combine with next node
    	if (this.rightsibling!=null&&this.rightsibling.parent==this.parent&&(this.rightsibling.getKeys().size() < this.getMaxSize()/2||this.rightsibling.getKeys().size()<=2)){
    		deletePosition(index, this);
    		
    		for (int i=0;i<this.rightsibling.getKeys().size();i++){
    			this.getKeys().add(this.rightsibling.getKeys().get(i));
    		}
    		
    		this.rightsibling.parent=null;
    		this.rightsibling.setKeys(null);
    		this.parent.getChildren().remove(this.rightsibling);
    		//update the sibling relationship
    		if (this.rightsibling.rightsibling!=null){
    			LeafNode temp=this.rightsibling;
    			temp.rightsibling.leftsibling=this;
    			this.rightsibling=temp.rightsibling;
    			temp.rightsibling=null;
    			temp.leftsibling=null;
    		}else{
    			this.rightsibling.leftsibling=null;
    			this.rightsibling=null;
    		}
    		
    		this.parent.getKeys().remove(this.parent.getChildren().indexOf(this));
    		if (( (this.parent.parent!=null) && (this.parent.getChildren().size()>=this.getMaxSize()/2 &&this.parent.getChildren().size()>=2) )||(this.parent.parent==null&&this.parent.getChildren().size()>=2)){
    			//System.out.println("Leaf8");
    			return findRoot(this);
    		}
    		
    		//System.out.println("Leaf9");
    		//this node's parents needs to updated
    		this.parent.updateRemove();
    		
    		return findRoot(this);		

    	}
    	return findRoot(this);
	
    }

}