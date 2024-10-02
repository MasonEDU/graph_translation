//should import everything I need, i don't remember where i found this
//but it works so i can't complain
import java.util.*;
//for io file reading/writing
import java.io.*;

//im confused on why i need main to encompass the entire program but it wouldn't work without it.
public class Main {

    //main functions
    public static void main(String[] args) {
        //declairing graph before graph is declaired but oh well
        Graph myGraph = new Graph();
        //normal stuff as the original graph.cpp but system.out stuff :(
        if (myGraph.readUndirectedGraph("graph.txt")) {
            System.out.println(myGraph.show());
            findConnectedComponents(myGraph);
        } else {
            System.err.println("Problem reading graph from graph.txt");
        }
    }

    //general function not within a class
    public static void findConnectedComponents(Graph g) {
        //take and adapated from: https://stackoverflow.com/questions/1200621/how-do-i-declare-and-initialize-an-array-in-java
        int[] componentNumber = new int[g.numVertices()];
        Arrays.fill(componentNumber, -1); // Initialize all to -1

        boolean foundComponent;
        do {
            foundComponent = false;
            int vertexNotInComponent = 0;
            //same same as the c++ stuff
            while (vertexNotInComponent < g.numVertices() && componentNumber[vertexNotInComponent] != -1) {
                vertexNotInComponent++;
            }
            
            
            if (vertexNotInComponent < g.numVertices()) {
                foundComponent = true;
                //https://www.geeksforgeeks.org/set-in-java/
                //hash set seems the same as binary tree's
                Set<Integer> open = new HashSet<>();
                Set<Integer> closed = new HashSet<>();
                open.add(vertexNotInComponent);
                System.out.println("\nMoving to new component:");
                System.out.println("Adding " + vertexNotInComponent + " to open");
                //the rest is basically the same but java types
                while (!open.isEmpty()) {
                    int firstOpenVertex = open.iterator().next();
                    open.remove(firstOpenVertex);
                    System.out.println("Popped " + firstOpenVertex + " from open");

                    closed.add(firstOpenVertex);
                    System.out.println("Added " + firstOpenVertex + " to closed");
                    componentNumber[firstOpenVertex] = vertexNotInComponent;

                    for (Vertex neighborVertex : g.vertexList().get(firstOpenVertex).getAdjacent()) {
                        if (!closed.contains(neighborVertex.getVertexNumber()) && !open.contains(neighborVertex.getVertexNumber())) {
                            System.out.println("Adding " + neighborVertex.getVertexNumber() + " to open");
                            open.add(neighborVertex.getVertexNumber());
                        }
                    }
                }
            }
        } while (foundComponent);

        for (int v = 0; v < g.numVertices(); v++) {
            System.out.println(v + ": " + componentNumber[v]);
        }
    }
}


class Vertex {
    //basically the same but i have to define functions within the class at declariation along with setting auto values.
    private static int vertexCount = 0; // defined now rather than later
    private String vertexName;
    private int vertexNumber;
    private Vector<Vertex> adjacent;

    public Vertex(String n) {
        this.vertexName = n;
        this.vertexNumber = ++vertexCount;
        this.adjacent = new Vector<>();
    }

    public String getName() {
        return vertexName;
    }

    public void setName(String n) {
        this.vertexName = n;
    }

    public int getVertexNumber() {
        return vertexNumber;
    }

    public boolean addConnection(Vertex v) {
        for (Vertex adj : adjacent) {
            if (adj == v) {
                return false;
            }
        }
        adjacent.addElement(v);
        return true;
    }

    public Vector<Vertex> getAdjacent() {
        return adjacent;
    }
    //java has stringBuilder built in i think? it worked idk...
    public String show() {
        StringBuilder result = new StringBuilder("  " + vertexName + ":\n");
        for (Vertex v : adjacent) {
            result.append("    ").append(v.getName()).append("\n");
        }
        return result.toString();
    }
}


class Graph {
    //same ideas as vertex class but adapted to this or something
    private Vector<Vertex> vertices;

    public Graph() {
        this.vertices = new Vector<>();
    }

    public boolean readUndirectedGraph(String fileName) {
        //had to use a try cuz it kept erroring
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] names = line.split(" ");
                //defining vertex strings here and now because it can't go later.
                String v1name = names[0];
                String v2name = names[1];
                Vertex v1 = getOrInsertVertexName(v1name);
                Vertex v2 = getOrInsertVertexName(v2name);

                if (!v1.addConnection(v2)) {
                    System.err.println("Warning: vertex " + v1name + " and " + v2name + " already connected.");
                }
                if (!v2.addConnection(v1)) {
                    System.err.println("Warning: vertex " + v2name + " and " + v1name + " already connected.");
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Problem reading graph from " + fileName);
            return false;
        }
    }
    //same as c++
    private Vertex getVertex(String vertexName) {
        for (Vertex v : vertices) {
            //used equals because https://www.geeksforgeeks.org/java-equals-compareto-equalsignorecase-and-compare/ this
            if (vertexName.equals(v.getName())) {
                return v;
            }
        }
        return null;
    }

    private Vertex insertVertexName(String vertexName) {
        Vertex newV = new Vertex(vertexName);
        vertices.add(newV);
        return newV;
    }

    private Vertex getOrInsertVertexName(String vertexName) {
        Vertex v = getVertex(vertexName);
        if (v == null) {
            return insertVertexName(vertexName);
        } else {
            return v;
        }
    }

    public Vector<Vertex> vertexList() {
        return vertices;
    }

    public String show() {
        StringBuilder result = new StringBuilder();
        for (Vertex v : vertices) {
            result.append(v.show());
        }
        return result.toString();
    }

    public int numVertices() {
        return vertices.size();
    }
}
