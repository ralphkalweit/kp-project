package util

func RangeIndices(n int) []int {
	idx := make([]int, n)
	for i := 0; i < n; i++ {
		idx[i] = i
	}
	return idx
}

func Concatenate[T any](lists ...[]T) []T {
	var result []T
	for _, part := range lists {
		result = append(result, part...)
	}
	return result
}
