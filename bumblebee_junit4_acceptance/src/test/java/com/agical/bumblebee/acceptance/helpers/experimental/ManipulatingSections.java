package com.agical.bumblebee.acceptance.helpers.experimental;

import org.junit.Test;

public class ManipulatingSections {

    @Test
    public void includeThisSectionInOtherSection() throws Exception {
        /*!
        This text should be included from another test.
        */
    }

    @Test
    public void includeOtherSectionHere() throws Exception {
         /*!
         #{include_node_content {|node| node.to_s.include?('includeThisSectionInOtherSection')}}
         Below there should be the content from the section above:
         #{assert.contains 'This text should be included from another test.', 'Text can be copied from one test (node) to the other'}
         */

    }

}
