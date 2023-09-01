package org.kie.pmml.commons.model.expressions;

import org.junit.jupiter.api.Test;
import org.kie.pmml.api.enums.CLOSURE;

import static org.assertj.core.api.Assertions.assertThat;

public class KiePMMLIntervalTest {

    @Test
    void isIn() {
        KiePMMLInterval kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.OPEN_OPEN);
        assertThat(kiePMMLInterval.isIn(30)).isTrue();
        assertThat(kiePMMLInterval.isIn(10)).isFalse();
        assertThat(kiePMMLInterval.isIn(20)).isFalse();
        assertThat(kiePMMLInterval.isIn(40)).isFalse();
        assertThat(kiePMMLInterval.isIn(50)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.OPEN_CLOSED);
        assertThat(kiePMMLInterval.isIn(30)).isTrue();
        assertThat(kiePMMLInterval.isIn(10)).isFalse();
        assertThat(kiePMMLInterval.isIn(20)).isFalse();
        assertThat(kiePMMLInterval.isIn(40)).isTrue();
        assertThat(kiePMMLInterval.isIn(50)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.CLOSED_OPEN);
        assertThat(kiePMMLInterval.isInsideClosedOpen(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(10)).isFalse();
        assertThat(kiePMMLInterval.isInsideClosedOpen(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(40)).isFalse();
        assertThat(kiePMMLInterval.isInsideClosedOpen(50)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.CLOSED_CLOSED);
        assertThat(kiePMMLInterval.isIn(30)).isTrue();
        assertThat(kiePMMLInterval.isIn(10)).isFalse();
        assertThat(kiePMMLInterval.isIn(20)).isTrue();
        assertThat(kiePMMLInterval.isIn(40)).isTrue();
        assertThat(kiePMMLInterval.isIn(50)).isFalse();
    }

    @Test
    void isInsideOpenOpen() {
        KiePMMLInterval kiePMMLInterval = new KiePMMLInterval(null, 20, CLOSURE.OPEN_OPEN);
        assertThat(kiePMMLInterval.isInsideOpenOpen(10)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenOpen(20)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenOpen(30)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, null, CLOSURE.OPEN_OPEN);
        assertThat(kiePMMLInterval.isInsideOpenOpen(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenOpen(20)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenOpen(10)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.OPEN_OPEN);
        assertThat(kiePMMLInterval.isInsideOpenOpen(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenOpen(10)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenOpen(20)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenOpen(40)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenOpen(50)).isFalse();
    }

    @Test
    void isInsideOpenClosed() {
        KiePMMLInterval kiePMMLInterval = new KiePMMLInterval(null, 20, CLOSURE.OPEN_CLOSED);
        assertThat(kiePMMLInterval.isInsideOpenClosed(10)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenClosed(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenClosed(30)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, null, CLOSURE.OPEN_CLOSED);
        assertThat(kiePMMLInterval.isInsideOpenClosed(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenClosed(20)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenClosed(10)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.OPEN_CLOSED);
        assertThat(kiePMMLInterval.isInsideOpenClosed(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenClosed(10)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenClosed(20)).isFalse();
        assertThat(kiePMMLInterval.isInsideOpenClosed(40)).isTrue();
        assertThat(kiePMMLInterval.isInsideOpenClosed(50)).isFalse();
    }

    @Test
    void isInsideClosedOpen() {
        KiePMMLInterval kiePMMLInterval = new KiePMMLInterval(null, 20, CLOSURE.CLOSED_OPEN);
        assertThat(kiePMMLInterval.isInsideClosedOpen(10)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(20)).isFalse();
        assertThat(kiePMMLInterval.isInsideClosedOpen(30)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, null, CLOSURE.CLOSED_OPEN);
        assertThat(kiePMMLInterval.isInsideClosedOpen(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(10)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.CLOSED_OPEN);
        assertThat(kiePMMLInterval.isInsideClosedOpen(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(10)).isFalse();
        assertThat(kiePMMLInterval.isInsideClosedOpen(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedOpen(40)).isFalse();
        assertThat(kiePMMLInterval.isInsideClosedOpen(50)).isFalse();
    }

    @Test
    void isInsideClosedClosed() {
        KiePMMLInterval kiePMMLInterval = new KiePMMLInterval(null, 20, CLOSURE.CLOSED_CLOSED);
        assertThat(kiePMMLInterval.isInsideClosedClosed(10)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(30)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, null, CLOSURE.CLOSED_CLOSED);
        assertThat(kiePMMLInterval.isInsideClosedClosed(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(10)).isFalse();
        kiePMMLInterval = new KiePMMLInterval(20, 40, CLOSURE.CLOSED_CLOSED);
        assertThat(kiePMMLInterval.isInsideClosedClosed(30)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(10)).isFalse();
        assertThat(kiePMMLInterval.isInsideClosedClosed(20)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(40)).isTrue();
        assertThat(kiePMMLInterval.isInsideClosedClosed(50)).isFalse();
    }
}