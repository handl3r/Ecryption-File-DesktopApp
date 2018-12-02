package com.Sec;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;

public class FileTreeModel implements TreeModel {
    protected File root;

    public FileTreeModel(File root) {
        this.root = root;
    }

    // Get root of JTree
    public Object getRoot() {
        return root;
    }

    // Tell JTree whether an object in the tree is a leaf or not
    public boolean isLeaf(Object node) {
        return ((File) node).isFile();
    }

    // Tell JTree how many children a node has
    public int getChildCount(Object parent) {
        String[] children = ((File) parent).list();
        if (children == null) return 0;
        return children.length;
    }

    // Fetch any numbered child of a node for the JTree.
    // Our model returns File objects for all nodes in the tree.  The
    // JTree displays these by calling the File.toString() method.
    public Object getChild(Object parent, int index) {
        String[] children = ((File) parent).list();
        if ((children == null) || (index >= children.length)) return null;
        return new File((File) parent, children[index]);
    }

    // Figure out a child's position in its parent node.
    public int getIndexOfChild(Object parent, Object child) {
        String[] children = ((File) parent).list();
        String childname = ((File) child).getName();
        if (children == null) return -1;
        for (int i = 0; i < children.length; i++) {
            if (childname.equals(children[i])) return i;
        }
        return -1;
    }

    // This method is only invoked by the JTree for editable trees.
    public void valueForPathChanged(TreePath path, Object newvalue) {

    }

    // Since this is not an editable tree model, we never fire any events,
    public void addTreeModelListener(TreeModelListener l) {
    }

    public void removeTreeModelListener(TreeModelListener l) {
    }
}
