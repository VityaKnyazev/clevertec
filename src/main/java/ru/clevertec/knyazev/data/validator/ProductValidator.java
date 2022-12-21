package ru.clevertec.knyazev.data.validator;

import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class ProductValidator implements Validator<ProductDTO> {
	private static final Long ID_MIN_VALUE = 1L;

	@Override
	public void validate(ProductDTO productDTO) throws ValidatorException {
		if (productDTO.getId() < ID_MIN_VALUE) {
			throw new ValidatorException("Error in id value. id=" + productDTO.getId() + ". Id must be equals or above 1");
		}
	}

}