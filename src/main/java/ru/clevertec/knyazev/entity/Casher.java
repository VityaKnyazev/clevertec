package ru.clevertec.knyazev.entity;

public class Casher {
	private Long casherId;

	public Casher(Long casherId) {
		this.casherId = casherId;
	}

	public Long getId() {
		return casherId;
	}

	public void setId(Long casherId) {
		this.casherId = casherId;
	}
	
	
}
