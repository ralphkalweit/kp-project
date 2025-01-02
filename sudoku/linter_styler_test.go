package sudoku

import (
	"os"
	"strings"
	"testing"
)

func TestAdjustNumDigits(t *testing.T) {
	cells := []Cell{
		{Value: 1},
		{Value: 10},
		{Empty: true},
	}
	kinds := []int{3, 5, 1}

	for _, c := range cells {
		baseStr := c.String()
		for _, uniLen := range kinds {
			if len(baseStr) > uniLen {
				if _, err := toStringWithLen(c, uniLen); err == nil {
					t.Fatal("expected to throw an error")
				}
			} else {
				actual, err := toStringWithLen(c, uniLen)
				if err != nil {
					t.Fatal(err)
				}
				if len(actual) != uniLen {
					t.Fatal("unexpected length")
				}
			}
		}
	}

	val, err := toStringWithLen(Cell{Empty: true}, 3)
	if err != nil || val != "  _" {
		t.Fatal(err)
	}

	val, err = toStringWithLen(Cell{Empty: true}, 2)
	if err != nil || val != " _" {
		t.Fatal(err)
	}

	val, err = toStringWithLen(Cell{Value: 1}, 1)
	if err != nil || val != "1" {
		t.Fatal(err)
	}
}

func Test4x4Empty(t *testing.T) {
	input := "_    _    _   1\n_  _  _  _\n_ _ _ 3\n_ _ _ _"
	cellGrid, err := loadSudokuFromString(input)
	if err != nil {
		t.Fatal(err)
	}
	linted, err := getStringFromCellGrid(cellGrid)
	if err != nil {
		t.Fatal(err)
	}
	if linted == input {
		t.Fatal("expected different string after linting")
	}
	if parts := strings.Split(linted, " "); len(parts) != 13 {
		t.Fatal("expected 13 tokens")
	}
}

func TestConvertLintConvert16x16(t *testing.T) {
	sudoku16 := `5 _ _ _ _ 8 _ _ 3 _ _ _ _ _ _ 7
_ 7 _ 1 _ _ _ 9 _ _ _ _ _ _ _ _
_ _ _ _ 6 _ _ _ 5 _ _ _ _ 12 _ _
_ 2 _ _ _ _ 6 _ _ _ _ _ _ _ _ _
_ _ _ 3 _ 7 _ _ _ _ 9 _ _ _ _ _
_ _ _ _ _ _ _ 4 _ _ _ _ _ 1 _ _
12 _ _ _ _ _ _ _ 7 _ _ _ _ _ _ 6
_ _ _ _ _ 5 _ _ _ _ _ _ 9 _ _ _
_ _ _ _ _ _ _ _ _ _ _ 14 _ _ 6 _
9 _ _ _ _ _ _ _ _ _ _ _ _ _ _ _
_ 12 _ _ _ 6 _ _ _ _ _ _ _ _ _ _
_ _ _ _ 5 _ _ _ 9 _ _ _ _ _ _ _
_ _ 9 _ _ _ _ _ _ _ _ _ 8 _ _ _
_ _ _ _ _ _ _ 7 _ _ 14 _ _ _ _ _
_ _ _ _ _ 9 _ _ _ 13 _ _ _ _ 8 _
6 _ _ _ _ _ _ _ _ _ _ _ _ _ _ 5`

	filePath := "../CLC_16.sudoku"
	if err := os.WriteFile(filePath, []byte(sudoku16), 0644); err != nil {
		t.Fatal(err)
	}
	defer os.Remove(filePath)

	grid, err := loadSudokuFromFile(filePath)
	if err != nil {
		t.Fatal(err)
	}
	newString, err := getStringFromCellGrid(grid)
	if err != nil {
		t.Fatal(err)
	}

	origSplit := strings.Split(sudoku16, " ")
	newSplit := strings.Split(newString, " ")

	if len(newSplit) <= len(origSplit) {
		t.Fatal("unexpected short length")
	}

	if len(strings.ReplaceAll(newString, " ", "")) != len(strings.ReplaceAll(sudoku16, " ", "")) {
		t.Error("incorrect length")
	}

	if err := writeSudokuFile(filePath, newString); err != nil {
		t.Fatal(err)
	}
}

func TestEmpty100x100(t *testing.T) {
	size := 100
	var sb strings.Builder
	for i := 0; i < size; i++ {
		row := strings.Repeat("_ ", size)
		sb.WriteString(strings.TrimSpace(row))
		if i < size-1 {
			sb.WriteByte('\n')
		}
	}
	sudoku, err := loadSudokuFromString(sb.String())
	linted, err := getStringFromCellGrid(sudoku)
	if err != nil {
		t.Fatal(err)
	}

	lines := strings.Split(linted, "\n")
	if len(lines) != size {
		t.Fatal("incorrect length")
	}
	for _, row := range lines {
		cells := strings.Fields(row)
		if len(cells) != size {
			t.Fatal("incorrect number of cells")
		}
		joined := strings.ReplaceAll(row, " ", "")
		if len(joined) != size {
			t.Fatal("incorrect amount of empty cells")
		}
	}
}
