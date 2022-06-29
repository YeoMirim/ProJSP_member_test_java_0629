package com.ici.member;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



// 해당 클래스가 서블릿이라고 명시
@WebServlet("/LoginOk")		// login.jsp에서 명시한 이름으로 호출됨
public class LoginOk extends HttpServlet {		// HttpServlet를 상속받아 JSP가 아닌 여기에서 LoginOk를 구현함
	// HttpServlet는 기본적인 메소드 지원
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	private String id, pw, name, email, phone, gender;

	@Override	// Get방식으로 가져온 데이터를 받음
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		actionDo(req, resp);	// req는 request객체, resp는 response객체
	}

	@Override	// Post방식으로 가져온 데이터를 받음
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		actionDo(req, resp);
	}
	
	private void actionDo (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");//한글깨짐 방지
		
		String mid = request.getParameter("userId");
		String mpw = request.getParameter("userPw");
		
		String driverName = "com.mysql.jdbc.Driver";		//	jar파일이 없으면 드라이버를 못불러옴
		String url = "jdbc:mysql://localhost:3306/odbo";
		String username = "root";
		String password = "12345";
		
		String sql = "SELECT * FROM testmember WHERE id = '"+mid+"' AND pw = '"+mpw+"'";
		// 찾으면 결과값 반환
		
		try{
			Class.forName(driverName);	//드라이버 로딩
			conn = DriverManager.getConnection(url, username, password);	//데이터베이스 연동
			stmt = conn.createStatement();	//sql을 실행해주는 statement 객체 생성
			
			rs = stmt.executeQuery(sql);		// sql실행 결과값 받음, select문은 executeQuery로 사용, 이외는 executeUpdate로 받음
	
			int loginFlag = 0;	// 못찾으면 0으로 계속 유지, 일치하는 데이터 하나라도 찾으면 증가됨
			
			HttpSession session = request.getSession();	// while문이라서 request문에서 뽑아와 초기값을 줘야함
			
			while(rs.next()) {	// select문에서 해당 값 빼와서 session에 name,id,pw를 올림
				id = rs.getString("id");
				pw = rs.getString("pw");
				name = rs.getString("name");
				email = rs.getString("email");
				phone = rs.getString("phone");
				gender = rs.getString("gender");
				System.out.println("아이디:"+ id);
				
				session.setAttribute("name", name);
				session.setAttribute("id", id);
				session.setAttribute("pw", pw);
				
				loginFlag++;
			}	 
			
			if(loginFlag == 0) {
				response.sendRedirect("login.jsp");
			} 
			else {			
				response.sendRedirect("loginSucess.jsp");
			}
		} 
		catch(Exception e) {		// 예외 처리
			e.printStackTrace();
		} 
		finally {					// 반드시 실행
			try{					// 열었으면 닫음(여럿이 사용하면 꼬일 수 있으므로)
				if(rs != null) {	
					rs.close();
				}
				if(stmt != null){
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} 
			catch(Exception e) {
				e.printStackTrace();
			}
		}	
	}
}
