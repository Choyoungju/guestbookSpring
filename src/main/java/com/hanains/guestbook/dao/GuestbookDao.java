package com.hanains.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.hanains.guestbook.vo.GuestbookVo;

@Repository
public class GuestbookDao {
	
	
	public List<GuestbookVo> getList(){
		List<GuestbookVo> list = new ArrayList<GuestbookVo>();
		Connection connection = null;
		Statement stmt= null;
		ResultSet rs= null;
		
		try{
			//1.드라이버 로딩(클래스 로딩)
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//2.DB연결
			String dbUrl="jdbc:oracle:thin:@localhost:1521:xe";
			connection = DriverManager.getConnection(dbUrl,"webdb","webdb");
			
			//3.statement 생성
			stmt = connection.createStatement();
			
			String sql="select no, name, password, message, to_char(reg_date,'YYYY-MM-DD HH:MI:SS') from guestbook ORDER BY no desc";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				Long no=rs.getLong(1);
				String name=rs.getString(2);
				String password=rs.getString(3);
				String message = rs.getString(4);
				String reg_date = rs.getString(5);
				
				GuestbookVo vo= new GuestbookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setPassword(password);
				vo.setMessage(message);
				vo.setReg_date(reg_date);
				
				list.add(vo);
				
				
			
			}
			
			
			
		}catch(ClassNotFoundException ex){
			System.out.println("드라이버 연결 오류.:"+ex);
		}catch(SQLException ex){
			ex.printStackTrace();
		}finally{
			
			try{
				if(rs != null){
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
				if(connection != null){
					connection.close();
				}
				
			}catch(SQLException ex){
				ex.printStackTrace();
			}
		}
		return list;
	}
	public void insert(GuestbookVo vo){
		Connection connection =null;
		PreparedStatement pstmt=null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dbUrl="jdbc:oracle:thin:@localhost:1521:xe";
			connection = DriverManager.getConnection(dbUrl,"webdb","webdb");
			
			//3.statement 준비
			String sql="insert into guestbook values(GUESTBOOK_SEQ.nextval,?,?,?,SYSDATE)";
			pstmt = connection.prepareStatement(sql);
			
			//4.binding
			pstmt.setString(1,vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getMessage());
			
			//5.SQL실행
			pstmt.executeUpdate();
			
			
			
		}catch(ClassNotFoundException ex){
			System.out.println("드라이버 로딩 실패 : "+ex);
		}catch(SQLException ex){
			System.out.println("SQL error : "+ ex);
		}
		finally{
			try{
				if(pstmt !=null){
					pstmt.close();
				}
				if(connection != null){
					connection.close();
				}
			}catch(SQLException ex){
				ex.printStackTrace();
			}
		}
	}
	public void delete(GuestbookVo vo){
		Connection connection = null;
		PreparedStatement pstmt= null;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String dbUrl="jdbc:oracle:thin:@localhost:1521:xe";
			connection = DriverManager.getConnection(dbUrl,"webdb","webdb");
			
			//3.statement 준비
			String sql="delete from guestbook where no = ? and password=?";
			pstmt = connection.prepareStatement(sql);
			
			//4.binding
			pstmt.setLong(1,vo.getNo());
			pstmt.setString(2,vo.getPassword());
			
			//5.SQL 실행
			pstmt.executeUpdate();
			
			
		}catch(ClassNotFoundException ex){
			System.out.println("드라이버 로딩 실패: "+ex);
		}catch(SQLException ex){
			System.out.println("에러 : "+ex);
		}finally{
			try{
				if(pstmt != null){
					pstmt.close();
				}
				if(connection != null){
					connection.close();
				}
				
			}catch(SQLException ex){
				ex.printStackTrace();
			}
		}
	}
}
