/**************************************************************************
 *
 * Copyright (c) 2016-2017 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg.spi;

import com.varmateo.yawg.spi.PageContext;
import com.varmateo.yawg.spi.PageContextBuilder;
import java.nio.file.Paths;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 *
 */
public final class PageContextBuilderTest {


    /**
     *
     */
    @Test
    public void missingMandatoryAttrs() {

        assertThatThrownBy(() -> PageContextBuilder.create().build())
                .isInstanceOf(NullPointerException.class);
    }


    /**
     *
     */
    @Test
    public void mandatoryAttrs() {

        PageContext context =
                PageContextBuilder.create()
                .setDirUrl("something")
                .setRootRelativeUrl("whatever")
                .build();

        assertThat(context.getDirUrl()).isEqualTo("something");
        assertThat(context.getRootRelativeUrl()).isEqualTo("whatever");
        assertThat(context.getTemplateFor(Paths.get("xxx"))).isNotPresent();
        assertThat(context.getPageVars().asMap()).isEmpty();
    }


}
