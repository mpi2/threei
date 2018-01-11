package org.immunophenotype.web.services;

import java.util.HashMap;
import java.util.Map;

public class HeatmapRow {
	//1 not enough data yet
	//2 not significantly different from WT calls
	//3 high sig
	//4 no data call it 0 before display as ranks senisbly then
	private String gene;
	public Map<String, Integer> getProcedureSignificance() {
		return procedureSignificance;
	}

	public void setProcedureSignificance(Map<String, Integer> procedureSignificance) {
		this.procedureSignificance = procedureSignificance;
	}

	public String getGene() {
		return gene;
	}

	public String getConstruct() {
		return construct;
	}

	private String construct;
	Map<String,Integer> procedureSignificance=new HashMap<>();//map of procedure name to significance string.
	
	public HeatmapRow(String gene, String construct){
		this.gene=gene;
		this.construct=construct;
	}

	@Override
	public String toString() {
		return "HeatmapRow [gene=" + gene + ", construct=" + construct + ", procedureSignificance="
				+ procedureSignificance + "]";
	}
	
	

}
