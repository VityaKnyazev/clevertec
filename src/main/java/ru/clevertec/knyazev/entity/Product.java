package ru.clevertec.knyazev.entity;

public class Product {
	private Long id;
	private String description;
	private Boolean isAuction;

	public Product() {
	}

	public Product(Long id, String description, Boolean isAuction) {
		this.id = id;
		this.description = description;
		this.isAuction = isAuction;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getAuction() {
		return isAuction;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAuction(Boolean isAuction) {
		this.isAuction = isAuction;
	}

	@Override
	public int hashCode() {
		return (int) id.longValue() * description.hashCode() * isAuction.hashCode();
	}

	@Override
	public boolean equals(Object objectProduct) {
		if (objectProduct == null) {
			return false;
		}
		if (objectProduct == this) {
			return true;
		}

		if (objectProduct instanceof Product) {
			Product custProduct = (Product) objectProduct;
			return this.getId().equals(custProduct.getId())
					&& this.getDescription().equals(custProduct.getDescription())
					&& this.getAuction().equals(custProduct.getAuction());
		}

		return false;
	}

	@Override
	public String toString() {
		return "Product[" + "id=" + id + ", description=" + description + ", isAuction=" + isAuction + "]";
	}
}