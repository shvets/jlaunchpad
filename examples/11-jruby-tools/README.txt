See also: 
http://weblogs.java.net/blog/arungupta/archive/2007/11/jruby_102_relea.html
http://www.danielfischer.com/2007/10/22/static-websites-the-ruby-way/

1. Configuration 


1.1. Test JRuby version:

>jruby -v

1.2. Test Gem version:

>jruby -S gem -v

1.3. Install Rails:

>jruby -S gem install rails -y --include-dependencies --no-rdoc --no-ri

1.4. Install raven:

>jruby -S gem install raven -y --no-rdoc --no-ri


2. Rails Project Generation


2.1. Create new project (testrails):

>jruby -S rails testrails

Change the current directory:

>cd testrails

2.2. Modify testrails\config\database.yml to point to your database.

2.3. Start the database. Create database for development: testrails_development

2.4. Generate the model

>jruby script\generate model MyTest

2.5. Generate the controller & view

>jruby script\generate controller MyTest index

2.6. Edit controller (app\controllers\my_test_controller.rb)

Add  @hello_string = "Hello from 1.1". The updated file looks like:

class MyTestController < ApplicationController

  def index
    @hello_string = "Hello from 1.1"
  end
end

2.7. Edit View (app\views\my_test\index.rhtml)

and add <%= @hello_string %> as the last line. The updated file looks like:

<h1>MyTest#index</h1>
<p>Find me in app/views/my_test/index.rhtml</p>
<%= @hello_string %>

2.8. Start the WEBrick server

>start jruby script\server

2.9. Test if server is started properly in the browser:

>http://localhost:3000/my_test/index

Now you should see your view.


How to run JRoR on Glassfish

1. Download glassfish gem locally:

>wget -c http://download.java.net/maven/glassfish/com/sun/enterprise/glassfish/glassfish-gem/10.0-SNAPSHOT/glassfish-gem-10.0-SNAPSHOT.gem

2. Install glassfish:

>jruby -S gem install glassfish-gem-10.0-SNAPSHOT.gem

3. Start glassfish:

>start jruby -S glassfish_rails testrails -wait

4.

http://localhost:8080/testrails/my_test/index


To do:

ruby.bat script/generate migration contact_db

Article: http://www.onlamp.com/lpt/a/6826

mysqld

mysql -u root -p

mysql>create database cookbook2_development;

mysql>grant all on cookbook2_development.* to 'ODBC'@'localhost';

mysql>exit

drop table if exists recipes;
drop table if exists categories;
create table categories (
    id                     int            not null auto_increment,
    name                   varchar(100)   not null default '',
    primary key(id)
) engine=InnoDB;

create table recipes (
    id                     int            not null auto_increment,
    category_id            int            not null,
    title                  varchar(100)   not null default '',
    description            varchar(255)   null,
    date                   date           null,
    instructions           text           null,
    constraint fk_recipes_categories foreign key (category_id) references categories(id),
    primary key(id)
) engine=InnoDB;


mysql cookbook2_development <db\create.sql
