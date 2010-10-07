package com.agical.bumblebee.ruby;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URL;

import org.junit.After;
import org.junit.Test;

public class RubyCollectorBehaviour {
    private PrintStream errPrintStream = System.err;

    @Test public void onNonExistingSourcePathesConstructorShouldPrintWarning() throws Exception {
        File file = new File("nonexisting");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(byteArrayOutputStream);
        System.setErr(err);
        new RubyCollector(new File[]{file}, new URL[]{} );
        assertTrue(new String(byteArrayOutputStream.toByteArray()).contains("** WARNING! Configured but non-existing source root: " + file.getAbsolutePath() ) );
    }

    @After public void resetPrintStream() throws Exception {
        System.setErr(errPrintStream);
    }
}
