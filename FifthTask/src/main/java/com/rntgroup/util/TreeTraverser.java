package com.rntgroup.util;

import com.rntgroup.entities.interfaces.Node;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Consumer;

@UtilityClass
public class TreeTraverser {

    /**
     * Depth-First Search (DFS) - алгоритм обхода графа, который
     * исследует дерево, углубляясь в ветви, пока не достигнет конечного узла,
     * а затем возвращается и исследует следующую ветвь.
     *
     * @param startNode Начальный узел для обхода
     * @param action    Действие, которое необходимо выполнить для каждого посещенного узла
     */
    public static void depthFirstSearch(Node startNode, Consumer<Node> action) {
        Stack<Node> stack = new Stack<>();
        Set<Node> visited = new HashSet<>();

        stack.push(startNode);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            processNodeDfs(currentNode, stack, visited, action);
        }
    }

    /**
     * Breadth-First Search (BFS) - алгоритм обхода графа, который
     * исследует дерево, проходя по уровням, посещая все узлы на текущем
     * уровне, прежде чем перейти к следующему уровню.
     *
     * @param startNode Начальный узел для обхода
     * @param action    Действие, которое необходимо выполнить для каждого посещенного узла
     */
    public static void breadthFirstSearch(Node startNode, Consumer<Node> action) {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        queue.offer(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            processNodeBfs(currentNode, queue, visited, action);
        }
    }

    private static void processNodeDfs(Node currentNode, Stack<Node> stack, Set<Node> visited, Consumer<Node> action) {
        if (!visited.contains(currentNode)) {
            visited.add(currentNode);
            action.accept(currentNode);

            for (Node outgoingNode : currentNode.getOutgoingNodes()) {
                if (!visited.contains(outgoingNode)) {
                    stack.push(outgoingNode);
                }
            }
        }
    }

    private static void processNodeBfs(Node currentNode, Queue<Node> queue, Set<Node> visited, Consumer<Node> action) {
        action.accept(currentNode);

        for (Node outgoingNode : currentNode.getOutgoingNodes()) {
            if (!visited.contains(outgoingNode)) {
                queue.offer(outgoingNode);
                visited.add(outgoingNode);
            }
        }
    }
}
