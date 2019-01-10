#!/bin/sh

#This script is intended for launching on Macs
#It may or may not work on *nix, definitely not on windows

#-Xdock:name again for Macs, sets the name in menu bar
#-Xmx4g indicates 4 gb of memory, adjust number up or down as needed
prefix=`dirname $(readlink $0 || echo $0)`

# Check whether or not to use the bundled JDK
if [ -d "${prefix}/../jdk-11" ]; then
    echo echo "Using bundled JDK."
    JAVA_HOME="${prefix}/../jdk-11"
    PATH=$JAVA_HOME/bin:$PATH
else
    echo "Bundled JDK not found.  Using system JDK."
fi

exec java --module-path="${prefix}/../Java/modules" -Xmx4g \
    @"${prefix}/../Java/msigdb-xml-browser.args" \
    --patch-module="jide.common=${prefix}/../Java/lib/jide-components-3.7.4.jar:${prefix}/../Java/lib/jide-dock-3.7.4.jar:${prefix}/../Java/lib/jide-grids-3.7.4.jar" \
    -Xdock:name="MSigDB XML Browser" \
    -Xdock:icon="${prefix}/../Resources/msigdb.png" \
    -Dapple.laf.useScreenMenuBar=true \
	--module=org.gsea_msigdb.xmlbrowser/xapps.browser.gsea.MSigDB_XML_Browser "$@"
