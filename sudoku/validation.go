package sudoku

import (
	"kp/util"
	"math"
)

func getSudokuRows(grid LogicalGrid) LogicalGrid {
	return grid
}

func getSudokuColumns(grid LogicalGrid) LogicalGrid {
	size := len(grid)

	columns, _ := util.Map(util.RangeIndices(size), func(i int) (CellList, error) {
		col, _ := util.Map(util.RangeIndices(size), func(j int) (Cell, error) {
			return grid[j][i], nil
		})
		return col, nil
	})

	return columns
}

func getSudokuBlocks(grid LogicalGrid) LogicalGrid {
	size := len(grid)
	blockSize := int(math.Sqrt(float64(size)))

	getBlock := func(blockRow, blockCol int) CellList {
		block := make(CellList, 0, size)
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

	blocks, _ := util.Map(util.RangeIndices(size), func(idx int) (CellList, error) {
		blockRow := idx / blockSize
		blockCol := idx % blockSize
		return getBlock(blockRow, blockCol), nil
	})

	return blocks
}

func isCompleteList(list CellList) bool {
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

func IsCompleteSudoku(grid LogicalGrid) bool {
	if grid == nil {
		return false
	}
	for _, row := range grid {
		if !isCompleteList(row) {
			return false
		}
	}
	return !HasErrors(grid)
}

func HasErrors(grid LogicalGrid) bool {
	hasDuplicate := func(region CellList) bool {
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
		grid.getRows(),
		grid.getColumns(),
		grid.getBlocks(),
	)

	return util.Reduce(regions, false, func(acc bool, region CellList) bool {
		return acc || hasDuplicate(region)
	})
}
