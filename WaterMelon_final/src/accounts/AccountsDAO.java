package accounts;

//DBConnectionMgr(DB접속,관리), BoardDTO(매개변수,반환형)
//DB사용
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//ArrayList,List을 사용하기 위해서
import java.util.ArrayList;

public class AccountsDAO { // MemberDAO

	private DBConnectionMgr pool = null;// 1.선언
	// 추가
	// 공통으로 접속할 경우 필요한 멤버변수
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	private String sql = "";// 실행시킬 SQL구문 저장
	// 2.생성자를 통해서 연결=>의존성

	public AccountsDAO() {
		try {
			pool = DBConnectionMgr.getInstance();
			System.out.println("pool=>" + pool);
		} catch (Exception e) {
			System.out.println("DB접속오류=>" + e);
		}
	}// 생성자

	// 요구분석에 따른 회원로그인을 체크인 해주는 메서드 필요->
	// 형식) public 반환형 메서드명(자료형(String) 2개)
	// select id,passwd from member where id='nup' and passwd='1234';

	public boolean loginCheck(String id, String password) {
		// 1.DB연결
		boolean check = false;
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			System.out.println("con=>" + con);
			sql = "select id,passwd from accounts where id=? and password=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
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

	// 회원가입->중복 id를 체크인 해주는 메서드가 필요
	// select id from member where id='nup';
	public boolean checkId(String id) {
		// 1.DB연결
		boolean check = false;
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			sql = "select id from accounts where id=?";
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

	// 회원가입-> 비밀번호와 비밀번호 확인란이 일치하는지 체크 해주는 메서드가 필요
	
	public boolean checkPassword(String password, String password_confirm) {
		
		boolean check = false;
		
		try {
			if (password.equals(password_confirm)) {
				check = true;
			}
		} catch (Exception e) {
			System.out.println("checkPassword()실행 에러유발->" + e);
		}
		System.out.println(check);
		return check;
		// 3.DB연결해제
	}

// 4)회원가입->insert into member values(?,?,?,?~);=>반환값 1,0
// AccountsDTO->1.테이블의 필드별로 저장,출력 2.메서드의 매개변수와 반환형으로 사용
	public boolean memberInsert(AccountsDTO account) {// 웹에서 입력한 값 전체값

		boolean check = false;// 회원가입 성공유무
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			// ----트랜잭션 처리-----------------------------
			con.setAutoCommit(false);// 자동 commit을 하도 못하도록 설정
			// -------------------------------------------------
			sql = "insert into accounts(id, password, nickname, name, email, admin) values(?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			// String id, password, nickname, name, email; int admin;
			pstmt.setString(1, account.getId()); // (1,"test")
			pstmt.setString(2, account.getPassword());
			pstmt.setString(3, account.getNickname());
			pstmt.setString(4, account.getName());
			pstmt.setString(5, account.getEmail());
			pstmt.setInt(6, 0);// 일반 회원은 0 -> default : 0 

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
	public AccountsDTO getMember(String id) {

		AccountsDTO account = null;// id값에 해당되는 레코드 한개를 저장할 객체선언

		try {
			con = pool.getConnection();
			sql = "select * from member where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			// 찾은 레코드가 한개 if(rs.next()) 한개이상 ->while(rs.next())
			if (rs.next()) {
				account = new AccountsDTO();
				// 찾은 필드값->Setter Method저장->웹에 출력->Getter Method
				account.setId(rs.getString("id")); // <%=account.getId()%>
				account.setPassword(rs.getString("password"));
				account.setNickname(rs.getString("nickname"));
				account.setName(rs.getString("name"));
				account.setEmail(rs.getString("email"));
			}
		} catch (Exception e) {
			System.out.println("getMember()실행오류=>" + e);// 로그파일로 작성->출력
		} finally {
			pool.freeConnection(con, pstmt, rs);
		}
		return account;
	}

// 6)찾은 데이터를 수정->회원가입과 동일
// update 테이블명 set 필드명=값,,, where 조건식
	public boolean memberUpdate(AccountsDTO account) {

		boolean check = false;// 회원수정 성공유무
		// 2.실행시킬 SQL->반환값 처리
		try {
			con = pool.getConnection();
			// ----트랜잭션 처리-----------------------------
			con.setAutoCommit(false);// 자동 commit을 하도 못하도록 설정
			// -------------------------------------------------
			sql = "update accounts set password=?, nickname=?, name=?, email=?  where id=?";

			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, account.getPassword());
			pstmt.setString(2, account.getNickname());
			pstmt.setString(3, account.getName());
			pstmt.setString(4, account.getEmail());
			pstmt.setString(5, account.getId());

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
	public int deleteMember(String id, String password) {
		String dbpasswd = "";// DB상에서 찾은 암호를 저장
		int x = -1;// 회원탈퇴 유무

		try {
			con = pool.getConnection();
			con.setAutoCommit(false);// 트랜잭션처리
			// 1.id값에 해당하는 암호먼저 찾기
			pstmt = con.prepareStatement("select password from accounts where id=?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			// 암호가 존재한다면
			if (rs.next()) {
				dbpasswd = rs.getString("password");// rs.getString(1);
				System.out.println("dbpasswd=>" + dbpasswd);
				// dbpasswd(DB상에서 찾은암호)==passwd(웹상에서 입력한 값)
				if (dbpasswd.equals(password)) {
					pstmt = con.prepareStatement("delete from accounts where id=?");
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
}