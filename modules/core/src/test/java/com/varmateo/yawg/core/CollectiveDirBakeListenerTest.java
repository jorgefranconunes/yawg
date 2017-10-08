/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import io.vavr.collection.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import com.varmateo.yawg.core.CollectiveDirBakeListener;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageVars;


/**
 *
 */
public final class CollectiveDirBakeListenerTest
 {


    /**
     *
     */
    @Test
    public void oneListenerAddVar() {

        MyListener listener01 = new MyListener("var01", "hello");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(List.of(listener01));
        PageContext context = buildContext("var02", "world");

        assertThat(listener01.getEventCount()).isEqualTo(0);

        PageVars vars = listener.onDirBake(context);

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("hello");
        assertThat(vars.get("var02")).hasValue("world");
    }


    /**
     *
     */
    @Test
    public void oneListenerOverrideVar() {

        MyListener listener01 = new MyListener("var01", "hello");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(List.of(listener01));
        PageContext context = buildContext("var01", "world");

        assertThat(listener01.getEventCount()).isEqualTo(0);

        PageVars vars = listener.onDirBake(context);

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("hello");
    }


    /**
     *
     */
    @Test
    public void twoListenersAddVar() {

        MyListener listener01 = new MyListener("var01", "hello");
        MyListener listener02 = new MyListener("var02", "world");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(
                        List.of(listener01, listener02));
        PageContext context = buildContext("var03", "VALUE03");

        assertThat(listener01.getEventCount()).isEqualTo(0);
        assertThat(listener02.getEventCount()).isEqualTo(0);

        PageVars vars = listener.onDirBake(context);

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(listener02.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("hello");
        assertThat(vars.get("var02")).hasValue("world");
        assertThat(vars.get("var03")).hasValue("VALUE03");
    }


    /**
     *
     */
    @Test
    public void twoListenersOverrideVar01() {

        MyListener listener01 = new MyListener("var01", "hello");
        MyListener listener02 = new MyListener("var01", "world");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(
                        List.of(listener01, listener02));
        PageContext context = buildContext("var03", "VALUE03");

        assertThat(listener01.getEventCount()).isEqualTo(0);
        assertThat(listener02.getEventCount()).isEqualTo(0);

        PageVars vars = listener.onDirBake(context);

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(listener02.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("world");
        assertThat(vars.get("var03")).hasValue("VALUE03");
    }


    /**
     *
     */
    @Test
    public void twoListenersOverrideVar02() {

        MyListener listener01 = new MyListener("var01", "hello");
        MyListener listener02 = new MyListener("var03", "world");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(
                        List.of(listener01, listener02));
        PageContext context = buildContext("var03", "VALUE03");

        assertThat(listener01.getEventCount()).isEqualTo(0);
        assertThat(listener02.getEventCount()).isEqualTo(0);

        PageVars vars = listener.onDirBake(context);

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(listener02.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("hello");
        assertThat(vars.get("var03")).hasValue("world");
    }


    /**
     *
     */
    private PageContext buildContext(
            final String varName,
            final String varValue) {

        PageContext context =
                PageContext.builder()
                .setDirUrl(".")
                .setRootRelativeUrl(".")
                .addVar(varName, varValue)
                .build();

        return context;
    }


    /**
     *
     */
    private static final class MyListener
            implements DirBakeListener {


        private final String _varName;
        private final String _varValue;
        private int _eventCount;


        /**
         *
         */
        public MyListener(
                final String varName,
                final String varValue) {

            _varName = varName;
            _varValue = varValue;
            _eventCount = 0;
        }


        /**
         *
         */
        @Override
        public PageVars onDirBake(final PageContext context) {

            PageVars newVars =
                    PageVars.builder(context.getPageVars())
                    .addVar(_varName, _varValue)
                    .build();

            ++_eventCount;

            return newVars;
        }


        /**
         *
         */
        public int getEventCount() {

            return _eventCount;
        }


    }


}
