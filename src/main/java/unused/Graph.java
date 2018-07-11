package unused;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * An variant of a graph that allows each node to
 * track the number of edges pointing to it.
 */
public class Graph<T> {
    protected class Node {
        protected T value;
        protected int numDependencies;

        protected Node(T value) {
            this.value = value;
        }
    }

    // Adjacency list that maps node keys to their neighbors
    protected HashMap<String, List<String>> adjs;

    // Maps keys to the actual Nodes. Each Node contains a value,
    // as well as the number of edges directed towards them
    protected HashMap<String, Node> nodes;

    public Graph() {
        adjs = new HashMap<>();
        nodes = new HashMap<>();
    }

    /*
     * Returns the node with the specified key. If the node does
     * not exist, then null is returned instead
     */
    public T get(String key) {
        if (!nodes.containsKey(key)) {
            return null;
        }

        return nodes.get(key).value;
    }

    /*
     * Returns a list of neighbors for the node specified by the
     * key. If the node does not exist, an empty list is returned
     */
    public List<String> getNeighbors(String key) {
        if (!nodes.containsKey(key)) {
            return new LinkedList<>();
        }

        return adjs.get(key);
    }

    /*
     * Inserts a node key/value pair into the graph if the node
     * does not already exist. If the key already exists, then
     * the graph is unchanged. Returns whether the node is added
     */
    public boolean insert(String key, T value) {
        if (nodes.containsKey(key)) {
            return false;
        }

        nodes.put(key, new Node(value));
        adjs.put(key, new LinkedList<>());
        System.out.println("Added " + key + " to graph");
        return true;
    }

    /*
     * Removes the node with the specified key from the graph,
     * along with all of its edges. If the node does not exist,
     * nothing happens
     */
    public T remove(String key) {
        if (!nodes.containsKey(key)) {
            System.out.println(key + " is not in the graph. Remove request ignored");
            return null;
        }

        // Do not remove if there's another node pointing to it
        if (nodes.get(key).numDependencies > 0) {
            return null;
        }

        for (String neighborKey : adjs.get(key)) {
            Node neighbor = nodes.get(neighborKey);
            neighbor.numDependencies = Math.max(0, neighbor.numDependencies - 1);
            System.out.println(neighborKey + "'s number of dependencies is " + neighbor.numDependencies);
        }

        adjs.remove(key);

        System.out.println("Removed " + key + " from the graph");
        return nodes.remove(key).value;
    }

    /*
     * Adds an edge between the nodes specified by the source
     * and destination keys, and returns true if the operation is
     * successful. The operation only fails if either node does
     * not exist
     */
    public boolean addEdge(String sourceKey, String destKey) {
        if (!nodes.containsKey(sourceKey) || !nodes.containsKey(destKey)) {
            System.out.println("Tried to add invalid edge from " + sourceKey + " to " + destKey);
            return false;
        }

        Node destNode = nodes.get(destKey);
        adjs.get(sourceKey).add(destKey);
        destNode.numDependencies++;
        System.out.println("Added edge from " + sourceKey + " to " + destKey);
        return true;
    }

    /*
     * Returns whether the node with the specified key has at least
     * one edge pointing to it. If the node does not exist, false
     * is returned
     */
    public boolean isDependent(String key) {
        if (!nodes.containsKey(key)) {
            return false;
        }

        return nodes.get(key).numDependencies > 0;
    }
}