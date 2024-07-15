package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import main.Program;
import model.CTPXModel;

public class CTPXDao extends IAbstractDao<CTPXModel> {
	public CTPXDao() {
		init();
	}

	private void init() {
		String sql = "SELECT * FROM CTPX";
		Program.myReader = Program.ExecSqlDataReader(sql);
		initModel();
		getColName().remove(getColCount() - 1);
		getColName().set(1, "TENVT");
		getColName().add("THANHTIEN");
	}

	public static CTPXDao getInstace() {
		return new CTPXDao();
	}

	@Override
	public void insert(CTPXModel t) throws SQLException {
		String sql = "INSERT INTO CTPX (MAPX, MAVT, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMapx(), t.getMavt(), t.getSoLuong(), t.getDonGia());

	}

	@Override
	public void update(CTPXModel t) throws SQLException {
		String sql = "UPDATE CTPX SET SOLUONG = ?, DONGIA = ? WHERE MAPX = ? AND MAVT = ?";
		Program.ExecSqlDML(sql, t.getSoLuong(), t.getDonGia(), t.getMapx(), t.getMavt());
	}

	@Override
	public void delete(CTPXModel t) throws SQLException {
		String sql = "DELETE FROM CTPX WHERE MAPX = ? AND MAVT = ?";
		Program.ExecSqlDML(sql, t.getMapx(), t.getMavt());
	}

	@Override
	public ArrayList<CTPXModel> selectAll() {
		ArrayList<CTPXModel> ctpxList = new ArrayList<CTPXModel>();
		String sql = "SELECT MAPX, MAVT, SOLUONG, DONGIA FROM CTPX";
		Program.myReader = Program.ExecSqlDataReader(sql);

		try {
			while (Program.myReader.next()) {
				CTPXModel ctpxModel = new CTPXModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getInt(3), Program.myReader.getFloat(4));
				ctpxList.add(ctpxModel);
			}
			return ctpxList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<CTPXModel> selectAllByMaPX(String mapx) {
		ArrayList<CTPXModel> ctpxList = new ArrayList<CTPXModel>();
		String sql = "SELECT * FROM CTPX WHERE MAPX = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, mapx);

		try {
			while (Program.myReader.next()) {
				CTPXModel ctpxModel = new CTPXModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getInt(3), Program.myReader.getFloat(4));
				ctpxList.add(ctpxModel);
			}
			return ctpxList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <E> CTPXModel selectById(E t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<CTPXModel> selectByCondition(String sql, Object... objects) {
		ArrayList<CTPXModel> ctpxList = new ArrayList<CTPXModel>();
//		String sql = "SELECT * FROM CTPX WHERE MAPX = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, objects);

		try {
			while (Program.myReader.next()) {
				CTPXModel ctpxModel = new CTPXModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getInt(3), Program.myReader.getFloat(4));
				ctpxList.add(ctpxModel);
			}
			return ctpxList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
