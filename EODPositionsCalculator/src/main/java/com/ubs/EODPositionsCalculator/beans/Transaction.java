package com.ubs.EODPositionsCalculator.beans;

public class Transaction {
	//	Id of transaction record
	long transactionId;
	
	//Stock symbol of the instrument
	String instrument;

	//transaction type - B (Buy) / S (Sell)
	String transactionType;

	//Quantity of transaction
	long TransactionQuantity;
	
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public long getTransactionQuantity() {
		return TransactionQuantity;
	}

	public void setTransactionQuantity(long transactionQuantity) {
		TransactionQuantity = transactionQuantity;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", instrument=" + instrument + ", transactionType="
				+ transactionType + ", TransactionQuantity=" + TransactionQuantity + "]";
	}
}
