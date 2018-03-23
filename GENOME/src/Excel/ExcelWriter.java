package Excel;

import Data.*;
import Utils.Logs;
import Utils.Options;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ExcelWriter {

    /**
     * Function call to write database summary in Excel workbook
     */
    public static void writeDatabase(DataBase _data) throws IOException {
        final String path = FileManager.getPathFor(_data);
        final XSSFWorkbook workbook = writeSheet(_data);
        saveSheet(path, workbook);
    }

    /**
     * Function call to write kingdom summary in Excel workbook
     */
    public static void writeKingdom(Kingdom _kingdom) throws IOException {
        final String path = FileManager.getPathFor(_kingdom);
        final XSSFWorkbook workbook = writeSheet(_kingdom);
        saveSheet(path, workbook);
    }

    /**
     * Function call to write group summary in Excel workbook
     */
    public static void writeGroup(Group _group) throws IOException {
        final String path = FileManager.getPathFor(_group);
        final XSSFWorkbook workbook = writeSheet(_group);
        saveSheet(path, workbook);
    }

    /**
     * Function call to write subgroup summary in Excel workbook
     */
    public static void writeSubGroup(SubGroup _subGroup) throws IOException {
        final String path = FileManager.getPathFor(_subGroup);
        final XSSFWorkbook workbook = writeSheet(_subGroup);
        saveSheet(path, workbook);
    }

    /**
     * Function call to write organism summary in Excel workbook
     */
    public static void writeOrganism(Organism _organism) throws IOException {
        final String path = FileManager.getPathFor(_organism);
        final XSSFWorkbook workbook = writeSheet(_organism);
        createRepliconsSheet(workbook, _organism);
        saveSheet(path, workbook);
    }

    private static XSSFWorkbook writeSheet(IDataBase _idataBase) {
        final XSSFWorkbook workbook = new XSSFWorkbook();
        final XSSFSheet generalInfoSheet = workbook.createSheet(_idataBase.getName());
        createGeneralInfoSheet(generalInfoSheet, _idataBase);
        createStatisticsSheet(workbook, _idataBase);
        return workbook;
    }

    private static void saveSheet(String _path, Workbook _workbook) throws IOException {
        //	Check if file exist
        final File file = new File(_path);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Logs.exception(e);
            throw e;
        }

        // Save file
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            _workbook.write(fileOut);
        } catch (IOException e) {
            final String message = "Unable to save file : " + _path;
            Logs.warning(message);
            Logs.exception(new IOException(message, e));
            throw e;
        }
    }

    private static void createGeneralInfoSheet(XSSFSheet _general_info_sheet, IDataBase _data) {
        _general_info_sheet.createRow(0).createCell(0).setCellValue("Information");

        _general_info_sheet.setColumnWidth(0, 7340);
        _general_info_sheet.autoSizeColumn(1);
        _general_info_sheet.setColumnWidth(4450, 2240);

        XSSFRow r;

        r = _general_info_sheet.createRow(2);
        r.createCell(0).setCellValue("Name");
        r.createCell(1).setCellValue(_data.getName());
        r.createCell(5).setCellValue("Genome");

        r = _general_info_sheet.createRow(4);
        r.createCell(0).setCellValue("Modification Date");
        r.createCell(1).setCellValue(new SimpleDateFormat("d MMM yyyy").format(_data.getModificationDate()));

        r = _general_info_sheet.createRow(6);
        r.createCell(0).setCellValue("Total number of CDS sequences");
        r.createCell(1).setCellValue(_data.getCDSNumber());

        r = _general_info_sheet.createRow(8);
        r.createCell(0).setCellValue("Number of valid CDS");
        r.createCell(1).setCellValue(_data.getValidCDSNumber());

        r = _general_info_sheet.createRow(10);
        r.createCell(0).setCellValue("Number of invalid CDS");
        r.createCell(1).setCellValue(_data.getCDSNumber() - _data.getValidCDSNumber());

        if (_data.getClass() != Organism.class) {
            r = _general_info_sheet.createRow(12);
            r.createCell(0).setCellValue("Number of Organisms");
            r.createCell(1).setCellValue(_data.getTotalOrganism());
        } else {
            r = _general_info_sheet.createRow(12);
            r.createCell(0).setCellValue("Id");
            r.createCell(1).setCellValue(((Organism) _data).getId());

            r = _general_info_sheet.createRow(14);
            r.createCell(0).setCellValue("Version");
            r.createCell(1).setCellValue(((Organism) _data).getVersion());
        }

        int genomeInfoRowNumber = 3;

        for (Map.Entry<Statistics.Type, Long> entry : _data.getGenomeNumber().entrySet()) {
            XSSFRow row = _general_info_sheet.getRow(genomeInfoRowNumber);
            if (row == null) {
                row = _general_info_sheet.createRow(genomeInfoRowNumber);
            }
            row.createCell(5).setCellValue(entry.getKey().toString());
            writeNumericCell(row.createCell(6), entry.getValue());
            genomeInfoRowNumber++;
        }

    }

    private static void createStatisticsSheet(XSSFWorkbook _workbook, IDataBase _data) {
        for (Statistics stat : _data.getStatistics().values()) {
            XSSFSheet s = _workbook.createSheet(Options.getSumPrefix() + stat.getType());
            createHeaderSheet(s);
            createTable(s, stat);
        }
    }

    private static void createRepliconsSheet(XSSFWorkbook _workbook, Organism _organism) {
        for (Replicon replicon : _organism.getReplicons()) {
            XSSFSheet s = _workbook.createSheet(replicon.getName());
            createHeaderSheet(s);
            createTable(s, replicon);
        }
    }

    private static void createHeaderSheet(XSSFSheet _sheet) {
        XSSFRow r = _sheet.createRow(0);
        XSSFCell c = r.createCell(0);
        c.setCellValue("Trinucleotide");
        c = r.createCell(1);
        c.setCellValue(Statistics.StatLong.PHASE0.toString());
        c = r.createCell(2);
        c.setCellValue(Statistics.StatFloat.FREQ0.toString());
        c = r.createCell(3);
        c.setCellValue(Statistics.StatLong.PHASE1.toString());
        c = r.createCell(4);
        c.setCellValue(Statistics.StatFloat.FREQ1.toString());
        c = r.createCell(5);
        c.setCellValue(Statistics.StatLong.PHASE2.toString());
        c = r.createCell(6);
        c.setCellValue(Statistics.StatFloat.FREQ2.toString());
        c = r.createCell(7);
        c.setCellValue(Statistics.StatLong.PREF0.toString());
        c = r.createCell(8);
        c.setCellValue(Statistics.StatLong.PREF1.toString());
        c = r.createCell(9);
        c.setCellValue(Statistics.StatLong.PREF2.toString());

    }

    private static void createTable(XSSFSheet _sheet, Statistics _stat) {
        int i = 1;
        XSSFRow r;
        XSSFCell c;

        for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {
            Tuple row = _stat.getTable()[tri.ordinal()];
            r = _sheet.createRow(i);
            c = r.createCell(0);
            c.setCellValue(tri.toString());
            c = r.createCell(1);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE0));
            c = r.createCell(2);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ0));
            c = r.createCell(3);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE1));
            c = r.createCell(4);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ1));
            c = r.createCell(5);
            writeNumericCell(c, row.get(Statistics.StatLong.PHASE2));
            c = r.createCell(6);
            writeNumericCell(c, row.get(Statistics.StatFloat.FREQ2));
            c = r.createCell(7);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF0));
            c = r.createCell(8);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF1));
            c = r.createCell(9);
            writeNumericCell(c, row.get(Statistics.StatLong.PREF2));
            i++;
        }

        r = _sheet.createRow(i);
        c = r.createCell(0);
        c.setCellValue("TOTAL");
        for (int p = 1; p <= 9; p++) {
            c = r.createCell(p);
            c.setCellType(CellType.NUMERIC);
            c.setCellFormula("SUM(" + (char) ('A' + p) + "2:" + (char) ('A' + p) + "65)");
        }

        i += 2;
        r = _sheet.createRow(i);
        r.createCell(0).setCellValue("Total number of CDS sequences");
        r.createCell(1).setCellValue(_stat.getCDSNumber());

        i += 2;
        r = _sheet.createRow(i);
        r.createCell(0).setCellValue("Number of valid CDS");
        r.createCell(1).setCellValue(_stat.getValidCDSNumber());

        i += 2;
        r = _sheet.createRow(i);
        r.createCell(0).setCellValue("Number of invalids CDS");
        r.createCell(1).setCellValue(_stat.getCDSNumber() - _stat.getValidCDSNumber());
    }

    private static void writeNumericCell(XSSFCell _cell, float _f) {
        _cell.setCellValue(_f);
        _cell.setCellType(CellType.NUMERIC);
    }

    private static void writeNumericCell(XSSFCell _cell, long _l) {
        _cell.setCellValue(_l);
        _cell.setCellType(CellType.NUMERIC);
    }
}

