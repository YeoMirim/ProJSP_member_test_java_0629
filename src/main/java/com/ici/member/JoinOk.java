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


@WebServlet("/JoinOk")		// join.jsp에서 명시한 이름으로 호출됨
public class JoinOk extends HttpServlet {
	
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
		request.setCharacterEncoding("utf-8");	//한글깨짐 방지
		
		String mid = request.getParameter("memberId");	// 값을 다 빼옴
		String mpw = request.getParameter("memberPw");
		String mname = request.getParameter("memberName");
		String memail = request.getParameter("memberEmail");
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone3 = request.getParameter("phone3");
		String mgender = request.getParameter("memberGender");
		
		String mphone = phone1 + "-" + phone2 + "-" + phone3;//전화번호
		
		String sql = "INSERT INTO testmember(id, pw, name, email, phone, gender) VALUES ('"+ mid +"','" + mpw + "','" + mname + "','" + memail + "','" + mphone + "','" + mgender + "')";
		// 해당하는 셀에 빼온 값을 넣어줌
		//data source 설정
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/odbo";
		String username = "root";
		String password = "12345";
		
		
		try{
			Class.forName(driverName);	//드라이버 로딩
			conn = DriverManager.getConnection(url, username, password);	//데이터베이스 연동
			stmt = conn.createStatement();	//sql을 실행해주는 statement 객체 생성
			
			int resultCheck = stmt.executeUpdate(sql);	//SQL실행->1이 반환되면 성공, 아니면 실패
			
			if(resultCheck == 1){
				response.sendRedirect("joinSucess.jsp");
			} 
			else {
				response.sendRedirect("join.jsp");
			}
				
		} 
		catch(Exception e) {
			e.printStackTrace();
		} 
		finally {
			try{
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
