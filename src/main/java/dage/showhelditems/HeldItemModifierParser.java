package dage.showhelditems;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Josh
 */

public class HeldItemModifierParser {

    public static class HeldItemModifier {
        private String LocatorName;
        private Map<String, Float> modifiers;

        public HeldItemModifier(String modifiedLocator, Map<String, Float> modifiers) {
            this.LocatorName = modifiedLocator;
            this.modifiers = modifiers;
        }

        public String getLocatorName() {
            return LocatorName;
        }

        public Map<String, Float> getModifiers() {
            return modifiers;
        }
    }

    public static HeldItemModifier parseHeldItemModifier(String input) {
        // Extract modifiedLocator
        String modifiedLocator = input.replaceFirst("^_null_", "").replaceAll("\\[.*\\]$", "");

        // Extract modifiers
        Map<String, Float> modifiers = new HashMap<>();
        int start = input.indexOf('[');
        int end = input.indexOf(']');

        if (start != -1 && end != -1 && end > start) {
            String modifierPart = input.substring(start + 1, end);
            String[] modifierPairs = modifierPart.split(",");

            for (String pair : modifierPairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    try {
                        Float value = Float.parseFloat(keyValue[1].trim());
                        modifiers.put(key, value);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format for: " + keyValue[1]);
                    }
                }
            }
        }

        return new HeldItemModifier(modifiedLocator, modifiers);
    }
}