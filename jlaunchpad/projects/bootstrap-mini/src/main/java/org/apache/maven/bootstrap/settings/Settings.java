package org.apache.maven.bootstrap.settings;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.bootstrap.util.AbstractReader;
import org.apache.maven.bootstrap.model.Repository;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Settings definition.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: Settings.java 640549 2008-03-24 20:05:11Z bentmann $
 */
public class Settings
{
    private String localRepository;

    private List mirrors = new ArrayList();

    private List proxies = new ArrayList();

    private Proxy activeProxy = null;

    private List activeProfiles = new ArrayList();

    private List repositories = new ArrayList();

    public Settings()
    {
        localRepository = System.getProperty( "maven.repo.local" );
    }

    public String getLocalRepository()
    {
        return localRepository;
    }

    public void setLocalRepository( String localRepository )
    {
        this.localRepository = localRepository;
    }

    public void addProxy( Proxy proxy )
    {
        proxies.add( proxy );
    }

    public void addMirror( Mirror mirror )
    {
        mirrors.add( mirror );
    }

    public void addActiveProfile( String activeProfile )
    {
        activeProfiles.add( activeProfile );
    }

  public void addRepository( Repository repository )
  {
      repositories.add( repository );
  }

    public Proxy getActiveProxy()
    {
        if ( activeProxy == null )
        {
            for ( Iterator it = proxies.iterator(); it.hasNext() && activeProxy == null; )
            {
                Proxy proxy = (Proxy) it.next();
                if ( proxy.isActive() )
                {
                    activeProxy = proxy;
                }
            }
        }
        return activeProxy;
    }

    public static Settings read( String userHome, File file )
        throws IOException, ParserConfigurationException, SAXException
    {
        return new Reader( userHome ).parseSettings( file );
    }

    public List getMirrors()
    {
        return mirrors;
    }

    public List getActiveProfiles()
    {
        return activeProfiles;
    }

  public List getRepositories()
  {
      return repositories;
  }


    private static class Reader
        extends AbstractReader
    {
        private Proxy currentProxy = null;

        private StringBuffer currentBody = new StringBuffer();

        private Mirror currentMirror;
        private String currentActiveProfile;
        private Repository currentRepository;

        private final Settings settings = new Settings();

        private final String userHome;

        private Reader( String userHome )
        {
            this.userHome = userHome;
        }

        public void characters( char[] ch, int start, int length )
            throws SAXException
        {
            currentBody.append( ch, start, length );
        }

        public void endElement( String uri, String localName, String rawName )
            throws SAXException
        {
            if ( "localRepository".equals( rawName ) )
            {
                if ( notEmpty( currentBody.toString() ) )
                {
                    String localRepository = currentBody.toString().trim();
                    if ( settings.getLocalRepository() == null )
                    {
                        settings.setLocalRepository( localRepository );
                    }
                }
                else
                {
                    throw new SAXException(
                        "Invalid profile entry. Missing one or more " + "fields: {localRepository}." );
                }
            }
            else if ( "proxy".equals( rawName ) )
            {
                if ( notEmpty( currentProxy.getHost() ) && notEmpty( currentProxy.getPort() ) )
                {
                    settings.addProxy( currentProxy );
                    currentProxy = null;
                }
                else
                {
                    throw new SAXException( "Invalid proxy entry. Missing one or more fields: {host, port}." );
                }
            }
            else if ( currentProxy != null )
            {
                if ( "active".equals( rawName ) )
                {
                    currentProxy.setActive( Boolean.valueOf( currentBody.toString().trim() ).booleanValue() );
                }
                else if ( "host".equals( rawName ) )
                {
                    currentProxy.setHost( currentBody.toString().trim() );
                }
                else if ( "port".equals( rawName ) )
                {
                    currentProxy.setPort( currentBody.toString().trim() );
                }
                else if ( "username".equals( rawName ) )
                {
                    currentProxy.setUserName( currentBody.toString().trim() );
                }
                else if ( "password".equals( rawName ) )
                {
                    currentProxy.setPassword( currentBody.toString().trim() );
                }
                else if ( "protocol".equals( rawName ) )
                {
                }
                else if ( "nonProxyHosts".equals( rawName ) )
                {
                }
                else
                {
                    throw new SAXException( "Illegal element inside proxy: \'" + rawName + "\'" );
                }
            }
            else if ( "mirror".equals( rawName ) )
            {
                if ( notEmpty( currentMirror.getId() ) && notEmpty( currentMirror.getMirrorOf() ) &&
                    notEmpty( currentMirror.getUrl() ) )
                {
                    settings.addMirror( currentMirror );
                    currentMirror = null;
                }
                else
                {
                    throw new SAXException( "Invalid mirror entry. Missing one or more fields: {id, mirrorOf, url}." );
                }
            }
            else if ( currentMirror != null )
            {
                if ( "id".equals( rawName ) )
                {
                    currentMirror.setId( currentBody.toString().trim() );
                }
                else if ( "mirrorOf".equals( rawName ) )
                {
                    currentMirror.setMirrorOf( currentBody.toString().trim() );
                }
                else if ( "url".equals( rawName ) )
                {
                    currentMirror.setUrl( currentBody.toString().trim() );
                }
                else if ( "name".equals( rawName ) )
                {
                }
                else
                {
                    throw new SAXException( "Illegal element inside proxy: \'" + rawName + "\'" );
                }
            }
            else if ( "activeProfile".equals( rawName ) )
            {
                if ( notEmpty( currentActiveProfile) )
                {
                    settings.addActiveProfile( currentActiveProfile);
                    currentActiveProfile = null;
                }
                else
                {
                    throw new SAXException( "Invalid active profile entry." );
                }
            }
           else if ( "repository".equals( rawName ) )
            {
                if ( notEmpty( currentRepository.getId() ) && notEmpty( currentRepository.getBasedir() ) )
                {
                    settings.addRepository( currentRepository );
                    currentRepository = null;
                }
                else
                {
                    throw new SAXException( "Invalid repository entry. Missing one or more fields: {id, url}." );
                }
            }
            else if ( currentRepository != null )
            {
                if ( "id".equals( rawName ) )
                {
                    currentRepository.setId( currentBody.toString().trim() );
                }
                else if ( "layout".equals( rawName ) )
                {
                    currentRepository.setLayout( currentBody.toString().trim() );
                }
                else if ( "url".equals( rawName ) )
                {
                    currentRepository.setBasedir(replaceNames(currentBody.toString().trim()) );
                }
                else if ( "name".equals( rawName ))
                {
                }
                else if ( "snapshots".equals( rawName ))
                {
                }
                else if ( "enabled".equals( rawName ) ) {
                }

                else
                {
                    throw new SAXException( "Illegal element inside proxy: \'" + rawName + "\'" );
                }
            }

            currentBody = new StringBuffer();
        }

  private String replaceNames(String text) {
    StringBuffer newText = new StringBuffer();

    String s = text;

    boolean ok = false;
    while(!ok) {
      int index1 = s.indexOf("${");

      if(index1 == -1) {
        newText.append(s);

        ok = true;
      }
      else {
        int index2 = s.indexOf("}");

        if(index2 == -1) {
          newText.append(s);

          ok = true;
        }
        else {
          String propertyName = s.substring(index1+2, index2);

          String propertyValue = System.getProperty(propertyName);

          if(propertyValue != null) {
            newText.append(s.substring(0, index1));
            newText.append(propertyValue.replace('\\', '/'));
          }

          s = s.substring(index2+1);
        }
      }
    }

    return newText.toString();
  }
        private boolean notEmpty( String test )
        {
            return test != null && test.trim().length() > 0;
        }

        public void startElement( String uri, String localName, String rawName, Attributes attributes )
            throws SAXException
        {
            if ( "proxy".equals( rawName ) )
            {
                currentProxy = new Proxy();
            }
            else if ( "mirror".equals( rawName ) )
            {
                currentMirror = new Mirror();
            }
            else if ( "activeProfile".equals( rawName ) )
            {
                currentActiveProfile = rawName;
            }
            else if ( "repository".equals( rawName ) )
            {
                currentRepository = new Repository();
                currentRepository.setSnapshots(false);
            }
        }

        public void reset()
        {
            this.currentBody = null;
            this.currentMirror = null;
            this.currentActiveProfile = null;
            this.currentRepository = null;
        }

        public Settings parseSettings( File settingsXml )
            throws IOException, ParserConfigurationException, SAXException
        {
            if ( settingsXml.exists() )
            {
                parse( settingsXml );
            }
            if ( settings.getLocalRepository() == null )
            {
                String m2LocalRepoPath = "/.m2/repository";

                File repoDir = new File( userHome, m2LocalRepoPath );
                if ( !repoDir.exists() )
                {
                    repoDir.mkdirs();
                }

                settings.setLocalRepository( repoDir.getAbsolutePath() );

                System.out.println(
                    "You SHOULD have a ~/.m2/settings.xml file and must contain at least the following information:" );
                System.out.println();

                System.out.println( "<settings>" );
                System.out.println( "  <localRepository>/path/to/your/repository</localRepository>" );
                System.out.println( "</settings>" );

                System.out.println();

                System.out.println( "Alternatively, you can specify -Dmaven.repo.local=/path/to/m2/repository" );

                System.out.println();

                System.out.println( "HOWEVER, since you did not specify a repository path, maven will use: " +
                    repoDir.getAbsolutePath() + " to store artifacts locally." );
            }
            return settings;
        }
    }
}
