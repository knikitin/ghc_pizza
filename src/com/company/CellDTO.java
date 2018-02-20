package com.company;

import java.util.ArrayList;

public class CellDTO {
    char cellType;
    ArrayList<CellSequence> cellSequences = new ArrayList<>();
    ArrayList<CellSequence> oldCellSequences;

    CellDTO(TaskDTO task, char type, int r, int c) {
        this.cellType = type;
        this.cellSequences.add(new CellSequence(task, r, c));
    }

}
