package com.ubs.EODPositionsCalculator.beans;

public class Position {

	//Stock symbol of the instrument
	String instrument;

	//Ledger Account Number
	String account;

	//Account type I (Internal) / E (External)
	String accountType;

	//Total postion quantity
	long quantity;

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Position [instrument=" + instrument + ", account=" + account + ", accountType=" + accountType
				+ ", quantity=" + quantity + "]";
	}
}
