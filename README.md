# Terminal-Based File System Simulator

A Java command-line simulation of a Unix-like in-memory file system. This project demonstrates the practical application of fundamental data structures and algorithms, including an N-ary Tree, a custom Hash Table with separate chaining using Singly Linked Lists, DFS traversals, and the Knuth-Morris-Pratt (KMP) pattern search algorithm.

---

## Compilation and Execution

To compile and run the simulator from a terminal environment:

1.  **Compile all source files:**
    ```bash
    javac *.java
    ```

2.  **Run the main program:**
    ```bash
    java Main
    ```

---

## Supported Commands

The simulator supports the following terminal operations:

| Command | Syntax | Description | Example |
| :--- | :--- | :--- | :--- |
| `mkdir` | `mkdir <dir_name>` | Creates a new directory in the current directory. | `mkdir documents` |
| `mkdir -p` | `mkdir -p <path>` | Creates a nested path of directories, creating any missing parent directories along the way. | `mkdir -p photos/vacation/2026` |
| `touch` | `touch <file_name> <size>` | Creates an empty file of the specified size (in bytes). *Note: Standard Unix touch modifies timestamps; in this simulator, touch allocates a file of a specific size.* | `touch notes.txt 512` |
| `ls` | `ls` | Lists all files and subdirectories (appending `/` to directories and `(size B)` to files) in the current directory. | `ls` |
| `cd` | `cd <path>` | Changes the current working directory to the specified relative or absolute path. Supports the `..` shortcut to navigate to the parent directory. | `cd photos/vacation` |
| `pwd` | `pwd` | Prints the absolute path of the current working directory. | `pwd` |
| `echo` | `echo "<text>" > <file>` | Writes the specified text to the file, completely replacing any existing data. The file size is truncated and updated to match the length of the new text in bytes. | `echo "Hello World" > hello.txt` |
| `grep` | `grep "<pattern>" <file>` | Searches for a pattern inside a file using the KMP algorithm and reports matching indices. | `grep "Hello" hello.txt` |
| `rm` | `rm <name>` | Deletes a file or an empty directory. | `rm notes.txt` |
| `rm -r` | `rm -r <dir>` | Recursively deletes a directory and all of its contents. | `rm -r photos` |
| `tree` | `tree` | Displays the hierarchical structure of the current directory in a visual tree format. | `tree` |
| `du` | `du` | Computes and displays the aggregate size of the current directory and all its nested sub-contents. | `du` |
| `end` | `end` | Exits the simulator program. | `end` |

---

## Technical Architecture

The simulator models directories and files as nodes in a hierarchical tree. To optimize lookup, insertion, and deletion times within directories, children are stored in a custom separate-chaining Hash Table rather than sequential arrays.

### Class Hierarchy and Relationships

The system utilizes an object-oriented class hierarchy derived from an abstract base class:
*   **`Node` (Abstract Base Class):** Defines common attributes such as `name` and `parent` pointers. Extended by `Directory` and `File`.
*   **`Directory` (Subclass of `Node`):** Represents directory nodes. Each directory contains a `HashTable` referencing its child elements.
*   **`File` (Subclass of `Node`):** Represents file nodes (leaf nodes) which store data strings and track file size.
*   **`HashTable`:** A custom separate-chaining hash table with a default array capacity of 10. Used inside `Directory` objects to associate file/folder names (keys) with their corresponding `Node` instances (values).
*   **`SLL` (Singly Linked List) & `SLLNode`:** Used at each hash bucket index to handle collisions via separate chaining.
*   **`Entry`:** Key-value structure stored inside `SLLNode` objects, wrapping the lookup key (string name) and node reference.

### Directory Storage Mechanism

Instead of using linear arrays, each directory node references a custom `HashTable` containing an array of Singly Linked Lists (SLL) as hash buckets.
1.  **Hashing:** When a child node is added or searched, its string name is hashed, and the modulo of the capacity (10) determines the target bucket index.
2.  **Separate Chaining:** If multiple child names hash to the same index (a collision), they are appended to a singly linked list at that index.
3.  **Entry Storage:** Each list node (`SLLNode`) stores an `Entry` containing the name of the child and its reference. The reference points to either a `File` or `Directory` instance.

---

## Data Structures and Algorithms Used

### 1. General Tree (N-ary Tree)
*   **Implementation:** Managed by the [Tree.java](Tree.java) class. The base class is [Node.java](Node.java), an abstract structure extended by [Directory.java](Directory.java) and [File.java](File.java).
*   **Usage:** Forms the skeleton of the hierarchical in-memory file system. Since a directory can have an arbitrary number of children, a general tree is preferred over binary trees.

### 2. Hash Table with Separate Chaining
*   **Implementation:** Defined in [HashTable.java](HashTable.java). It initializes with a default bucket array capacity of 10.
*   **Usage:** Used inside directories to keep lookups, additions, and removals efficient ($O(1)$ average complexity) compared to scanning a linear list of files.
*   **Performance Characteristic:** Because the custom `HashTable` implementation has a fixed capacity of 10 and does not implement dynamic load-factor resizing, lookup times will degrade to worst-case $O(N)$ linear search if a directory contains a very large number of files.

### 3. Singly Linked List (SLL)
*   **Implementation:** Contained in [SLL.java](SLL.java) and [SLLNode.java](SLLNode.java).
*   **Usage:** Acts as the bucket chain in the `HashTable` implementation to resolve key collisions.

### 4. Stack
*   **Implementation:** Utilizes Java's standard `java.util.Stack`.
*   **Usage:** Located in [Commands.java](Commands.java) inside the `pwd` command. It traverses from the current directory node upward via parent pointers to the root node, pushing folder names onto the stack. Popping them in reverse order constructs the correct absolute directory path string.
*   **Architectural Consideration:** The project uses the legacy synchronized `java.util.Stack` class. In modern production Java environments, the `java.util.Deque` interface (e.g., `java.util.ArrayDeque`) is preferred for stack operations as it is unsynchronized and has lower overhead.

### 5. Depth-First Search (DFS) Tree Traversal
*   **Implementation:** Implemented recursively.
*   **Usage:** 
    *   `tree`: Recursively logs all directory descendants with appropriate ASCII connector symbols.
    *   `du`: Traverses the directory subtree recursively, summing up individual file sizes to report total folder disk usage.

### 6. Knuth-Morris-Pratt (KMP) String Matcher
*   **Implementation:** Implemented in [KMP.java](KMP.java).
*   **Usage:** Used by the `grep` command to search for string patterns inside a file's data string. It constructs a Longest Prefix Suffix (LPS) array to prevent redundant character matching, offering $O(M + N)$ search time complexity.

---

## Project Structure

*   [Main.java](Main.java): Entry point containing the program loop and CLI parser.
*   [Commands.java](Commands.java): Method implementations for the supported console commands.
*   [Tree.java](Tree.java): N-ary tree data structure representing the file system.
*   [Node.java](Node.java): Abstract base class for directories and files.
*   [Directory.java](Directory.java): Directory subclass extending Node, storing contents in a HashTable.
*   [File.java](File.java): File subclass extending Node, storing data strings and size bounds.
*   [HashTable.java](HashTable.java): Separate-chaining hash table implementation.
*   [Entry.java](Entry.java): Hash table entry representation (Key-Value pair).
*   [SLL.java](SLL.java): Custom generic Singly Linked List.
*   [SLLNode.java](SLLNode.java): Node structure for the Singly Linked List.
*   [KMP.java](KMP.java): Knuth-Morris-Pratt string matching utility.
