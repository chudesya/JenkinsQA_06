package school.redrover;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;
import school.redrover.runner.BaseUtils;

import java.time.Duration;

public class GroupZeroBugTest extends BaseTest {

    private WebDriverWait webDriverWait3;

    public final WebDriverWait getWait3() {
        if (webDriverWait3 == null) {
            webDriverWait3 = new WebDriverWait(getDriver(), Duration.ofSeconds(3));
        }
        return webDriverWait3;
    }

    private void mainPage() {
        getDriver().get("http://localhost:8080/");
    }

    private void jobPage() {
        getDriver().get("http://localhost:8080/me/my-views/view/all/job/ZeroBugJavaPractice/");
    }

    private void newJob() {

        WebElement newJob = getDriver().findElement(By.xpath("//a[@href='/view/all/newJob']/.."));
        newJob.click();

        WebElement inputField = getDriver().findElement(By.id("name"));
        inputField.sendKeys("ZeroBugJavaPractice");

        WebElement freestyleProject = getDriver().findElement(By.cssSelector(".hudson_model_FreeStyleProject"));
        freestyleProject.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement gitProject = getDriver().findElement((By.xpath("//label[text()='GitHub project']")));
        gitProject.click();

        WebElement urlField = getDriver().findElement((By.xpath("//input[@name='_.projectUrlStr']")));
        urlField.sendKeys("https://github.com/Lighter888/ZeroBugJavaPractice");
        getDriver().findElement(By.xpath("//button[@name='Submit']")).click();
    }

    private void deleteJob() {

        WebElement deleteJob = getDriver().findElement(By.cssSelector(".task-link.confirmation-link"));
        deleteJob.click();

        try {
            Alert alert = getDriver().switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e) {
            System.out.println("No alert is present.");
        }
    }

    @Test(priority = 1)
    public void testNewJobCreated() {

        newJob();

        String actualNameJob = getDriver().findElement(By.cssSelector(".job-index-headline.page-headline")).getText();
        String expectedNameJob = "ZeroBugJavaPractice";
        Assert.assertEquals(actualNameJob,"Project "+ expectedNameJob," The name of job is not equal");

        deleteJob();
    }

    @Ignore
    @Test(priority = 2)
    public void testJobBuild() {

        newJob();
        mainPage();

        for (int trial = 1; trial <=3; trial++) {

            WebElement scheduleBuild = getDriver().findElement(By.xpath("//a[@title='Schedule a Build for ZeroBugJavaPractice']"));
            getWait3().until(ExpectedConditions.elementToBeClickable(scheduleBuild));
            scheduleBuild.click();

            WebElement buildHistory = getDriver().findElement(By.xpath("//a[@href='/view/all/builds']/.."));
            getWait3().until(ExpectedConditions.elementToBeClickable(buildHistory));
            buildHistory.click();

            getWait3().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[.='#%s']".formatted(trial))));
            WebElement numberBuild = getDriver().findElement(By.xpath("//a[.='#%s']".formatted(trial)));

            String actualNumberBuild = numberBuild.getText();
            String expectedNumberBuild = "#" + trial;
             BaseUtils.log("Check Build #%s".formatted(trial));
            Assert.assertEquals(actualNumberBuild,expectedNumberBuild, "Build has been scheduled incorrectly");
            mainPage();
        }

        jobPage();
        deleteJob();

    }

    @Test
    public void testJenkinsVersionCheck() {

        String expectedResult = "Jenkins 2.387.2";
        WebElement versionNumber = getDriver().findElement(By.xpath("//a[text()='Jenkins 2.387.2']"));

        String actualResult = versionNumber.getText();
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testSearchFieldIsPresent() {

        String expectedResult = "Welcome to Jenkins!";
        WebElement result = getDriver().findElement(By.xpath("//h1[.='Welcome to Jenkins!']"));
        String actualResult = result.getText();
        Assert.assertEquals(actualResult, expectedResult);

        WebElement searchField = getDriver().findElement(By.cssSelector("#search-box"));
        Assert.assertTrue(searchField.isDisplayed());
    }

    @Test
    public void testDashboardIsPresent() {

        String dashboardExpected = "Dashboard";
        WebElement dashboard = getDriver().findElement(By.xpath("//a[@href='/'][@class='model-link']"));
        String dashboardActual = dashboard.getText();

        Assert.assertEquals(dashboardActual, dashboardExpected);
    }

    @Test
    public void testCreateJob() {

        WebElement createButton = getDriver().findElement(By.xpath("//span[text()= 'Create a job']"));
        createButton.click();

        WebElement textBox = getDriver().findElement(By.name("name"));
        textBox.sendKeys("Project_1");

        WebElement freestyleProject = getDriver().findElement(By.xpath("//span[.='Freestyle project']"));
        freestyleProject.click();

        WebElement okButton = getDriver().findElement(By.id("ok-button"));
        okButton.click();

        WebElement dashboard = getDriver().findElement(By.xpath("//a[@href='/'][@class ='model-link']"));
        dashboard.click();

        WebElement projectName = getDriver().findElement(By.xpath("//a[@href = 'job/Project_1/']"));

        Assert.assertTrue(projectName.isDisplayed());
    }
}
