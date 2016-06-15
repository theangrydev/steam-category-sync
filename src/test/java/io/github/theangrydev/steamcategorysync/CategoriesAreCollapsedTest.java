package io.github.theangrydev.steamcategorysync;

import io.github.theangrydev.yatspecfluent.ThenFactory;
import org.junit.Test;
import testinfrastructure.*;

import java.io.File;

public class CategoriesAreCollapsedTest extends AcceptanceTest {

    private final GivenAUserConfigData anExistingConfiguration = new GivenAUserConfigData(testInfrastructure);
    private final WhenTheCategoryCollapseToolIsRun theToolIsRun = new WhenTheCategoryCollapseToolIsRun(testInfrastructure);
    private final ThenFactory<ThenTheUserConfigData, File> theConfigFile = ThenTheUserConfigData::new;

    @Test
    public void categoriesAreCollapsed() {
        given(anExistingConfiguration
                .inFile("DialogConfig.vdf")
                .withCategory("Indie", "0")
                .withCategory("RPG", "0")
                .withCategory("Action", "1"));
        when(theToolIsRun.againstConfigFile("DialogConfig.vdf"));
        then(theConfigFile).hasAllCategoriesCollapsed();
        and(theConfigFile).hasBackupCopyWithName("DialogConfig.vdf.bak");
    }
}
