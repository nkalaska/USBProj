package com.ubs.EODPositionsCalculator.utils;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.ubs.EODPositionsCalculator.beans.Position;
import com.ubs.EODPositionsCalculator.beans.PositionKey;

public class PositionReader {
	public static Map<PositionKey, Position> readPositions() {
		List<Position> positions = new ArrayList<Position>();
		try {
			FileReader filereader = new FileReader("resources/Input_StartOfDay_Positions.txt");
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			for (String[] row : allData) {
				Position position = new Position();
				position.setInstrument(row[0]);
				position.setAccount(row[1]);
				position.setAccountType(row[2]);
				position.setQuantity(Long.parseLong(row[3]));

				positions.add(position);
			}
			csvReader.close();
		} catch (Exception e) {
			System.out.println("Exception occured while reading positions..");
			e.printStackTrace();
		}
		return buildPositionMap(positions);
	}

	private static Map<PositionKey, Position> buildPositionMap(final List<Position> positions) {
		Map<PositionKey, Position> positionMap = new HashMap<PositionKey, Position>();

		for (Position pos : positions) {
			PositionKey key = new PositionKey(pos.getInstrument(), pos.getAccount(), pos.getAccountType());
			positionMap.put(key, pos);
		}

		return positionMap;
	}
	// public static void main(String[] args) {
	// List<Position> positions = readPositions();
	// System.out.println(positions);
	// }
}
