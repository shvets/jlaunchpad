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

/**
 * Mirror definition.
 *
 * @author <a href="mailto:brett@apache.org">Brett Porter</a>
 * @version $Id: Mirror.java 640549 2008-03-24 20:05:11Z bentmann $
 */
public class Mirror
{
    private String id;

    private String mirrorOf;

    private String url;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public void setMirrorOf( String mirrorOf )
    {
        this.mirrorOf = mirrorOf;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getMirrorOf()
    {
        return mirrorOf;
    }

    public String getUrl()
    {
        return url;
    }
}
