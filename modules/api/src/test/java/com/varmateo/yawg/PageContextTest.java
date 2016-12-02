/**************************************************************************
 *
 * Copyright (c) 2016 Yawg project contributors.
 *
 **************************************************************************/

package com.varmateo.yawg;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.varmateo.testutils.TestUtils;

import com.varmateo.yawg.PageContext;


/**
 *
 */
public final class PageContextTest
 {


    /**
     *
     */
    @Test
    public void missingMandatoryAttrs() {

        assertThatThrownBy(() -> PageContext.builder().build())
                .isInstanceOf(NullPointerException.class);
    }


    /**
     *
     */
    @Test
    public void mandatoryAttrs() {

        PageContext context =
                PageContext.builder()
                .setDirUrl("something")
                .setRootRelativeUrl("whatever")
                .build();

        assertThat(context.getDirUrl()).isEqualTo("something");
        assertThat(context.getRootRelativeUrl()).isEqualTo("whatever");
        assertThat(context.getTemplateFor(Paths.get("xxx"))).isNotPresent();
        assertThat(context.getPageVars().asMap()).isEmpty();
    }


}
