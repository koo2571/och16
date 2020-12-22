package service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class UpdateFormAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//나중 수정
		int num = 1;
		String pageNum = "1";
		try {
			BoardDao bd = BoardDao.getInstance();
			Board board = bd.select(num);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("board", board);
		} catch (Exception e) {
			System.out.println("UpdateFormAction requestPro ->"+e.getMessage());
		}
		return "updateForm.jsp";
	}

}
