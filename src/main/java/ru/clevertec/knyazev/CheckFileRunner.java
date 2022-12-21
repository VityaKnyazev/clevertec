package ru.clevertec.knyazev;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.data.ConverterValidatorDataReaderWriter;
import ru.clevertec.knyazev.data.FileDataReader;
import ru.clevertec.knyazev.data.FileDataWriter;
import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.util.Settings;
import ru.clevertec.knyazev.view.Receipt;

public class CheckFileRunner {

	public static void main(String[] args) {
		final String outputFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss dd-MM-YYYY")) + Settings.OUTPUT_FILE_NAME;
		
		try {
			ConverterValidatorDataReaderWriter dataReaderWriter = new ConverterValidatorDataReaderWriter(new FileDataReader(args), new FileDataWriter(outputFileName));
			Map<ProductDTO, BigDecimal> purchases = dataReaderWriter.readPurchases();
			Set<DiscountCardDTO> discountCardsDTO = dataReaderWriter.readDiscountCards();
			
			PurchaseService purchaseService = ContextInitializer.createContext();
			Receipt recept = purchaseService.buyPurchases(purchases, discountCardsDTO);
			
			dataReaderWriter.writeData(recept.toString());
		} catch (IOException | ConverterException | ValidatorException | ServiceException e) {
			System.out.println(e.getMessage());
//			e.printStackTrace();
		}
	}

}