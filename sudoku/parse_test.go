package sudoku

import (
	"testing"
)

func TestParseInvalidShape(t *testing.T) {
	sudoku := "_\n_ 2"

	_, err := parseSudokuString(sudoku)
	if err == nil {
		t.Fatal("expected error for invalid shape")
	}
}

func TestParse1x1(t *testing.T) {
	sudoku := "_"

	grid, err := parseSudokuString(sudoku)
	if err != nil {
		t.Fatal("unexpected error")
	}
	if len(grid) != 1 || len(grid[0]) != 1 {
		t.Fatal("invalid grid dimensions")
	}
}

func TestParse3x3(t *testing.T) {
	sudoku := "_ _ _\n_ _ _\n_ _ _"

	_, err := parseSudokuString(sudoku)
	if err == nil {
		t.Fatal("expected error for unsupported grid size 3x3")
	}
}

func TestWithExtraSpaces(t *testing.T) {
	sudoku := "_  _ _ _\n_ _ _ _ \n_ _ _       _\n_   _   _     _"

	grid, err := parseSudokuString(sudoku)
	if err != nil {
		t.Fatal("unexpected error")
	}
	if len(grid) != 4 || len(grid[0]) != 4 {
		t.Fatal("invalid grid dimensions")
	}
}

func TestUnsupportedChars(t *testing.T) {
	sudoku := "_ a\n_ ("

	_, err := parseSudokuString(sudoku)
	if err == nil {
		t.Fatal("expected error for unsupported characters")
	}
}

func TestContainsOnlyValidStrings(t *testing.T) {
	list := []string{"1", "2", "_", "4", "5"}

	if !listContainsOnlyValidStrings(list, 5) {
		t.Fatal("expected valid")
	}
	if !listContainsOnlyValidStrings(list, 6) {
		t.Fatal("expected valid")
	}
	if listContainsOnlyValidStrings(list, 4) {
		t.Fatal("expected invalid")
	}

	list2 := []string{"-1"}
	if listContainsOnlyValidStrings(list2, 5) {
		t.Fatal("expected invalid")
	}

	wrongGrid := [][]string{
		{"_", "a"},
		{"2", "_"},
	}
	_, err := toCellGrid(wrongGrid)
	if err == nil {
		t.Fatal("expected error for invalid character in grid")
	}
}

func TestToCellGridEmptyCells(t *testing.T) {
	strGrid := [][]string{
		{"_", "_", "_", "_"},
		{"_", "_", "_", "_"},
		{"_", "_", "_", "_"},
		{"_", "_", "_", "_"},
	}

	cellGrid, err := toCellGrid(strGrid)
	if err != nil {
		t.Fatal("unexpected error")
	}

	for _, row := range cellGrid {
		for _, cell := range row {
			if !cell.Empty || cell.Value != 0 {
				t.Fatal("expected all cells to be empty")
			}
		}
	}
}

func TestToCellGridValidNumbers(t *testing.T) {
	strGrid := [][]string{
		{"1", "2", "3", "4"},
		{"3", "2", "1", "4"},
		{"3", "2", "1", "4"},
		{"3", "2", "1", "4"},
	}

	cellGrid, err := toCellGrid(strGrid)
	if err != nil {
		t.Fatal("unexpected error")
	}

	expected := [][]Cell{
		{{Value: 1, Empty: false}, {Value: 2, Empty: false}, {Value: 3, Empty: false}, {Value: 4, Empty: false}},
		{{Value: 3, Empty: false}, {Value: 2, Empty: false}, {Value: 1, Empty: false}, {Value: 4, Empty: false}},
		{{Value: 3, Empty: false}, {Value: 2, Empty: false}, {Value: 1, Empty: false}, {Value: 4, Empty: false}},
		{{Value: 3, Empty: false}, {Value: 2, Empty: false}, {Value: 1, Empty: false}, {Value: 4, Empty: false}},
	}

	for i, row := range cellGrid {
		for j, cell := range row {
			if cell.Empty != expected[i][j].Empty || cell.Value != expected[i][j].Value {
				t.Fatalf("mismatch at (%d, %d): expected %+v, got %+v", i, j, expected[i][j], cell)
			}
		}
	}
}

func TestToCellGridInvalidCharacter(t *testing.T) {
	strGrid := [][]string{
		{"1", "2", "3", "4"},
		{"3", "2", "1", "a"},
		{"3", "2", "1", "4"},
		{"3", "2", "1", "4"},
	}

	_, err := toCellGrid(strGrid)
	if err == nil {
		t.Fatal("expected error for invalid character")
	}
}

func TestToStringGrid(t *testing.T) {
	strGrid := [][]string{
		{"1", "2", "3", "4"},
		{"3", "2", "1", "4"},
		{"3", "2", "1", "4"},
		{"3", "2", "1", "4"},
	}

	cellGrid, err := toCellGrid(strGrid)

	actualStrGrid, err := toStringGrid(cellGrid)

	if err != nil {
		t.Fatal("unexpected error")
	}

	for i := range strGrid {
		if len(actualStrGrid[i]) != len(strGrid[i]) {
			t.Fatal("incorrect row len")
		}
		for j := range strGrid[i] {
			if actualStrGrid[i][j] != strGrid[i][j] {
				t.Error("mismatch at of expected and actual row")
			}
		}
	}

}
