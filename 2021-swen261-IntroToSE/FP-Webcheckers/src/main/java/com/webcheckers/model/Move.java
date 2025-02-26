/**instance of a move
 * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * @author<a href='mailto:ajg7654@rit.edu'>Aiden Green</a>*/
package com.webcheckers.model;

import java.util.ArrayList;
import java.util.Objects;

public class Move {
    private Position start;
    private Position end;
    private int moveQuality;
    private boolean isJumpMove;
    private ArrayList<Position> jumpPath;
    private ArrayList<Position> destPath;

    public Move() {
        start = new Position(0, 0);
        end = new Position(0, 0);
        moveQuality = -Integer.MIN_VALUE;
    }

    public Move(Position initialPosition, Position finalPosition) {
        this.start = initialPosition;
        this.end = finalPosition;
        this.destPath = new ArrayList<Position> ();
        this.moveQuality = 0;
        this.isJumpMove = false;
    }

    public Move(Position initialPosition, Position finalPosition, boolean isJumpMove, ArrayList<Position> paths) {
        this(initialPosition, finalPosition);
        this.isJumpMove = isJumpMove;
        this.jumpPath = paths;

        //Fills destinations with the moves that will be made that follows the path
        for (int p = 0; Objects.nonNull(paths) && p<paths.size() - 1; p++) {
            Position path = paths.get(p);
            Position nextPath = paths.get(p + 1);

            int direction = path.cell<0 ? -1 : 1;

            Position jumpDestination = path.add(nextPath.subtract(path).mult(.5));
            if (jumpDestination.cell == path.cell) {
                jumpDestination.cell += 1 * direction;
            }

            destPath.add(jumpDestination);
        }
    }

    public void set(Move o) {
        this.start = o.getInitialPosition();
        this.end = o.getFinalPosition();
        this.destPath = new ArrayList<Position> (o.getDestPaths());
        this.moveQuality = o.getMoveQuality();
        this.isJumpMove = o.isJumpMove();
        this.jumpPath = isJumpMove ? new ArrayList<Position> (o.getJumpPaths()) : null;
    }

    public boolean isInBounds() {
        return !start.isOutOfBounds() && !end.isOutOfBounds();
    }

    public void setMoveQuality(int mv) {
        moveQuality = mv;
    }

    public int getMoveQuality() {
        return moveQuality;
    }

    /**
     * flips the cordinates to the opposite side; rotates 180 degrees
     * @return
     */
    public Move getFlipped() {
        int startRow = start.getRow();
        int startCell = start.getCell();
        int endRow = end.getRow();
        int endCell = end.getCell();
        return new Move(new Position(7 - startRow, 7 - startCell), new Position(7 - endRow, 7 - endCell));
    }

    // public Move getOpposite(){
    //     return new Move(new Position(this.start.row, this.start.cell), new Position(this.start.row-(this.end.row-this.start.row), this.end.cell));
    // }

    public Move getInverse() {
        int initialPositionRow = start.getRow();
        int initialPositionCell = start.getCell();
        int finalPositionRow = end.getRow();
        int finalPositionCell = end.getCell();
        return new Move(new Position(finalPositionRow, finalPositionCell), new Position(initialPositionRow, initialPositionCell));
    }

    public Position getInitialPosition() {
        return start;
    }

    public Position getFinalPosition() {
        return end;
    }

    public ArrayList<Position> getJumpPaths() {
        return jumpPath;
    }

    public ArrayList<Position> getDestPaths() {
        return destPath;
    }

    public boolean isJumpMove() {
        return isJumpMove;
    }

    public int count() {
        return destPath.size() + 1;
    }

    public boolean isSubsetOf(Move other) {
        ArrayList<Position> oPath = other.getJumpPaths();
        boolean hasPath = Objects.nonNull(jumpPath);
        boolean oHasPath = Objects.nonNull(oPath);

        if (!hasPath && !oHasPath)
            return this.equals(other);

        if (hasPath && !oHasPath || !hasPath)
            return false;

        for (Position p: jumpPath) {
            if (oPath.indexOf(p) == -1)
                return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return moveQuality == move.moveQuality &&
            isJumpMove == move.isJumpMove &&
            start.equals(move.start) &&
            end.equals(move.end);

    }
    public String toString() {

        String result = "Initial Position: " + start + " Final Position: " + end;

        return result;

    }
}