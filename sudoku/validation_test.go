package sudoku

import (
	"testing"
)

func TestGetSudokuRegions4x4(t *testing.T) {
	// TODO this is a good test to compare with the scala3 version

	sudokuInputString := "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"
	grid, err := loadSudokuFromString(sudokuInputString)
	if err != nil {
		t.Fatal("no solution found")
	}

	actualRowsGrid := getSudokuRows(grid)
	actualRows, err := getStringFromCellGrid(actualRowsGrid)
	if err != nil {
		t.Error("Unexpected error")
	}

	if sudokuInputString != actualRows {
		println(actualRows)
		t.Error("rows are incorrect")
	}

	//expectedCols := [][]Cell{
	//	{{Value: 1}, {Value: 3}, {Value: 2}, {Value: 4}},
	//	{{Value: 2}, {Value: 4}, {Value: 1}, {Value: 3}},
	//	{{Value: 3}, {Value: 1}, {Value: 4}, {Value: 2}},
	//	{{Value: 4}, {Value: 2}, {Value: 3}, {Value: 1}},
	//}

	expectedCols := "1 3 2 4\n2 4 1 3\n3 1 4 2\n4 2 3 1"
	actualColsGrid := getSudokuColumns(grid)
	actualCols, err := getStringFromCellGrid(actualColsGrid)
	if err != nil {
		t.Error("Unexpected error")
	}

	if expectedCols != actualCols {
		t.Error("columns are incorrect")
	}

	//expectedBlocks := [][]Cell{
	//	{{Value: 1}, {Value: 2}, {Value: 3}, {Value: 4}},
	//	{{Value: 3}, {Value: 4}, {Value: 1}, {Value: 2}},
	//	{{Value: 2}, {Value: 1}, {Value: 4}, {Value: 3}},
	//	{{Value: 4}, {Value: 3}, {Value: 2}, {Value: 1}},
	//}

	expectedBlocks := "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"

	actualBlocksGrid := getSudokuBlocksQuadratic(grid)
	actualBlocks, err := getStringFromCellGrid(actualBlocksGrid)
	if err != nil {
		t.Error("Unexpected error")
	}

	if expectedBlocks != actualBlocks {
		t.Error("blocks are incorrect")
	}

}

func TestIsCompleteList(t *testing.T) {
	if !isCompleteList([]Cell{{1, false}, {2, false}, {3, false}, {4, false}}) {
		t.Fatal("expected complete list")
	}
	if isCompleteList([]Cell{{1, false}, {4, true}}) {
		t.Fatal("expected incomplete list")
	}
	if isCompleteList([]Cell{{1, false}, {2, true}}) {
		t.Fatal("expected incomplete list")
	}
	if isCompleteList([]Cell{}) {
		t.Fatal("expected incomplete list for empty")
	}
}

func TestIsCompleteSudoku(t *testing.T) {
	if isCompleteSudoku(nil) {
		t.Fatal("nil sudoku can not be complete!")
	}

	validSudoku := "1 2 3 4\n3 4 1 2\n2 1 4 3\n4 3 2 1"
	sudoku, err := loadSudokuFromString(validSudoku)
	if err != nil {
		t.Fatal("error")
	}

	if !isCompleteSudoku(sudoku) {
		t.Fatal("This sudoku has no empty fields or errors")
	}

	invalidSudoku := [][]Cell{
		{{1, false}, {2, false}, {3, false}, {4, false}},
		{{3, false}, {4, false}, {1, false}, {2, false}},
		{{2, false}, {1, false}, {4, false}, {3, false}},
		{{1, false}, {Empty: true}, {1, false}, {1, false}},
	}
	if isCompleteSudoku(invalidSudoku) {
		t.Fatal("expected incomplete sudoku")
	}

	notComplete9, err := loadSudokuFromString("3 4 5 7 8 1 9 2 6\n7 2 6 4 3 9 8 5 1\n8 9 1 5 2 6 4 7 3\n9 1 3 2 4 7 5 6 8\n6 8 7 1 9 5 3 4 2\n2 5 4 8 6 3 1 9 7\n4 7 9 3 1 2 6 8 5\n1 6 2 9 5 8 7 3 4\n5 3 8 6 7 4 2 1 _")
	if isCompleteSudoku(notComplete9) {
		t.Fatal("expected incomplete sudoku")
	}

	if err != nil {
		t.Fatal("error")
	}

}

func TestHasErrors(t *testing.T) {
	grid1 := [][]string{
		{"1", "2", "3", "4"},
		{"3", "4", "1", "2"},
		{"2", "1", "4", "3"},
		{"4", "3", "2", "1"},
	}
	cellGrid1, _ := toCellGrid(grid1)
	if hasErrors(cellGrid1) {
		t.Fatal("expected no errors")
	}

	grid2 := [][]string{
		{"1", "2", "3", "4"},
		{"3", "4", "1", "2"},
		{"2", "1", "4", "3"},
		{"4", "3", "3", "2"},
	}
	cellGrid2, _ := toCellGrid(grid2)
	if !hasErrors(cellGrid2) {
		t.Fatal("expected errors")
	}

	grid3 := [][]string{
		{"1", "2", "3", "4"},
		{"3", "4", "1", "2"},
		{"2", "1", "4", "3"},
		{"4", "_", "2", "1"},
	}
	cellGrid3, _ := toCellGrid(grid3)
	if hasErrors(cellGrid3) {
		t.Fatal("expected no errors")
	}

	grid31 := [][]string{
		{"1", "2", "3", "4"},
		{"_", "4", "_", "2"},
		{"2", "1", "4", "3"},
		{"4", "_", "2", "1"},
	}
	cellGrid31, _ := toCellGrid(grid31)
	if hasErrors(cellGrid31) {
		t.Fatal("expected no errors")
	}

	grid4 := [][]string{
		{"_", "_", "_", "1"},
		{"_", "_", "_", "_"},
		{"_", "_", "_", "1"},
		{"_", "_", "_", "_"},
	}
	cellGrid4, _ := toCellGrid(grid4)
	if !hasErrors(cellGrid4) {
		t.Fatal("expected errors")
	}

	grid5 := [][]string{
		{"_", "_", "_", "_"},
		{"_", "_", "_", "_"},
		{"_", "_", "_", "_"},
		{"_", "_", "_", "_"},
	}
	cellGrid5, _ := toCellGrid(grid5)
	if hasErrors(cellGrid5) {
		t.Fatal("expected no errors")
	}

	grid6 := [][]string{
		{"1", "2", "3", "4", "5", "6", "7", "8", "9"},
		{"4", "5", "6", "7", "8", "9", "1", "2", "3"},
		{"7", "8", "9", "1", "2", "3", "4", "5", "6"},
		{"2", "3", "4", "5", "6", "7", "8", "9", "1"},
		{"5", "6", "7", "8", "9", "1", "2", "3", "4"},
		{"8", "9", "1", "2", "3", "4", "5", "6", "7"},
		{"3", "4", "5", "6", "7", "8", "9", "1", "2"},
		{"6", "7", "8", "9", "1", "2", "3", "4", "5"},
		{"9", "1", "2", "3", "4", "5", "6", "7", "8"},
	}
	cellGrid6, _ := toCellGrid(grid6)
	if hasErrors(cellGrid6) {
		t.Fatal("expected no errors")
	}
}
