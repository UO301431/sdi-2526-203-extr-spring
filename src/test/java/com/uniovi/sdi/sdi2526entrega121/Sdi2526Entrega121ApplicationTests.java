package com.uniovi.sdi.sdi2526entrega121;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.*;

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
    public void setUp() {
        driver.navigate().to(URL);
    }

    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    @BeforeAll
    static public void begin() {}

    @AfterAll
    static public void end() {
        driver.quit();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void loginAsAdmin() {
        driver.navigate().to(URL + "/login");
        driver.findElement(By.id("dni")).sendKeys("12345678Z");
        driver.findElement(By.id("password")).sendKeys("@Dm1n1str@D0r");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private String bodyText() {
        return driver.findElement(By.tagName("body")).getText();
    }
    /*
    // ─────────────────────────────────────────────────────────────────────────
    // TAREA 4 – Registrar nuevos espacios
    // ─────────────────────────────────────────────────────────────────────────

    // [Prueba 11] Registrar un nuevo espacio con datos válidos
    @Test
    @Order(11)
    public void prueba11_registrarEspacioValido() {
        loginAsAdmin();
        driver.navigate().to(URL + "/admin/spaces/new");

        driver.findElement(By.id("name")).sendKeys("SalaSelenium01");
        new Select(driver.findElement(By.id("type"))).selectByValue("SALA");
        driver.findElement(By.id("location")).sendKeys("Planta 2 Test");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("8");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/admin/spaces/list")
                        || bodyText().contains("SalaSelenium01"),
                "Tras crear un espacio válido debe redirigir al listado o mostrar el espacio"
        );
    }

    // [Prueba 12] Registrar un nuevo espacio con nombre vacío (debe fallar)
    @Test
    @Order(12)
    public void prueba12_registrarEspacioNombreVacio() {
        loginAsAdmin();
        driver.navigate().to(URL + "/admin/spaces/new");

        // Dejamos el nombre vacío
        new Select(driver.findElement(By.id("type"))).selectByValue("AULA");
        driver.findElement(By.id("location")).sendKeys("Planta 3");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("5");

        // Eliminamos el 'required' del campo para poder enviar el formulario vacío
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('name').removeAttribute('required');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/admin/spaces/new")
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
        driver.navigate().to(URL + "/admin/spaces/new");

        driver.findElement(By.id("name")).sendKeys("SalaCapInvalida");
        new Select(driver.findElement(By.id("type"))).selectByValue("COWORK");
        driver.findElement(By.id("location")).sendKeys("Planta 1");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("0");

        // Eliminamos el min=1 para que el navegador no bloquee el envío
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementById('capacity').removeAttribute('min');"
        );
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/admin/spaces/new")
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
        driver.navigate().to(URL + "/admin/spaces/new");

        // "Sala A" debe existir en los datos semilla como espacio activo
        driver.findElement(By.id("name")).sendKeys("Sala A");
        new Select(driver.findElement(By.id("type"))).selectByValue("SALA");
        driver.findElement(By.id("location")).sendKeys("Planta 0");
        driver.findElement(By.id("capacity")).clear();
        driver.findElement(By.id("capacity")).sendKeys("5");
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
        driver.navigate().to(URL + "/admin/spaces/list");

        driver.findElement(By.cssSelector("a[href*='/admin/spaces/edit/']")).click();

        WebElement location = driver.findElement(By.id("location"));
        location.clear();
        location.sendKeys("Ubicación editada Selenium");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/admin/spaces/list")
                        || bodyText().contains("correctamente"),
                "Edición válida debe redirigir al listado o mostrar mensaje de éxito"
        );
    }

    // [Prueba 16] Editar un espacio con capacidad menor que 1 (debe fallar)
    @Test
    @Order(16)
    public void prueba16_editarEspacioCapacidadInvalida() {
        loginAsAdmin();
        driver.navigate().to(URL + "/admin/spaces/list");

        driver.findElement(By.cssSelector("a[href*='/admin/spaces/edit/']")).click();

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
        driver.navigate().to(URL + "/admin/spaces/list");

        // Obtener el nombre del espacio en la fila del primer botón "Desactivar"
        WebElement deactivateBtn = driver.findElement(By.xpath(
                "//button[contains(@class,'btn-toggle') and contains(@class,'btn-warning')]"
        ));
        String spaceName = deactivateBtn
                .findElement(By.xpath("ancestor::tr/td[1]"))
                .getText();

        // AJAX: el botón no envía un formulario, hace un POST con jQuery
        // Selenium hace clic igual — el navegador ejecuta el JS
        deactivateBtn.click();

        // Esperar a que el fragmento se reemplace (el badge cambia a Inactivo)
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Verificar que el badge del espacio ahora es "Inactivo"
        WebElement row = driver.findElement(By.xpath(
                "//tr[td[1][normalize-space()='" + spaceName + "']]"
        ));
        assertTrue(
                row.getText().contains("Inactivo") || row.getText().contains("Inactive"),
                "Tras desactivar, el badge del espacio debe mostrar Inactivo sin recargar la página"
        );

        // Verificar que el espacio ya no aparece en el listado de usuario estándar
        driver.navigate().to(URL + "/spaces/list");
        assertFalse(
                bodyText().contains(spaceName),
                "El espacio desactivado '" + spaceName + "' no debe aparecer en el listado de usuario"
        );
    }

    // [Prueba 18] Activar un espacio y verificar que sí aparece como reservable
    @Test
    @Order(18)
    public void prueba18_activarEspacio() {
        loginAsAdmin();
        driver.navigate().to(URL + "/admin/spaces/list");

        // Obtener el nombre del espacio en la fila del primer botón "Activar"
        WebElement activateBtn = driver.findElement(By.xpath(
                "//button[contains(@class,'btn-toggle') and contains(@class,'btn-success')]"
        ));
        String spaceName = activateBtn
                .findElement(By.xpath("ancestor::tr/td[1]"))
                .getText();

        activateBtn.click();

        // Esperar a que el fragmento AJAX se reemplace
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Verificar que el badge del espacio ahora es "Activo"
        WebElement row = driver.findElement(By.xpath(
                "//tr[td[1][normalize-space()='" + spaceName + "']]"
        ));
        assertTrue(
                row.getText().contains("Activo") || row.getText().contains("Active"),
                "Tras activar, el badge del espacio debe mostrar Activo sin recargar la página"
        );

        // Verificar que el espacio ahora sí aparece en el listado de usuario estándar
        driver.navigate().to(URL + "/spaces/list");
        assertTrue(
                bodyText().contains(spaceName),
                "El espacio activado '" + spaceName + "' debe aparecer en el listado de usuario"
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
        driver.navigate().to(URL + "/admin/spaces/list");

        // Ir a la página de bloqueos del primer espacio
        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/admin/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/
            /*
                admin/blocks/list/(\\d+).*", "$1");


        driver.navigate().to(URL + "/admin/blocks/new/" + spaceId);

        // Fechas lejanas para evitar colisión con datos semilla
        driver.findElement(By.id("startDate")).sendKeys("2099-06-01T09:00");
        driver.findElement(By.id("endDate")).sendKeys("2099-06-01T18:00");
        driver.findElement(By.id("reason")).sendKeys("Mantenimiento Selenium válido");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getCurrentUrl().contains("/admin/blocks/list/")
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
        driver.navigate().to(URL + "/admin/spaces/list");

        WebElement blocksLink = driver.findElement(
            By.cssSelector("a[href*='/admin/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
            .replaceAll(".*/
        /*
        admin/blocks/list/(\\d+).*", "$1");

                // Paso 1: crea su propio bloqueo base, independiente de prueba 19
                driver.navigate().to(URL + "/admin/blocks/new/" + spaceId);
        driver.findElement(By.id("startDate")).sendKeys("2099-07-01T09:00");
        driver.findElement(By.id("endDate")).sendKeys("2099-07-01T18:00");
        driver.findElement(By.id("reason")).sendKeys("Bloqueo base prueba 20");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Paso 2: intenta crear un bloqueo solapado
        driver.navigate().to(URL + "/admin/blocks/new/" + spaceId);
        driver.findElement(By.id("startDate")).sendKeys("2099-07-01T10:00");
        driver.findElement(By.id("endDate")).sendKeys("2099-07-01T17:00");
        driver.findElement(By.id("reason")).sendKeys("Bloqueo solapado");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                bodyText().contains("solapa") || bodyText().contains("overlap")
                || bodyText().contains("bloqueo"),
            "Bloqueo solapado con otro activo debe mostrar error"
                    );
        assertFalse(bodyText().toLowerCase().contains("correctamente"));
    }

    // [Prueba 21] Crear un bloqueo solapado con una reserva activa (debe fallar)
    // IMPORTANTE: ajusta las fechas para que coincidan con una reserva semilla activa
    @Test
    @Order(21)
    public void prueba21_crearBloqueoSolapadoConReserva() {
        loginAsAdmin();
        driver.navigate().to(URL + "/admin/spaces/list");

        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/admin/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/
        /*
        admin/blocks/list/(\\d+).*", "$1");


        driver.navigate().to(URL + "/admin/blocks/new/" + spaceId);

        // Ajusta estas fechas para que caigan dentro de una reserva activa de tus datos semilla
        driver.findElement(By.id("startDate")).sendKeys("2026-05-01T08:00");
        driver.findElement(By.id("endDate")).sendKeys("2026-05-01T20:00");
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
        driver.navigate().to(URL + "/admin/spaces/list");

        WebElement blocksLink = driver.findElement(
                By.cssSelector("a[href*='/admin/blocks/list/']")
        );
        String spaceId = blocksLink.getAttribute("href")
                .replaceAll(".*/
        /*
        admin/blocks/list/(\\d+).*", "$1");

        driver.navigate().to(URL + "/admin/blocks/list/" + spaceId);

        // Pulsar el primer botón de cancelar bloqueo activo
        WebElement cancelBtn = driver.findElement(By.xpath(
                "//form[contains(@action,'/admin/blocks/cancel/')]//button[@type='submit']"
        ));
        cancelBtn.click();

        // El bloqueo debe aparecer como CANCELADO en el listado
        assertTrue(
                bodyText().contains("CANCELADO") || bodyText().contains("CANCELLED"),
                "El bloqueo cancelado debe mostrarse con estado CANCELADO"
        );
    }
    */

    @Test
    @Order(26)
        //Consultar el listado de espacios disponibles
    void PR26(){
        //TODO
    }

    //Se aplica el filtro de capacidad con el valor 10
    @Test
    @Order(27)
    void PR27(){
        //TODO
    }

    //Se accede a los detalles del primer espacio
    @Test
    @Order(28)
    void PR28(){
        //TODO
    }

    //Se consulta la disponibilidad de un espacio
    @Test
    @Order(29)
    void PR29(){
        //TODO
    }
}