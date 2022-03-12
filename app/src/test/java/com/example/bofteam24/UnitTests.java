package com.example.bofteam24;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTests {
    @Test
    public void testCleanCSVInput() {
        String csvInput = "uuid,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "2022,SP,CSE,110,Small";

        String[] expectedOutput = {"uuid", "", "", "", "Bill", "", "", "", "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0",
                "", "", "", "2021", "FA", "CSE", "210", "Small_2022", "WI", "CSE", "110", "Large_2022",
        "SP", "CSE", "110", "Small"};
        String[] actualOutput = ParseUtils.cleanCSVInput(csvInput);
        assertArrayEquals(actualOutput, expectedOutput);
    }

    @Test
    public void testGetFirstNameAndUrl() {
        String csvInput = "uuid,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "2022,SP,CSE,110,Small";

        String[] csvOutput = ParseUtils.cleanCSVInput(csvInput);

        String[] expectedOutput = {"Bill",  "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0"};
        String[] actualOutput = ParseUtils.getFirstNameAndUrl(csvOutput);

        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetAllCoursesInfo() {
        String csvInput = "uuid,,,,\n" +
                "Bill,,,,\n" +
                "https://lh3.googleusercontent.com/pw/AM-JKLXQ2ix4dg-PzLrPOSMOOy6M3PSUrijov9jCLXs4IGSTwN73B4kr-F6Nti_4KsiUU8LzDSGPSWNKnFdKIPqCQ2dFTRbARsW76pevHPBzc51nceZDZrMPmDfAYyI4XNOnPrZarGlLLUZW9wal6j-z9uA6WQ=w854-h924-no?authuser=0,,,,\n" +
                "2021,FA,CSE,210,Small\n" +
                "2022,WI,CSE,110,Large\n" +
                "2022,SP,CSE,110,Small";

        String[] csvOutput = ParseUtils.cleanCSVInput(csvInput);

        String[] expectedOutputArr = {"2021 FA CSE 210", "2022 WI CSE 110", "2022 SP CSE 110"};
        List<String> actualOutput = ParseUtils.getAllCoursesInfo(csvOutput);

        assertArrayEquals(expectedOutputArr, actualOutput.toArray());
    }

}