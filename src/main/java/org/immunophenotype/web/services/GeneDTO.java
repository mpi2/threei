package org.immunophenotype.web.services;

import org.apache.solr.client.solrj.beans.Field;

public class GeneDTO {

	
	public static final String MGI_ACCESSION_ID = "mgi_accession_id";

	public static final String MARKER_SYMBOL = "marker_symbol";
	
	@Field(MARKER_SYMBOL)
	private String markerSymbol;
	
	@Field(MGI_ACCESSION_ID)
	private String accession;

	public String getMarkerSymbol() {
		return markerSymbol;
	}

	public void setMarkerSymbol(String markerSymbol) {
		this.markerSymbol = markerSymbol;
	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public static String getMgiAccessionId() {
		return MGI_ACCESSION_ID;
	}
}
