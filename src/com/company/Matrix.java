package com.company;

import java.io.InputStream;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.function.DoubleBinaryOperator;


public class Matrix {

    static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    private Vector<String> columnNames;
    private Vector<Vector<Number>> array;

    private String columnBaseName;
    private int id;

    private int height;
    private int width;
    private Matrix lower;
    private Matrix upper;

    public Matrix(){
        this(0, 0);
    }


    public Matrix(int m, int n){
        height = m;
        width = n;
        array = new Vector<>();

        columnBaseName = "x";
        columnNames = new Vector<>();
        nameAllColumns();

        for(int i = 0; i < height; i++){
            array.add(new Vector<>());
            for(int j = 0; j < width; j++){
                array.get(i).add(0.0);
            }
        }

        lower = null;
        upper = null;

        id = 0;
    }

    public static Matrix getIdentityMatrix(int n){
        Matrix diagonal = new Matrix(n, n);
        int i = 0;
        while(i < n){
            diagonal.set(1, i, i);
            i++;
        }

        return diagonal;
    }

    public static Matrix getDiagonalMatrix(Number ... args){
        Matrix diagonal = new Matrix(args.length, args.length);
        int i = 0;
        for(Number value: args){
            diagonal.set(value, i, i);
            i++;
        }

        return diagonal;
    }

    public void setColumnBaseName(String name){
        columnBaseName = name;
        nameAllColumns();
    }

    public void nameAllColumns(){
        columnNames.clear();
        for(int i = 0; i < width; i++) {
            columnNames.add(nameColumn(i));
        }
    }

    public String nameColumn(int col){
        String format = String.format("%%0%dd", (int)Math.log10(width) + 2);
        String colName = columnBaseName + "_" + String.format(format, col + 1);
        return colName;
    }

    public void setColumnNames(Vector<String> names){
        if(names.size() >= width){
            int i =0;
            for(String name: names){
                this.columnNames.set(i, name);
                i++;
            }
        }
    }

    public Vector<String> getColumnNames(){
        return columnNames;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public void setDimention(int m, int n){
        array.clear();
        fillWith(0);

    }

    public void setMatrix(){
        setMatrix(System.in);
    }

    public void setMatrix(InputStream is){
        Scanner in = new Scanner(is);

        show();
        for(int i = 0; i < this.getHeight(); i++){
            for(int j = 0; j < this.getWidth(); j++){
                System.out.format("Podaj liczbę w (%d,%d)\n", i+1, j+1);
                this.set(in.nextDouble(),i, j);
                this.show();
            }
        }
    }

    public void fillWith(Number value){
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                set(value, i, j);
            }
        }
    }

    public void set(Number elem, int m, int n){
        array.get(m).set(n, elem);
    }

    public Double get(int m, int n){
        return array.get(m).get(n).doubleValue();
    }

    public void multiply(Number mult, int m, int n){
        set(get(m,n)*mult.doubleValue(), m, n);
    }

    public Matrix add(Matrix m){
        if(width == m.getWidth() && height == m.getHeight()){
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width; j++){
                    add(m.get(i,j), i, j);
                }
            }
        }
        return this;
    }

    public Matrix substract(Matrix m){
        if(width == m.getWidth() && height == m.getHeight()){
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width ; j++){
                    add(-m.get(i,j), i, j);
                }
            }
        }
        return this;
    }

    public Matrix multiply(Matrix m){
        Matrix newMatrix = new Matrix(height, m.getWidth());
        if(width == m.getHeight()){
            double value;
            for(int i = 0; i < height; i++){
                for(int j = 0; j < m.getWidth(); j++){
                    value = 0;
                    for(int k = 0; k < width; k++){
                        value += get(i,k)*m.get(k, j);
                    }
                    newMatrix.set(value, i, j);
                }
            }
        }

        return newMatrix;
    }

    public void add(Number toAdd, int m, int n){
        set(get(m,n) + toAdd.doubleValue(), m, n);
    }

    public void addRow(int p, int q){
        addRow(p,q,1);
    }

    public void addColumn(int p, int q){
        addColumn(p, q, 1.0);
    }

    public void addRow(int p, int q, Number mult ){
        for(int i = 0; i < width; i++){
            add(get(q, i)*mult.doubleValue(), p, i);
        }
    }

    public void addColumn(int p, int q, Number mult){
        for(int i = 0; i < height; i++){
            add(get(i, q)*mult.doubleValue(), i, p);
        }
    }

    public void multiplyRow(Number mult, int p){
        for(int i = 0; i < width; i++){
            multiply(mult, p, i);
        }
    }

    public void multiplyColumn(Number mult, int q){
        for(int i = 0; i < height; i++){
            multiply(mult, i, q);
        }
    }

    public void multiply(Number mult){
        for(int i = 0; i < height; i++){
            multiplyRow(mult, i);
        }
    }

    public void replaceRows(int m, int n){
        if(m == n){
            return;
        }

        Vector<Number> row1 = array.get(n);
        array.set(n, array.get(m));
        array.set(m, row1);
    }

    public void replaceColumns(int m, int n){
        if(m == n){
            return;
        }

        for(int i = 0; i < height; i++){
            Number temp = get(i, m);
            set(get(i, n), i, m);
            set(temp, i, n);
        }

        String mName = columnNames.get(m);
        columnNames.set(m, columnNames.get(n));
        columnNames.set(n, mName);
    }

    public Vector<Number> getRow(int p){
        return array.get(p);
    }

    public Vector<Number> getColumn(int q){
        Vector<Number> col = new Vector<>();
        for(int i = 0; i < height; i++){
            col.add(get(i, q));
        }
        return col;
    }

    public Matrix transpose(){
        Matrix transposed = new Matrix(getWidth(), getHeight());

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                transposed.set(get(i, j), j, i);
            }
        }

        return transposed;
    }

    //test
    public Matrix makeLuDecomposition(){
        Matrix l = new Matrix(height, width);
        Matrix u = new Matrix(height, width);
        double sum;
        for (int i = 0; i < height; i++)
        {
            for (int j = i; j < width; j++) {
                sum = 0;
                for(int k = 0; k < i; k++){
                    sum += l.get(i, k) * u.get(k, j);
                }
                u.set( get(i, j) - sum, i, j);
            }

            for(int j = i + 1; j < width; j++){
                sum = 0;
                for(int k = 0; k < i; k++){
                    sum += l.get(j,k)*u.get(k, i);
                }
                l.set((1/u.get(i,i)) * (get(j,i) -sum), j, i);
            }

        }

        lower = l.add(getIdentityMatrix(l.getHeight()));
        upper = u;

        return l.add(u);
    }


    public void adjugateRows(Matrix m){
        if(width == m.getWidth()){
            for(int i = 0; i < m.getHeight(); i++){
                array.add(m.getRow(i));
            }
            height += m.getHeight();
        }
    }

    public void adjugateColumns(Matrix m){
        if(height == m.getHeight()){
            for(int i = height; i < height + m.getWidth(); i++){
                columnNames.add(nameColumn(i));
            }

            for(int i = 0; i < m.getHeight(); i++){
                for(int j = 0; j < m.getWidth(); j++){
                    array.get(i).add(m.get(i, j));
                }
            }
            width += m.getWidth();
        }
    }

    public Vector<Matrix> gaussianElimination(){
        return gaussianElimination(null);
    }

    public Vector<Matrix> gaussianElimination(Matrix adjugate){
        Vector<Matrix> result = new Vector<>();

        Matrix m = this.copy();
        Matrix b;
        if(adjugate != null) {
            b = adjugate.copy();
        } else {
            b = new Matrix(height, 1);
            b.setId(-1);
        }

        if(m.getHeight() == b.getHeight()){
            int biggest, height = m.getHeight();
            double coeff1, coeff2;
            for(int i = 0; i < height; i++){
                biggest = m.getMaxIndexAbsoluteInRow(i);
                m.replaceColumns(i, biggest);
                coeff1 = m.get(i,i);
                for(int j = i + 1; j < height; j++){
                    coeff2 = m.get(j, i);
                    m.addRow(j, i, -coeff2/coeff1);
                    if(b.getId() != -1)
                        b.addRow(j, i, -coeff2/coeff1);
                }
            }

        }

        result.add(m);
        if(b.getId() != -1){
            result.add(b);
        }

        return result;
    }

    public Double det(){
        if(height == width){
            double determinant = 1;
            Matrix triangular = this.gaussianElimination().firstElement();

            for(int i = 0; i < triangular.getHeight(); i++){
                determinant *= triangular.get(i, i);
            }

            return determinant;
        } else {
            return 0.0;
        }
    }

    public void setRandomInt(){
        setRandomInt(-10, 10);
    }

    public void setRandomInt(int min, int max){
        Random rand = new Random();
        double value;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                value = rand.nextInt(max - min + 1) + min;
                set(value,i,j);
            }
        }
    }

    public void setRandom(){
        setRandom(-1, 1);
    }

    public void setRandom(int min, int max){
        Random rand = new Random();
        double value;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                value = rand.nextDouble()*(max-min) + min;;
                set(value,i,j);
            }
        }
    }


    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public Integer getIndexInRowOnCondition(int p, DoubleBinaryOperator op){
        Integer best = 0;

        for(int i = 0; i < width; i++){
            if(op.applyAsDouble(get(p, best), get(p, i)) < 0){
                best = i;
            }
        }
        return best;
    }

    public Integer getIndexInColumnOnCondition(int q, DoubleBinaryOperator op){
        Integer best = 0;

        for(int i = 0; i < height; i++){
            if(op.applyAsDouble(get(best, q), get(i, q)) < 0){
                best = i;
            }
        }
        return best;
    }

    public Integer getMaxIndexInRow(int p){
        return getIndexInRowOnCondition(p, (a, b) -> a-b);
    }

    public Integer getMaxIndexInColumn(int q){
        return getIndexInColumnOnCondition(q, (a, b) -> a-b);
    }

    public Integer getMinIndexInRow(int p){
        return getIndexInRowOnCondition(p, (a, b) -> b-a);
    }

    public Integer getMinIndexInColumn(int q){
        return getIndexInColumnOnCondition(q, (a, b) -> b-a);
    }

    public Integer getMaxIndexAbsoluteInRow(int p ){
        return getIndexInRowOnCondition(p, (a, b) -> Math.abs(a) - Math.abs(b));
    }

    public Integer getMaxIndexAbsoluteInColumn(int q ){
        return getIndexInColumnOnCondition(q, (a, b) -> Math.abs(a) - Math.abs(b));
    }

    public void sortByColumns(){
        int index;
        String temp;
        for(int i = 0; i < width; i++){
            for(int j = i; j < width; j++){
                if(columnNames.get(i).compareTo(columnNames.get(j)) > 0){
                    replaceColumns(i, j);
                }
            }
        }
    }

    private int getSmallestIndex(Vector<Integer> vec, int min, int max){
        int smallest = 0;
        for(int i = min; i < max; i++){
            if(vec.get(i) < vec.get(smallest)){
                smallest = i;
            }
        }
        return smallest;
    }

    public void show(){
        System.out.print(this);
    }

    public String toString(){
        String matrixString = "|";
        for(int i = 0; i < width; i++){
            matrixString += columnNames.get(i) + "|";
        }

        matrixString += "\n";

        for(int i = 0; i < height; i++){
            matrixString += "|";
            for(int j = 0; j < width; j++){
                matrixString += get(i, j) + "|";
            }
            matrixString += "\n";
        }
        matrixString += "\n";

        return matrixString;
    }

    public Matrix copy(){
        Matrix newMatrix = new Matrix(height, width);
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                newMatrix.set(get(i, j), i, j);
            }
        }
        return newMatrix;
    }

    public boolean equals(Matrix m){
        if(height != m.getHeight() || width != m.getWidth()){
            return false;
        }

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(!get(i, j).equals(m.get(i, j))){
                    return false;
                }
            }
        }

        return true;
    }

}
