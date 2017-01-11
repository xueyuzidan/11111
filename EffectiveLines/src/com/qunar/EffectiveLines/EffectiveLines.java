package com.qunar.EffectiveLines;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/****************统计一个java文件的有效行数*****************************/
public class EffectiveLines {
	static int cntCode=0;   //代码行
	static int cntNode=0;  //注释行
	static int cntSpace=0;  //空白行
	static boolean flagNode=false; //多行注释标识符
	
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

		System.out.println("代码行："+cntCode);

	}
	private static void pattern(String line){	
		String regNodeBegin="\\s*/\\*.*";   //多行注释开始/*
		String regNodeEnd=".*\\*/\\s*";     //多行注释结尾 */
		String regx="\\s*//.*";             //单行注释
		String regSpace="\\s*";             //空格的正则表达式
		//注释行
		if(line.matches(regNodeBegin) && line.matches(regNodeEnd)){
			++cntNode;
			return ;
		}
		if(line.matches(regNodeBegin)){//多行注释
			++cntNode;
			flagNode = true;
		}else if(line.matches(regNodeEnd)){
			++cntNode;
			flagNode = false;
		}else if(line.matches(regSpace)){//空行
			++cntSpace;
		}else if(line.matches(regx)){//单行注释
			++cntNode;
		}else if(flagNode){
			++cntNode;
		}else{
			++cntCode;
		}
	}
}