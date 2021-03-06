import java.util.ArrayList;
import java.util.LinkedList;

public class IndexNode extends Node {
    
	//the constructor for the index node
    public IndexNode(int maxSize) {
        this.setMaxSize(maxSize);
        this.setKeys(new LinkedList<>());
        this.children = new LinkedList<>();
    }
    
    //find the leaf node and insert the key
    @Override
    public Node insert(double index, String value) {
        LeafNode leafNode = findLeaf(index, this);
        return leafNode.insert(index, value);
    }
    
    //for the index node, the splitting rules is different from that of the leaf node, it needs to 
    //consider the children
    public void split(Key midKey, Node rightNode) {

        int i = findPosition(midKey.getIndex(),this);
        this.getKeys().add(i, midKey);
        this.children.add(i + 1, rightNode);
        rightNode.setParent(this);

        IndexNode newRight = new IndexNode(getMaxSize());

        for (int j = getMaxSize(); j > ((getMaxSize() + 1) / 2); j--) {
            newRight.getKeys().addFirst(this.getKeys().remove(j));
            newRight.getChildren().addFirst(this.getChildren().remove(j+1));
        }

        Key newMidKey = this.getKeys().remove( (getMaxSize() + 1) / 2);
        newRight.getChildren().addFirst(this.getChildren().remove(((getMaxSize() + 1) / 2)+1));

        for (int k=0 ; k < newRight.getChildren().size() ; k++ ) {
            newRight.getChildren().get(k).setParent(newRight);
        }
        //split the node into the midkey and a new right node
        //and update the parent

        this.insertToParent(newMidKey, newRight);
    }

    //find the leaf node and search the index
    @Override
    public Key search(double index) {
        Node leafNode = findLeaf(index, this);
        return leafNode.search(index);
    }

    @Override
    //find the leaf node and search the index range
    public ArrayList<Key> search(double index1, double index2) {
        Node leafNode = findLeaf(index1, this);
        return leafNode.search(index1, index2);
    }
    
    //find the leaf node and remove the index
    public Node remove (double index){
    	LeafNode leafNode = findLeaf(index, this);
    	return leafNode.remove(index);
		    	
    }
    
    //this function is for update the parent after each removing operation
    protected Node updateRemove(){
    	if(this.getChildren().size()<this.getMaxSize()/2 || this.getChildren().size()<2 ){
    		//if it is the root and maximum size smaller than 2, delete this root and merge with the children
    		if (this.parent==null){
    			if (children.size()>=2) return findRoot(this);
    			Node root=this.getChildren().get(0);
    			root.parent=null;
    			this.setKeys(null);
    			this.children=null;
    			//System.out.println("Index10");
    			return root;
    		}
    		
    		//find out the previous indexnode and the next indexnode for borrowing
    		int currIdx=this.parent.getChildren().indexOf(this);
    		int prevIdx=currIdx-1;
    		int nextIdx=currIdx+1;
    		
    		Node previous=null;
    		Node next=null;

    		if (prevIdx>=0){
    			previous=this.parent.getChildren().get(prevIdx);
 
    		}
    		if (nextIdx<this.parent.getChildren().size()){
    			next=this.parent.children.get(nextIdx);
    		}
    		
    		//if its previous node size is larger than a threshold, borrow the rightmost index from previous node
    		if (previous!=null && previous.getChildren().size()>this.getMaxSize()/2 && previous.getChildren().size()>2){

    			int idx=previous.getChildren().size()-1;
    			Node borrow=previous.getChildren().get(idx);

    			previous.getChildren().remove(idx);//change here
    			borrow.parent=this;
    			this.getChildren().add(0,borrow);
    			int preIndex=this.parent.getChildren().indexOf(previous);
    			
    			this.getKeys().add(0,this.parent.getKeys().get(preIndex));
    			this.parent.getKeys().set(preIndex, previous.getKeys().remove(idx-1));//change here
    			//System.out.println("Index11");
    			return findRoot(this);
    		}
    		//if its next node size is larger than a threshold, borrow the leftmost index from next node
    		if (next!=null && next.getChildren().size()>this.getMaxSize()/2 &&next.getChildren().size()>2){
    			Node borrow=next.getChildren().get(0);

    			next.getChildren().remove(0);
    			borrow.parent=this;
    			this.getChildren().add(borrow);
    			int preIndex=this.parent.getChildren().indexOf(this);
    			this.getKeys().add(this.parent.getKeys().get(preIndex));
    			this.parent.getKeys().set(preIndex, next.getKeys().remove(0));
    			//System.out.println("Index12");
    			return findRoot(this);
    			
    		}
    		//merge with the previous node
    		if (previous!=null && (previous.getChildren().size()<=this.getMaxSize()/2||previous.getChildren().size()<=2)){
    			for (int i=0;i<this.getChildren().size();i++){
    				previous.getChildren().add(this.getChildren().get(i));
    			}
    			for (int i=0;i<previous.getChildren().size();i++){
    				previous.getChildren().get(i).parent=this;
    			}
    			
    			int indexPre=this.parent.getChildren().indexOf(previous);
    			previous.getKeys().add(this.parent.getKeys().get(indexPre));
    			for (int i=0;i<this.getKeys().size();i++){
    				previous.getKeys().add(this.getKeys().get(i));
    			}
    			this.children=previous.children;
    			this.setKeys(previous.getKeys());
    			//update the relationships in this parent
    			this.parent.getChildren().remove(previous);
    			previous.parent=null;
    			previous.children=null;
    			previous.setKeys(null);
    			this.parent.getKeys().remove(this.parent.getChildren().indexOf(this));
    			if ((this.parent!=null&&(this.parent.getChildren().size()>=this.getMaxSize()/2&&this.parent.getChildren().size()>=2))||this.parent==null&&this.parent.getChildren().size()>=2){
    				//System.out.println("Index13");
    				return findRoot(this);
    			}
    			
    			//System.out.println("Index14");
    			this.parent.updateRemove();
    			
    			return findRoot(this);
    			
    		}
    		//merge with next node
    		if (next!=null && (next.getChildren().size()<=this.getMaxSize()/2 ||next.getChildren().size()<=2)){
    			for (int i=0;i<next.getChildren().size();i++){
    				Node child=next.getChildren().get(i);
    				this.getChildren().add(child);
    				child.parent=this;
    			}
    		
    			int index=this.parent.getChildren().indexOf(this);
    			this.getKeys().add(parent.getKeys().get(index));
    			for (int i=0;i<next.getKeys().size();i++){
    				this.getKeys().add(next.getKeys().get(i));
    			}
    			    //update the relationships in this parent
    				this.parent.getChildren().remove(next);
    				next.parent=null;
    				next.children=null;
    				next.setKeys(null);
    				this.parent.getKeys().remove(this.parent.getChildren().indexOf(this));
    				if ((this.parent!=null &&(this.parent.getChildren().size()>=this.getMaxSize()/2 &&this.parent.getChildren().size()>=2)) || this.parent==null && this.parent.getChildren().size()>=2 ){
    					//System.out.println("Index15");
    					return findRoot(this);
    				}
    				
    				//System.out.println("Index16");
    				this.parent.updateRemove();
    				
    				return findRoot(this); 

    		}
    		
    	}
    	return findRoot(this);
    }

}