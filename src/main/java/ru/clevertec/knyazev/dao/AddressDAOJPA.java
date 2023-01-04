package ru.clevertec.knyazev.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Address;

@Repository
public class AddressDAOJPA implements AddressDAO {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Address findById(Long id) {
		Address address = entityManager.find(Address.class, id);
		
		return address != null ? address : null;
	}

}
