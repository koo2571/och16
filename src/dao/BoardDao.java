package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDao {
	private static BoardDao instance;
	private BoardDao() {}
	public static BoardDao getInstance() {
		if(instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}
	public Connection getConnection() {
		Connection conn = null;
		try {
			Context ctx = new InitialContext();
			DataSource ds =(DataSource)ctx.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}
	public Board select(int num) throws SQLException {
		Board board = new Board();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String sql = "select * from BOARD where num=?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				board.setNum(rs.getInt(1));
				board.setWriter(rs.getString(2));
				board.setSubject(rs.getString(3));
				board.setContent(rs.getString(4));
				board.setEmail(rs.getString(5));
				board.setReadcount(rs.getInt(6));
				board.setPasswd(rs.getString(7));
				board.setRef(rs.getInt(8));
				board.setRe_step(rs.getInt(9));
				board.setRe_level(rs.getInt(10));
				board.setIp(rs.getString(11));
				board.setReg_date(rs.getDate(12));				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		return board;
	}
	public int getTotalCnt() throws SQLException {
		int tot = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from BOARD";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) 	tot = rs.getInt(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		return	tot;
	}
	public List<Board> list(int startRow, int endRow) throws SQLException{
		List<Board> list = new ArrayList<Board>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * from (select rownum rn ,a.*"
				+ "from  (select * from board order by ref desc,re_step) a )"
				+ "WHERE rn between ? and ?";
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				do {
					Board board = new Board();
					//rs.get 메소드는 sql입력했을때 나오는 순서대로 숫자 입력.
					board.setNum(rs.getInt(2));
					board.setWriter(rs.getString(3));
					board.setSubject(rs.getString(4));
					board.setContent(rs.getString(5));
					board.setEmail(rs.getString(6));
					board.setReadcount(rs.getInt(7));
					board.setPasswd(rs.getString(8));
					board.setRef(rs.getInt(9));
					board.setRe_step(rs.getInt(10));
					board.setRe_level(rs.getInt(11));
					board.setIp(rs.getString(12));
					board.setReg_date(rs.getDate(13));
					list.add(board);
				}while(rs.next());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
			if(conn != null) conn.close();
		}
		return list;
	}
}
