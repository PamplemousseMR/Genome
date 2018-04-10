package GUI;

import Utils.Options;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

import static GUI.Constant.*;

public class TreePanel extends IPanel {

    private static final String s_TITLE = "Arborescence des fichiers";

    private DefaultTreeModel m_treeModel;
    private JTree m_tree;
    private ScrollComponent m_scrollPane;

    protected TreePanel() {
        super(s_TITLE);
        m_tree.addTreeSelectionListener(e -> {
            System.out.println("J'ai été selectionné " + e.getPath());
            System.out.println("fichier : " + getFileName(e.getPath()));
        });
    }

    protected void createComponent() {
        m_tree = new JTree();
        m_scrollPane = new ScrollComponent(m_tree);
    }

    protected void initLayout() {

    }

    protected void addComponents() {
        super.add(m_scrollPane);
    }

    protected void swagComponent() {
        final DefaultMutableTreeNode root = loadTree();
        m_treeModel = new DefaultTreeModel(root);

        m_tree.setModel(m_treeModel);
        m_tree.setRootVisible(false);
        m_tree.setShowsRootHandles(true);
        m_tree.setEditable(true);
        m_tree.setBackground(s_LIGHTGRAY);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) m_tree.getCellRenderer();
        renderer.setTextSelectionColor(s_WHITE);
        renderer.setBackgroundSelectionColor(s_BLUEGRAY);
        renderer.setBackgroundNonSelectionColor(s_LIGHTGRAY);
        renderer.setTextNonSelectionColor(s_WHITE);
        renderer.setBorderSelectionColor(s_BLACK);
        renderer.setLeafIcon(null);

        m_scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
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
                Arrays.sort(files);
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
