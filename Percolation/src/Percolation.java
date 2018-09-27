/*
    @author: Tristin Glunt
    @email: tglunt@unm.edu

    Percolation problem from Princeton Algorithms 1 course
    Started 6/13/2017


    NOTE: THE BELOW 'CIRCLES' USED ARE OH'S, AS IN O O HELL OOOOOOOO, NOT ZEROS AS IN 0000 0 10 0
 */
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;


//implement the Percolation data type using the weighted quick union algorithm in WeightedQuickUnionUF
// Open site is O, closed site is X, full site is F
// create n-by-n grid, with all sites blocked
//TODO set n*n and n*n+1 with global names
public class Percolation {
    private char grid[][];
    private WeightedQuickUnionUF theUnion;
    private WeightedQuickUnionUF otherUnion;
    private int n;
    private int numOpenSites;

    public static void main(String[] args) {
        Percolation perc = new Percolation(Integer.parseInt(args[0]));
        perc.open(1, 1);
        perc.open(2, 1);
        perc.open(3, 1);
        perc.open(4,1);
        StdOut.println(perc.theUnion.connected(perc.xyTo1D(1,1), perc.xyTo1D(2,1)));
        StdOut.println(perc.theUnion.connected(perc.xyTo1D(3,4), perc.xyTo1D(2,4)));
        System.out.println(perc.percolates());
    }

    /*
    uses 2 virtual sites of WeightedQuickUnionUF
     */
    public Percolation(int n) {
        if (n <= 0) throw new java.lang.IllegalArgumentException("bad args");
        this.n = n;
        theUnion = new WeightedQuickUnionUF((n*n)+2);
        otherUnion = new WeightedQuickUnionUF((n*n) + 2);

        grid = new char[n+1][n+1];

        for(int row = 1; row < n+1; row++) {
            for(int col = 1; col < n+1; col++) {
                grid[row][col] = 'X';
            }
        }

    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if(indexError(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("args out of bounds");
        }
        if(grid[row][col] != 'O') {
            grid[row][col] = 'O';
            checkNeighbor(row, col);
            if(row == 1) {
                theUnion.union(xyTo1D(row, col), 0);
                otherUnion.union(xyTo1D(row, col), 0);
            } else if(row == n) {
                theUnion.union(xyTo1D(row, col), (n*n)+1);
            }
            numOpenSites++;
        }

    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col)  {
        if(indexError(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("args out of bounds");
        }
        if(grid[row][col] == 'O') {
            return true;
        }
        return false;
    }

    public boolean isFull(int row, int col) {
        if(indexError(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("args out of bounds");
        }
        int currentNum = xyTo1D(row, col);
        if (otherUnion.connected(currentNum, 0)) {
            return true;
        }
        return false;
    }  // is site (row, col) full?

    public int numberOfOpenSites()   {
        return numOpenSites;
    }     // number of open sites

    public boolean percolates() {
        int currentNum;
        if(n == 1) {
            if(isOpen(1,1)) {
                return true;
            }
            else {
                return false;
            }
        }
        if(theUnion.connected(0, (n*n)+1)) {
            return true;
        }
        return false;
    }              // does the system percolate?

    private void checkNeighbor(int row, int col) {
        if(indexError(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("args out of bounds");
        }
        int oldState, newState;
        if(col != n) {
            if (grid[row][col + 1] == 'O') {
                newState = xyTo1D(row, col);
                oldState = xyTo1D(row, col + 1);
                theUnion.union(newState, oldState);
                otherUnion.union(newState, oldState);
            }
        }
        if(col != 1) {
            if (grid[row][col - 1] == 'O') {
                newState = xyTo1D(row, col);
                oldState = xyTo1D(row, col - 1);
                theUnion.union(newState, oldState);
                otherUnion.union(newState, oldState);
            }
        }
        if(row != 1) {
            if (grid[row - 1][col] == 'O') {
                newState = xyTo1D(row, col);
                oldState = xyTo1D(row - 1, col);
                theUnion.union(newState, oldState);
                otherUnion.union(newState, oldState);
            }
        }
        if(row != n) {
            if (grid[row+1][col] == 'O') {
                newState = xyTo1D( row, col);
                oldState = xyTo1D(row+1, col);
                theUnion.union(newState, oldState);
                otherUnion.union(newState, oldState);
            }
        }
    }

    //since our grid starts at index 1 instead of 0, to do a 2d to 1d conversion we must subtract row from 1
    private int xyTo1D(int row, int col) {
        return this.n * (row-1) + col;
    }

    private boolean indexError(int row, int col) {
        if(row < 1 || row > this.n || col < 1 || col > this.n ) {
            return true;
        }
        return false;
    }
}