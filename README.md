CacheFilter
===========

Servlet filter used to force browsers to download files from Java servers even if it has been cached.

--
At work, several times I had to force the reload of the JavaScript files in browser. Often this involved proprietary applications. And it was not interesting to ask users clear their caches. So I wrote this code, where I can choose which files should be forced to expire when loaded. So everytime I edit a file I am sure it reloads in all user's browsers.

--
How to configure:

Edit your web.xml project file and add these lines:

    <filter>
        <filter-name>ExpiresFilter</filter-name>
        <filter-class>com.f0gg.cachefilter.ExpiresFilter</filter-class>
        <init-param>
            <param-name>nocache</param-name>
            <param-value>file1.js,file2.js</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>ExpiresFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
        <dispatcher>INCLUDE</dispatcher>
        <dispatcher>ERROR</dispatcher>
    </filter-mapping>

The example above forces expiration of file1.js and file2.js whenever pages load.

Lines below show Log4J console when it is in action:


2013-07-06 14:13:30,686 DEBUG (ExpiresFilter.java:68) - Web.xml NOCACHE parameter: file1.js,file2.js

2013-07-06 14:13:32,701 DEBUG (ExpiresFilter.java:52) - Expiring /CacheFilterTest/file1.js

2013-07-06 14:13:32,703 DEBUG (ExpiresFilter.java:59) - Changing file Header: /CacheFilterTest/file1.js

2013-07-06 14:13:32,709 DEBUG (ExpiresFilter.java:52) - Expiring /CacheFilterTest/file2.js

2013-07-06 14:13:32,709 DEBUG (ExpiresFilter.java:59) - Changing file Header: /CacheFilterTest/file2.js

2013-07-06 14:13:32,718 DEBUG (ExpiresFilter.java:96) - Header changed successfully


Hope you enjoy!
