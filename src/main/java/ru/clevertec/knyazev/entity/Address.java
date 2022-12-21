package ru.clevertec.knyazev.entity;

public class Address {
	String postalCode;
	String country;
	String city;
	String street;
	String buildingNumber;

	public Address(String postalCode, String country, String city, String street, String buildingNumber) {
		this.postalCode = postalCode;
		this.country = country;
		this.city = city;
		this.street = street;
		this.buildingNumber = buildingNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}
}
