package ru.clevertec.knyazev;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.data.ConsoleDataWriter;
import ru.clevertec.knyazev.data.ConverterValidatorDataReaderWriter;
import ru.clevertec.knyazev.data.StandardDataReader;
import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;

public class CheckStandardRunner {
	
	public static void main(String[] args) {		
		ConverterValidatorDataReaderWriter dataReaderWriter = new ConverterValidatorDataReaderWriter(new StandardDataReader(args), new ConsoleDataWriter());
		
		try {
			Map<ProductDTO, BigDecimal> purchases = dataReaderWriter.readPurchases();
			Set<DiscountCardDTO> discountCardsDTO = dataReaderWriter.readDiscountCards();
			
			PurchaseService purchaseService = ContextInitializer.createContext();
			Receipt recept = purchaseService.buyPurchases(purchases, discountCardsDTO);
			
			dataReaderWriter.writeData(recept.toString());
		} catch (IOException | ConverterException | ValidatorException | ServiceException e) {
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}	
		
	}
	
}