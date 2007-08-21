Links: 
http://dev2dev.bea.com/pub/a/2006/10/introduction-groovy-grails.html?page=1
http://grails.codehaus.org/Quick+Start

Before you run this tool, install Grails into ${grails.home}, correct it in "tool.properties" file
and then run install.ant.


1. Create new grails application:

>grails create-app

>Enter application name: testgrails


2. Navigate to the home directory of new grails application:

>cd testgrails


3. Change database configuration in grails-app/conf/ApplicationDataSource.groovy

4. In grails-app/conf/ApplicationBootStrap.groovy file you can add some code that will be executed
at start-up.

5. Create domain class:

>grails create-domain-class

>Enter domain class name: book 

Add new properties to domain class, e.g. title, author etc.

6. Add some data in grails-app/conf/ApplicationBootStrap.groovy

    def init = { servletContext ->
        // Create some test data
        new Book(author:"Stephen King",title:"The Shining").save()
        new Book(author:"James Patterson",title:"Along Came a Spider").save()
    }


7. Create controller for the domain class:

>grails create-controller

>Enter controller name: book

Add scaffolding to the controller:

class BookController {

//	def index = { }

  def scaffold = Book

}


8. Create views:

>grails generate-views

>Enter domain class name: book

9. Start grails as web server

>start grails run-app

//or
//>start mvn jetty:run


10. Start the browser:

>http://localhost:8080/testgrails


11. Additional commands

>grails console - to run GUI Console

>grails shell - to run command line Console
