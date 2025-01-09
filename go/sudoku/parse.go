package sudoku

import (
	"errors"
	"fmt"
	"kp/go/util"
	"math"
	"strconv"
	"strings"
)

func parseSudokuString(input string) (StringGrid, error) {
	rows := strings.Split(strings.TrimSpace(input), "\n")
	grid := StringGrid{}

	sqrt := int(math.Sqrt(float64(len(rows))))
	if sqrt*sqrt != len(rows) {
		return nil, errors.New("expected shape n^2 * n^2")
	}

	grid, err := util.Map(rows, func(row string) (StringList, error) {
		cells := strings.Fields(row)
		if len(cells) != len(rows) {
			return nil, errors.New("invalid Sudoku shape")
		}

		invalidCells := util.Filter(cells, func(cell string) bool {
			return !isValidCell(cell, len(rows))
		})
		if len(invalidCells) > 0 {
			return nil, errors.New("unsupported character in Sudoku")
		}

		return cells, nil
	})
	if err != nil {
		return nil, err
	}

	return grid, nil
}

func isValidCell(cell string, maxNumber int) bool {
	if num, err := strconv.Atoi(cell); err == nil {
		return num <= maxNumber
	}
	return cell == "_"
}

func listContainsOnlyValidStrings(list StringList, maxNumber int) bool {
	invalidStrings := util.Filter(list, func(str string) bool {
		if str == "_" {
			return false
		}
		num, err := strconv.Atoi(str)
		return err != nil || num < 1 || num > maxNumber
	})

	return len(invalidStrings) == 0
}

func (c Cell) String() string {
	if c.Empty {
		return "_"
	}
	return fmt.Sprintf("%d", c.Value)
}

func toCellGrid(stringGrid StringGrid) (LogicalGrid, error) {
	return util.Map(stringGrid, func(row StringList) (CellList, error) {
		return util.Map(row, func(cell string) (Cell, error) {
			if cell == "_" {
				return Cell{Empty: true}, nil
			}

			num, err := strconv.Atoi(cell)
			if err != nil {
				return Cell{}, errors.New("invalid character in grid")
			}

			return Cell{Value: num, Empty: false}, nil
		})
	})
}

func toStringGrid(grid LogicalGrid) (StringGrid, error) {
	cellWidth := len(strconv.Itoa(len(grid)))

	return util.Map(grid, func(row CellList) (StringList, error) {
		return util.Map(row, func(cell Cell) (string, error) {
			return toStringWithLen(cell, cellWidth)
		})
	})
}

func GetStringFromCellGrid(grid LogicalGrid) (string, error) {
	stringGrid, err := toStringGrid(grid)
	if err != nil {
		return "", err
	}
	return getStringFromStringGrid(stringGrid), nil
}

func loadSudokuFromString(input string) (LogicalGrid, error) {
	stringGrid, err := parseSudokuString(input)
	if err != nil {
		return LogicalGrid{}, err
	}
	cellGrid, err := toCellGrid(stringGrid)
	if err != nil {
		return LogicalGrid{}, err
	}
	return cellGrid, nil
}

func LoadSudokuFromFile(filePath string) (LogicalGrid, error) {
	fileContent, err := readSudokuFile(filePath)
	if err != nil {
		return LogicalGrid{}, err
	}
	return loadSudokuFromString(fileContent)
}

func SaveSudokuToFile(filePath string, grid LogicalGrid) error {
	fileContent, err := GetStringFromCellGrid(grid)
	if err != nil {
		return err
	}

	err = writeSudokuFile(filePath, fileContent)
	if err != nil {
		return err
	}
	return nil
}
