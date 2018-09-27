/*
  @author: Tristin Glunt
  @email: tglunt@unm.edu
  The following project is for Princeton Algorithms course
  under Coursera
  7/7/17
 */

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int n;


    // construct an empty deque
    public Deque() {}

    private class Node
    {
        Item item;
        Node next;
        Node previous;
    }

    // is the deque empty?
    public boolean isEmpty()
    { return (first == null); }

    // return the number of items on the deque
    public int size()
    { return n; }

    // add the item to the front
    public void addFirst(Item item)
    {
        throwNullItem(item);

        Node oldFirst = first;

        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;
        n++;


        //If im understanding everything correctly, this
        //can be replaced by first.next == null;
        if(oldFirst == null) last = first;
        else
        {
            oldFirst.previous = first;
        }
    }

    // add the item to the end
    public void addLast(Item item)
    {
        throwNullItem(item);

        Node oldLast = last;

        last = new Node();
        last.item = item;
        last.next = null;
        last.previous = oldLast;
        n++;

        if(isEmpty()) first = last;
        else oldLast.next = last;

    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        throwRemoveItem(n);

        Item item = first.item;
        if(first.next == null)
        {
            first = null;
            last = first;
        } else
        {
            first = first.next;
            first.previous = null;
        }

        n--;

        if(first.next == null) last = first;

        return item;

    }

    // remove and return the item from the end
    public Item removeLast()
    {
        throwRemoveItem(n);

        Item item = last.item;
        if(last.previous == null)
        {
            last = null;
            first = last;
        }
        else
        {
            last = last.previous;
            last.next = null;
        }
        n--;

        if(n == 1)
        {
            first = last;
        }
        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node current = first;
        public boolean hasNext() { return current != null; }

        public void remove() {
            throw new java.lang.UnsupportedOperationException("Unsupported exception");
        }

        public Item next()
        {
            if(!hasNext())
            {
                throw new java.util.NoSuchElementException("Empty deque");
            }
            throwNullItem(current.item);
            Item item = current.item;
            current = current.next;
            return item;

        }

    }


    private void throwNullItem(Item item) {
        if (item == null)
        {
            throw new java.lang.IllegalArgumentException("illegal arg");
        }
    }

    private void throwRemoveItem(int n) {
        if (n == 0)
        {
            throw new java.util.NoSuchElementException("deque is empty");
        }
    }

    /*
    currently the deque seems to be functioning properly
     */
    public static void main(String[] args)
    {
        Deque<Integer> dq = new Deque<Integer>();

        dq.addLast(1);
        dq.addLast(2);


        System.out.println(dq.first.item);
        System.out.println(dq.first.previous);
        System.out.println(dq.first.next);
        System.out.println(dq.last.item);
        System.out.println(dq.last.previous);
        System.out.println(dq.last.next);
        System.out.println(dq.last.next == null);

        for(int i : dq)
        {
            System.out.println(i);
        }

    }// unit testing (optional)

}

