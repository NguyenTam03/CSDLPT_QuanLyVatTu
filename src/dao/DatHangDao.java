package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import main.Program;
import model.DatHangModel;

public class DatHangDao extends IAbstractDao<DatHangModel> {
	public DatHangDao() {
		
	}

	public static DatHangDao getInstance() {
		return new DatHangDao();
	}

	@Override
	public void insert(DatHangModel t) throws SQLException {
		String sql = "INSERT INTO DatHang (MasoDDH, NGAY, NhaCC, MANV, MAKHO) VALUES (?, ?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMaSoDDH(), t.getNgay(), t.getNhaCC(), t.getManv(), t.getMakho());
	}

	@Override
	public ArrayList<DatHangModel> selectAll() {
		ArrayList<DatHangModel> datHangList = new ArrayList<DatHangModel>();
		String sql = "SELECT MasoDDH, NGAY, NhaCC, MANV, MAKHO FROM DatHang";
		Program.myReader = Program.ExecSqlDataReader(sql);
		initModel();
		getColName().set(3, "TENNV");
		getColName().set(4, "TENKHO");
		try {
			while (Program.myReader.next()) {
				DatHangModel datHang = new DatHangModel(Program.myReader.getString(1).trim(), Program.myReader.getDate(2),
						Program.myReader.getString(3).trim(), Program.myReader.getInt(4), Program.myReader.getString(5).trim());

				datHangList.add(datHang);
			}
			return datHangList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void update(DatHangModel t) throws SQLException {
		String sql = "UPDATE DatHang SET NhaCC = ?, MAKHO = ? WHERE MasoDDH = ?"; 
		Program.ExecSqlDML(sql, t.getNhaCC(), t.getMakho(), t.getMaSoDDH());
	}

	@Override
	public void delete(DatHangModel t) throws SQLException {
		String sql = "DELETE FROM DatHang WHERE MasoDDH = ?";
		Program.ExecSqlDML(sql, t.getMaSoDDH());
	}
	
	@Override
	public ArrayList<DatHangModel> selectByCondition(String sql, Object...obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> DatHangModel selectById(E t) {
		// TODO Auto-generated method stub
		return null;
	}
}
