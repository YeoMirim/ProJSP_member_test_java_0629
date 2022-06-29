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


@WebServlet("/ModifyOk")		// modify.jsp에서 명시한 이름으로 호출됨
public class ModifyOk extends HttpServlet {

	private Connection conn;	
	private Statement stmt;		// sql을 실행해주는 statement 객체
//	private ResultSet rs;
	
	private String id, pw, name, email, phone, gender;
	
	HttpSession session;
	
	
	@Override	// Get방식으로 가져온 데이터를 받음
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionDo(req, resp);	// req는 request객체, resp는 response객체
	}

	@Override	// Post방식으로 가져온 데이터를 받음
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		actionDo(req, resp);
	}
	
	private void actionDo (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	// doGet, doPost 두개 다 이 메소드 실행
		
		request.setCharacterEncoding("utf-8");	//한글깨짐 방지
		session = request.getSession();
		
		String mid = request.getParameter("memberId");
		String mpw = request.getParameter("memberPw");
		String mname = request.getParameter("memberName");
		String memail = request.getParameter("memberEmail");
		String mphone = request.getParameter("memberPhone");	
		String mgender = request.getParameter("memberGender");
		
		
		String sessionPw = (String)session.getAttribute("pw");
	
		if(sessionPw.equals(mpw)) {
		
			String sql = "UPDATE testmember SET name='"+mname+"',email='"+memail+"',phone='"+mphone+"',gender='"+mgender+"' WHERE id='"+mid+"'";
			
			//data source 설정
			String driverName = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/odbo";
			String username = "root";
			String password = "12345";
			
			try{
				Class.forName(driverName);//드라이버 로딩
				conn = DriverManager.getConnection(url, username, password);//데이터베이스 연동
				stmt = conn.createStatement();//sql을 실행해주는 statement 객체 생성
				
				int resultCheck = stmt.executeUpdate(sql);//SQL실행->1이 반환되면 성공, 아니면 실패
				
				if(resultCheck == 1){
					response.sendRedirect("modifySucess.jsp");
				} 
				else {
					System.out.print("회원정보수정단계 실패");
					response.sendRedirect("modify.jsp");
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
		
		else {
			response.sendRedirect("modify.jsp");
		}
	}
}
