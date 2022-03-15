# NYTSudoku
A program to solve the daily NYT sudokus

Run the main method in Sudoku.java and open the daily NYT sudoku in a new window.

### Algorithm

The program reads the sudoku board from the visible part of the screen. The method of reading the numbers from the board might be dependent on the screen resolution (needs further testing). The solving algorithm is done in Python using backtracking and a forward pass iteration that searches for naked singles. While there are many more solving techniques, backtracking + naked singles is comparably fast for the overwhelming majority of sudokus.

### Performance

The bot solves the NYT easy/medium/hard sudokus in ~5 seconds each, much faster than a human!
