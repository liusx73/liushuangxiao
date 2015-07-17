package test;

import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class TestNews
{
	
	/**
	 * @param args
	 */
	public static void main(String[] args)	{
		Parser parser2;
		try {
			
			parser2 = new Parser("http://item.taobao.com/item.htm?spm=a230r.1.14.39.OxPst8&id=520449964097&ns=1&abbucket=16#detail");
			parser2.setEncoding("gbk");
			Date date =new Date();
//			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			String time=df.format(date);   //当前时间
			try {
				URLConnection c = parser2.getConnection();
				c.connect();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			Node node=parser2.extractAllNodesThatMatch(new HasAttributeFilter("class", "cnblogs_code")).elementAt(0);
			NodeIterator node = parser2.elements();
			
//			NodeList nodeList=node.getChildren().extractAllNodesThatMatch(new TagNameFilter("span"), true);
////			System.out.println(nodeList);
//			for(int i=0;i<nodeList.size();i++){
//				TagNode tagNode=(TagNode)nodeList.elementAt(i);
//				NodeList href=tagNode.getChildren();
//				System.out.println(href.toHtml());
//			}
			NodeList ns1 = parser2.extractAllNodesThatMatch(new TagNameFilter("h3"));
			NodeList ns2 = ns1.extractAllNodesThatMatch(new HasAttributeFilter("class", "tb-main-title"));
			for (int i = 0; i < ns2.size(); i++) {
				System.out.println(((TagNode)ns2.elementAt(i)).toHtml());
			}
			NodeList ns3 = parser2.extractAllNodesThatMatch(new TagNameFilter("h3"));
			System.out.println(ns3.size());
			NodeList ns4 = ns3.extractAllNodesThatMatch(new HasAttributeFilter("class", "tb-rmb-num"));
			
			for (int i = 0; i < ns4.size(); i++) {
				System.out.println(((TagNode)ns4.elementAt(i)).toHtml());
			}
			
//			while (node.hasMoreNodes()) {
//				System.out.println(node.nextNode().toHtml());
//			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
	
	
	
}
