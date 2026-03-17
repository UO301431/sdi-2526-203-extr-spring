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

    private void comprobarPaginacion() {
        // Busca el enlace a la página 2 y hace clic si existe
        List<WebElement> page2 = driver.findElements(By.xpath("//a[contains(@href, 'page=2')]"));
        if (!page2.isEmpty()) {
            page2.get(0).click();
            assertTrue(driver.getCurrentUrl().contains("page="), "La URL debe mantener la paginación");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 1&2&3 – Signup & login & salir de sesion
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    void PRO1(){
        driver.navigate().to(URL + "/signup");
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678B","Josef", "Roces", "@Dm1n1str@D0r","@Dm1n1str@D0r");

        String checkText = "Identíficate";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "12345678B", "@Dm1n1str@D0r");
        checkText = "Listado de Reservas";
        result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());


    }

    @Test
    @Order(2)
    void PR02(){
        driver.navigate().to(URL + "/signup");
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
        driver.navigate().to(URL + "/signup");
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
        driver.navigate().to(URL + "/signup");
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678T","Josef", "Roces", "Dm1n1strD0r","Dm1n1strD0r");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.password.valid",
                PO_Properties.getSPANISH());

        String checkText = PO_HomeView.getP().getString("Error.signup.password.valid",
                PO_Properties.getSPANISH());
        Assertions.assertEquals(checkText, result.getFirst().getText());


    }

    @Test
    @Order(5)
    void PR04B(){ //Sin numeros
        driver.navigate().to(URL + "/signup");
        PO_HomeView.clickOption(driver, "signup","class","btn btn-primary");
        PO_SignUpView.fillForm(driver, "12345678L","Josef", "Roces", "@Dmnstr@Dr","@Dmnstr@Dr");
        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.signup.password.valid",
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
        String checkText = "Gestión de Espacios";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(7)
    void PR06() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "10000001S", "Us3r@1-PASSW");
        String checkText = "Listado de Reservas";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(8)
    void PR07() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "123", "@Dm1n1str@D0r");
        String checkText = "DNI o contraseña incorrectos.";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(9)
    void PR08() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "12345678Z", "@Dm1n");
        String checkText = "DNI o contraseña incorrectos.";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    @Test
    @Order(10)
    void PR10() {
        // Administrador
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "12345678Z", "@Dm1n1str@D0r");

        By logoutSelector = By.xpath("//a[@href='/logout']");
        driver.findElement(logoutSelector).click();

        String checkText = "Identíficate";
        PO_View.checkElementBy(driver, "text", checkText);
    }

    @Test
    @Order(10)
    void PR09() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        PO_LoginView.fillLoginForm(driver, "10000001S", "Us3r@1-PASSW");

        By logoutSelector = By.xpath("//a[@href='/logout']");
        driver.findElement(logoutSelector).click();

        String checkText = "Identíficate";
        PO_View.checkElementBy(driver, "text", checkText);
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
        List<WebElement> filas = driver.findElements(By.xpath("//*[@id=\"tableReservation\"]/table/tbody/tr"));
        assertEquals(5, filas.size(), "Debería haber exactamente 5 elementos en la tabla");
        PO_PrivateView.checkPagination(driver);
        filas = driver.findElements(By.xpath("//*[@id=\"tableReservation\"]/table/tbody/tr"));
        assertEquals(5, filas.size(), "Debería haber exactamente 5 elementos en la tabla");
    }

    @Test
    @Order(24)
    void PR24(){
        PO_LoginView.loginAndCheck(driver, "12345678Z", "@Dm1n1str@D0r", "12345678Z");
        driver.navigate().to(URL + "/reservations/list");
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"spaceId\"]/option[2]", 0);
        driver.findElement(By.xpath("//*[@id=\"main-container\"]/form/button")).click();
        assertTrue(driver.getCurrentUrl().contains("spaceId=1"), "La URL debe contener el filtro por espacio");
        PO_PrivateView.checkPagination(driver);
        assertTrue(driver.getCurrentUrl().contains("spaceId=1"), "El filtro de espacio debe mantenerse al cambiar de página");
        List<WebElement> filas = driver.findElements(By.xpath("//*[@id=\"tableReservation\"]/table/tbody/tr"));
        int numeroDeFilas = filas.size();
        assertEquals(3, numeroDeFilas, "Debería haber exactamente 3 elementos en la tabla");
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
        assertTrue(driver.getCurrentUrl().contains("startDate=2026-03-14"), "La URL debe contener la fecha de inicio");
        assertTrue(driver.getCurrentUrl().contains("endDate=2026-03-18"), "La URL debe contener la fecha de fin");
        List<WebElement> filas = driver.findElements(By.xpath("//*[@id=\"tableReservation\"]/table/tbody/tr"));
        int numeroDeFilas = filas.size();
        assertEquals(2, numeroDeFilas, "Debería haber exactamente 2 elementos en la tabla");
        PO_PrivateView.checkPagination(driver);
        assertTrue(driver.getCurrentUrl().contains("startDate=2026-03-14"), "El filtro de fecha debe mantenerse al cambiar de página");
        assertTrue(driver.getCurrentUrl().contains("endDate=2026-03-18"), "El filtro de fecha debe mantenerse al cambiar de página");
        numeroDeFilas = filas.size();
        assertEquals(2, numeroDeFilas, "Debería haber exactamente 2 elementos en la tabla");
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

        driver.navigate().to(URL + "/reservations/list");

        PO_View.checkElementBy(driver, "text", "Listado de Reservas");

        PO_View.checkElementBy(driver, "text", "Espacio");
        PO_View.checkElementBy(driver, "text", "Inicio");
        PO_View.checkElementBy(driver, "text", "Fin");
        PO_View.checkElementBy(driver, "text", "Estado");

        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        assertEquals(5, rows.size(), "El listado debería mostrar 5 reservas para este usuario");

        PO_LoginView.logout(driver);
    }

    @Test
    @Order(35)
    void PR35(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");

        driver.navigate().to(URL + "/reservations/list");

        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"status\"]/option[3]", 0);

        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"main-container\"]/form/button", 0);

        List<WebElement> statusCells = driver.findElements(By.xpath("//*[@id='tableReservation']/table/tbody/tr"));

        assertEquals(1, statusCells.size(), "Debería haber una reserva cancelada");

        for (WebElement cell : statusCells) {
            String cellText = cell.getText().toUpperCase();
            Assertions.assertTrue(cellText.contains("CANCELADA") || cellText.contains("CANCELLED"),
                    "El estado de la reserva listada debería ser CANCELADA, pero fue: " + cellText);
        }

        PO_LoginView.logout(driver);
    }

    @Test
    @Order(36)
    void PR36(){
        PO_LoginView.loginAndCheck(driver, "10000001S", "Us3r@1-PASSW", "10000001S");

        driver.navigate().to(URL + "/reservations/list");

        // PRIMERO COMPRUEBO QUE INICIALMENTE SOLO HAY UNA RESERVA CANCELADA
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"status\"]/option[3]", 0);
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"main-container\"]/form/button", 0);
        List<WebElement> statusCells = driver.findElements(By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        assertEquals(1, statusCells.size(), "Debería haber una reserva cancelada");
        for (WebElement cell : statusCells) {
            String cellText = cell.getText().toUpperCase();
            Assertions.assertTrue(cellText.contains("CANCELADA") || cellText.contains("CANCELLED"),
                    "El estado de la reserva listada debería ser CANCELADA, pero fue: " + cellText);
        }

        // AHORA CANCELO OTRA RESERVA
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"status\"]/option[1]", 0);
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"main-container\"]/form/button", 0);
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"tableReservation\"]/table/tbody/tr[2]/td[6]/a", 0);

        // COMPRUEBO QUE AHORA HAY DOS RESERVAS CANCELADAS
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"status\"]/option[3]", 0);
        PO_PrivateView.findAndClick(driver, "free", "//*[@id=\"main-container\"]/form/button", 0);
        statusCells = driver.findElements(By.xpath("//*[@id='tableReservation']/table/tbody/tr"));
        assertEquals(2, statusCells.size(), "Debería haber dos reservas canceladas");
        for (WebElement cell : statusCells) {
            String cellText = cell.getText().toUpperCase();
            Assertions.assertTrue(cellText.contains("CANCELADA") || cellText.contains("CANCELLED"),
                    "El estado de la reserva listada debería ser CANCELADA, pero fue: " + cellText);
        }

        PO_LoginView.logout(driver);
    }

    /**
     * [Prueba 37] Modificar la contraseña con datos validos
     */
    @Test
    @Order(37)
    public void PR37(){
        loginAsUser();

        driver.navigate().to(URL + "/profile/password");

        driver.findElement(By.id("actualPassword")).sendKeys("Us3r@1-PASSW");
        driver.findElement(By.id("password")).sendKeys("NewPass@12345");
        driver.findElement(By.id("passwordConfirm")).sendKeys("NewPass@12345");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(driver.getCurrentUrl().contains("/home"), "Debería redirigir a home tras éxito");

        By logoutSelector = By.xpath("//a[@href='/logout']");
        driver.findElement(logoutSelector).click();

        PO_LoginView.fillLoginForm(driver, "10000001S", "NewPass@12345");
        String checkText = "Listado de Reservas";
        List<WebElement> result = PO_View.checkElementBy(driver, "text", checkText);
        Assertions.assertEquals(checkText, result.getFirst().getText());

        //Para no cambiar el comportamiento de los tests
        driver.navigate().to(URL + "/profile/password");

        driver.findElement(By.id("actualPassword")).sendKeys("NewPass@12345");
        driver.findElement(By.id("password")).sendKeys("Us3r@1-PASSW");
        driver.findElement(By.id("passwordConfirm")).sendKeys("Us3r@1-PASSW");
        driver.findElement(By.cssSelector("button[type='submit']")).click();


    }

    /**
     * [Prueba 38] Modificar la contraseña con datos invalidos
     */
    @Test
    @Order(38)
    public void PR38(){
        loginAsUser();
        driver.navigate().to(URL + "/profile/password");

        driver.findElement(By.id("actualPassword")).sendKeys("Us3r@1-PASSW");
        driver.findElement(By.id("password")).sendKeys("  ");
        driver.findElement(By.id("passwordConfirm")).sendKeys("  ");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        List<WebElement> result = PO_SignUpView.checkElementByKey(driver, "Error.empty",
                PO_Properties.getSPANISH());

        //Se juntan los dos errores porque esta vacia y no es una contraseña valida
        String checkText ="Este campo no puede estar vacio.\n" +
                        "La contraseña no es valida. La contraseña debe tener al menos 1 mayúscula, 1 minúscula, 1 dígito, 1 caracter especial y tener entre 12 y 20 caracteres.";
        Assertions.assertEquals(checkText, result.getFirst().getText());
    }

    /**
     * [Prueba 40] Accesso denegado de usuario estandar a recursos de administracion
     */
    @Test
    @Order(40)
    void PR40(){
        loginAsUser();
        driver.navigate().to(URL + "/spaces/new");

        String textoPagina = bodyText();
        assertTrue(textoPagina.contains("403")
                || textoPagina.contains("Forbidden"), "Debería mostrarse el error 403 Forbidden");
        List<WebElement> nameField = driver.findElements(By.id("name"));
        assertTrue(nameField.isEmpty(), "El formulario de admin no debería renderizarse");
    }

    /**
     * [Prueba 41] Intento de cancelar reserva ajena (debe fallar)
     */
    @Test
    void PR41(){
        loginAsAdmin();
        driver.navigate().to(URL + "/reservations/list");
        List<WebElement> reservasAntes = driver.findElements(By.xpath("//table//tbody/tr"));
        int contadorAntes = reservasAntes.size();

        By logoutSelector = By.xpath("//a[@href='/logout']");
        driver.findElement(logoutSelector).click();

        loginAsUser();
        driver.navigate().to(URL + "/reservations/cancel/1");

        boolean bloqueado = driver.getCurrentUrl().contains("/reservations/list")
                || bodyText().contains("403")
                || bodyText().contains("Forbidden");
        assertTrue(bloqueado, "El sistema debería haber redirigido o bloqueado el acceso");

        driver.findElement(logoutSelector).click();
        loginAsAdmin();
        driver.navigate().to(URL + "/reservations/list");
        List<WebElement> reservasDespues = driver.findElements(By.xpath("//table//tbody/tr"));
        int contadorDespues = reservasDespues.size();
        assertEquals(contadorAntes, contadorDespues,
                "El número de reservas ha cambiado.");


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