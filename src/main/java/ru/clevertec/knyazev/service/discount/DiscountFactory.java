package ru.clevertec.knyazev.service.discount;

import java.util.Set;

import ru.clevertec.knyazev.dao.DiscountCardDAOImpl;
import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.service.discount.DiscountService.Group;
import ru.clevertec.knyazev.service.exception.ServiceException;

public class DiscountFactory {
	private static DiscountFactory discountFactory;

	private DiscountFactory() {
	}

	public DiscountService<?, ?> createDiscountService(Group group)
			throws ServiceException {		
		return switch (group) {
		case DISCOUNT_PRODUCT_GROUP -> new DiscountProductGroupService();
		default -> throw new ServiceException("Error occured when choosing discount type policy!");
		};
	}
	
	public DiscountService<?, ?> createDiscountService(Group group, Set<DiscountCardDTO> discountCardDTO) throws ServiceException {

		if (discountCardDTO != null) {
			return new DiscountCardService(new DiscountCardDAOImpl(), discountCardDTO);
		}
		
		return createDiscountService(group);
	}

	public static DiscountFactory getInstance() {
		if (discountFactory == null) {
			discountFactory = new DiscountFactory();
		}

		return discountFactory;
	}

}
