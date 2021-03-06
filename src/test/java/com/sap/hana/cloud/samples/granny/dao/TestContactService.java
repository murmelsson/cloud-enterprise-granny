package com.sap.hana.cloud.samples.granny.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sap.hana.cloud.samples.granny.model.Contact;
import com.sap.hana.cloud.samples.granny.model.StatusMessage;
import com.sap.hana.cloud.samples.granny.model.ValidationError;
import com.sap.hana.cloud.samples.granny.srv.ContactService;
import com.sap.hana.cloud.samples.granny.srv.DataValidationException;

/**
 * Tests for the {@link ContactService} class.  
 */
@ContextConfiguration("classpath:/META-INF/spring/app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(profiles = "test")
public class TestContactService
{
	@Autowired
	ContactService contactSrv = null;
	
	@Before
	public void setUp() throws Exception 
	{
		// nothing needs to be done here
	}

	@After
	public void tearDown() throws Exception 
	{
		// nothing needs to be done here
	}
	
	/**
	 * Tests the input validation when creating/updating objects.
	 * 
	 * TODO - remove data validation from DAO layer and implement it on service layer!
	 */
	@Test
	@Transactional
	public void testDataValidation()
	{
	
		Contact contact = new Contact();
		contact.setId(null);
		
		// try to create
		try
		{
			contact = contactSrv.createContact(contact);
			fail("Should not be possible to save entities withour a proper ID!");
		}
		catch (DataValidationException ex)
		{
			StatusMessage msg = ex.getStatusMessage();
			
			List<ValidationError> errors = msg.getErrors();
			
			if (errors != null && errors.size() > 0)
			{
				ValidationError error = errors.get(0);
				assertTrue("Should not be possible to save entities withour a proper ID!", ("model.object.id.not_null.error".equals(error.getMessageKey())));
			}	
		}
		catch (Exception ex)
		{
			assertTrue("RootCause should be DataValidationException!", (ex instanceof DataValidationException));
		}
		
		catch (NoSuchMethodError er)
		{
			// TDOD issues with javax-el not present during Maven build or jUnit tests
			er.printStackTrace();
		}
		catch (java.lang.NoClassDefFoundError er)
		{
			// TODO issues with hibernate-validator not present during Maven build or jUnit tests
			er.printStackTrace();
		}
	
	}
}
