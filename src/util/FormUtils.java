package util;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JTextField;

public class FormUtils {	
    public static boolean validateForm(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField && ((JTextField)comp).getText().trim().isEmpty()) {
                return false;
            }
            if (comp instanceof Container container1) {
                if (!validateForm(container1)) {
                    return false;
                }
            }
        }
        return true;
    }
}
