package com.ubs.EODPositionsCalculator;

import static com.ubs.EODPositionsCalculator.utils.Constants.TRANSACTION_BUY;
import static com.ubs.EODPositionsCalculator.utils.Constants.TRANSACTION_SELL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubs.EODPositionsCalculator.beans.EODPosition;
import com.ubs.EODPositionsCalculator.beans.Position;
import com.ubs.EODPositionsCalculator.beans.PositionKey;
import com.ubs.EODPositionsCalculator.beans.Transaction;
import com.ubs.EODPositionsCalculator.utils.EODPositionsWriter;
import com.ubs.EODPositionsCalculator.utils.PositionReader;
import com.ubs.EODPositionsCalculator.utils.TransactionReader;

public class Main {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("Starting to process EOD Positions...");
		startJob();
		long totalTime = (System.currentTimeMillis() - start);
		System.out.println("Finished processing EOD Positions! Total time taken in seconds: " + (totalTime / 1000));
	}

	private static void startJob() {
		List<Transaction> transactions = TransactionReader.readTransactions();
		System.out.println("*****Transactions*****");
		System.out.println(transactions);
		System.out.println("**********************");
		System.out.println("*****Initial Positions*****");
		Map<PositionKey, Position> initialPositions = PositionReader.readPositions();
		System.out.println(initialPositions);
		System.out.println("***************************");
		calculateAndWriteEODPositions(transactions, initialPositions);
	}

	/**
	 * If Transaction Type =B , For AccountType=E, Quantity=Quantity +
	 * TransactionQuantity For AccountType=I, Quantity=Quantity -
	 * TransactionQuantity If Transaction Type =S , For AccountType=E,
	 * Quantity=Quantity - TransactionQuantity For AccountType=I, Quantity=Quantity
	 * + TransactionQuantity
	 **/
	private static void calculateAndWriteEODPositions(final List<Transaction> transactions,
			final Map<PositionKey, Position> initialPositions) {
		Map<PositionKey, EODPosition> eodPositions = new HashMap<PositionKey, EODPosition>();
		// iterate over all transactions
		for (Transaction transaction : transactions) {
			String instrument = transaction.getInstrument();
			if (TRANSACTION_BUY.equals(transaction.getTransactionType())) {
				processTransaction(initialPositions, eodPositions, transaction, instrument, true);
			} else if (TRANSACTION_SELL.equals(transaction.getTransactionType())) {
				processTransaction(initialPositions, eodPositions, transaction, instrument, false);
			}
		}
		populateDeltasAndEODPositionsForZeroActivity(initialPositions, eodPositions);
		System.out.println("eodPositions: " + eodPositions);
		// write eod positions to csv
		writeEODPositions(eodPositions);
	}

	private static void writeEODPositions(final Map<PositionKey, EODPosition> eodPositions) {
		EODPositionsWriter.writePositionsData(eodPositions);
	}

	public static void processTransaction(final Map<PositionKey, Position> initialPositions,
			final Map<PositionKey, EODPosition> eodPositions, final Transaction transaction, final String instrument,
			final boolean isBuyTransaction) {
		PositionKey keyExtern = new PositionKey(instrument, "101", "E");
		// if a transaction was already processed
		if (eodPositions.containsKey(keyExtern)) {
			EODPosition eodPosition = eodPositions.get(keyExtern);
			if (isBuyTransaction) {
				eodPosition.setQuantity(eodPosition.getQuantity() + transaction.getTransactionQuantity());
			} else {
				eodPosition.setQuantity(eodPosition.getQuantity() - transaction.getTransactionQuantity());
			}
			eodPosition.setDelta(0);
			// eodPositions.put(keyExtern, eodPosition);// this is not required

		} else // if it's the first transaction get it from initial positions and populate it
				// on EOD map
		{
			Position initialPosition = initialPositions.get(keyExtern);
			EODPosition newEODPosition = new EODPosition(initialPosition.getInstrument(), initialPosition.getAccount(),
					initialPosition.getAccountType(), initialPosition.getQuantity(), 0);
			if (isBuyTransaction) {
				newEODPosition.setQuantity(initialPosition.getQuantity() + transaction.getTransactionQuantity());
			} else {
				newEODPosition.setQuantity(initialPosition.getQuantity() - transaction.getTransactionQuantity());
			}
			eodPositions.put(keyExtern, newEODPosition);
		}

		PositionKey keyIntern = new PositionKey(instrument, "201", "I");
		// if a transaction was already processed
		if (eodPositions.containsKey(keyIntern)) {
			EODPosition eodPosition = eodPositions.get(keyIntern);
			if (isBuyTransaction) {
				eodPosition.setQuantity(eodPosition.getQuantity() - transaction.getTransactionQuantity());
			} else {
				eodPosition.setQuantity(eodPosition.getQuantity() + transaction.getTransactionQuantity());
			}
			eodPosition.setDelta(0);
			// eodPositions.put(keyIntern, eodPosition);

		} else // if it's the first transaction get it from initial positions and populate it
				// on EOD map
		{
			Position initialPosition = initialPositions.get(keyIntern);
			EODPosition newEODPosition = new EODPosition(initialPosition.getInstrument(), initialPosition.getAccount(),
					initialPosition.getAccountType(), initialPosition.getQuantity(), 0);
			if (isBuyTransaction) {
				newEODPosition.setQuantity(initialPosition.getQuantity() - transaction.getTransactionQuantity());
			} else {
				newEODPosition.setQuantity(initialPosition.getQuantity() + transaction.getTransactionQuantity());
			}
			eodPositions.put(keyIntern, newEODPosition);
		}
	}

	private static void populateDeltasAndEODPositionsForZeroActivity(final Map<PositionKey, Position> initialPositions,
			final Map<PositionKey, EODPosition> eodPositions) {
		// iterate over initial positions and find delta by using latestQty - initialQty
		for (PositionKey posKey : initialPositions.keySet()) {
			Position iPos = initialPositions.get(posKey);
			EODPosition eodPos = eodPositions.get(posKey);
			if (null != eodPos) {
				// transactions were found for this positionKey, calculate delta
				eodPos.setDelta(eodPos.getQuantity() - iPos.getQuantity());
			} else {
				// no transactions for this positionKey, zero activity case - set delta zero and
				// populate eodPositions!
				eodPositions.put(posKey, new EODPosition(iPos.getInstrument(), iPos.getAccount(), iPos.getAccountType(),
						iPos.getQuantity(), 0));
			}
		}
	}
}
