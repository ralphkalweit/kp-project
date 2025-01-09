package sudoku

type Cell struct {
	Value int
	Empty bool
}

type CellList []Cell
type LogicalGrid []CellList

func (grid LogicalGrid) IsComplete() bool        { return IsCompleteSudoku(grid) }
func (grid LogicalGrid) HasErrors() bool         { return HasErrors(grid) }
func (grid LogicalGrid) getRows() LogicalGrid    { return getSudokuRows(grid) }
func (grid LogicalGrid) getColumns() LogicalGrid { return getSudokuColumns(grid) }
func (grid LogicalGrid) getBlocks() LogicalGrid  { return getSudokuBlocks(grid) }

type StringList []string
type StringGrid []StringList
