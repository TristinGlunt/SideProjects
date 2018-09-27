import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by tristin on 7/18/2017.
 */

/*
Each node knows it's word, it's neighbors (neighborMap), it's location in the
adjacency matrix, and it's g cost, h cost and total cost
 */
public class Node
{
    //Hashmap telling us which Node it is connected to and
    //what the cost is from that node to it's neighbor (costs can change and not just be + 1
    //since your neighbor could either be moving close or further away to END
    private PriorityQueue<Node> neighborMap;
    private String word;
    private double gCost;
    private double hCost;
    private double cost;

    public Node(String word, double gCost, double hCost, double cost)
    {
        this.word = word;
        this.gCost = gCost;
        this.hCost = hCost;
        this.cost = cost;
        Comparator<Node> comparator = new Comparator<Node>() //initialize queue with comparator
        {
            @Override
            public int compare(Node nodeA, Node nodeB)                    //compares two nodes for the PQ
            {                                                             //node with lower ost has higher priority
                int costA =(int) nodeA.getCost();
                int costB =(int) nodeB.getCost();
                if(nodeA.getCost() > nodeB.getCost()) { return 1; }
                else if (nodeB.getCost() > nodeA.getCost()) { return -1; }
                else { return 1; }
            }
        };
        neighborMap = new PriorityQueue<>(11, comparator);

    }

    //addNeighbor adds the node to the neighborMap and lets us know
    //what type of connection the nodes have
    public void addNeighbor(Node node, Double cost)
    {
        neighborMap.add(node);
    }

    public void setgCost(double gCost)
    {
        this.gCost = gCost;
    }

    public void sethCost(double hCost)
    {
        this.hCost = hCost;
    }

    public void setCost(double cost)
    {
        this.cost = cost;
    }
    public void setWord(String word)
    {
        this.word = word;
    }

    public double getgCost() { return gCost; }
    public double gethCost() { return hCost; }
    public double getCost() { return cost; }
    public String getWord() { return word; }

    public PriorityQueue<Node> getNeighborMap() { return neighborMap; }




}
