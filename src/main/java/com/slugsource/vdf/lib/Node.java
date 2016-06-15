package com.slugsource.vdf.lib;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A node object that can hold either a value, or child node objects
 *
 * @author Nathan Fearnley
 */
public class Node {

    private String name;
    private String value = null;
    private LinkedList<Node> children = new LinkedList<>();

    public List<Node> getChildren() {
        return children;
    }

    /**
     * Constructor that sets the name of the node.
     *
     * @param name Name of the node. Cannot be null.
     */
    public Node(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        this.name = name;
    }

    /**
     * Constructor that sets the name and value of the node.
     *
     * @param name  Name of the node. Cannot be null.
     * @param value Value of the node. Cannot be null.
     */
    public Node(String name, String value) {
        this(name);
        if (value == null) {
            throw new NullPointerException("Value cannot be null.");
        }
        this.value = value;
    }

    /**
     * Get the name of the node.
     *
     * @return Name of the node.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the value of the node.
     *
     * @return Value of the node. Null if value is not set.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value for this node and deletes all child nodes.
     *
     * @param value Value to set node to. Cannot be null.
     */
    public void setValue(String value) {
        if (value == null) {
            throw new NullPointerException("Value cannot be null.");
        }
        this.value = value;
        children = new LinkedList<>();
    }

    /**
     * Lookup a child node.
     *
     * @param node Child node to lookup. Cannot be null.
     * @return Node or null if no node is found.
     */
    public Node getNode(Node node) {
        return getNode(node.getName());
    }

    /**
     * Lookup a child node by name.
     *
     * @param name Name of child node. Cannot be null.
     * @return Node or null if no node is found.
     */
    public Node getNode(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        int index = children.indexOf(new Node(name));

        if (index == -1) {
            return null;
        }
        return children.get(index);
    }

    /**
     * Add a child node to this node. Will delete node value.
     *
     * @param newNode Child node to add. Cannot be null.
     * @return True on success, False on failure
     */
    public boolean addNode(Node newNode) {
        if (newNode == null) {
            throw new NullPointerException("Node cannot be null.");
        }

        boolean result = false;
        // Check to see if child node exists
        Node oldNode = getNode(newNode.name);
        if (oldNode == null) {
            children.add(newNode);
            result = true;
        }
        return result;
    }

    /**
     * Add a child node to this node. Will delete node value.
     *
     * @param name Name of child node to add. Cannot be null.
     * @return True on success, False on failure
     */
    public boolean addNode(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        return addNode(new Node(name));

    }

    /**
     * Delete a child node from this node.
     *
     * @param node Child node to delete.
     * @return True on success, False on failure
     */
    public boolean delNode(Node node) {
        if (node == null) {
            throw new NullPointerException("Node cannot be null.");
        }
        return children.remove(node);
    }

    /**
     * Delete a child node from this node.
     *
     * @param name Name of child node to delete
     * @return True on success, False on failure
     */
    public boolean delNode(String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        return delNode(new Node(name));
    }

    /**
     * Get the value of a particular sub-node.
     *
     * @param path Array of node path that sub-node is contained in. If null, an
     *             empty array is assumed.
     * @param name Name of sub-node to get. Cannot be null.
     * @return Value of node, null if node does not exist.
     */
    public String getValue(String[] path, String name) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        if (path == null) {
            path = new String[0];
        }

        Node node = getNode(path, name);

        if (node != null) {
            return node.getValue();
        } else {
            return null;
        }
    }

    /**
     * Set the value of a particular sub-node.
     *
     * @param path  Array of node path that sub-node is contained in. If null, an
     *              empty array is assumed.
     * @param name  Name of sub-node to set. Cannot be null.
     * @param value Value to set sub-node to. Cannot be null.
     */
    public void setValue(String[] path, String name, String value) {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        if (value == null) {
            throw new NullPointerException("Value cannot be null.");
        }
        if (path == null) {
            path = new String[0];
        }

        Node newNode = new Node(name, value);
        // Try adding the node
        if (!addNode(path, newNode)) {
            // If failed, get the current node and set the value
            Node node = getNode(path, name);
            node.setValue(value);
        }
    }

    /**
     * Get a particular sub-node
     *
     * @param path Array of node path that sub-node is contained in. If null, an
     *             empty array is assumed.
     * @param name Name of sub-node to get
     * @return Sub-node or null if node cannot be found.
     */
    public Node getNode(String[] path, String name) {
        if (path == null) {
            path = new String[0];
        }
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }

        // Loop through nodes in the path
        Node node = this;

        for (String nodeName : path) {
            node = node.getNode(nodeName);
            // if node is null, then child node cannot be found
            if (node == null) {
                break;
            }
        }
        if (node != null) {
            node = node.getNode(name);
        }
        return node;

    }

    /**
     * Add a child node to this node and deletes this node's value.
     *
     * @param path    Array of node path that sub-node is contained in. If null, an
     *                empty array is assumed.
     * @param newNode Child node to add.
     * @return True on success, False on failure
     */
    public boolean addNode(String[] path, Node newNode) {
        if (newNode == null) {
            throw new NullPointerException("Node cannot be null.");
        }

        // Assume null path is empty
        if (path == null) {
            path = new String[0];
        }

        // Loop over the nodes in the path, creating nodes if necessary
        Node node = this;

        for (String nodeName : path) {
            Node childNode = node.getNode(nodeName);
            if (childNode == null) {
                childNode = new Node(nodeName);
                node.addNode(childNode);
            }
            node = childNode;

        }

        return node.addNode(newNode);
    }

    /**
     * Delete a sub-node from this node.
     *
     * @param path Array of node path that sub-node is contained in. If null, an
     *             empty array is assumed.
     * @param name Name of sub-node to delete.
     * @return True on success, False on failure
     */
    public boolean delNode(String[] path, String name) {
        // Check arguments for nulls
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
        if (path == null) {
            path = new String[0];
        }

        boolean result = false;
        Node node = getNode(name);
        if (node != null) {
            result = node.delNode(name);
        }
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Read a node from a configuration file.
     *
     * @param file Configuration file to read from
     * @return Node that was read.
     * @throws InvalidFileException  Is thrown when the file is improperly
     *                               formatted.
     * @throws FileNotFoundException Is thrown when the file does not exist.
     * @throws IOException           Is thrown when there is a file system problem.
     */
    public static Node readFromFile(File file) throws InvalidFileException, IOException {
        // Check arguments for null
        if (file == null) {
            throw new NullPointerException("File cannot be null.");
        }

        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             Reader r = new BufferedReader(isr)) {
            return Node.parse(r);
        }
    }

    /**
     * Write a node to a configuration file. Overwrites existing file.
     *
     * @param file File to write to.
     * @param node Node to save.
     * @throws IOException Is thrown when there is a file system problem.
     */
    public static void writeToFile(File file, Node node) throws IOException {
        if (file == null) {
            throw new NullPointerException("File cannot be null.");
        }
        if (node == null) {
            throw new NullPointerException("Node cannot be null.");
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos);
             BufferedWriter w = new BufferedWriter(osw)){
            // Write the node to a file
            w.write(node.toVdf());
        }
    }

    /**
     * Write this node to a configuration file. Overwrites existing file.
     *
     * @param file File to write to
     * @throws IOException Is thrown when there is a file system problem.
     */
    public void writeToFile(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File cannot be null.");
        }
        writeToFile(file, this);
    }

    /**
     * Read a configuration file from a reader and return the node.
     *
     * @param reader The reader to read from
     * @return The node read
     * @throws InvalidFileException Is thrown when the file is improperly
     *                              formatted.
     * @throws IOException          Is thrown when there is a file system problem.
     */
    public static Node parse(Reader reader) throws InvalidFileException, IOException {
        if (reader == null) {
            throw new NullPointerException("Reader cannot be null.");
        }
        StreamTokenizer parser = getParser(reader);
        return parse(parser);
    }

    /**
     * Read a configuration file using provided parser and return the node.
     *
     * @param parser The parser used to read
     * @return The node read
     * @throws InvalidFileException Is thrown when the file is improperly
     *                              formatted.
     * @throws IOException          Is thrown when there is a file system problem.
     */
    private static Node parse(StreamTokenizer parser) throws InvalidFileException, IOException {
        if (parser == null) {
            throw new NullPointerException("Parser cannot be null.");
        }

        Node node = null;
        // Read node name
        parser.nextToken();
        String name = parser.sval;
        if (name == null) {
            throw new InvalidFileException("This is not a vdf file.");
        }

        parser.nextToken();
        // Check if next token is value or open of branch
        if (parser.sval != null) {
            // If token is value, read value
            String value = parser.sval;
            node = new Node(name, value);

        } else if (parser.ttype == '{') {
            // If token is open branch, read child nodes
            Node branchNode = new Node(name);
            while (parser.nextToken() != '}') {
                parser.pushBack();
                if (!branchNode.addNode(Node.parse(parser))) {
                    throw new InvalidFileException("Could not read node from file.");
                }
            }
            node = branchNode;
        }

        assert node != null;
        return node;
    }

    /**
     * Build a StreamTokenizer for parsing vdf files.
     *
     * @param reader A reader to read from.
     * @return The StreamTokenizer to parse the given reader file.
     */
    private static StreamTokenizer getParser(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null.");
        }

        StreamTokenizer parser = new StreamTokenizer(reader);
        parser.resetSyntax();
        parser.eolIsSignificant(false);
        parser.lowerCaseMode(false);
        parser.slashSlashComments(true);
        parser.slashStarComments(false);
        parser.commentChar('/');
        parser.quoteChar('"');
        parser.whitespaceChars('\u0000', '\u0020');
        parser.wordChars('A', 'Z');
        parser.wordChars('a', 'z');
        parser.wordChars('\u00A0', '\u00FF');
        return parser;
    }

    /**
     * Returns a string representing this node and subnodes in vdf format.
     *
     * @return String in vdf format.
     */
    public String toVdf() {
        return toVdf(0);
    }

    /**
     * Returns a string representing this node and subnodes in vdf format.
     *
     * @param level The level of indentation to return
     * @return String in vdf format.
     */
    public String toVdf(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Level cannot be less than zero.");
        }

        String output;
        // Check if this is a value node or branch node
        if (value != null) {
            // Build the string for value nodes
            String name = '"' + StringEscapeUtils.escapeJava(this.getName()) + '"';
            String value = '"' + StringEscapeUtils.escapeJava(this.getValue()) + '"';
            output = StringUtils.repeat('\t', level) + name + "\t\t" + value + '\n';
        } else {
            // Build the string for branch nodes
            String name = '"' + StringEscapeUtils.escapeJava(getName()) + '"';

            StringBuilder childrenOutput = new StringBuilder();
            for (Node child : children) {
                childrenOutput.append(child.toVdf(level + 1));
            }

            output = StringUtils.repeat('\t', level) + name + '\n';
            output += StringUtils.repeat('\t', level) + "{\n";
            output += childrenOutput;
            output += StringUtils.repeat('\t', level) + "}\n";
        }
        return output;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", children=" + children +
                '}';
    }
}