/**
 * 
 */
package com.k99k.khunter;

/**
 * imei验证器,仅作示例用
 * @author keel
 *
 */
public class StringValidator implements KObjColumnValidate {

	public StringValidator() {
	}
	
	
	private KObjColumnValidate validator;
	
	private int small;
	
	private int big;

	@Override
	public void initType(int type, String[] paras) {
		if (type == 0) {
			//长度大于某值
			this.validator = new BigThanValidator();
			this.big = Integer.parseInt(paras[0]);
		}else if(type == 1){
			this.validator = new SmallThanValidator();
			this.small = Integer.parseInt(paras[0]);
		}else if(type == 2){
			this.validator = new IntervalValidator();
			this.small = Integer.parseInt(paras[0]);
			this.big = Integer.parseInt(paras[1]);
		}
		
	}

	class BigThanValidator implements KObjColumnValidate{
		@Override
		public boolean validate(Object value) {
			if (value.toString().length() < StringValidator.this.big) {
				return false;
			}
			return true;
		}

		@Override
		public void initType(int type, String[] paras) {
			
		}
	}
	class SmallThanValidator implements KObjColumnValidate{
		@Override
		public boolean validate(Object value) {
			if (value.toString().length() < StringValidator.this.small) {
				return false;
			}
			return true;
		}

		@Override
		public void initType(int type, String[] paras) {
		}
	}
	class IntervalValidator implements KObjColumnValidate{
		@Override
		public boolean validate(Object value) {
			int len = value.toString().length();
			if (len > small && len < big) {
				return true;
			}
			return false;
		}

		@Override
		public void initType(int type, String[] paras) {
		}
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.KObjColumnValidate#validate(java.lang.Object)
	 */
	@Override
	public boolean validate(Object value) {
		return this.validator.validate(value);
	}




	

}
