package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.Node;
import com.rntgroup.util.TreeTraverser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Tree {
    private final Leaf root;

    public Tree(int rootValue) {
        this.root = new Leaf(rootValue);
    }

    public static Tree generateRandomTree(int numLeaves, int maxBranchesPerLeaf, int maxValue, int maxWeight) {
        Random random = new Random();

        Tree tree = new Tree(random.nextInt(maxValue) + 1);
        List<Leaf> leavesOnCurrentLevel = new ArrayList<>();
        leavesOnCurrentLevel.add(tree.getRoot());

        List<Leaf> remainingLeaves = new ArrayList<>();
        for (int i = 1; i < numLeaves; i++) {
            remainingLeaves.add(new Leaf(random.nextInt(maxValue) + 1));
        }

        while (!remainingLeaves.isEmpty()) {
            List<Leaf> leavesOnNextLevel = new ArrayList<>();

            for (Leaf leaf : leavesOnCurrentLevel) {
                int numBranches = random.nextInt(maxBranchesPerLeaf) + 1;

                for (int i = 0; i < numBranches; i++) {
                    if (!remainingLeaves.isEmpty()) {
                        int randomIndex = random.nextInt(remainingLeaves.size());
                        Leaf newLeaf = remainingLeaves.get(randomIndex);
                        remainingLeaves.remove(randomIndex);

                        leaf.addBranch(newLeaf, random.nextInt(maxWeight) + 1);
                        leavesOnNextLevel.add(newLeaf);
                    }
                }
            }

            leavesOnCurrentLevel = leavesOnNextLevel;
        }

        return tree;
    }

    public void addBranch(Leaf startLeaf, Leaf endLeaf, int weight) {
        startLeaf.addBranch(endLeaf, weight);
    }

    public void removeBranch(Leaf startLeaf, Leaf endLeaf) {
        startLeaf.removeBranch(endLeaf);
    }

    public void print() {
        TreeTraverser.breadthFirstSearch(root, Node::print);
    }

}