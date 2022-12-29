package ru.clevertec.knyazev.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ru.clevertec.knyazev.dao.AddressDAO;
import ru.clevertec.knyazev.dao.AddressDAOImpl;
import ru.clevertec.knyazev.dao.CasherDAO;
import ru.clevertec.knyazev.dao.CasherDaoImpl;
import ru.clevertec.knyazev.dao.ProductDAO;
import ru.clevertec.knyazev.dao.ProductDAOImpl;
import ru.clevertec.knyazev.dao.ShopDAO;
import ru.clevertec.knyazev.dao.ShopDAOImpl;
import ru.clevertec.knyazev.dao.StorageDAO;
import ru.clevertec.knyazev.dao.StorageDAOImpl;
import ru.clevertec.knyazev.service.CasherService;
import ru.clevertec.knyazev.service.CasherServiceImpl;
import ru.clevertec.knyazev.service.PurchaseService;
import ru.clevertec.knyazev.service.PurchaseServiceImpl;
import ru.clevertec.knyazev.service.ShopService;
import ru.clevertec.knyazev.service.ShopServiceImpl;
import ru.clevertec.knyazev.service.StorageService;
import ru.clevertec.knyazev.service.StorageServiceImpl;
import ru.clevertec.knyazev.view.AbstractReceiptBuilder;
import ru.clevertec.knyazev.view.ReceiptBuilderImpl;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ru.clevertec.knyazev.rest.controller")
public class AppConfig {

	@Bean
	PurchaseService purchaseServiceImpl(StorageService storageServiceImpl, CasherService casherServiceImpl, ShopService shopServiceImpl, AbstractReceiptBuilder receiptBuilderImpl) {
		return new PurchaseServiceImpl(storageServiceImpl, casherServiceImpl, shopServiceImpl, receiptBuilderImpl);
	}
	
	@Bean
	StorageService storageServiceImpl(StorageDAO storageDAOImpl) {
		return new StorageServiceImpl(storageDAOImpl);
	}
	
	@Bean
	CasherService casherServiceImpl(CasherDAO casherDAOImpl) {
		return new CasherServiceImpl(casherDAOImpl);
	}	
	
	@Bean
	ShopService shopServiceImpl(ShopDAO shopDAOImpl) {
		return new ShopServiceImpl(shopDAOImpl);
	}
	
	@Bean
	StorageDAO storageDAOImpl(ProductDAO productDAOImpl) {
		return new StorageDAOImpl(productDAOImpl);
	}
	
	@Bean
	ProductDAO productDAOImpl() {
		return new ProductDAOImpl();
	}
	
	@Bean
	CasherDAO casherDAOImpl() {
		return new CasherDaoImpl();
	}
	
	@Bean
	ShopDAO shopDAO(AddressDAO addressDAOImpl) {
		return new ShopDAOImpl(addressDAOImpl);
	}
	
	@Bean
	AddressDAO addressDAOImpl() {
		return new AddressDAOImpl();
	}
	
	@Bean
	AbstractReceiptBuilder consoleReceiptBuilder() {
		return new ReceiptBuilderImpl();		
	}
}
