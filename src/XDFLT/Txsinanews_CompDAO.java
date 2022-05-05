package XDFLT;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.tej.error.ErrorTitle;
import com.tej.frame.DaoFrame;
import com.tej.postgresql.connection.IDBConnector;


public class Txsinanews_CompDAO extends DaoFrame{
	private String tableNameComp;
	private IDBConnector market;
	private String NEW_LINE = System.getProperty("line.separator");
	public Txsinanews_CompDAO(String tableNameComp  , IDBConnector market){
		this.tableNameComp = tableNameComp;
	    this.market = market;
	}
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 非版本別 先修改後新增
	 * @param listMap_comp
	 * @throws SQLException 
	 * @throws Exception
	 */
	public void modify(Object[] tempMap) throws Exception {
		
		if(tempMap.length == 0){
			logger.warn("本次資料0筆,不處理匯入");
			return ;
		}
		
		Txsinanews_Comp[] listMap_comp = (Txsinanews_Comp[])tempMap;
		int counter = 0;
		PreparedStatement pstmt = null ;
		Connection conn = null;
		try{
			conn = this.market.getConnection();
			
			//設定連線
			conn.setAutoCommit(false);
			
			logger.info(this.getClass().getName()+" 本次資料共"+listMap_comp.length+"筆");
			//建立表格
			logger.info("step1 更新資料(SQL):");
			
			String updateSQL = getUpadteSQL();
			pstmt = conn.prepareStatement(updateSQL);
			logger.debug("更新指令:"+updateSQL);
			
			int[] ok = new int[listMap_comp.length] ;
			for(int j=0;j<listMap_comp.length;j++){
				Txsinanews_Comp obj = listMap_comp[j];
				try{
					pstmt.clearParameters();
					pstmt.setDate(1,obj.getAnn_date());
					pstmt.setString(2,obj.getLink());
					pstmt.setString(3,obj.getStk_id());
//					pstmt.setString(4,obj.getMkt());
					ResultSet res = pstmt.executeQuery();
					while(res.next()){
						String judge = "";
						judge = res.getString("stk_id");
						if(!judge.equals("")) {
							ok[j] = 1;
						}
					}
					
					
					conn.commit();
					counter++;
				}catch(Exception e){
				    logger.error(ErrorTitle.UPDATE_TITLE.getTitle(obj.getPK()) , e);
					
					ok[j] = -1;
					conn.rollback();
				}
			}
			logger.info("更新完畢, 共"+counter+"筆執行更新指令");
			counter = 0;
			
			logger.info("step2 新增資料(SQL):");
			String insertSQL = this.getInsertSQL();
			pstmt = conn.prepareStatement(insertSQL);
			logger.debug("新增指令:"+insertSQL);
			
			for(int i = 0;i<listMap_comp.length;i++){
				//已 update 過的不再新增
				if(ok[i]==1)continue ;
				Txsinanews_Comp obj = listMap_comp[i];
				try{
					pstmt.clearParameters();
					
					pstmt.setDate(1,obj.getAnn_date());
					pstmt.setString(2,obj.getLink());
					pstmt.setString(3,obj.getStk_id());
					pstmt.setString(4,obj.getMkt());
					pstmt.setObject(5,obj.getTej_comp_id(),java.sql.Types.BIGINT);
					pstmt.execute();
					
					conn.commit();
					counter++;
				}catch(Exception e){
					logger.error(ErrorTitle.INSERT_TITLE.getTitle(obj.getPK()) , e);
					
					ok[i] = -1;
					conn.rollback();
				}
			}
			pstmt.close();
			logger.info("新增完畢, 共"+counter+"筆新增");
			counter = 0;
			
		}catch (Exception e) {
			try { if (conn != null) conn.rollback();} catch(Exception ee) {logger.error(e);}
			throw e ;
		}finally{
            try { if (pstmt != null) pstmt.close();} catch(Exception e) {logger.error(e);}
            try { if (conn != null) conn.close();} catch(Exception e) {logger.error(e);}
		}
		
		//此function例外錯誤(log紀錄)由抽象類別(DaoFrame)統一回傳
	}
	
	
	public void updateTej_comp_id() throws Exception {
		PreparedStatement pstmt = null ;
		Connection conn = null;
		try{
			conn = this.market.getConnection();
			//設定連線
			conn.setAutoCommit(false);
			logger.info("更新tej_comp_id");
			//建立表格
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd");
			Date current = new Date();
			String annDate = sdFormat.format(current);
			String updateSQL = getTej_comp_idSQL();
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setDate(1,checkDate(annDate));
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			logger.info("tej_comp_id更新完畢");
		}catch (Exception e) {
			logger.error(ErrorTitle.UPDATE_TITLE.getTitle(), e);
			conn.rollback();
			try { if (conn != null) conn.rollback();} catch(Exception ee) {logger.error(e);}
			throw e;
		}finally{
            try { if (pstmt != null) pstmt.close();} catch(Exception e) {logger.error(e);}
            try { if (conn != null) conn.close();} catch(Exception e) {logger.error(e);}
		}
			
	}


	/**
	 * 新增
	 * @return
	 * @throws Exception
	 */
	public String getInsertSQL(){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" INSERT INTO "+tableNameComp);
		sql.append("( ann_date,link,stk_id,mkt,tej_comp_id)");
		sql.append("  values ( ? , ? , ? , ?, ? )");
				
		return sql.toString();
		
	}	
	/**
	 * 更新
	 * @return
	 * @throws Exception
	 */
	public String getUpadteSQL(){
		
		StringBuilder sql = new StringBuilder();

		sql.append(" select * from  "+tableNameComp);
		sql.append(" WHERE (ann_date = ?  AND link = ?  AND stk_id = ?  )"); 
				
		return sql.toString();
		
	}
	
	
	public String getTej_comp_idSQL(){
		
		StringBuilder sql = new StringBuilder();
//		sql.append(" UPDATE  ").append(tableNameComp);
//		sql.append(" SET tej_comp_id = stk.attr_event.tej_comp_id ");
//		sql.append(" FROM stk.attr_event ");
//		sql.append(" WHERE stk.attr_event.stk_id = fincd.fincd_txsinanews_comp.comp_id ");
		
		sql.append(" UPDATE  ").append(tableNameComp);
		sql.append(" SET tej_comp_id = stk.global_attr_event.tej_comp_id ");
		sql.append(" FROM stk.global_attr_event ");
		sql.append(" where source_country IN ('CHN','HKG') and ");
		sql.append(" stk.global_attr_event.stk_id = fincd.fincd_txsinanews_comp.stk_id");
		sql.append(" and fincd.fincd_txsinanews_Comp.keyin >= ? ");
		
		return sql.toString();	
	}
	private java.sql.Date checkDate(String d) throws ParseException {
		if (d.trim().equals(""))
			return null;
		else
			return new java.sql.Date(sdf.parse(d).getTime());
	}
}
