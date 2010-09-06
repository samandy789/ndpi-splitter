/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */

package au.org.intersect.ndpisplitter.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import au.org.intersect.ndpisplitter.util.NdpiSplitterProperties;
import au.org.intersect.ndpisplitter.util.NdpiSplitterPropertiesException;

/**
 * Runnable application
 * 
 * @version $Rev$
 */
public class NdpiApplication extends ApplicationWindow
{
    private static final String NDPISPLITTER_PROPERTY_FILE = "/ndpisplitter.properties";
    private static final String JNA_LIBRARY_PATH_PROPERTY = "jna.library.path";
    private static final String SEMI_COLON = ";";
    private static final Logger LOG = Logger.getLogger(NdpiApplication.class);
    private final NdpiSplitterProperties properties;

    public NdpiApplication(NdpiSplitterProperties properties)
    {
        super(null);
        this.properties = properties;
    }

    /**
     * Application main method.
     * 
     * @param args
     * @throws IOException
     * @throws NdpiSplitterPropertiesException
     */
    public static void main(final String[] args) throws IOException, NdpiSplitterPropertiesException
    {
        configureEnvironment();
        NdpiSplitterProperties properties = loadProperties();
        NdpiApplication application = new NdpiApplication(properties);
        // application.setBlockOnOpen(true);
        application.open();
    }

    private static NdpiSplitterProperties loadProperties() throws IOException, NdpiSplitterPropertiesException
    {
        Properties properties = new Properties();
        InputStream propertiesStream = NdpiApplication.class.getResourceAsStream(NDPISPLITTER_PROPERTY_FILE);
        if (propertiesStream == null)
        {
            throw new IllegalStateException("Could not find properties file " + NDPISPLITTER_PROPERTY_FILE);
        }
        properties.load(propertiesStream);
        return new NdpiSplitterProperties(properties);
    }

    private static void configureEnvironment()
    {
        String currentLibraryPath = System.getProperty(JNA_LIBRARY_PATH_PROPERTY);
        if (currentLibraryPath == null)
        {
            currentLibraryPath = "";
        }
        LOG.info("Current jna.library.path " + currentLibraryPath);
        String workingDirectory = System.getProperty("user.dir");
        LOG.info("Working directory " + workingDirectory);

        // add these in just in case it helps
        String extraPaths = "/windows/system32;/ndpisplitter/dll";
        // add this for debugging in eclipse
        String eclipseDebuggingPath = workingDirectory + File.separator + "install";
        // add the current working directory/dll which should be correct
        String workingDirectoryPath = workingDirectory + File.separator + "dll";
        // include the current library path (if set), working directory, plus extras
        String newLibraryPath = eclipseDebuggingPath + SEMI_COLON + currentLibraryPath + SEMI_COLON
                + workingDirectoryPath + SEMI_COLON + extraPaths;

        System.setProperty(JNA_LIBRARY_PATH_PROPERTY, newLibraryPath);
        LOG.info("New jna.library.path " + System.getProperty(JNA_LIBRARY_PATH_PROPERTY));
    }

    /**
     * Create the wizard dialog
     * 
     * @param parent
     *            the composite where to create the wizard
     */
    protected Control createContents(Composite parent)
    {
        NdpiWizard wizard = new NdpiWizard(properties);

        WizardDialog dialog = new NdpiWizardDialog(getShell(), wizard);

        dialog.setBlockOnOpen(true);
        dialog.open();
        return parent;
    }
}
