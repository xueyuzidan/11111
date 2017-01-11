package com.qunar.EffectiveLines;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/****************ͳ��һ��java�ļ�����Ч����*****************************/
public class EffectiveLines {
	static int cntCode=0;   //������
	static int cntNode=0;  //ע����
	static int cntSpace=0;  //�հ���
	static boolean flagNode=false; //����ע�ͱ�ʶ��
	
	public static void main(String[] args)
	{
		try {
			BufferedReader br = null;   //com.qunar.EffectiveLines.EffectiveLines.java
			br = new BufferedReader(new FileReader("src/com/qunar/EffectiveLines/EffectiveLines.java"));
			String line=null;
			while((line = br.readLine())!=null)
			{
				pattern(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("�����У�"+cntCode);

	}
	private static void pattern(String line){	
		String regNodeBegin="\\s*/\\*.*";   //����ע�Ϳ�ʼ/*
		String regNodeEnd=".*\\*/\\s*";     //����ע�ͽ�β */
		String regx="\\s*//.*";             //����ע��
		String regSpace="\\s*";             //�ո��������ʽ
		//ע����
		if(line.matches(regNodeBegin) && line.matches(regNodeEnd)){
			++cntNode;
			return ;
		}
		if(line.matches(regNodeBegin)){//����ע��
			++cntNode;
			flagNode = true;
		}else if(line.matches(regNodeEnd)){
			++cntNode;
			flagNode = false;
		}else if(line.matches(regSpace)){//����
			++cntSpace;
		}else if(line.matches(regx)){//����ע��
			++cntNode;
		}else if(flagNode){
			++cntNode;
		}else{
			++cntCode;
		}
	}
}