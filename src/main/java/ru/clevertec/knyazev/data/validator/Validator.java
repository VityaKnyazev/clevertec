package ru.clevertec.knyazev.data.validator;

import ru.clevertec.knyazev.data.exception.ValidatorException;

public interface Validator<T> {
	
	/**
	 * 
	 * @param <T>
	 * @param t value that need validation
	 * @throws ValidatorException when validation error occurred
	 */
	void validate(T t) throws ValidatorException;

}
