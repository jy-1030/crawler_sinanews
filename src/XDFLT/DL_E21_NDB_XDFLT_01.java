package XDFLT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.cake.net._useage.Parameter;
import com.cake.net.http.connection.URLProxy;
import com.cake.net.http.html.HTML;
import com.cake.net.http.html.HTMLX;
import com.cake.net.http.html.htmlparser.Tag.iframe;
import com.cake.net.http.html.htmlparser.Tag.p;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.sun.corba.se.spi.orb.StringPair;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.tej.error.ErrorTitle;
import com.tej.frame.DownloadFrame;
import com.tej.frame.Table;
import com.tej.setting.IP;

public class DL_E21_NDB_XDFLT_01 extends DownloadFrame {
	private Parameter parameter;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

//	private String rmk;
	public DL_E21_NDB_XDFLT_01(Parameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * 連結抓取資料處理 網頁連線 or 規則複雜的資料截取
	 */
	@Override
	public String getData(String url) throws Exception {
		String host = "";
		String port = "";
		URLProxy newIP = null;
		HTML htmlx = null;
		List<String> prox = IP.getIPFromTable();
		for (int i = prox.size(); i >= 0; i--) {
			if (i < prox.size()) {
				String proxy = prox.get(i);
				host = proxy.substring(0, proxy.indexOf(':'));
				port = proxy.substring(proxy.indexOf(':') + 1, proxy.length());
				logger.info("使用IP => " + host + ":" + port + "抓取資料..." + (prox.size() - i) + "/" + prox.size());
				newIP = new URLProxy(host, port);
			}
			try {
				htmlx = newIP == null ? new HTMLX(url, false) : new HTMLX(url, newIP, false);
				htmlx.setIgnoreSSLCertificate(true);
				htmlx.setConnectTimeout(parameter.getConnectTimeout(), parameter.getRetry());
				// 設定網頁連結多久中斷 ,為避免連線過久卡在網頁
				htmlx.setReadTimeout(parameter.getReadTimeout(), parameter.getRetry());
				htmlx.connect();
				htmlx.extractInputStream(parameter.getEncoding());

				// 回傳值應為html格式 ,能將資料區塊先截出來最好
				// 轉換IP重試連線等行為在此運行
				// 模擬瀏覽器 模擬行為也在此運行
				// 此function例外錯誤由呼叫class接收
				return htmlx.getCodeStringtype();
			} catch (Exception e) {
				logger.error(e);
			}
			Thread.sleep(5000);
		}
		logger.error("用光所有proxy 還是被擋");
		return "";
	}

	public String getHKnews(String url) throws Exception {
		String host = "";
		String port = "";
		URLProxy newIP = null;
		HTML htmlx = null;
		List<String> prox = IP.getIPFromTable();
		for (int i = prox.size(); i >= 0; i--) {
			if (i < prox.size()) {
				String proxy = prox.get(i);
				host = proxy.substring(0, proxy.indexOf(':'));
				port = proxy.substring(proxy.indexOf(':') + 1, proxy.length());
				logger.info("使用IP => " + host + ":" + port + "抓取資料..." + (prox.size() - i) + "/" + prox.size());
				newIP = new URLProxy(host, port);
			}
			try {
				htmlx = newIP == null ? new HTMLX(url, false) : new HTMLX(url, newIP, false);
				htmlx.setIgnoreSSLCertificate(true);
				htmlx.setConnectTimeout(parameter.getConnectTimeout(), parameter.getRetry());
				// 設定網頁連結多久中斷 ,為避免連線過久卡在網頁
				htmlx.setReadTimeout(parameter.getReadTimeout(), parameter.getRetry());
				htmlx.connect();
				htmlx.extractInputStream(parameter.getEncoding());

				// 回傳值應為html格式 ,能將資料區塊先截出來最好
				// 轉換IP重試連線等行為在此運行
				// 模擬瀏覽器 模擬行為也在此運行
				// 此function例外錯誤由呼叫class接收
				return htmlx.getCodeStringtype();
			} catch (Exception e) {
				logger.warn(ErrorTitle.ANALYSIS_TITLE.getTitle("此IP失敗"), e);
			}
			Thread.sleep(5000);
		}
		logger.error("用光所有proxy 還是被擋");
		return "";
	}

	/**
	 * 分析資料 存入暫存 網頁資料剖析 , 外部檔欄位
	 */
	@Override
	public Table[] parseListMap(String source) {

		// 表格陣列
 		List<Table> tableList = new ArrayList<>();

		// 宣告表格資料暫存陣列
		List<Txsinanews> listMap = new ArrayList<Txsinanews>();
		List<Txsinanews_Comp> listMap_comp = new ArrayList<Txsinanews_Comp>();
		List<Txsinanews_noid> listMap_noid = new ArrayList<Txsinanews_noid>();

		// 此處應只有網頁核心資料已截取過區塊
		// 外部檔案讀取可直接由此處讀入並寫入暫存

		try {
			// news page
			Document doc = Jsoup.parse(source);
			Elements links = doc.getElementsByClass("listBlk");
//			System.out.println("link="+links);

			Elements urls = links.select("li");
			for (Element link : urls) {
//				if(link.select("a").attr("href").contains("page") ||  link.text().contains("下一页") || link.text().contains("大宗交易") || link.text().contains("关于") || link.text().contains("辞职")|| link.text().contains("公告速递")|| link.text().contains("公告提示")|| link.text().contains("")){
				
				if(link.select("a").attr("href").contains("finpagefr")){
//					System.out.println("S="+link);
					
					if (link.text().contains("下一页")
//							|| link.text().contains("公告提示") || link.text().contains("公告速递")
//							|| link.text().contains("公布") && link.text().contains("季报")
//							|| link.text().contains("公布") && link.text().contains("年报")
//							|| link.text().contains("关于") && link.text().contains("辞职") || link.text().contains("大宗交易")
							) {
						continue;
					} else {// titles//
						String t_urls = link.select("a").attr("href"); // article url
						String titles = link.select("a").text(); // article title
						String ann_date = link.select("span").text(); // article date

						
						
						
						System.out.println(titles);
						System.out.println(t_urls);
						// current date
						SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy");
						Date current = new Date();

						// ann_Date
						String str = ann_date;
						String[] date = str.split("");
						String annDate = sdFormat.format(current) + date[1] + date[2] + date[4] + date[5];
						String comp_id = "";
						String mkt = "";

						// string => date ((2021-10-20))
						SimpleDateFormat sdFormat2 = new SimpleDateFormat("yyyyMMdd");
						Date datex = new Date();
						datex = sdFormat2.parse(annDate);
						java.sql.Date date2 = new java.sql.Date(datex.getTime());

						// article content
						
						Document doc2 = Jsoup.connect(t_urls).get();
						Thread.sleep(3000);

						
//						System.out.println("d="+doc2);
						String doc2String = doc2.toString();
						Elements ListDiv = doc2.getElementsByAttributeValue("class", "article");
						String rmk = ListDiv.select("div.article > p").text();
						
//						System.out.println("rmk="+rmk);

						if (doc2String.contains("var strCode")) {
							// ((comp_id= code))
							Elements div11 = doc2.select("script[language=JavaScript]");
							// String comp_id = "";
//				  BigInteger Tej_comp_id;
							ArrayList<String> comp_strings = new ArrayList<String>();

							for (Element scriptElement : div11) {
								String val = scriptElement.data();
								if (val.contains("strCode")) {
									Pattern pattern = Pattern.compile("var strCode = \"[A-Za-z0-9,]+\"");
									Matcher matcher = pattern.matcher(val);
									if (matcher.find()) {
										String code = matcher.group();
										int i = code.indexOf("\"");
										code = code.substring(i, code.length()).replace("\"", "");
//										System.out.println("code="+code);
										
										String[] comp_code = code.split(",");
										for (int j = 0; j < comp_code.length; j++) {
											comp_strings.add(comp_code[j]);
											mkt = comp_strings.toString().replaceAll("[0-9\\s]", "").replace("[", "").replace("]",""); //留字母
											comp_id = comp_strings.toString().replaceAll("[a-zA-z\\s]", ""); //留數字
//											System.out.println("compid="+comp_strings.toString().replaceAll("[0-9]","").replace("[", "").replace("]",""));
//											System.out.println("comp2="+comp_id);
//											System.out.println("mkt="+mkt);
										
										}
									}
									break;
								}
							}
							String[] a = comp_id.split(",");
							String[] b = mkt.split(",");
  							try {
								int temp=0;
								for (int q = 0;q < a.length; q++) {
									for (int o = 0; o < b.length; o++) {

										if(o==q){
											if(b[o].length()>3)
											continue;
									if (a[0] == "") { // var strcode is eng
										Txsinanews_noid noidbean = new Txsinanews_noid();
										noidbean.setAnn_date(date2);
										noidbean.setLink(t_urls);
										noidbean.setAnn_title(titles);
										noidbean.setRmk(rmk);
										listMap_noid.add(noidbean);
										
//										System.out.println("AA="+noidbean.toString());


									} else { // var strcode is number
										if(temp==0) {
											Txsinanews bean = new Txsinanews();
											bean.setAnn_date(date2);
											bean.setLink(t_urls);
											bean.setAnn_title(titles);
											bean.setRmk(rmk);
											
											listMap.add(bean);
											
//											System.out.println("BB="+bean.toString());
										}
										temp++;
										Txsinanews_Comp Compbean = new Txsinanews_Comp();
										Compbean.setAnn_date(date2);
										Compbean.setLink(t_urls);
										Compbean.setStk_id(a[q]);
										Compbean.setMkt(b[o]);
										listMap_comp.add(Compbean);
//										System.out.println("CC="+Compbean.toString());
									}
								}
								}
								} //b loop
							} catch (Exception e) {
								logger.error(ErrorTitle.CONTENT_TITLE.getTitle(), e);
							}

						} // has varcode

						else { // without str varcode
							try {
								///////////// TxsinanewsNoid table
								Txsinanews_noid noidbean = new Txsinanews_noid();
								noidbean.setAnn_date(date2);
								noidbean.setLink(t_urls);
								noidbean.setAnn_title(titles);
								noidbean.setRmk(rmk);
								listMap_noid.add(noidbean);
//								System.out.println("DD="+noidbean.toString());

								///////////// TxsinanewsNoid table end
							} catch (Exception e) {
								logger.error(ErrorTitle.CONTENT_TITLE.getTitle(), e);
							}

						}
					} // titles
					
					
					
				}
			}
		} catch (Exception e) {
			// 網頁轉換 或 其餘例外錯誤
			logger.error(ErrorTitle.ANALYSIS_TITLE.getTitle(), e);
		} finally {
			tableList.add(new Table(listMap.toArray(new Txsinanews[0]), "Txsinanews"));
			tableList.add(new Table(listMap_comp.toArray(new Txsinanews_Comp[0]), "Txsinanews_Comp"));
			tableList.add(new Table(listMap_noid.toArray(new Txsinanews_noid[0]), "Txsinanews_noid"));

		}
		// 此function例外錯誤由抽象類別(DownloadFrame)統一回傳
		return tableList.toArray(new Table[0]);
	}

	public Table[] parseListMap2(String source) {

		// 表格陣列
		List<Table> tableList = new ArrayList<>();

		// 宣告表格資料暫存陣列
		List<Txsinanews> listMap = new ArrayList<Txsinanews>();
		List<Txsinanews_Comp> listMap_comp = new ArrayList<Txsinanews_Comp>();
		List<Txsinanews_noid> listMap_noid = new ArrayList<Txsinanews_noid>();

		// 此處應只有網頁核心資料已截取過區塊
		// 外部檔案讀取可直接由此處讀入並寫入暫存

		try {
			// news page
			Document doc = Jsoup.parse(source);
			Elements links = doc.getElementsByClass("listBlk");
//			System.out.println(links);

			Elements urls = links.select("li");
			for (Element link : urls) {
				if(link.select("a").attr("href").contains("finpagefr")){

					if (link.text().contains("下一页"))
					//|| link.text().contains("公告提示") || link.text().contains("公告速递")
//						|| link.text().contains("公布") && link.text().contains("季报")
//						|| link.text().contains("公布") && link.text().contains("年报")) 
						{
					continue;
				} else {// titles//
					String t_urls = link.select("a").attr("href"); // article url
					String titles = link.select("a").text(); // article title
					String ann_date = link.select("span").text(); // article date
					

					System.out.println(titles);
					System.out.println(t_urls);
					// current date
					SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy");
					Date current = new Date();

					// ann_Date
					String str = ann_date;
					String[] date = str.split("");
					String annDate = sdFormat.format(current) + date[1] + date[2] + date[4] + date[5];
					String comp_id = "";
					String mkt="";

					// string => date ((2021-10-20))
					SimpleDateFormat sdFormat2 = new SimpleDateFormat("yyyyMMdd");
					Date datex = new Date();
					datex = sdFormat2.parse(annDate);
					java.sql.Date date2 = new java.sql.Date(datex.getTime());

					// article content
					Document doc2 = Jsoup.connect(t_urls).get();
					String doc2String = doc2.toString();
					Elements ListDiv = doc2.getElementsByAttributeValue("class", "article");
					String rmk = ListDiv.select("div.article > p").text();

					if (doc2String.contains("var strCode")) {
						// ((comp_id= code))
						Elements div11 = doc2.select("script[language=JavaScript]");
						// String comp_id = "";
//			  BigInteger Tej_comp_id;
						ArrayList<String> comp_strings = new ArrayList<String>();

						for (Element scriptElement : div11) {
							String val = scriptElement.data();
							if (val.contains("strCode")) {
								Pattern pattern = Pattern.compile("var strCode = \"[A-Za-z0-9,]+\"");
								Matcher matcher = pattern.matcher(val);
								if (matcher.find()) {
									String code = matcher.group();
									int i = code.indexOf("\"");
									code = code.substring(i, code.length()).replace("\"", "");
									String[] comp_code = code.split(",");
									for (int j = 0; j < comp_code.length; j++) {
										comp_strings.add(comp_code[j]);
										mkt = comp_strings.toString().replaceAll("[0-9\\s]", "").replace("[", "").replace("]",""); //留字母
										comp_id = comp_strings.toString().replaceAll("[a-zA-z\\s]", ""); //留數字
									}
								}
								break;
							}
						}
						String[] a = comp_id.split(",");
						String[] b = mkt.split(",");
						try {
							int temp=0;
							for (int q = 0; q < a.length; q++) {
								for(int o=0; o<b.length; o++){
									if(o==q){
										
										if(b[o].length()>3)
											continue;
								if (a[0] == "") { // var strcode is eng
									Txsinanews_noid noidbean = new Txsinanews_noid();
									noidbean.setAnn_date(date2);
									noidbean.setLink(t_urls);
									noidbean.setAnn_title(titles);
									noidbean.setRmk(rmk);
									listMap_noid.add(noidbean);
//									System.out.println("B="+noidbean.toString());
								} else { // var strcode is number
									if(temp==0) {
										Txsinanews bean = new Txsinanews();
										bean.setAnn_date(date2);
										bean.setLink(t_urls);
										bean.setAnn_title(titles);
										bean.setRmk(rmk);
										listMap.add(bean);
//										System.out.println("C="+bean.toString());

									}
									temp++;
									Txsinanews_Comp Compbean = new Txsinanews_Comp();
									Compbean.setAnn_date(date2);
									Compbean.setLink(t_urls);
									Compbean.setStk_id(a[q]);
									Compbean.setMkt(b[o]);
									listMap_comp.add(Compbean);
//									System.out.println("D="+Compbean.toString());
									
								}
							}
							}
							}
							
							


//							String tt = "(12345)";
//							String p = "(?<=\\()[^\\)]+";
//
//							Pattern P=Pattern.compile(p);
//						    Matcher matcher1=P.matcher(tt); 
//						    if(matcher1.find())
//						    {
//						    	System.out.println(matcher1.group(0).replaceAll(p, "$1"));
//						    }
							
							//////////////////////if title has stk 
					
							Txsinanews_Comp Compbean = new Txsinanews_Comp();
							if(titles.contains("(")){

								String t = titles.replaceAll("[\u4e00-\u9fa5_]",",");
								String p = "(?<=\\()[^\\)]+";
								
								Pattern P=Pattern.compile(p);
							    Matcher matcher1=P.matcher(t); 
							    if(matcher1.find())
							    {	
							    	String s = matcher1.group().replaceAll(p, "$1").toString();
//							    	System.out.println(matcher1.group().replaceAll(p, "$1").replace(".","#"));
							    	String stkID[] = s.replace(".","#").split("#");		
							    	
							    	if(s.matches("^[A-Z.]+$")){
//							    		System.out.println("s="+s);
							    		continue;
							    	}
							   
							    	//沒有MK進不了迴圈
							    	if(stkID.length>1){  							    	
									Compbean.setAnn_date(date2);
									Compbean.setLink(t_urls);	
									Compbean.setStk_id(stkID[0]);
									Compbean.setMkt(stkID[1]);
									listMap_comp.add(Compbean);
//									System.out.println("D2="+Compbean);    	
							    	}	
							    }
							} 
//							
							
							
						} catch (Exception e) {
							logger.error(ErrorTitle.CONTENT_TITLE.getTitle(), e);
						}

					} // has varcode

					else { // without str varcode
						try {
												
							//////////////////// without str varcode but title has stk
	
							if(titles.contains("(")){ 
								
								String t = titles.replaceAll("[\u4e00-\u9fa5_]",",");
								String p = "(?<=\\()[^\\)]+";
								
								Pattern P=Pattern.compile(p);
							    Matcher matcher1=P.matcher(t); 
							    if(matcher1.find())
							    {	
							    	String s = matcher1.group().replaceAll(p, "$1").toString();
						    	
							    	if(s.matches("^[A-Z.]+$")){ //title stk eng
							    		Txsinanews_noid noidbean = new Txsinanews_noid();
										noidbean.setAnn_date(date2);
										noidbean.setLink(t_urls);
										noidbean.setAnn_title(titles);
										noidbean.setRmk(rmk);
										listMap_noid.add(noidbean);
//										System.out.println("E="+noidbean.toString());
							    	} 
							    	
							    	else{ //title stk 
							    		
							    		Txsinanews bean = new Txsinanews();
										bean.setAnn_date(date2);
										bean.setLink(t_urls);
										bean.setAnn_title(titles);
										bean.setRmk(rmk);
										listMap.add(bean);
//										System.out.println("nostr_D="+bean.toString());
										
										Txsinanews_Comp Compbean = new Txsinanews_Comp();	
								    	String stkID[] = s.replace(".","#").split("#");	

										if(stkID.length>1){  							    	
											Compbean.setAnn_date(date2);
											Compbean.setLink(t_urls);	
											Compbean.setStk_id(stkID[0]);
											Compbean.setMkt(stkID[1]);
											listMap_comp.add(Compbean);
//											System.out.println("nostr_D="+Compbean);    	
									    	}
									    	else {
												Compbean.setAnn_date(date2);
												Compbean.setLink(t_urls);	
												Compbean.setStk_id(stkID[0]);
												Compbean.setMkt("hk");
												listMap_comp.add(Compbean);
//												System.out.println("nostr_D="+Compbean);  	
									    		
									    	}

							    	}
							    }
							} 						
							/////////////標題沒有 文章內容也沒有
							else{							
							///////////// TxsinanewsNoid table 
							Txsinanews_noid noidbean = new Txsinanews_noid();
							noidbean.setAnn_date(date2);
							noidbean.setLink(t_urls);
							noidbean.setAnn_title(titles);
							noidbean.setRmk(rmk);
							listMap_noid.add(noidbean);
//							System.out.println("E="+noidbean.toString());

							///////////// TxsinanewsNoid table end
							}
						} catch (Exception e) {
							logger.error(ErrorTitle.CONTENT_TITLE.getTitle(), e);
						}

					}
				} // titles
			}
			}
		} catch (Exception e) {
			// 網頁轉換 或 其餘例外錯誤
			logger.error(ErrorTitle.ANALYSIS_TITLE.getTitle(), e);
		} finally {
			tableList.add(new Table(listMap.toArray(new Txsinanews[0]), "Txsinanews"));
			tableList.add(new Table(listMap_comp.toArray(new Txsinanews_Comp[0]), "Txsinanews_Comp"));
			tableList.add(new Table(listMap_noid.toArray(new Txsinanews_noid[0]), "Txsinanews_noid"));

		}
		// 此function例外錯誤由抽象類別(DownloadFrame)統一回傳
		return tableList.toArray(new Table[0]);
	}
	/**
	 * 檢查日期格式
	 * 
	 * @param d
	 * @return
	 * @throws ParseException
	 */
	private java.sql.Date checkDate(String d) throws ParseException {
		if (d.trim().equals(""))
			return null;
		else
			return new java.sql.Date(sdf.parse(d).getTime());
	}

	/**
	 * BigDecimal 格式檢查
	 * 
	 * @param d
	 * @return
	 */
	private BigDecimal checkBigDecimal(String d) {
		d = d.replace(",", "").trim().replaceAll("[A-Za-z]", "");
		if (d.replace("-", "").trim().equals(""))
			return null;
		else
			return new BigDecimal(d);
	}
}
