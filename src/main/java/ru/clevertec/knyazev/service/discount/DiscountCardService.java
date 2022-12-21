package ru.clevertec.knyazev.service.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.dao.DiscountCardDAO;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.entity.DiscountCard;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.util.Settings;

class DiscountCardService extends AbstractDiscountService<DiscountCardDTO, Integer> {

	private DiscountCardDAO discountCardDAO;
	private Set<DiscountCardDTO> discountCardsDTO;

	DiscountCardService(DiscountCardDAO discountCardDAO, Set<DiscountCardDTO> discountCardsDTO) {
		this.discountCardDAO = discountCardDAO;
		this.discountCardsDTO = discountCardsDTO;
	}

	@Override
	Integer calculateDiscount(Collection<DiscountCardDTO> discountCardsDTO) throws ServiceException {
		int discountValueInPercent = 0;

		for (DiscountCardDTO discountCardDTO : discountCardsDTO) {
			String discountCardNumber = discountCardDTO.getNumber();
			if (!discountCardDAO.isDiscountCardExists(discountCardNumber)) {
				throw new ServiceException("Discount card with number=" + discountCardNumber + " not exists!");
			}

			DiscountCard discountCard = discountCardDAO.getDiscountCardByNumber(discountCardNumber);

			discountValueInPercent += discountCard.getDiscountValue();

		}

		return discountValueInPercent >= Settings.MAX_DISCOUNT_CARD_VAUE ? Settings.MAX_DISCOUNT_CARD_VAUE
				: discountValueInPercent;
	}

	@Override
	Map<Long, List<Storage>> divideOnGroup(Map<Long, List<Storage>> boughtProductsInStorages) {
		Map<Long, List<Storage>> productGroupForDiscountCard = new HashMap<>();

		boughtProductsInStorages.forEach((id, storageList) -> {
			if (!storageList.get(0).getProduct().getAuction()) {
				productGroupForDiscountCard.put(id, storageList);
			}
		});

		return productGroupForDiscountCard;
	}

	@Override
	public BigDecimal applyDiscount(Map<Long, List<Storage>> boughtProductsInStorages) {
		BigDecimal totalDiscountCardValue = new BigDecimal(0);
		totalDiscountCardValue.setScale(Settings.PRICE_SCALE_VALUE, RoundingMode.HALF_UP);

		if (discountCardsDTO == null || discountCardsDTO.isEmpty()) {
			return totalDiscountCardValue;
		}

		if (boughtProductsInStorages == null || boughtProductsInStorages.isEmpty()) {
			return totalDiscountCardValue;
		}

		Map<Long, List<Storage>> productGroups = divideOnGroup(boughtProductsInStorages);
		if (productGroups.isEmpty())
			return totalDiscountCardValue;

		try {
			Integer discountPercent = super.calculateFinalCardsDiscount(calculateDiscount(discountCardsDTO));
			
			for (Map.Entry<Long, List<Storage>> listStorages : productGroups.entrySet()) {
				totalDiscountCardValue = totalDiscountCardValue.add(listStorages.getValue().stream()
						.map(product -> product.getPrice().multiply(product.getQuantity())).reduce((a, b) -> a.add(b))
						.orElse(new BigDecimal(0))
						.multiply(new BigDecimal(discountPercent).divide(new BigDecimal(100))));
			}
		} catch (ServiceException e) {
			// Nothing: Service exception because discount card number was not found
		}

		return totalDiscountCardValue;
	}

}
