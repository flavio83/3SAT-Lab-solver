package com.abc;




public class Clause {
	
	
	private String first = null;
	private boolean firstNegate = false;
	
	private String second = null;
	private boolean secondNegate = false;
	
	private String third = null;
	private boolean thirdNegate = false;
	
	
	public Clause(String first, boolean firstNegate, 
			String second, boolean secondNegate, 
			String third, boolean thirdNegate) {
		super();
		this.first = first;
		this.firstNegate = firstNegate;
		this.second = second;
		this.secondNegate = secondNegate;
		this.third = third;
		this.thirdNegate = thirdNegate;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public boolean isFirstNegate() {
		return firstNegate;
	}

	public void setFirstNegate(boolean firstNegate) {
		this.firstNegate = firstNegate;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public boolean isSecondNegate() {
		return secondNegate;
	}

	public void setSecondNegate(boolean secondNegate) {
		this.secondNegate = secondNegate;
	}

	public String getThird() {
		return third;
	}

	public void setThird(String third) {
		this.third = third;
	}

	public boolean isThirdNegate() {
		return thirdNegate;
	}

	public void setThirdNegate(boolean thirdNegate) {
		this.thirdNegate = thirdNegate;
	}

	
}
