/*

    7/18/2017
    tglunt@unm.edu

    I plan for this class to create the nodes out of the words from the file, and then
    reading in the dictionary for possible changes to words and associate a cost for a change

 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class wordPaths
{
    public HashSet<String> words = new HashSet<>();
    private Node begin;
    private Node end;
    private String beginWord;
    private String endWord;

    public wordPaths(Node begin, Node end)
    {
        this.end = end;
        this.begin = begin;
    }

    /*
        loadFile will load in our dictionary to be referenced as possible
        word changes, the plan is to check valid word changes with the hashset
        words.contains(WORD_TO_BE_VALIDATED)
     */
    public void loadDictionary(String fileName)
    {
        try
        {
            Scanner file = new Scanner(new File(fileName));

            while (file.hasNext())
            {
                words.add(file.next().trim());
            }

        } catch (IOException e)
        {
            System.out.println("file not found");
        }
    }


    public boolean isAdjacent(String nodeA, String nodeB)
    {
        if (levenshtein(nodeA, nodeB) > 10)
        {
            return false;
        }
        return true;
    }


    /*
        Levenshtein calculates distance between nodes even if they are varying
        of length. It currently can tell if a word is adjacent if it needs to remove
        a single letter in the middle of the word. It will also detect if a single letter
        needs to be added by using the same logic. (removing the letter of the longer word and
        comparing the two words)
     */
    public double levenshtein(String wordA, String wordB)
    {
        int lDistance = 0;
        StringBuilder sbWordA = new StringBuilder(wordA);
        StringBuilder sbWordB = new StringBuilder(wordB);

        int n = wordA.length();
        int m = wordB.length();

        char wordAArr[] = wordA.toCharArray();
        char wordBArr[] = wordB.toCharArray();

        if (n < m)                                   //if wordA is shorter than wordB
        {
            lDistance = (m - n) * 10;               //find the distance between them
            if (lDistance == 10)
            {
                for (int i = 0; i < n; i++)         //loop through our shorter word
                {
                    if (wordAArr[i] != wordBArr[i]) //this will cover a letter difference in the middle of the word
                    {
                        sbWordB.deleteCharAt(i);    //remove the letter that is making them still differ
                        wordBArr = sbWordB.toString().toCharArray(); //converts SB to string then to char array
                        break;                      //only do this for one letter
                    }
                }
            }
            for (int i = 0; i < n; i++)             //if there was only one additional letter in the word,
            {                                       //we will have now removed it
                if (wordAArr[i] != wordBArr[i]) lDistance += 10;    //and there should be no more lDist. added
            }                                       //if there is, the words were not adjacent
        } else if (n > m)
        {
            lDistance = (n - m) * 10;
            if (lDistance == 10)
            {
                for (int i = 0; i < m; i++)
                {
                    if (wordAArr[i] != wordBArr[i])
                    {
                        sbWordA.deleteCharAt(i);
                        wordAArr = sbWordA.toString().toCharArray();
                        break;
                    }
                }
            }
            for (int i = 0; i < m; i++)
            {
                if (wordAArr[i] != wordBArr[i]) lDistance += 10;
            }
        } else
        {
            for (int i = 0; i < n; i++)
            {
                if (wordAArr[i] != wordBArr[i]) lDistance += 10;
            }
        }
        return lDistance;
    }


    /*
    finds all of the neighbors for node being passed
    to the function and adds those neighbors to nodes
    neighbor map, returns an arrayList of the Node words
     */
    public PriorityQueue<Node> findNeighbors(Node node)
    {
        for (String step : words)
        {
            if (isAdjacent(step, node.getWord()))
            {
                double hCost = levenshtein(step, this.end.getWord());
                //double gCost = levenshtein(step, this.begin.getWord());
                double gCost = (node.getCost() + levenshtein(step, node.getWord())) * 0.25;
                Node newNode = new Node(step, gCost, hCost, gCost + hCost);
                node.addNeighbor(newNode, newNode.getCost());
            }
        }
        return node.getNeighborMap();
    }


    public static void main(String[] args)
    {
        //wordPaths wp = new wordPaths();
        //wp.loadInput();
    }
}