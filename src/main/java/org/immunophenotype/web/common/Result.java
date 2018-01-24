package org.immunophenotype.web.common;

public class Result {
		
		private SexType sexType;
		private ZygosityType zygosityType;
		private SignificanceType significant;

	    
		public SignificanceType getSignificant() {
			return significant;
		}

		public void setSignificant(SignificanceType significant) {
			this.significant = significant;
		}

		public SexType getSexType() {
			return sexType;
		}

		public void setSexType(SexType sexType) {
			this.sexType = sexType;
		}

		public ZygosityType getZygosityType() {
			return zygosityType;
		}

		public void setZygosityType(ZygosityType zygosityType) {
			this.zygosityType = zygosityType;
		}

	

}
