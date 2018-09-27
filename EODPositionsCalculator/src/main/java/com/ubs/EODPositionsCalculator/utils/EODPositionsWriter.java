package com.ubs.EODPositionsCalculator.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVWriter;
import com.ubs.EODPositionsCalculator.beans.EODPosition;
import com.ubs.EODPositionsCalculator.beans.PositionKey;

public class EODPositionsWriter {
	public static void writePositionsData(final Map<PositionKey, EODPosition> eodPositions) {

		String fileName = "EODPositions_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".csv";
		File file = new File("resources/" + fileName);
		long maxDelta = 0;
		String maxInstrument = "";
		long minDelta = 0;
		String minInstrument = "";

		try {
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			List<String[]> data = new ArrayList<String[]>();
			data.add(new String[] { "Instrument", "Account", "AccountType", "Quantity", "Delta" });
			for (Map.Entry<PositionKey, EODPosition> entry : eodPositions.entrySet()) {
				PositionKey key = entry.getKey();
				EODPosition position = entry.getValue();
				if (maxDelta < position.getDelta()) {
					maxDelta = position.getDelta();
					maxInstrument = position.getInstrument();
				}
				if (minDelta > position.getDelta()) {
					minDelta = position.getDelta();
					minInstrument = position.getInstrument();
				}
				data.add(new String[] { position.getInstrument(), position.getAccount(), position.getAccountType(),
						String.valueOf(position.getQuantity()), String.valueOf(position.getDelta()) });
			}
			writer.writeAll(data);
			writer.close();
			System.out.println("Instrument with Largest Net Volume: >" + maxInstrument + "< : Delta: >" + maxDelta + "<");
			System.out.println("Instrument with Lowest Net Volume: >" + minInstrument + "< : Delta: >" + minDelta + "<");
		} catch (IOException e) {
			System.out.println("Exception occured while writing EOD Positions data to file...");
			e.printStackTrace();
		}
	}
}
