//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MoodDetector {
    private static final Map<String, List<String>> moodKeywords = new HashMap();

    public MoodDetector() {
    }

    public static String detect(String text) {
        text = text.toLowerCase();
        String detectedMood = "Calm";
        int maxCount = 0;
        Iterator var3 = moodKeywords.keySet().iterator();

        while(var3.hasNext()) {
            String mood = (String)var3.next();
            int count = 0;
            Iterator var6 = ((List)moodKeywords.get(mood)).iterator();

            while(var6.hasNext()) {
                String word = (String)var6.next();
                if (text.contains(word)) {
                    ++count;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                detectedMood = mood;
            }
        }

        return detectedMood;
    }

    static {
        moodKeywords.put("Happy", Arrays.asList("happy", "good", "great", "joy", "excited"));
        moodKeywords.put("Sad", Arrays.asList("sad", "down", "depressed", "unhappy"));
        moodKeywords.put("Angry", Arrays.asList("angry", "mad", "furious"));
        moodKeywords.put("Calm", Arrays.asList("calm", "relax", "peace"));
        moodKeywords.put("Energetic", Arrays.asList("gym", "workout", "pump", "energetic"));
    }
}
