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
 * Gant script that creates a WAR file from a Grails project
 * 
 * @author Graeme Rocher
 *
 * @since 0.4
 */

Ant.property(environment:"env")                             
grailsHome = Ant.antProject.properties."env.GRAILS_HOME"    
                              

includeTargets << new File ( "${grailsHome}/scripts/Clean.groovy" ) 
includeTargets << new File ( "${grailsHome}/scripts/Package.groovy" )

task ('default': "Creates a WAR archive") {
    depends( checkVersion)

	war()
} 

task (war: "The implementation task") {
	depends( clean, packagePlugins, packageApp, generateWebXml )
	 
	try {
		Ant.mkdir(dir:"${basedir}/staging")
		Ant.copy(todir:"${basedir}/staging", overwrite:true) {
			fileset(dir:"${basedir}/web-app", includes:"**") 
		}       
		Ant.copy(todir:"${basedir}/staging/WEB-INF/grails-app", overwrite:true) {
			fileset(dir:"${basedir}/grails-app", includes:"**")
		}
		              
		scaffoldDir = "${basedir}/staging/WEB-INF/templates/scaffolding"
		packageTemplates()
		

		Ant.copy(todir:"${basedir}/staging/WEB-INF/lib") {
			fileset(dir:"${grailsHome}/dist") {
					include(name:"grails-*.jar")
			}
			fileset(dir:"${basedir}/lib") {
					include(name:"*.jar")
			}
		}              
		
	    Ant.propertyfile(file:"${basedir}/staging/WEB-INF/classes/application.properties") {
	        entry(key:"grails.env", value:grailsEnv)
	    }		
		
		Ant.replace(file:"${basedir}/staging/WEB-INF/applicationContext.xml", 
				token:"classpath*:", value:"" ) 

		def fileName = grailsAppName
		def version = Ant.antProject.properties.'app.version'
		if (version) {
		    version = '-'+version
		} else {
		    version = ''
		}
		warName = "${basedir}/${fileName}${version}.war"

		warPlugins()		
		Ant.jar(destfile:warName, basedir:"${basedir}/staging")
		
	}   
	finally {
		cleanUpAfterWar()
	}
    event("StatusFinal", ["Created WAR ${warName}"])
}                                                                    
   
task(cleanUpAfterWar:"Cleans up after performing a WAR") {
	Ant.delete(dir:"${basedir}/staging", failonerror:true)
}

task(warPlugins:"Includes the plugins in the WAR") {  
	Ant.sequential {
		mkdir(dir:"${basedir}/staging/WEB-INF/plugins")
		copy(todir:"${basedir}/staging/WEB-INF/plugins", failonerror:false) {
			fileset(dir:"${basedir}/plugins")  {    
				include(name:"**/*GrailsPlugin.groovy")
				include(name:"**/grails-app/**")
				exclude(name:"**/grails-app/i18n")				
			}
		}
	}
}

