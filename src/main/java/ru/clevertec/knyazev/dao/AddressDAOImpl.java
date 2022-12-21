package ru.clevertec.knyazev.dao;

import ru.clevertec.knyazev.entity.Address;

public class AddressDAOImpl implements AddressDAO {
	private static final Address ADDRESS = new Address("128546", "Belarus", "Mogilev", "Bulgakov str", "15А");

	@Override
	public Address getAddress() {
		return ADDRESS;
	}
}
