package testinfrastructure;

import com.googlecode.yatspec.junit.SpecRunner;
import io.github.theangrydev.yatspecfluent.FluentTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(SpecRunner.class)
public abstract class AcceptanceTest extends FluentTest<File, File> {

    @Rule
    public TemporaryFolder configFolder = new TemporaryFolder();

    protected final TestInfrastructure testInfrastructure = new TestInfrastructure(this);

    @Before
    public void setUp() {
        testInfrastructure.setUp(configFolder.getRoot());
    }

    @After
    public void tearDown() {
        testInfrastructure.tearDown();
    }
}
