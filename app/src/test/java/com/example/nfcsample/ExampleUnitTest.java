package com.example.nfcsample;

import com.example.nfcsample.ReadCardFeatures.ToDec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(35, 33 + 2);
    }
    @Test
    public  void Test_toDec(){ long result= ToDec.toDec(new byte[]{-127});
    assertEquals(result,129);
    }



}