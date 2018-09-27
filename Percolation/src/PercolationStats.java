import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double globalTrials[];
    private int globalT;


    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new
                java.lang.IllegalArgumentException("bad args");

        int T = trials;
        globalT = trials;
        globalTrials = new double[trials];

        int j = 0;
        int t;
        for(int i = 0; i < T; i++) {
            Percolation perc = new Percolation(n);
            t = 0;
            while(true) {
                int ranNum = StdRandom.uniform(1, n+1);
                int ranNum2 = StdRandom.uniform(1, n+1);
                //System.out.println("rannum = " + ranNum);
                int col = ranNum;
                int row = ranNum2;
                //System.out.println("row = " + row + " col = " + col);

                if (!perc.isOpen(row, col)) {
                    //System.out.println("site opened");
                    perc.open(row, col);
                    //System.out.println(perc.percolates());
                    //System.out.println("sites opened = " + t);
                    t++;
                }

                if(perc.percolates()) {
                    globalTrials[j] = (double)t/(n*n);
                    //System.out.println("System percolated:" + j);
                    j++;
                    break;
                }
            }
        }
    } // perform trials independent experiments on an n-by-n grid

    public double mean() {
        double mean;
        mean = StdStats.mean(globalTrials);
        return mean;
    }                   // sample mean of percolation threshold

    public double stddev() {
        return StdStats.stddev(globalTrials);
    }                        // sample standard deviation of percolation threshold

    public double confidenceLo() {
        double lo = mean() - (1.96*stddev()/ Math.sqrt(globalT));
        return lo;
    }                  // low  endpoint of 95% confidence interval
    public double confidenceHi()    {
        double hi = mean() + (1.96*stddev()/ Math.sqrt(globalT));
        return hi;
    }               // high endpoint of 95% confidence interval

    public static void main(String[] args)  {

        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]),Integer.parseInt(args[1]));

        System.out.println("mean =          " + percStats.mean());
        System.out.println("stdDev =          " + percStats.stddev());
        System.out.println("95% confidence interval =          [" + percStats.confidenceLo() + "], [" + percStats
                .confidenceHi() + "]");
    }
}
