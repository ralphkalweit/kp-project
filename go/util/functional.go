package util

func Reduce[T any, U any](slice []T, initial U, reducer func(acc U, item T) U) U {
	acc := initial
	for _, item := range slice {
		acc = reducer(acc, item)
	}
	return acc
}

func Map[T any, U any](input []T, transform func(T) (U, error)) ([]U, error) {
	output := make([]U, len(input))
	for i, v := range input {
		res, err := transform(v)
		if err != nil {
			return nil, err
		}
		output[i] = res
	}
	return output, nil
}

func Filter[T any](input []T, filterFn func(T) bool) []T {
	result := make([]T, 0, len(input))
	for _, item := range input {
		if filterFn(item) {
			result = append(result, item)
		}
	}
	return result
}

func FlatMap[T any, U any](input []T, transform func(T) ([]U, error)) ([]U, error) {
	var result []U
	for _, item := range input {
		partials, err := transform(item)
		if err != nil {
			return nil, err
		}
		result = append(result, partials...)
	}
	return result, nil
}
