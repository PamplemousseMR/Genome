package Excel;

 class ExcelCell
{
	private  int m_row;
	private  int m_column;

	public ExcelCell(int m_row, int m_column) {
		this.m_row = m_row;
		this.m_column = m_column;
	}

	public int getRow() {
		return m_row;
	}

	public int getColumn() {
		return m_column;
	}
}
