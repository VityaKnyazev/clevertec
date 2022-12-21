package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Address;
import ru.clevertec.knyazev.entity.Shop;

public class ShopDAOImpl implements ShopDAO {
	private AddressDAO addressDAO;
	
	public ShopDAOImpl(AddressDAO addressDAO) {
		this.addressDAO = addressDAO;
	}

	@Override
	public Shop getShop() {
		Address address = addressDAO.getAddress();
		Shop shop = new Shop("SHoP Of Art ANd Wine", address, "+375 44 569 25 64");
		return shop;
	}

}
