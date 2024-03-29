Introduction
============

This application is created to allow user to setup some properties for eXo Platform Server.
We are going to explain the steps to build and deploy Setup Wizard with eXo Platform -3.5.4

System requirements
===================
   
* Java Development Kit 1.6
* Recent Git client
* Recent Maven 3
* eXo-Platform-tomcat-3.5.4 Tomcat Bundle
* eXo-Platform-tomcat-3.5.4 JBoss EAP -5.1
* {PLF_HOME}: The location of the unzipped eXo server.
* The eXo server will run on port 8080, make sure this port is not currently in use


Build and Deploy instructions
=============================

1) Clone Setup Wizard
-----------------------

* git clone git@github.com:exo-addons/setup-wizard-extension.git

* {PROJECT_HOME}: refers to the directory where you cloned the project.

* cd PROJECT_HOME/plf-setup-wizard


2) Build Setup Wizard
-----------------------

You can build Setup Wizard using the following command :

mvn clean install

After a build success of the project you will have:

* "PROJECT_HOME/plf-setup-wizard/component/target/setup-wizard-component-0.9-SNAPSHOT.jar" -> Contains services, resources and accessories files. 
* "PROJECT_HOME/plf-setup-wizard/webapp/target/setup.war" -> Contains the JavaScrpt, JSP, CSS code and the necessary libraries.

In the next step you will only need "setup.war". This web application already contains "setup-wizard-component-0.9-SNAPSHOT.jar". You find it under "setup.war/WEB-INF/lib" 

3) Deploy Setup Wizard 
-----------------------

3.1) Deploy with eXo Platform -3.5.4 Tomcat Bundle
---------------------------------------------

* After building Setup Wizard, copy "setup.war" that you find under "PROJECT_HOME/plf-setup-wizard/webapp/target" to "PLF_HOME/tomcat-bundle/webapps" directory.

* You need to disable the Portal Container. Go to "web.xml" under "PLF_HOME/tomcat-bundle/webapps/starter/WEB-INF" and comment this part :

        <listener>
          <listener-class>org.exoplatform.container.web.PortalContainerCreator</listener-class> 
        </listener>

The portal container will be launched at the end of the Wizard process with the new properties entered by user.

3.2) Deploy with eXo Platform -3.5.4 JBoss EAP -5.1
----------------------------------------------

* After building Setup Wizard, copy "setup.war" that you find under "PROJECT_HOME/plf-setup-wizard/webapp/target" to "PLF_HOME/server/default/deploy" directory. 

* You need to disable the Portal Container. Go to "web.xml" under "PLF_HOME/server/default/deploy/starter-gatein-3.2.x-PLF.ear/starter.war/WEB-INF" and comment this part :

        <listener>
          <listener-class>org.exoplatform.container.web.PortalContainerCreator</listener-class> 
        </listener>

The portal container will be launched at the end of the Wizard process with the new properties entered by user.


4) Start Setup Wizard 
-----------------------

Start eXo Platform -3.5.4 

Access Setup Wizard at: http://localhost:8080/setup


Troubleshooting
===============

Maven dependencies issues
-------------------------

 While Platform should build without any extra maven repository configuration it may happen that the build complains about missing artifacts.

 If you encounter this situation, please let us know via our forums (http://forum.exoplatform.org).

 As a quick workaround you may try setting up maven repositories as follows.

 Create file settings.xml in $HOME/.m2  (%HOMEPATH%\.m2 on Windows) with the following content:

         <settings>
          <profiles>      
            <profile>
              <id>exo-public-repository</id>
              <repositories>
                <repository>
                <id>exo-public-repository-group</id>
                <name>eXo Public Maven Repository Group</name>
                <url>http://repository.exoplatform.org/content/groups/public</url>
                <layout>default</layout>
                <releases>
                  <enabled>true</enabled>
                  <updatePolicy>never</updatePolicy>
                </releases>
                <snapshots>
                  <enabled>true</enabled>
                  <updatePolicy>never</updatePolicy>
                </snapshots>
              </repository>
            </repositories>
            <pluginRepositories>
              <pluginRepository>
                <id>exo-public-repository-group</id>
                <name>eXo Public Maven Repository Group</name>
                <url>http://repository.exoplatform.org/content/groups/public</url>
                <layout>default</layout>
                <releases>
                  <enabled>true</enabled>
                  <updatePolicy>never</updatePolicy>
                </releases>
                <snapshots>
                  <enabled>true</enabled>
                  <updatePolicy>never</updatePolicy>
                </snapshots>
              </pluginRepository>
            </pluginRepositories>
          </profile>
        </profiles>

        <activeProfiles>
          <activeProfile>exo-public-repository</activeProfile>
        </activeProfiles>
      </settings>

Going Further
=============
* Developers: learn how to build your own portal, gadgets, REST services or eXo-based applications in the Developer Guide
[http://docs.exoplatform.com/PLF35/topic/org.exoplatform.doc.35/DeveloperGuide.html] and the Reference Documentation
[http://docs.exoplatform.com/PLF35/topic/org.exoplatform.doc.35/GateInReferenceGuide.html]
* Administrators: learn how to install eXo Platform on a server in the Administrator Guide:
http://docs.exoplatform.com/PLF35/topic/org.exoplatform.doc.35/AdministratorGuide.html
* End Users: learn more about using the features in the User Manuals:
http://docs.exoplatform.com/PLF35/topic/org.exoplatform.doc.35/UserGuide.html
