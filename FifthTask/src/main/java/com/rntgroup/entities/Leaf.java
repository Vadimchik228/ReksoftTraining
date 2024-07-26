package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.Node;
import com.rntgroup.entities.interfaces.Processable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class Leaf implements Node, Processable {
    private int value;
    private List<Branch> branches;

    public Leaf(int inputValue) {
        value = inputValue;
        branches = new ArrayList<>();
    }

    @Override
    public void print() {
        StringBuilder message = new StringBuilder();
        if (branches.isEmpty()) {
            System.out.println("leaf value " + value + " -->");
            return;
        }
        for (int i = 0; i < branches.size(); ++i) {
            if (i == 0) {
                message.append("leaf value ").append(branches.get(i).getStartLeaf().value).append(" --> ");
            }
            message.append("next leaf value ").append(branches.get(i).getEndLeaf().value);
            message.append(" (branch weight ").append(branches.get(i).getWeight()).append(")");
            if (i != branches.size() - 1) {
                message.append(", ");
            }
        }
        System.out.println(message);
    }

    @Override
    public List<Node> getOutgoingNodes() {
        return Collections.unmodifiableList(branches);
    }

    @Override
    public void process() {
        System.out.println("I found leaf with value = " + value);
    }

    public void addBranch(Leaf endLeaf, int weight) {
        branches.add(new Branch(this, endLeaf, weight));
    }

    public void removeBranch(Leaf endLeaf) {
        branches.removeIf(branch -> branch.getEndLeaf().equals(endLeaf));
    }
}
