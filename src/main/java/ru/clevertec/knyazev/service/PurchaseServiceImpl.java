package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.discount.DiscountServiceComposite;
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

	@Transactional
	@Override
	public Receipt buyPurchases(Map<ProductDTO, BigDecimal> productsDTO)
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
		BigDecimal totalProductGroupsDiscount = DiscountServiceComposite.getInstance().getTotalProductGroupsDiscount(boughtProductsInStorages);
		BigDecimal totalCardsDiscount = DiscountServiceComposite.getInstance().getTotalCardsDiscount(boughtProductsInStorages);

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
