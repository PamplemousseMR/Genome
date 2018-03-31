package GUI;

import Data.DataBase;
import Utils.Options;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.File;

public class DBTree extends JTree {

    private static DBTree s_dbTree;

    private DBTree(){
        super();
    }
    private DBTree(TreeNode _root) {
        super(_root);
    }

    public static DBTree getTree(){
        if (s_dbTree == null){
            loadTree();
        }
        return s_dbTree;
    }

    private static void loadTree(){
        File path = new File(Options.getSerializeDirectory());
        if (!path.exists()) {
            return;
        }
        File[] files = path.listFiles();
        if (files == null) {
            return;
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode db = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode kg = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode gp = new DefaultMutableTreeNode("");
        DefaultMutableTreeNode su = new DefaultMutableTreeNode("");
        for (File f : files) {
            if(f.getName().endsWith(Options.getDateModifSerializeExtension()))
                continue;

            String table[] = f.getName().split("--");
            String temp;
            if(table.length >= 2){
                if(!table[0].startsWith("D_"))
                    continue;
                temp = table[0].substring(2, table[0].length()).replaceAll("_"," ");
                if(!temp.equals(db.toString())){
                    db = new DefaultMutableTreeNode(temp);
                    root.add(db);
                }
            }
            if(table.length >= 3){
                if(!table[1].startsWith("K_"))
                    continue;
                temp = table[1].substring(2, table[1].length()).replaceAll("_"," ");
                if(!temp.equals(kg.toString())){
                    kg = new DefaultMutableTreeNode(temp);
                    db.add(kg);
                }
            }
            if(table.length >= 4){
                if(!table[2].startsWith("G_"))
                    continue;
                temp = table[2].substring(2, table[2].length()).replaceAll("_"," ");
                if(!temp.equals(gp.toString())){
                    gp = new DefaultMutableTreeNode(temp);
                    kg.add(gp);
                }
            }
            if(table.length >= 5){
                if(!table[3].startsWith("SG_"))
                    continue;
                temp = table[3].substring(3, table[3].length()).replaceAll("_"," ");
                if(!temp.equals(su.toString())){
                    su = new DefaultMutableTreeNode(temp);
                    gp.add(su);
                }
            }
            if(table.length >= 6){
                if(!table[4].startsWith("O_"))
                    continue;
                temp = table[4].substring(2, table[4].length()).replaceAll("_"," ");
                su.add(new DefaultMutableTreeNode(temp));
            }
        }
        s_dbTree = new DBTree(root);
    }

    public void update(DataBase _data){
        return;
    }
}
