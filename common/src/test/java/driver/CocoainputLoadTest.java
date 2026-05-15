package driver;
import org.junit.jupiter.api.Test;

import com.hyfata.najoan.koreanpatch.driver.InputController;
import static org.junit.jupiter.api.Assertions.*;

public class CocoainputLoadTest {
    @Test
    void newInputController() {
        InputController inputController = InputController.newController();
        assertNotNull(inputController);
    }
}