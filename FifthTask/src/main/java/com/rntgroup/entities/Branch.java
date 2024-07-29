package com.rntgroup.entities;

import com.rntgroup.entities.interfaces.Node;
import com.rntgroup.entities.interfaces.Processable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Branch implements Node, Processable {
    private final Leaf startLeaf;
    private final Leaf endLeaf;
    private int weight;

    public Branch(Leaf startLeaf, Leaf endLeaf, int weight) {
        this.startLeaf = startLeaf;
        this.endLeaf = endLeaf;
        this.weight = weight;
    }

    @Override
    public void print() {
        StringBuilder message = new StringBuilder();
        message.append("start leaf ").append(startLeaf.getValue()).append(" --> ")
                .append("branch weight ").append(weight).append(" --> ")
                .append("end leaf ").append(endLeaf.getValue());
        System.out.println(message);
    }

    @Override
    public List<Node> getOutgoingNodes() {
        return List.of(endLeaf);
    }

    @Override
    public void process() {
        System.out.println("I found branch with weight = " + weight);
    }
}
