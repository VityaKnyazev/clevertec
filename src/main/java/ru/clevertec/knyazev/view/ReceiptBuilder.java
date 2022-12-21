package ru.clevertec.knyazev.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ru.clevertec.knyazev.dto.PurchaseDTO;
import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;
import ru.clevertec.knyazev.entity.Storage.Unit;
import ru.clevertec.knyazev.util.Settings;

public class ReceiptBuilder {
	private String casherId;
	private String shop;
	private LocalDateTime dateTime;
	private String purchases;
	private String totalPrice;
	private String discountCardsValue;
	private String productGroupsDiscountValue;
	private String totalDiscountPrice;

	public ReceiptBuilder() {
		dateTime = LocalDateTime.now();
	}

	public ReceiptBuilder setCasherIdWithDateTime(Long casherId) {		
		this.casherId = String.format("%-100S", "casher: № " + casherId + "   date: " + dateTime.format(DateTimeFormatter.ofPattern("dd-MM-YYYY")))
				+ System.lineSeparator() + String.format("%100S", "time: " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
				+ System.lineSeparator();
		return this;
	}

	public ReceiptBuilder setShop(Shop shop) {
		final String SHOP_FORMAT = "%100s";
		
		String name = shop.getName();
		Address address = shop.getAddress();
		String phone = shop.getPhone();

		this.shop = String.format(SHOP_FORMAT, name) + System.lineSeparator()
				+ String.format(SHOP_FORMAT,
						address.getPostalCode() + ", " + address.getCountry() + ", " + address.getCity() + ", "
								+ address.getStreet() + ", " + address.getBuildingNumber())
				+ System.lineSeparator() + String.format(SHOP_FORMAT, "Tel: " + phone) + System.lineSeparator();
		return this;
	}

	public ReceiptBuilder setPurchases(List<PurchaseDTO> purchasesDTO) {
		final String HEADER_FORMAT = "%-10S %-8S %-63S %-10S %-10S";
		final String BODY_FORMAT = "%-10.3f %-8s %-63s %-10.2f %-10.2f";
		
		final String header = String.format(HEADER_FORMAT, "quantity", "unit", "description", "price", "total")
				+ System.lineSeparator();

		String bodyData = "";

		for (PurchaseDTO purchaseDTO : purchasesDTO) {
			BigDecimal quantity = purchaseDTO.getQuantity();
			Unit unit = purchaseDTO.getUnit();
			String description = purchaseDTO.getDescription();
			BigDecimal price = purchaseDTO.getPrice();

			BigDecimal productGroupPrice = new BigDecimal(0);
			productGroupPrice.setScale(Settings.PRICE_SCALE_VALUE, RoundingMode.HALF_UP);
			productGroupPrice = quantity.multiply(price);

			bodyData += String.format(BODY_FORMAT, quantity, unit, description, price, productGroupPrice)
					+ System.lineSeparator();
		}

		String purchases = header + bodyData;

		this.purchases = purchases;
		return this;
	}
	
	public ReceiptBuilder setTotalPrice(BigDecimal totalPrice) {
		final String TOTAL_PRICE_FORMAT = "%-8S %92.2f";
		this.totalPrice = String.format(TOTAL_PRICE_FORMAT, "total: ", totalPrice) + System.lineSeparator();
		return this;
	}

	public ReceiptBuilder setDiscountCardsValue(BigDecimal discountCardsValue) {
		final String DISCOUNT_CARDS_VALUE_FORMAT = "%-8S %78.2f";
		
		this.discountCardsValue = String.format(DISCOUNT_CARDS_VALUE_FORMAT, "cards discount value:", discountCardsValue)
				+ System.lineSeparator();
		return this;
	}

	public ReceiptBuilder setProductGroupsDiscountValue(BigDecimal productGroupsDiscountValue) {
		final String PRODUCT_GROUPS_DISCOUNT_FORMAT = "%-8S %72.2f";
		
		this.productGroupsDiscountValue = String.format(PRODUCT_GROUPS_DISCOUNT_FORMAT, "discount on product groups:",
				productGroupsDiscountValue) + System.lineSeparator();
		return this;
	}

	public ReceiptBuilder setTotalDiscountPrice(BigDecimal totalDiscountPrice) {
		final String DISCOUNT_TOTAL_PRICE_FORMAT = "%-8S %80.2f";
		
		this.totalDiscountPrice = String.format(DISCOUNT_TOTAL_PRICE_FORMAT, "total with discount:", totalDiscountPrice);
		return this;
	}

	public Receipt build() {
		return new Receipt(casherId, shop, purchases, totalPrice, discountCardsValue, productGroupsDiscountValue,
				totalDiscountPrice);
	}

}