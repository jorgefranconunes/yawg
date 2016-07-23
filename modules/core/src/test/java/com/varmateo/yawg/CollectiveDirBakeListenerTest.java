/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import com.varmateo.yawg.CollectiveDirBakeListener;
import com.varmateo.yawg.DirBakeListener;
import com.varmateo.yawg.PageContext;
import com.varmateo.yawg.TemplateVars;


/**
 *
 */
public final class CollectiveDirBakeListenerTest
        extends Object {


    /**
     *
     */
    @Test
    public void oneListenerAddVar() {

        MyListener listener01 = new MyListener("var01", "hello");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(Arrays.asList(listener01));
        PageContext context = buildContext("var02", "world");

        assertEquals(0, listener01.getEventCount());

        TemplateVars vars = listener.onDirBake(context);

        assertEquals(1, listener01.getEventCount());
        assertEquals("hello", vars.get("var01").get());
        assertEquals("world", vars.get("var02").get());
    }


    /**
     *
     */
    @Test
    public void oneListenerOverrideVar() {

        MyListener listener01 = new MyListener("var01", "hello");
        CollectiveDirBakeListener listener =
                new CollectiveDirBakeListener(Arrays.asList(listener01));
        PageContext context = buildContext("var01", "world");

        assertEquals(0, listener01.getEventCount());

        TemplateVars vars = listener.onDirBake(context);

        assertEquals(1, listener01.getEventCount());
        assertEquals("hello", vars.get("var01").get());
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
                        Arrays.asList(listener01, listener02));
        PageContext context = buildContext("var03", "VALUE03");

        assertEquals(0, listener01.getEventCount());
        assertEquals(0, listener02.getEventCount());

        TemplateVars vars = listener.onDirBake(context);

        assertEquals(1, listener01.getEventCount());
        assertEquals(1, listener02.getEventCount());
        assertEquals("hello", vars.get("var01").get());
        assertEquals("world", vars.get("var02").get());
        assertEquals("VALUE03", vars.get("var03").get());
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
                        Arrays.asList(listener01, listener02));
        PageContext context = buildContext("var03", "VALUE03");

        assertEquals(0, listener01.getEventCount());
        assertEquals(0, listener02.getEventCount());

        TemplateVars vars = listener.onDirBake(context);

        assertEquals(1, listener01.getEventCount());
        assertEquals(1, listener02.getEventCount());
        assertEquals("world", vars.get("var01").get());
        assertEquals("VALUE03", vars.get("var03").get());
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
                        Arrays.asList(listener01, listener02));
        PageContext context = buildContext("var03", "VALUE03");

        assertEquals(0, listener01.getEventCount());
        assertEquals(0, listener02.getEventCount());

        TemplateVars vars = listener.onDirBake(context);

        assertEquals(1, listener01.getEventCount());
        assertEquals(1, listener02.getEventCount());
        assertEquals("hello", vars.get("var01").get());
        assertEquals("world", vars.get("var03").get());
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
            extends Object
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
        public TemplateVars onDirBake(final PageContext context) {

            TemplateVars newVars =
                    TemplateVars.builder(context.templateVars)
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
