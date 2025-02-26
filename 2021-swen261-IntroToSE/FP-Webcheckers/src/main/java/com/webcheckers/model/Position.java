package com.webcheckers.model;

public class Position {
    public int row;
    public int cell;

    public Position(int row, int column) {
        this.row = row;
        this.cell = column;
    }

    public int getRow() {
        return row;
    }

    public int getCell() {
        return cell;
    }

    public Position[] getNeighbors() {
        Position[] neigh = new Position[4];

        neigh[0] = new Position(1, 1);
        neigh[1] = (new Position(1, -1));
        neigh[2] = (new Position(-1, 1));
        neigh[3] = (new Position(-1, -1));

        return neigh;
    }

    //Absolute Value
    public Position flip() {
        return new Position(-this.row, -this.cell);
    }

    public Position add(Position o) {
        return new Position(this.row + o.row, this.cell + o.cell);
    }

    public Position subtract(Position o) {
        return new Position((this.row - o.row), (this.cell - o.cell));
    }

    public Position mult(double d) {
        return new Position((int)(this.row * d), (int)(this.cell * d));
    }

    public boolean isOutOfBounds() {
        return this.row<0 || this.row > 7 || this.cell<0 || this.cell > 7; //this.isBetween(new Position(0,0), new Position(7,7));
    }

    @Override
    public String toString() {
        return "Position{" +
            "row=" + row +
            ", column=" + cell +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return this.row == position.getRow() && this.cell == position.getCell();
    }
}