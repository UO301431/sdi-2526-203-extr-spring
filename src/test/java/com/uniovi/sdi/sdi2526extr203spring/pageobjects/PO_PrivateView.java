package com.uniovi.sdi.sdi2526extr203spring.pageobjects;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public class PO_PrivateView extends PO_NavView {
    static public void findAndClick(WebDriver driver, String type, String XPath, int position) {
        List<WebElement> elements = PO_View.checkElementBy(driver, type, XPath);
        elements.get(position).click();
    }

    static public void checkPagination(WebDriver driver) {
        List<WebElement> page2 = driver.findElements(By.xpath("//*[@id=\"main-container\"]/div[3]/ul/li[3]/a"));
        if (!page2.isEmpty()) {
            page2.get(0).click();
            Assertions.assertTrue(driver.getCurrentUrl().contains("page="), "La URL debe mantener el parámetro de paginación");
        } else {
            System.out.println("Aviso: No hay suficientes datos para probar la paginación física (menos de 5 reservas).");
        }
    }
}
