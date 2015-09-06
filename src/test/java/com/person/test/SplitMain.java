package com.person.test;

import org.apache.commons.lang3.StringUtils;

import com.person.exception.InParticularCases;

public class SplitMain {

	public static void main(String[] args) {
		String strs = "房旭东,,,ID, 511702199012161876,M,19820720,\"\"\",,\"\"日照市五莲县许孟镇东楼子村\",待定,F,ASI,CHN,11,110101,,,待定,待定,待定,13828830628,待定,待定,待定,待定,待定,待定,,,,,0,2012-7-26 11:21:14,16049340";
		boolean t = StringUtils.contains(strs, InParticularCases.MultipleQuotes);
		System.out.println(t);
		strs = StringUtils.replace(strs, InParticularCases.MultipleQuotes, "");
		System.out.println(strs);
	}
}
