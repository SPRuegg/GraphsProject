package graphs;

import java.util.Comparator;

public class Graph <E extends Comparable<E>> {

    /**
     * A vertex representation for a graph
     */
    private class Vertex {

        /**
         * The data describing this vertex.
         */
        E data;

        /**
         * The in-degree of this vertex
         */
        int inDeg;

        /**
         * The out-degree of this vertex
         */
        int outDeg;

        /**
         * Identifier of if we have seen this vertex
         */
        boolean accessed;

        /**
         * Identifier of if we have processed this vertex
         */
        boolean processed;

        /**
         * Pointer to the first edge of this vertex
         */
        Edge pEdgeList;

        /**
         * Pointer to the next vertex of the graph
         */
        Vertex nextVertex;
    }

    /**
     * An edge representation for a graph
     */
    private class Edge implements Comparable<Edge> {

        /**
         * The weight of this edge
         */
        Double weight;

        /**
         * The source vertex, for a directed graph
         */
        Vertex from;

        /**
         * The destination vertex, for a directed graph
         */
        Vertex to;

        /**
         * Pointer to the next Edge of the from vertex
         */
        Edge nextEdge;

        @Override
        public int compareTo(Edge other) {
            if (cmp.compare(from.data, other.from.data) != 0)
                return cmp.compare(from.data, other.from.data);
            else if (cmp.compare(to.data, other.to.data) != 0) {
                return cmp.compare(to.data, other.to.data);
            }
            return Double.compare(weight, other.weight);
        }
    }


    private Vertex first;
    private int size;
    private int edges;
    Comparator<? super E> cmp;

    private final boolean directed;

    /**
     * Constructor that uses the default defined comparison
     * system, from the Comparable interface.
     *
     * @param di the type of graph, <b>true</b> if the
     *           graph should be directed, and false
     *           if undirected.
     */
    public Graph(boolean di) {
        first = null;
        size = 0;
        cmp = Comparable::compareTo;
        directed = di;
    }

    /**
     * Constructor that intakes a new comparison system,
     * as defined by the input function.
     *
     * @param fn the new comparison definition
     * @param di the type of graph, <b>true</b> if the
     *        graph should be directed, and false
     *        if undirected.
     */
    public Graph(Comparator<? super E> fn, boolean di) {
        first = null;
        size = 0;
        cmp = fn;
        directed = di;
    }

    /**
     * Gets the amount of vertices in the graph.
     *
     * @return the amount of vertices in the graph
     */
    public int size() {
        return size;
    }

    /**
     * Gets the amount of edges in the graph.
     *
     * @return the amount of vertices in the graph
     */
    public int edges() {
        return edges;
    }

    /**
     * Gets the in-degree of the specified vertex, if it exists.
     *
     * @param data the data of the specified vertex
     * @return the in-degree of the specified vertex
     * @throws GraphException if the graph is undirected,
     *                        or the vertex doesn't exist
     */
    public int getInDegree(E data) throws GraphException {
        if (!directed) {
            String message = String.format("Undirected graphs, don't have in-degrees. Please use %n" +
                                           "'getDegree(E data)'.");
            throw new GraphException(message);
        }

        Vertex vert = retrieveVertex(data);

        if (vert == null) throw new GraphException("Vertex: " + data.toString() + " does not exist.");

        return vert.inDeg;
    }

    /**
     * Gets the out-degree of the specified vertex, if it exists.
     *
     * @param data the data of the specified vertex
     * @return the out-degree of the specified vertex
     * @throws GraphException if the graph is undirected,
     *                        or the vertex doesn't exist
     */
    public int getOutDegree(E data) throws GraphException{
        if (!directed) {
            String message = String.format("Undirected graphs, don't have out-degrees. Please use %n" +
                                           "'getDegree(E data)'.");
            throw new GraphException(message);
        }

        Vertex vert = retrieveVertex(data);

        if (vert == null) throw new GraphException("Vertex: " + data.toString() + " does not exist.");

        return vert.outDeg;
    }

    /**
     * Gets the degree of the specified vertex, if it exists.
     *
     * @param data the data of the specified vertex
     * @return the degree of the vertex
     * @throws GraphException if the graph is directed,
     *                        or the vertex doesn't exist
     */
    public int getDegree(E data) throws GraphException {
        if (directed) {
            String message = String.format("Directed graphs, has specific degrees. Please use %n" +
                                           "'getInDegree(E data)', or 'getOutDegree(E data)'.");
            throw new GraphException(message);
        }

        Vertex vert = retrieveVertex(data);

        if (vert == null) throw new GraphException("Vertex: " + data.toString() + " does not exist.");

        return (vert.outDeg * 2) + (vert.inDeg * 2);
    }

    /**
     * Identifies if the graph is empty.
     *
     * @return <b>true</b>> if the graph is empty,
     *         <b>false</b> otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Auxiliary method to retrieve a vertex from a key, if it
     * exists.
     *
     * @param data the key/data of the vertex to be located
     * @return the vertex, if it exists, or null otherwise
     */
    private Vertex retrieveVertex(E data) {
        Vertex curr = first;
        while (curr != null && cmp.compare(data, curr.data) > 0) {
            curr = curr.nextVertex;
        }

        if (curr == null || cmp.compare(data, curr.data) != 0) {
            return null;
        }

        return curr;
    }

    /**
     * Checks if the graph contains a vertex with the specified data.
     *
     * @param data the data to search for in the graph
     * @return <b>true</b> if a vertex with the specified data exists in the graph,
     *         <b>false</b> otherwise
     */
    public boolean contains(E data) {
        if (isEmpty()) return false;

        Vertex vert = retrieveVertex(data);

        return vert != null;
    }

    /**
     * Inserts a new vertex with the specified data into the graph.
     * The vertex is inserted in sorted order based on the comparator.
     *
     * @param data the data to be added as a vertex in the graph
     * @return <b>true</b> if the vertex was not already present,
     *         <b>false</b> if the data already exists in the graph
     */
    public boolean insertVertex(E data) {
        if (isEmpty()) {
            first = new Vertex();
            first.data = data;
            vertices++;
            return true;
        }

        // Special Case: first vertex
        if (cmp.compare(data, first.data) < 0) {
            Vertex newVert = new Vertex();
            newVert.data = data;

            // LinkedList head reassignment
            newVert.nextVertex = first;
            first = newVert;
            vertices++;
            return true;
        } else if (cmp.compare(data, first.data) == 0) {
            return false;
        }

        Vertex curr = first;

        // Find correct position
        while (curr.nextVertex != null && cmp.compare(data, curr.nextVertex.data) > 0) {
            curr = curr.nextVertex;
        }

        // Ensure the requested data, doesn't exist already
        if (cmp.compare(data, curr.data) != 0) {
            Vertex newVert = new Vertex();
            newVert.data = data;

            // LinkedList insertion
            newVert.nextVertex = curr.nextVertex;
            curr.nextVertex = newVert;
            vertices++;
            return true;
        }

        return false;
    }

    /**
     * Removes a vertex and all its associated edges from the graph.
     * Updates in-degrees, and out-degrees accordingly.
     *
     * @param data the data of the vertex to be removed
     * @return <b>true</b> if the vertex was found and removed,
     *         <b>false</b> if no vertex with the specified data exists
     */
    public boolean deleteVertex(E data) {
        // To be implemented
      
        return false;
    }

    /**
     * Inserts an edge between two vertices in the graph.
     * Updates the vertices in/out-degrees.
     *
     * @param from data of the source vertex
     * @param to data of the destination vertex
     * @param weight the weight of the edge
     * @return <b>true</b> if the edge was successfully added,
     *         <b>false</b> if the edge already exists or vertices not found
     */
    public boolean insertEdge(E from, E to, double weight) {
        // To be implemented

        return false;
    }

    /**
     * Deletes an edge between two vertices in the graph.
     * Updates the vertices in/out-degrees.
     *
     * @param from data of the source vertex
     * @param to data of the destination vertex
     * @param weight the weight of the edge
     * @return <b>true</b> if the edge was successfully deleted,
     *         <b>false</b> if the edge doesn't exist or vertices not found
     */
    public boolean deleteEdge(E from, E to, double weight) {
        // To be implemented

        return false;
    }
}
