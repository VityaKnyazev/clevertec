package ru.clevertec.knyazev.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.Storage.Unit;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;
import ru.clevertec.knyazev.view.ReceiptBuilderImpl;

public class ReceiptControllerTests {
	private PurchaseService purchaseServiceMock;
	private ReceiptController receiptController;
	private Receipt receipt;

	private final static String RECEIPT_REQUEST = "/receipt";

	private MockMvc mockMVC;

	@BeforeEach
	public void setUp() {
		purchaseServiceMock = Mockito.mock(PurchaseService.class);
		receiptController = new ReceiptController(purchaseServiceMock);
		mockMVC = MockMvcBuilders.standaloneSetup(receiptController).defaultRequest(MockMvcRequestBuilders.get("/"))
				.build();

		List<PurchaseDTO> purchases = new ArrayList<>() {
			private static final long serialVersionUID = 7534980376943621763L;

			{
				add(new PurchaseDTO(new BigDecimal(3), Unit.шт, "Кофеинка темно-серая", new BigDecimal(2)));
			}
		};

		Address Adddress = new Address("120589", "Russia", "MOSCOW", "Chapaeva, str", "128");
		Shop shop = new Shop("TestShop", Adddress, "+7xx 85 698 55");

		receipt = new ReceiptBuilderImpl().setCasherIdWithDateTime(1L).setShop(shop).setPurchases(purchases)
				.setDiscountCardsValue(BigDecimal.ZERO).setProductGroupsDiscountValue(BigDecimal.ZERO)
				.setTotalPrice(new BigDecimal(6)).setTotalDiscountPrice(new BigDecimal(6)).build();
	}

	@Test
	public void whenGetReceipt() throws Exception {
		final String purchaseParamName = "purchase";
		final String purchaseParamValue = "1-3";

		final String discountCardParamName = "card";
		final String discountCardParamValue = "card-123456789";

		Mockito.when(purchaseServiceMock.buyPurchases(Mockito.anyMap(), Mockito.anySet())).thenReturn(receipt);

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST)
				.param(purchaseParamName, purchaseParamValue).param(discountCardParamName, discountCardParamValue))
				.andReturn();

		final int expectedStatus = 200;
		final String expectedContentType = "text/plain; charset=utf-8";

		assertNotNull(expectedContentType, result.getResponse().getContentType());
		assertEquals(expectedStatus, result.getResponse().getStatus());
	}

	@Test
	public void whenGetReceiptWithoutPurchaseParam() throws Exception {

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST)).andReturn();

		final int expectedStatus = 400;

		assertEquals(expectedStatus, result.getResponse().getStatus());
	}

	@Test
	public void whenGetReceiptOnEmptyPurchase() throws Exception {
		final String purchaseParamName = "purchase";
		final String purchaseParamValue = "";

		Mockito.when(purchaseServiceMock.buyPurchases(Mockito.anyMap(), Mockito.anySet())).thenReturn(receipt);

		MvcResult result = mockMVC
				.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST).param(purchaseParamName, purchaseParamValue))
				.andReturn();

		final int expectedStatus = 400;

		assertEquals(expectedStatus, result.getResponse().getStatus());
	}
	
	@Test
	public void whenGetReceiptAndBuyPurchaseThrowsServiceException() throws Exception {
		final String purchaseParamName = "purchase";
		final String purchaseParamValue = "1-3";

		final String discountCardParamName = "card";
		final String discountCardParamValue = "card-123456789";

		Mockito.when(purchaseServiceMock.buyPurchases(Mockito.anyMap(), Mockito.anySet())).thenThrow(ServiceException.class);

		MvcResult result = mockMVC.perform(MockMvcRequestBuilders.get(RECEIPT_REQUEST)
				.param(purchaseParamName, purchaseParamValue).param(discountCardParamName, discountCardParamValue))
				.andReturn();

		final int expectedStatus = 400;

		assertEquals(expectedStatus, result.getResponse().getStatus());
	}
}