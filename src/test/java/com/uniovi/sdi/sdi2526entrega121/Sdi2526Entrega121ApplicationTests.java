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

    static WebDriver driver = getDriver(PathFirefox);
    static String URL = "http://localhost:8090";

    public static WebDriver getDriver(String PathFirefox) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
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

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void loginAsAdmin() {
        driver.navigate().to(URL + "/login");
        PO_LoginView.fillLoginForm(driver, "12345678Z", "@Dm1n1str@D0r");
    }

    private void loginAsUser() {
        driver.navigate().to(URL + "/login");
        PO_LoginView.fillLoginForm(driver, "10000001S", "Us3r@1-PASSW");
    }

    private String bodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TESTS 1–9 (sin cambios)
    // ─────────────────────────────────────────────────────────────────────────

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
    void PR04A(){
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
    void PR04B(){
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

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 4 – Registrar nuevos espacios
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 11] Registrar un nuevo espacio con datos válidos
    @Test
    @Order(11)
    public void prueba11_registrarEspacioValido() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/new");

        // Rellenar el formulario
        driver.findElement(By.id("name")).sendKeys("SalaSelenium01");
        new Select(driver.findElement(By.id("type"))).selectByValue("SALA");
        driver.findElement(By.id("location")).sendKeys("Planta 2 Test");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("8");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que redirige al listado o muestra el espacio creado
        assertTrue(
                driver.getCurrentUrl().contains("/spaces/list")
                        || !PO_View.checkElementBy(driver, "text", "SalaSelenium01").isEmpty(),
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

        // Eliminar 'required' para poder enviar el formulario sin nombre
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('name').removeAttribute('required');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que aparece el mensaje de error por clave i18n
        PO_View.checkElementByKey(driver, "space.error.name.empty", PO_Properties.getSPANISH());
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

        // Eliminar min=1 para que el navegador no bloquee el envío
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('capacity').removeAttribute('min');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que aparece el mensaje de error por clave i18n
        PO_View.checkElementByKey(driver, "space.error.capacity.invalid", PO_Properties.getSPANISH());
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // [Prueba 14] Registrar espacio con nombre duplicado (debe fallar)
    @Test
    @Order(14)
    public void prueba14_registrarEspacioNombreDuplicado() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/new");

        // "Sala Ada Lovelace" existe en los datos semilla como espacio activo
        driver.findElement(By.id("name")).sendKeys("Sala Ada Lovelace");
        new Select(driver.findElement(By.id("type"))).selectByValue("SALA");
        driver.findElement(By.id("location")).sendKeys("Planta 1 - Edificio A");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("10");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que aparece el mensaje de error por clave i18n
        PO_View.checkElementByKey(driver, "space.error.name.duplicate", PO_Properties.getSPANISH());
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

        // Clicar el primer enlace de editar
        PO_PrivateView.findAndClick(driver, "@href", "/spaces/edit/", 0);

        WebElement location = driver.findElement(By.id("location"));
        location.clear();
        location.sendKeys("Ubicación editada Selenium");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que redirige al listado
        assertTrue(
                driver.getCurrentUrl().contains("/spaces/list"),
                "Edición válida debe redirigir al listado"
        );
    }

    // [Prueba 16] Editar un espacio con capacidad menor que 1 (debe fallar)
    @Test
    @Order(16)
    public void prueba16_editarEspacioCapacidadInvalida() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        // Clicar el primer enlace de editar
        PO_PrivateView.findAndClick(driver, "@href", "/spaces/edit/", 0);

        WebElement capacity = driver.findElement(By.id("capacity"));
        capacity.clear();
        capacity.sendKeys("0");

        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('capacity').removeAttribute('min');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que aparece el mensaje de error por clave i18n
        PO_View.checkElementByKey(driver, "space.error.capacity.invalid", PO_Properties.getSPANISH());
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

        // Localizar el primer botón "Desactivar" y el nombre del espacio en su fila
        WebElement deactivateBtn = driver.findElement(By.xpath(
                "//button[contains(@class,'btn-toggle') and contains(@class,'btn-warning')]"
        ));
        String spaceName = deactivateBtn
                .findElement(By.xpath("ancestor::tr/td[1]"))
                .getText();

        deactivateBtn.click();

        // Esperar a que AJAX reemplace el fragmento
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Verificar que el botón cambió a verde (estado: inactivo → botón Activar)
        WebElement toggleBtn = driver.findElement(By.xpath(
                "//tr[td[1][normalize-space()='" + spaceName + "']]//button[contains(@class,'btn-toggle')]"
        ));
        assertTrue(
                toggleBtn.getAttribute("class").contains("btn-success"),
                "Tras desactivar, el botón debe cambiar a verde (Activar)"
        );

        // Verificar que el espacio no aparece en el listado de usuario estándar
        driver.manage().deleteAllCookies();
        loginAsUser();
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

        // Localizar el primer botón "Activar" y el nombre del espacio en su fila
        WebElement activateBtn = driver.findElement(By.xpath(
                "//button[contains(@class,'btn-toggle') and contains(@class,'btn-success')]"
        ));
        String spaceName = activateBtn
                .findElement(By.xpath("ancestor::tr/td[1]"))
                .getText();

        activateBtn.click();

        // Esperar a que AJAX reemplace el fragmento
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Verificar que el botón cambió a amarillo (estado: activo → botón Desactivar)
        WebElement toggleBtn = driver.findElement(By.xpath(
                "//tr[td[1][normalize-space()='" + spaceName + "']]//button[contains(@class,'btn-toggle')]"
        ));
        assertTrue(
                toggleBtn.getAttribute("class").contains("btn-warning"),
                "Tras activar, el botón debe cambiar a amarillo (Desactivar)"
        );

        // Verificar que el espacio sí aparece en el listado de usuario estándar
        driver.manage().deleteAllCookies();
        loginAsUser();
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

        // Obtener el id del primer espacio desde el enlace de bloqueos
        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/blocks/list/(\\d+).*", "$1");

        driver.navigate().to(URL + "/blocks/new/" + spaceId);

        // Fechas lejanas para evitar colisión con datos semilla
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-06-01T09:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-06-01T18:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Mantenimiento Selenium válido");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar que redirige al listado de bloqueos o muestra mensaje de éxito
        assertTrue(
                driver.getCurrentUrl().contains("/blocks/list/"),
                "Bloqueo válido debe redirigir al listado de bloqueos"
        );
        // Comprobar que el motivo aparece en el listado
        PO_View.checkElementBy(driver, "text", "Mantenimiento Selenium válido");
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

        // Paso 1: crear bloqueo base independiente de la prueba 19
        driver.navigate().to(URL + "/blocks/new/" + spaceId);
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-07-01T09:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-07-01T18:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo base prueba 20");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Paso 2: intentar crear bloqueo solapado con el anterior
        driver.navigate().to(URL + "/blocks/new/" + spaceId);
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-07-01T10:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-07-01T17:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo solapado");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar mensaje de error por clave i18n
        PO_View.checkElementByKey(driver, "block.error.overlap.block", PO_Properties.getSPANISH());
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // [Prueba 21] Crear un bloqueo solapado con una reserva activa (debe fallar)
    @Test
    @Order(21)
    public void prueba21_crearBloqueoSolapadoConReserva() {
        loginAsAdmin();
        driver.navigate().to(URL + "/spaces/list");

        // Buscar bloqueos de "Sala Ada Lovelace" — donde está la reserva semilla fija
        WebElement blocksLink = driver.findElement(
                By.xpath("//tr[td[1][normalize-space()='Sala Ada Lovelace']]//a[contains(@href,'/blocks/list/')]")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/blocks/list/(\\d+).*", "$1");

        driver.navigate().to(URL + "/blocks/new/" + spaceId);

        // Fechas que solapan con la reserva semilla fija (2099-09-15 09:00–18:00)
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-09-15T10:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-09-15T17:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo sobre reserva");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Comprobar mensaje de error por clave i18n
        PO_View.checkElementByKey(driver, "block.error.overlap.reservation", PO_Properties.getSPANISH());
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

        // Crear un bloqueo propio para asegurar que hay uno activo que cancelar
        driver.navigate().to(URL + "/blocks/new/" + spaceId);
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('startDate').value = '2099-08-01T09:00';"
        );
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('endDate').value = '2099-08-01T18:00';"
        );
        driver.findElement(By.id("reason")).sendKeys("Bloqueo a cancelar prueba 22");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Pulsar el primer botón de cancelar un bloqueo activo
        PO_PrivateView.findAndClick(driver, "free",
                "//form[contains(@action,'/blocks/cancel/')]//button[@type='submit']", 0);

        // Comprobar mensaje de éxito por clave i18n
        PO_View.checkElementByKey(driver, "block.success.cancelled", PO_Properties.getSPANISH());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TESTS 23–35 (sin cambios)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(23)
    void PR23(){
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
    void PR26(){ //TODO
         }
    @Test
    @Order(27)
    void PR27(){
        //TODO
        }
    @Test
    @Order(28)
            void PR28(){ //TODO
        }

                @Test
                @Order(29)
                void PR29(){ //TODO
         }

                    @Test
                    @Order(30)
                    void PR30(){ //TODO
         }

                        @Test
                        @Order(31)
                        void PR31(){ //TODO
         }

                            @Test
                            @Order(32)
                            void PR32(){ //TODO
         }

                                @Test
                                @Order(33)
                                void PR33(){ //TODO
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
                                }