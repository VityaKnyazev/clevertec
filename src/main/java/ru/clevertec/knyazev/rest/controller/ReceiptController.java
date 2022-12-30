package ru.clevertec.knyazev.rest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.clevertec.knyazev.data.DataFactory;
import ru.clevertec.knyazev.data.DataReaderWriterDecorator;
import ru.clevertec.knyazev.data.exception.ConverterException;
import ru.clevertec.knyazev.data.exception.ValidatorException;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;

@Controller
public class ReceiptController {
	private final static String NOTHING_TO_BUY = "No information about purchasing products. Fill out purchase-parameter of request for bought in format URL?purchase=id-quantity.";

	private PurchaseService purchaseService;

	@Autowired
	ReceiptController(@Qualifier(value = "purchaseServiceImpl") PurchaseService purchaseService) {
		this.purchaseService = purchaseService;
	}

	@GetMapping(value = "/receipt", produces = "text/plain; charset=utf-8")
	@ResponseBody
	public ResponseEntity<String> getReceipt(@RequestParam(name = "purchase") List<String> purchases,
			@RequestParam(name = "card", required = false) List<String> cards) {
		final DataFactory.InputSource INPUT_VAL = DataFactory.InputSource.STANDARD;
		final DataFactory.OutputSource OUTPUT_VAL = DataFactory.OutputSource.CONSOLE;
		
		if (purchases == null || purchases.isEmpty()) {
			return new ResponseEntity<String>(NOTHING_TO_BUY, HttpStatus.BAD_REQUEST);
		} 
		
		List<String> purchasesCards = new ArrayList<>() {
			private static final long serialVersionUID = 15215815L;

			{
				add(INPUT_VAL.name().toLowerCase());
				add(OUTPUT_VAL.name().toLowerCase());
			}
		};
		
		purchasesCards.addAll(purchases);

		if (cards != null && !cards.isEmpty()) {
			purchasesCards.addAll(cards);
		}
		
		String[] inputData = purchasesCards.toArray(new String[0]);

		try {
			DataReaderWriterDecorator dataReaderWriter = new DataFactory(inputData).getDataReaderWriter();
			
			Map<ProductDTO, BigDecimal> products = dataReaderWriter.readPurchases();
			Set<DiscountCardDTO> discountCardsDTO = dataReaderWriter.readDiscountCards();
			
			Receipt receipt = purchaseService.buyPurchases(products, discountCardsDTO);

			return new ResponseEntity<String>(receipt.toString(), HttpStatus.OK);
			
		} catch (IOException | ConverterException | ValidatorException | ServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	
	}
}
