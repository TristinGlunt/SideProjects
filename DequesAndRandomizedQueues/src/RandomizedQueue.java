import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] a = (Item[]) new Object[1];
    private int n;

    // is the queue empty?
    public boolean isEmpty()
    { return n == 0; }

    // return the number of items on the queue
    public int size()
    { return n; }

    public RandomizedQueue() { }


    private void resize(int max)
    {
        Item[] temp = (Item[]) new Object[max];
        for (int i = 0; i < n; i++)
        {
            if (!(a[i] == null))
            {
                temp[i] = a[i];
            }
        }
        a = temp;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException("illegal arg");
        }
        if (n == a.length) { resize(a.length*2); }
        a[n++] = item;

    }

    // remove and return a random item
    //HINT (from pg 168 algs) Use an array representation (with resizing). To remove an item,
    //swap one at a random position (index 0 through n-1) with one at the last position (index n-1).
    //Then delete and return the last object.

    public Item dequeue()
    {
        int ranNum = 0;
        if(isEmpty())
        {
            throw new java.util.NoSuchElementException("Empty deque");
        }
        if(n == 1)
        {
            ranNum = 0;
        } else
        {
            ranNum = StdRandom.uniform(0, n-1);
        }
        Item temp = a[ranNum];
        Item item = a[ranNum];
        a[ranNum] = a[n-1];
        a[n-1] = null;

        n--;

        if(n > 0 && n == a.length/4) resize(a.length / 2);
        return item;

    }

    // return (but do not remove) a random item
    public Item sample()
    {
        int ranNum;
        if(isEmpty())
        {
            throw new java.util.NoSuchElementException("Empty deque");
        }

        if(n == 1)
        {
            ranNum = 0;
        } else
        {
            ranNum = StdRandom.uniform(0, n-1);
        }
        return a[ranNum];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        if(n <= 1)
        {
            StdRandom.shuffle(a);
        }
        else
        {
            StdRandom.shuffle(a, 0, n - 1);
        }
        return new RandomizedQueueIterator();
    }


    /*
    the most important thing about this iterator is the hasNext method catching
    the ending element before it goes into next, next will catch all elements
    that are null and skip them (other than the last element which hasNext will return false on)
     */
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        // total will be all of the elements in the array//
        private int total = n - 1;
        int i = 0;

        public boolean hasNext() {
            //if i is the total, i.e 2 and a[2] is null, there is no next it's the last item
            if(i == total && a[i] == null)
            {
                return false;
            }
            return i <= total;
        }

        public void remove() { throw new java.lang.UnsupportedOperationException("no removing"); }


        //this next will catch if a[0] is null, a[1] will be returned instead and then
        //a[2] will be next
        public Item next()
        {
            if(!hasNext())
            {
                throw new java.util.NoSuchElementException("Empty deque");
            }
            if(a[i] == null) {
                i = i + 1;
                return a[i++];
            }
            return a[i++];
        }

    }

    public static void main(String[] args)
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.dequeue();

        for(int i : rq)
        {
            System.out.println(i);
        }
    }
}