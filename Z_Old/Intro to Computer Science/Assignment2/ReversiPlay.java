public class ReversiPlay {

	public static void main(String[] args) {
        int[][] matrix = createBoard(8);

        for (int indexOne = 0; indexOne < matrix.length; indexOne = indexOne + 1) {
            for (int indexTwo = 0; indexTwo < matrix[indexOne].length; indexTwo = indexTwo + 1) {
                System.out.print(locationValue(matrix.length, indexOne, indexTwo) +  " ");
            }
            // Go down one line.
            System.out.println();
        }
	}

    /**
     * Print every item in the matrix.
     *
     * @param matrix
     *   Matrix to print.
     */
	public static void printMatrix(int[][] matrix) {
		for (int indexOne = 0; indexOne < matrix.length; indexOne = indexOne + 1) {
            for (int indexTwo = 0; indexTwo < matrix[indexOne].length; indexTwo = indexTwo + 1) {
                System.out.print(matrix[indexOne][indexTwo] +  " ");
            }
            // Go down one line.
            System.out.println();
        }
	}

    /**
     * Check if every item in matrix1 matches matrix2.
     *
     * @param matrix1
     *   Matrices to compare.
     * @param matrix2
     *   Matrices to compare.
     *
     * @return
     *   True if matching, false otherwise.
     */
	public static boolean isEqual(int[][] matrix1, int[][] matrix2) {
        if (matrix1.length != matrix2.length) {
            // Not the same length.
            return false;
        }

        for (int indexOne = 0; indexOne < matrix1.length; indexOne = indexOne + 1) {
            if (matrix1[indexOne].length != matrix2[indexOne].length) {
                // Not the same length.
                return false;
            }

            for (int indexTwo = 0; indexTwo < matrix1[indexOne].length; indexTwo = indexTwo + 1) {
                if (matrix1[indexOne][indexTwo] != matrix2[indexOne][indexTwo]) {
                    // Not the same item.
                    return false;
                }
            }
        }

        // Matrices are the same.
		return true;
	}

    /**
     * Copy an existing matrix.
     *
     * @param matrix
     *   Matrix to copy.
     *
     * @return
     *   A copy of the matrix (in a different place in the memory).
     */
	public static int[][] copyMatrix(int[][] matrix) {
        // Initialize matrix.
        int[][] newMatrix = new int[matrix.length][];

        for (int indexOne = 0; indexOne < matrix.length; indexOne = indexOne + 1) {
            // Initialize array.
            newMatrix[indexOne] = new int[matrix[indexOne].length];

            for (int indexTwo = 0; indexTwo < matrix[indexOne].length; indexTwo = indexTwo + 1) {
                newMatrix[indexOne][indexTwo] = matrix[indexOne][indexTwo];
            }
        }

        // Return the new matrix.
		return newMatrix;
	}

    /**
     * Create a new board, sizing size*size.
     *
     * @param size
     *   Board size, between 4 to 40 and even.
     * @return
     *   Created board.
     */
	public static int[][] createBoard(int size) {
        if (size > 40 || size < 4 || (size % 2 != 0)) {
            throw new IllegalArgumentException("Wrong size.");
        }
        int[][] board = new int[size][size];

        int center = (size / 2) - 1;
        board[center][center] = 2;
        board[center + 1][center] = 1;
        board[center][center + 1] = 1;
        board[center + 1][center + 1] = 2;

		return board;
	}

    /**
     * Check if a move is legal.
     *
     * @param board
     *   Board matrix.
     * @param player
     *   What player is trying to move.
     * @param row
     *   X coordinate.
     * @param column
     *   Y coordinate.
     *
     * @return
     *   Whether the move is legal.
     */
	public static boolean isLegal(int[][] board, int player, int row, int column) {
        if (board[row][column] != 0) {
            return false;
        }

        // Define rowChange and columnChange to check in a loop.
        int[][] directionChanges = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};

        for (int index = 0; index < directionChanges.length; index = index + 1) {
            if (checkDirection(board,player,row,column, directionChanges[index][0],directionChanges[index][1])) {
                // The move is legal.
                return true;
            }
        }

        // The move is illegal.
		return false;
	}

    /**
     * Helper function to determine if a direction is an optional move.
     */
    public static boolean checkDirection(int[][] board, int player, int row, int column,  int rowChange, int columnChange) {
        int otherPlayer = player == 1 ? 2 : 1;
        int size = board.length;
        if (outOfBounds(board, 2, row, column, rowChange, columnChange)) {
            // Too close to the edge of the board (must have at least two
            // spaces).
            return false;
        }
        if (board[row + rowChange][column + columnChange] == otherPlayer) {
            int distance = 1;
            while (!outOfBounds(board, distance, row, column, rowChange, columnChange)) {
                int currentBox = board[row + (distance * rowChange)][column + (distance * columnChange)];
                if (currentBox == player) {
                    // Legal.
                    return true;
                }
                else if (currentBox == 0) {
                    // Not legal.
                    return false;
                }
                // Next distance (we found otherPlayer again).
                distance = distance + 1;
            }
        }

        // Out of bounds.
        return false;
    }

    /**
     * Helper function to determine if we're looking for something out of bounds
     * of the array.
     *
     * @param distance
     *   Distance from original location.
     *
     * @return
     *   Whether the new location is out of bounds.
     */
    public static boolean outOfBounds(int[][] board, int distance, int row, int column,  int rowChange, int columnChange) {

        // Size of of each row/column.
        int size = board.length;

        int newRow = row + (distance * rowChange);
        int newColumn = column + (distance * columnChange);
        if (isBetween(newRow, -1, size) && isBetween(newColumn, -1, size)) {
            // Too close to the edge of the board.
            return false;
        }

        // Out of bounds of the array.
        return true;
    }

    /**
     * Helper function: Check if a number is between two other numbers.
     *
     * @param check
     *   Number to check.
     * @param small
     *   Number that has to be smaller.
     * @param large
     *   Number that has to be larger.
     */
    public static boolean isBetween(int check, int small, int large) {
        return (small < check && check < large);
    }

    /**
     * Changes the board according to the play.
     *
     * @param board
     *   Board matrix.
     * @param player
     *   Player to move.
     *
     * @param row
     * @param column
     *   Location to move to.
     *
     * @return
     *   Board matrix after making the move.
     */
	public static int[][] play(int[][] board, int player, int row, int column) {
        if (!isLegal(board,player,row,column)) {
            return board;
        }

        // Set the new place.
        board[row][column] = player;

        // Define rowChange and columnChange to check in a loop.
        int[][] directionChanges = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};

        for (int index = 0; index < directionChanges.length; index = index + 1) {
            if (checkDirection(board,player,row,column, directionChanges[index][0],directionChanges[index][1])) {
                // The move is legal.
                reversiFlip(board, player, row, column, directionChanges[index][0], directionChanges[index][1]);
            }
        }

		return board;
	}

    /**
     * Helper function: Flip all opponents' pieces in the direction.
     */
    public static void reversiFlip(int[][] board, int player, int row, int column, int rowChange, int columnChange) {

        // Change all opponent's pieces in the direction.
        int distance = 1;
        while (board[row + (distance * rowChange)][column + (distance * columnChange)] != player) {
            // Flip the current space.
            board[row + (distance * rowChange)][column + (distance * columnChange)] = player;
            distance = distance + 1;
        }

        // Reached the player's piece.
    }

    /**
     * Check number of flipped pieces when using a specific move.
     *
     * @return
     *   Number of flipped pieces.
     */
	public static int benefit(int[][] board, int player, int row, int column) {
        if (!isLegal(board, player, row, column)) {
            return 0;
        }

        // Define rowChange and columnChange to check in a loop.
        int[][] directionChanges = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
        int benefit = 0;

        for (int index = 0; index < directionChanges.length; index = index + 1) {
            int rowChange = directionChanges[index][0];
            int columnChange = directionChanges[index][1];

            if (checkDirection(board, player, row, column, rowChange, columnChange)) {
                // The move is legal.
                int distance = 1;
                while (board[row + (distance * rowChange)][column + (distance * columnChange)] != player) {
                    // Will be flipped.
                    benefit = benefit + 1;

                    // Check next.
                    distance = distance + 1;
                }
            }
        }

		return benefit;
	}

    /**
     * Find all possible moves.
     *
     * @return
     *   Return array of possible moves. Each possible moves is represented by
     *   x,y coordinates.
     */
	public static int[][] possibleMoves(int[][] board, int player) {
        int size = board.length;

        // Assume every square is a possible move.
        int[][] possibilities = new int[size * size][2];

        // Check every square if it's a possible move.
        int possibleMoveIndex = 0;
        for (int indexOne = 0; indexOne < board.length; indexOne = indexOne + 1) {
            for (int indexTwo = 0; indexTwo < board.length; indexTwo = indexTwo + 1) {
                if (isLegal(board,player,indexOne,indexTwo)) {
                    // Save this option.
                    possibilities[possibleMoveIndex][0] = indexOne;
                    possibilities[possibleMoveIndex][1] = indexTwo;
                    possibleMoveIndex = possibleMoveIndex + 1;
                }
            }
        }

        // Reduce size of possible moves.
        int[][] moves = new int[possibleMoveIndex][];
        for (int index = 0; index < moves.length; index = index + 1) {
            moves[index] = possibilities[index];
        }

		return moves;
	}

    /**
     * Find if a player has any legal moves.
     *
     * @param board
     *  Board to check
     * @param player
     *  Player to check.
     *
     * @return
     *   Whether the player has moves.
     */
	public static boolean hasMoves(int[][] board, int player) {
        return (possibleMoves(board, player).length != 0);
	}

    /**
     * Find which player has more pieces on the board.
     */
	public static int findTheWinner(int[][] board) {
        int[] score = getScores(board);

        // Check who the winner is.
        if (score[0] > score[1]) {
            // Player 1 won.
            return 1;
        }
        else if (score[0] < score[1]) {
            // Player 2 won.
            return 2;
        }

        // Draw.
		return 0;
	}

    /**
     * Get players' scores.
     *
     * @param board
     * @return
     *   Array with number of pieces per player.
     *    - Player 1 is in index 0.
     *    - Player 2 is in index 1.
     */
    public static int[] getScores(int[][] board) {
        int[] score = new int[2];

        // Sum up number of pieces for each user.
        for (int indexOne = 0; indexOne < board.length; indexOne = indexOne + 1) {
            for (int indexTwo = 0; indexTwo < board[indexOne].length; indexTwo = indexTwo + 1) {
                if (board[indexOne][indexTwo] != 0) {
                    // Increment player score.
                    score[board[indexOne][indexTwo] - 1] = score[board[indexOne][indexTwo] - 1] + 1;
                }
            }
        }

        return score;
    }

    /**
     * Check if the game ended.
     * @param board
     *   Board to check.
     *
     * @return boolean
     *   Whether the game ended.
     */
	public static boolean gameOver(int[][] board) {
        if (hasMoves(board,1) || hasMoves(board, 2)) {
		    return false;
        }

        // This will happen if:
        //  1. The board is full.
        //  2. All the pieces are the same color.
        //  3. No more legal moves for either player.
        return true;

	}

    // --- Begin AI.

    /**
     * Random player AI - choose a move randomly.
     */
	public static int[] randomPlayer(int[][] board, int player) {
        int[][] possibleMoves = possibleMoves(board, player);

        if (possibleMoves.length > 0) {
            int chosenMove = randomize(possibleMoves.length);
            return possibleMoves[chosenMove];
        }

        // No moves.
		return null;
	}

    /**
     * Greedy player AI - go for the move with the most benefit.
     */
	public static int[] greedyPlayer(int[][] board, int player) {
        int[][] possibleMoves = possibleMoves(board, player);

        if (possibleMoves.length == 0) {
            // No moves.
            return null;
        }

        // Rank moves by benefit.
        int[] benefits = new int[possibleMoves.length];
        int maxBenefit = 0;
        for (int index = 0; index < possibleMoves.length; index = index + 1) {
            benefits[index] = benefit(board,player, possibleMoves[index][0], possibleMoves[index][1]);
            if (benefits[index] > maxBenefit) {
                // Save the maximum benefit.
                maxBenefit = benefits[index];
            }
        }

        // Select the move randomly; take only the maximum beneficial one.
        int index = randomize(benefits.length);
        while (benefits[index] != maxBenefit) {
            index = randomize(benefits.length);
        }

        // Return the move.
        return possibleMoves[index];
	}

    /**
     * Defensive player AI - choose a move according to how much will be lost
     * after the other player's move.
     */
	public static int[] defensivePlayer(int[][] board, int player) {
        int[][] possibleMoves = possibleMoves(board, player);
        int otherPlayer = player == 1? 2 : 1;
        if (possibleMoves.length == 0) {
            return null;
        }

        int maxScore = 0;
        int[] scores = new int[possibleMoves.length];
        for (int moveIndex = 0; moveIndex < possibleMoves.length; moveIndex = moveIndex + 1) {
            // Check this move on a different board.
            int[][] tempBoard = copyMatrix(board);
            play(tempBoard, player, possibleMoves[moveIndex][0], possibleMoves[moveIndex][1]);
            // Other player plays "greedy".
            int[] otherPlayerMove = greedyPlayer(tempBoard, otherPlayer);

            if (otherPlayerMove != null) {
                // If this is not the last move.
                play(tempBoard, otherPlayer, otherPlayerMove[0], otherPlayerMove[1]);
            }

            // Store how many pieces are left for the player.
            int tempScores[] = getScores(tempBoard);
            scores[moveIndex] = tempScores[player - 1];
            if (scores[moveIndex] > maxScore) {
                maxScore = scores[moveIndex];
            }
        }


        // Select the move randomly; take only the maximum beneficial one.
        int index = randomize(scores.length);
        while (scores[index] != maxScore) {
            index = randomize(scores.length);
        }

        // Return the move.
        return possibleMoves[index];
	}

    /**
     * "By location" AI player. Chooses the corners or the places closest to the
     *  center.
     */
	public static int[] byLocationPlayer(int[][] board, int player) {
        int[][] possibleMoves = possibleMoves(board, player);

        if (possibleMoves.length == 0) {
            return null;
        }

        int[] locationValues = getLocationValues(board.length, possibleMoves);
        int maxValue = getMax(locationValues);
        int minValue = getMin(locationValues);

        // Get value of the corners.
        int cornerValue = locationValue(board.length, 0, 0);
        if (maxValue == cornerValue) {
            // We need to select the corner.
            int index = randomize(possibleMoves.length);
            while (locationValues[index] != maxValue) {
                index = randomize(possibleMoves.length);
            }
            return possibleMoves[index];
        }

        // We will select the minimum possible location value.
        int index = randomize(possibleMoves.length);
        while (locationValues[index] != minValue) {
            index = randomize(possibleMoves.length);
        }

        return possibleMoves[index];
	}

    /**
     * Helper function: Calculate distance from location to center of the board.
     *
     * @return
     *   "Distance" to center.
     */
    public static int locationValue(int size, int row, int column) {
        int center = (int) Math.floor(size / 2);

        int distance = 0;

        distance = distance + (Math.min(Math.abs(row - center), Math.abs(row - center - 1)));
        distance = distance + (Math.min(Math.abs(column - center), Math.abs(column - center - 1)));

        return distance;
    }

    /**
     * Get list of location values.
     *
     * @param size
     *   Size of the board.
     * @param moves
     *   Moves to check.
     *
     * @return
     * Return list of location values sorted by moves' indexes.
     */
    public static int[] getLocationValues(int size, int[][] moves) {

        int[] locationValues = new int[moves.length];
        for (int index = 0; index < moves.length; index = index + 1) {
            locationValues[index] = locationValue(size, moves[index][0], moves[index][1]);
        }

        return locationValues;
    }

// -- myPlayer AI helper functions.

    /**
     * Custom AI to win as many games as possible.
     * See PDF attachment.
     */
    public static int[] myPlayer(int[][] board, int player) {
        int[][] possibleMoves = possibleMoves(board, player);

        if (possibleMoves.length == 0) {
            // No moves.
            return null;
        }

        // Get moves' location-based scores.
        int[] locationBaseScore = getLocationScores(possibleMoves, board.length);
        int[] nextPlayerNumberOfMoves = nextPlayerNumberOfMoves(board, player, possibleMoves);

        // Compute scores according to a combination of location score and next
        // player's number of moves.
        int[] computedScores = new int[possibleMoves.length];
        for (int moveIndex = 0; moveIndex < computedScores.length; moveIndex = moveIndex + 1) {
            // Calculate the value of the move according to a secret formula.
            computedScores[moveIndex] = locationBaseScore[moveIndex] - (5 * nextPlayerNumberOfMoves[moveIndex]);
        }

        return possibleMoves[getMaxIndex(computedScores)];
    }

    /**
     * Find out how many moves the other player will have in the next round.
     *
     * @param board
     *  Board matrix to check.
     * @param player
     *  Player to move first.
     * @param moves
     *  List of player moves.
     *
     * @return
     *   Array of  how many moves the other player will have in the next round,
     *   sorted by the moves array indexes.
     */
    public static int[] nextPlayerNumberOfMoves(int[][] board, int player, int[][] moves) {
        int[] scores = new int[moves.length];
        int otherPlayer = player == 1 ? 2 : 1;

        for (int moveIndex = 0; moveIndex < moves.length; moveIndex = moveIndex + 1) {
            // Play the move.
            int[][] tempBoard = copyMatrix(board);
            play(tempBoard, player, moves[moveIndex][0], moves[moveIndex][1]);

            // Check how many moves the other player has.
            int[][] otherPlayerPossibleMoves = possibleMoves(tempBoard, otherPlayer);
            scores[moveIndex] = otherPlayerPossibleMoves.length;
        }

        return scores;
    }

    /**
     * Get location ratings for every place on the board.
     * Uses a better calculation than the one specified in the assignment.
     * See PDF file for details.
     *
     * @param size
     *  Size of board. (size*size).
     *
     * @return
     *   Location score of the specified location.
     */
    public static int[][] getLocationMap(int size){
        int[][] locationMapScore = new int[size][size];
        final int UPPER_BOUND = size - 1;
        final int LOWER_BOUND = 0;

        for (int rowIndex = 0; rowIndex < locationMapScore.length; rowIndex = rowIndex + 1) {
            for (int columnIndex = 0; columnIndex < locationMapScore[rowIndex].length; columnIndex = columnIndex + 1) {
                // default.
                locationMapScore[rowIndex][columnIndex] = 4;

                // edges of inner square.
                if ((rowIndex == LOWER_BOUND + 2 | rowIndex == UPPER_BOUND - 2) & (columnIndex == LOWER_BOUND + 2 | columnIndex == UPPER_BOUND - 2)) {
                    locationMapScore[rowIndex][columnIndex] = 7;
                }
                // semi-outer square.
                if (rowIndex == LOWER_BOUND + 1 | rowIndex == UPPER_BOUND - 1 | columnIndex == LOWER_BOUND + 1 | columnIndex == UPPER_BOUND - 1) {
                    locationMapScore[rowIndex][columnIndex] = -3;
                }
                // outer square.
                if (rowIndex == LOWER_BOUND | rowIndex == UPPER_BOUND || columnIndex == LOWER_BOUND || columnIndex == UPPER_BOUND) {
                    locationMapScore[rowIndex][columnIndex] = 6;
                }
            }
        }

        // Specific points in the board, from least important to important (that way in smaller boards, they will get the right value.

        // semi-outer square - 2 spaces from corner
        locationMapScore[LOWER_BOUND + 1][UPPER_BOUND - 2] = -4;
        locationMapScore[LOWER_BOUND + 1][LOWER_BOUND + 2] = -4;

        locationMapScore[UPPER_BOUND - 1][UPPER_BOUND - 2] = -4;
        locationMapScore[UPPER_BOUND - 1][LOWER_BOUND + 2] = -4;

        locationMapScore[LOWER_BOUND + 2][UPPER_BOUND -1] = -4;
        locationMapScore[LOWER_BOUND + 2][LOWER_BOUND + 1] = -4;

        locationMapScore[UPPER_BOUND - 2][UPPER_BOUND - 1] = -4;
        locationMapScore[UPPER_BOUND - 2][LOWER_BOUND + 1] = -4;

        // outer square - 2 spaces from corner
        locationMapScore[LOWER_BOUND][UPPER_BOUND - 2] = 8;
        locationMapScore[LOWER_BOUND][LOWER_BOUND + 2] = 8;

        locationMapScore[UPPER_BOUND][UPPER_BOUND - 2] = 8;
        locationMapScore[UPPER_BOUND][LOWER_BOUND + 2] = 8;

        locationMapScore[LOWER_BOUND + 2][UPPER_BOUND] = 8;
        locationMapScore[LOWER_BOUND + 2][LOWER_BOUND] = 8;

        locationMapScore[UPPER_BOUND - 2][UPPER_BOUND] = 8;
        locationMapScore[UPPER_BOUND - 2][LOWER_BOUND] = 8;

        //corner-adjacent
        locationMapScore[LOWER_BOUND + 1][LOWER_BOUND + 1] = -24;
        locationMapScore[LOWER_BOUND][LOWER_BOUND + 1] = -8;
        locationMapScore[LOWER_BOUND + 1][LOWER_BOUND] = -8;

        locationMapScore[UPPER_BOUND - 1][UPPER_BOUND - 1] = -24;
        locationMapScore[UPPER_BOUND][UPPER_BOUND - 1] = -8;
        locationMapScore[UPPER_BOUND - 1][UPPER_BOUND] = -8;

        locationMapScore[UPPER_BOUND - 1][LOWER_BOUND + 1] = -24;
        locationMapScore[UPPER_BOUND - 1][UPPER_BOUND] = -8;
        locationMapScore[UPPER_BOUND][UPPER_BOUND - 1] = -8;

        locationMapScore[LOWER_BOUND + 1][UPPER_BOUND - 1] = -24;
        locationMapScore[LOWER_BOUND + 1][UPPER_BOUND] = -8;
        locationMapScore[LOWER_BOUND][UPPER_BOUND - 1] = -8;

        //corners
        locationMapScore[LOWER_BOUND][LOWER_BOUND] = 100;
        locationMapScore[LOWER_BOUND][UPPER_BOUND] = 100;
        locationMapScore[UPPER_BOUND][LOWER_BOUND] = 100;
        locationMapScore[UPPER_BOUND][UPPER_BOUND] = 100;

        return locationMapScore;
    }

    /**
     * Rank moves by location.
     *
     * @param moves
     *   Moves to rank.
     * @param size
     *   Size of board.
     *
     * @return
     *   Ranks of moves (sorted by the same index as the provided moves array).
     */
    public static int[] getLocationScores(int[][] moves, int size) {
        // Get location scores.
        int[][] locationScoreMap = getLocationMap(size);

        int scores[] = new int[moves.length];
        for (int index = 0; index < moves.length; index = index + 1) {
            // Rank move by location.
            scores[index] = locationScoreMap[moves[index][0]][moves[index][1]];
        }

        return scores;
    }

    // -- Generic functions.

    /**
     * Get a random number in the range.
     *
     * @param minimum
     *   Minimum output.
     * @param maximum
     *   Maximum output.
     *
     * @return
     *   Random number between minimum and maximum (including).
     */
    public static int randomize(int maximum, int minimum) {
        return (int) (Math.random() * (maximum - minimum)) + minimum;
    }

    /**
     * Get a random number in the range.
     * Assume minimum = 0.
     *
     * @param maximum
     *   Maximum output.
     *
     * @return
     *   Random number between 0 and maximum (including).
     */
    public static int randomize(int maximum) {
        return randomize(maximum, 0);
    }

    /**
     * Get the lowest value in an array.
     *
     * @param haystack
     *   Array to check.
     *
     * @return
     *   Minimum value in the array.
     */
    public static int getMin(int[] haystack) {
        if (haystack.length == 0) {
            // Nothing in the array.
            return 0;
        }

        int minValue = haystack[0];
        for (int index = 1; index < haystack.length; index = index + 1) {
            if (haystack[index] < minValue) {
                minValue = haystack[index];
            }
        }
        return minValue;
    }

    /**
     * Get the highest value in an array.
     *
     * @param haystack
     *   Array to check.
     *
     * @return
     *   Maximum value in the array.
     */
    public static int getMax(int[] haystack) {
        if (haystack.length == 0) {
            // Nothing in the array.
            return 0;
        }

        int maxValue = haystack[0];
        for (int index = 1; index < haystack.length; index = index + 1) {
            if (haystack[index] > maxValue ) {
                maxValue = haystack[index];
            }
        }
        return maxValue;
    }

    /**
     * Get the highest value's index.
     *
     * @param haystack
     *   Array to search in.
     *
     * @return
     *  Highest value's index.
     */
    public static int getMaxIndex(int[] haystack){
        int maxIndex = 0, maxValue = haystack[0];
        for (int index = 1; index < haystack.length; index = index + 1) {
            if (maxValue < haystack[index]) {
                maxValue = haystack[index];
                maxIndex = index;
            }
        }
        return maxIndex;
    }
}
