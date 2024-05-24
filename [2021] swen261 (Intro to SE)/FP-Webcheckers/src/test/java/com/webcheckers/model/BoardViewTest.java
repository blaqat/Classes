package com.webcheckers.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Model-Tier") @Testable
public class BoardViewTest {

    @Test
    public void testCreateBoardView(){
        Row[] rows;
        int size;
        size = 8;
        //Initialize rows parameter with a capacity of 8
        rows = new Row[size];

        //Inserts 8 rows into Board
        for (int i = 0; i < size; i++) {
            rows[i] = new Row(i);
        }

        for (int i = 0; i < size; i++) {
            Row currentRow = rows[i];
            for (int j = 0; j < size; j++) {
                Space currentSpace = currentRow.getSpace(j);
                Piece placePiece = new Piece((i < 4) ? Color.WHITE : Color.RED, Type.SINGLE);
                if (i < 3 || i > 4)
                    switch (i % 2) {
                        case 0:
                            if (j % 2 == 1)
                                currentSpace.setPiece(placePiece);
                            break;
                        case 1:
                            if (j % 2 == 0)
                                currentSpace.setPiece(placePiece);
                            break;
                    }
            }
        }


        for(int i = 0; i < size; i++){
            Row currentRow = rows[i];
            for (int j = 0; j < size; j++) {
                Space currentSpace = currentRow.getSpace(j);
                Piece expected = currentSpace.getPiece();
                Piece actual = currentSpace.getPiece();

                assertEquals(expected, actual);

            }
        }


    }


}
