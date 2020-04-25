package com.company;

public class Main {

    public static void main(String[] args) {
        Matrix aa,bb;
        aa = new Matrix(4, 4);
        bb = new Matrix(4, 1);

        aa.setRandomInt(1,9);
        bb.setRandomInt(1,5);

        Matrix b;

        b = aa.multiply(bb);

        aa.show();
        aa.makeLuDecomposition();

    }
}
