package driver;
import org.junit.jupiter.api.Test;

import com.hyfata.najoan.koreanpatch.driver.InputController;

public class CocoainputLoadTest {
    @Test
    void newInputController() {
        InputController inputController = InputController.newController();
        assertNotNull(inputController);
    }
}
