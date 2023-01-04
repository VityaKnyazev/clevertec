package ru.clevertec.knyazev.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Shop;

@Repository
public class ShopDAOJPA implements ShopDAO {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Shop findById(Long id) {
		Shop shop = entityManager.find(Shop.class, id);
		
		return shop != null ? shop : null;
	}

}
