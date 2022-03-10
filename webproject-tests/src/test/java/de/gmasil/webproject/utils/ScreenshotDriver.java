/*
 * Webproject NG
 * Copyright © 2021 - 2022 Gmasil
 *
 * This file is part of Webproject NG.
 *
 * Webproject NG is licensed under the Creative Commons
 * Attribution-NonCommercial-ShareAlike 4.0 International
 * Public License ("Public License").
 *
 * Webproject NG is non-free software: you can redistribute
 * it and/or modify it under the terms of the Public License.
 *
 * You should have received a copy of the Public License along
 * with Webproject NG. If not, see
 * https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
 */
package de.gmasil.webproject.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class ScreenshotDriver {

    @Autowired
    private WebDriver driver;

    @Value("${webdriver.screenshot.folder:target/selenium-screenshots}")
    private File screenshotFolder;

    @Value("${webdriver.screenshot.timestamp.format:yyyy-MM-dd-HH-mm-ss}")
    private String timestampFormat;

    @Value("${webdriver.screenshot.filename:%i-%ts.png}")
    private String defaultFilename;

    private int number = 1;

    /**
     * Take a screenshot of the current browser view, but wait <i>millis</i> ms
     * before doing so. Save the screenshot to the specified <i>file</i>. <br>
     * <br>
     * The placeholder <code>%ts</code> will be replaced by the current timestamp.
     * The format can be changed with the
     * <code>webdriver.screenshot.timestamp.format</code> property, default is
     * <code>yyyy-MM-dd-HH-mm-ss</code>. <br>
     * The placeholder <code>%i</code> will be replaced with an ongoing number
     * prepended with zeros to match the length of 4.
     *
     * @param filename the name of the output file
     * @param millis   time in ms to wait before screenshot is taken
     * @return file of the created screenshot
     */
    public File take(String filename, long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return take(filename);
    }

    /**
     * Take a screenshot of the current browser view. Save the screenshot to the
     * specified <i>file</i>. <br>
     * <br>
     * The placeholder <code>%ts</code> will be replaced by the current timestamp.
     * The format can be changed with the
     * <code>webdriver.screenshot.timestamp.format</code> property, default is
     * <code>yyyy-MM-dd-HH-mm-ss</code>. <br>
     * The placeholder <code>%i</code> will be replaced with an ongoing number
     * prepended with zeros to match the length of 3.
     *
     * @param filename the name of the output file
     * @return file of the created screenshot
     */
    public File take(String filename) {
        if (filename.contains("%ts")) {
            filename = filename.replace("%ts", new SimpleDateFormat(timestampFormat).format(new Date()));
        }
        if (filename.contains("%i")) {
            filename = filename.replace("%i", String.format("%03d", number));
            number++;
        }
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File outputFile = new File(screenshotFolder, filename);
        try {
            FileUtils.copyFile(scrFile, outputFile);
        } catch (IOException e) {
            throw new RuntimeException("Error while copying screenshot file", e);
        }
        return outputFile;
    }

    /**
     * Take a screenshot of the current browser view. Save the screenshot to the
     * file specified in property <code>webdriver.screenshot.filename</code>. <br>
     * <br>
     * The placeholder <code>%ts</code> will be replaced by the current timestamp.
     * The format can be changed with the
     * <code>webdriver.screenshot.timestamp.format</code> property, default is
     * <code>yyyy-MM-dd-HH-mm-ss</code>. <br>
     * The placeholder <code>%i</code> will be replaced with an ongoing number
     * prepended with zeros to match the length of 3.
     *
     * @return file of the created screenshot
     */
    public File take() {
        return take(defaultFilename);
    }

    /**
     * Take a screenshot of the current browser view. Save the screenshot to the
     * file specified in property <code>webdriver.screenshot.filename</code>, but
     * inside the specified folder.<br>
     * <br>
     * The placeholder <code>%ts</code> will be replaced by the current timestamp.
     * The format can be changed with the
     * <code>webdriver.screenshot.timestamp.format</code> property, default is
     * <code>yyyy-MM-dd-HH-mm-ss</code>. <br>
     * The placeholder <code>%i</code> will be replaced with an ongoing number
     * prepended with zeros to match the length of 3.
     *
     * @param foldername name of the folder to save the screenshot in
     * @return file of the created screenshot
     */
    public File takeInFolder(String foldername) {
        return take(foldername + "/" + defaultFilename);
    }
}
