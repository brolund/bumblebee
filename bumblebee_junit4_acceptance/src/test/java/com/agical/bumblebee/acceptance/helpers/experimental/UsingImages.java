package com.agical.bumblebee.acceptance.helpers.experimental;

import org.junit.Test;

public class UsingImages {
    /*!!
    By adding images to your document the whole experience gets more vibrant.
    
    Since Bumblebee can include runtime data, also runtime data such as screen-shots 
    and similar can be extracted. The goal of this package is that it also should be easy to add
    information such as explanations and balloons as overlays in the images. 
    */
    
    @Test
    public void addAnImageToYourDocument() throws Exception {
        /*!
        #{copy('src/main/resources/images/bumblebee.jpg', 'target/site/images/bumblebee.jpeg');''}
        Images are included using the link tag:
        >>>>
        [[images/bumblebee.jpeg][]]
        <<<<
        [[images/bumblebee.jpeg][]]
        
        The site will by default be placed in =target/site=, hence images with relative URLs 
        should reside there or in a sub folder.

        The wiki-parser recognizes images of types =jpeg=  ( *not* jpg) and =png= and possibly some more formats.
        
        Copying of resources are handled in 
        [[#com.agical.bumblebee.acceptance.resources.Configuration.imagesAndOtherResources][Images and other resources]]
        */
    }
    
    @Test
    public void creatingImagesFromASwingGui() throws Exception {
        /*!
        Bumblebee can be used to include runtime data into the document, and snapshots 
        of GUI components is no exception.
        */
    }
}
