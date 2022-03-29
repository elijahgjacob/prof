/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.ArrayList;
import java.util.Random;

import static ataxx.PieceColor.*;
import static java.lang.Math.max;

/** A Player that computes its own moves.
 *  @author
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;


    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    void movesArray (Board board){
        // 1. add all often the possible moves and their associated scores to an array/hashmap
       // 2. Alpha beta pruning process to pick the best possible route

        ArrayList<Move> movesArr = new ArrayList<>();
        for (int x = 0; x < board.length(); x++) {
            if (board.get(x) == board.whoseMove()) {
                for (int colsaway = 0; colsaway < 2; colsaway++) {
                    for (int rowsaway = 0; rowsaway < 2; rowsaway++) {
                        int toInd = board.neighbor(x, colsaway, rowsaway);
                        if (board.get(toInd) == EMPTY) {
                            char c0 = board.reverseindexc(x);
                            char r0 = board.reverseindexr(x);
                            char c1 = board.reverseindexc(toInd);
                            char r1 = board.reverseindexr(toInd);
                            if (board.legalMove(Move.move(c0, r0, c1, r1))) {
                                movesArr.add(Move.move(c0, r0, c1, r1));
                           }
                        }
                    }
                }
            }
        }

    }



    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */
        movesArray(board);
        ArrayList<Move> movesArr = new ArrayList<>();
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        for (int x = 0; x < movesArr.size(); x++) {
            board.makeMove(movesArr.get(x));
            if (sense == 1) {
                int bestsofar = Integer.MIN_VALUE;
                int response = minMax(board, depth - 1, false, -1, alpha, beta);
                if (response > alpha) {
                    alpha = response;
                    alpha = max(alpha, bestsofar);
                    if (saveMove) {
                        _lastFoundMove = movesArr.get(x);
                    }
                }
            } else {
                int bestsofar = Integer.MAX_VALUE;
                int response = minMax(board, depth - 1, false, -1, alpha, beta);
                if (response < beta) {
                    beta = response;
                    beta = max(beta, bestsofar);
                    if (saveMove) {
                        _lastFoundMove = movesArr.get(x);
                    }
                }
                board.undo();
            }
        }
        return staticScore(board, WINNING_VALUE + depth);
    }


    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        return board.bluePieces() - board.redPieces(); // FIXME
    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}
