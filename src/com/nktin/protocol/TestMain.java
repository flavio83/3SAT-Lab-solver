package com.nktin.protocol;

import com.nktin.protocol.Protocol.Category.Messages.Message;
import com.ntkn.messages.IndicatorMessage;

public class TestMain {

	public TestMain() {
		ObjectFactory objFactory = new ObjectFactory();
		Message msg = objFactory.createProtocolCategoryMessagesMessage();
		IndicatorMessage indicatorMessage = new IndicatorMessage(null,null);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
