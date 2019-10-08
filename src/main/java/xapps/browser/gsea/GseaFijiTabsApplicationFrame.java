/*
 * Copyright (c) 2003-2019 Broad Institute, Inc., Massachusetts Institute of Technology, and Regents of the University of California.  All rights reserved.
 */
package xapps.browser.gsea;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;
import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockingManager;

import edu.mit.broad.msigdb_browser.genome.Conf;
import edu.mit.broad.msigdb_browser.genome.JarResources;
import edu.mit.broad.msigdb_browser.genome.swing.GuiHelper;
import edu.mit.broad.msigdb_browser.genome.swing.SystemConsole;
import edu.mit.broad.msigdb_browser.xbench.actions.ShowAppRuntimeHomeDirAction;
import edu.mit.broad.msigdb_browser.xbench.actions.ShowDefaultOutputDirAction;
import edu.mit.broad.msigdb_browser.xbench.actions.XAction;
import edu.mit.broad.msigdb_browser.xbench.actions.ext.BrowserAction;
import edu.mit.broad.msigdb_browser.xbench.core.api.Application;
import edu.mit.broad.msigdb_browser.xbench.core.api.FileManager;
import edu.mit.broad.msigdb_browser.xbench.core.api.FileManagerImpl;
import edu.mit.broad.msigdb_browser.xbench.core.api.VdbManager;
import edu.mit.broad.msigdb_browser.xbench.core.api.WindowManager;
import edu.mit.broad.msigdb_browser.xbench.prefs.XPreferencesFactory;
import xapps.browser.MSigDBViewerContainer;
import xapps.browser.api.frameworks.fiji.StatusBarAppender;
import xapps.browser.api.frameworks.fiji.WindowManagerImplJideTabbedPane;

public class GseaFijiTabsApplicationFrame extends DefaultDockableHolder implements Application.Handler {

    private static final Properties buildProps = JarResources.getBuildInfo();

    static {
        System.setProperty("GSEA", Boolean.TRUE.toString()); // needed for vdb manager to work properly
        if (StringUtils.isBlank(buildProps.getProperty("build.version"))) buildProps.setProperty("build.version", "[NO BUILD VERSION FOUND]");
        if (StringUtils.isBlank(buildProps.getProperty("build.number"))) buildProps.setProperty("build.number", "Error loading build.properties!");
    }

    public static final String RPT_CACHE_BUILD_DATE = "April4_2006_build";

    private static String USER_VISIBLE_FRAME_TITLE = "MSigDB XML Browser " + buildProps.getProperty("build.version");

    // Application's Icon that people see in their operating system task bar
    private static final Image ICON = JarResources.getImage("icon_64x64.png");

    private static final List<Image> FRAME_ICONS = Arrays.asList(
            JarResources.getImage("icon_16x16.png"),
            JarResources.getImage("icon_16x16@2x.png"),
            JarResources.getImage("icon_32x32.png"),
            JarResources.getImage("icon_32x32@2x.png"),
            JarResources.getImage("icon_128x128.png"),
            JarResources.getImage("icon_128x128@2x.png"),
            JarResources.getImage("icon_256x256.png"),
            JarResources.getImage("icon_256x256@2x.png"),
            JarResources.getImage("icon_512x512.png"),
            JarResources.getImage("icon_512x512@2x.png"));

    // @note IMP IMP: this is the name under which docking prefs etc are stored
    public static final String PROFILE_NAME = "gsea_browser";

    private StatusBarAppender fStatusBar;

    private GseaFijiTabsApplicationFrame fFrame = this;

    private WindowAdapter fWindowListener;

    private MyWindowManagerImplJideTabbedPane fWindowManager;

    /**
     * Class constructor
     *
     * @throws HeadlessException
     */
    public GseaFijiTabsApplicationFrame() {
        super(USER_VISIBLE_FRAME_TITLE);

        fFrame.setVisible(false);

        fFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); // we catch and ask
        fFrame.setIconImages(FRAME_ICONS);

        // add a window listener to do clear up when windows closing.
        fWindowListener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApplication(e);
            }
        };

        fFrame.addWindowListener(fWindowListener);

        // Set the profile key
        fFrame.getDockingManager().setProfileKey(PROFILE_NAME);

        // Uses light-weight outline. There are several options here.
        fFrame.getDockingManager().setOutlineMode(DockingManager.PARTIAL_OUTLINE_MODE);

        // Now let's start to addFrame()
        fFrame.getDockingManager().beginLoadLayoutData();

        fFrame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        this.fWindowManager = new MyWindowManagerImplJideTabbedPane();

        this.fStatusBar = SystemConsole.createStatusBarAppender("StatusBar");

        Application.registerHandler(this);

        // add menu bar
        fFrame.setJMenuBar(createMenuBar());
        jbInit();
    }

    public void makeVisible(final boolean bring2front) {
        // load layout information from previous session. This indicates the end of beginLoadLayoutData() method above.
        // This makes the frame visible
        fFrame.getDockingManager().loadLayoutData();

        // disallow drop dockable frame to workspace area
        fFrame.getDockingManager().getWorkspace().setAcceptDockableFrame(false);

        if (bring2front) {
            fFrame.toFront();
        }
    }

    // contains routines that make future displays faster
    public void backgroundInit() {

        try {
            Application.getFileManager().getFileChooser();
            Application.getFileManager().getDirChooser("test");
        } catch (Throwable t) {
            System.out.println("Error background initing: " + t);
        }

        System.out.println("Done background init");
    }

    private void jbInit() {

        fFrame.getContentPane().setLayout(new BorderLayout());

        fFrame.getContentPane().add(new MSigDBViewerContainer());

        // create one project tab for current project
        fFrame.getContentPane().add(fStatusBar.getAsComponent(), BorderLayout.AFTER_LAST_LINE);
    }

    /**
     * @return
     * @note Placing this here rather in ActionFactory as a developing aid
     * - > quicker launch of XReg isnt initialized at startup
     * The applications menu bar is defined here.
     * @maint if new actions are added -> need to review to see if they should also
     * be added to the menu bar.
     */

    private JMenuBar createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // @note JGOODIES SUGGESTIONS
        menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);

        if (!SystemUtils.IS_OS_MAC_OSX) {
            menuBar.add(createJMenu("File", new Object[]{
                    new MyExitAction()
            }));
        }

        menuBar.add(createJMenu("Help", new Object[]{
                new GseaHelpAction("GSEA web site",
                        "Open the GSEA website in a web browser",
                        GseaWebResources.getGseaBaseURL()),
                new GseaHelpAction(),
                new BrowserAction("GSEA & MSigDB License Terms", "GSEA & MSigDB License Terms", GuiHelper.ICON_HELP16, 
                        GseaWebResources.getGseaBaseURL() + "/" + "license_terms_list.jsp"),
                null,
                new ShowAppRuntimeHomeDirAction("Show GSEA home folder"),
                new ShowDefaultOutputDirAction("Show GSEA output folder (default location)"),
                null,
                new ContactAction(),
                null,
                formatBuildInfoForHelp(),
                formatBuildTimestampForHelp()
        }));

        return menuBar;
    }

    private String formatBuildInfoForHelp() {
        String buildVer = buildProps.getProperty("build.version");
        String buildNum = buildProps.getProperty("build.number");
        String buildInfo = "MSigDB XML Browser v" + buildVer + " [build: " + buildNum + "]";
        return buildInfo;
    }
    
    private String formatBuildTimestampForHelp() {
        String buildTS = buildProps.getProperty("build.timestamp");
        if (StringUtils.isBlank(buildTS)) return null; 
        return "Built: " +  buildTS;
    }

    /**
     * @param name
     * @param objs -> array of Action objects interspersed with nulls wherever
     *             a seperator is needed.
     *             For example new Object[]{Foo, Bar, null, Zoo};
     * @return
     */
    private JMenu createJMenu(final String name, final Object[] objs) {

        JMenu menu = new JMenu(name, true);    // true -> can tear off

        for (int i = 0; i < objs.length; i++) {
            if (objs[i] == null) {
                menu.addSeparator();
            } else {
                if (objs[i] instanceof JMenuItem) {
                    menu.add((JMenuItem) objs[i]);
                } else if (objs[i] instanceof String) {
                    menu.add(objs[i].toString());
                } else {
                    menu.add(new JMenuItem((Action) objs[i]));
                }
            }
        }

        return menu;
    }

    private void exitApplication(WindowEvent e_opt) {
        boolean ask = XPreferencesFactory.kAskBeforeAppShutdown.getBoolean();
        if (ask) {
            final boolean res = getWindowManager().showConfirm("Exit the application?");
            if (!res) {
                return;
            }
        }

        fFrame.removeWindowListener(fWindowListener);
        fWindowListener = null;

        if (fFrame.getDockingManager() != null) {
            fFrame.getDockingManager().saveLayoutData();
        }

        fFrame.dispose();
        fFrame = null;
        if (Conf.isDebugMode() == false) {
            Conf.exitSystem(false);
        }
    }

    class MyWindowManagerImplJideTabbedPane extends WindowManagerImplJideTabbedPane {

        MyWindowManagerImplJideTabbedPane() {
            super(fFrame);
        }

    } // End class MyWindowManagerImplJideTabbedPane

    class MyExitAction extends XAction {
        public MyExitAction() {
            super("ExitAction", "Exit", "Quit the GSEA application");
        }

        public void actionPerformed(final ActionEvent evt) {
            exitApplication(null);
        }
    }    // End inner class ExitAction

    // -------------------------------------------------------------------------------------------- //
    // --------------------------- APPLICATION HANDLER IMPLEMENTATION ------------------------------ //
    // -------------------------------------------------------------------------------------------- //

    private static final VdbManager fVdbmanager = new VdbManagerForGsea(RPT_CACHE_BUILD_DATE);

    private FileManager fFileManager;

    public String getName() {
        return "GSEA";
    }

    public FileManager getFileManager() {
        if (fFileManager == null) {
            this.fFileManager = new FileManagerImpl();
        }

        return fFileManager;
    }

    public VdbManager getVdbManager() {
        return fVdbmanager;
    }

    public WindowManager getWindowManager() throws HeadlessException {
        return fWindowManager;
    }

    public StatusBarAppender getStatusBarAppender() {
        return fStatusBar;
    }

} // End class GseaFiji2Application

