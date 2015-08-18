package com.nktin.protocol.tailored;

import java.util.Map;

public class ParseAllXMLs {
	
	String[] xmls = new String[] {
			"BEMessageSpec_Client",
			"BOCTreasuryAuctionMessageSpec_Client",
			"CAMessageSpec_Client",
			"CHMessageSpec_Client",
			"DEMessageSpec_Client",
			"EUMessageSpec_Client",
			"FitchRatingsMessageSpec_Client",
			"FRMessageSpec_Client",
			"SystemMessageSpec_Client",
			"TreasuryAuctionMessageSpec_Client",
			"UKMessageSpec_Client",
			"UKTreasuryAuctionMessageSpec_Client",
			"USMessageSpec_Client",
	};

	public ParseAllXMLs() {
		for(String xml : xmls) {
			new XMLParserTest(xml);
		}
		map = XMLParserTest.getMap();
	}
	
	Map<Integer,NewsEvent> map = null;
	
	public Map<Integer,NewsEvent> getMap() {
		return map;
	}

	public static void main(String[] args) {
		new ParseAllXMLs();
	}

}
