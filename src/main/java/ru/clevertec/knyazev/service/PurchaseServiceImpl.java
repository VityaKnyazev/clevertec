package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.discount.DiscountFactory;
import ru.clevertec.knyazev.service.discount.DiscountService.Group;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.util.PurchaseConverter;
import ru.clevertec.knyazev.util.Settings;
import ru.clevertec.knyazev.view.AbstractReceiptBuilder;
import ru.clevertec.knyazev.view.Receipt;

public class PurchaseServiceImpl implements PurchaseService {
	private StorageService storageService;
	private CasherService casherService;
	private ShopService shopService;
	private AbstractReceiptBuilder receiptBuilder;

	public PurchaseServiceImpl(StorageService storageService, CasherService casherService, ShopService shopService, AbstractReceiptBuilder receiptBuilder) {
		this.storageService = storageService;
		this.casherService = casherService;
		this.shopService = shopService;
		this.receiptBuilder = receiptBuilder;
	}

	@Override
	public Receipt buyPurchases(Map<ProductDTO, BigDecimal> productsDTO, Set<DiscountCardDTO> discountCardsDTO)
			throws ServiceException {
		if (productsDTO == null || productsDTO.isEmpty())
			throw new ServiceException("Error when buying purchases. Products are null or empty!");

		// Получаем купленные товары.
		Map<Long, List<Storage>> boughtProductsInStorages = new HashMap<>();

		for (Map.Entry<ProductDTO, BigDecimal> productsDTOQuantities : productsDTO.entrySet()) {
			ProductDTO productDTO = productsDTOQuantities.getKey();
			BigDecimal quantity = productsDTOQuantities.getValue();

			List<Storage> groupProductInStorages = storageService.buyProductFromStorages(productDTO.getId(), quantity);
			boughtProductsInStorages.put(productDTO.getId(), groupProductInStorages);
		}

		if (boughtProductsInStorages.isEmpty())
			throw new ServiceException("Nothing to buy on current products ids and count!");

		// Расчет скидок. Делим товары на группы для расчета и применения скидок.
		BigDecimal totalProductGroupsDiscount = DiscountFactory.getInstance()
				.createDiscountService(Group.DISCOUNT_PRODUCT_GROUP).applyDiscount(boughtProductsInStorages);
		BigDecimal totalCardsDiscount = DiscountFactory.getInstance()
				.createDiscountService(Group.DISCOUNT_CARD_GROUP, discountCardsDTO)
				.applyDiscount(boughtProductsInStorages);

		BigDecimal totalPrice = storageService.getBoughtProductsTotalPrice(boughtProductsInStorages);
		totalPrice = totalPrice.setScale(Settings.PRICE_SCALE_VALUE, RoundingMode.HALF_UP);

		List<PurchaseDTO> purchases = new PurchaseConverter().convertToDTO(boughtProductsInStorages);

		Shop shop = shopService.showShop();

		Long casherId = casherService.showCasherId();
		casherService.increaseCasherId();

		BigDecimal totalDiscountPrice = totalPrice.subtract(totalProductGroupsDiscount).subtract(totalCardsDiscount);
		totalDiscountPrice = totalDiscountPrice.setScale(Settings.PRICE_SCALE_VALUE, RoundingMode.HALF_UP);
		
		return receiptBuilder.setCasherIdWithDateTime(casherId).setShop(shop).setPurchases(purchases)
				.setTotalPrice(totalPrice).setProductGroupsDiscountValue(totalProductGroupsDiscount)
				.setDiscountCardsValue(totalCardsDiscount).setTotalDiscountPrice(totalDiscountPrice).build();

	}

}
