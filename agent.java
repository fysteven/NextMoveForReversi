

import java.io.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Frank on 10/11/14.
 */
public class agent {
    public String algorithm = "";
    public String myPlayer = "";
    public static char opponent;
    public static char player;
    public int cuttingOffDepth;
    public String[] matrixLine = new String[8];
    public char[][] matrix = new char[8][8];


    public static void main(String[] args) {
        agent reversi = new agent();
        try {



            //reversi.write("abc");
            reversi.read();
            reversi.determineWhichAlgorithm();
            if (reversi.determineWhichAlgorithm() == 1) {
                MiniMax minimax = new MiniMax(reversi.matrix, reversi.player, reversi.opponent, 1);
                String string = minimax.printTraverseLog();
                reversi.write(string);

                //System.out.print(string);
                //System.out.println("end");

            } else if (reversi.determineWhichAlgorithm() == 2) {
                MiniMax minimax = new MiniMax(reversi.matrix, reversi.player, reversi.opponent, reversi.cuttingOffDepth);
                String string = minimax.printTraverseLog();
                StringBuilder builder = new StringBuilder();
                builder.append(string);
                builder.append("Node,Depth,Value");
                builder.append('\n');
                for (String line: minimax.path) {
                    builder.append(line);
                    builder.append('\n');
                }
                reversi.write(builder);


            } else if (reversi.determineWhichAlgorithm() == 3) {
                AlphaBeta alphabeta = new AlphaBeta(reversi.matrix, reversi.player, reversi.opponent, reversi.cuttingOffDepth);
                String string = alphabeta.printTraverseLog();
                StringBuilder builder = new StringBuilder();
                builder.append(string);
                builder.append("Node,Depth,Value,Alpha,Beta");
                builder.append('\n');
                for (String line: alphabeta.path) {
                    builder.append(line);
                    builder.append('\n');
                }
                reversi.write(builder);

            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean writeOutputFile(StringBuilder builder) throws IOException {
        //System.out.print(builder.toString());
        String outputFile = "output.txt";
        FileWriter fileWriter = new FileWriter(outputFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(builder.toString());
        bufferedWriter.close();
        return true;
    }

    public boolean readInputFile(BufferedReader reader) throws IOException {
        Scanner sc = new Scanner(reader).useDelimiter("\\s*\n\\s*");
        algorithm = sc.next();
        System.out.println(algorithm);

        myPlayer = sc.next();
        //System.out.println(myPlayer);

        player = myPlayer.charAt(0);
        System.out.println(player);


        cuttingOffDepth = Integer.parseInt(sc.next());
        System.out.println(cuttingOffDepth);

        for (int i = 0; i < 8; i++) {
            matrixLine[i] = sc.next();
            matrix[i] = matrixLine[i].toCharArray();
            System.out.println(matrix[i]);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (matrix[i][j] != player && matrix[i][j] != '*') {
                    opponent = matrix[i][j];
                }
            }
        }

        return true;
    }

    public boolean read(String input) throws IOException {
        if (input == null) {
            input = "input.txt";
        }
        FileReader fileReader = new FileReader(input);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        this.readInputFile(bufferedReader);
        return true;
    }

    public boolean write(String output) throws IOException {
        writeOutputFile(new StringBuilder(output));
        return true;
    }

    public boolean write(StringBuilder builder) throws IOException {
        writeOutputFile(builder);
        return true;
    }

    public boolean read() throws IOException {
        this.read("input.txt");
        return true;
    }

    public int determineWhichAlgorithm() {
        if (this.algorithm.equals("1")) {
            //System.out.println("Greedy");
            return 1;


        } else if (this.algorithm.equals("2")) {
            //System.out.println("MiniMax");
            return 2;

        } else if (this.algorithm.equals("3")) {
            //System.out.println("Competition");
            return 3;

        } else {
            return -1;
        }

    }

    public static class Evaluation {
        public static final int[][] positionalWeight = {
                { 99, -8, 8, 6, 6, 8, -8, 99 },
                { -8, -24, -4, -3, -3, -4, -24, -8 },
                { 8, -4, 7, 4, 4, 7, -4, 8 },
                { 6, -3, 4, 0, 0, 4, -3, 6 },
                { 6, -3, 4, 0, 0, 4, -3, 6 },
                { 8, -4, 7, 4, 4, 7, -4, 8 },
                { -8, -24, -4, -3, -3, -4, -24, -8 },
                { 99, -8, 8, 6, 6, 8, -8, 99 }
        };
        public static int getEvaluation(char[][] matrix, char xPlayer, char oPlayer) {

            xPlayer = agent.player;
            oPlayer = agent.opponent;

            int sum = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (matrix[i][j] == xPlayer) {
                        sum += positionalWeight[i][j];
                    }
                    if (matrix[i][j] == oPlayer) {
                        sum -= positionalWeight[i][j];
                    }
                }

            }
            return sum;
        }
    }

}

class AlphaBeta {
    public String[] name = {"a", "b", "c", "d", "e", "f", "g", "h"};
    public char[][] matrix;
    public char player;
    public char opponent;
    public int cutting_off_depth;
    public LinkedList<String> path;



    public static void main(String[] args) {


     /*   int[] a = new int[]{1,2,3,4,5,6,7};
        int[] b = new int[10];
        System.arraycopy(a, 0, b, 0, 7);
*/
        //AlphaBeta ab = new AlphaBeta();
        //System.out.println(ab.print("root", 3, 2, Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

    public AlphaBeta() {

    }

    public AlphaBeta(char[][] matrix, char player, char opponent, int cutting_off_depth) {
        this.matrix = matrix;
        this.player = player;
        this.opponent = opponent;
        this.cutting_off_depth = cutting_off_depth;
        this.path = new LinkedList<String>();
    }

    public String printTraverseLog() {
        int a = Integer.MIN_VALUE;
        int b = Integer.MAX_VALUE;
        BoardState root = new BoardState("root", 0, this.matrix, this.player);
        root.parent = root;
        root.nextBoardState = root;
        maxValue(root, a, b);
        return root.getNextBoardState().printOut();
    }

    public boolean isGameOver(BoardState boardstate)
    {
        int xCount = 0,oCount = 0,sCount = 0;
        for(int i=0;i<8;i++)
            for(int j =0;j<8;j++)
                if(boardstate.matrix[i][j] == player)
                    xCount ++;
                else if(boardstate.matrix[i][j] == opponent)
                    oCount ++;
                else
                    sCount ++;

        if(xCount == 0 || oCount == 0 || sCount == 0)
            return true;
        else
            return false;
    }

    public int maxValue(BoardState boardstate, int a, int b) {
        LinkedList<BoardState> children = findLegalBoardStates(boardstate);
        BoardState opponentState = new BoardState("pass", boardstate.depth + 1, boardstate.matrix, boardstate.opponent);
        LinkedList<BoardState> childrenOfOpponent = findLegalBoardStates(opponentState);

        if (boardstate.depth == this.cutting_off_depth || isGameOver(boardstate) || (boardstate.node.equals("pass") && boardstate.parent.node.equals("pass"))) {
            int value = agent.Evaluation.getEvaluation(boardstate.matrix, boardstate.myPlayer, boardstate.opponent);
            boardstate.value = value;
            path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));
            return value;
        }

        boardstate.value = Integer.MIN_VALUE;
        path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));
        if (children.size() == 0 && childrenOfOpponent.size() > 0) {
            opponentState.parent = boardstate;
            children.add(opponentState);
        }
        if (children.size() == 0 && childrenOfOpponent.size() == 0) {
            opponentState.parent = boardstate;
            children.add(opponentState);
        }


        for (BoardState child: children) {
            boardstate.value = max(boardstate.value, minValue(child, a, b));

            //boardstate.setNextBoardState(child);
            child.parent = boardstate;


            if (boardstate.value >= b) {
                path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));
                return boardstate.value;
            } else {
                if(boardstate.value > a) {
                    boardstate.nextBoardState = child;
                }
                a = max(a, boardstate.value);
            }

            path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));

        }

        return boardstate.value;
    }

    public int minValue(BoardState boardstate, int a, int b) {
        LinkedList<BoardState> children = findLegalBoardStates(boardstate);

        BoardState opponentState = new BoardState("pass", boardstate.depth + 1, boardstate.matrix, boardstate.opponent);
        LinkedList<BoardState> childrenOfOpponent = findLegalBoardStates(opponentState);

        if (boardstate.depth == this.cutting_off_depth || isGameOver(boardstate) ||  (boardstate.node.equals("pass") && boardstate.parent.node.equals("pass"))) {
            int value = agent.Evaluation.getEvaluation(boardstate.matrix, boardstate.myPlayer, boardstate.opponent);
            boardstate.value = value;
            path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));

            return value;
        }

        boardstate.value = Integer.MAX_VALUE;
        path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));

        if (children.size() == 0 && childrenOfOpponent.size() > 0) {

            opponentState.parent = boardstate;
            children.add(opponentState);
        }
        if (children.size() == 0 && childrenOfOpponent.size() == 0) {

            opponentState.parent = boardstate;
            children.add(opponentState);
        }

        for (BoardState child: children) {

            boardstate.value = min(boardstate.value, maxValue(child, a, b));
            //boardstate.setNextBoardState(child);
            child.parent = boardstate;

            if (boardstate.value <= a) {
                path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));
                return boardstate.value;
            } else {
                if(boardstate.value < b) {
                    boardstate.nextBoardState = child;
                }
                b = min(b, boardstate.value);
            }
            path.add(print(boardstate.node, boardstate.depth, boardstate.value, a, b));

        }
        return boardstate.value;
    }


    public String print(String node, int depth, int value, int a, int b) {
        StringBuilder builder = new StringBuilder();
        builder.append(node);
        builder.append(',');
        builder.append(depth);
        builder.append(',');
        builder.append(showInfinity(value));
        builder.append(',');
        builder.append(showInfinity(a));
        builder.append(',');
        builder.append(showInfinity(b));
        return builder.toString();
    }



    public LinkedList<BoardState> findLegalBoardStates(BoardState boardState) {
        LinkedList<BoardState> boardStates = new LinkedList<BoardState>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                HashSet<int[]> set = getFlipPosition(boardState, i, j);
                if (set.size() > 0) {
                    int[] piece = {i, j};
                    set.add(piece);
                    char[][] flippedBoard = getFlippedBoard(boardState, set);
                    BoardState board2 = new BoardState(name[j] + (i + 1), boardState.depth + 1, flippedBoard, boardState.opponent);
                    boardStates.add(board2);

                }
            }

        }
        return boardStates;
    }

    public HashSet<int[]> getFlipPosition(BoardState b, int i, int j) {
        HashSet<int[]> set = new HashSet<int[]>();
        if (makeSureInBound(i, j) && b.matrix[i][j] == '*') {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    int tx = i + dx;
                    int ty = j + dy;
                    while (makeSureInBound(tx, ty) && b.matrix[tx][ty] != '*') {
                        if (b.matrix[tx][ty] != b.myPlayer) {
                            tx += dx;
                            ty += dy;
                        } else {

                            int px = tx - dx;
                            int py = ty - dy;
                            while (!(px == i && py == j)) {
                                int piece[] = {px, py};
                                set.add(piece);
                                px -= dx;
                                py -= dy;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return set;
    }

    public char[][] getFlippedBoard(BoardState b, HashSet<int[]> set) {
        char[][] matrix2 = new char[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(b.matrix[i], 0, matrix2[i], 0, 8);
            /*for (int j = 0; j < 8; j++) {
                matrix2[i][j] = b.matrix[i][j];
            }*/
        }

        for (int[] s:set) {
            matrix2[s[0]][s[1]] = b.myPlayer;
        }
        return matrix2;
    }

    public boolean makeSureInBound(int i, int j) {
        if (i < 0) {
            return false;
        }
        if (i >= 8) {
            return false;
        }
        if (j < 0) {
            return false;
        }
        if (j >= 8) {
            return false;
        }
        return true;
    }

    public String showInfinity(int value) {
        String result;
        if (value == Integer.MAX_VALUE) {
            result = "Infinity";
        } else if (value == Integer.MIN_VALUE) {
            result = "-Infinity";
        } else {
            result = Integer.toString(value);
        }
        return result;
    }

    public int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }
    public int min(int a, int b) {
        if (a < b) {
            return a;
        } else {
            return b;
        }
    }

    private static void fast() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 100000; i++)
            s.append("*");
    }

    private static void slow() {
        String s = "";
        for (int i = 0; i < 100000; i++)
            s += "*";
    }

    private static void medium() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < 100000; i++) {
            s.append("*");
        }
    }
}

class MiniMax {
    public String[] name = { "a", "b", "c", "d", "e", "f", "g", "h" };
    public char[][] matrix;
    public char player;
    public char opponent;

    public int cutting_off_depth;
    public LinkedList<String> path;

    public MiniMax() {

    }
    public MiniMax(char[][] matrix, char player, char opponent, int cutting_off_depth) {
        this.matrix = matrix;
        this.player = player;
        this.opponent = opponent;
        this.cutting_off_depth = cutting_off_depth;
        this.path = new LinkedList<String>();

    }

    public String printTraverseLog() {
        BoardState root = new BoardState("root", 0, this.matrix, this.player);
        root.parent = root;
        root.nextBoardState = root;
        maxValue(root);
        return root.getNextBoardState().printOut();
    }



    public int maxValue(BoardState boardstate) {
        LinkedList<BoardState> children = findLegalBoardStates(boardstate);
        BoardState opponentState = new BoardState("pass", boardstate.depth + 1, boardstate.matrix, boardstate.opponent);
        LinkedList<BoardState> childrenOfOpponent = findLegalBoardStates(opponentState);


        if (boardstate.depth == this.cutting_off_depth|| isGameOver(boardstate)  || (boardstate.node.equals("pass") && boardstate.parent.node.equals("pass"))) {
            int value = agent.Evaluation.getEvaluation(boardstate.matrix, boardstate.myPlayer, boardstate.opponent);
            boardstate.value = value;
            path.add(print(boardstate.node, boardstate.depth, boardstate.value));
            return value;
        }

        boardstate.value = Integer.MIN_VALUE;
        path.add(print(boardstate.node, boardstate.depth, boardstate.value));

        if (children.size() == 0 && childrenOfOpponent.size() > 0) {
            children.add(opponentState);
            opponentState.parent = boardstate;
        }
        if (children.size() == 0 && childrenOfOpponent.size() == 0) {
            children.add(opponentState);
            opponentState.parent = boardstate;
        }

        for (BoardState board: children) {
            int temp = minValue(board);
            if (temp > boardstate.value) {
                boardstate.value = temp;
                boardstate.setNextBoardState(board);
                board.parent = boardstate;

            }
            path.add(print(boardstate.node, boardstate.depth, boardstate.value));
        }
        return boardstate.value;
    }

    public int minValue(BoardState boardstate) {
        LinkedList<BoardState> children = findLegalBoardStates(boardstate);
        BoardState opponentState = new BoardState("pass", boardstate.depth + 1, boardstate.matrix, boardstate.opponent);
        LinkedList<BoardState> childrenOfOpponent = findLegalBoardStates(opponentState);


        if (boardstate.depth == this.cutting_off_depth|| isGameOver(boardstate) || (boardstate.node.equals("pass") && boardstate.parent.node.equals("pass"))) {
            int value = agent.Evaluation.getEvaluation(boardstate.matrix, boardstate.myPlayer, boardstate.opponent);
            boardstate.value = value;
            path.add(print(boardstate.node, boardstate.depth, boardstate.value));
            return value;
        }

        boardstate.value = Integer.MAX_VALUE;
        path.add(print(boardstate.node, boardstate.depth, boardstate.value));

        if (children.size() == 0 && childrenOfOpponent.size() > 0) {
            children.add(opponentState);
            opponentState.parent = boardstate;
        }
        if (children.size() == 0 && childrenOfOpponent.size() == 0) {
            children.add(opponentState);
            opponentState.parent = boardstate;
        }

        for (BoardState board: children) {
            int temp = maxValue(board);
            if (temp < boardstate.value) {
                boardstate.value = temp;
                boardstate.setNextBoardState(board);
                board.parent = boardstate;

            }
            path.add(print(boardstate.node, boardstate.depth, boardstate.value));
        }
        return boardstate.value;
    }

    public boolean isGameOver(BoardState boardstate) {
        int xCount = 0;
        int oCount = 0;
        int sCount = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardstate.matrix[i][j] == player) {
                    xCount++;
                } else if (boardstate.matrix[i][j] == opponent) {
                    oCount++;
                } else {
                    sCount++;
                }
            }
        }

        if(xCount == 0 || oCount == 0 || sCount == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String print(String node, int depth, int value) {
        StringBuilder builder = new StringBuilder();
        builder.append(node);
        builder.append(",");
        builder.append(depth);
        builder.append(",");
        String temp;
        if (value == Integer.MAX_VALUE) {
            temp = "Infinity";
        } else if (value == Integer.MIN_VALUE) {
            temp = "-Infinity";
        } else {
            temp = Integer.toString(value);
        }
        builder.append(temp);
        return builder.toString();
    }

    public LinkedList<BoardState> findLegalBoardStates(BoardState boardState) {
        LinkedList<BoardState> boardStates = new LinkedList<BoardState>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                HashSet<int[]> set = getFlipPosition(boardState, i, j);
                if (set.size() > 0) {
                    int[] piece = {i, j};
                    set.add(piece);
                    char[][] flippedBoard = getFlippedBoard(boardState, set);
                    BoardState board2 = new BoardState(name[j] + (i + 1), boardState.depth + 1, flippedBoard, boardState.opponent);
                    boardStates.add(board2);

                }
            }

        }
        return boardStates;
    }

    public HashSet<int[]> getFlipPosition(BoardState b, int i, int j) {
        HashSet<int[]> set = new HashSet<int[]>();
        if (makeSureInBound(i, j) && b.matrix[i][j] == '*') {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }
                    int tx = i + dx;
                    int ty = j + dy;
                    while (makeSureInBound(tx, ty) && b.matrix[tx][ty] != '*') {
                        if (b.matrix[tx][ty] != b.myPlayer) {
                            tx += dx;
                            ty += dy;
                        } else {
                            // backward a step to get the flip pieces
                            int px = tx - dx;
                            int py = ty - dy;
                            while (!(px == i && py == j)) {
                                int piece[] = {px, py};
                                set.add(piece);
                                px -= dx;
                                py -= dy;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return set;
    }

    public char[][] getFlippedBoard(BoardState b, HashSet<int[]> set) {
        char[][] matrix2 = new char[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                matrix2[i][j] = b.matrix[i][j];
            }
        }
        for (int[] s:set) {
            matrix2[s[0]][s[1]] = b.myPlayer;
        }
        return matrix2;
    }

    public boolean makeSureInBound(int i, int j) {
        if (i < 0) {
            return false;
        }
        if (i >= 8) {
            return false;
        }
        if (j < 0) {
            return false;
        }
        if (j >= 8) {
            return false;
        }
        return true;
    }
}

class BoardState {
    public String node;
    public int depth;
    public int value;
    public char[][] matrix;
    //public String myPlayer;
    public char myPlayer;
    public char opponent;
    public BoardState nextBoardState;
    public BoardState parent;
    public int a;
    public int b;

    public BoardState(String node, int depth, char[][] matrix, char myPlayer) {
        this.node = node;
        this.depth = depth;
        this.matrix = matrix;
        this.myPlayer = myPlayer;
        if (this.myPlayer == 'X') {
            this.opponent = 'O';
        } else {
            this.opponent = 'X';
        }
    }

    public String printOut() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                builder.append(matrix[i][j]);
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    public String getNode() {
        return node;
    }

    public int getDepth() {
        return depth;
    }

    public int getValue() {
        return value;
    }

    public char[][] getMatrix() {
        return matrix;
    }

    public char getMyPlayer() {
        return myPlayer;
    }

    public char getOpponent() {
        return opponent;
    }

    public BoardState getNextBoardState() {
        return nextBoardState;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMatrix(char[][] matrix) {
        this.matrix = matrix;
    }

    public void setMyPlayer(char myPlayer) {
        this.myPlayer = myPlayer;
    }

    public void setOpponent(char opponent) {
        this.opponent = opponent;
    }

    public void setNextBoardState(BoardState nextBoardState) {
        this.nextBoardState = nextBoardState;
    }
    //public BoardState g
}
