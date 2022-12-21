package ru.clevertec.knyazev.entity;

import java.math.BigDecimal;

public class Storage implements Comparable<Storage>{
	private Long id;
	private Product product;
	private Unit unit;
	private BigDecimal price;
	private BigDecimal quantity;

	public Storage(Long id, Product product, Unit unit, BigDecimal price, BigDecimal quantity) {
		this.id = id;
		this.product = product;
		this.unit = unit;
		this.price = price;
		this.quantity = quantity;
	}

	public Long getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public Unit getUnit() {
		return unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	

	public static enum Unit {
		т, кг, г, ед, л, шт
	}

	@Override
	public int compareTo(Storage o) {
		return price.compareTo(o.getPrice());
	}
}
