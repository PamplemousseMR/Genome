package Excel;

import Data.Kingdom;
import Data.Organism;
import Data.Replicon;
/*import Data.Kingdom;
import Data.SubGroup;
import Data.Group;*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* external library import  */
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

    public static void main(String[] args) {

    }

	public static void Test() throws InvalidFormatException, IOException {
		    // Obtain a workbook from the excel file
		    Workbook workbook = WorkbookFactory.create(new File("templateExemple.xlsx"));

		    // Get Sheet at index 0
		    Sheet sheet = workbook.getSheetAt(0);

		    if (sheet == null)
		    {
		    	sheet= workbook.createSheet();
		    }

		    // Get Row at index 1
		    Row row = sheet.getRow(1);
		    if(row == null)
		    {
		    	row=sheet.createRow(1);
		    }

		    // Get the Cell at index 2 from the above row
		    Cell cell = row.getCell(2);

		    // Create the cell if it doesn't exist
		    if (cell == null)
		        cell = row.createCell(2);

		    // Update the cell's value
		    cell.setCellType(CellType.STRING);
		    cell.setCellValue("Updated Value");

		    // Write the output to the file
		    FileOutputStream fileOut = new FileOutputStream("resultatExemple.xlsx");
		    workbook.write(fileOut);
		    fileOut.close();


	}



	/**
	 * Function call to write kingdom summary in Excel workbook
	 */
	public void writeKingdom(Kingdom _kingdom)
	{
	    //get path from file manager
        //String Path = FileManager.GetPathFor(_kingdom);
        //check if file exist

        //yes, delete it? Keep general info?

        //create proper file

        //LE fun qui commence

        //create workbook

        //create sheet summary

		return;
	}

	/**
	 * Function call to write group summary in Excel workbook
	 */
	public void writeGroup(/*Group _group*/)
	{
		return;
	}

	/**
	 * Function call to write subgroup summary in Excel workbook
	 */
	public void writeSubGroup(/*SubGroup _subGroup*/)
	{
		return;
	}

	/**
	 * Function call to write organism summary in Excel workbook
	 */
	public void writeOrganism(Organism _organism)
	{
		//get path from file manager
        //String Path = FileManager.GetPathFor(_organism);
        //check if file exist

        //yes, delete it? Keep general info?

        //LE fun qui commence

        //create workbook

        //create general info


        //Summary sheets

            //for each Summary page, create sheet and fill it

        //for each replicon

                //create sheet

                //create header

                //for each trinucleotide

                    //fill statistics ( cf StatisticsTest)

                //for each dinucleotid

                    //fill statistics ( cf StatisticsTest)

        //save with correct name


        //Success?


        return;
	}


}
