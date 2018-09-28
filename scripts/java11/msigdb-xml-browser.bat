::Get the current batch file's short path

for %%x in (%0) do set BatchPath=%%~dpsx

for %%x in (%BatchPath%) do set BatchPath=%%~dpsx

start java --module-path=%BatchPath%\modules -Xmx4g @%BatchPath%\msigdb-xml-browser.args --module org.gsea-msigdb.msigdb-xml-browser/xapps.browser.gsea.MSigDB_XML_Browser  %*
