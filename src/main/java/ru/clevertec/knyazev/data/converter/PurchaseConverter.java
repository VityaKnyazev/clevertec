package ru.clevertec.knyazev.data.converter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.dto.ProductDTO;

public class PurchaseConverter implements Converter<Map<ProductDTO, BigDecimal>, String[]> {

	@Override
	public Map<ProductDTO, BigDecimal> convert(String[] productData) throws ConverterException {
		Map<ProductDTO, BigDecimal> purchases = new HashMap<>();

		for (int i = 0; i < productData.length; i++) {
			String[] productQuantiy = productData[i].split("-");
			
			ProductDTO productDTO = null;
			BigDecimal quantity = null;
			
			try {
				productDTO = new ProductDTO(convertToLong(productQuantiy[0]));
				quantity = convertToBigDecimal(productQuantiy[1]);
			} catch(NumberFormatException e) {
				throw new ConverterException("Error in purchase id or quantity. Incorrect data given!", e);
			}

			if (purchases.containsKey(productDTO)) {
				for (Map.Entry<ProductDTO, BigDecimal> purchase : purchases.entrySet()) {
					if (purchase.getKey().getId() == productDTO.getId()) {
						purchase.setValue(purchase.getValue().add(quantity));
					}
				}
			} else {
				purchases.put(productDTO, quantity);
			}
		}
		
		return purchases;
	}
}