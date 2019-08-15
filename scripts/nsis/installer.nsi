Name "GSEA"

OutFile "MSigDB_XML_Browser_@VERSION@-installer.exe"
InstallDir "$PROGRAMFILES64\MSigDB_XML_Browser_@VERSION@"

ShowInstDetails nevershow
ShowUninstDetails nevershow
SetCompressor /solid lzma
AutoCloseWindow true
Icon "MSigDB_XML_Browser_@VERSION@\XMLBrowser_Icon.ico"
LicenseData LICENSE_WIN.txt
LicenseForceSelection radiobuttons

Page license
Page directory
Page instfiles
UninstPage instfiles

section
     setOutPath "$INSTDIR"
     File /a /r MSigDB_XML_Browser_@VERSION@\*.*
     createShortCut "$DESKTOP\MSigDB_XML_Browser_@VERSION@.lnk" "$INSTDIR\msigdb-xml-browser.bat" "" "$INSTDIR\XMLBrowser_Icon.ico"
     createDirectory "$SMPROGRAMS\MSigDB_XML_Browser_@VERSION@"
     createShortCut "$SMPROGRAMS\MSigDB_XML_Browser_@VERSION@\MSigDB_XML_Browser.lnk" "$INSTDIR\msigdb-xml-browser.bat" "" "$INSTDIR\XMLBrowser_Icon.ico"
     
     WriteUninstaller $INSTDIR\uninstaller.exe
     createShortCut "$SMPROGRAMS\MSigDB_XML_Browser_@VERSION@\uninstaller.lnk" "$INSTDIR\uninstaller.exe"
sectionEnd

Function un.onInit
    MessageBox MB_YESNO "This will uninstall MSigDB_XML_Browser_@VERSION@.  Continue?" IDYES NoAbort
      Abort ; causes uninstaller to quit.
    NoAbort:
FunctionEnd

#RequestExecutionLevel admin

section "Uninstall"
	setAutoClose true
	RMDir /r "$SMPROGRAMS\MSigDB_XML_Browser_@VERSION@"
	Delete "$Desktop\MSigDB_XML_Browser_@VERSION@.lnk"
	
	# NSIS bset-practice recommends not using RMDir /r $INSTDIR... 
	RMDir /r /REBOOTOK $INSTDIR\*.*
	RMDir /REBOOTOK $INSTDIR
sectionEnd