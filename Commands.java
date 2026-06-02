import java.util.ArrayList;
import java.util.Stack;

public class Commands {

    //get full path
    public static String pwd(Directory currentDirectory){
        if (currentDirectory.getParent() == null){
            return "/";
        }

        //Use a stack to get the path
        Stack<String> pathStack = new Stack<>();

        // get parents and push to stack
        while (currentDirectory.getParent() != null){
            pathStack.push(currentDirectory.getName());
            currentDirectory = currentDirectory.getParent();
        }
        //Use string builder to make the path
        StringBuilder builder = new StringBuilder();
        while (!pathStack.isEmpty()){
            builder.append("/");
            builder.append(pathStack.pop());
        }
        return builder.toString();
    }

    // show contents
    public static void ls(Directory currentDirectory){
        // show each child
        ArrayList<String> children = currentDirectory.getAllChildren();
        for (String child : children){
            System.out.print(child + "    ");
        }
        System.out.println();
    }

    // creates a new file or clears an existing one
    public static void touch(String[] tokens, Tree fileSystem ){
        if (tokens.length < 3) {
            System.out.println("Error: File name and size arguments required.");
            return;
        }
        
        String inputPath = tokens[1];
        int size;
        try {
            size = Integer.parseInt(tokens[2]);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid file size. Size must be an integer.");
            return;
        }

        Directory targetDir;
        String fileName;

        //check if the input contains a path
        int slashIndex = inputPath.lastIndexOf('/');
        //no slashes, use the current directory
        if (slashIndex == -1){
            fileName = inputPath;
            targetDir = fileSystem.getCurrentDir();
        }
        else{
            //Has slashes
            String parentPath;
            //File at root
            if (slashIndex == 0){
                parentPath = "/";
            }
            else{
                parentPath = inputPath.substring(0, slashIndex);
            }

            //Check to see if directory exists at path
            Node foundNode = fileSystem.search(parentPath);

            //if directory not found
            if (foundNode == null){
                System.out.println("Error: Directory '" + parentPath + "' not found.");
                return;
            }
            //if given path is for a file not a directory
            if(!foundNode.isDirectory()){
                System.out.println("Error: '" + parentPath + "' is not a directory.");
                return;
            }
            targetDir = (Directory) foundNode;
            fileName = inputPath.substring(slashIndex + 1);
        }

        if (fileName.trim().isEmpty()) {
            System.out.println("Error: Filename cannot be empty.");
            return;
        }

        //Create a file at the found target directory
        Node existing = targetDir.getChild(fileName);
        if (existing != null){
            //Check to see if a directory exists with the same file name
            if (existing.isDirectory()){
                System.out.println("Error: Directory already exists with the same name");
            }
            else{
                //Overwrite existing file
                targetDir.removeChild(fileName);
                targetDir.addChild(new File(fileName, targetDir, size));
            }
        }
        else{
            //Create new file
            targetDir.addChild(new File(fileName, targetDir, size));
        }
    }

    //Writing to a file
    public static void echo(String input, Tree fileSystem ){
        // Extract text and validate structure
        int firstQuote = input.indexOf('"');
        int lastQuote = input.lastIndexOf('"');
        int symbol = input.lastIndexOf('>');

        if (firstQuote == -1 || lastQuote == -1 || symbol == -1 || firstQuote >= lastQuote || symbol < lastQuote){
            System.out.println("Error: Invalid input format. Expected \"text\" > filename");
            return;
        }

        String text = input.substring(firstQuote + 1, lastQuote);
        String inputPath = input.substring(symbol + 1).trim();

        Directory targetDir;
        String fileName;

        //resolving the path
        int lastSlashIndex = inputPath.lastIndexOf('/');
        if (lastSlashIndex == -1) {
            targetDir = fileSystem.getCurrentDir();
            fileName = inputPath;
        } else {
            String parentPath;
            if (lastSlashIndex == 0) {
                parentPath = "/";
            } else {
                parentPath = inputPath.substring(0, lastSlashIndex);
            }

            Node foundNode = fileSystem.search(parentPath);
            if (foundNode == null || !foundNode.isDirectory()) {
                System.out.println("Error: Path not found.");
                return;
            }
            targetDir = (Directory) foundNode;
            fileName = inputPath.substring(lastSlashIndex + 1);
        }

        if (fileName.trim().isEmpty()) {
            System.out.println("Error: Filename cannot be empty.");
            return;
        }

        //Writing to file
        Node existing = targetDir.getChild(fileName);

        if (existing != null) {
            if (!existing.isDirectory()) {
                ((File) existing).write(text);
            } else {
                System.out.println("Error: Cannot write to a directory.");
            }
        } else {
            targetDir.addChild(new File(fileName, targetDir, text));
        }
    }

    public static void tree( Directory currentDirectory ){
        System.out.println(".");
        printTree(currentDirectory, "");
    }

    // helper method
    public static void printTree( Directory currentDirectory , String prefix){

        if (currentDirectory == null){
            return;
        }

        ArrayList<Node> children = currentDirectory.getChildrenNodes();

        // to track when we get to the final element
        int count = 0;
        int total = children.size();

        for (Node child : children) {
            count++;
            boolean isLast = (count == total);

            String connector = isLast ? "└── " : "├── ";

            if (child.isDirectory()){

                System.out.println(prefix + connector + child.getName() + "/");

                // if it is the last directory then the lower children need spaces
                // else they need a line to continue the tree structure
                String nextPrefix = prefix + (isLast ? "    " : "│   ");

                // child must be a directory so we type it
                // recursively go through each directory
                printTree((Directory) child, nextPrefix);

            } else {

                // child is a file, so it is just a leaf
                System.out.println(prefix + connector + child.getName() +" (" + child.getSize() + "B)");

            }
        }
    }

    // Change Directory
    public static void cd(String[] tokens, Tree fileSystem){
        if (tokens.length < 2) {
            System.out.println("Error: Path argument missing.");
            return;
        }

        String path = tokens[1];

        if (path.equals("..")){
            if(fileSystem.getCurrentDir().getParent() != null) {
                fileSystem.setCurrentDir(fileSystem.getCurrentDir().getParent());
            }
            return;
        }

        Node target = fileSystem.search(path);

        // if directory does not exist or is a file
        if (target == null) {
            // does not exist
            System.out.println("Error: Path '" + path + "' not found.");
        }
        else if (!target.isDirectory()){
            // target is a file
            System.out.println("Error: '" + target.getName() + "' is not a directory.");
        }
        else {
            // target IS a directory so typecast it
            fileSystem.setCurrentDir((Directory) target);
        }
    }

    // Create new directory
    public static void mkdir(String[] tokens, Tree fileSystem){

        if (tokens.length < 2) {
            System.out.println("Error: Directory name argument missing.");
            return;
        }

        // check for -p
        if (tokens[1].equals("-p")){
            if (tokens.length < 3) {
                System.out.println("Error: Path argument missing for -p");
                return;
            }
            String path = tokens[2];
        
            String[] parts = path.split("/");
            Directory current =  fileSystem.getCurrentDir();
            if (path.startsWith("/")) {
                current = fileSystem.getRoot();
            }

            for (String part : parts){
                // skip blanks
                if (part.isEmpty()) continue;

                // check location
                Node target = current.getChild(part);

                if  (target == null){
                    // doesn't exist so create a new directory
                    Directory newDir = new Directory(part, current);
                    current.addChild(newDir);
                    current = newDir;
                } else {
                    // if exists, move to directory
                    if (!target.isDirectory()) {
                        System.out.println("Error: '" + target.getName() + "' already exists and is not a directory." );
                        break;
                    }
                    current = (Directory) target;
                }
            }

        } else {
            Directory current = fileSystem.getCurrentDir();
            for (int i = 1; i < tokens.length; i++){

                String name = tokens[i];
                if (name.trim().isEmpty()) continue;
                if (!current.addChild(new Directory(name, current))){
                    System.out.println("Error: '" + name + "' already exists.");
                }
            }
        }
    }

    // get total size
    public static void du( Directory currentDirectory ){

        int totalSize = currentDirectory.getSize();
        System.out.println("Total size: " + totalSize +  "B");
    }

    // pattern search in file
    public static void grep(String input,  Tree fileSystem){

        // extract pattern and file name
        int firstQuote = input.indexOf('"');
        int lastQuote = input.lastIndexOf('"');

        if (firstQuote == -1 || lastQuote == -1 || firstQuote >= lastQuote){
            System.out.println("Error: Invalid input format. Expected: grep \"pattern\" filename");
            return;
        }

        String pattern = input.substring(firstQuote + 1, lastQuote);
        String fileName = input.substring(lastQuote + 1).trim();

        Directory currentDirectory = fileSystem.getCurrentDir();
        Node target = currentDirectory.getChild(fileName);

        if (target == null){
            System.out.println("File does not exist");
        } 
        else if (target.isDirectory()){
            System.out.println("Error: Cannot search in a directory.");
        }
        else {
            // get content from file
            String text = ((File) target).getData();

            String result = KMP.searchKMP(pattern, text);

            if (!result.isEmpty()){
                System.out.println("Pattern \"" + pattern + "\" found in " + fileName + ".");
                System.out.println(result.trim());
            } else {
                System.out.println("Pattern \"" + pattern + "\" not found in " + fileName + ".");
            }
        }

    }

    // remove file / directory
    public static void rm(String[] tokens, Directory currentDirectory ){
        if (tokens.length < 2) {
            System.out.println("Error: Name argument missing.");
            return;
        }

        String name = tokens[1];

        Node target = currentDirectory.getChild(name);

        if (target == null){
            System.out.println("Error: '" + name + "' not found.");
            return;
        }

        if (target.isDirectory()){
            // check if directory empty
            if (((Directory) target).getAllChildren().isEmpty()){
                currentDirectory.removeChild(name);
            } else {
                System.out.println("Error: Cannot remove directory " + target.getName() + ". It is not empty.");
            }
        } else {
            // file so just remove
            currentDirectory.removeChild(name);
        }
    }

    // recursive remove directory
    public static void rmr(String[] tokens, Directory currentDirectory ){
        if (tokens.length < 3) {
            System.out.println("Error: Directory name argument missing.");
            return;
        }

        String name = tokens[2];

        Node target = currentDirectory.getChild(name);
        if (target == null){
            System.out.println("Does not exist");
            return;
        }
        recursiveRemove(target);
        currentDirectory.removeChild(name);
    }

    // Helper for recursive remove
    public static void recursiveRemove(Node node){

        // only empty if directory, if file no need
        if (node.isDirectory()){
            Directory dir = (Directory) node;

            ArrayList<Node> children = dir.getChildrenNodes();
            for (Node child : children){
                recursiveRemove(child);
            }
        }
    }

}
