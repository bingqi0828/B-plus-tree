/*
 * Key of each node
 * For index node, there is just a index
 * For leaf node, there is a index and a value list
 */

import java.util.ArrayList;
import java.util.LinkedList;

public class Key {
	/*
	 * Index is for searching
	 */
    private double index;
    
	/*
	 * Value is for storing values inside the index
	 */
    private ArrayList<String> values;

	/*
	 * Constructor for index node
	 */
    public Key(double index) {
        this.index = index;
    }
    
	/*
	 * Constructor for leaf node
	 */
    public Key(double index, String value) {
        this.index = index;
        this.values = new ArrayList<String>();
        this.values.add(value);
    }

	/*
	 * Some basic functions to call the paremeters inside the key
	 */
    public double getIndex() {
        return index;
    }

    public ArrayList<String> getValues() {
        return values;
    }
   
}