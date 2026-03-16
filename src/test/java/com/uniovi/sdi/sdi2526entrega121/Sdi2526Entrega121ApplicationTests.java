package com.uniovi.sdi.sdi2526entrega121;

import com.uniovi.sdi.sdi2526entrega121.pageobjects.PO_HomeView;
import com.uniovi.sdi.sdi2526entrega121.pageobjects.PO_SignUpView;
import com.uniovi.sdi.sdi2526entrega121.pageobjects.PO_View;
import com.uniovi.sdi.sdi2526entrega121.pageobjects.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2526Entrega121ApplicationTests {

    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    //static String Geckodriver = "C:\\geckodriver.exe";

    static WebDriver driver = getDriver(PathFirefox/*, Geckodriver*/);
    static String URL = "http://localhost:8090";

    public static WebDriver getDriver(String PathFirefox/*, String Geckodriver*/) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        //System.setProperty("webdriver.gecko.driver", Geckodriver);
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


    // ── Helpers ───────────────────────────────────────────────────────────────

    private void loginAsAdmin() {
        driver.navigate().to(URL + "/login");
        driver.findElement(By.id("username")).sendKeys("12345678Z");
        driver.findElement(By.id("password")).sendKeys("@Dm1n1str@D0r");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private String bodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    private void comprobarPaginacion() {
        // Busca el enlace a la página 2 y hace clic si existe
        List<WebElement> page2 = driver.findElements(By.xpath("//a[contains(@href, 'page=2')]"));
        if (!page2.isEmpty()) {
            page2.get(0).click();
            assertTrue(driver.getCurrentUrl().contains("page="), "La URL debe mantener la paginación");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 4 – Registrar nuevos espacios
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 11] Registrar un nuevo espacio con datos válidos
    @Test
    @Order(11)
    public void prueba11_registrarEspacioValido() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/new");

        driver.findElement(By.id("name")).sendKeys("SalaSelenium01");
        new Select(driver.findElement(By.id("type"))).selectByValue("SALA");
        driver.findElement(By.id("location")).sendKeys("Planta 2 Test");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("8");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        System.out.println("URL actual: " + driver.getCurrentUrl());
        System.out.println("Cuerpo: " + bodyText());

        assertTrue(
                driver.getCurrentUrl().contains("/spaces/list")
                        || bodyText().contains("SalaSelenium01"),
                "Tras crear un espacio válido debe redirigir al listado o mostrar el espacio"
        );
    }

    // [Prueba 12] Registrar un nuevo espacio con nombre vacío (debe fallar)
    @Test
    @Order(12)
    public void prueba12_registrarEspacioNombreVacio() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/new");

        new Select(driver.findElement(By.id("type"))).selectByValue("AULA");
        driver.findElement(By.id("location")).sendKeys("Planta 3");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("5");

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('name').removeAttribute('required');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/spaces/new")
                        || bodyText().contains("nombre") || bodyText().contains("name"),
                "Nombre vacío debe mostrar error y permanecer en el formulario"
        );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // [Prueba 13] Registrar un nuevo espacio con capacidad menor que 1 (debe fallar)
    @Test
    @Order(13)
    public void prueba13_registrarEspacioCapacidadInvalida() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/new");

        driver.findElement(By.id("name")).sendKeys("SalaCapInvalida");
        new Select(driver.findElement(By.id("type"))).selectByValue("COWORK");
        driver.findElement(By.id("location")).sendKeys("Planta 1");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("0");

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('capacity').removeAttribute('min');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/spaces/new")
                        || bodyText().contains("capacidad") || bodyText().contains("capacity"),
                "Capacidad < 1 debe mostrar error y permanecer en el formulario"
        );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // [Prueba 14] Registrar espacio con el mismo nombre que otro espacio activo (debe fallar)
    @Test
    @Order(14)
    public void prueba14_registrarEspacioNombreDuplicado() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/new");

        // "Sala A" debe existir en los datos semilla como espacio activo
        driver.findElement(By.id("name")).sendKeys("Sala Ada Lovelace");
        new Select(driver.findElement(By.id("type"))).selectByValue("SALA");
        driver.findElement(By.id("location")).sendKeys("Planta 1 - Edificio A");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("10");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                bodyText().contains("nombre") || bodyText().contains("existe")
                        || bodyText().contains("duplicado"),
                "Nombre duplicado de espacio activo debe mostrar error"
        );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 5 – Editar espacios existentes
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 15] Editar un espacio existente con datos válidos
    @Test
    @Order(15)
    public void prueba15_editarEspacioValido() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        driver.findElement(By.cssSelector("a[href*='/spaces/edit/']")).click();

        WebElement location = driver.findElement(By.id("location"));
        location.clear();
        location.sendKeys("Ubicación editada Selenium");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/spaces/list")
                        || bodyText().contains("correctamente"),
                "Edición válida debe redirigir al listado o mostrar mensaje de éxito"
        );
    }

    // [Prueba 16] Editar un espacio con capacidad menor que 1 (debe fallar)
    @Test
    @Order(16)
    public void prueba16_editarEspacioCapacidadInvalida() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        driver.findElement(By.cssSelector("a[href*='/spaces/edit/']")).click();

        WebElement capacity = driver.findElement(By.id("capacity"));
        capacity.clear();
        capacity.sendKeys("0");

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('capacity').removeAttribute('min');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                bodyText().contains("capacidad") || bodyText().contains("capacity"),
                "Capacidad < 1 en edición debe mostrar error"
        );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 6 – Activar/Desactivar espacios (baja lógica)
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 17] Desactivar un espacio y verificar que no aparece como reservable
    @Test
    @Order(17)
    public void prueba17_desactivarEspacio() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        WebElement deactivateBtn = driver.findElement(By.xpath(
                "//button[contains(@class,'btn-toggle') and contains(@class,'btn-warning')]"
        ));
        String spaceName = deactivateBtn
                .findElement(By.xpath("ancestor::tr/td[1]"))
                .getText();

        deactivateBtn.click();

        // Esperar a que AJAX reemplace el fragmento
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        WebElement toggleBtn = driver.findElement(By.xpath(
                "//tr[td[1][normalize-space()='" + spaceName + "']]//button[contains(@class,'btn-toggle')]"
        ));
        assertTrue(
                toggleBtn.getAttribute("class").contains("btn-success"),
                "Tras desactivar, el botón debe cambiar a verde (Activar)"
        );

        // Verificar que el espacio ya no aparece en el listado de usuario
        driver.manage().deleteAllCookies();
        driver.navigate().to(URL + "/login");
        driver.findElement(By.id("username")).sendKeys("10000001S");
        driver.findElement(By.id("password")).sendKeys("Us3r@1-PASSW");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.navigate().to(URL + "/spaces/list");
        assertFalse(
                bodyText().contains(spaceName),
                "El espacio desactivado no debe aparecer en el listado de usuario"
        );
    }

    // [Prueba 18] Activar un espacio y verificar que sí aparece como reservable
    @Test
    @Order(18)
    public void prueba18_activarEspacio() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        WebElement activateBtn = driver.findElement(By.xpath(
                "//button[contains(@class,'btn-toggle') and contains(@class,'btn-success')]"
        ));
        String spaceName = activateBtn
                .findElement(By.xpath("ancestor::tr/td[1]"))
                .getText();

        activateBtn.click();

        // Esperar a que AJAX reemplace el fragmento
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Verificar que el badge del espacio cambió a Activo en la misma página
        WebElement toggleBtn = driver.findElement(By.xpath(
                "//tr[td[1][normalize-space()='" + spaceName + "']]//button[contains(@class,'btn-toggle')]"
        ));
        assertTrue(
                toggleBtn.getAttribute("class").contains("btn-warning"),
                "Tras activar, el botón debe cambiar a amarillo (Desactivar)"
        );

        // Verificar que el espacio ahora sí aparece en el listado de usuario
        driver.manage().deleteAllCookies();
        driver.navigate().to(URL + "/login");
        driver.findElement(By.id("username")).sendKeys("10000001S");
        driver.findElement(By.id("password")).sendKeys("Us3r@1-PASSW");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.navigate().to(URL + "/spaces/list");
        assertTrue(
                bodyText().contains(spaceName),
                "El espacio activado debe aparecer en el listado de usuario"
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 7 – Crear bloqueos de mantenimiento
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 19] Crear un bloqueo de mantenimiento válido
    @Test
    @Order(19)
    public void prueba19_crearBloqueoValido() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/blocks/list/(\\d+).*", "$1");

        driver.navigate().to(URL + "/blocks/new/" + spaceId);

        driver.findElement(By.id("startDate")).sendKeys("2099-06-01T09:00");
        driver.findElement(By.id("endDate")).sendKeys("2099-06-01T18:00");
        driver.findElement(By.id("reason")).sendKeys("Mantenimiento Selenium válido");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        System.out.println("URL: " + driver.getCurrentUrl());

        assertTrue(
                driver.getCurrentUrl().contains("/blocks/list/")
                        || bodyText().contains("correctamente"),
                "Bloqueo válido debe guardarse y redirigir al listado de bloqueos"
        );
        assertTrue(
                bodyText().contains("Mantenimiento Selenium válido")
                        || bodyText().contains("correctamente"),
                "El motivo del bloqueo o mensaje de éxito debe aparecer"
        );
    }

    // [Prueba 20] Crear un bloqueo solapado con otro bloqueo activo (debe fallar)
    @Test
    @Order(20)
    public void prueba20_crearBloqueoSolapadoConBloqueo() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/blocks/list/(\\d+).*", "$1");

        // Paso 1: crea su propio bloqueo base
        driver.navigate().to(URL + "/blocks/new/" + spaceId);
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-07-01T09:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-07-01T18:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo base prueba 20");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Paso 2: intenta crear un bloqueo solapado
        driver.navigate().to(URL + "/blocks/new/" + spaceId);
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-07-01T10:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-07-01T17:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo solapado");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                bodyText().contains("solapa") || bodyText().contains("overlap")
                        || bodyText().contains("bloqueo") || bodyText().contains("block.error"),
                "Bloqueo solapado con otro activo debe mostrar error"
        );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    @Test
    @Order(21)
    public void prueba21_crearBloqueoSolapadoConReserva() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        // Busca el enlace de bloqueos de "Sala Ada Lovelace" (space1, donde está la reserva semilla)
        WebElement blocksLink = driver.findElement(
                By.xpath("//tr[td[1][normalize-space()='Sala Ada Lovelace']]//a[contains(@href,'/blocks/list/')]")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/blocks/list/(\\d+).*", "$1");

        driver.navigate().to(URL + "/blocks/new/" + spaceId);

        // Usar JavaScript para setear las fechas — sendKeys no funciona bien en Firefox
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-09-15T10:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-09-15T17:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo sobre reserva");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                bodyText().contains("reserva") || bodyText().contains("reservation")
                        || bodyText().contains("solapa"),
                "Bloqueo solapado con reserva activa debe mostrar error"
        );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 8 – Cancelar bloqueos de mantenimiento
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 22] Cancelar un bloqueo y verificar que queda en estado CANCELADO
    @Test
    @Order(22)
    public void prueba22_cancelarBloqueo() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/blocks/list/(\\d+).*", "$1");

        // Primero crea un bloqueo para asegurar que hay uno activo que cancelar
        driver.navigate().to(URL + "/blocks/new/" + spaceId);
        driver.findElement(By.id("startDate")).sendKeys("2099-08-01T09:00");
        driver.findElement(By.id("endDate")).sendKeys("2099-08-01T18:00");
        driver.findElement(By.id("reason")).sendKeys("Bloqueo a cancelar prueba 22");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Pulsa el primer botón de cancelar bloqueo activo
        WebElement cancelBtn = driver.findElement(By.xpath(
                "//form[contains(@action,'/blocks/cancel/')]//button[@type='submit']"
        ));
        cancelBtn.click();

        // El bloqueo debe aparecer como CANCELADO en el listado
        assertTrue(
                bodyText().contains("CANCELADO") || bodyText().contains("CANCELLED"),
                "El bloqueo cancelado debe mostrarse con estado CANCELADO"
        );
    }

    @Test
    @Order(23)
    void PR23(){


        // TODO falta por corregir y terminar


        PO_LoginView.loginAndCheck(driver, "12345678Z", "@Dm1n1str@D0r", "12345678Z");

        driver.navigate().to(URL + "/reservations/list");

        PO_View.checkElementBy(driver, "text", "Estado");
        PO_View.checkElementBy(driver, "text", "Usuario");
        PO_View.checkElementBy(driver, "text", "Inicio");
        PO_View.checkElementBy(driver, "text", "Espacio");
        PO_View.checkElementBy(driver, "text", "Fin");

        PO_PrivateView.checkPagination(driver);

        List<WebElement> filas = driver.findElements(By.xpath("//*[@id=\"tableReservation\"]/table/tbody"));
        int numeroDeFilas = filas.size();
        assertEquals(5, numeroDeFilas, "Debería haber exactamente 5 elementos en la tabla");
        assertTrue(numeroDeFilas > 0, "La tabla no debería estar vacía");
    }

    @Test
    @Order(24)
    void PR24(){


        // TODO falta por corregir y terminar


        PO_LoginView.loginAndCheck(driver, "12345678Z", "@Dm1n1str@D0r", "12345678Z");

        driver.navigate().to(URL + "/reservations/list");

        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"spaceId\"]/option[2]", 0);

        driver.findElement(By.xpath("//*[@id=\"main-container\"]/form/button")).click();

        assertTrue(driver.getCurrentUrl().contains("spaceId="), "La URL debe contener el filtro por espacio");

        PO_PrivateView.checkPagination(driver);

        assertTrue(driver.getCurrentUrl().contains("spaceId="), "El filtro de espacio debe mantenerse al cambiar de página");

        List<WebElement> filas = driver.findElements(By.xpath("//*[@id=\"tableReservation\"]/table/tbody"));
        int numeroDeFilas = filas.size();
        assertEquals(5, numeroDeFilas, "Debería haber exactamente 5 elementos en la tabla");
        assertTrue(numeroDeFilas > 0, "La tabla no debería estar vacía");
    }

    @Test
    @Order(25)
    void PR25(){


        // TODO falta por corregir y terminar


        PO_LoginView.loginAndCheck(driver, "12345678Z", "@Dm1n1str@D0r", "12345678Z");

        driver.navigate().to(URL + "/reservations/list");

        driver.findElement(By.id("startDate")).sendKeys("2026-03-14");
        driver.findElement(By.id("endDate")).sendKeys("2026-03-18");

        PO_PrivateView.findAndClick(driver, "free",
                "//form[@action='/reservations/list']//button[@type='submit']", 0);

        try { Thread.sleep(2500); } catch (InterruptedException ignored) {}

        assertTrue(driver.getCurrentUrl().contains("startDate="), "La URL debe contener la fecha de inicio");
        assertTrue(driver.getCurrentUrl().contains("endDate="), "La URL debe contener la fecha de fin");

        PO_PrivateView.checkPagination(driver);

        assertTrue(driver.getCurrentUrl().contains("startDate="), "El filtro de fecha debe mantenerse al cambiar de página");
    }

    @Test
    @Order(26)
        //Consultar el listado de espacios disponibles
    void PR26(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/spaces/list");

        String checkText = "Sala Ada Lovelace";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

    }

    //Se aplica el filtro de capacidad con el valor 10
    @Test
    @Order(27)
    void PR27(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/spaces/list");

        driver.findElement(By.id("minCapacity")).sendKeys("10");
        String checkText = "Sala Ada Lovelace";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

        checkText = "Laboratorio Alan Turing";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

        checkText = "Auditorio Grace Hopper";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    //Se accede a los detalles del primer espacio
    @Test
    @Order(28)
    void PR28(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/spaces/list");

        driver.findElement(By.xpath("/html/body/div/div/div[1]/table/tbody/tr[1]/td[5]/a")).click();
        //btn btn-sm btn-info

        String checkText = "Sala Ada Lovelace";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

        checkText = "SALA";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

        checkText = "Planta 1 - Edificio A";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }


    //Se consulta la disponibilidad de un espacio
    @Test
    @Order(29)
    void PR29(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/spaces/list");

        driver.findElement(By.xpath("/html/body/div/div/div[1]/table/tbody/tr[4]/td[6]/a")).click();
        driver.findElement(By.id("startDate")).sendKeys("01-01-2026T01:00");
        driver.findElement(By.id("endDate")).sendKeys("29-12-2027T23:50");
        driver.findElement(By.xpath("/html/body/div/form/div/button")).click();

        String checkText = "RESERVA";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
        Assertions.assertEquals(checkText, result.getLast().getText());
    }

    //Registrar una reserva valida
    @Test
    @Order(30)
    void PR30(){

        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/reservations/add");

        //rellenar datos reserva
        driver.findElement(By.id("startDate")).sendKeys("12-01-2028T14:00");
        driver.findElement(By.id("endDate")).sendKeys("12-01-2028T16:00");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        //se va a la lista de reservas
        driver.navigate().to(URL + "/reservations/list");
        driver.findElement(By.xpath("/html/body/div/div[3]/ul/li[3]/a")).click();

        String checkText = "Sala Ada Lovelace";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    //Registrar una reserva inválida (inicio posterior al fin)
    @Test
    @Order(31)
    void PR31(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/reservations/add");

        //rellenar datos reserva
        driver.findElement(By.id("startDate")).sendKeys("12-01-2028T18:00");
        driver.findElement(By.id("endDate")).sendKeys("12-01-2028T16:00");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String checkText = "La fecha de inicio debe ser anterior a la de fin";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    //Crear dos reservas solapadas en el mismo espacio (la primera es válida, pero la segunda
    //debe fallar).
    @Test
    @Order(32)
    void PR32(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/reservations/add");

        //rellenar datos reserva valida
        driver.findElement(By.id("startDate")).sendKeys("12-01-2029T14:00");
        driver.findElement(By.id("endDate")).sendKeys("12-01-2029T16:00");

        driver.findElement(By.cssSelector("button[type='submit']")).click();


        //Se realiza una reserva en el mismo horario
        driver.findElement(By.id("startDate")).sendKeys("12-01-2029T14:00");
        driver.findElement(By.id("endDate")).sendKeys("12-01-2029T16:00");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String checkText = "La reserva nos e puede realizar por un solapamiento";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    //Intentar reservar dentro de un bloqueo (debe fallar).
    @Test
    @Order(33)
    void PR33(){
        //LocalDateTime.of(2027, 4, 10, 8, 0),
        //LocalDateTime.of(2027, 4, 10, 18, 0),
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/reservations/add");

        driver.findElement(By.id("startDate")).sendKeys("10-04-2029T13:00");
        driver.findElement(By.id("endDate")).sendKeys("10-04-2029T15:00");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String checkText = "La reserva nos e puede realizar por un solapamiento";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(34)
    void PR34(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");

        List<WebElement> elements = PO_View.checkElementBy(driver, "@href", "/reservations/list");
        elements.get(0).click();

        PO_View.checkElementBy(driver, "text", "Listado Global de Reservas");

        PO_View.checkElementBy(driver, "text", "Espacio");
        PO_View.checkElementBy(driver, "text", "Inicio");
        PO_View.checkElementBy(driver, "text", "Fin");
        PO_View.checkElementBy(driver, "text", "Estado");

        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        assertEquals(4, rows.size(), "El listado debería mostrar 4 reservas para este usuario");

        PO_LoginView.logout(driver);
    }

    @Test
    @Order(35)
    void PR35(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");

        List<WebElement> elements = PO_View.checkElementBy(driver, "@href", "/reservations/list");
        elements.get(0).click();

        WebElement statusSelect = driver.findElement(By.id("status"));
        statusSelect.click();

        WebElement cancelledOption = driver.findElement(By.xpath("//*[@id=\"status\"]/option[3]"));
        cancelledOption.click();

        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"main-container\"]/form/button"));
        searchButton.click();

        List<WebElement> statusCells = driver.findElements(By.xpath("//*[@id='tableReservation']/table/tbody/tr"));

        assertEquals(1, statusCells.size(), "Debería haber al menos una reserva cancelada");

        for (WebElement cell : statusCells) {
            String cellText = cell.getText().toUpperCase();
            Assertions.assertTrue(cellText.contains("CANCELADA") || cellText.contains("CANCELLED"),
                    "El estado de la reserva listada debería ser CANCELADA, pero fue: " + cellText);
        }

        PO_LoginView.logout(driver);
    }

    // [Prueba 42] Crear una reserva recurrente semanal válida y comprobar que
    // se han creado todas las reservas previstas.
    @Test
    @Order(42)
    void PR42() {
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/reservations/list");

        List<WebElement> rowsAntes = driver.findElements(
                By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        int countAntes = rowsAntes.size();

        driver.navigate().to(URL + "/reservations/add");

        new Select(driver.findElement(By.id("space")))
                .selectByVisibleText("Sala Ada Lovelace - Capacidad: 10");

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2031-03-03T10:00';");
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2031-03-03T12:00';");

        new Select(driver.findElement(By.id("recurrenceFrequency")))
                .selectByValue("WEEKLY");

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('recurrenceEndDate').value = '2031-03-24';" +
                        "document.getElementById('recurrenceEndDate').disabled = false;");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        String successMsg = PO_HomeView.getP().getString(
                "reservation.recurrence.success", PO_Properties.getSPANISH());
        List<WebElement> successElement = PO_View.checkElementBy(driver, "text", successMsg);
        Assertions.assertFalse(successElement.isEmpty(),
                "Debe aparecer el mensaje de éxito al crear reservas recurrentes válidas");

        driver.navigate().to(URL + "/reservations/list");
        List<WebElement> rowsDespues = driver.findElements(
                By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        int countDespues = rowsDespues.size();

        Assertions.assertTrue(countDespues >= countAntes || countDespues == 5,
                "Tras crear 4 reservas recurrentes el listado debe tener más entradas o estar lleno");

        PO_LoginView.logout(driver);
    }

    // [Prueba 43] Intentar crear una reserva recurrente que genere un solape
    // y comprobar que el sistema la rechaza mostrando un mensaje adecuado.
    @Test
    @Order(43)
    void PR43() {
        // Login como empleado
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");
        driver.navigate().to(URL + "/reservations/list");

        // Contar reservas del usuario ANTES de intentar la recurrente con solape
        List<WebElement> rowsAntes = driver.findElements(
                By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        int countAntes = rowsAntes.size();

        // Ir al formulario de añadir reserva
        driver.navigate().to(URL + "/reservations/add");

        // Seleccionar "Sala Linus Torvalds" (space5)
        // Tiene un bloqueo activo el 2027-04-10 de 08:00 a 18:00
        new Select(driver.findElement(By.id("space")))
                .selectByVisibleText("Sala Linus Torvalds - Capacidad: 8");

        // Reserva base: 2027-03-27 10:00-12:00
        // Recurrencia semanal hasta 2027-04-17 genera: 27/03, 03/04, 10/04 ← SOLAPA, 17/04
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2027-03-27T10:00';");
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2027-03-27T12:00';");

        // Seleccionar frecuencia WEEKLY
        new Select(driver.findElement(By.id("recurrenceFrequency")))
                .selectByValue("WEEKLY");

        // Fecha fin: 2027-04-17 → la ocurrencia del 10/04 solapa con el bloqueo
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('recurrenceEndDate').value = '2027-04-17';" +
                        "document.getElementById('recurrenceEndDate').disabled = false;");

        // Enviar formulario
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que se muestra el mensaje de error de solape
        String errorMsg = PO_HomeView.getP().getString(
                "reservation.recurrence.overlap", PO_Properties.getSPANISH());
        List<WebElement> errorElement = PO_View.checkElementBy(driver, "text", errorMsg);
        Assertions.assertFalse(errorElement.isEmpty(),
                "Debe aparecer el mensaje de error cuando la recurrencia solapa con un bloqueo");

        // Comprobar que no se creó ninguna reserva (el sistema rechaza todas o ninguna)
        driver.navigate().to(URL + "/reservations/list");
        List<WebElement> rowsDespues = driver.findElements(
                By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        int countDespues = rowsDespues.size();

        Assertions.assertEquals(countAntes, countDespues,
                "El número de reservas no debe haber aumentado si hubo un solape");

        PO_LoginView.logout(driver);
    }
}