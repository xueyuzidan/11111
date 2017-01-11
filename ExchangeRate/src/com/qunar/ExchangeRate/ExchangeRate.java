package com.qunar.ExchangeRate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.htmlparser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


class ExchangeRate {
	 static Double dol = 0.0;
	 static Double eng = 0.0;
	 static Double hon = 0.0;
	
	/**************************getPage ��ȡ��Ŀ����ҳ**********************/
	public static String getPage(String url) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = new WebClient();
		//htmlunit ��css��javascript��֧�ֲ��ã�������ر�
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		//��ȡҳ��
		HtmlPage page = webClient.getPage(url);
		//��xml����ʽ��ȡ��Ӧ�ı�
		String pageXml = page.asXml();  
		return pageXml;
		
	}
	/***************************jsoupToPage �����й��������л�����ҳ**********************/
	private static List<String> jsoupToPage(String pageXml) {
		List<String> list = new ArrayList<String>();
		Document doc = Jsoup.parse(pageXml);
    	System.out.println("********jsoup******");
        Element content = doc.getElementById("r_con");	
        Elements links = content.getElementsByTag("a");
        for(int i = 0;i<20;i++){
        	Element link = links.get(i);
        	String linkHref = link.attr("href");
        	String linkText = link.text();
        	System.out.println(linkHref);
        	list.add("http://www.pbc.gov.cn"+linkHref);
        }
        return list;      
	}
	/***************************exchangeRate �õ�����**********************/
	private static void exchangeRate(String str) {
		String[] temparray = str.split("��"); //��������,�ָ�
        String doltemp = temparray[1];
        String date = doltemp.substring(0, 10); //����
        doltemp = doltemp.split("��")[1];  //����Ԫ�Ļ���
        String engtemp = temparray[2];    //��ŷԪ�Ļ���
        String hontemp = temparray[4];    //�Ը۱ҵĻ���
        dol = dol
        		+ Double.parseDouble(doltemp.substring(7,
                        doltemp.length() - 1));
        eng = eng
                + Double.parseDouble(engtemp.substring(7,
                        engtemp.length() - 1));
       hon = hon
                + Double.parseDouble(hontemp.substring(7,
                        hontemp.length() - 1));
       
       
	}
	/***************************average �õ�����ƽ��ֵ**********************/
	private static void average() {
		DecimalFormat df = new DecimalFormat("######0.00");  //�����ֽ��и�ʽ��
		   dol = dol / 20;
		   eng = eng / 20;
		   hon = hon / 20;
		   System.out.println(df.format(dol));   //�ֱ�õ�ƽ��ֵ
		   System.out.println(df.format(eng));
		   System.out.println(df.format(hon));
	}
	
	/***************************jsoupToChildPage �����й���������20���ڶ������**********************/
	private static void jsoupToChildPage(List<String> list, int i)
			throws MalformedURLException, IOException {
		String xml = getPage(list.get(i));
		Document doc = Jsoup.parse(xml);
		Element content = doc.getElementById("zoom");	
		Elements links = content.getElementsByTag("p");	
		String str = links.text();   //�õ�p��ǩ�е��ı����� 
		System.out.println(str);
		exchangeRate(str);
		
	}
	/***************************excel ��¼����һ�������**********************/
	public static void excel(){
		WritableWorkbook wwb = null;  //�Ͳ���excel���
        OutputStream os = null;      //�����
        DecimalFormat df = new DecimalFormat("######0.00");  //�����ֽ��и�ʽ��
        try {
            String[] title = { "1��Ԫ�������", "1ŷԪԪ�������", "1�۱Ҷ������" };
            String filePath = "F:\\qunar1.xls";  //excel�ļ�����ĵ�ַ
            File file = new File(filePath);  
                file.createNewFile();
            os = new FileOutputStream(filePath);
            wwb = Workbook.createWorkbook(os);             //����һ��������
            WritableSheet sheet = wwb.createSheet("sheet1", 0); //����һ��������
            Label label = new Label(0,0,"30����RMB�����м��");   //Ϊ���������һ����ǩ
            sheet.addCell(label);      
            for (int i = 0; i < title.length; i++) {
                label = new Label(1 , i , title[i]);
                sheet.addCell(label);
            }
            label = new Label ( 2, 0 , df.format(dol));
            sheet.addCell(label);
            label = new Label ( 2, 1 , df.format(eng));
            sheet.addCell(label);
            label = new Label ( 2 , 2 , df.format(hon));
            sheet.addCell(label);
            wwb.write();
        } catch(FileNotFoundException e){
            System.out.println("�ļ�û�ҵ�");
        }  catch (WriteException e) {
            System.out.println("�����쳣");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        } finally{
            if(wwb != null)
                try {
                    wwb.close();
                } catch (WriteException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(os != null)
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
	
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String url = "http://www.pbc.gov.cn/zhengcehuobisi/125207/125217/125925/index.html";
		String pageXml = getPage(url);
		//ʹ��jsoup����.xml�ĵ������Ի�ȡ����Ҫ������
		List<String> list = new ArrayList<String>();
		list = jsoupToPage(pageXml);  
		
        for(int i = 0;i<list.size();i++) {
        	jsoupToChildPage(list, i);
        } 
        average();
        excel();    
    }
}


