package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.time.LocalTime.now;

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

//        String fileName = "C:\\Work\\Java\\GoogleHashCode2018\\Task\\small.in";
//        String fileName = "C:\\Work\\Java\\GoogleHashCode2018\\Task\\medium.in";
        String fileName = "C:\\Work\\Java\\GoogleHashCode2018\\Task\\big.in";

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

    private static void fillCellsWithSequence(TaskDTO task, CellSequence sequence) {
        for (int i = sequence.minR; i < (sequence.maxR + 1); i++) {
            for (int j = sequence.minC; j < (sequence.maxC + 1); j++) {
                task.cells[i][j].mainCellSequences = sequence;
            }
        }
    }

    private static void outputSequences(ArrayList<CellSequence> sequences) {
        int score = 0;
        int number = 0;
        for (CellSequence sequence : sequences) {
            number = ++number;
            score = score + ((sequence.maxR - sequence.minR + 1) * (sequence.maxC - sequence.minC + 1));
            System.out.println(number + " " + sequence.minC + " " + sequence.minR + " " + sequence.maxC + " " + sequence.maxR);
        }
        System.out.println("score: " + score);
    }

    private static boolean checkPossibility(TaskDTO task, int sr, int sc, int fr, int fc) {
        // try to merge all sequence of the cells
        int countT1 = 0;
        int countT2 = 0;
        for (int i = sr; i < fr + 1; i++) {
            for (int j = sc; j < fc + 1; j++) {
                if (task.cells[sr][sc].cellType == task.cells[i][j].cellType) {
                    countT1 = ++countT1;
                } else {
                    countT2 = ++countT2;
                }
            }
        }
        return ((task.l <= countT1) && (task.l <= countT2));
    }

    private static void findInCell(TaskDTO task, int r, int c) {
        for (int j = 0; (j < task.h) && (j + c < task.c); j++) {
            if (task.cells[r][c + j].mainCellSequences == null) {
                int di = task.h / (j + 1);
                for (int i = 0; (i < di) && (i + r < task.r); i++) {
                    if (checkPossibility(task, r, c, r + i, c + j)) {
                        CellSequence cur = new CellSequence(r, c, r + i, c + j);
                        task.cells[r][c].cellSequences.add(cur);
                    }
                    ;
                }
            } else {
                break;
            }
        }
    }

    private static CellSequence selectOptimalSequence(TaskDTO task, int r, int c) {
        CellSequence selectedSequence = null;
        int hSelected = 0;
        if (!task.cells[r][c].cellSequences.isEmpty()) {
            // loop for select the min sequence
            for (CellSequence sequence : task.cells[r][c].cellSequences) {
                // check that the sequence start from the cell
                if (selectedSequence == null) {
                    selectedSequence = sequence;
                    hSelected = (selectedSequence.maxR - selectedSequence.minR + 1) * (selectedSequence.maxC - selectedSequence.minC + 1);
                } else {
                    // select the min sequence
                    int hCur = (sequence.maxR - sequence.minR + 1) * (sequence.maxC - sequence.minC + 1);
                    if ((hCur < hSelected) || ((hCur == hSelected) && (selectedSequence.maxR > sequence.maxR) )) {
                        selectedSequence = sequence;
                        hSelected = hCur;
                    }
                }
            }
        }
        return selectedSequence;
    }

    private static ArrayList<CellSequence> loopForFinding(TaskDTO task) {
        ArrayList<CellSequence> selectedSequences = new ArrayList<>();
        for (int i = 0; i < task.r; i++) {
            for (int j = 0; j < task.c; j++) {
                if (task.cells[i][j].mainCellSequences == null) {
                    findInCell(task, i, j);
                    CellSequence selectedSequence = selectOptimalSequence(task, i, j);
                    if (selectedSequence == null) {
                    } else {
                        fillCellsWithSequence(task, selectedSequence);
                        selectedSequences.add(selectedSequence);
                    }
                }
            }
        }
        return selectedSequences;
    }

    public static void main(String[] args) {
        System.out.println("check start " + now());
        TaskDTO task = loadFile();
        System.out.println("check after loading " + now());
        ArrayList<CellSequence> selectedSequence = loopForFinding(task);
        System.out.println("check after fill " + now());
        outputSequences(selectedSequence);
        System.out.println(task.r);
        System.out.println(task.c);
        System.out.println(task.l);
        System.out.println(task.h);
        Arrays.stream(task.pizza).forEach(System.out::println);
    }
}
