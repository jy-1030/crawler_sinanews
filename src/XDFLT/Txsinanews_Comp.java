package XDFLT;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Txsinanews_Comp {

	private Date date2;
	private String link;
	private String stk_id;
	private String mkt;

	private BigInteger tej_comp_id;
	public Date getAnn_date(){
//		return ann_date;
		return date2;
	}
//	public void setAnn_date(Date Ann_date ){
//		this.ann_date = Ann_date;
//	}
	
	public void setAnn_date(Date date2 ){
	this.date2 = date2;
}
	public String getLink(){
		return link;
	}
	public void setLink(String Link ){
		this.link = Link;
	}
	public String getStk_id(){
		return stk_id;
	}
	public void setStk_id(String Stk_id ){
		this.stk_id = Stk_id;
	}
	public BigInteger getTej_comp_id(){
		return tej_comp_id;
	}
	public void setTej_comp_id(BigInteger Tej_comp_id ){
		this.tej_comp_id = Tej_comp_id;
	}
	public String getPK(){
//		return  ann_date+","+link+","+comp_id;
		return date2+","+link+","+stk_id;
	}
	


	public String getMkt() {
		return mkt;
	}

	public void setMkt(String mkt) {
		this.mkt = mkt;
	}

	@Override
	public String toString() {
//		return "Txsinanews_CompDAO [ann_date=" + date2 +", link="+link+ ", stk_id=" + stk_id + ",tej_comp_id=" + tej_comp_id+ ", mkt=" + mkt +"]";
		return "Txsinanews_CompDAO [ann_date=" + date2 +", stk_id=" + stk_id +", mkt=" + mkt + ", link="+link+"]";
		
	}
	
}
