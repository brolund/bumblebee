package com.agical.bumblebee.acceptance.defaults;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.agical.bumblebee.collector.BumblebeeCollectors;
import com.agical.bumblebee.junit4.BumbleBeeSuiteRunner;
import com.agical.bumblebee.ruby.RubyCollector;

@RunWith(BumbleBeeSuiteRunner.class)
@SuiteClasses({DefaultCase.class})
@BumblebeeCollectors({RubyCollector.class})
public class DefaultTesting {
    /*!!
    #{assert.equals 'target/site/documentation.html', configuration.target_file, 'There is a default target file configured'}
    #{assert.equals 'Copyright', configuration.copyright, 'There is a sensible copyright'}
    #{assert.equals Time.new.year, configuration.inception_year, 'The default inception year is this year'}
    #{assert.equals 'Documentation', configuration.document_title, 'There is a sensible title default'}
    */
}
