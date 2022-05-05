package XDFLT;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;


public class Txsinanews_noid {
	

	private Date date2;
	private String link;
	private String ann_title;
	private String rmk;
	public Date getAnn_date(){
		return date2;
	}
	public void setAnn_date(Date date2 ){
		this.date2 = date2;
	}
	public String getLink(){
		return link;
	}
	public void setLink(String Link ){
		this.link = Link;
	}
	public String getAnn_title(){
		return ann_title;
	}
	public void setAnn_title(String Ann_title ){
		this.ann_title = Ann_title;
	}
	public String getRmk(){
		return rmk;
	}
	public void setRmk(String Rmk ){
		this.rmk = Rmk;
	}
	public String getPK(){
		return  date2+","+link;
	}
	
	@Override
	public String toString() {
		return "Txsinanews [ann_date=" + date2 + ",title=" + ann_title + "]";
	}

}
