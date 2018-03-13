package Excel;

import java.text.SimpleDateFormat;

class ExcelConstants
{
	//File names



	//Sheet names

	public static  String s_generalInfoSheet="General Information";


	//General information sheet constants


	public static  String s_generalInfoNameHeader="Name";
	public static  ExcelCell s_generalInfoNameCell= new ExcelCell(5,2);


	//

	public static SimpleDateFormat s_dateFormat = new SimpleDateFormat("d MMM yyyy");

}
