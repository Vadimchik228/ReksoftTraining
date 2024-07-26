package com.rntgroup.entities.interfaces;

import java.util.List;

public interface Node {
    List<Node> getOutgoingNodes();

    void print();
}
