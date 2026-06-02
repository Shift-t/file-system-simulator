//Node Subclass for directories
import java.util.ArrayList;

public class Directory extends Node{
    //Each directory stores its children in a hashtable
    private HashTable children;

    public Directory(String name, Directory parent) {
        super(name, parent);
        children = new HashTable(10); //Capacity set to 10 change if needed
    }

    //Adds a child to the hashtable associated with the directory
    public boolean addChild(Node child) {
        if (children.containsKey(child.getName())) {
            return false;
        }
        children.insert(child.getName(), child);
        return true;
    }

    //Searches for a given child in the Hashtable associated with the directory
    public Node getChild(String name) {
        return children.search(name);
    }

    //Removes a child from the hashtable associated with the directory
    public boolean removeChild(String name) {
        if (children.containsKey(name)){
            children.remove(name);
            return true;
        }
        return false;
    }

    //Returns an array list of strings containing the names of all the directory's
    //children with a / for subdirectories, and the name and size for files
    public ArrayList<String> getAllChildren(){
        ArrayList<String> childList = new ArrayList<String>();

        for (Node child: children.getNodes()){
            if (child.isDirectory()){
                childList.add(child.getName() + "/");
            }
            else{
                childList.add(child.getName() + " (" + child.getSize() + "B)");
            }
        }
        return childList;
    }

    //Returns an array list of nodes containing all the children nodes of the directory
    public ArrayList<Node> getChildrenNodes(){
        return children.getNodes();
    }

    @Override
    //returns the total size of the directory by calculating the size of all of its children
    public int getSize() {
        int size = 0;
        for (Node child: children.getNodes()){
            size += child.getSize();
        }
        return size;
    }

    @Override
    //is used to check if a Node is a directory or a file
    public boolean isDirectory() {
        return true;
    }
}
