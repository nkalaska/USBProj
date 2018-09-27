package com.ubs.EODPositionsCalculator.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ubs.EODPositionsCalculator.beans.Transaction;

public class TransactionReader {
	public static List<Transaction> readTransactions() {
		JSONParser jsonParser = new JSONParser();
		List<Transaction> transactions = new ArrayList<Transaction>();

		try (FileReader reader = new FileReader("resources/1537277231233_Input_Transactions.txt")) {
			Object obj = jsonParser.parse(reader);
			JSONArray objectList = (JSONArray) obj;
			// Iterate over obj array
			objectList.forEach(tmpObj -> parseObject((JSONObject) tmpObj, transactions));

		} catch (FileNotFoundException e) {
			System.out.println("Transacton file not found...");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception while accessing Transacton file...");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Invalid Jason...");
			e.printStackTrace();
		}
		return transactions;
	}

	private static void parseObject(JSONObject jsonObj, List<Transaction> transactions) {

		Transaction transaction = new Transaction();

		long transactionId = (long) jsonObj.get("TransactionId");
		transaction.setTransactionId(transactionId);

		String instrument = (String) jsonObj.get("Instrument");
		transaction.setInstrument(instrument);

		String transactionType = (String) jsonObj.get("TransactionType");
		transaction.setTransactionType(transactionType);

		long TransactionQuantity = (long) jsonObj.get("TransactionQuantity");
		transaction.setTransactionQuantity(TransactionQuantity);

		transactions.add(transaction);
	}
	// public static void main(String[] args) {
	// List<Transaction> transactions = getTransactions();
	// System.out.println("Transactions in main: " + transactions);
	// }
}
