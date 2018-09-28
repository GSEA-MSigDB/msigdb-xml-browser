#!/bin/sh

#This script is intended for launch on *nix machines

#-Xmx4g indicates 4 gb of memory, adjust number up or down as needed
#Add the flag -Dsun.java2d.uiScale=2 for HiDPI displays
prefix=`dirname $(readlink $0 || echo $0)`
exec java --module-path="$prefix"/modules -Xmx4g \
    @msigdb-xml-browser.args \
    -Dsun.java2d.uiScale=2 \
	--module org.gsea-msigdb.msigdb-xml-browser/xapps.browser.gsea.MSigDB_XML_Browser "$@"
