package ru.clevertec.knyazev.util;

import java.util.function.Function;

public abstract class AbstractToDTOConverter<T, U> {
	private Function<T, U> functionToDTO;
	
	AbstractToDTOConverter(Function<T, U> functionToDTO) {
		this.functionToDTO = functionToDTO;
	}
	
	public U convertToDTO(T convertingObject) {
		return functionToDTO.apply(convertingObject);
	}
}
