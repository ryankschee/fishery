package com.ryanworks.fishery.shared.util;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ImageFileFilter 
    extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = FileUtil.getExtension(f);
        if (extension != null) {
            if (extension.equals(FileUtil.tiff) ||
                extension.equals(FileUtil.tif) ||
                extension.equals(FileUtil.gif) ||
                extension.equals(FileUtil.jpeg) ||
                extension.equals(FileUtil.jpg) ||
                extension.equals(FileUtil.png)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        return "Image Only";
    }
}
