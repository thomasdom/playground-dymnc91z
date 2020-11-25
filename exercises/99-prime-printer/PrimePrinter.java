public class PrimePrinter {
    public static void main(String[] args) {
        // number of primes to print
        final int M = 1000;
        // lines/page
        final int RR = 50;
        // column count
        final int CC = 4;
        // The value of ord will never get larger than a certain value ordmax,
        // which must be chosen sufficiently large. It turns out that ord never
        // exceeds 30 when M = 1000.
        final int ORDMAX = 30;
        int P[] = new int[M + 1];
        int PAGENUMBER;
        int PAGEOFFSET;
        int ROWOFFSET;
        int C;
        int J;
        int K;
        boolean JPRIME;
        int ORD;
        int SQUARE;
        int N = 0;
        int MULT[] = new int[ORDMAX + 1];

        J = 1;
        K = 1;
        P[1] = 2;
        ORD = 2;
        SQUARE = 9;
        // generate primes
        while (K < M) {
            // find next prime number
            do {
                J += 2;
                if (J == SQUARE) {
                    ORD++;
                    SQUARE = P[ORD] * P[ORD];
                    MULT[ORD - 1] = J;
                }
                N = 2;
                JPRIME = true;
                while (N < ORD && JPRIME) {
                    while (MULT[N] < J)
                        MULT[N] += P[N] + P[N];
                    if (MULT[N] == J)
                        JPRIME = false;
                    N++;
                }
            } while (!JPRIME);
            K++;
            // save found prime number in array of primes
            P[K] = J;
        }
        // print numbers
        PAGENUMBER = 1;
        PAGEOFFSET = 1;
        while (PAGEOFFSET <= M) {
            System.out.print("The First ");
            System.out.print(Integer.toString(M));
            System.out.print(" Prime Numbers === Page ");
            System.out.print(Integer.toString(PAGENUMBER));
            System.out.println("\n");
            for (ROWOFFSET = PAGEOFFSET; ROWOFFSET <= PAGEOFFSET + RR - 1; ROWOFFSET++) {
                for (C = 0; C <= CC - 1; C++)
                    if (ROWOFFSET + C * RR <= M)
                        System.out.printf("%10d", P[ROWOFFSET + C * RR]);
                System.out.println();
            }
            System.out.println("\f");
            PAGENUMBER++;
            PAGEOFFSET += RR * CC;
        }
        // end of program
    }
}