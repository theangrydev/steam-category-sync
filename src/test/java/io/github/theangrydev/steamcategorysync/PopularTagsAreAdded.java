package io.github.theangrydev.steamcategorysync;

import io.github.theangrydev.yatspecfluent.ThenFactory;
import org.junit.Test;
import testinfrastructure.*;

import java.io.File;

public class PopularTagsAreAdded extends AcceptanceTest {

    private final GivenAnExistingConfiguration anExistingConfiguration = new GivenAnExistingConfiguration(testInfrastructure);
    private final GivenTagsOnSteamForApp theSteamAppPage = new GivenTagsOnSteamForApp(testInfrastructure);
    private final WhenTheToolIsRun theToolIsRun = new WhenTheToolIsRun(testInfrastructure);
    private final ThenFactory<ThenTheConfigFile, File> theConfigFile = ThenTheConfigFile::new;

    @Test
    public void popularTagsFromTheSteamStorePageAreAddedToMultipleApps() {
        given(anExistingConfiguration
                .inFile("sharedconfig.vdf")
                .withApp("41300", "All", "Incomplete", "Indie")
                .withApp("440", "All", "FPS")
                .withApp("439880"));
        and(theSteamAppPage.forAppWithId("41300").hasPopularTags("RPG", "Strategy", "Indie"));
        and(theSteamAppPage.forAppWithId("440").hasPopularTags("Action", "Online", "FPS"));
        and(theSteamAppPage.forAppWithId("439880").hasPopularTags("Adventure"));
        when(theToolIsRun.againstConfigFile("sharedconfig.vdf"));
        then(theConfigFile).hasAppWithTags("41300", "All", "Incomplete", "Indie", "RPG", "Strategy");
        and(theConfigFile).hasAppWithTags("440", "All", "FPS", "Action", "Online");
        and(theConfigFile).hasAppWithTags("439880", "Adventure");
        and(theConfigFile).hasSequentialTagsStartingAtZero();
    }
}
