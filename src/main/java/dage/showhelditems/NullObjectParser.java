package dage.showhelditems;

import java.util.HashMap;
import java.util.Map;


/**
 * Uses the name of null objects in a model to get modifier values for a locator.
 * @author Josh
 */
public class NullObjectParser {

    /**
     * Stores modifiers for a particular locator.
     */
    public static class NullObjectModifier {
        private final String LocatorName;
        private final Map<String, Float> modifiers;

        public NullObjectModifier(String modifiedLocator, Map<String, Float> modifiers) {
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

    public static NullObjectModifier parseNullObject(String input) {
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

        return new NullObjectModifier(modifiedLocator, modifiers);
    }
}