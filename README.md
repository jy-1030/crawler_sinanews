
# 新浪網的中國新聞以及香港新聞爬蟲
> ch_news_url = http://finance.sina.com.cn/roll/index.d.html?cid=56592        
> hk_news_url = https://finance.sina.com.cn/roll/index.d.html?cid=57038      



![image](https://user-images.githubusercontent.com/77848848/167262291-78e540d8-7eb6-42fa-affb-dd1d80d9c4e1.png)

> - 標題的文字>> ANN_TITLE  日期>>ANN_DATE

![image](https://user-images.githubusercontent.com/77848848/167262349-3b269c60-4556-43bd-89d4-ad6e8691ab1a.png)


> - 內文 >> RMK

![image](https://user-images.githubusercontent.com/77848848/167262308-857587d1-7369-4f84-a038-69260dbd9e67.png)


> - 小規則


![image](https://user-images.githubusercontent.com/77848848/167262314-c8f09679-e22e-440f-ad8c-19b3d097a817.png)

1.根據標題，若標題含有stk_id 則新聞抓進 Table_txsinanews  stk_id 抓進 Table_txsinanews_comp       
2.再根據網頁原始碼的內容                   
3.若有其它的 str_varcode就要再抓進 Table = txsinanews_comp       
4.若標題無stk_id 則檢查內文是否有，若有則新聞抓進 Table_txsinanews  stk_id 抓進 Table_txsinanews_comp        
5.若標題無，內文也無，或是stk_id為英文(表外國新聞)，則抓進Table_txsinanews_noid        

