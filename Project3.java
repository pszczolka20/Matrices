/*
 * PROJECT III: Project3.java
 *
 * This file contains a template for the class Project3. None of methods are
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class, as 
 * well as GeneralMatrix and TriMatrix.
 *
 * Remember not to change the names, parameters or return types of any
 * variables in this file!
 *
 * The function of the methods and instance variables are outlined in the
 * comments directly above them.
 * 
 * Tasks:
 *
 * 1) Complete this class with the indicated methods and instance variables.
 *
 * 2) Fill in the following fields:
 *
 * NAME: Aleksandra Pasieka
 * UNIVERSITY ID: 2113104
 * DEPARTMENT: Mathematics
 */

public class Project3 {
    /**
     * Calculates the variance of the distribution defined by the determinant
     * of a random matrix. See the formulation for a detailed description.
     *
     * @param matrix      The matrix object that will be filled with random
     *                    samples.
     * @param nSamp       The number of samples to generate when calculating 
     *                    the variance. 
     * @return            The variance of the distribution.
     */
    public static double matVariance(Matrix matrix, int nSamp) {
        double sum1=0; //stores sums of square of determinants
        double sum2=0; //stoses sums of determinants
        double det;
        for(int i=0;i<nSamp;i++){
            matrix.random();
            det=matrix.determinant();
            sum1+=Math.pow(det,2);
            sum2+=det;
        }
        return sum1/nSamp-Math.pow(sum2/nSamp,2);
        // You need to fill in this method.


    }
    
    /**
     * This function should calculate the variances of matrices for matrices
     * of size 2 <= n <= 50 and print the results to the output. See the 
     * formulation for more detail.
     */
    public static void main(String[] args) {
        String a;
        String b;
        for(int n=2;n<51;n++){
            GeneralMatrix gen_matrix1=new GeneralMatrix(n,n);
            TriMatrix tri_matrix1=new TriMatrix(n);
            a=String.format("[%.15f]",matVariance(gen_matrix1,20000));
            b=String.format("[%.15f]",matVariance(tri_matrix1,200000));
            System.out.println(n+" "+a+" "+b);
        }
        // You need to fill in this method.
    }
}