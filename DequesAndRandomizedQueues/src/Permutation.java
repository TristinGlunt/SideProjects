import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

public class Permutation
{
        public static void main(String[] args)
        {
            RandomizedQueue<String> rq = new RandomizedQueue<String>();
            int k = Integer.parseInt(args[0]);
            //In in = new In("C:/Users/Owner/IdeaProjects/DequesAndRandomizedQueues/src/queues/tinytale.txt");

            while (!StdIn.isEmpty())
            {
                String item = StdIn.readString();
                //System.out.println(item);
                rq.enqueue(item);
            }

            //System.out.println();

            /*
            for(String i : rq)
            {
                System.out.println(i);
            }

            System.out.println();

            */
            for(int i = 0; i < k; i++)
            {
                String removedItem = rq.dequeue();
                System.out.println(removedItem);
            }


        }
}
