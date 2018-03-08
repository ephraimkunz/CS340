package visitorpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ephraimkunz on 3/5/18.
 */

public class DirectoryStructure {
    private DirectoryStructureNode root;

    public int getSize() {
        SizeVisitor visitor = new SizeVisitor();
        visitor.run(root);
        return visitor.getSize();
    }

    public String[] getFileNames() {
        FileNameVisitor visitor = new FileNameVisitor();
        visitor.run(root);
        return visitor.getFileNames();
    }
}

abstract class DirectoryStructureNode {
    abstract void accept(NodeVisitor visitor);
}

class DirectoryNode extends DirectoryStructureNode {
    @Override
    void accept(NodeVisitor visitor) {
        visitor.visitDirectoryNode(this);
        for (DirectoryStructureNode node: getFileNodes()) {
            node.accept(visitor);
        }

        for (DirectoryStructureNode node: getDirectoryNodes()) {
            node.accept(visitor);
        }
    }

    public FileNode[] getFileNodes(){}
    public DirectoryNode[] getDirectoryNodes(){}
}

class FileNode extends DirectoryStructureNode {
    @Override
    void accept(NodeVisitor visitor) {
        visitor.visitFileNode(this);
    }

    public String getName() {}
    public int getSize(){}

}


////////////// Visitors ///////////////

interface NodeVisitor {
    void visitFileNode(FileNode node);
    void visitDirectoryNode(DirectoryNode node);
}

class SizeVisitor implements NodeVisitor {
    private int size = 0;

    @Override
    public void visitFileNode(FileNode node) {
        size += node.getSize();
    }

    @Override
    public void visitDirectoryNode(DirectoryNode node) {
        // Nothing to do here.
    }

    public int getSize() {
        return size;
    }

    public void run(DirectoryStructureNode start) {
        start.accept(this);
    }
}

class FileNameVisitor implements NodeVisitor {
    private List<String> filenames = new ArrayList<String>();

    @Override
    public void visitFileNode(FileNode node) {
       filenames.add(node.getName());
    }

    @Override
    public void visitDirectoryNode(DirectoryNode node) {
        // Nothing to do here.
    }

    public String[] getFileNames() {
        return filenames.toArray(new String[filenames.size()]);
    }

    public void run(DirectoryStructureNode start) {
        start.accept(this);
    }
}