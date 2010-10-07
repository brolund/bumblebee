package com.agical.bumblebee.junit4.runners.helpers;

import static org.junit.Assert.fail;

import org.junit.Test;

public class Failure {
    @Test
    public void failure() throws Exception {
        fail("Failing on purpose");
    }
}
