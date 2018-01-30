package org.immunophenotype.web.services;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppServiceTestConfig.class})
public class HeatmapServiceTest {

	@Autowired
	DataSource komp2DataSource;
	
	HeatmapService heatmapService;
	
	@Before
	public void setUp() throws Exception {
		this.heatmapService=new HeatmapService((org.apache.tomcat.jdbc.pool.DataSource) komp2DataSource);
	}

	@Test
	public final void testGetHeatmapRows() {
		List<HeatmapRow> rows = null;
		try {
			rows = heatmapService.getHeatmapRows();
			List<String> headers = heatmapService.getDistinctProcedureNames(rows);
			System.out.println("{");
			for (String header : headers) {

				System.out.println("\""+header + "\",");

			}
			System.out.println("}");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(rows.size() > 100);
		assertTrue(HeatmapService.getHeaderorder().get(0).equalsIgnoreCase("Viability Primary Screen"));
	}

}
