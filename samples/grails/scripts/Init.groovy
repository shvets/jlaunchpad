/*
 * Copyright 2004-2005 the original author or authors.
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
 * Gant script that handles general initialization of a Grails applications
 * 
 * @author Graeme Rocher
 *
 * @since 0.4
 */    
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.springframework.core.io.support.PathMatchingResourcePatternResolver            
import org.codehaus.groovy.control.*  

Ant.property(environment:"env")       

servletVersion = System.getProperty("servlet.version") ? System.getProperty("servlet.version") : "2.4"
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"   
Ant.property(file:"${grailsHome}/build.properties")

grailsVersion =  Ant.antProject.properties.'grails.version'
grailsEnv = System.getProperty("grails.env") 
defaultEnv = System.getProperty("grails.default.env") == "true" ? true : false 
serverPort = System.getProperty('server.port') ? System.getProperty('server.port').toInteger() : 8080   
serverHost = System.getProperty('server.host') ? System.getProperty('server.host') : null
basedir = System.getProperty("base.dir") 
baseFile = new File(basedir)
baseName = baseFile.name      
userHome = Ant.antProject.properties."user.home"                     
grailsTmp = "${userHome}/.grails/tmp"
eventsClassLoader = new GroovyClassLoader(getClass().classLoader)

resolver = new PathMatchingResourcePatternResolver()
grailsAppName = null
appGrailsVersion = null
hookScripts = [this]
hooksLoaded = false
classpathSet = false

// Send a scripting event notification to any and all event hooks in plugins/user scripts
event = { String name, def args ->
    if (!hooksLoaded) {
        setClasspath()

        loadEventHooks()

        hooksLoaded = true

        // Give scripts a chance to modify classpath
        event('setClasspath', [getClass().classLoader.rootLoader])
    }

    hookScripts.each() {
        try {
            def handler = it."event$name"
            handler.delegate = binding
            handler(*args)
        } catch (MissingPropertyException e) {
        }
    }
}

eventStatusFinal = { message ->
    println message
}

eventStatusUpdate = { message ->
    println message + ' ...'
}

eventStatusError = { message ->
    System.err.println message
}

eventCreatedArtefact = { artefactType, artefactName ->
    println "Created $artefactType for $artefactName"
}

/* Standard handlers not handled by Init
eventSetClasspath = { rootLoader ->
    // Called when root classloader is being configured
}
eventCreatedFile = { fileName ->
    // Called when any file is created in the project tree, that is to be part of the project source (not regenerated)
}
eventPluginInstalled = { pluginName ->
    // Called when a plugin is installed
}
eventExiting = { returnCode ->
    // Called when the Gant scripting is about to end
}
*/

void loadEventHooks() {
    // Look for user script
    def f = new File( userHome, ".grails/scripts/Events.groovy")
    if (f.exists()) {
        println "Found user events script"
        loadEventScript(f)
    }

    // Look for plugin-supplied scripts
    def pluginsDir = new File( basedir, "plugins")
    if (pluginsDir.exists()) {
        pluginsDir.eachDir() {
            f = new File( it, "scripts/Events.groovy")
            if (f.exists()) {
                println "Found events script in plugin ${it.name}"
                loadEventScript(f)
            }
        }
    }
}

void loadEventScript(theFile) {
    try {
        def script = eventsClassLoader.parseClass( theFile).newInstance()
        script.delegate = binding
        script.run()
        hookScripts << script
    } catch (Throwable t) {
        println "Unable to load event script $theFile: ${t.message}"
        t.printStackTrace()
    }
}

exit = {
    event("Exiting", [it])
    // Prevent system.exit during unit/integration testing
    if (System.getProperty("grails.cli.testing")) {
        throw new RuntimeException("Gant script exited")
    } else {
        System.exit(it)
    }
}

// Get App's metadata if there is any
if (new File("${basedir}/application.properties").exists()) {
    // We know we have an app
    Ant.property(file:"${basedir}/application.properties")

    grailsAppName = Ant.antProject.properties.'app.name'
    appGrailsVersion = Ant.antProject.properties.'app.grails.version'
}

// If no app name property (upgraded/new/edited project) default to basedir
if (!grailsAppName) {
    grailsAppName = baseName
}
appClassName = GCU.getClassNameRepresentation(grailsAppName)


// a resolver that doesn't throw exceptions when resolving resources
resolveResources = { String pattern ->
	try {
		return resolver.getResources(pattern)	   
	}
	catch(Throwable e) {
         return []
	}	
}

getGrailsLibs =  {
  def result = ''
   (new File("${grailsHome}/lib")).eachFileMatch(~/.*\.jar/) { file ->
      result += "<classpathentry kind=\"var\" path=\"GRAILS_HOME/lib/${file.name}\" />\n\n"
   }
   result
}
getGrailsJar =  { args ->
   result = ''
   (new File("${grailsHome}/dist")).eachFileMatch(~/^grails-.*\.jar/) { file ->
      result += "<classpathentry kind=\"var\" path=\"GRAILS_HOME/dist/${file.name}\" />\n\n"
   }
   result
}

args = System.getProperty("grails.cli.args")   

confirmInput = { String message ->
	Ant.input(message: message, addproperty:"confirm.message",validargs:"y,n")
	Ant.antProject.properties."confirm.message"	
}

task ( createStructure: "Creates the application directory structure") {
	Ant.sequential {
	    mkdir(dir:"${basedir}/src")
	    mkdir(dir:"${basedir}/src/java")
	    mkdir(dir:"${basedir}/src/groovy")
	    mkdir(dir:"${basedir}/src/test")  
	    mkdir(dir:"${basedir}/grails-app")
	    mkdir(dir:"${basedir}/grails-app/controllers")
	    mkdir(dir:"${basedir}/grails-app/services")
	    mkdir(dir:"${basedir}/grails-app/domain")
	    mkdir(dir:"${basedir}/grails-app/taglib")
	    mkdir(dir:"${basedir}/grails-app/utils")
	    mkdir(dir:"${basedir}/grails-app/views")
	    mkdir(dir:"${basedir}/grails-app/views/layouts")
	    mkdir(dir:"${basedir}/grails-app/i18n")
	    mkdir(dir:"${basedir}/grails-app/conf")
	    mkdir(dir:"${basedir}/test")
	    mkdir(dir:"${basedir}/test/unit")	
	    mkdir(dir:"${basedir}/test/integration")	
	    mkdir(dir:"${basedir}/scripts") 
	    mkdir(dir:"${basedir}/web-app")
	    mkdir(dir:"${basedir}/web-app/js")
	    mkdir(dir:"${basedir}/web-app/css")
	    mkdir(dir:"${basedir}/web-app/images")
	    mkdir(dir:"${basedir}/web-app/WEB-INF/classes")
	    mkdir(dir:"${basedir}/web-app/META-INF")
	    mkdir(dir:"${basedir}/lib")
	    mkdir(dir:"${basedir}/spring")
	    mkdir(dir:"${basedir}/hibernate") 
	}
}  

task (checkVersion: "Stops build if app expects different Grails version") {
    if (new File("${basedir}/application.properties").exists()) {
        if (appGrailsVersion != grailsVersion) {
            event("StatusFinal", ["Application expects grails version [$appGrailsVersion], but GRAILS_HOME is version " +
                "[$grailsVersion] - use the correct Grails version or run 'grails upgrade' if this Grails "+
                "version is newer than the version your application expects."])
            exit(1)
        }
    } else {
        // We know this is pre-0.5 application
	    event("StatusFinal", ["Application is pre-Grails 0.5, please run: grails upgrade"])
	    exit(1)
    }
}

task( updateAppProperties: "Updates default application.properties")  {
    Ant.propertyfile(file:"${basedir}/application.properties",
        comment:"Do not edit app.grails.* properties, they may change automatically. "+
            "DO NOT put application configuration in here, it is not the right place!") {
        entry(key:"app.name", value:"$grailsAppName")
        entry(key:"app.grails.version", value:"$grailsVersion")
    }
    // Make sure if this is a new project that we update the var to include version
    appGrailsVersion = grailsVersion
}

task ( copyBasics: "Copies the basic resources required for a Grails app to function") {
    def libs = getGrailsLibs()
    def jars =  getGrailsJar()

	Ant.sequential {
		copy(todir:"${basedir}") {
			fileset(dir:"${grailsHome}/src/grails/templates/ide-support/eclipse",
					includes:"*.*",
					excludes:".launch")
		} 
		replace(dir:"${basedir}",includes:"*.*", 
				token:"@grails.libs@", value:"${libs}" )
		replace(dir:"${basedir}", includes:"*.*", 
				token:"@grails.jar@", value:"${jars}" )
		replace(dir:"${basedir}", includes:"*.*", 
				token:"@grails.project.name@", value:"${grailsAppName}" )
		
		copy(todir:"${basedir}/web-app/WEB-INF") {
			fileset(dir:"${grailsHome}/src/war/WEB-INF") {
				include(name:"applicationContext.xml")
				exclude(name:"log4j.properties")
				include(name:"sitemesh.xml")
			}			
		}	
		copy(file:"${grailsHome}/src/war/WEB-INF/web${servletVersion}.template.xml",
				 tofile:"${basedir}/web-app/WEB-INF/web.template.xml") 
				 
		copy(todir:"${basedir}/web-app/WEB-INF/tld") {
			fileset(dir:"${grailsHome}/src/war/WEB-INF/tld/${servletVersion}")	
			fileset(dir:"${grailsHome}/src/war/WEB-INF/tld", includes:"spring.tld")
			fileset(dir:"${grailsHome}/src/war/WEB-INF/tld", includes:"grails.tld")			
		}			 			
	}
}
task( init: "main init task") {
	depends( createStructure, copyBasics )

	Ant.sequential {
		copy(todir:"${basedir}/web-app") {
			fileset(dir:"${grailsHome}/src/war") {
				include(name:"**/**")
				exclude(name:"WEB-INF/**")
			} 
		}		
		copy(file:"${grailsHome}/src/war/WEB-INF/log4j.properties",
			 tofile:"${basedir}/grails-app/conf/log4j.development.properties")
		copy(file:"${grailsHome}/src/war/WEB-INF/log4j.properties",
			 tofile:"${basedir}/grails-app/conf/log4j.test.properties")
		copy(file:"${grailsHome}/src/war/WEB-INF/log4j.properties",
			 tofile:"${basedir}/grails-app/conf/log4j.production.properties")

        copy(file:"${grailsHome}/src/grails/templates/artifacts/UrlMappings.groovy",
             tofile:"${basedir}/grails-app/conf/${appClassName}UrlMappings.groovy")    

		replace(file:"${basedir}/grails-app/conf/${appClassName}UrlMappings.groovy", 
					token:"@artifact.name@", value:"${appClassName}UrlMappings" )
			
		copy(todir:"${basedir}/grails-app") {
			fileset(dir:"${grailsHome}/src/grails/grails-app", excludes:"**/taglib/**, **/utils/**")
		}                                                                              
		
		
		createCorePlugin()   	                                                                                  
					
		copy(todir:"${basedir}/spring") {
			fileset(dir:"${grailsHome}/src/war/WEB-INF/spring") {
				include(name:"*.xml")
			}
		}  
		touch(file:"${basedir}/grails-app/i18n/messages.properties") 
	}
}

task(createCorePlugin:"Creates the core plugin and its resources") {
	Ant.mkdir(dir:"${basedir}/plugins/core/grails-app/taglib")
	Ant.mkdir(dir:"${basedir}/plugins/core/grails-app/utils")		
          
	Ant.delete {
		fileset(dir:"${basedir}/plugins/core/grails-app/taglib", includes:"*.groovy")
		fileset(dir:"${basedir}/plugins/core/grails-app/utils", includes:"*.groovy")		
	}
	Ant.copy(todir:"${basedir}/plugins/core/grails-app/taglib") {
			fileset(dir:"${grailsHome}/src/grails/grails-app/taglib", includes:"*")
	}		                                                                                  
	Ant.copy(todir:"${basedir}/plugins/core/grails-app/utils") {
			fileset(dir:"${grailsHome}/src/grails/grails-app/utils", includes:"*")
    } 
}
task("default": "Initializes a Grails application. Warning: This task will overwrite artifacts,use the 'upgrade' task for upgrades.") {
	depends( init )
}  

task (createArtifact: "Creates a specific Grails artifact") {
	depends( promptForName)
	
	Ant.mkdir(dir:"${basedir}/${artifactPath}")
	
	className = GCU.getClassNameRepresentation(args)   
	propertyName = GCU.getPropertyNameRepresentation(args)            
	artifactFile = "${basedir}/${artifactPath}/${className}${typeName}.groovy"


	if(new File(artifactFile).exists()) {
		Ant.input(addProperty:"${args}.${typeName}.overwrite", message:"${artifactName} ${className}${typeName}.groovy already exists. Overwrite? [y/n]")
		if(Ant.antProject.properties."${args}.${typeName}.overwrite" == "n")
			return
	}
		
	// first check for presence of template in application
	templateFile = "${basedir}/src/templates/artifacts/${artifactName}.groovy"
	if (!new File(templateFile).exists()) {
        // now check for template provided by plugins
        def pluginTemplateFiles = resolveResources("plugins/*/src/templates/artifacts/${artifactName}.groovy")
        if ( pluginTemplateFiles ) {
            templateFile = pluginTemplateFiles[0].path
        } else {
            // template not found in application, use default template
            templateFile = "${grailsHome}/src/grails/templates/artifacts/${artifactName}.groovy"
        }
    }
	Ant.copy(file: templateFile, tofile: artifactFile, overwrite:true)
	Ant.replace(file:artifactFile, 
				token:"@artifact.name@", value:"${className}${typeName}" )
				
	event("CreatedFile", [artifactFile])
	event("CreatedArtefact", [typeName, className])
}  

task(promptForName:"Prompts the user for the name of the Artifact if it isn't specified as an argument") {
	if(!args) {
		Ant.input(addProperty:"artifact.name", message:"${typeName} name not specified. Please enter:")
		args = Ant.antProject.properties."artifact.name"
	}          	
}

task(classpath:"Sets the Grails classpath") {    
    setClasspath()
}

void setClasspath() {
    if (classpathSet) return

    def grailsDir = resolveResources("file:${basedir}/grails-app/*")

	Ant.path(id:"grails.classpath")  {
		pathelement(location:"${basedir}")
		pathelement(location:"${basedir}/grails-tests")
		pathelement(location:"${basedir}/web-app")
		pathelement(location:"${basedir}/web-app/WEB-INF")
		pathelement(location:"${basedir}/web-app/WEB-INF/classes")
		if (new File("${basedir}/web-app/WEB-INF/lib").exists()) {
		    fileset(dir:"${basedir}/web-app/WEB-INF/lib")
		}
		fileset(dir:"${grailsHome}/lib")
		fileset(dir:"${grailsHome}/dist")
		fileset(dir:"lib")
		for(d in grailsDir) {
			pathelement(location:"${d.file.absolutePath}")
		}
	}
    StringBuffer cpath = new StringBuffer("")

    def jarFiles = resolveResources("lib/*.jar").toList()

    resolveResources("plugins/*/lib/*.jar").each { pluginJar ->
        boolean matches = jarFiles.any { it.file.name == pluginJar.file.name }
        if(!matches) jarFiles.add(pluginJar)
    }

    resolveResources("file:${userHome}/.grails/lib/*.jar").each { userJar ->
		jarFiles.add(userJar)
	}



    def rootLoader = getClass().classLoader.rootLoader

    jarFiles.each { jar ->
        cpath << jar.file.absolutePath << File.pathSeparator
        rootLoader?.addURL(jar.URL)
    }

	grailsDir.each { dir ->
        cpath << dir.file.absolutePath << File.pathSeparator
		rootLoader?.addURL(dir.URL)
   }

    cpath << "${basedir}/web-app/WEB-INF/classes"
	rootLoader?.addURL(new File("${basedir}/web-app/WEB-INF/classes").toURL())
       cpath << "${basedir}/web-app/WEB-INF"
	rootLoader?.addURL(new File("${basedir}/web-app/WEB-INF").toURL())

  	// println "Classpath with which to generate web.xml: \n${cpath.toString()}"

   	parentLoader = getClass().getClassLoader()

   	compConfig = new CompilerConfiguration()
   	compConfig.setClasspath(cpath.toString());

    classpathSet = true

    event('setClasspath', [rootLoader])
}

task( configureProxy: "The implementation task")  {
    def scriptFile = new File("${userHome}/.grails/scripts/ProxyConfig.groovy")
    if(scriptFile.exists()) {
        includeTargets << scriptFile.text
        if( proxyConfig.proxyHost ) {
            // Let's configure proxy...
            def proxyHost = proxyConfig.proxyHost 
            def proxyPort = proxyConfig.proxyPort ? proxyConfig.proxyPort : '80'
            def proxyUser = proxyConfig.proxyUser ? proxyConfig.proxyUser : ''
            def proxyPassword = proxyConfig.proxyPassword ? proxyConfig.proxyPassword : ''
            println "Configured HTTP proxy: ${proxyHost}:${proxyPort}${ proxyConfig.proxyUser ? '(' + proxyUser + ')' : ''}"
            // ... for Ant. We can remove this line with Ant 1.7.0 as it uses system properties.
            Ant.setproxy(proxyhost:proxyHost,proxyport:proxyPort,proxyuser:proxyUser,proxypassword:proxyPassword)
            // ... for all other code
            System.properties.putAll( ["http.proxyHost":proxyHost, "http.proxyPort":proxyPort, "http.proxyUserName":proxyUser, "http.proxyPassword":proxyPassword] )
        }
    }
}
