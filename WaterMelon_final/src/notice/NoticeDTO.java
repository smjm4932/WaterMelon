package notice;

import java.sql.Timestamp;

//DTO->입력폼의 name와 반드시 같아야 한다.->액션태그때문에
public class NoticeDTO {

	private int n_Num;
	private String n_subject, n_content;
	private Timestamp writedate;
	private int readCount;
	
	
	public int getN_Num() {
		return n_Num;
	}
	public void setN_Num(int n_Num) {
		this.n_Num = n_Num;
	}
	public String getN_subject() {
		return n_subject;
	}
	public void setN_subject(String n_subject) {
		this.n_subject = n_subject;
	}
	public String getN_content() {
		return n_content;
	}
	public void setN_content(String n_content) {
		this.n_content = n_content;
	}
	public Timestamp getWritedate() {
		return writedate;
	}
	public void setWritedate(Timestamp writedate) {
		this.writedate = writedate;
	}
	public int getReadCount() {
		return readCount;
	}
	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}
	
}
