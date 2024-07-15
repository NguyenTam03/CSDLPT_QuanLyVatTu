package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import main.Program;
import model.CTPLModel;

public class CTPLDao extends IAbstractDao<CTPLModel> {
	public CTPLDao() {
		init();
	}
	
	private void init() {
		String sql = "SELECT * FROM CTPN";
		Program.myReader = Program.ExecSqlDataReader(sql);
		initModel();
		getColName().remove(getColCount() - 1);
	}

	
	public static CTPLDao getInstance() {
		return new CTPLDao();
	}	

	public ArrayList<CTPLModel> selectAllCTPN(String MaPN) {
		ArrayList<CTPLModel> ctpnList = new ArrayList<>();
		String sql = "SELECT * FROM CTPN where MaPN = ?";
		Program.myReader = Program.ExecSqlDataReader(sql, MaPN);
		
		try {
			while (Program.myReader.next()) {
				CTPLModel CTPhieuNhap = new CTPLModel(
						Program.myReader.getString(1),
						Program.myReader.getString(2),
						Program.myReader.getInt(3),
						Program.myReader.getFloat(4));
						
				ctpnList.add(CTPhieuNhap);
			}
			return ctpnList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void insert(CTPLModel t) throws SQLException{
		// TODO Auto-generated method stub
		String sql = "INSERT INTO CTPN (MaPN, MaVT, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMaPN(), t.getMavt(), t.getSoLuong(), t.getDonGia());
	}

	@Override
	public void update(CTPLModel t) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "UPDATE CTPN SET SoLuong = ?, DonGia = ? WHERE MaPN = ? AND MaVT = ?";
		Program.ExecSqlDML(sql, t.getSoLuong(), t.getDonGia(), t.getMaPN(), t.getMavt());
	}

	@Override
	public void delete(CTPLModel t) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM CTPN WHERE MaPN = ? AND MaVT = ?";
		Program.ExecSqlDML(sql, t.getMaPN(), t.getMavt());
	}
	@Override
	public ArrayList<CTPLModel> selectAll() {
		return null;
	}

	@Override
	public <E> CTPLModel selectById(E t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<CTPLModel> selectByCondition(String sql, Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}
}
