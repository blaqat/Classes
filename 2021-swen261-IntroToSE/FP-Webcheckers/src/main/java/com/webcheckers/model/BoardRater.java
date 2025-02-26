/**Calculates the difficulty of the AI player
 * * @author<a href='mailto:mss6522@rit.edu'>Max Shenk</a>
 * @author<a href='mailto:ajg7654@rit.edu'>Aiden Green</a>*/
package com.webcheckers.model;
import java.util.ArrayList;

public class BoardRater {

    //This single function just assigns the board a rating
    public static int rateBoard(BoardView board, Color ratingFor) {

        ArrayList<Piece> redPiecesArray = board.getPieces(Color.RED);

        ArrayList<Piece> whitePiecesArray = board.getPieces(Color.WHITE);

        int redPieces = redPiecesArray.size();
        int whitePieces = whitePiecesArray.size();
        int rating = 0;
        int redKings = 0;
        int whiteKings = 0;
        //King pieces are worth more tahn other pieces.
        for (Piece piece: redPiecesArray)
            if (piece.isKingPiece())
                redKings += 5;

        for (Piece piece: whitePiecesArray)
            if (piece.isKingPiece())
                whiteKings += 5;

        rating = (redPieces + redKings) - (whitePieces + whiteKings);
        rating = ratingFor == Color.WHITE ? -rating : rating;

        return rating;
    }

}