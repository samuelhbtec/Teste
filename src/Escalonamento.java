/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author Lê
 */
public class Escalonamento {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BufferedReader br = null;

        int trilhas = 0;
        int ini = 0;
        int[] req = {};

        try {

            String sCurrentLine;
            File file = new File("redes.txt");
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));

            int line = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                switch (line) {
                    case 0:
                        trilhas = Integer.parseInt(sCurrentLine);
                        break;
                    case 1:
                        ini = Integer.parseInt(sCurrentLine);
                        break;
                    case 2: {
                        String[] sArray = sCurrentLine.split(",");
                        req = stringArrayToInt(sArray);
                    }
                    break;
                }
                line++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        scan(ini, req);
    }

    public static void fifo(int ini, int[] req) {
        int dist = 0;
        int pos = ini;
        int exeDist = 0;
        System.out.println(".: FIFO :.");
        for (int i = 0; i < req.length; i++) {
            System.out.println();
            System.out.println("step = " + i);
            System.out.println("from = " + pos);
            System.out.println("to = " + req[i]);
            if (req[i] > pos) {
                exeDist = req[i] - pos;
                dist += exeDist;
            } else {
                exeDist = pos - req[i];
                dist += exeDist;
            }
            pos = req[i];
            System.out.println("exeDist = " + exeDist);
            System.out.println("totalDist = " + dist);
        }
    }

    public static void sstf(int ini, int[] req) {
        int dist = 0;
        int pos = ini;
        int exeDist = 0;
        int posShort = 0;
        int distShort = 200;

        System.out.println(".: SSTF :.");
        for (int j = 0; j < req.length; j++) {
            for (int i = 0; i < req.length; i++) {
                if (req[i] >= 0) {
                    if (req[i] > pos) {
                        exeDist = req[i] - pos;
                        //dist += exeDist;
                    } else {
                        exeDist = pos - req[i];
                        //dist += exeDist;
                    }

                    if (exeDist < distShort) {
                        distShort = exeDist;
                        posShort = i;
                    }
                }
            }
            System.out.println();
            System.out.println("step = " + j);
            System.out.println("from = " + pos);
            System.out.println("to = " + req[posShort]);
            System.out.println("exeDist = " + distShort);
            dist += distShort;
            System.out.println("totalDist = " + dist);
            pos = req[posShort];
            distShort = 200;
            req[posShort] = -1;
        }
    }

    public static void scan(int ini, int[] req) {
        int dist = 0;
        int pos = ini;
        int exeDist = 0;
        int step = 0;

        int[] betweenFromTo = startArray(req.length, -1); //iniciando com -1

        System.out.println(".: SCAN :.");
        for (int j = 0; j < req.length; j++) {
            if (req[j] >= 0) {
                System.out.println();
                System.out.println("step = " + step);
                step++;

                int to = req[j];
                int dir = pos - req[j]; //direação do elevador + desce - sobe

                for (int i = 0; i < req.length; i++) {
                    if (req[i] >= 0) {
                        //System.out.println(req[i]);
                        if (dir < 0) { //subindo
                            //System.out.println("subindo");
                            if (req[i] > pos && req[i] < to) {
                                //System.out.println("entrou");
                                betweenFromTo[i] = req[i];
                                req[i] = -1;
                            }
                        } else { // descendo
                            // System.out.println("descendo");
                            if (req[i] < pos && req[i] > to) {
                                //System.out.println("entrou");
                                betweenFromTo[i] = req[i];
                                req[i] = -1;
                            }
                        }
                    }
                }
                //ordernar between from to
                Arrays.sort(betweenFromTo);

                if (dir < 0) { //subindo
                    for (int z = 0; z < betweenFromTo.length; z++) { //lista dos elementos que estão entre from-to
                        if (betweenFromTo[z] >= 0) {
                            if (betweenFromTo[z] > pos) {
                                exeDist = betweenFromTo[z] - pos;
                                dist += exeDist;
                            } else {
                                exeDist = pos - betweenFromTo[z];
                                dist += exeDist;
                            }
                            System.out.println("from = " + pos);
                            System.out.println("to = " + betweenFromTo[z]);
                            pos = betweenFromTo[z];
                            System.out.println("exeDist = " + exeDist);
                        }
                    }
                } else { //descendo
                    for (int z = betweenFromTo.length-1; z >= 0; z--) { //lista dos elementos que estão entre from-to
                        if (betweenFromTo[z] >= 0) {
                            if (betweenFromTo[z] > pos) {
                                exeDist = betweenFromTo[z] - pos;
                                dist += exeDist;
                            } else {
                                exeDist = pos - betweenFromTo[z];
                                dist += exeDist;
                            }
                            System.out.println("from = " + pos);
                            System.out.println("to = " + betweenFromTo[z]);
                            pos = betweenFromTo[z];
                            System.out.println("exeDist = " + exeDist);
                        }
                    }
                }

                exeDist = Math.abs(pos - to); //pega o ultimo elemento da lista e diminui da posição que quero chegar (to).
                System.out.println("from = " + pos);
                System.out.println("to = " + to);
                System.out.println("exeDist = " + exeDist);
                dist += exeDist;

                pos = to;
                req[j] = -1; //numero já atendido, 'retira' da lista
                betweenFromTo = startArray(req.length, -1); //iniciando com -1

                System.out.println("totalDist = " + dist);
            }
        }
    }

    public static int[] startArray(int qty, int v) {
        int[] array = new int[qty];
        for (int i = 0; i < qty; i++) {
            array[i] = v;
        }
        return array;
    }

    public static int[] stringArrayToInt(String[] array) {
        int[] toInt = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            toInt[i] = Integer.parseInt(array[i]);
        }
        return toInt;
    }
}
