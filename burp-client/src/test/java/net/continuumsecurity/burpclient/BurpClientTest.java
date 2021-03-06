package net.continuumsecurity.burpclient;

import net.continuumsecurity.restyburp.Settings;
import net.continuumsecurity.restyburp.model.HttpMessage;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BurpClientTest {
	   static Logger log = Logger.getLogger(BurpClientTest.class.toString());
	    HtmlUnitDriver driver;
	    static BurpClient burp;
	    Settings settings;
	    final String target = "http://localhost:9110/ropeytasks/";
	    int proxyPort = 8080;

	    @BeforeClass
	    public static void setUpClass() throws Exception {
	        burp = new BurpClient("http://127.0.0.1:8181/","localhost",8086);
	    }

	    @AfterClass
	    public static void tearDownClass() throws Exception {
	    	log.debug("Test case done.");
	    }

	    @Before
	    public void setUp() {
	        driver = new HtmlUnitDriver();
	        driver.setProxy("127.0.0.1", proxyPort);
	        try {
				burp.reset();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

        @Test
        public void testMakeRequest() throws UnsupportedEncodingException {
            driver.get(target+"user/login");
            HttpMessage message = burp.getProxyHistory().get(0);
            assertNotNull(message.getResponse());
            HttpMessage manual = burp.makeRequest("localhost",9110,false,message.getRequest());
            String orig = message.getResponseBody();
            String manualBody = manual.getResponseBody();
            assertEquals(orig, manualBody);
        }
	    

	    /*@Test
	    public void testFindInResponse() {
	    	String regex = ".*<input.*?type.*?=.*?pasword.*";
	    	//driver.get(target);
	    	driver.get(target+"user/login");
	    	HttpRequestResponseBean result = burp.findInResponseHistory(regex);
	    	if (result == null) {
	    		log.debug("no result found");
	    	} else {
	    		log.debug("found regex in "+result.getUrl()+" response:"+result.getResponse());
	    	}
	    	String response = new String(result.getResponse());
	    	assert (response.contains(regex));
	    	log.debug("Response="+response);
	    }*/
	    

}
