/*
 * PROJECT III: GeneralMatrix.java
 *
 * This file contains a template for the class GeneralMatrix. Not all methods
 * implemented and they do not have placeholder return statements. Make sure 
 * you have carefully read the project formulation before starting to work 
 * on this file. You will also need to have completed the Matrix class.
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

import java.util.Arrays;
import java.util.Random;

public class GeneralMatrix extends Matrix {
    /**
     * This instance variable stores the elements of the matrix.
     */
    private double[][] values;

    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the data array.
     *
     * @param firstDim   The first dimension of the array.
     * @param secondDim  The second dimension of the array.
     */
    public GeneralMatrix(int firstDim, int secondDim) {
        super(firstDim,secondDim);
        if((iDim<1)||(jDim<1)){
            throw new MatrixException ("column and row size have to be greater than zero !!!");
        } 
        values=new double [iDim][jDim];
        // You need to fill in this method.
    }

    /**
     * Constructor function. This is a copy constructor; it should create a
     * copy of the second matrix.
     *
     * @param second  The matrix to create a copy of.
     */
    public GeneralMatrix(GeneralMatrix second) {
        super(second.iDim,second.iDim);
        if((second.iDim<1)||(second.jDim<1)){
            throw new MatrixException ("column and row size has to be greater than zero !!!");
        } 
        values=new double [second.iDim][second.jDim];
        for(int i=0;i<second.iDim;i++){
            for(int j=0;j<second.jDim;j++){
                this.values[i][j]=second.getIJ(i,j);
            }
        }
        // You need to fill in this method.
    }
    
    /**
     * Getter function: return the (i,j)'th entry of the matrix.
     *
     * @param i  The location in the first co-ordinate.
     * @param j  The location in the second co-ordinate.
     * @return   The (i,j)'th entry of the matrix.
     */
    public double getIJ(int i, int j) {
        if((i<0)||(j<0)||(i>iDim-1)||(j>iDim-1)){
            throw new MatrixException ("not valid entry!");
        }
        else{
            return values[i][j];
        }
        // You need to fill in this method.
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the values array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        if((i<0)||(j<0)||(i>iDim-1)||(j>iDim-1)){
            throw new MatrixException ("not valid entry!");
        }
        else{
            values[i][j]=value;
        }
        // You need to fill in this method.
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        double [] sign1=new double [1]; //an array we pass to LUdecomp
        double det=1;
        GeneralMatrix matrix=new GeneralMatrix(iDim,jDim);
        try{
            matrix=this.LUdecomp(sign1);
        } catch (MatrixException e){
            String error = e.getMessage();
            if (e.getMessage()=="Matrix is singular"){
                return 0.0;
                //System.out.println("singular!");
            }
        }
        //matrix=this.LUdecomp(sign1);
        for(int i=0;i<this.iDim;i++){
            //determinant of "LU" is the product of determinant of "L" and determinant of "U"  
            //"L" and "U" matrices are lower- and upper- traingular so their determinants are products of their diagonal enteries. 
            //Since l_ii=1 for all i, the determinant of "matrix" is the product of all u_ii, which are the diagonal enteries of "matrix".
            det*=matrix.getIJ(i,i);
        }
        return det*sign1[0];
        // You need to fill in this method.
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return   The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second) {
        if((this.iDim!=second.iDim)||(this.jDim!=second.jDim)){
            throw new MatrixException ("sizes don't match !!!");
        } 
        //number of rows and columns have to match

        GeneralMatrix added_matrix=new GeneralMatrix(second.iDim,second.jDim);
        for (int i=0; i< second.iDim; i++){
            for (int j=0;j<second.jDim; j++){
                added_matrix.setIJ(i,j,this.values[i][j]+second.getIJ(i,j));
            }
        }
        return added_matrix;
        // You need to fill in this method.
    }
    
    /**
     * Multiply the matrix by another matrix A. This is a _left_ product,
     * i.e. if this matrix is called B then it calculates the product BA.
     *
     * @param A  The Matrix to multiply by.
     * @return   The product of this matrix with the matrix A.
     */
    public Matrix multiply(Matrix A) {

        //number of columns of this matrix have to be equal to the number of rows of matrix A
        if(this.jDim!=A.iDim){
            throw new MatrixException ("sizes don't match !!!");
        } 
        GeneralMatrix multiplied_matrix=new GeneralMatrix(A.iDim,A.jDim);

        //variable that will be helpful for calculating scalar product
        double sum=0;

        //running through all entries in multiplied_matrix
        for(int i=0;i<this.iDim;i++){
            for(int j=0;j<A.jDim;j++){
                //the scalar product of a suitable row of this matrix and a suitable column of matrix A
                for(int r=0;r<this.jDim;r++){
                    sum+=this.values[i][r]*A.getIJ(r,j);
                }
                multiplied_matrix.setIJ(i,j,sum);
                sum=0;
            }
        }
        return multiplied_matrix;
        // You need to fill in this method.
    }

    /**
     * Multiply the matrix by a scalar.
     *
     * @param scalar  The scalar to multiply the matrix by.
     * @return        The product of this matrix with the scalar.
     */
    public Matrix multiply(double scalar) {
        GeneralMatrix multiplied_scalar=new GeneralMatrix(iDim,jDim);
        //running through all entries in the matrix
        for(int i=0;i<iDim;i++){
            for(int j=0;j<jDim;j++){
                multiplied_scalar.setIJ(i,j,scalar*this.values[i][j]);
            }
        }
        return multiplied_scalar;
        // You need to fill in this method.
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        Random r =	new Random();
        //running through all entries in the matrix
        for(int i=0;i<this.iDim;i++){
            for(int j=0;j<this.jDim;j++){
                this.setIJ(i,j,r.nextDouble());
            }
        }
        // You need to fill in this method.
    }

    /**
     * Returns the LU decomposition of this matrix; i.e. two matrices L and U
     * so that A = LU, where L is lower-diagonal and U is upper-diagonal.
     * 
     * On exit, decomp returns the two matrices in a single matrix by packing
     * both matrices as follows:
     *
     * [ u_11 u_12 u_13 u_14 ]
     * [ l_21 u_22 u_23 u_24 ]
     * [ l_31 l_32 u_33 u_34 ]
     * [ l_41 l_42 l_43 u_44 ]
     *
     * where u_ij are the elements of U and l_ij are the elements of l. When
     * calculating the determinant you will need to multiply by the value of
     * sign[0] calculated by the function.
     * 
     * If the matrix is singular, then the routine throws a MatrixException.
     * In this case the string from the exception's getMessage() will contain
     * "singular"
     *
     * This method is an adaptation of the one found in the book "Numerical
     * Recipies in C" (see online for more details).
     * 
     * @param sign  An array of length 1. On exit, the value contained in here
     *              will either be 1 or -1, which you can use to calculate the
     *              correct sign on the determinant.
     * @return      The LU decomposition of the matrix.
     */
    public GeneralMatrix LUdecomp(double[] sign) {
        // This method is complete. You should not even attempt to change it!!
        if (jDim != iDim)
            throw new MatrixException("Matrix is not square");
        if (sign.length != 1)
            throw new MatrixException("d should be of length 1");
        
        int           i, imax = -10, j, k; 
        double        big, dum, sum, temp;
        double[]      vv   = new double[jDim];
        GeneralMatrix a    = new GeneralMatrix(this);
        
        sign[0] = 1.0;
        
        for (i = 1; i <= jDim; i++) {
            big = 0.0;
            for (j = 1; j <= jDim; j++)
                if ((temp = Math.abs(a.values[i-1][j-1])) > big)
                    big = temp;
            if (big == 0.0)
                throw new MatrixException("Matrix is singular");
            vv[i-1] = 1.0/big;
        }
        
        for (j = 1; j <= jDim; j++) {
            for (i = 1; i < j; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < i; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
            }
            big = 0.0;
            for (i = j; i <= jDim; i++) {
                sum = a.values[i-1][j-1];
                for (k = 1; k < j; k++)
                    sum -= a.values[i-1][k-1]*a.values[k-1][j-1];
                a.values[i-1][j-1] = sum;
                if ((dum = vv[i-1]*Math.abs(sum)) >= big) {
                    big  = dum;
                    imax = i;
                }
            }
            if (j != imax) {
                for (k = 1; k <= jDim; k++) {
                    dum = a.values[imax-1][k-1];
                    a.values[imax-1][k-1] = a.values[j-1][k-1];
                    a.values[j-1][k-1] = dum;
                }
                sign[0] = -sign[0];
                vv[imax-1] = vv[j-1];
            }
            if (a.values[j-1][j-1] == 0.0)
                a.values[j-1][j-1] = 1.0e-20;
            if (j != jDim) {
                dum = 1.0/a.values[j-1][j-1];
                for (i = j+1; i <= jDim; i++)
                    a.values[i-1][j-1] *= dum;
            }
        }
        
        return a;
    }

    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        GeneralMatrix matrix1=new GeneralMatrix(4,4);
        matrix1.random();
        System.out.println(matrix1.toString());
        System.out.println(matrix1.determinant());
        // Test your class implementation using this method.

        Matrix a = new GeneralMatrix(2,2);
		a.setIJ(0,0,5);
		a.setIJ(0,1,2);
		a.setIJ(1,0,-1);
		a.setIJ(1,1,2);

        //checking getIJ
        System.out.println(a.getIJ(0,1));
		Matrix b = new GeneralMatrix(2,2);
		b.setIJ(0,0,3);
		b.setIJ(0,1,1);
		b.setIJ(1,0,-2);
		b.setIJ(1,1,1);

        Matrix c = new GeneralMatrix(4,4);
		c.setIJ(0,0,1);
		c.setIJ(0,1,0);
		c.setIJ(0,2,0);
		c.setIJ(0,3,0);
        c.setIJ(1,0,0);
        c.setIJ(1,1,1);
        c.setIJ(1,2,0);
        c.setIJ(1,3,0);
        c.setIJ(2,0,0);
        c.setIJ(2,1,1);
        c.setIJ(2,2,1);
        c.setIJ(2,3,0);
        c.setIJ(3,0,0);
        c.setIJ(3,1,0);
        c.setIJ(3,2,0);
        c.setIJ(3,3,1);

        System.out.println("det of c: "+c.determinant());
        System.out.println("matrix a: "+"\n"+a.toString());
        System.out.println("matrix b: "+"\n"+b.toString());
        System.out.println("a+b: "+"\n"+a.add(b).toString());
        System.out.println("ab: "+"\n"+a.multiply(b).toString());
        System.out.println("ba: "+"\n"+b.multiply(a).toString());
        System.out.println("3a: "+"\n"+a.multiply(3).toString());




       
    }
}