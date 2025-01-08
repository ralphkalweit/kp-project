package sudoku

import (
	"kp/util"
)

func Backtracking(grid [][]Cell) [][]Cell {
	if IsCompleteSudoku(grid) {
		return grid
	}

	row, col, found := findNextEmptyCell(grid)
	if !found {
		return grid
	}

	candidateRange := candidates(len(grid))

	transformFn := func(candidate int) ([][][]Cell, error) {
		testGrid := placeCandidate(grid, row, col, candidate)
		if HasErrors(testGrid) {
			return nil, nil
		}

		newGrid := placeCandidate(grid, row, col, candidate)
		solution := Backtracking(newGrid)
		if solution == nil {
			return nil, nil
		}

		return [][][]Cell{solution}, nil
	}

	solutions, _ := util.FlatMap(candidateRange, transformFn)

	if len(solutions) > 0 {
		return solutions[0]
	}

	return nil
}

func candidates(n int) []int {
	result := make([]int, n)
	for i := 1; i <= n; i++ {
		result[i-1] = i
	}
	return result
}

func placeCandidate(grid [][]Cell, row, col, candidate int) [][]Cell {
	newGrid := copyGrid(grid)
	newGrid[row][col] = Cell{Value: candidate, Empty: false}
	return newGrid
}

func copyGrid(grid [][]Cell) [][]Cell {
	newGrid := make([][]Cell, len(grid))
	for i := range grid {
		newGrid[i] = make([]Cell, len(grid[i]))
		copy(newGrid[i], grid[i])
	}
	return newGrid
}

func findNextEmptyCell(grid [][]Cell) (int, int, bool) {
	for r := 0; r < len(grid); r++ {
		for c := 0; c < len(grid[r]); c++ {
			if grid[r][c].Empty {
				return r, c, true
			}
		}
	}
	return 0, 0, false
}
