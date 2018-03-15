package Excel;

import Data.*;
import Utils.Logs;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/* external library import  */

public class ExcelWriter {

	/**
	 * Function call to write kingdom summary in Excel workbook
	 */

	

	public  static  void writeDatabase(DataBase _data) throws IOException
    {
        String Path = FileManager.GetPathFor(_data);

        //	Check if file exist

        File file = new File(Path );

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
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet _generalInfoSheet = workbook.createSheet();
        writeGeneralInfoSheet(_generalInfoSheet,_data);
        //	Create sheet
        createStatistics(workbook,_data);

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
	public static void writeKingdom(Kingdom _kingdom) throws IOException {

	    //	Get path from file manager

        String Path = FileManager.GetPathFor(_kingdom);

		//	Check if file exist

		File file = new File(Path );

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
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet _generalInfoSheet = workbook.createSheet();
		writeGeneralInfoSheet(_generalInfoSheet,_kingdom);
		//	Create sheet
		createStatistics(workbook,_kingdom);

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
	public static void writeGroup(Group _group) throws IOException {

		//	Get path from file manager

		String Path = FileManager.GetPathFor(_group);

		//	Check if file exist

		File file = new File(Path );

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
		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFSheet _generalInfoSheet = workbook.createSheet();
		writeGeneralInfoSheet(_generalInfoSheet,_group);
		//	Create sheet
		createStatistics(workbook,_group);
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
	public static void writeSubGroup(SubGroup _subGroup) throws IOException {

		//	Get path from file manager

		String Path = FileManager.GetPathFor(_subGroup);

		//	Check if file exist

		File file = new File(Path );

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
		XSSFWorkbook workbook = new XSSFWorkbook();

		//	Create general info sheet
		XSSFSheet _generalInfoSheet = workbook.createSheet();
		writeGeneralInfoSheet(_generalInfoSheet,_subGroup);

		createStatistics(workbook,_subGroup);


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

		File file = new File(Path );
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
		XSSFWorkbook workbook = new XSSFWorkbook();

		//	Create general info sheet
		XSSFSheet general_info_sheet = workbook.createSheet(ExcelConstants.s_generalInfoSheet);

		writeGeneralInfoSheet(general_info_sheet,_organism);

		

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

	public  static  void writeGeneralInfoSheet(XSSFSheet _general_info_sheet, IDataBase _data)
	{
		_general_info_sheet.createRow(0).createCell(0).setCellValue("Information");

		XSSFRow r;

		r=_general_info_sheet.createRow(2);
		r.createCell(0).setCellValue("Name");
		r.createCell(1).setCellValue(_data.getName());
		r.createCell(5).setCellValue("Genome");

		r=_general_info_sheet.createRow(4);
		r.createCell(0).setCellValue("Modification Date");
		r.createCell(1).setCellValue(ExcelConstants.s_dateFormat.format(_data.getModificationDate()));

		r=_general_info_sheet.createRow(6);
		r.createCell(0).setCellValue("Number of CDS sequences");
		r.createCell(1).setCellValue(_data.getValidCDSNumber());

		r=_general_info_sheet.createRow(8);
		r.createCell(0).setCellValue("Number of invalids CDS");
		r.createCell(1).setCellValue(_data.getInvalidCDSNumber());

		if(_data.getClass()!=Organism.class)
		{
			r=_general_info_sheet.createRow(10);
			r.createCell(0).setCellValue("Number of Organisms");
			r.createCell(1).setCellValue(_data.getTotalOrganism());
		}


		int genomeInfoRowNumber=3;

		for(Map.Entry<Statistics.Type,Long> entry: _data.getGenomeNumber().entrySet())
		{
			XSSFRow row=_general_info_sheet.createRow(genomeInfoRowNumber);
			row.createCell(5).setCellValue(entry.getKey().toString());
			writeNumericCell( row.createCell(6),entry.getValue());
			genomeInfoRowNumber++;
		}

		for(int i=0; i<6;i++)
        {
            _general_info_sheet.autoSizeColumn(i);
        }

	}

	public static void createReplicons(XSSFWorkbook _workbook, Organism _organism) throws IOException {
        for (Replicon replicon : _organism.getReplicons()) {
            createRepliconSheet(_workbook, replicon);
        }
    }

    public static void createStatistics(XSSFWorkbook _workbook, IDataBase _data) throws IOException {
        for (Statistics stat : _data.getStatistics().values()) {
            createStatisticSheet(_workbook, stat);
        }
    }

	public static void createStatisticSheet(XSSFWorkbook _workbook, Statistics _stat) throws IOException {
		XSSFSheet s = _workbook.createSheet("SUM_" + _stat.getType());
		createHeader(s);
		createTable(s, _stat);
	}

    public static void createRepliconSheet(XSSFWorkbook _workbook, Replicon _replicon) throws IOException {
        XSSFSheet s = _workbook.createSheet(_replicon.getName());
        createHeader(s);
        createTable(s, _replicon);
    }

	public static void  createHeader(XSSFSheet _sheet) throws IOException {
        XSSFRow r = _sheet.createRow(0);
       /* XSSFCellStyle style=_sheet.getWorkbook().createCellStyle();
        style.setFillBackgroundColor(new XSSFColor(Color.blue));
          style.
        XSSFFont font =_sheet.getWorkbook().createFont();
        font.setBold(true);

        style.setFont(font);*/
        XSSFCell c = r.createCell(0);
        c.setCellValue("Trinucléotides");
        c = r.createCell(1);
       // c.setCellStyle(style);
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

	public static void createTable(XSSFSheet _sheet, Statistics _stat) throws IOException {
		int i = 1;
		XSSFRow r;
		XSSFCell c;



        for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {
			Tuple row = _stat.getTable()[tri.ordinal()];
			r = _sheet.createRow(i);
			c = r.createCell(0);
			c.setCellValue(tri.toString());
			c = r.createCell(1);
			writeNumericCell(c,row.get(Statistics.StatLong.PHASE0));
			c = r.createCell(2);
			writeNumericCell(c,row.get(Statistics.StatFloat.FREQ0));
			c = r.createCell(3);
			writeNumericCell(c,row.get(Statistics.StatLong.PHASE1));
			c = r.createCell(4);
			writeNumericCell(c,row.get(Statistics.StatFloat.FREQ1));
			c = r.createCell(5);
			writeNumericCell(c,row.get(Statistics.StatLong.PHASE2));
			c = r.createCell(6);
			writeNumericCell(c,row.get(Statistics.StatFloat.FREQ2));
			c = r.createCell(7);
			writeNumericCell(c,row.get(Statistics.StatLong.PREF0));
			c = r.createCell(8);
			writeNumericCell(c,row.get(Statistics.StatLong.PREF1));
			c = r.createCell(9);
			writeNumericCell(c,row.get(Statistics.StatLong.PREF2));
			i++;
		}

		r = _sheet.createRow(i);
		c = r.createCell(0);
		c.setCellValue("TOTAL");
		_sheet.autoSizeColumn(0);
		for(int p = 1 ; p <= 9 ; p++){
			c = r.createCell(p);
			c.setCellType(CellType.NUMERIC);
			c.setCellFormula("SUM(" + (char)('A' + p) + "2:" + (char)('A' + p) + "65)");
            _sheet.autoSizeColumn(p);
		}



	}

	public  static  void writeNumericCell(XSSFCell _cell, float _f)
	{

		_cell.setCellValue(_f);
		_cell.setCellType(CellType.NUMERIC);
	}

	public  static  void writeNumericCell(XSSFCell _cell, long _l)
	{

		_cell.setCellValue(_l);
		_cell.setCellType(CellType.NUMERIC);
	}
}

