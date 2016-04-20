package com.example.v_wuwa.myapplication;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void se() throws Exception {
        for (int i=0;i<20;i++)
            EventSender.PostEventHub("{ 'DeviceId':'qqqqqqqqqqqqqqqqqqq', 'Temperature':'11111111111111111111111111111111111111111' }");
    }
}