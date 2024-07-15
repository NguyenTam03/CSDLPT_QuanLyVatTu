package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.Program;


public abstract class IAbstractDao<T> {
	private int colCount;
	private List<String> colName;

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public int getColCount() {
		return colCount;
	}

	public List<String> getColName() {
		return colName;
	}

	public void setColName(List<String> colName) {
		this.colName = colName;
	}

	public abstract void insert(T t) throws SQLException;

	public abstract void update(T t) throws SQLException;

	public abstract void delete(T t) throws SQLException;

	public abstract ArrayList<T> selectAll();

	public abstract <E extends Object> T selectById(E t);
	

	public abstract ArrayList<T> selectByCondition(String sql, Object...objects);
	
	public void initModel() {
		colName = new ArrayList<String>();
		try {
			colCount = Program.myReader.getMetaData().getColumnCount();
			for (int i = 0; i < colCount; i++) {
				String temp = Program.myReader.getMetaData().getColumnName(i + 1);
				colName.add(temp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
