
package androiddatabase.wallj.com.androiddatabase;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the com.wallj.androiddatabase.app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("androiddatabase.wallj.com.androiddatabase", appContext.getPackageName());
    }
}
