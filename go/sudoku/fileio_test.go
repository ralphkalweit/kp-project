package sudoku

import (
	"os"
	"strings"
	"testing"
)

func TestReadNonExistingFile(t *testing.T) {
	_, err := readSudokuFile("this_filepath_will_never_exist_ever_vern_ever_ever_never_ever")
	if err == nil {
		t.Fatal("expected an error for non-existing file")
	}
}

func TestReadExistingFile(t *testing.T) {
	filename := "some-file.txt"
	content := "Random Content"
	err := os.WriteFile(filename, []byte(content), 0644)
	if err != nil {
		t.Fatal("could not create test file before testing func readSudokuFile")
	}
	defer os.Remove(filename)

	data, err := readSudokuFile(filename)
	if err != nil {
		t.Fatalf("%v", err)
	}

	if data != content {
		t.Fatalf("%q != %q", content, data)
	}
}

func TestReadEmptyFile(t *testing.T) {
	filename := "test_empty_file.txt"
	defer os.Remove(filename)

	err := os.WriteFile(filename, []byte(""), 0644)
	if err != nil {
		t.Fatalf("%v", err)
	}

	_, err = readSudokuFile(filename)
	if err == nil {
		t.Fatalf("expected an error for empty file")
	}
}

func TestWriteNewFile(t *testing.T) {
	filename := "write-to-this-file.txt"
	content := "new content"
	defer os.Remove(filename)

	err := writeSudokuFile(filename, content)
	if err != nil {
		t.Fatalf("%v", err)
	}

	data, err := os.ReadFile(filename)
	if err != nil {
		t.Fatal("could not read written file")
	}

	if string(data) != content {
		t.Fatalf("%q != %q", content, string(data))
	}
}

func TestOverwriteFile(t *testing.T) {
	filename := "overwrite-this-file.txt"
	initialContent := "initial content"
	newContent := "overwritten content"
	defer os.Remove(filename)

	err := os.WriteFile(filename, []byte(initialContent), 0644)
	if err != nil {
		t.Fatalf("%v", err)
	}

	err = writeSudokuFile(filename, newContent)
	if err != nil {
		t.Fatalf("%v", err)
	}

	data, err := os.ReadFile(filename)
	if err != nil {
		t.Fatal("could not read written file")
	}

	if string(data) != newContent {
		t.Fatalf("%q != %q", newContent, string(data))
	}
}

func TestGetString(t *testing.T) {
	strGrid := StringGrid{{"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}}
	singleString := getStringFromStringGrid(strGrid)

	rows := strings.Split(singleString, "\n")

	if len(rows) != 4 {
		t.Fatal("wrong num of rows")
	}

	for _, row := range rows {
		rowArray := strings.Split(row, " ")
		if len(rowArray) != 4 {
			t.Fatal("invalid row")
		}
	}
}

func TestLoadCellGridFromFile(t *testing.T) {
	strGrid := StringGrid{{"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}}
	filename := "test_load_into_cell_grid.txt"
	defer os.Remove(filename)

	err := writeSudokuFile(filename, getStringFromStringGrid(strGrid))
	if err != nil {
		t.Fatalf("%v", err)
	}

	grid, err := LoadSudokuFromFile(filename)
	if err != nil {
		t.Fatal("Error loading cell grid")
	}

	actual, err := GetStringFromCellGrid(grid)
	if err != nil {
		t.Fatal("Unexpected error")
	}

	expected := getStringFromStringGrid(strGrid)

	if actual != expected {
		t.Fatalf("%q != %q", expected, actual)
	}
}

func TestGetStringFromCellGrid(t *testing.T) {
	cellGrid := LogicalGrid{
		{{Value: 1}, {Value: 3}, {Value: 2}, {Value: 4}},
		{{Value: 2}, {Value: 4}, {Value: 1}, {Value: 3}},
		{{Value: 3}, {Value: 1}, {Value: 4}, {Value: 2}},
		{{Value: 4}, {Value: 2}, {Value: 3}, {Empty: true}},
	}
	str, _ := GetStringFromCellGrid(cellGrid)
	expectedStr := "1 3 2 4\n2 4 1 3\n3 1 4 2\n4 2 3 _"
	if str != expectedStr {
		t.Fatal("error")
	}
}

func TestGetStringFromStringGrid(t *testing.T) {
	strGrid := StringGrid{{"1", "3", "2", "4"}, {"2", "4", "1", "3"}, {"3", "1", "4", "2"}, {"4", "2", "3", "_"}}
	expectedStr := "1 3 2 4\n2 4 1 3\n3 1 4 2\n4 2 3 _"

	actualStr := getStringFromStringGrid(strGrid)
	if actualStr != expectedStr {
		t.Fatal("error")
	}

}
