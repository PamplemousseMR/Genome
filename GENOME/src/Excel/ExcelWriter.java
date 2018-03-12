package Excel;

import Data.*;

/* external library import  */
import Utils.Logs;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter {

	/**
	 * Function call to write kingdom summary in Excel workbook
	 */
	public void writeKingdom(Kingdom _kingdom) throws IOException, InvalidFormatException {

	    //	Get path from file manager

        String Path = FileManager.GetPathFor(_kingdom);

		//	Check if file exist

		File file = new File(Path + "/" +  _kingdom.getName() + ".xls");

		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}

		//	Create workbook
		Workbook workbook = new XSSFWorkbook();

		//	Create sheet
		Sheet sheet = workbook.createSheet();

		// Fill the sheet

			// TODO

		// Write the output to the file

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}

	}

	/**
	 * Function call to write group summary in Excel workbook
	 */
	public void writeGroup(Group _group) throws IOException, InvalidFormatException {

		//	Get path from file manager

		String Path = FileManager.GetPathFor(_group);

		//	Check if file exist

		File file = new File(Path + "/" +  _group.getName() + ".xls");

		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}

		//	Create workbook
		Workbook workbook = new XSSFWorkbook();

		//	Create sheet
		Sheet sheet = workbook.createSheet();

		// Fill the sheet

			// TODO

		// Write the output to the file

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
	}

	/**
	 * Function call to write subgroup summary in Excel workbook
	 */
	public void writeSubGroup(SubGroup _subGroup) throws IOException, InvalidFormatException {

		//	Get path from file manager

		String Path = FileManager.GetPathFor(_subGroup);

		//	Check if file exist

		File file = new File(Path + "/" +  _subGroup.getName() + ".xlsx");

		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}

		//	Create workbook
		Workbook workbook = new XSSFWorkbook();

		//	Create general info sheet
		Sheet sheet = workbook.createSheet();

		// Fill the sheet

			// TODO

		// Write the output to the file

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
	}

	/**
	 * Function call to write organism summary in Excel workbook
	 */
	public static void writeOrganism(Organism _organism) throws IOException {

		//	Get path from file manager
        String Path = FileManager.GetPathFor(_organism);

        //	Check if file exist

		File file = new File(Path + "/" +  _organism.getName() + ".xlsx");
		if (file.exists()) {
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}

        //	Create workbook
		Workbook workbook = new XSSFWorkbook();

		//	Create general info sheet
		Sheet general_info_sheet = workbook.createSheet(ExcelConstants.s_generalInfoSheet);
		general_info_sheet.createRow(0).createCell(0).setCellValue("Allo");
		// 	Create stats & replicons
		createStatistics(workbook, _organism);
		createReplicons(workbook, _organism);

		// Write the output to the file

		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(file);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			workbook.write(fileOut);
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}
		try {
			fileOut.close();
		} catch (IOException e) {
			Logs.exception(e);
			throw e;
		}

	}

	public static void createReplicons(Workbook _workbook, Organism _organism) throws IOException {
        for (Replicon replicon : _organism.getReplicons()) {
            createRepliconSheet(_workbook, replicon);
        }
    }

    public static void createStatistics(Workbook _workbook, Organism _organism) throws IOException {
        for (Statistics stat : _organism.getStatistics().values()) {
            createStatisticSheet(_workbook, stat);
        }
    }

	public static void createStatisticSheet(Workbook _workbook, Statistics _stat) throws IOException {
		Sheet s = _workbook.createSheet("SUM_" + _stat.getType());
		createHeader(s);
		createTable(s, _stat);
	}

    public static void createRepliconSheet(Workbook _workbook, Replicon _replicon) throws IOException {
        Sheet s = _workbook.createSheet(_replicon.getName());
        createHeader(s);
        createTable(s, _replicon);
    }

	public static void  createHeader(Sheet _sheet) throws IOException {
        Row r = _sheet.createRow(0);
        Cell c = r.createCell(0);
        c.setCellValue("Trinucléotides");
        c = r.createCell(1);
        c.setCellValue("Phase0");
        c = r.createCell(2);
        c.setCellValue("Freq0");
        c = r.createCell(3);
        c.setCellValue("Phase1");
        c = r.createCell(4);
        c.setCellValue("Freq1");
        c = r.createCell(5);
        c.setCellValue("Phase2");
        c = r.createCell(6);
        c.setCellValue("Freq2");
        c = r.createCell(7);
        c.setCellValue("Pref0");
        c = r.createCell(8);
        c.setCellValue("Pref1");
        c = r.createCell(9);
        c.setCellValue("Pref2");
    }

	public static void createTable(Sheet _sheet, Statistics _stat) throws IOException {
		int i = 1;
		Row r;
		Cell c;
		for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {
			Tuple row = _stat.getTable().get(tri);
			r = _sheet.createRow(i);
			c = r.createCell(0);
			c.setCellValue(tri.toString());
			c = r.createCell(1);
			c.setCellValue(row.get(Statistics.StatLong.PHASE0));
			c = r.createCell(2);
			c.setCellValue(row.get(Statistics.StatFloat.FREQ0));
			c = r.createCell(3);
			c.setCellValue(row.get(Statistics.StatLong.PHASE1));
			c = r.createCell(4);
			c.setCellValue(row.get(Statistics.StatFloat.FREQ1));
			c = r.createCell(5);
			c.setCellValue(row.get(Statistics.StatLong.PHASE2));
			c = r.createCell(6);
			c.setCellValue(row.get(Statistics.StatFloat.FREQ2));
			c = r.createCell(7);
			c.setCellValue(row.get(Statistics.StatLong.PREF0));
			c = r.createCell(8);
			c.setCellValue(row.get(Statistics.StatLong.PREF1));
			c = r.createCell(9);
			c.setCellValue(row.get(Statistics.StatLong.PREF2));
			i++;
		}

		r = _sheet.createRow(i);
		c = r.createCell(0);
		c.setCellValue("TOTAL");
		for(int p = 1 ; p <= 9 ; p++){
			c = r.createCell(p);
			c.setCellFormula("SUM(" + (char)('A' + p) + "2:" + (char)('A' + p) + "65)");
		}

	}


}

