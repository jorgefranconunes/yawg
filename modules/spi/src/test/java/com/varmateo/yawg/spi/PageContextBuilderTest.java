/**************************************************************************
 *
 * Copyright (c) 2016-2019 Yawg project contributors.
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

        final PageContext context = PageContextBuilder.create()
                .setDirUrl("something")
                .setRootRelativeUrl("whatever")
                .build();

        assertThat(context.dirUrl()).isEqualTo("something");
        assertThat(context.rootRelativeUrl()).isEqualTo("whatever");
        assertThat(context.templateFor(Paths.get("xxx"))).isNotPresent();
        assertThat(context.pageVars().asMap()).isEmpty();
    }


}
