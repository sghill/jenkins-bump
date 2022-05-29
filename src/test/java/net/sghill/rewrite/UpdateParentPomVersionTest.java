package net.sghill.rewrite;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openrewrite.test.RewriteTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class UpdateParentPomVersionTest implements RewriteTest {

    @Test
    @Disabled
    void shouldUpdateVersion() {
        UpdateParentPomVersion recipe = new UpdateParentPomVersion("org.jenkins-ci.plugins", "plugin", "4.40");
        rewriteRun(spec -> spec.recipe(recipe), pomXml(
                fromClasspath("parent-pom/match-before.xml"),
                fromClasspath("parent-pom/match-after.xml")
        ));
    }

    @Test
    void shouldLeaveStylingAlone() {

    }

    @Test
    void shouldNoOpIfParentIsDifferent() {

    }

    public static String fromClasspath(String path) {
        try (InputStream is = UpdateParentPomVersionTest.class.getResourceAsStream("/" + path);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader r = new BufferedReader(isr);
             Stream<String> lines = r.lines()) {
            return lines.collect(Collectors.joining(String.format("%n")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
