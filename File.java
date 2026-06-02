//Node subclass for Files
public class File extends Node{
    private int size; //Size of the file
    private String data; //Data contained in the file

    //Constructor Method without input data
    public File(String name, Directory parent, int size){
        super(name, parent);
        this.size = size;
        this.data = "";
    }

    //Constructor Method with data input
    public File(String name, Directory parent, String data){
        super(name, parent);
        this.data = data;
        this.size = data.length();
    }

    //Method to write data to the file (deletes old data)
    public void write(String newData){
        this.data = newData;
        this.size = newData.length();
    }

    //Method to append data to the file (does not delete old data)
    public void append(String newData){
        this.data += newData;
        this.size = this.data.length();
    }

    //Getter method to get the file data
    public String getData(){
        return this.data;
    }

    @Override
    //Returns the size of data stored in the file
    public int getSize(){
        return this.size;
    }

    @Override
    //is used to check if a Node is a directory or a file
    public boolean isDirectory(){
        return false;
    }

    @Override
    //Returns null for the getChild method since files cannot have children
    public Node getChild(String name){
        return null;
    }
}
