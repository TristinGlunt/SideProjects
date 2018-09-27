/*
implement a star algorithm
from begin node to end node
 */
import java.io.File;
import java.io.IOException;
import java.util.*;

public class AStarAlg
{
    private Node begin;
    private Node end;
    private Set<Node> closed;
    private LinkedHashMap<String, String> path;
    private PriorityQueue<Node> open;
    private wordPaths wp;


    /*
    Constructor sets nodes to find path of, size of adj matrix
    adds the beginning node to the open nodes and calls
    find the pathBetween()
     */
    public AStarAlg(Node begin, Node end)
    {
        wp = new wordPaths(begin, end);
        this.begin = begin;
        this.end = end;
        //pathBetween();
    }

    /*
    PathBetween is our main A* algorithm

     */
    public List<String> pathBetween()
    {
        closed = new HashSet<>();
        path = new LinkedHashMap<>();


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

        open = new PriorityQueue<Node>(11, comparator);  //priorityQ to keep node with lowest weight at top
        open.add(begin);                                            //queue will start with the beginning node

        while(!open.isEmpty())
        {
            Node current = open.poll();                             //current node will be highest priority from queue

            System.out.println("Current: " + current.getWord());
            closed.add(current);                                    //add current node to closed set
            wp.words.remove(current.getWord());    //(I can't remove the word from dictionary because if I look at multiple
                                                    // word ladders the algorithm will not work)
            for(Node neighbor : wp.findNeighbors(current))
            {
                if(neighbor.getWord().equals(end.getWord()))
                {
                    path.put(neighbor.getWord(), current.getWord());
                    return backTracePath(neighbor.getWord());
                }

                if(closed.contains(neighbor)) { continue; }

                double distBetweenNodes = wp.levenshtein(current.getWord(), neighbor.getWord());
                double tentG = (distBetweenNodes + current.getgCost()) * 0.25;

                if(tentG < neighbor.getgCost() || !open.contains(neighbor)) //if the new score to the neighbor node is better
                {                                                           //rewrite the path to the node and add that new
                    neighbor.setgCost(tentG);                               //possible path to open queue to be evaluated
                    neighbor.sethCost(wp.levenshtein(neighbor.getWord(), end.getWord()));
                    neighbor.setCost(neighbor.getgCost() + neighbor.gethCost());

                    path.put(neighbor.getWord(), current.getWord());

                    open.add(neighbor);
                }
            }
        }
        return null;
    }

    /*
    Build the path made by pathBetween() by using the Hashset
    and accessing the keys giving us the values
     */
    //TODO go through path, and remove words it could've jumped   }
    //TODO ex wanning -> banning -> manning -> fanning to           }   I believe this will be the last path finding polishing i do
    //TODO    wanning -> fanning                                  }
    private List<String> backTracePath(String destination) {

        final List<String> pathList = new ArrayList<>();
        pathList.add(destination);
        while (path.containsKey(destination)) {
            destination = path.get(destination);
            pathList.add(destination);
        }
        Collections.reverse(pathList);
        return pathList;
    }

    public void printNeighbors(Node node)
    {
        PriorityQueue<Node> neighbors = new PriorityQueue<>();
        neighbors = wp.findNeighbors(node);
        for(Node step : neighbors)
        {
            System.out.println(step.getWord());
        }
    }

    public static void main(String[] args)
    {
        //todo check if source and destination nodes have any neighbors
        try
        {
            Scanner file = new Scanner(new File("wordPathTestArgs.txt"));
            String fileName = file.next();

            while (file.hasNext())
            {
                String beginWord = file.next();
                String endWord = file.next();
                Node begin = new Node(beginWord, 0, 0, 0);
                Node end = new Node(endWord, 0, 0, 0);
                AStarAlg aStar = new AStarAlg(begin, end);
                aStar.wp.loadDictionary(fileName);
                List<String> thePath = aStar.pathBetween();
                for (String step : thePath)
                {
                    System.out.println(step);
                }
            }
        } catch (IOException e)
        {
            System.out.println(new File("input.txt").getAbsolutePath());
            System.out.println(e);
        }
    }
}

 /*

        Node begin = new Node("running", 0, 8, 8);
        Node end = new Node("swimming", 4, 0, 0);
        int i = 0;
        AStarAlg aStar = new AStarAlg(begin, end);
        aStar.wp.loadDictionary("OpenEnglishWordList.txt");
        //aStar.printNeighbors(end);
        //System.out.println(aStar.wp.isAdjacent("bilking", "biking"));
        //below is temp, will print only length of array made from getWordPath!!
        //System.out.println(aStar.wp.levenshtein("brown", "ghee"));
        List<String> thePath = aStar.pathBetween();
        System.out.println("\n\n\n\nPath:");
        for(String step : thePath)
        {
            System.out.println(step);
        }
        ArrayList<String> finishedPath = new ArrayList<>();
        System.out.println("\n\nPath: \n");
        String current = end.getWord();
        while(i < 25)
        {
            //System.out.println(current);
            finishedPath.add(current);
            if(current.equals(begin.getWord())) { break; }
            current = thePath.get(current);
            i++;
        }
        Collections.reverse(finishedPath);
        for(String step : finishedPath) { System.out.println(step); }
        //System.out.println(aStar.wp.isAdjacent("brews", "brown"));
        //aStar.printNeighbors(begin);

    }

public void loadInput()
        {
        try
        {
        Scanner file = new Scanner(new File("C:/Users/tristin/IdeaProjects/WordPathFinder/input.txt"));
        String fileName = file.next();
        wp.loadDictionary(fileName);

        while(file.hasNext())
        {
        String beginWord = file.next();
        String endWord = file.next();
        Node begin = new Node(beginWord, 0, 0, 0);
        Node end = new Node(endWord, 0, 0, 0);
        AStarAlg aStar = new AStarAlg(begin, end);
        List<String> thePath = aStar.pathBetween();
        for(String step : thePath)
        {
        System.out.println(step);
        }
        }
        } catch (IOException e)
        {
        System.out.println(new File("input.txt").getAbsolutePath());
        System.out.println(e);
        }
        }
*/