package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import main.Program;
import model.VattuModel;

public class VatTuDao extends IAbstractDao<VattuModel> {
	public VatTuDao() {
		init();
	}

	private void init() {
		String sql = "SELECT MAVT, TENVT, DVT, SOLUONGTON FROM Vattu";
		Program.myReader = Program.ExecSqlDataReader(sql);
		initModel();
	}

	public static VatTuDao getInstance() {
		return new VatTuDao();
	}
	
	public void insert(VattuModel t) throws SQLException {
		String sql = "INSERT INTO Vattu (MAVT, TENVT, DVT, SOLUONGTON) VALUES (?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMavt(), t.getTenVT(), t.getDvt(), t.getSoLuongTon());
	}

	public void update(VattuModel t) throws SQLException {
		String sql = "UPDATE Vattu SET TENVT = ?, DVT = ?, SOLUONGTON = ? WHERE MAVT = ?";
		Program.ExecSqlDML(sql, t.getTenVT(), t.getDvt(), t.getSoLuongTon(), t.getMavt());
	}

	public void delete(VattuModel t) throws SQLException {
		String sql = "DELETE FROM Vattu WHERE MAVT = ?";
		Program.ExecSqlDML(sql, t.getMavt());
	}

	public ArrayList<VattuModel> selectAll() {
		ArrayList<VattuModel> dsVatTu = new ArrayList<VattuModel>();
		String sql = "SELECT MAVT, TENVT, DVT, SOLUONGTON FROM Vattu";
		Program.myReader = Program.ExecSqlDataReader(sql);
		
		try {
			while (Program.myReader.next()) {
				VattuModel vattu = new VattuModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getString(3), Program.myReader.getInt(4));
				dsVatTu.add(vattu);
			}
			return dsVatTu;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<VattuModel> selectByCondition(String sql, Object... objects) {
		ArrayList<VattuModel> list = new ArrayList<VattuModel>();
		Program.myReader = Program.ExecSqlDataReader(sql, objects);
		initModel();
		try {
			while (Program.myReader.next()) {
				VattuModel vt = new VattuModel();
				vt.setMavt(Program.myReader.getString(1));
				vt.setTenVT(Program.myReader.getString(2));
				vt.setDvt(Program.myReader.getString(3));
				list.add(vt);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<VattuModel> selectByConditionForPX(String sql, Object... objects) {
		ArrayList<VattuModel> list = new ArrayList<VattuModel>();
		Program.myReader = Program.ExecSqlDataReader(sql, objects);
		initModel();
		try {
			while (Program.myReader.next()) {
				VattuModel vt = new VattuModel();
				vt.setMavt(Program.myReader.getString(1));
				vt.setTenVT(Program.myReader.getString(2));
				vt.setDvt(Program.myReader.getString(3));
				vt.setSoLuongTon(Program.myReader.getInt(4));
				list.add(vt);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <E> VattuModel selectById(E t) {
		// TODO Auto-generated method stub
		return null;
	}

}
