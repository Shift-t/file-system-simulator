import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //initialising tree
        Tree fileSystem = new Tree();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the File Management System");

        // main loop

        boolean exit = false;
        while (!exit) {

            if (System.console() != null) {
                System.out.print("/" + Commands.pwd(fileSystem.getCurrentDir()).substring(1) + "$ ");
            }
            String command = scanner.nextLine().trim();
            if (command.isEmpty()) {
                continue;
            }
            String[] tokens = command.split(" +");

            switch (tokens[0]) {

                // add -P
                case "mkdir" : Commands.mkdir(tokens, fileSystem); break;

                case "touch" : Commands.touch(tokens, fileSystem); break;

                case "echo" : Commands.echo(command,  fileSystem); break;

                case "ls" : Commands.ls(fileSystem.getCurrentDir()); break;

                case "cd" : Commands.cd(tokens, fileSystem); break;

                case "pwd" : System.out.println(Commands.pwd(fileSystem.getCurrentDir())); break;

                // check for -r
                case "rm" :
                if (tokens.length < 2) {
                    System.out.println("Error: Missing file or directory name.");
                } else if (tokens[1].equals("-r")) {
                    Commands.rmr(tokens, fileSystem.getCurrentDir());
                } else {
                    Commands.rm(tokens, fileSystem.getCurrentDir());
                } break;

                case "tree" :  Commands.tree(fileSystem.getCurrentDir()); break;

                case "du" :  Commands.du(fileSystem.getCurrentDir()); break;

                case "grep" : Commands.grep(command, fileSystem); break;

                case "end" : exit = true; break;

                default : System.out.println("Invalid command, Try again");
            }
        }
    }
}
