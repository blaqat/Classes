/**
 * This class will represent the colors that a chip could possibly be
 * @author<a href='mailto:mck9433@rit.edu'>Morgan Kreifels</a>
 */
package com.webcheckers.model;

import java.util.Iterator;

public class Row implements Iterable<Space> {
    private Space[] spaces;
    private int index;
    private int size;

    public Row(int index) {
        this.size = 8;
        this.spaces = new Space[size];
        this.index = index;

        for (int i = 0; i<size; i++) {
            spaces[i] = new Space(i, index);
        }
    }

    public int getIndex() {
        return this.index;
    }

    public Space getSpace(int index) {
        return spaces[index];
    }
    @Override
    public Iterator<Space> iterator() {
        return new Iterator<Space> () {
            int j = 0;
            @Override
            public boolean hasNext() {
                boolean has = j<size;
                return has;
            }

            @Override
            public Space next() {
                Space next = spaces[j];
                j++;
                return next;
            }
        };
    }
}