package notice;

//DB사용
import java.sql.*;

//ArrayList,List을 사용하기 위해서
import java.util.*;

public class NoticeDAO {

	//DB 접속 경우 필요한 멤버변수 선언
	private DBConnectionMgr pool=null; 
	private Connection con=null;
	private PreparedStatement pstmt=null;
	private ResultSet rs=null;
	private String sql="";//실행시킬 SQL구문 저장
	
	//생성자 통해서 연결
	public NoticeDAO() {
		try {
			pool=DBConnectionMgr.getInstance();
			System.out.println("pool=>"+pool);
		}catch(Exception e) {
			System.out.println("DB접속오류=>"+e);
		}
	}
	 
	
	//레코드 갯수를 구함.
	public int getNoticeCount() { 
		int x=0;//레코드갯수
		
		try {
			con=pool.getConnection();
			System.out.println("con="+con);
			sql="select count(*) from notice";  
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) { 
				x=rs.getInt(1);                    
			}
		}catch(Exception e) {
			System.out.println("getNoticeCount()메서드 에러유발"+e);
		}finally {
			pool.freeConnection(con,pstmt,rs);
		}
		return x;
	}
	
	// 글 목록보기
	public List getNotice(int start,int end) {
		List noticeList=null; 
		
		try {
			con=pool.getConnection();
			sql="select * from notice order by n_Num desc limit ?,?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, start-1);
			pstmt.setInt(2, end); 
			rs=pstmt.executeQuery();
			if(rs.next()) {
				noticeList=new ArrayList(end);
				do {
					NoticeDTO article=makeNotice();
					noticeList.add(article);
				}while(rs.next());
			}
		}catch(Exception e) {
			System.out.println("getNotice()메서드 에러유발"+e);
		}finally {
			pool.freeConnection(con, pstmt,rs);
		}
		return noticeList;
	}
	
	
	//페이징 처리를 따로 해주는 메서드 작성
	public Hashtable pageList(String pageNum, int count) {
		
		Hashtable<String,Integer> pgList = new Hashtable<String, Integer>();
		
		int pageSize=6;
		int blockSize=3;
		
		if(pageNum==null) {
			pageNum="1";
		}
		int currentPage=Integer.parseInt(pageNum);//현재페이지->nowPage
	    //시작레코드번호 ->limit ?,?
	    //                  (1-1)*10+1=1,(2-1)*10+1=11,(3-1)*10+1=21
	    int startRow=(currentPage-1)*pageSize+1;		
	    int endRow=currentPage*pageSize;//1*10=10,2*10=20,3*10=30
	    int number=0; // beginPerPage -> 페이지별 시작하는 맨 처음에 나오는 게시물 번호
	    System.out.println("현재 레코드수(count)=>"+count);
	    number=count-(currentPage-1)*pageSize;
	    System.out.println("페이지별 number=>"+number);
	    
	  //총 페이지수, 시작, 종료페이지 계산
	    int pageCount=count/pageSize+(count%pageSize==0?0:1);
	       //2.시작페이지 
	       //블럭당 페이지수 계산->10->10배수,3->3의 배수
	       int startPage=0;//1,2,3,,,,10 [다음블럭 10],11,12,,,,,20
	       if(currentPage%blockSize!=0){ //1~9,11~19,21~29,,,
	    	   startPage=currentPage/blockSize*blockSize+1;
	       }else{ //10%10 (10,20,30,40~)
	    	   //             ((10/10)-1)*10+1=1
	    	  startPage=((currentPage/blockSize)-1)*blockSize+1; 
	       }
	       int endPage=startPage+blockSize-1;//1+10-1=10
	       System.out.println("startPage="+startPage+",endPage=>"+endPage);
	       //블럭별로 구분해서 링크걸어서 출력
	       if(endPage > pageCount) endPage=pageCount;//마지막페이지=총페이지수
	       
	     //~DAO -> 실질적인 업무에 관련된 코딩 -> 액션클래스로 전달 -> view(jsp)에 최종출력
	       pgList.put("pageSize", pageSize);
	       pgList.put("blockSize", blockSize);
	       pgList.put("currentPage", currentPage);
	       pgList.put("startRow", startRow);
	       pgList.put("endRow", endRow);
	       pgList.put("count", count);
	       pgList.put("number", number);
	       pgList.put("startPage", startPage);
	       pgList.put("endPage", endPage);
	       pgList.put("pageCount", pageCount);
	    
	    return pgList;
	}
	
	
	
	
	
	
	//---------------------------중복된 레코드 한번에 담을 수 있는 메서드----
	private NoticeDTO makeNotice() throws Exception{
		NoticeDTO article=new NoticeDTO();
		article.setN_Num(rs.getInt("n_Num"));
		article.setN_subject(rs.getString("n_subject"));
		article.setN_content(rs.getString("n_content"));
		article.setWritedate(rs.getTimestamp("writedate"));
		article.setReadCount(rs.getInt("readCount"));
		return article;
	}
	
	
}
