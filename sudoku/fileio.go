package sudoku

import (
	"errors"
	"kp/util"
	"os"
	"strings"
)

func readSudokuFile(filename string) (string, error) {
	data, err := os.ReadFile(filename)
	if err != nil {
		if errors.Is(err, os.ErrNotExist) {
			return "", errors.New("file does not exist")
		}
		return "", err
	}
	if len(data) == 0 {
		return "", errors.New("file is empty")
	}
	return string(data), nil
}

func writeSudokuFile(filename string, content string) error {
	return os.WriteFile(filename, []byte(content), 0644)
}

func getStringFromStringGrid(stringGrid [][]string) string {
	return util.Reduce(stringGrid, "", func(acc string, row []string) string {
		if acc == "" {
			return strings.Join(row, " ")
		}
		return acc + "\n" + strings.Join(row, " ")
	})
}
