package org.sf.pomreader;


import org.apache.maven.bootstrap.download.AbstractArtifactResolver;
import org.apache.maven.bootstrap.download.DownloadFailedException;
import org.apache.maven.bootstrap.download.HttpUtils;
import org.apache.maven.bootstrap.download.RepositoryMetadata;
import org.apache.maven.bootstrap.model.Dependency;
import org.apache.maven.bootstrap.model.Model;
import org.apache.maven.bootstrap.model.Repository;
import org.apache.maven.bootstrap.util.FileUtils;
import org.apache.maven.bootstrap.util.StringUtils;

import java.io.*;
import java.util.*;

public class OnlineArtifactDownloader
        extends AbstractArtifactResolver {
  public static final String SNAPSHOT_SIGNATURE = "-SNAPSHOT";

  private boolean useTimestamp = true;

  private boolean ignoreErrors = false;

  private String proxyHost;

  private String proxyPort;

  private String proxyUserName;

  private String proxyPassword;

  //private static final String REPO_URL = "http://repo1.maven.org/maven2";

  private Map<String, Dependency> downloadedArtifacts = new HashMap<String, Dependency>();

  private List<Repository> remoteRepositories;

  public OnlineArtifactDownloader(Repository localRepository)
          throws Exception {
    super(localRepository);
  }

  public void setProxy(String host, String port, String userName, String password) {
    proxyHost = host;
    proxyPort = port;
    proxyUserName = userName;
    proxyPassword = password;
    //System.out.println( "Using the following proxy : " + proxyHost + "/" + proxyPort );
  }

  public void downloadDependencies(Collection dependencies)
          throws DownloadFailedException {
    for (Object dependency : dependencies) {
      Dependency dep = (Dependency) dependency;

      if (isAlreadyBuilt(dep)) {
        continue;
      }

      String dependencyConflictId = dep.getDependencyConflictId();
      if (!downloadedArtifacts.containsKey(dependencyConflictId)) {
        File destinationFile = getLocalRepository().getArtifactFile(dep);
        // The directory structure for this project may
        // not exists so create it if missing.
        File directory = destinationFile.getParentFile();

        if (!directory.exists()) {
          directory.mkdirs();
        }

        if (!getRemoteArtifact(dep, destinationFile) && !destinationFile.exists()) {
          throw new DownloadFailedException(dep);
        }

        downloadedArtifacts.put(dependencyConflictId, dep);
      } else {
        Dependency d = downloadedArtifacts.get(dependencyConflictId);
        dep.setResolvedVersion(d.getResolvedVersion());
      }
    }
  }

  public boolean isOnline() {
    return true;
  }

  private static boolean isSnapshot(Dependency dep) {
    // Assume managed snapshot
    if (dep == null || dep.getGroupId().startsWith("org.apache.maven")) {
      return false;

    }

    // Assume managed snapshot
    if (dep.getVersion() == null) {
      return false;
    }

    return dep.getVersion().indexOf(SNAPSHOT_SIGNATURE) >= 0;
  }

  private boolean getRemoteArtifact(Dependency dep, File destinationFile) {
    boolean fileFound = false;

    List<Object> repositories = new ArrayList<Object>();
    repositories.addAll(getRemoteRepositories());
    repositories.addAll(dep.getRepositories());

    for (Object o : dep.getChain()) {
      repositories.addAll(((Model) o).getRepositories());
    }

    for (Object repository : repositories) {
      Repository remoteRepo = (Repository) repository;

      boolean snapshot = isSnapshot(dep);
      if (snapshot && !remoteRepo.isSnapshots()) {
        continue;
      }
      if (!snapshot && !remoteRepo.isReleases()) {
        continue;
      }

      // The username and password parameters are not being used here.
      String url = remoteRepo.getBasedir() + "/" + remoteRepo.getArtifactPath(dep);

      // Attempt to retrieve the artifact and set the checksum if retrieval
      // of the checksum file was successful.
      try {
        String version = dep.getVersion();
        if (snapshot) {
          String filename = "maven-metadata-" + remoteRepo.getId() + ".xml";
          File localFile = getLocalRepository().getMetadataFile(dep.getGroupId(), dep.getArtifactId(),
                  dep.getVersion(), dep.getType(),
                  "maven-metadata-local.xml");
          File remoteFile = getLocalRepository().getMetadataFile(dep.getGroupId(), dep.getArtifactId(),
                  dep.getVersion(), dep.getType(), filename);
          String metadataPath = remoteRepo.getMetadataPath(dep.getGroupId(), dep.getArtifactId(),
                  dep.getVersion(), dep.getType(),
                  "maven-metadata.xml");
          String metaUrl = remoteRepo.getBasedir() + "/" + metadataPath;
          log("Downloading " + metaUrl);


          if (metaUrl.startsWith("file://")) {
            loadFileLocally(metaUrl, remoteFile);
          }
          else {
          try {
            HttpUtils.getFile(metaUrl, remoteFile, ignoreErrors, true, proxyHost, proxyPort, proxyUserName,
                    proxyPassword, false);
          }
          catch (IOException e) {
            log("WARNING: remote metadata version not found, using local: " + e.getMessage());
            remoteFile.delete();
          }
          }

          File file = localFile;
          if (remoteFile.exists()) {
            if (!localFile.exists()) {
              file = remoteFile;
            }
            else {
              RepositoryMetadata localMetadata = RepositoryMetadata.read(localFile);

              RepositoryMetadata remoteMetadata = RepositoryMetadata.read(remoteFile);

              if (remoteMetadata.getLastUpdatedUtc() > localMetadata.getLastUpdatedUtc()) {
                file = remoteFile;
              }
              else {
                file = localFile;
              }
            }
          }

          if (file.exists()) {
            log("Using metadata: " + file);

            RepositoryMetadata metadata = RepositoryMetadata.read(file);

            if (!file.equals(localFile)) {
              version = metadata.constructVersion(version);
            }
            log("Resolved version: " + version);
            dep.setResolvedVersion(version);
            if (!version.endsWith("SNAPSHOT")) {
              String ver =
                      version.substring(version.lastIndexOf("-", version.lastIndexOf("-") - 1) + 1);
              String extension = url.substring(url.length() - 4);
              url = getSnapshotMetadataFile(url, ver + extension);
            }
            else if (destinationFile.exists()) {
              // It's already there
              return true;
            }
          }
        }
        if (!"pom".equals(dep.getType())) {
          String name = dep.getArtifactId() + "-" + dep.getResolvedVersion() + ".pom";
          File file = getLocalRepository().getMetadataFile(dep.getGroupId(), dep.getArtifactId(),
                  dep.getVersion(), dep.getType(), name);

          file.getParentFile().mkdirs();

          if (!file.exists() || version.indexOf("SNAPSHOT") >= 0) {
            String filename = dep.getArtifactId() + "-" + version + ".pom";
            String metadataPath = remoteRepo.getMetadataPath(dep.getGroupId(), dep.getArtifactId(),
                    dep.getVersion(), dep.getType(), filename);
            String metaUrl = remoteRepo.getBasedir() + "/" + metadataPath;
            log("Downloading " + metaUrl);


            if (metaUrl.startsWith("file://")) {
              loadFileLocally(metaUrl, file);
            } else {

            try {
              HttpUtils.getFile(metaUrl, file, ignoreErrors, false, proxyHost, proxyPort, proxyUserName,
                      proxyPassword, false);
            }
            catch (IOException e) {
              log("Couldn't find POM - ignoring: " + e.getMessage());
            }
          }
        }
        }

        destinationFile = getLocalRepository().getArtifactFile(dep);
        if (!destinationFile.exists()) {
          log("Downloading " + url);

          if (url.startsWith("file://")) {
            loadFileLocally(url, destinationFile);
          } else {

          HttpUtils.getFile(url, destinationFile, ignoreErrors, useTimestamp, proxyHost, proxyPort,
                  proxyUserName, proxyPassword, true);
          if (dep.getVersion().indexOf("SNAPSHOT") >= 0) {
            String name = StringUtils.replace(destinationFile.getName(), version, dep.getVersion());
            FileUtils.copyFile(destinationFile, new File(destinationFile.getParentFile(), name));
          }
        }
        }

        // Artifact was found, continue checking additional remote repos (if any)
        // in case there is a newer version (i.e. snapshots) in another repo
        fileFound = true;
      }
      catch (FileNotFoundException e) {
        log("Artifact not found at [" + url + "]");
        // Ignore
      }
      catch (Exception e) {
        // If there are additional remote repos, then ignore exception
        // as artifact may be found in another remote repo. If there
        // are no more remote repos to check and the artifact wasn't found in
        // a previous remote repo, then artifactFound is false indicating
        // that the artifact could not be found in any of the remote repos
        //
        // arguably, we need to give the user better control (another command-
        // line switch perhaps) of what to do in this case? Maven already has
        // a command-line switch to work in offline mode, but what about when
        // one of two or more remote repos is unavailable? There may be multiple
        // remote repos for redundancy, in which case you probably want the build
        // to continue. There may however be multiple remote repos because some
        // artifacts are on one, and some are on another. In this case, you may
        // want the build to break.
        //
        // print a warning, in any case, so user catches on to mistyped
        // hostnames, or other snafus
        log("Error retrieving artifact from [" + url + "]: " + e);
      }
    }

    return fileFound;
  }

  private void loadFileLocally(String url, File destinationFile) throws IOException {
    InputStream is = new FileInputStream(url.substring(7));

    FileOutputStream fos = new FileOutputStream(destinationFile);

    byte[] buffer = new byte[100 * 1024];
    int length;

    while ((length = is.read(buffer)) >= 0) {
      fos.write(buffer, 0, length);
      System.out.print(".");
    }

    System.out.println();
    fos.close();
    is.close();
  }

  private static String getSnapshotMetadataFile(String filename, String s) {
    int index = filename.lastIndexOf("SNAPSHOT");
    return filename.substring(0, index) + s;
  }

  private void log(String message) {
    System.out.println(message);
  }

  public List<Repository> getRemoteRepositories() {
    if (remoteRepositories == null) {
      remoteRepositories = new ArrayList<Repository>();
    }

    if (remoteRepositories.isEmpty()) {

      remoteRepositories.add(new Repository("scriptlandia0-repo", "file://" + System.getProperty("maven.repo.local") + "-accelerator",
        Repository.LAYOUT_DEFAULT, false, true));

      remoteRepositories.add(new Repository("mergere", "http://repo.mergere.com/maven2", Repository.LAYOUT_DEFAULT,
        false, true));
      // TODO: use super POM?
//            remoteRepositories.add( new Repository( "central", REPO_URL, Repository.LAYOUT_DEFAULT, false, true ) );

      // TODO: use maven root POM?
      remoteRepositories.add(new Repository("apache.snapshots", "http://cvs.apache.org/maven-snapshot-repository",
              Repository.LAYOUT_DEFAULT, true, false));

      remoteRepositories.add(new Repository("central2", "http://repo1.maven.org/maven2", Repository.LAYOUT_DEFAULT,
        false, true));

      remoteRepositories.add(new Repository("central3", "http://repo1.maven.org/maven-spring", Repository.LAYOUT_DEFAULT,
        false, true));

      remoteRepositories.add(new Repository("scriptlandia-languages-repo", "http://scriptlandia-repository.googlecode.com/svn/trunk/languages",
        Repository.LAYOUT_DEFAULT, false, true));
    }

    return remoteRepositories;
  }

  public void setRemoteRepositories(List<Repository> remoteRepositories) {
    this.remoteRepositories = remoteRepositories;
  }

}
