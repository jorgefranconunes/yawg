/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.core;

import com.varmateo.yawg.core.CollectiveDirBakeListener;
import com.varmateo.yawg.spi.DirBakeListener;
import com.varmateo.yawg.spi.OnDirBakeResult;
import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import com.varmateo.yawg.spi.PageVars;
import com.varmateo.yawg.spi.PageVarsBuilder;
import com.varmateo.yawg.util.OnDirBakeResults;
import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


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

        final MyListener listener01 = new MyListener("var01", "hello");
        final CollectiveDirBakeListener listener = new CollectiveDirBakeListener(
                List.of(listener01));
        final PageContext context = buildContext("var02", "world");

        assertThat(listener01.getEventCount()).isEqualTo(0);

        final PageVars vars = listener.onDirBake(context).pageVars();

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("hello");
        assertThat(vars.get("var02")).hasValue("world");
    }


    /**
     *
     */
    @Test
    public void oneListenerOverrideVar() {

        final MyListener listener01 = new MyListener("var01", "hello");
        final CollectiveDirBakeListener listener = new CollectiveDirBakeListener(
                List.of(listener01));
        final PageContext context = buildContext("var01", "world");

        assertThat(listener01.getEventCount()).isEqualTo(0);

        final PageVars vars = listener.onDirBake(context).pageVars();

        assertThat(listener01.getEventCount()).isEqualTo(1);
        assertThat(vars.get("var01")).hasValue("hello");
    }


    /**
     *
     */
    @Test
    public void twoListenersAddVar() {

        final MyListener listener01 = new MyListener("var01", "hello");
        final MyListener listener02 = new MyListener("var02", "world");
        final CollectiveDirBakeListener listener = new CollectiveDirBakeListener(
                List.of(listener01, listener02));
        final PageContext context = buildContext("var03", "VALUE03");

        assertThat(listener01.getEventCount()).isEqualTo(0);
        assertThat(listener02.getEventCount()).isEqualTo(0);

        final PageVars vars = listener.onDirBake(context).pageVars();

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

        final MyListener listener01 = new MyListener("var01", "hello");
        final MyListener listener02 = new MyListener("var01", "world");
        final CollectiveDirBakeListener listener = new CollectiveDirBakeListener(
                List.of(listener01, listener02));
        final PageContext context = buildContext("var03", "VALUE03");

        assertThat(listener01.getEventCount()).isEqualTo(0);
        assertThat(listener02.getEventCount()).isEqualTo(0);

        final PageVars vars = listener.onDirBake(context).pageVars();

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

        final MyListener listener01 = new MyListener("var01", "hello");
        final MyListener listener02 = new MyListener("var03", "world");
        final CollectiveDirBakeListener listener = new CollectiveDirBakeListener(
                List.of(listener01, listener02));
        final PageContext context = buildContext("var03", "VALUE03");

        assertThat(listener01.getEventCount()).isEqualTo(0);
        assertThat(listener02.getEventCount()).isEqualTo(0);

        final PageVars vars = listener.onDirBake(context).pageVars();

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

        return PageContextBuilder.create()
                .dirUrl(".")
                .rootRelativeUrl(".")
                .addVar(varName, varValue)
                .bakeId("TestBakeId")
                .build();
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
        public OnDirBakeResult onDirBake(final PageContext context) {

            final PageVars newVars = PageVarsBuilder.create(context.pageVars())
                    .addVar(_varName, _varValue)
                    .build();

            ++_eventCount;

            return OnDirBakeResults.success(newVars);
        }


        /**
         *
         */
        public int getEventCount() {

            return _eventCount;
        }


    }


}
