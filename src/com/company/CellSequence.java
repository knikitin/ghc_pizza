package com.company;

public class CellSequence {
    int minR, maxR, minC, maxC;

    CellSequence(int r, int c){
        this.minR = r;
        this.maxR = r;
        this.minC = c;
        this.maxC = c;
    }

    CellSequence(int sr, int sc, int fr, int fc){
        this.minR = sr;
        this.maxR = fr;
        this.minC = sc;
        this.maxC = fc;
    }
}
