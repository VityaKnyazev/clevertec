package ru.clevertec.knyazev.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import ru.clevertec.knyazev.dto.DiscountCardDTO;
import ru.clevertec.knyazev.dto.ProductDTO;
import ru.clevertec.knyazev.service.exception.ServiceException;
import ru.clevertec.knyazev.view.Receipt;

public interface PurchaseService {
	Receipt buyPurchases(Map<ProductDTO, BigDecimal> purchases, Set<DiscountCardDTO> discountCardsDTO) throws ServiceException;
}