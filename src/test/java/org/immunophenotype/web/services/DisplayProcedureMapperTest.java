package org.immunophenotype.web.services;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.immunophenotype.web.common.DisplayProcedureMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {AppServiceTestConfig.class})
public class DisplayProcedureMapperTest {

	
	
	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public final void testGetDisplayNameForProcedure() {
		System.out.println(DisplayProcedureMapper.getDisplayNameForProcedure("Viability Primary Screen"));
		assertTrue("Homozygous viability at P14".equals(DisplayProcedureMapper.getDisplayNameForProcedure("Viability Primary Screen")) );
		
	}

	@Test
	public final void testGetDisplayHeaderOrder(){
		String[] order=DisplayProcedureMapper.getDisplayHeaderOrder();
		for(String name: order){
			System.out.println(name);
		}
		
	}
	
	@Test
	public final void testGetProceduresFromDisplayName(){
		List<String> procedures=DisplayProcedureMapper.getProceduresFromDisplayName("Homozygous viability at P14");
		for(String procedure: procedures){	
			System.out.println(procedure);
		}
		assertTrue(procedures.size()>0);
	}
	
	@Test
	public final void testGetProceduresFromDisplayNameSalmonella(){
		List<String> procedures=DisplayProcedureMapper.getProceduresFromDisplayName("Salmonella Challenge");
		for(String procedure: procedures){	
			System.out.println(procedure);
		}
		assertTrue(procedures.size()==2);
	}
}
