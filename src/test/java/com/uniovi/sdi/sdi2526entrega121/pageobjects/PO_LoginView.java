package com.uniovi.sdi.sdi2526entrega121.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PO_LoginView extends PO_NavView{
    static public void fillLoginForm(WebDriver driver, String dnip, String passwordp) {
        // Rellenar DNI/Username
        WebElement dni = driver.findElement(By.name("username"));
        dni.click();
        dni.clear();
        dni.sendKeys(dnip);

        // Rellenar Password
        WebElement password = driver.findElement(By.name("password"));
        password.click();
        password.clear();
        password.sendKeys(passwordp);

        // Pulsar el botón de login (usando el tipo submit para mayor precisión)
        By boton = By.cssSelector("button[type='submit']");
        driver.findElement(boton).click();
    }

    static public List<WebElement> login (WebDriver driver, String dnip, String passwordp, String checkText) {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, dnip, passwordp);
        List<WebElement> elements =  PO_View.checkElementBy(driver, "text", checkText);
        return elements;
    }

}
