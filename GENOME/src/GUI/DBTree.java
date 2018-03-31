package GUI;

import Utils.Options;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;

public class DBTree extends JTree {

    private static DBTree s_dbTree;
    private DefaultMutableTreeNode m_rootNode;
    private DefaultTreeModel m_treeModel;

    private DBTree() {
        super();
    }

    public static DBTree getTree() {
        if (s_dbTree == null) {
            createTree();
        }
        return s_dbTree;
    }

    private static void createTree() {
        DefaultMutableTreeNode root = loadTree();
        DefaultTreeModel model = new DefaultTreeModel(root);
        model.addTreeModelListener(new TreeModelListener() {
            @Override
            public void treeNodesChanged(TreeModelEvent e) {
                //System.out.println("CHANGED");
            }

            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                //System.out.println("INSERTED");
            }

            @Override
            public void treeNodesRemoved(TreeModelEvent e) {
                //System.out.println("REMOVED");
            }

            @Override
            public void treeStructureChanged(TreeModelEvent e) {
                //System.out.println("STRUCTURE_CHANGED");
            }
        });
        s_dbTree = new DBTree();
        s_dbTree.m_rootNode = root;
        s_dbTree.m_treeModel = model;
        s_dbTree.setModel(model);
        s_dbTree.setRootVisible(false);
        s_dbTree.setShowsRootHandles(true);
        s_dbTree.setEditable(true);

        s_dbTree.addTreeSelectionListener(e -> {
            System.out.println("J'ai été selectionné " + e.getPath());
            System.out.println("fichier : " + getFileName(e.getPath()));
        });
    }

    private static DefaultMutableTreeNode loadTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        File path = new File(Options.getSerializeDirectory());
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                DefaultMutableTreeNode db = new DefaultMutableTreeNode("");
                DefaultMutableTreeNode kg = new DefaultMutableTreeNode("");
                DefaultMutableTreeNode gp = new DefaultMutableTreeNode("");
                DefaultMutableTreeNode su = new DefaultMutableTreeNode("");
                for (File f : files) {
                    if (f.getName().endsWith(Options.getDateModifSerializeExtension())) {
                        continue;
                    }

                    String table[] = f.getName().split("--");
                    String temp;
                    if (table.length >= 2) {
                        if (!table[0].startsWith("D_")) {
                            continue;
                        }
                        temp = table[0].substring(2, table[0].length()).replaceAll("_", " ");
                        if (!temp.equals(db.toString())) {
                            db = new DefaultMutableTreeNode(temp);
                            root.add(db);
                        }
                    }
                    if (table.length >= 3) {
                        if (!table[1].startsWith("K_")) {
                            continue;
                        }
                        temp = table[1].substring(2, table[1].length()).replaceAll("_", " ");
                        if (!temp.equals(kg.toString())) {
                            kg = new DefaultMutableTreeNode(temp);
                            db.add(kg);
                        }
                    }
                    if (table.length >= 4) {
                        if (!table[2].startsWith("G_")) {
                            continue;
                        }
                        temp = table[2].substring(2, table[2].length()).replaceAll("_", " ");
                        if (!temp.equals(gp.toString())) {
                            gp = new DefaultMutableTreeNode(temp);
                            kg.add(gp);
                        }
                    }
                    if (table.length >= 5) {
                        if (!table[3].startsWith("SG_")) {
                            continue;
                        }
                        temp = table[3].substring(3, table[3].length()).replaceAll("_", " ");
                        if (!temp.equals(su.toString())) {
                            su = new DefaultMutableTreeNode(temp);
                            gp.add(su);
                        }
                    }
                    if (table.length >= 6) {
                        if (!table[4].startsWith("O_")) {
                            continue;
                        }
                        temp = table[4].substring(2, table[4].length()).replaceAll("_", " ");
                        su.add(new DefaultMutableTreeNode(temp));
                    }
                }
            }
        }
        return root;

    }

    private static String getFileName(TreePath _path) {
        String result = "";
        Object[] table = _path.getPath();
        if (table.length >= 2)
            result += "D_" + table[1].toString().replaceAll(" ", "_");
        if (table.length >= 3)
            result += "--K_" + table[2].toString().replaceAll(" ", "_");
        if (table.length >= 4)
            result += "--G_" + table[3].toString().replaceAll(" ", "_");
        if (table.length >= 5)
            result += "--SG_" + table[4].toString().replaceAll(" ", "_");
        if (table.length >= 6)
            result += "--O_" + table[5].toString().replaceAll(" ", "_");
        return Options.getSerializeDirectory() + File.separator + result + Options.getSerializeExtension();
    }

    synchronized public void update(String _string) {
        String table[] = _string.split("--");
        String temp;
        DefaultMutableTreeNode actual = (DefaultMutableTreeNode) m_treeModel.getRoot(), progressing;
        Boolean found;
        if (table.length >= 2) {
            if (!table[0].startsWith("D_")) {
                return;
            }
            temp = table[0].substring(2, table[0].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; i++) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(temp);
                actual.add(progressing);
                m_treeModel.reload();
            }
            actual = progressing;
        }
        if (table.length >= 3) {
            if (!table[1].startsWith("K_")) {
                return;
            }
            temp = table[1].substring(2, table[1].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; i++) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(temp);
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            }
            actual = progressing;
        }
        if (table.length >= 4) {
            if (!table[2].startsWith("G_")) {
                return;
            }
            temp = table[2].substring(2, table[2].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; i++) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(temp);
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            }
            actual = progressing;
        }
        if (table.length >= 5) {
            if (!table[3].startsWith("SG_")) {
                return;
            }
            temp = table[3].substring(3, table[3].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; i++) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(temp);
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            }
            actual = progressing;
        }
        if (table.length >= 6) {
            if (!table[4].startsWith("O_")) {
                return;
            }
            temp = table[4].substring(2, table[4].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; i++) {
                if (progressing.toString().equals(temp)) {
                    found = true;
                    break;
                }
                progressing = progressing.getNextSibling();
            }
            if (!found) {
                progressing = new DefaultMutableTreeNode(temp);
                actual.add(progressing);
                m_treeModel.insertNodeInto(progressing, actual, actual.getChildCount() - 1);
                m_treeModel.nodeChanged(actual);
            }
        }
    }

}
