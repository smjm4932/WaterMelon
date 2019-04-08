package hewon;

//DB에 접속해서 원하는 데이터를 검색,입력,수정,삭제시키기위해서
//필요로하는 메서드를 선언->호출 할 수 있도록 설계된 클래스
//1.DBConnectionMgr--->MemberDAO의 메서드 호출(has a 관계)

import java.sql.*;//DB
import java.util.*;//자료구조->Vector,ArrayList,,,

public class MemberDAO {
	// 1.DBConnectionMgr객체를 선언(멤버변수)
	private DBConnectionMgr pool = null; // getConnection(),freeConnection()

	// 공통으로 접속할 경우 필요한 멤버변수
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";// 실행시킬 SQL구문 저장

	// 2.생성자를 통해서 객체를 얻어오는 구문
	public MemberDAO() {
		try {
			pool = DBConnectionMgr.getInstance();
			System.out.println("pool=>" + pool);
		} catch (Exception e) {
			System.out.println("DB연결 실패->" + e);// e.toString()
		}
	}// 생성자
		// 1)요구분석에 따른 회원로그인을 체크인 해주는 메서드 필요->
		// 형식) public 반환형 메서드명(자료형(String) 2개)
		// select id,passwd from member where id='nup' and passwd='1234';

	public boolean loginCheck(String id, String passwd) {
		// 1.DB연결
		boolean check = false;
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			System.out.println("con=>" + con);
			sql = "select id,passwd from member where  id=? and passwd=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			check = rs.next();// 데이터가 존재->true, 없으면 ->false
		} catch (Exception e) {
			System.out.println("logCheck()실행 에러유발->" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return check;
		// 3.DB연결해제
	}

	// 2)회원가입->중복 id를 체크인 해주는 메서드가 필요
	// select id from member where id='nup';
	public boolean checkId(String id) {
		// 1.DB연결
		boolean check = false;
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			sql = "select id from member where  id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			check = rs.next();// 데이터가 존재->true, 없으면 ->false
		} catch (Exception e) {
			System.out.println("checkId()실행 에러유발->" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return check;
		// 3.DB연결해제
	}

	// 3)우편번호->우편번호를 검색->자동으로 입력
	// sql>
	public Vector<ZipcodeDTO> zipcodeRead(String area3) {
		Vector<ZipcodeDTO> vecList = new Vector();

		try {
			con = pool.getConnection();
			// select * from zipcode where area3 like '수유3동%';
			sql = "select * from zipcode where area3 like '" + area3 + "%'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// 찾은 레코드가 한개 if(rs.next()) 한개이상 ->while(rs.next())
			while (rs.next()) {
				// vector or ZipcodeDTO저장(rs.getXXX(필드명))->필드별로 저장,꺼내오기
				ZipcodeDTO tempZipcode = new ZipcodeDTO();
				tempZipcode.setZipcode(rs.getString("zipcode"));// "142-098"
				tempZipcode.setArea1(rs.getString("area1"));// "서울"
				tempZipcode.setArea2(rs.getString("area2"));
				tempZipcode.setArea3(rs.getString("area3"));
				tempZipcode.setArea4(rs.getString("area4"));
				// vecor or ArrayList에 담는 구문
				vecList.add(tempZipcode);
			}
		} catch (Exception e) {
			System.out.println("zipcodeRead()실행오류=>" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return vecList;
	}

	// 4)회원가입->insert into member values(?,?,?,?~);=>반환값 1,0
	// MemberDTO->1.테이블의 필드별로 저장,출력 2.메서드의 매개변수와 반환형으로 사용
	public boolean memberInsert(MemberDTO mem) {// 웹에서 입력한 값 전체값

		boolean check = false;// 회원가입 성공유무
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			// ----트랜잭션 처리-----------------------------
			con.setAutoCommit(false);// 자동 commit을 하도 못하도록 설정
			// -------------------------------------------------
			sql = "insert into member values(?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem.getMem_id()); // (1,"test")
			pstmt.setString(2, mem.getMem_passwd());
			pstmt.setString(3, mem.getMem_name());
			pstmt.setString(4, mem.getMem_email());
			pstmt.setString(5, mem.getMem_phone());
			pstmt.setString(6, mem.getMem_zipcode());
			pstmt.setString(7, mem.getMem_address());
			pstmt.setString(8, mem.getMem_job());

			int insert = pstmt.executeUpdate();// 반환 1 (성공), 0 (실패)
			System.out.println("insert(데이터 입력유무)=>" + insert);
			con.commit();// 실질적으로 insert 작동된다.
			if (insert > 0) {// if(insert==1)
				check = true;// 데이터 성공확인
			}
		} catch (Exception e) {
			System.out.println("memberInsert()실행 에러유발->" + e);
		} finally {
			pool.freeConnection(con, pstmt);
		}
		return check;
	}

	// 5)회원수정->특정 회원 찾기->nup
	// 형식)select * from member where id='nup';
	public MemberDTO getMember(String mem_id) {

		MemberDTO mem = null;// id값에 해당되는 레코드 한개를 저장할 객체선언

		try {
			con = pool.getConnection();
			sql = "select * from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem_id);
			rs = pstmt.executeQuery();
			// 찾은 레코드가 한개 if(rs.next()) 한개이상 ->while(rs.next())
			if (rs.next()) {
				mem = new MemberDTO();
				// 찾은 필드값->Setter Method저장->웹에 출력->Getter Method
				mem.setMem_id(rs.getString("id")); // <%=mem.getMem_id()%>
				mem.setMem_passwd(rs.getString("passwd"));
				mem.setMem_name(rs.getString("name"));
				mem.setMem_phone(rs.getString("phone"));
				mem.setMem_zipcode(rs.getString("zipcode"));
				mem.setMem_address(rs.getString("address"));
				mem.setMem_email(rs.getString("e_mail"));
				mem.setMem_job(rs.getString("job"));
			}
		} catch (Exception e) {
			System.out.println("getMember()실행오류=>" + e);// 로그파일로 작성->출력
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return mem;
	}

	// 6)찾은 데이터를 수정->회원가입과 동일
	// update 테이블명 set 필드명=값,,, where 조건식
	public boolean memberUpdate(MemberDTO mem) {

		boolean check = false;// 회원수정 성공유무
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			// ----트랜잭션 처리-----------------------------
			con.setAutoCommit(false);// 자동 commit을 하도 못하도록 설정
			// -------------------------------------------------
			sql = "update member set passwd=?,name=?,e_mail=?,phone=?," + " zipcode=?,address=?, job=?  where  id=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mem.getMem_passwd());
			pstmt.setString(2, mem.getMem_name());
			pstmt.setString(3, mem.getMem_email());
			pstmt.setString(4, mem.getMem_phone());
			pstmt.setString(5, mem.getMem_zipcode());
			pstmt.setString(6, mem.getMem_address());
			pstmt.setString(7, mem.getMem_job());
			pstmt.setString(8, mem.getMem_id());

			int update = pstmt.executeUpdate();// 반환 1 (성공), 0 (실패)
			System.out.println("update(데이터 수정유무)=>" + update);
			con.commit();// 실질적으로 update 작동된다.
			if (update == 1) {
				check = true;// 수정성공
			}
		} catch (Exception e) {
			System.out.println("memberUpdate()실행 에러유발->" + e);
		} finally {
			pool.freeConnection(con, pstmt);// rs는 없다.(select가 아님)
		}
		return check;
	}

	// 7)회원 탈퇴->실행시킬 sql구문이 있다
	// select passwd from member where id=?
	// 형식)delete from member where id='nup';
	public int deleteMember(String id, String passwd) {
		String dbpasswd = "";// DB상에서 찾은 암호를 저장
		int x = -1;// 회원탈퇴 유무

		try {
			con = pool.getConnection();
			con.setAutoCommit(false);// 트랜잭션처리
			// 1.id값에 해당하는 암호먼저 찾기
			pstmt = con.prepareStatement("select passwd from member where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			// 암호가 존재한다면
			if (rs.next()) {
				dbpasswd = rs.getString("passwd");// rs.getString(1);
				System.out.println("dbpasswd=>" + dbpasswd);
				// dbpasswd(DB상에서 찾은암호)==passwd(웹상에서 입력한 값)
				if (dbpasswd.equals(passwd)) {
					pstmt = con.prepareStatement("delete from member where id=?");
					pstmt.setString(1, id);
					int delete = pstmt.executeUpdate();
					System.out.println("delete(회원탈퇴 성공유무)=>" + delete);
					con.commit();
					x = 1;// 회원탈퇴 성공
				} else {
					x = 0;// 회원탈퇴 실패->암호가 틀린경우
				}
			} /*
				 * else { x=-1;//암호가 존재하지 않은 경우 }
				 */
		} catch (Exception e) {
			System.out.println("deleteMember() 호출됨=>" + e); // con.rollback();
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return x;// 1,0,-1
	}

	// 8)회원리스트 및 페이징 처리->게시판의 글목록보기 및 페이징 처리
	// select * from member ->getMemberList()
	// public ArrayList<MemberDTO> getMemberList(){}
	public ArrayList<MemberDTO> getMemberList() {

		ArrayList<MemberDTO> arrList = new ArrayList();

		try {
			con = pool.getConnection();
			sql = "select * from member";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberDTO tempMember = new MemberDTO();
				tempMember.setMem_id(rs.getString("id"));
				tempMember.setMem_passwd(rs.getString("passwd"));
				tempMember.setMem_name(rs.getString("name"));
				tempMember.setMem_email(rs.getString("e_mail"));
				tempMember.setMem_zipcode(rs.getString("zipcode"));
				tempMember.setMem_address(rs.getString("address"));
				tempMember.setMem_job(rs.getString("job"));
				arrList.add(tempMember);
			}
		} catch (Exception e) {
			System.out.println("getMemberList()메서드 에러 유발 =>" + e);
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return arrList;
	}
}
