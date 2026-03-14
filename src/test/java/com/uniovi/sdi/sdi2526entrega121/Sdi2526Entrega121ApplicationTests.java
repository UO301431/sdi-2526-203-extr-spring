package com.uniovi.sdi.sdi2526entrega121;

import com.uniovi.sdi.sdi2526entrega121.pageobjects.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2526Entrega121ApplicationTests {

    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    static String Geckodriver = "C:\\geckodriver.exe";

    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String URL = "http://localhost:8090";

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp(){
        driver.navigate().to(URL);
    }

    @AfterEach
    public void tearDown(){
        driver.manage().deleteAllCookies();
    }

    @BeforeAll
    static public void begin() {}

    @AfterAll
    static public void end() {
        if(driver != null) {
        driver.quit();
        }
    }

    @Test
    @Order(1)
    void PRO1(){
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678K","Josef", "Roces", "@Dm1n1str@D0r","@Dm1n1str@D0r");

        String checkText = "";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(2)
    void PR02(){
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678Z","Josef", "Roces", "@Dm1n1str@D0r","@Dm1n1str@");

        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.passwordConfirm.coincidence",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.signup.passwordConfirm.coincidence",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());

    }

    @Test
    @Order(3)
    void PR03(){
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678Z","Josef", "Roces", "@Dm1n1str@D0r","@Dm1n1str@D0r");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.dni.duplicate",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.signup.dni.duplicate",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());

    }

    @Test
    @Order(4)
    void PR04A(){ //Sin simbolos especiales
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678Z","Josef", "Roces", "Dm1n1strD0r","Dm1n1strD0r");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.dni.duplicate",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.signup.password.valid",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());


    }

    @Test
    @Order(5)
    void PR04B(){ //Sin numeros
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678Z","Josef", "Roces", "@Dmnstr@Dr","@Dmnstr@Dr");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.dni.duplicate",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.signup.password.valid",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(6)
    void PR05() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "12345678Z", "@Dm1n1str@D0r");
        String checkText = "";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(7)
    void PR06() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "12345678K", "@Dm1n1str@D0r");
        String checkText = "";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(8)
    void PR07() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "123", "@Dm1n1str@D0r");
        String checkText = "";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }
    @Test
    @Order(9)
    void PR08() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "12345678Z", "@Dm1n");
        String checkText = "";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }



}