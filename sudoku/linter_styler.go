package sudoku

import (
	"fmt"
	"strings"
)

func toStringWithLen(cell Cell, width int) (string, error) {
	s := cell.String()
	if len(s) > width {
		return "", fmt.Errorf("string %q too long for width=%d", s, width)
	}
	return leftPad(s, width), nil
}

func leftPad(s string, totalLen int) string {
	if len(s) >= totalLen {
		return s
	}
	return strings.Repeat(" ", totalLen-len(s)) + s
}
