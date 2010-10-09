BUMBLEBEE_VERSION = '1.1.1'

UMLSPEED_NAME='umlspeed:umlspeed:jar:0.19'
MUSE_NAME='oqube.muse:muse-parser:jar:1.0-rc3'
LIFL_NAME='fr.lifl:parsing:jar:1.0.1'
JHIGHLIGHT_NAME='jhighlight:jhighlight:jar:1.0'

MUSE_PARSER = [
	MUSE_NAME,
    'commons-collections:commons-collections:jar:3.1',
    'commons-logging:commons-logging:jar:1.0.4']
BATIK = [
    'batik:batik-rasterizer:jar:1.6', 
    'batik:batik-awt-util:jar:1.6',
    'batik:batik-bridge:jar:1.6',
    'batik:batik-css:jar:1.6',
    'batik:batik-dom:jar:1.6',
    'batik:batik-ext:jar:1.6',
    'batik:batik-gvt:jar:1.6',
    'batik:batik-parser:jar:1.6',
    'batik:batik-script:jar:1.6',
    'batik:batik-svg-dom:jar:1.6',
    'batik:batik-transcoder:jar:1.6',
    'batik:batik-util:jar:1.6',
    'batik:batik-xml:jar:1.6',
    'xerces:xercesImpl:jar:2.5.0',
    'xml-apis:xmlParserAPIs:jar:2.0.2']
PMD = [
    'pmd:pmd:jar:4.2.4',
    'jaxen:jaxen:jar:1.1.1',
    'asm:asm:jar:3.1']
JUNIT_4_4  = ['junit:junit:jar:4.4']

JUNIT_4_5 = ['junit:junit:jar:4.5']

JUNIT_4_6 = ['junit:junit:jar:4.6']

JUNIT = JUNIT_4_6

JRUBY = ['org.jruby:jruby-complete:jar:1.1']

SELENIUM_OLD = [
    'org.openqa.selenium.client-drivers:selenium-java-client-driver:jar:0.9.2',
    'org.openqa.selenium.server:selenium-server:jar:0.9.2',
    'org.openqa.selenium.server:selenium-server:jar:standalone:0.9.2',
    'org.openqa.selenium.core:selenium-core:jar:0.8.3',
    'org.openqa.selenium.server:selenium-server-coreless:jar:0.9.2']

SELENIUM_1_0 = [
    'org.seleniumhq.selenium.client-drivers:selenium-java-client-driver:jar:1.0.1',
    'org.seleniumhq.selenium.server:selenium-server:jar:1.0.1',
    'org.seleniumhq.selenium.server:selenium-server:jar:standalone:1.0.1',
    'org.seleniumhq.selenium.core:selenium-core:jar:1.0.1',
    'org.seleniumhq.selenium.server:selenium-server-coreless:jar:1.0.1']

SELENIUM_2_0 = [
    'org.seleniumhq.selenium:selenium:jar:2.0a5',
    'org.seleniumhq.selenium:selenium-common:jar:2.0a5',
    'org.seleniumhq.selenium:selenium-chrome-driver:jar:2.0a5',
    'org.seleniumhq.selenium:selenium-server:jar:2.0a5']

SELENIUM = SELENIUM_2_0
JETTY = ['jetty:jetty:jar:5.1.10']
    
BUMBLEBEE = ['com.agical.bumblebee:bumblebee:jar:' + BUMBLEBEE_VERSION]
BUMBLEBEE_JUNIT4 = ['com.agical.bumblebee:bumblebee_junit4:jar:' + BUMBLEBEE_VERSION]
BUMBLEBEE_JRUBY = ['com.agical.bumblebee:bumblebee_jruby:jar:' + BUMBLEBEE_VERSION]
BUMBLEBEE_SWING = ['com.agical.bumblebee:bumblebee_swing:jar:' + BUMBLEBEE_VERSION]
BUMBLEBEE_SELENIUM = ['com.agical.bumblebee:bumblebee_selenium:jar:' + BUMBLEBEE_VERSION]
BUMBLEBEE_UML = ['com.agical.bumblebee:bumblebee_uml:jar:' + BUMBLEBEE_VERSION]
JUNITALT = ['com.agical.bumblebee:junitalt:jar:' + BUMBLEBEE_VERSION]


