package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

public class Main {

    public static int Length;
    public static int min;
    public static int max;
    public static int[] A;
    public static int[][] ThreeSets;

    public static void main(String[] args) {


        //A = GenerateTestList(Length, min, max);
        //PrintList(A);
        //ThreeSumBrute(A);
        //ThreeSumFaster(A);
        //ThreeSumFastest(A);


        TestAlgorithms();
        PrintList(ThreeSets);
    }

    public static void TestAlgorithms(){
        float DoublingRatio = 0;
        long TimeBefore = 0;
        long TimeAfter = 0;
        long TotalTime = 1;
        long LastTime = 0;

        min = 0;
        for(Length = 40; Length != 80; Length=Length*2){
            ThreeSets =  new int[Length*(Length)][3];
            A = new int[Length];
            min = min - (Length*4);
            max = max + (Length*4);
            A = GenerateTestList(Length, min, max);
            TimeBefore = getCpuTime();
            ThreeSumBrute(A);
            TimeAfter = getCpuTime();
            LastTime = TotalTime;
            TotalTime = TimeAfter - TimeBefore;
            DoublingRatio = (float)TotalTime / (float)LastTime;

            //System.out.println(Length + "  " + TotalTime + "  " + DoublingRatio);

        }


    }

    public static void ThreeSumFastest (int[] A){
        int a = 0;

        MergeSort(A,0,A.length-1);          //first sort the list so we can use binary search
        PrintList(A);

        int i,j,k = 0;

        /* some concepts for this particular algorithm were observed from the source: https://fizzbuzzed.com/top-interview-questions-1/ */

        for(i=0; i<A.length; i++){
            if(i > 0 && A[i] == A[i-1])             //this helps avoid duplicate triplets by not using the same A[i] value twice
                continue;
            j = i+1;                                //point j at the beginning of possible values
            k = A.length - 1;                       //point k at the end of possible values

            while(k>j){
                if(A[i]+A[j]+A[k] == 0 && (A[i] != A[j]) && (A[i] != A[k]) && (A[j] != A[k])){
                    ThreeSets[a][0] = A[i];
                    ThreeSets[a][1] = A[j];         //if the three values add up to 0 and they are all unique, put them into the array
                    ThreeSets[a][2] = A[k];
                    a++;                            //increment a so the next triplet has its own spot in the array
                    j++;                            //increment the "2nd value" so we can try to find the next triplet that works for i
                    while (k>j && A[j] == A[j-1])
                        j++;                        //this helps avoid duplicate triplets by not using the same A[j] value twice
                }
                else if(A[i]+A[j]+A[k] > 0)         //if the addition of the 3 values is greater than zero, we need a smaller 3rd number
                    k--;
                else
                    j++;                            //if the addition of the 3 values is less than zero, we need a bigger 2nd number
            }
        }
    }

    public static void ThreeSumFaster (int[] A){
        int a = 0;

        MergeSort(A,0,A.length-1);              //first sort the list so we can use binary search
        PrintList(A);

        int i,j,k = 0;

        for(i=0; i<A.length-1; i++) {
            if (i > 0 && A[i] == A[i - 1])          //this helps avoid duplicate triplets by not using the same A[i] value twice
                continue;

            for (j = i+1; j < A.length; j++) {
                if (j > 1 && A[j] == A[j - 1])         //this helps avoid duplicate triplets by not using the same A[j] value twice
                    continue;
                k = k - (A[i] + A[j]);                      //calculate what value would zero out the other 2 values.
                k = BinarySearch(A, k, Length);             //search the list to see if that calculated value exists

                if (k >= 0) {                               //binary search will return -1 if the value was not found
                    if ((A[i] != A[j]) && (A[i] != A[k]) && (A[j] != A[k])) {
                        ThreeSets[a][0] = A[i];
                        ThreeSets[a][1] = A[j];         //if they are all unique, put them into the array
                        ThreeSets[a][2] = A[k];
                        a++;                            //increment a so the next triplet has its own spot in the array
                    }
                }
            }
        }
    }


    public static void ThreeSumBrute (int[] A){
        int a = 0;

        PrintList(A);

        int i,j,k;

        // This is a brute force algorithm that simply looks at every single possible combination one at a time

        for(i=0; i<A.length-2; i++) {
            if (i > 0 && A[i] == A[i - 1])          //this helps avoid duplicate triplets by not using the same A[i] value twice
                continue;

            for (j = i+1; j < A.length - 1; j++) {
                if (j > 1 && A[j] == A[j - 1])         //this helps avoid duplicate triplets by not using the same A[j] value twice
                    continue;

                for (k = j+1; k < A.length; k++) {

                    if (A[i] + A[j] + A[k] == 0 && (A[i] != A[j]) && (A[i] != A[k]) && (A[j] != A[k])) {
                        ThreeSets[a][0] = A[i];
                        ThreeSets[a][1] = A[j];         //if the three values add up to 0 and they are all unique, put them into the array
                        ThreeSets[a][2] = A[k];
                        a++;                            //increment a so the next triplet has its own spot in the array
                    }

                }
            }
        }
    }

    public static int BinarySearch(int[] A, int key, int Length){
        int index = -1;
        int middle = (Length-1)/2;
        int top = Length-1;
        int bottom = 0;
        int temp = 0;


        while( middle != top-1) {
            if (A[middle] == key)
                return middle;
            else if (A[middle] > key) {
                top = middle;
                middle = (bottom+top) / 2;
            } else {
                temp = middle;
                middle = (top + middle) / 2;
                bottom = temp;
            }
        }

        return index;
    }

    public static int[] GenerateTestList(int Length, int min, int max){

        int[] A = new int[Length];
        int i;
        Random rand = new Random();
        for(i = 0; i < Length; i++){
            A[i] = rand.nextInt(max - min + 1) + min;
            // System.out.println(A[i]);
        }

        return A;
    }

    public static void PrintList(int[] A){
        int i;
        int z = A.length;
           for (i=0; i < z; i++) {

            System.out.print(A[i]+" ");
        }
        System.out.println();

    }

    public static void PrintList(int[][] A){
        int i,j;
        int z = A.length;
        for (i=0; i < z; i++) {
            if(A[i][0] != 0) {
                for (j = 0; j < 3; j++)
                    System.out.print(A[i][j] + " ");
                System.out.println();
            }
        }

        System.out.println();

    }

    public static void MergeSort(int[] List, int l, int r){
        // referenced geeksforgeeks.org for parts of this algorithm

        if (l < r){                                 //ensures the list can be split into 2
            int m = (l+r)/2;                        // finds the middle point of the list

            MergeSort(List, l, m);                  //recursively calls itself using the first half of the list
            MergeSort(List, m+1, r);             //recursively calls itself using the second half of the list

            Merge(List,l,m,r);                      //merges the List on this particular recursive call
        }
    }

    public static void Merge(int[] List, int l, int m, int r){

        int LSize = m - l + 1;                      //size of left half
        int RSize = r - m;                          //size of right half

        int L[] = new int[LSize];             //creates string arrays of those sizes
        int R[] = new int[RSize];

        System.arraycopy(List,l, L, 0, LSize);          //copies left half of main string array
        System.arraycopy(List,m+1,R,0,RSize);    //copies right half of main string array

        int i = 0;
        int j = 0;
        int k = l;

        while (i < LSize && j < RSize){
            if(L[i] <= R[j]){                 //if int in L is less than or equal to int in R, copy L int to List array
                List[k] = L[i];
                i++;
            }
            else{
                List[k] = R[j];                             //R int is less, so copy it to List array
                j++;
            }
            k++;
        }

        while (i<LSize){                                    //grab any extra ints for odd numbered arrays
            List[k] = L[i];
            i++;
            k++;
        }

        while (j<RSize){
            List[k] = R[j];
            j++;
            k++;
        }
    }

    // Get CPU time in nanoseconds since the program(thread) started.
    /** from: http://nadeausoftware.com/articles/2008/03/java_tip_how_get_cpu_and_user_time_benchmarking#TimingasinglethreadedtaskusingCPUsystemandusertime **/
    public static long getCpuTime( ) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean( );
        return bean.isCurrentThreadCpuTimeSupported( ) ?
                bean.getCurrentThreadCpuTime( ) : 0L;
    }
}
