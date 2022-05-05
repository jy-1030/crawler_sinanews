package XDFLT;

import org.apache.log4j.xml.DOMConfigurator;
import com.cake.net._useage.Parameter;
import com.tej.error.ErrorTitle;
import com.tej.frame.PorcessFrame;
import com.tej.frame.Table;
import com.tej.postgresql.DBAdminConnector;
import com.tej.postgresql.connection.ConnectorTableBuilder;
import com.tej.postgresql.connection.IDBConnector;

/**
 * 獨立開發程式
 * 
 * @author :
 *
 */
public class TEJ_E21_NDB_XDFLT_01 extends PorcessFrame {
	public void taskDescription(Parameter param, IDBConnector market, String[] args) {
		super.parameter = param; // 參數檔
		super.market = market; // 匯入連線資訊

		String url = parameter.getUrl();
		String url2 = parameter.getMenu().get("url2");

		String tableName = parameter.getTableName();
		String tableNameComp = parameter.getMenu().get("tableNameComp");
		String tableNamenoid = parameter.getMenu().get("tableNameNoid");

		DL_E21_NDB_XDFLT_01 dl = new DL_E21_NDB_XDFLT_01(parameter);

		// 第一段 : 抓檔 與 網頁處理(若讀取外部檔匯入可以不需此段)

		String source = "";
		String source2 = "";
		try {
			// page1-20
//			for (int i =1; i <=3; i++) {
//					logger.info("網址 : "+url+i);
//					logger.info("網址 : "+url2+i);
//					
////					source = dl.getData(url+i);
//					source2 = dl.getHKnews(url2+i);
//			}
			String page = param.getMenu().get("page");
			int page2 = Integer.parseInt(page);
			for (int i = 1; i <= page2; i++) {
				logger.info("ch網址 : " + url + i);
				logger.info("hk網址 : " + url2 + i);
//				source = dl.getHKnews(url + i); //ch
				source = dl.getData(url + i); //ch
				source2 = dl.getHKnews(url2 + i); //hk
				// 第二段 : 分析資料存入暫存處理
				Table[] tableList = null;
				Table[] tableList2 = null;
				try {
					tableList = dl.parseListMap(source);
					tableList2 = dl.parseListMap2(source2);
				} catch (Exception e) {
					logger.error(ErrorTitle.PROCESS_TITLE.getTitle(), e);
				}

				// //資料處理暫存筆數為0 不需再執行匯入
				if (parameter.isDltestMode()) { // 偵側網改程式，參數test
					if (tableList != null && tableList[0].getTableBean().length == 0)
						logger.error(ErrorTitle.IMPORT_TITLE.getTitle("截取 0 筆資料"));
				} else if (tableList != null) {

					// 第三段 : 匯入資料庫處理
					TxsinanewsDAO dao = new TxsinanewsDAO(tableName, market);
					Txsinanews_CompDAO dao_comp = new Txsinanews_CompDAO(tableNameComp, market);
					Txsinanews_noidDAO dao_noid = new Txsinanews_noidDAO(tableNamenoid, market);

					try {
						boolean ishead = false;
						boolean ishead2 = false;
//						 依照分析資料存入暫存 處理的內容決定使用的表格
						for (Table table : tableList) {
							Object[] tableArray = table.getTableBean();
							switch (table.getDescription()) {
							case "Txsinanews":
								dao.modify(tableArray);
								break;
							case "Txsinanews_Comp":
								dao_comp.modify(tableArray);
								ishead=true;
								
								break;
							case "Txsinanews_noid":
								dao_noid.modify(tableArray);
								break;
							}
						}
						for (Table table : tableList2) {
							Object[] tableArray = table.getTableBean();
							switch (table.getDescription()) {
							case "Txsinanews":
								dao.modify(tableArray);
								break;
							case "Txsinanews_Comp":
								dao_comp.modify(tableArray);
								ishead2=true;
								break;
							case "Txsinanews_noid":
								dao_noid.modify(tableArray);
								break;
							}
						}
						if(ishead || ishead2) {
							dao_comp.updateTej_comp_id();  //tej_comp_id 配碼 有點問題
						}
					} catch (Exception e) {
						logger.error(ErrorTitle.IMPORT_TITLE.getTitle(), e);
//					}

				}
	}	
			} // for loop pages
		} catch (Exception e) {
			logger.warn(ErrorTitle.ANALYSIS_TITLE.getTitle("網址有錯"), e);
		}
	}

	public static void main(String[] args) {

		DOMConfigurator.configure(".\\log4j.xml");

		// admin連線資訊
		IDBConnector admin = DBAdminConnector.getInstance();

		// 宣告RFP名稱字串
		String spt = System.getProperty("file.separator");
		String propertyPath = System.getProperty("user.dir") + spt + "property" + spt + "TEJ_E21_NDB_XDFLT_01.property";

		// 宣告參數檔物件(給予檔案路徑)
		Parameter param = new Parameter(propertyPath);

		// 初始化物件
		param.initial();

		// 欲匯入表格連線資訊
		ConnectorTableBuilder builder = new ConnectorTableBuilder(admin, param.getDbName());

		try {
			IDBConnector market = builder.buildConnector();

			TEJ_E21_NDB_XDFLT_01 runPg = new TEJ_E21_NDB_XDFLT_01();

			runPg.taskDescription(param, market, args);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
