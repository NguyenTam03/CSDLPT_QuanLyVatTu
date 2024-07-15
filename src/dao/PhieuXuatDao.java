package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import main.Program;
import model.PhieuXuatModel;

public class PhieuXuatDao extends IAbstractDao<PhieuXuatModel>{

	public PhieuXuatDao() {
	}
	
	public static PhieuXuatDao getInstance() {
		return new PhieuXuatDao();
	}
	@Override
	public void insert(PhieuXuatModel t) throws SQLException {
		String sql = "INSERT INTO PhieuXuat(MAPX, NGAY, HOTENKH, MANV, MAKHO) VALUES(?, ?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMapx(), t.getNgay(), t.getHoTenKH(), t.getManv(), t.getMaKho());
		
	}

	@Override
	public void update(PhieuXuatModel t) throws SQLException {
		String sql = "UPDATE PhieuXuat SET HOTENKH = ?, MAKHO = ? WHERE MAPX = ?"; 
		Program.ExecSqlDML(sql, t.getHoTenKH(), t.getMaKho(), t.getMapx());
	}

	@Override
	public void delete(PhieuXuatModel t) throws SQLException {
		String sql = "DELETE FROM PhieuXuat WHERE MAPX = ?";
		Program.ExecSqlDML(sql, t.getMapx());
	}

	@Override
	public ArrayList<PhieuXuatModel> selectAll() {
		ArrayList<PhieuXuatModel> phieuXuatList = new ArrayList<PhieuXuatModel>();
		String sql = "SELECT MAPX, NGAY, HOTENKH, MANV, MAKHO FROM PhieuXuat";
		Program.myReader = Program.ExecSqlDataReader(sql);
		initModel();
		getColName().set(3, "TENNV");
		getColName().set(4, "TENKHO");
		try {
			while (Program.myReader.next()) {
				PhieuXuatModel phieuXuatModel = new PhieuXuatModel(
						Program.myReader.getString(1),
						Program.myReader.getDate(2),
						Program.myReader.getString(3),
						Program.myReader.getInt(4),
						Program.myReader.getString(5));	
				phieuXuatList.add(phieuXuatModel);
			}
			return phieuXuatList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <E> PhieuXuatModel selectById(E t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<PhieuXuatModel> selectByCondition(String sql, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}
