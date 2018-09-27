package com.ubs.EODPositionsCalculator.beans;

public class EODPosition {

	// Stock symbol of the instrument
	String instrument;

	// Ledger Account Number
	String account;

	// Account type I (Internal) / E (External)
	String accountType;

	// Total postion quantity
	long quantity;

	// Net change in positions during the day
	long delta;

	public EODPosition(String instrument, String account, String accountType, long quantity, long delta) {
		this.instrument = instrument;
		this.account = account;
		this.accountType = accountType;
		this.quantity = quantity;
		this.delta = delta;
	}

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

	public long getDelta() {
		return delta;
	}

	public void setDelta(long delta) {
		this.delta = delta;
	}

	@Override
	public String toString() {
		return "EODPosition [instrument=" + instrument + ", account=" + account + ", accountType=" + accountType
				+ ", quantity=" + quantity + ", delta=" + delta + "]";
	}
}
