package com.company;

import java.util.ArrayList;

public class CellDTO {
    char cellType;
    ArrayList<CellSequence> cellSequences = new ArrayList<>();
    CellSequence mainCellSequences;

    CellDTO(TaskDTO task, char type, int r, int c) {
        this.cellType = type;
    }

}
