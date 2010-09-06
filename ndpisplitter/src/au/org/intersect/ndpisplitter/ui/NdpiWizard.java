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
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import au.org.intersect.ndpisplitter.ndpireader.ImageInformation;
import au.org.intersect.ndpisplitter.splitter.ComponentFactory;
import au.org.intersect.ndpisplitter.splitter.ImageReadingException;
import au.org.intersect.ndpisplitter.splitter.ImageTilingException;
import au.org.intersect.ndpisplitter.splitter.NdpiFileInfoGetter;
import au.org.intersect.ndpisplitter.splitter.NdpiFileSplitter;
import au.org.intersect.ndpisplitter.splitter.StatusUpdater;
import au.org.intersect.ndpisplitter.splitter.TilePositionCalculator;
import au.org.intersect.ndpisplitter.splitter.TilePositions;
import au.org.intersect.ndpisplitter.util.NdpiSplitterProperties;
import au.org.intersect.ndpisplitter.util.ReportStringCompiler;

/**
 * The wizard window
 * 
 * @version $Rev$
 */
public class NdpiWizard extends Wizard
{
    private static final Logger LOG = Logger.getLogger(NdpiWizard.class);

    private NdpiFileInfoGetter ndpiFileInfoGetter;
    private WizardPage fileSelectionPage;
    private WizardPage imageSizePage;
    private PerformTransformationPage performTransformationPage;
    private TransformationData transformationData = new TransformationDataImpl();
    private final NdpiSplitterProperties properties;

    /**
     * Constructor for the wizard window
     * 
     * @param properties
     */
    public NdpiWizard(NdpiSplitterProperties properties)
    {
        this.properties = properties;
        ndpiFileInfoGetter = ComponentFactory.getNdpiFileInfoGetterInstance();

        fileSelectionPage = new FileSelectionPage(ndpiFileInfoGetter);
        performTransformationPage = new PerformTransformationPage();
        imageSizePage = new ImageSizePage(properties);

        setWindowTitle("NDPI File Splitter");
        setNeedsProgressMonitor(true);
        URL bannerImageUrl = ClassLoader.getSystemResource("images/Westmead_Banner.png");
        setDefaultPageImageDescriptor(ImageDescriptor.createFromURL(bannerImageUrl));

    }

    TransformationData getTransformationData()
    {
        return transformationData;
    }

    /**
     * Add the different pages for the wizard
     */
    public void addPages()
    {
        addPage(fileSelectionPage);
        addPage(imageSizePage);
        addPage(performTransformationPage);
    }

    /**
     * Function to execute once the wizard has completed
     */
    @Override
    public boolean performFinish()
    {
        performTransformationPage.getCancelButton().setEnabled(true);
        final Shell shell = getShell();
        NdpiFileSplitter ndpiFileSplitter = ComponentFactory.getNdpiFileSplitterInstance(getTransformationData()
                .getEmptyTilesAlgorithm(), properties);
        Thread thread = new Thread(new TransformRunnable(ndpiFileSplitter, ndpiFileInfoGetter));
        thread.start();

        while (!getTransformationData().isTransformationFinished())
        {
            if (!shell.getDisplay().readAndDispatch())
            {
                shell.getDisplay().sleep();
            }
        }
        String reportString = ReportStringCompiler.compileReportString(getTransformationData().getTilingErrorMap(),
                getTransformationData().getTilingSuccessList());
        MessageDialog.openInformation(shell, "Operation finished", "Operation completed.\n" + reportString);
        return true;
    }

    /**
     * Function to execute if the user cancels the wizard
     */
    @Override
    public boolean performCancel()
    {
        boolean confirmCancel = MessageDialog.openConfirm(getShell(), "Confirmation",
                "Are you sure you want to exit the application?");
        return confirmCancel;
    }

    /**
     * Runnable that performs the image transformation
     * 
     * @version $Rev$
     * 
     */
    class TransformRunnable implements Runnable
    {
        private final TransformationData transformationData;
        private final Table table;
        private final ProgressBar bar;
        private final Shell shell;
        private final NdpiFileSplitter splitter;
        private final NdpiFileInfoGetter ndpiFileInfoGetter;

        public TransformRunnable(NdpiFileSplitter ndpiFileSplitter, NdpiFileInfoGetter ndpiFileInfoGetter)
        {
            this.ndpiFileInfoGetter = ndpiFileInfoGetter;
            this.splitter = ndpiFileSplitter;

            transformationData = getTransformationData();
            table = performTransformationPage.getTable();
            bar = performTransformationPage.getBar();
            shell = getShell();

            initialiseProgressBar();
        }

        private void initialiseProgressBar()
        {
            int numberOfFilesToProcess = transformationData.getOriginAndDestinationFiles().size();
            bar.setMaximum(numberOfFilesToProcess);
        }

        /*
         * Execute image transformation
         */
        @Override
        public void run()
        {
            Map<File, File> originAndDestination = transformationData.getOriginAndDestinationFiles();
            int numberOfProcessedFiles = 0;

            for (Entry<File, File> fileToProcess : originAndDestination.entrySet())
            {
                if (table.isDisposed())
                {
                    return;
                }
                updateUiForCurrentFileProcessed(fileToProcess.getKey().getName(), numberOfProcessedFiles);
                processFile(fileToProcess, numberOfProcessedFiles);
                numberOfProcessedFiles++;
            }
            getTransformationData().setTransformationFinished(true);
        }

        private void updateUiForCurrentFileProcessed(final String fileName, final int currentFileProcessed)
        {
            shell.getDisplay().syncExec(new Runnable()
            {
                public void run()
                {
                    if (table.isDisposed() || bar.isDisposed())
                    {
                        return;
                    }
                    TableItem item = table.getItem(currentFileProcessed);
                    item.setText(0, "Processing file " + fileName);
                    bar.setSelection(currentFileProcessed);
                }
            });
        }

        private void processFile(Entry<File, File> fileToProcess, final int numberOfProcessedFiles)
        {
            final File finalFile = fileToProcess.getKey();
            final File destination = fileToProcess.getValue();
            try
            {
                int tileWidth = transformationData.getTileWidth();
                int tileHeight = transformationData.getTileHeight();
                int magnification = transformationData.getImageMagnification();
                String finalFileAbsolutePath = finalFile.getAbsolutePath();
                tileImage(numberOfProcessedFiles, destination, tileWidth, tileHeight, magnification,
                        finalFileAbsolutePath);

                transformationData.addTilingSuccess(finalFile);
            }
            catch (ImageTilingException ite)
            {
                LOG.error("Image tiling failed for " + finalFile.getAbsolutePath(), ite);
                transformationData.addTilingError(finalFile, ite);
            }
            catch (ImageReadingException e)
            {
                LOG.error("Image reading failed for " + finalFile.getAbsolutePath(), e);
                transformationData.addTilingError(finalFile, new ImageTilingException(e));
            }
        }

        private void tileImage(final int numberOfProcessedFiles, final File destination, int tileWidth, int tileHeight,
                int magnification, String finalFileAbsolutePath) throws ImageReadingException, ImageTilingException
        {
            ImageInformation imageInfo = ndpiFileInfoGetter.getImageInformation(finalFileAbsolutePath);
            TilePositions tilePositions = new TilePositionCalculator(imageInfo, tileWidth, tileHeight, magnification);
            splitter.tileImage(finalFileAbsolutePath, tilePositions, imageInfo, magnification, destination,
                    new StatusUpdater()
                    {
                        private ProgressBar fileProgressBar;

                        @Override
                        public void setNumberOfTilesCompleted(final int tilesCompleted)
                        {
                            updateProgressBar(tilesCompleted);
                        }

                        @Override
                        public void setNumberOfTiles(final int totalNumberOfTiles)
                        {
                            initialiseProgressBar(numberOfProcessedFiles, totalNumberOfTiles);

                        }

                        private void updateProgressBar(final int tilesCompleted)
                        {
                            shell.getDisplay().syncExec(new Runnable()
                            {
                                public void run()
                                {
                                    fileProgressBar.setSelection(tilesCompleted);
                                }
                            });
                        }

                        private void initialiseProgressBar(final int numberOfProcessedFiles,
                                final int totalNumberOfTiles)
                        {
                            shell.getDisplay().syncExec(new Runnable()
                            {
                                public void run()
                                {
                                    TableEditor editor = new TableEditor(table);
                                    fileProgressBar = new ProgressBar(table, SWT.NONE);
                                    TableItem item = table.getItem(numberOfProcessedFiles);
                                    fileProgressBar.setMaximum(totalNumberOfTiles);
                                    editor.grabHorizontal = true;
                                    editor.setEditor(fileProgressBar, item, 1);
                                }
                            });
                        }
                    });
        }

    }

}