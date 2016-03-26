package com.jaeckel.etherwallet;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class EtherFormatterTest {

    @Test
    public void testFormatEther() throws Exception {
        assertThat(EtherFormatter.formatWeiAsEther(2000000000000000000L), is("2.00ETH"));
    }

    @Test
    public void testFormatEtherFraction() throws Exception {
        assertThat(EtherFormatter.formatWeiAsEther(2110000000000000000L), is("2.11ETH"));
    }

    @Test
    public void testFormatEtherFraction2() throws Exception {
        assertThat(EtherFormatter.formatWeiAsEther(2119000000000000000L), is("2.12ETH"));
    }
}
