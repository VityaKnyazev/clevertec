package ru.clevertec.knyazev.view;

public class Receipt {
	private String casherIdWithDateTime;
	private String shop;
	private String purchases;
	private String totalPrice;
	private String discountCardsValue;
	private String productDiscountGroupsValue;
	private String totalDiscountPrice;

	Receipt(String casherIdWithDateTime, String shop, String purchases, String totalPrice,
			String discountCardsValue, String productDiscountGroupsValue, String totalDiscountPrice) {
		this.casherIdWithDateTime = casherIdWithDateTime;
		this.shop = shop;
		this.purchases = purchases;
		this.totalPrice = totalPrice;
		this.discountCardsValue = discountCardsValue;
		this.productDiscountGroupsValue = productDiscountGroupsValue;
		this.totalDiscountPrice = totalDiscountPrice;
	}
	
	public String getCasherIdWithDateTime() {
		return casherIdWithDateTime;
	}



	public String getShop() {
		return shop;
	}



	public String getPurchases() {
		return purchases;
	}



	public String getTotalPrice() {
		return totalPrice;
	}



	public String getDiscountCardsValue() {
		return discountCardsValue;
	}



	public String getProductDiscountGroupsValue() {
		return productDiscountGroupsValue;
	}



	public String getTotalDiscountPrice() {
		return totalDiscountPrice;
	}

	@Override
	public String toString() {
		return String.format("%50S", "CASH RECEIPT") + System.lineSeparator() +
				shop +
				casherIdWithDateTime +
				purchases +
				totalPrice +
				productDiscountGroupsValue +
				discountCardsValue + 
				totalDiscountPrice;
	}

}