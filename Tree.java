// N-ary Tree implementation to build the hierarchical
// tree structure of the file management system
import java.util.ArrayList;
public class Tree {
    protected Directory root; //Root directory
    protected Directory currentDir;

    //Constructor Method
    public Tree() {
        root = new Directory("/", null); //initializing the root directory
        currentDir = root;
    }

    //Check to see if the tree is empty
    public boolean isEmpty(){
        return root == null;
    }

    //Clears the tree
    public void clear(){
        root = null;
        currentDir = null;
    }

    //Returns the root
    public Directory getRoot(){
        return root;
    }

    //Returns the currrent Directory the user is in
    public Directory getCurrentDir(){
        return currentDir;
    }

    //sets the current directory
    public void setCurrentDir(Directory dir){
        this.currentDir = dir;
    }

    //Searches for a Node given the path
    public Node search(String path){
        if (root == null || path == null || path.isEmpty()){
            return null; //for empty path input
        }

        //starting Directory is set to current Directory
        Directory startDir = currentDir;

        //If the absolute path is given the starting directory is changed to the root
        if (path.charAt(0) == '/'){
            startDir = root;
        }

        Node current = startDir;

        //Splits the given path into an array of strings using / as the parameter of splitting
        String[] parts = path.split("/");

        //Enhanced for loop to go through each node in the path
        for(String part : parts){
            //if the Node name is empty skip it
            if (part.isEmpty()){
                continue;
            }

            //if the part of the path is ".." go to the parent node
            if (part.equals("..")){
                if (current.getParent() != null){
                    current = current.getParent();
                }
            }
            else{
                //if the node is a directory get its child using next part of path as child name
                if (current.isDirectory()){
                    Node child = current.getChild(part);
                    //if the child doesn't exist return null
                    if (child == null){
                        return null;
                    }
                    //if the child exists make current the child to run the next iteration of the for loop on
                    current = child;
                }
                else{
                    //if the child is a file and it isn't the last part of the path return null
                    return null;
                }
            }
        }
        return current;
    }

}
