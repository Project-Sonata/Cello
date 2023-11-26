package com.odeyalo.sonata.cello.support;

import com.odeyalo.sonata.cello.support.io.RxIoReactiveFileReader;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathLoginPageLoaderTest {
    final String HTML_CONTENT = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Testable page</title>
            </head>
            <body>
            <p>I LOVE MIKU <3</p>
            </body>
            </html>""".replaceAll("\n", "")
            .replaceAll("\r", "");
    final String HTML_PAGE_PATH = "src/test/resources/static/test.html";

    @Test
    void shouldReadFromFileAndReturnAsFlux() {
        var testable = new PathLoginPageLoader(HTML_PAGE_PATH, new RxIoReactiveFileReader());

        List<String> strings = testable.getLoginPage()
                .map((buffer -> new String(buffer.array())))
                .collectList()
                .block();

        assertThat(strings).isNotNull();

        String actualHtmlContent = String.join("", strings)
                .replaceAll("\n", "")
                .replaceAll("\r", ""); // Replace because contents have difference only in separators, we don't care about separators

        assertThat(actualHtmlContent).isEqualTo(HTML_CONTENT);
    }

    @Test
    void shouldFailFastIfFilePathNotExist() {
        assertThatThrownBy(() -> new PathLoginPageLoader("not_exist.html", new RxIoReactiveFileReader()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Invalid path to html file was provided");
    }
}