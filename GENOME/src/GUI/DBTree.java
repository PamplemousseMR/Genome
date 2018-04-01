package GUI;

import Utils.Options;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.File;

public class DBTree extends JTree {

    private final DefaultTreeModel m_treeModel;

    protected DBTree() {
        super();
        final DefaultMutableTreeNode root = loadTree();
        final DefaultTreeModel model = new DefaultTreeModel(root);
        m_treeModel = model;
        super.setModel(model);
        super.setRootVisible(false);
        super.setShowsRootHandles(true);
        super.setEditable(true);

        addTreeSelectionListener(e -> {
            System.out.println("J'ai été selectionné " + e.getPath());
            System.out.println("fichier : " + getFileName(e.getPath()));
        });
    }

    protected synchronized void update(String _path) {
        String table[] = _path.split(Options.getSerializationSpliter());
        String temp;
        DefaultMutableTreeNode actual = (DefaultMutableTreeNode) m_treeModel.getRoot(), progressing;
        Boolean found;
        if (table.length >= 2) {
            if (!table[0].startsWith(Options.getDatabaseSerializationPrefix())) {
                return;
            }
            temp = table[0].substring(2, table[0].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
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
            if (!table[1].startsWith(Options.getKingdomSerializationPrefix())) {
                return;
            }
            temp = table[1].substring(2, table[1].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
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
            if (!table[2].startsWith(Options.getGroupSerializationPrefix())) {
                return;
            }
            temp = table[2].substring(2, table[2].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
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
            if (!table[3].startsWith(Options.getSubGroupSerializationPrefix())) {
                return;
            }
            temp = table[3].substring(3, table[3].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
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
            if (!table[4].startsWith(Options.getOrganismSerializationPrefix())) {
                return;
            }
            temp = table[4].substring(2, table[4].length()).replaceAll("_", " ");
            int nbChild = actual.getChildCount();
            progressing = actual.getNextNode();
            found = false;
            for (int i = 0; i < nbChild; ++i) {
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

    private DefaultMutableTreeNode loadTree() {
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

                    String table[] = f.getName().split(Options.getSerializationSpliter());
                    String temp;
                    if (table.length >= 2) {
                        if (!table[0].startsWith(Options.getDatabaseSerializationPrefix())) {
                            continue;
                        }
                        temp = table[0].substring(2, table[0].length()).replaceAll("_", " ");
                        if (!temp.equals(db.toString())) {
                            db = new DefaultMutableTreeNode(temp);
                            root.add(db);
                        }
                    }
                    if (table.length >= 3) {
                        if (!table[1].startsWith(Options.getKingdomSerializationPrefix())) {
                            continue;
                        }
                        temp = table[1].substring(2, table[1].length()).replaceAll("_", " ");
                        if (!temp.equals(kg.toString())) {
                            kg = new DefaultMutableTreeNode(temp);
                            db.add(kg);
                        }
                    }
                    if (table.length >= 4) {
                        if (!table[2].startsWith(Options.getGroupSerializationPrefix())) {
                            continue;
                        }
                        temp = table[2].substring(2, table[2].length()).replaceAll("_", " ");
                        if (!temp.equals(gp.toString())) {
                            gp = new DefaultMutableTreeNode(temp);
                            kg.add(gp);
                        }
                    }
                    if (table.length >= 5) {
                        if (!table[3].startsWith(Options.getSubGroupSerializationPrefix())) {
                            continue;
                        }
                        temp = table[3].substring(3, table[3].length()).replaceAll("_", " ");
                        if (!temp.equals(su.toString())) {
                            su = new DefaultMutableTreeNode(temp);
                            gp.add(su);
                        }
                    }
                    if (table.length >= 6) {
                        if (!table[4].startsWith(Options.getOrganismSerializationPrefix())) {
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

    private String getFileName(TreePath _path) {
        String result = "";
        Object[] table = _path.getPath();
        if (table.length >= 2)
            result += Options.getDatabaseSerializationPrefix() + table[1].toString().replaceAll(" ", "_");
        if (table.length >= 3)
            result += Options.getSerializationSpliter() + Options.getKingdomSerializationPrefix() + table[2].toString().replaceAll(" ", "_");
        if (table.length >= 4)
            result += Options.getSerializationSpliter() + Options.getGroupSerializationPrefix() + table[3].toString().replaceAll(" ", "_");
        if (table.length >= 5)
            result += Options.getSerializationSpliter() + Options.getSubGroupSerializationPrefix() + table[4].toString().replaceAll(" ", "_");
        if (table.length >= 6)
            result += Options.getSerializationSpliter() + Options.getOrganismSerializationPrefix() + table[5].toString().replaceAll(" ", "_");
        return Options.getSerializeDirectory() + File.separator + result + Options.getSerializeExtension();
    }
}
