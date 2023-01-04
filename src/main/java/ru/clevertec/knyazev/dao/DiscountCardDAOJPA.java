package ru.clevertec.knyazev.dao;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.DiscountCard;

@Repository
public class DiscountCardDAOJPA implements DiscountCardDAO {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public DiscountCard getDiscountCardByNumber(String number) {
		DiscountCard discountCard = (DiscountCard) entityManager.createQuery("SELECT discountCard FROM DiscountCard discountCard WHERE discountCard.number = ?1").setParameter(1, number).getSingleResult();
		
		return discountCard != null ? discountCard : null;
	}

	@Override
	public DiscountCard saveDiscountCard(DiscountCard discountCard) {
		if (discountCard.getId() == null) {
			entityManager.persist(discountCard);
			return discountCard;
		}
		
		return null;
	}

	@Override
	public boolean isDiscountCardExists(String discountCardNumber) {
		int quantity = 0;		
		quantity = entityManager.createNativeQuery("SELECT count(id) FROM discount_card WHERE card_number = ?1").setParameter(1, discountCardNumber).getFirstResult();
		
		return quantity > 0;
	}

}
