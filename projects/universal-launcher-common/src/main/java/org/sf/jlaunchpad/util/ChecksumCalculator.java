package org.sf.jlaunchpad.util;

import java.io.File;

public class ChecksumCalculator {

  public void calculate(File file) {
    if (file.isDirectory()) {
      calculateForDir(file);
    }
    else if (file.isFile()) {
      calculateForFile(file);
    }
  }

  public void calculateForDir(File dir) {
    File[] files = dir.listFiles();

    for (File file : files) {
      calculate(file);
    }
  }

  public void calculateForFile(File file) {
    String ext = FileUtil.getExtension(file);

    if (ext != null && !ext.contains("md5") && !ext.contains("sha")) {
      Checksum checksum = new Checksum();

      checksum.setFile(file);
      checksum.setForceOverwrite(false);

      checksum.setAlgorithm("sha1");

      if (!new File(file.getPath() + ".sha1").exists() &&
        !new File(file.getPath() + ".SHA1").exists()) {
        checksum.execute();
      }

      checksum.setAlgorithm("md5");

      if (!new File(file.getPath() + ".md5").exists() &&
        !new File(file.getPath() + ".MD5").exists()) {
        checksum.execute();
      }
    }
  }

}
