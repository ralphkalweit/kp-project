package main

import (
	"fmt"
	"kp/go/sudoku"
	"os"
)

type SolvingStrategyFunc func(sudoku.LogicalGrid) sudoku.LogicalGrid

func main() {
	fmt.Println("Starting demonstration!")

	// HARD-CODED PARAMETERS FOR DEMO:
	filename := "9x9.sudoku"
	solvingStrategyChoice := "Backtracking"

	strategies := map[string]SolvingStrategyFunc{
		"Backtracking": sudoku.Backtracking,
	}

	demonstration(filename, strategies[solvingStrategyChoice])
}

func demonstration(filename string, strategy SolvingStrategyFunc) {
	defer fmt.Println("End of Demonstration!")

	loadedSudoku, err := sudoku.LoadSudokuFromFile(filename)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Error loading sudoku: %v\n", err)
	}

	str, _ := sudoku.GetStringFromCellGrid(loadedSudoku)
	if str == "" {
		return
	}
	fmt.Println(str)

	if loadedSudoku.HasErrors() {
		fmt.Println("Sudoku has errors")
	} else if loadedSudoku.IsComplete() {
		fmt.Println("Sudoku is complete!")
	} else {
		chosenStrategy := strategy

		hopefullySolvedSudoku := chosenStrategy(loadedSudoku)

		if hopefullySolvedSudoku.IsComplete() {
			fmt.Println("Found a solution for the sudoku!")
		} else {
			fmt.Println("Could not find a complete solution with this strategy. Please try a different strategy next time.")
		}
		str, _ = sudoku.GetStringFromCellGrid(hopefullySolvedSudoku)
		fmt.Println(str)
		err := sudoku.SaveSudokuToFile("9x9.sudoku.edit", hopefullySolvedSudoku)
		if err != nil {
			fmt.Printf("Error saving sudoku: %v\n", err)
		}
	}
}
