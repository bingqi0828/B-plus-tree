import java.io.*;
import java.util.ArrayList;
/*
 * Main function has the functions:
 * 1) Read the strings from input.txt
 * 2) write the operations into the output.txt file
 */
public class bplustree {
	
	/*
	 * Transfer the format of results of the search functions from Node class into 
	 * the uniform class that can be read easier.
	 */
	
    public static String PrintResult(Key key) {
        if (key == null) {
            return "Null\n";
        } else {
        	
            String print = "";
            for (int i = 0; i <  key.getValues().size() -1 ; i++) {
                print += key.getValues().get(i) + ", " ;
            }
            print=print + key.getValues().get(key.getValues().size()-1);
            print =print +"\n";
            return print;
        }
    }

    public static String PrintResult(ArrayList<Key> keys) {

        if (keys.isEmpty()) {
            return "Null\n";
        } else {
        	
            String print = "";

            for (int i = 0 ; i <keys.size()-1 ; i++) {
                for (int j = 0; j < keys.get(i).getValues().size(); j++) {
                    print += keys.get(i).getValues().get(j) + ",";
                }
            }
            
            for (int i=0;i<keys.get(keys.size()-1).getValues().size()-1;i++){
            	print+=keys.get(keys.size()-1).getValues().get(i)+",";
            }
            
            print+=keys.get(keys.size()-1).getValues().get(keys.get(keys.size()-1).getValues().size()-1);
            
            print=print +"\n";
            return print;
        }
    }

    public static void main(String[] args) throws IOException {
    	
   //read file
        File reader = new File("./input.txt");
        BufferedReader readfile = new BufferedReader(new FileReader(reader));
        
        //write file 
        File writer = new File("./output_file.txt");
        BufferedWriter wr = new BufferedWriter(new FileWriter(writer));
        
        String str = null;
        str = readfile.readLine();
        
    	int m;
    	String tem= str.substring(11,str.length()-1);
    	//read the order of the tree
    	m=Integer.parseInt(tem);
    	
    	System.out.println("This is a "+m+"-order tree");
    	
    	//initialize the tree
    	Tree tree = new Tree(m);
    	
        while((str = readfile.readLine()) != null) {
        	//Search(key) function
        	if (!str.contains(",")&&str.startsWith("Search")) { 
        			String str2="";
        			if(str != null && !"".equals(str)){
	        			for(int i=0;i<str.length();i++){
		        			if(str.charAt(i)==45||(str.charAt(i)>=48 && str.charAt(i)<=57)){ 
		        				str2+=str.charAt(i);

	        			}
        			}
	        			float index;
	        			index = Float.parseFloat(str2.trim());
	        			System.out.println(PrintResult(tree.searchTree(index)));
	        			wr.write(PrintResult(tree.searchTree(index)));
	   
        			}
        		}
        	
        	//insert function
        		
        		else if (str.startsWith("Insert")) {
        			float key;
        			String value;
        			String str2="";
        			str2 = str.substring(str.indexOf("(")+1,str.indexOf(",")); 
        			
        			key = Float.parseFloat(str2.trim());
        			value = str.substring(str.indexOf(",")+1,str.indexOf(")")).trim();
        			
        			tree.insertIntoTree(key, value);
        		}
        	
        	     //delete function
        		else if (str.startsWith("Delete")){
        			String str2="";
        			if(str!=null && !"".equals(str)){
        				for (int i=0;i<str.length();i++){
        					if(str.charAt(i)==45||(str.charAt(i)>=48 && str.charAt(i)<=57)){
        						str2+=str.charAt(i);
        					}
        				}
        				
        				float index;
        				index=Float.parseFloat(str2.trim());
        				tree.remove(index);
        				
        			}
        		}
        	
        	//range search function
        		else { 
        			float key1;
        			float key2;
        			String str2="";
        			String str3="";
        			
        			str2 = str.substring(str.indexOf("(")+1,str.indexOf(","));
        			str3 = str.substring(str.indexOf(",")+1,str.indexOf(")"));
        			
        			key1 = Float.parseFloat(str2.trim());
        			key2 = Float.parseFloat(str3.trim());
        			
        			System.out.println(PrintResult(tree.searchTree(key1, key2)));
        			wr.write(PrintResult(tree.searchTree(key1, key2)));
        		}	
        }
        
        wr.close();
        readfile.close();
        
        

    }
    
}

