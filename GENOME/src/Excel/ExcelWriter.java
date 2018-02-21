package Excel;

import Data.*;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* external library import  */
import Utils.Logs;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

public class ExcelWriter {

    public static void main(String[] args) throws Exception {

		Kingdom k = new Kingdom("KINGDOM");
		k.start();

		Group g = new Group("GROUP");
		g.start();
		k.addGroup(g);

		SubGroup s = new SubGroup("SUBGROUP");
		s.start();
		g.addSubGroup(s);

		Organism o = new Organism(new RawOrganism(new JSONObject("{\n" +
				"\t\"id\": 152753,\n" +
				"\t\"organism\": \"'Brassica napus' phytoplasma\",\n" +
				"\t\"kingdom\": \"Bacteria\",\n" +
				"\t\"group\": \"Terrabacteria group\",\n" +
				"\t\"subgroup\": \"Tenericutes\",\n" +
				"\t\"replicons\": \"pPABN1:NC_016583.1/HQ637382.1\",\n" +
				"\t\"modify_date\": \"2014-12-18T00:00:00Z\",\n" +
				"\t\"_version_\": 1592820474201505800\n" +
				"}")));

		s.addOrganism(o);

		writeOrganism(o);
		
    }

	/**
	 * Function call to write kingdom summary in Excel workbook
	 */
	public void writeKingdom(Kingdom _kingdom) throws IOException, InvalidFormatException {

	    //	Get path from file manager

        String Path = FileManager.GetPathFor(_kingdom);

		//	Check if file exist

		File file = new File(Path + "\\" +  _kingdom.getName() + ".xls");

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

		// NAIVE TEST
		Row r  = sheet.createRow(0);
		Cell c = r.createCell(0);
		c.setCellType(CellType.STRING);
		c.setCellValue("Hey");
		// NAIVE TEST

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

		File file = new File(Path + "\\" +  _group.getName() + ".xls");

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

		// NAIVE TEST
		Row r  = sheet.createRow(0);
		Cell c = r.createCell(0);
		c.setCellType(CellType.STRING);
		c.setCellValue("Hey");
		// NAIVE TEST

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

		File file = new File(Path + "\\" +  _subGroup.getName() + ".xls");

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

		// NAIVE TEST
		Row r  = sheet.createRow(0);
		Cell c = r.createCell(0);
		c.setCellType(CellType.STRING);
		c.setCellValue("Hey");
		// NAIVE TEST

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

		File file = new File(Path + "\\" +  _organism.getName() + ".xlsx");

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

		// Fill it

		// NAIVE TEST
		Row r  = general_info_sheet.createRow(0);
		Cell c = r.createCell(0);
		c.setCellType(CellType.STRING);
		c.setCellValue("Hey");
		// NAIVE TEST

			// TODO

		//	For each Summary page, create sheet and fill it

				// TODO

		// 	For each replicon

                //	Create sheet

					// TODO

				// 	Create header ( cf StatisticsTest)

					// TODO

                //	For each trinucleotide

                    // 	Fill statistics

						// TODO

                //	For each dinucleotid

                    //	Fill statistics ( cf StatisticsTest)

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


}

