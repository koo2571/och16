package service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Board;
import dao.BoardDao;

public class ListAction implements CommandProcess {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String pageNum = request.getParameter("pageNum");
			if(pageNum == null || pageNum.equals("")) pageNum="1";
			//현재페이지
			int currentPage = Integer.parseInt(pageNum);
			//  한번에 보여주는것    밑에 페이지 넘버
			int pageSize = 10, blockSize = 10;
			BoardDao bd = BoardDao.getInstance();
			int startRow = (currentPage-1)*pageSize+1;  // 초기값:1
			int endRow = startRow + pageSize-1;			// 초기값:10
			int totCnt = bd.getTotalCnt();
			int startNum = totCnt - startRow+1;  //38 ->최신글을 가장 먼저 띄워주기위한 변수
			List<Board> list = bd.list(startRow,endRow);
			// 페이지 수               올림 (반올림이나 내림을 하면 남은 게시물이 올라오지를 못함)
			int pageCnt = (int)Math.ceil((double)totCnt/pageSize); 
			int startPage = (int)(currentPage-1)/blockSize*blockSize+1; //초기값: 1
			int endPage = startPage + blockSize - 1;                    //초기값: 10
			//쓸데없는 페이지 번호를 안나타나게 하기위한 로직
			if(endPage > pageCnt) endPage = pageCnt;
			
			request.setAttribute("totCnt", totCnt); 
			request.setAttribute("list", list); 
			request.setAttribute("startNum", startNum);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("currentPage", currentPage);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("blockSize", blockSize);
			request.setAttribute("pageCnt", pageCnt);
			request.setAttribute("startPage", startPage);
			request.setAttribute("endPage", endPage);
			System.out.println("==========================================");
			System.out.println("pageNum -> "+pageNum);
			System.out.println("currentPage -> "+currentPage);
			System.out.println("pageSize -> "+pageSize);
			System.out.println("blockSize ->" + blockSize);
			System.out.println("pageCnt -> "+pageCnt);
			System.out.println("startPage -> "+startPage);
			System.out.println("endPage -> "+endPage);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "list.jsp";
	}

}
