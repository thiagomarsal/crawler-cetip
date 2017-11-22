package br.com.clubedoporquinho.service;

import com.paulhammant.ngwebdriver.ByAngular;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class WebScraping {

    private static final Logger log = LoggerFactory.getLogger(WebScraping.class);

    public void start(String cpf, String senha, String codInstituicao) {
        WebDriver driver = null;

        try {
//            driver = new ChromeDriver();
            driver = new RemoteWebDriver(new URL("http://localhost:9515"), DesiredCapabilities.chrome());
            driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
            NgWebDriver ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);

            driver.manage().window().maximize();

            driver.get("https://www.cetipmeusinvestimentos.com.br/extrato/#/");

            log.debug(driver.getCurrentUrl());

//            WebElement inputCPF = driver.findElement(By.xpath("/html/body/div[2]/form/div[1]/input"));
            WebElement inputCPF = driver.findElement(ByAngular.model("cpf"));
            inputCPF.sendKeys(cpf);
            WebElement btnAcessar = driver.findElement(By.xpath("//*[@id=\"btnMain\"]"));
            btnAcessar.click();
            ngWebDriver.waitForAngularRequestsToFinish();

            log.debug("Preenchendo CPF={}", cpf);
            log.debug("Clicar no botão Acessar");
            log.debug(driver.getCurrentUrl());

//            WebElement inputContaComitente = driver.findElement(By.xpath("//*[@id=\"ctaComitente\"]"));
            WebElement inputContaComitente = driver.findElement(ByAngular.model("contaComitente"));
            inputContaComitente.sendKeys(codInstituicao);

//            WebElement inputSenha = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/form/div/div[2]/div/div/div[5]/password-keyboard/input"));
            WebElement inputSenha = driver.findElement(ByAngular.model("fakeSenha"));
            inputSenha.sendKeys(senha);

            WebElement btnEntrar = driver.findElement(By.xpath("//*[@id=\"pass\"]"));
            btnEntrar.click();
            ngWebDriver.waitForAngularRequestsToFinish();

            log.debug("Preenchendo codigo da entidade={} e senha={}", codInstituicao, senha);
            log.debug("Clicar no botão Entrar");
            log.debug(driver.getCurrentUrl());

            WebElement nome = driver.findElement(By.xpath("/html/body/nav/div/div[1]/div[2]/div/div[1]/span[1]"));
            WebElement corretora = driver.findElement(By.xpath("/html/body/nav/div/div[1]/div[2]/div/div[1]/span[2]"));

            log.debug(nome.getText());
            log.debug(corretora.getText());

            WebElement b3 = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div/div[3]/div/table/tbody/tr[1]/th"));
            WebElement razao = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div/div[3]/div/table/tbody/tr[2]/td[1]"));
            WebElement documento = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div/div[3]/div/table/tbody/tr[2]/td[2]"));
            WebElement instituicao = driver.findElement(By.xpath("/html/body/div[2]/div[1]/div[2]/div/div[3]/div/table/tbody/tr[3]/td[1]"));
            log.debug(b3.getText());
            log.debug(razao.getText());
            log.debug(documento.getText());
            log.debug(instituicao.getText());

            List<WebElement> certifica = driver.findElements(ByAngular.repeater("rowRendaFixa in extract.cetipCertificaPaginado.dados"));
            certifica.forEach(webElement -> {
                log.debug(webElement.getText());
            });

            WebElement btnValoresMobiliariosEDerivativos = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div/div[1]/div[2]/a[2]"));
            btnValoresMobiliariosEDerivativos.click();
            ngWebDriver.waitForAngularRequestsToFinish();

            List<WebElement> valoresMobiliariosEDerivativos = driver.findElements(ByAngular.repeater("rowRendaFixa in extract.rendaFixaPaginado.dados"));
            valoresMobiliariosEDerivativos.forEach(webElement -> {
                log.debug(webElement.getText());
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
