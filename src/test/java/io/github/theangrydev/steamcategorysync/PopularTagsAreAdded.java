package io.github.theangrydev.steamcategorysync;

import org.junit.Test;
import testinfrastructure.AcceptanceTest;
import testinfrastructure.GivenAnExistingConfiguration;
import testinfrastructure.GivenTagsOnSteamForApp;
import testinfrastructure.WhenTheToolIsRun;

public class PopularTagsAreAdded extends AcceptanceTest {

    private final GivenAnExistingConfiguration anExistingConfiguration = new GivenAnExistingConfiguration(testInfrastructure);
    private final GivenTagsOnSteamForApp theSteamAppPage = new GivenTagsOnSteamForApp(testInfrastructure);
    private final WhenTheToolIsRun theToolIsRun = new WhenTheToolIsRun(testInfrastructure);

    @Test
    public void popularTagsFromTheSteamStorePageAreAdded() {
        given(anExistingConfiguration
                .inFile("sharedconfig.vdf")
                .withApp("41300", "All", "Incomplete", "Indie")
                .withApp("440", "All", "FPS"));
        and(theSteamAppPage.forAppWithId("41300").hasPopularTags("RPG", "Strategy"));
        and(theSteamAppPage.forAppWithId("440").hasPopularTags("Action", "Online"));
        when(theToolIsRun.againstConfigFile("sharedconfig.vdf"));
    }
}
