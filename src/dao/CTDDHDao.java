package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import main.Program;
import model.CTDDHModel;
import views.DatHangForm;

public class CTDDHDao extends IAbstractDao<CTDDHModel> {
	private DatHangForm dhForm;
	
	public CTDDHDao(DatHangForm dhForm) {
		init();
		this.dhForm = dhForm;
	}
	
	public CTDDHDao() {
		init();
	}

	public void init() {
		String sql = "SELECT * FROM CTDDH";
		Program.myReader = Program.ExecSqlDataReader(sql);
		initModel();
		getColName().remove(getColCount() - 1);
		getColName().set(1, "TENVT");
		getColName().add("THANHTIEN");
	}

	public static CTDDHDao getInstance(DatHangForm form) {
		return new CTDDHDao(form);
	}
	
	public static CTDDHDao getInstance() {
		return new CTDDHDao();
	}

	@Override
	public ArrayList<CTDDHModel> selectAll() {
		ArrayList<CTDDHModel> ctdhList = new ArrayList<>();
		String sql = "SELECT MasoDDH, MAVT, SOLUONG, DONGIA FROM CTDDH";
		Program.myReader = Program.ExecSqlDataReader(sql);

		try {
			while (Program.myReader.next()) {
				CTDDHModel datHang = new CTDDHModel(Program.myReader.getString(1).trim(), Program.myReader.getString(2).trim(),
						Program.myReader.getInt(3), Program.myReader.getFloat(4));

				ctdhList.add(datHang);
			}
			return ctdhList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insert(CTDDHModel t) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO CTDDH (MASODDH, MAVT, SOLUONG, DONGIA) VALUES (?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMaSoDDH(), t.getMavt(), t.getSoLuong(), t.getDonGia());
	}

	@Override
	public void update(CTDDHModel t) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "UPDATE CTDDH SET MAVT = ?, SOlUONG = ?, DONGIA = ? WHERE MASODDH = ? AND MAVT = ?";
		System.out.println("UPDATE CTDDH SET MAVT = " + t.getMavt() + ", SOlUONG = " + t.getSoLuong() + ", DONGIA = " + t.getDonGia() + " WHERE MASODDH = " + t.getMaSoDDH() + " AND MAVT = " + dhForm.getMaVT().get(dhForm.getTableCTDH().getSelectedRow()));
		Program.ExecSqlDML(sql, t.getMavt(), t.getSoLuong(), t.getDonGia(), t.getMaSoDDH(), dhForm.getMaVT().get(dhForm.getTableCTDH().getSelectedRow()));
	}

	@Override
	public void delete(CTDDHModel t) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM CTDDH WHERE MASODDH = ? AND MAVT = ?";
		Program.ExecSqlDML(sql, t.getMaSoDDH(), t.getMavt());
	}

	@Override
	public ArrayList<CTDDHModel> selectByCondition(String sql, Object... condition) {
		ArrayList<CTDDHModel> list = new ArrayList<CTDDHModel>();
		Program.myReader = Program.ExecSqlDataReader(sql, condition);

		try {
			while (Program.myReader.next()) {
				CTDDHModel ctdh = new CTDDHModel(Program.myReader.getString(1), Program.myReader.getString(2),
						Program.myReader.getInt(3), Program.myReader.getFloat(4));
				list.add(ctdh);
			}

			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public <E> CTDDHModel selectById(E t) {
		// TODO Auto-generated method stub
		return null;
	}

}
