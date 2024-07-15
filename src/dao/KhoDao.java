package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import main.Program;
import model.KhoModel;

public class KhoDao extends IAbstractDao<KhoModel> {

	public KhoDao() {
		init();
	}

	private void init() {
		String sql = "SELECT MAKHO, TENKHO, DIACHI FROM Kho";
		Program.myReader = Program.ExecSqlDataReader(sql);

		initModel();
	}

	public static KhoDao getInstance() {
		return new KhoDao();
	}

	@Override
	public void insert(KhoModel t) throws SQLException {
		String sql = "INSERT INTO Kho (MAKHO, TENKHO, DIACHI, MACN) VALUES (?, ?, ?, ?)";
		Program.ExecSqlDML(sql, t.getMaKho(), t.getTenKho(), t.getDiaChi(), t.getMacn());
	}

	@Override
	public void update(KhoModel t) throws SQLException {
		String sql = "UPDATE Kho SET TENKHO = ?, DIACHI = ? WHERE MAKHO = ?";
		Program.ExecSqlDML(sql, t.getTenKho(), t.getDiaChi(), t.getMaKho());
	}

	@Override
	public void delete(KhoModel t) throws SQLException {
		String sql = "DELETE FROM Kho WHERE MAKHO = ?";
		Program.ExecSqlDML(sql, t.getMaKho());
	}

	@Override
	public ArrayList<KhoModel> selectAll() {
		ArrayList<KhoModel> dsKho = new ArrayList<KhoModel>();
		String sql = "SELECT MAKHO, TENKHO, DIACHI FROM dbo.Kho";
		Program.myReader = Program.ExecSqlDataReader(sql);
		
		try {
			while (Program.myReader.next()) {
				KhoModel kho = new KhoModel();
				kho.setMaKho(Program.myReader.getString(1));
				kho.setTenKho(Program.myReader.getString(2));
				kho.setDiaChi(Program.myReader.getString(3));
				dsKho.add(kho);
			}
			return dsKho;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<KhoModel> selectByCondition(String sql, Object...objects) {
		return null;
		
	}

	@Override
	public <E> KhoModel selectById(E t) {
		String sql = "SELECT * FROM dbo.Kho WHERE MAKHO = ?";
		Program.myReader = Program.ExecSqlDataReader(sql,t);

		try {
			Program.myReader.next();
			KhoModel kho = new KhoModel(Program.myReader.getString(1), Program.myReader.getString(2),
					Program.myReader.getString(3), Program.myReader.getString(4));
			return kho;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
