package testinfrastructure;

import io.github.theangrydev.yatspecfluent.FluentTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;

public class AcceptanceTest extends FluentTest<File, File> {

    @Rule
    public TemporaryFolder configFolder = new TemporaryFolder();

    protected final TestInfrastructure testInfrastructure = new TestInfrastructure();

    @Before
    public void setUp() {
        testInfrastructure.setUp(configFolder.getRoot());
    }

    @After
    public void tearDown() {
        testInfrastructure.tearDown();
    }
}
