package pl.piomin.service.customer.model;

public class Account {

	private String id;
	private String number;
	private int amount;

	public Account() {

	}

	public Account(String id, String number, int amount) {
		this.id = id;
		this.number = number;
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", number=" + number + ", amount=" + amount + "]";
	}

}
