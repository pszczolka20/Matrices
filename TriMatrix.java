/*
 * PROJECT III: TriMatrix.java
 *
 * This file contains a template for the class TriMatrix. Not all methods are
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

public class TriMatrix extends Matrix {
    /**
     * An array holding the diagonal elements of the matrix.
     */
    private double[] diagonal;

    /**
     * An array holding the upper-diagonal elements of the matrix.
     */
    private double[] upperDiagonal;

    /**
     * An array holding the lower-diagonal elements of the matrix.
     */
    private double[] lowerDiagonal;
    
    /**
     * Constructor function: should initialise iDim and jDim through the Matrix
     * constructor and set up the values array.
     *
     * @param dimension  The dimension of the array.
     */
    public TriMatrix(int dimension) {
        super(dimension,dimension);
        if((iDim<1)||(jDim<1)){
            throw new MatrixException ("the dimension has to be bigger than 0 !!!");
        } 
        diagonal=new double[iDim];
        upperDiagonal=new double[iDim-1];
        lowerDiagonal=new double[iDim-1];
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
            throw new MatrixException ("invalid entries!");
        }
        if(i==j){
            return diagonal[i];
        }
        else if(i==j+1){
            return lowerDiagonal[j];
        }
        else if(i+1==j){
            return upperDiagonal[i];
        }
        else{
            return 0;
        }
        // You need to fill in this method.
    }
    
    /**
     * Setter function: set the (i,j)'th entry of the data array.
     *
     * @param i      The location in the first co-ordinate.
     * @param j      The location in the second co-ordinate.
     * @param value  The value to set the (i,j)'th entry to.
     */
    public void setIJ(int i, int j, double value) {
        if((i<0)||(j<0)||(i>iDim-1)||(j>iDim-1)){
            throw new MatrixException ("invalid entries!");
        }
        if(i==j){
            diagonal[i]=value;
        }
        else if(i==j+1){
            lowerDiagonal[j]=value;
        }
        else if(i+1==j){
            upperDiagonal[i]=value;
        }
        else{
            throw new MatrixException ("this value can't be set in a tri-diagonal matrix !");
        }
        // You need to fill in this method.
    }
    
    /**
     * Return the determinant of this matrix.
     *
     * @return The determinant of the matrix.
     */
    public double determinant() {
        TriMatrix det = LUdecomp();
        double d=1;
        for(int i=0;i<iDim;i++){
            //determinant of "LU" is the product of determinant of "L" and determinant of "U"
            //matrices "L" and "U" are lower- and upper diagonal so their determinants are the products of their diagonal entries
            //since diagonal entries in "L" are all equal to 1, the determinant is the product of all d_ii, which are the diagonal enteries of "U"
            d*=det.getIJ(i,i);
        }
        return d;
        // You need to fill in this method.
    }
    
    /**
     * Returns the LU decomposition of this matrix. See the formulation for a
     * more detailed description.
     * 
     * @return The LU decomposition of this matrix.
     */
    public TriMatrix LUdecomp() {
        TriMatrix LU = new TriMatrix (iDim);
        LU.setIJ(0,0,diagonal[0]);
        //the difference relation is:
        //u_m^=u_m
        //d_1^=d_1
        //l_m^=l_m/d_(m-1)^
        //d_m^=d_m-(l_m*u_(m-1)^)
        //where ^ is the corresponding entry in LU decomposition
        for(int i=0;i<iDim;i++){
            if(i>0){
                LU.setIJ(i-1,i,upperDiagonal[i-1]);
                LU.setIJ(i,i,diagonal[i]-LU.getIJ(i,i-1)*upperDiagonal[i-1]);
            }
            if((i<iDim-1)){
                LU.setIJ(i+1,i,lowerDiagonal[i]/LU.getIJ(i,i));
            }
        }
        return LU;

        // You need to fill in this method.
    }

    /**
     * Add the matrix to another second matrix.
     *
     * @param second  The Matrix to add to this matrix.
     * @return        The sum of this matrix with the second matrix.
     */
    public Matrix add(Matrix second){
        //dimension have to be equal
        if ((second.iDim!=iDim)||(second.jDim!=jDim)){
            throw new MatrixException ("sizes don't match !!!");
        }
        //create two GeneralMatrices that we will use as arguments
        GeneralMatrix a= new GeneralMatrix(iDim,iDim);
        GeneralMatrix b= new GeneralMatrix(iDim,iDim);
        //running through all entries
        for (int i=0;i<iDim;i++){
            for(int j=0;j<iDim;j++){
                a.setIJ(i,j,this.getIJ(i,j));
                b.setIJ(i,j,second.getIJ(i,j));
            }
        }
        //add using addition in GenralMatrix
        return a.add(b);
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
        //matrices must have the same dimension
        if(A.iDim!=iDim){
            throw new MatrixException ("sizes don't match !!!");
        }
        //create two GeneralMatrices that will store matrices 
        GeneralMatrix a= new GeneralMatrix(iDim,iDim);
        GeneralMatrix b= new GeneralMatrix(iDim,iDim);
        //running through all entries
        for (int i=0;i<iDim;i++){
            for(int j=0;j<iDim;j++){
                a.setIJ(i,j,this.getIJ(i,j));
                b.setIJ(i,j,A.getIJ(i,j));
            }
        }
        //multiplication using multiplication in GenralMatrix
        return a.multiply(b);
        // You need to fill in this method.
    }
    
    /**
     * Multiply the matrix by a scalar.
     *
     * @param scalar  The scalar to multiply the matrix by.
     * @return        The product of this matrix with the scalar.
     */
    public Matrix multiply(double scalar) {
        TriMatrix multiplied = new TriMatrix (iDim);
        for(int i=0;i<iDim;i++){
            //diagonal entries
            multiplied.setIJ(i,i,diagonal[i]*scalar);
            if(i<iDim-1){
                //upper diagonal entries
                multiplied.setIJ(i,i+1,upperDiagonal[i]*scalar);
            }
            if(i>0){
                //lower diagonal entries
                multiplied.setIJ(i,i-1,lowerDiagonal[i-1]*scalar);
            }
        }
        return multiplied;
        // You need to fill in this method.
    }

    /**
     * Populates the matrix with random numbers which are uniformly
     * distributed between 0 and 1.
     */
    public void random() {
        for(int i=0;i<iDim;i++){
            //diagonal entries
            diagonal[i]=Math.random();
            if(i<iDim-1){
                //upper diagonal entries
                upperDiagonal[i]=Math.random();
            }
            if(i>0){
                //lower diagonal entries
                lowerDiagonal[i-1]=Math.random();
            }
        }
        // You need to fill in this method.
    }
    
    /*
     * Your tester function should go here.
     */
    public static void main(String[] args) {
        Matrix a = new TriMatrix(4);
        a.setIJ(0,0,1);
		a.setIJ(0,1,2);
		a.setIJ(1,0,3);
		a.setIJ(1,1,4);
        a.setIJ(1,2,5);
		a.setIJ(2,1,6);
		a.setIJ(2,2,7);
		a.setIJ(2,3,8);
        a.setIJ(3,2,10);
        a.setIJ(3,3,1);
       
        Matrix b = new TriMatrix(3);
		b.setIJ(0,0,5);
		b.setIJ(0,1,7);
		b.setIJ(1,0,8);
		b.setIJ(1,1,2);
		b.setIJ(1,2,6);
		b.setIJ(2,1,11);
		b.setIJ(2,2,2);	

        Matrix c = new TriMatrix(3);
		c.setIJ(0,0,1);
		c.setIJ(0,1,2);
		c.setIJ(1,0,3);
		c.setIJ(1,1,4);
		c.setIJ(1,2,5);
		c.setIJ(2,1,6);
		c.setIJ(2,2,7);	

        Matrix d = new TriMatrix(4);
        d.random();
        System.out.println("d: \n"+d.toString());	

        Matrix c1 = new GeneralMatrix(4,4);
		c1.random();
        System.out.println("c1: \n"+c1.toString());
        System.out.println("d+c1: \n"+d.add(c1));
        System.out.println("c1*d: \n"+c1.multiply(d));
        Matrix e = new TriMatrix(3);
        e.random();

        Matrix f = new TriMatrix(3);
        f.random();
		System.out.println("(1,1) element of a is: "+a.getIJ(1,1));
		System.out.println("a: \n"+a.toString());
		System.out.println("b: \n"+b.toString());
        System.out.println("c: \n"+c.toString());
		System.out.println("Determinant of a: "+a.determinant()+"\n");
		System.out.println("Determinant of b: "+b.determinant()+"\n");
        System.out.println("Determinant of c: "+c.determinant()+"\n");
        System.out.println("Determinant of d: "+d.determinant()+"\n");
        System.out.println("b+c : \n"+b.add(c).toString()+"\n");
        System.out.println("a+d : \n"+a.add(d).toString()+"\n");
        System.out.println("3*b : \n"+b.multiply(3).toString()+"\n");
        System.out.println("2*d : \n"+d.multiply(2).toString()+"\n");
        System.out.println("b*c : \n"+b.multiply(c).toString()+"\n");
        System.out.println("e: \n"+e.toString());
        System.out.println("f: \n"+f.toString());
        System.out.println("e*f : \n"+e.multiply(f).toString()+"\n");
        // Test your class implementation using this method.
    }
}