package selenium.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.nio.file.Paths;
import java.util.HashMap;

import static selenium.model.OSInfo.*;


public enum DriverType implements DriverSetup {

    FIREFOX {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            WebDriverManager.firefoxdriver().targetPath(System.getProperty("seleniumBinariesDir"))
                    /*.version("0.24.0")*/.setup();
            FirefoxOptions options = new FirefoxOptions();
            options.merge(capabilities);
            options.setHeadless(HEADLESS);
            setWebDriverLocation("webdriver.gecko.driver", "marionette", "geckodriver");
            return new FirefoxDriver(options);
        }
    },
    CHROME {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            WebDriverManager.chromedriver().targetPath(System.getProperty("seleniumBinariesDir")).setup();
            HashMap<String, Object> chromePreferences = new HashMap<>();
            chromePreferences.put("profile.password_manager_enabled", false);

            ChromeOptions options = new ChromeOptions();
            options.merge(capabilities);
            options.setHeadless(HEADLESS);
            options.addArguments("--no-default-browser-check");
            options.setExperimentalOption("prefs", chromePreferences);
            setWebDriverLocation("webdriver.chrome.driver", "googlechrome", "chromedriver");

            return new ChromeDriver(options);
        }
    },
    IE {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.merge(capabilities);
            options.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            options.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            options.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);

            return new InternetExplorerDriver(options);
        }

        @Override
        public String toString() {
            return "internet explorer";
        }
    },
    EDGE {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            EdgeOptions options = new EdgeOptions();
            options.merge(capabilities);

            return new EdgeDriver(options);
        }
    },
    SAFARI {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            SafariOptions options = new SafariOptions();
            options.merge(capabilities);

            return new SafariDriver(options);
        }
    },
    OPERA {
        public RemoteWebDriver getWebDriverObject(DesiredCapabilities capabilities) {
            OperaOptions options = new OperaOptions();
            options.merge(capabilities);

            return new OperaDriver(options);
        }
    };

    public final static boolean HEADLESS = Boolean.getBoolean("headless");

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    public void setWebDriverLocation(String key, String driverType, String driverName) {
        if (System.getProperty(key) != null)
            return;

        String baseDriverDir = System.getProperty("seleniumBinariesDir");
        String driverLocation = Paths.get(baseDriverDir, getOsName(), driverType, getOsType(), driverName).toString();

        if (Windows)
            driverLocation += ".exe";

        System.setProperty(key, driverLocation);
    }
}