package com.db.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;

public class NewTest {
	
	public static Connection con;
	public static Statement st;
	public static WebDriver driver;
	
	public static ArrayList<String> final_values;
	
	
  @Test(dataProvider = "dp")
  public void f(String email, String pwd) throws SQLException {
	  
	  System.out.println(email);
	  System.out.println(pwd);
	  
	  driver.get("https://dev.competitrack.com/home/#/login/");
	  
	  driver.findElement(By.id("helloEmail")).sendKeys(email);
	  
	  driver.findElement(By.className("btn-left-side")).click();
	  
	  driver.findElement(By.id("password")).sendKeys(pwd);
	  
	  driver.findElement(By.className("btn-left-side")).click();
	  
	  driver.findElement(By.xpath("//button[@class='btn btn-default btn-toolbar btn-block dropdown-toggle btn-icon btn-account-chooser']")).click();
	  
	  textVerifier();
	  
	  List <WebElement> allelems = driver.findElements(By.xpath("//span[@ng-bind-html]"));
	  
	  for (WebElement element : allelems) {
		  
		  String text = element.getText();
		  textValidator(text);
	  }

	  
	  
	  
  }
  

  @DataProvider
  public Object[][] dp() throws SQLException {

	  st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	  
	  ResultSet rs = st.executeQuery("select * from test_num1");
	  
	  Object ob[][] = new Object[1][2];
	  
	  while (rs.next()) {
		  
		  ob[0][0]= rs.getString(1);
		  ob[0][1]= rs.getString(2);
		  
		  System.out.println(ob[0][0]);
		  System.out.println(ob[0][1]);
		  
	  }
	  return ob;
	  
  }
  

  public static void  textVerifier() throws SQLException {

	  st = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
	  
	  ResultSet rs = st.executeQuery("select * from data_validator");
	  
	  List<String> sample = new ArrayList<String>();
	  
	  while (rs.next()) {
		  
		  sample.add(rs.getString(1));
	  }
	  
	  System.out.println(sample);
	  final_values = (ArrayList<String>) sample;
	  
	  
  }
  
  public void textValidator (String text) {
	  
	  for (int i = 0;i<final_values.size();i++) {
		  String te = final_values.get(i);
		  

			  SoftAssert sa = new SoftAssert();
			  sa.assertEquals(text, te, "Not matched");
		  
	  }
	  
  }
  @BeforeTest
  public void beforeTest() throws IOException, ClassNotFoundException, SQLException {
	  
	  System.setProperty("web.chrome.driver", "C:\\Users\\sahoo\\Downloads\\chromedriver_win32\\chromedriver.exe");
	  
	  driver = new ChromeDriver();
	  
	  driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	  
	  
	  FileInputStream fis = new FileInputStream ("db.properties");
	  
	  Properties pis = new Properties();
	  
	  pis.load (fis);
	  
	  String className = pis.getProperty("className");
	  String url = pis.getProperty("url");
	  String userName = pis.getProperty("userName");
	  String pwd = pis.getProperty("password");
	  
	  Class.forName(className);
	  
	  con= DriverManager.getConnection(url,userName,pwd);
	  
	  
	  
	  
  }

  @AfterTest
  public void afterTest() throws SQLException {
	  
	  con.close();
	  st.close();
  }

}
