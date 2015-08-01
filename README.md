# image-retriever
Tomcat Servlets to automatically download images given in HTTP POST request. Images can later be retrieved with a HTTP GET request.

## Setup

1. Install [Apache Tomcat 7](http://tomcat.apache.org/tomcat-7.0-doc/) and the Java SDK on the machine that will host the service. Other versions might work but have not been tested.
If your server already has an Apache web server, use mod_jk ([instructions here](http://tomcat.apache.org/connectors-doc/webserver_howto/apache.html)) to configure Tomcat running through the web server.
2. Change the 'basedirectory' in the properties file to the directory on the host machine that should contain all folders of stored images.
3. Make sure Tomcat has the appropriate permissions to write to your base directory.
4. Add the project to your Tomcat web apps as described in the Tomcat user guide.
5. Run the app!
