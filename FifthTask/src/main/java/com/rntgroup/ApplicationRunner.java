package com.rntgroup;

import com.rntgroup.entities.Tree;
import com.rntgroup.entities.interfaces.Processable;
import com.rntgroup.util.DataProcessor;
import com.rntgroup.util.TreeTraverser;

import java.util.ArrayList;
import java.util.List;

public class ApplicationRunner {

    public static void main(String[] args) {

        int numLeaves = 5;
        int maxBranchesPerLeaf = 3;
        int maxLeafValue = 10;
        int maxBranchWeight = 10;
        Tree tree = Tree.generateRandomTree(numLeaves, maxBranchesPerLeaf, maxLeafValue, maxBranchWeight);

        tree.print(); // внутри используется BFS алгоритм для обхода всего дерева

        System.out.println("\nBFS:");
        List<Processable> treeNodesByBfs = new ArrayList<>();
        TreeTraverser.breadthFirstSearch(tree.getRoot(), node -> treeNodesByBfs.add((Processable) node));
        DataProcessor.processAll(treeNodesByBfs);

        System.out.println("\nDFS:");
        List<Processable> treeNodesByDfs = new ArrayList<>();
        TreeTraverser.depthFirstSearch(tree.getRoot(), node -> treeNodesByDfs.add((Processable) node));
        DataProcessor.processAll(treeNodesByDfs);

    }
}
