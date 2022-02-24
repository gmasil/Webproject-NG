package de.gmasil.webproject.utils.extension;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.gmasil.webproject.service.dataimport.DataImportService;

public class TestDataImportExtension implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        DataImportService service = SpringExtension.getApplicationContext(context).getBean(DataImportService.class);
        service.performDataImport();
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        DataImportService service = SpringExtension.getApplicationContext(context).getBean(DataImportService.class);
        service.performClean();
    }
}
