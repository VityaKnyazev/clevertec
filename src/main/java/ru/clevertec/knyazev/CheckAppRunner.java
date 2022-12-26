package ru.clevertec.knyazev;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.data.DataFactory;
import ru.clevertec.knyazev.data.DataReaderWriterDecorator;
import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;

public class CheckAppRunner {

	public static void main(String[] args) throws IOException {
		
		DataReaderWriterDecorator dataReaderWriter = new DataFactory(args).getDataReaderWriter();

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