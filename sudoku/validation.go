package sudoku

import (
	"kp/util"
	"math"
)

func getSudokuRows(grid [][]Cell) [][]Cell {
	return grid
}

func getSudokuColumns(grid [][]Cell) [][]Cell {
	size := len(grid)

	columns, _ := util.Map(util.RangeIndices(size), func(i int) ([]Cell, error) {
		col, _ := util.Map(util.RangeIndices(size), func(j int) (Cell, error) {
			return grid[j][i], nil
		})
		return col, nil
	})

	return columns
}

func getSudokuBlocksQuadratic(grid [][]Cell) [][]Cell {
	size := len(grid)
	blockSize := int(math.Sqrt(float64(size)))

	getBlock := func(blockRow, blockCol int) []Cell {
		block := make([]Cell, 0, size)
		for i := 0; i < blockSize; i++ {
			for j := 0; j < blockSize; j++ {
				block = append(
					block,
					grid[blockRow*blockSize+i][blockCol*blockSize+j],
				)
			}
		}
		return block
	}

	blocks, _ := util.Map(util.RangeIndices(size), func(idx int) ([]Cell, error) {
		blockRow := idx / blockSize
		blockCol := idx % blockSize
		return getBlock(blockRow, blockCol), nil
	})

	return blocks
}

func isCompleteList(list []Cell) bool {
	if len(list) == 0 {
		return false
	}
	nonEmpty := func(c Cell) bool {
		return !c.Empty
	}

	filtered := util.Filter[Cell](list, nonEmpty)
	if len(filtered) != len(list) {
		return false
	}

	return true
}

func isCompleteSudoku(grid [][]Cell) bool {
	if grid == nil {
		return false
	}
	for _, row := range grid {
		if !isCompleteList(row) {
			return false
		}
	}
	return !hasErrors(grid)
}

func hasErrors(grid [][]Cell) bool {
	hasDuplicate := func(region []Cell) bool {
		seen := make(map[int]bool)
		for _, cell := range region {
			if cell.Empty {
				continue
			}
			if seen[cell.Value] {
				return true
			}
			seen[cell.Value] = true
		}
		return false
	}

	regions := util.Concatenate(
		getSudokuRows(grid),
		getSudokuColumns(grid),
		getSudokuBlocksQuadratic(grid),
	)

	return util.Reduce(regions, false, func(acc bool, region []Cell) bool {
		return acc || hasDuplicate(region)
	})
}
