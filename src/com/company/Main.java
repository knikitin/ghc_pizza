package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

public class Main {

    private static void fillTaskHead(TaskDTO task, String head) {
        Integer[] numbers = Arrays.stream(head.split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
        task.r = numbers[0];
        task.c = numbers[1];
        task.l = numbers[2];
        task.h = numbers[3];
    }

    private static TaskDTO loadFile() {
        TaskDTO task = new TaskDTO();

        String fileName = "C:\\Work\\Java\\GoogleHashCode2018\\Task\\small.in";
//        String fileName = "C:\\Work\\Java\\GoogleHashCode2018\\Task\\medium.in";
//        String fileName = "C:\\Work\\Java\\GoogleHashCode2018\\Task\\big.in";

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            String[] fileContent = stream.toArray(String[]::new);
            fillTaskHead(task, fileContent[0]);
            task.pizza = Arrays.copyOfRange(fileContent, 1, fileContent.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        task.cells = new CellDTO[task.r][task.c];
        for (int i = 0; i < task.r; i++) {
            char[] curStr = task.pizza[i].toCharArray();
            for (int j = 0; j < task.c; j++) {
                task.cells[i][j] = new CellDTO(task, curStr[j], i, j);
            }
        }

        return task;
    }

    private static  void nextTurnPreparing(TaskDTO task) {
        for (int i = 0; i < task.r; i++) {
            for (int j = 0; j < task.c; j++) {
                task.cells[i][j].oldCellSequences = task.cells[i][j].cellSequences;
                task.cells[i][j].cellSequences = new ArrayList<>();
            }
        }
    }

    private static boolean checkBorder(TaskDTO task, int r, int c){
        return (r>0) && ( c > 0) && (r <= task.r) && (c <= task.c);
    }

    private static CellSequence tryConcatRectangles(TaskDTO task, CellSequence firstS, CellSequence secondS){
        int minR = min(firstS.minR, secondS.minR);
        int minC = min(firstS.minC, secondS.minC);
        int maxR = max(firstS.maxR, secondS.maxR);
        int maxC = max(firstS.maxC, secondS.maxC);
        int s = (maxR- minR +1) * (maxC- minC +1);
        if (s <= task.h) {
            return new CellSequence(task, minR, minC, maxR, maxC);
        }
        return null;
    }

    private static void copySequence(TaskDTO task, int sr, int sc, int fr, int fc){
        // check border
        if (checkBorder(task, fr, fc)) {
            // check type
            if (task.cells[sr][sc].cellType == task.cells[fr][fc].cellType) {
                // copy all sequences to the type
                for (CellSequence sequence : task.cells[sr][sc].oldCellSequences) {
                    // Check before copying size of sequence
                    CellSequence newSequence = tryConcatRectangles(task, sequence, new CellSequence(task, fr,fc));
                    if (newSequence == null) {
                    } else {
                        task.cells[fr][fc].cellSequences.add(newSequence);
                    }
                }
            }
        }
    }

    private static void sequenceNextStep(TaskDTO task, int r, int c){
        // cycle by first line
        for (int j = 1; j < task.h; j++ ){
            copySequence(task, r, c, r, c+j);
        }
        // cycle by diapason
        for (int i = 1; i < task.h; i++ ){
            int dj = task.h / (i+1);
            dj = dj - 1;
            for (int j = -dj; j < dj + 1; j++ ){
                copySequence(task, r, c, r+i, c+j);
            }
        }
    }

    private static void loopForSequences(TaskDTO task){
        for (int t = 1; t< task.l; t++) {
            nextTurnPreparing(task);
            for (int i = 0; i < task.r; i++) {
                for (int j = 0; j < task.c; j++) {
                    sequenceNextStep(task, i,j);
                }
            }
        }
    }

    private static void checkPartners(TaskDTO task, int sr, int sc, int fr, int fc){
    // try to merge all sequence of the cells
        if (task.cells[sr][sc].cellType != task.cells[fr][fc].cellType) {
            for (CellSequence sequenceS : task.cells[sr][sc].oldCellSequences) {
                for (CellSequence sequenceF : task.cells[fr][fc].oldCellSequences) {
                    // Check before copying size of sequence
                    CellSequence newSequence = tryConcatRectangles(task, sequenceS, sequenceF);
                    if (newSequence == null) {
                    } else {
                        task.cells[newSequence.minR][newSequence.minC].cellSequences.add(newSequence);
                    }
                }
            }
        }
    }


    private static void findMergeForCell(TaskDTO task, int r, int c){
        // cycle by first line
        for (int j = 1; j < task.h; j++ ){
            checkPartners(task, r, c, r, c+j);
        }
        // cycle by diapason
        for (int i = 1; i < task.h; i++ ){
            int dj = task.h / (i+1);
            dj = dj - 1;
            for (int j = -dj; j < dj + 1; j++ ){
                checkPartners(task, r, c, r+i, c+j);
            }
        }
    }

    private static void loopForMerge(TaskDTO task){
        nextTurnPreparing(task);
        for (int i = 0; i < task.r; i++) {
            for (int j = 0; j < task.c; j++) {
                findMergeForCell(task, i,j);
            }
        }
    }

    private static CellSequence findSequenceToFill(TaskDTO task, int r, int c){
        // check that the cell have not yet a sequence
        CellSequence selectedSequence = null;
        if (task.cells[r][c].cellSequences.isEmpty()){
            // loop for select the min sequence
            for (CellSequence sequence : task.cells[r][c].oldCellSequences) {
                // check that the sequence start from the cell
                if (selectedSequence == null) {
                    selectedSequence = sequence;
                } else {
                    // select the min sequence
                    if ((sequence.maxR - sequence.minR +1) * ( sequence.maxC- sequence.minC +1) > (selectedSequence.maxR - selectedSequence.minR +1) * ( selectedSequence.maxC- selectedSequence.minC +1)) {
                        selectedSequence = sequence;
                    }
                }
            }
        }
        return selectedSequence;
    }

    private static void fillCellsWithSequence(TaskDTO task, CellSequence sequence) {
        for (int i = sequence.minR; i < (sequence.maxR + 1); i++) {
            for (int j = sequence.minC; j < (sequence.maxC + 1); j++) {
                task.cells[i][j].cellSequences.add(sequence);
            }
        }
    }

    // change on set sequence info in left top corner

    private static ArrayList<CellSequence> loopForFill(TaskDTO task){
        nextTurnPreparing(task);
        ArrayList<CellSequence> selectedSequences =  new ArrayList<>();
        for (int i = 0; i < task.r; i++) {
            for (int j = 0; j < task.c; j++) {
                // fill list of selected sequence
                CellSequence selectedSequence = findSequenceToFill(task, i,j);
                if (selectedSequence == null) {
                } else {
                    selectedSequences.add(selectedSequence);
                    fillCellsWithSequence(task, selectedSequence);
                }
            }
        }
        return selectedSequences;
    }

    private static void outputSequences(ArrayList<CellSequence> sequences){
        int score = 0;
        int number = 0;
        for (CellSequence sequence : sequences) {
            number = number++;
            System.out.println(number + " " + sequence.minC + " " + sequence.minR  + " " + sequence.maxC + " " + sequence.maxR);
        }
    }

    public static void main(String[] args) {
        System.out.println("check");
        TaskDTO task = loadFile();
        loopForSequences(task);
        loopForMerge(task);
        ArrayList<CellSequence> selectedSequence = loopForFill(task);
        outputSequences(selectedSequence);
        System.out.println(task.r);
        System.out.println(task.c);
        System.out.println(task.l);
        System.out.println(task.h);
        Arrays.stream(task.pizza).forEach(System.out::println);
    }
}
