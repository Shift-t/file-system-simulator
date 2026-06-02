//Abstract Node class for files and directories
public abstract class Node {
    protected String name;
    protected Directory parent; //Every Node has a parent directory except the root Node

    //Constructor method
    public Node(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    //Getter to get the name of the Node
    public String getName() {
        return name;
    }

    //Setter to set the name of the Node
    public void setName(String name) {
        this.name = name;
    }

    //Method to get the parent directory of the node
    public Directory getParent(){
        return parent;
    }

    //Method to set the parent directory of the node
    public void setParent(Directory parent){
        this.parent = parent;
    }

    //Abstract methods to be implemented in the subclasses
    public abstract int getSize();
    public abstract boolean isDirectory();
    public abstract Node getChild(String name);
}
