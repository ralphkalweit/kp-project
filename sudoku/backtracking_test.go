package sudoku

import "testing"

func TestBacktrackingSolvable4x4(t *testing.T) {
	sudokuString := "1 2 3 4\n3 _ _ 2\n2 1 4 3\n4 3 2 1"
	expectedSolution := "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"

	grid, err := loadSudokuFromString(sudokuString)
	if err != nil {
		t.Fatal("no solution found")
	}

	solution := Backtracking(grid)
	if solution == nil {
		t.Fatal("no solution found")
	}

	solutionString, err := GetStringFromCellGrid(solution)
	if err != nil {
		t.Fatal("failed to convert solution to string")
	}

	if solutionString != expectedSolution {
		t.Fatal("solution does not match expected result")
	}
}

func TestBacktrackingEmpty4x4(t *testing.T) {
	sudokuString := "_ _ _ _\n_ _ _ _\n_ _ _ _\n_ _ _ _"

	grid, err := loadSudokuFromString(sudokuString)
	if err != nil {
		t.Fatal("no solution found")
	}

	solution := Backtracking(grid)
	if solution == nil {
		t.Fatal("no solution found")
	}
}

func TestBacktrackingSolvable9x9(t *testing.T) {
	sudokuString := "_ 6 _ 3 _ 8 1 _ _\n5 8 1 _ _ 9 6 _ _\n_ _ 3 5 _ _ _ _ _\n_ 9 _ 8 _ 2 _ _ _\n _ _ _ 7 4 5 9 3 6\n3 5 _ _ _ 6 8 _ 4\n1 3 5 _ _ _ _ _ _\n7 _ 8 6 1 3 4 _ _\n9 4 _ _ _ 7 3 _ _"

	stringGrid, err := parseSudokuString(sudokuString)
	if err != nil {
		t.Fatal("failed to parse sudoku string")
	}

	cellGrid, err := toCellGrid(stringGrid)
	if err != nil {
		t.Fatal("failed to convert to cell grid")
	}

	solution := Backtracking(cellGrid)
	if solution == nil {
		t.Fatal("no solution found")
	}
}

func TestNotSolvable(t *testing.T) {
	sudokuStrings := []string{
		"1 _ _ _\n2 _ 3 4\n_ _ _ _\n_ _ _ _",
		"1 _ _ _\n_ 2 3 4\n_ _ _ _\n_ _ _ _",
		"1 _ _ _\n_ _ _ 4\n_ _ _ 3\n_ _ _ 2",
	}

	for _, sudokuString := range sudokuStrings {
		grid, err := loadSudokuFromString(sudokuString)
		if err != nil {
			t.Fatal("no solution found")
		}

		solution := Backtracking(grid)
		if solution != nil {
			t.Fatal("there should be no solution")
		}
	}
}
