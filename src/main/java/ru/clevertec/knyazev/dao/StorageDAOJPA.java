package ru.clevertec.knyazev.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.clevertec.knyazev.entity.Storage;
import ru.clevertec.knyazev.util.Settings;

@Repository
public class StorageDAOJPA implements StorageDAO {
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Boolean isStorageExistsByProductId(Long productId) {
		long quantity = 0;		
		quantity = (long) entityManager.createNativeQuery("SELECT COUNT(id) FROM storage WHERE product_id = ?1").setParameter(1, productId).getSingleResult();
		return quantity > 0;
	}

	@Override
	public BigDecimal getProductQuantityInStorages(Long productId) {
		Number dbResultQuantity = (Number) entityManager.createNativeQuery("SELECT SUM(quantity) FROM storage WHERE product_id = ?1").setParameter(1, productId).getSingleResult();
		BigDecimal productQuantity = new BigDecimal(dbResultQuantity.longValue());
		productQuantity = productQuantity.setScale(Settings.QUANTITY_SCALE_VALUE, RoundingMode.HALF_UP);
		
		return productQuantity;
	}

	@Override
	public List<Storage> getProductGroupInStoragesById(Long productId) {
				
		List<Storage> productsGroup = entityManager.createQuery("SELECT storage FROM Storage storage WHERE storage.product.id = ?1", Storage.class).setParameter(1, productId).getResultList();
		
		return productsGroup;		
	}

	@Override
	public Storage updateStorage(Storage storage) {		
		if (storage.getId() != null) {
			entityManager.persist(storage);
			return storage;
		}
		
		return null;
	}

	@Override
	public void deleteStorage(Long storageId) {
		entityManager.createQuery("DELETE FROM Storage storage WHERE storage.id = ?1").setParameter(1, storageId).executeUpdate();		
	}

}
