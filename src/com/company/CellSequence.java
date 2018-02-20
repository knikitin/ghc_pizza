package com.company;

public class CellSequence {
    TaskDTO task;
    int minR, maxR, minC, maxC;

    CellSequence(TaskDTO task, int r, int c){
        this.task = task;
        this.minR = r;
        this.maxR = r;
        this.minC = c;
        this.maxC = c;
    }

    CellSequence(TaskDTO task, int sr, int sc, int fr, int fc){
        this.task = task;
        this.minR = sr;
        this.maxR = fr;
        this.minC = sc;
        this.maxC = fc;
    }
}
