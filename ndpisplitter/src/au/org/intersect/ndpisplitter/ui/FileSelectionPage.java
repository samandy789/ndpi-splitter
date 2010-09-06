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

import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import au.org.intersect.ndpisplitter.splitter.NdpiFileInfoGetter;

/**
 * This is the wizard page that allows user to select the files to transform
 * 
 * @version $Rev$
 * 
 */
final class FileSelectionPage extends WizardPage
{

    private static final String PARTIALLY_COMPLETED_DIALOG_TITLE = "Operation partially completed";
    private static final String CONFIRM_OVERWRITE_DIALOG_MESSAGE = "The output directory already "
            + "exists for one or more of your files. "
            + "If you continue, existing files in these directories will be replaced. "
            + "Are you sure you want to continue?";
    private static final long KILOBYTE = 1024L;
    private static final long MEGABYTE = KILOBYTE * 1024L;
    private static final long GIGABYTE = MEGABYTE * 1024L;

    private static final String[] FILTER_EXTS = {"*.ndpi"};
    private static final String[] FILTER_NAMES = {"NDPI files (*.ndpi)"};
    private static final int FILE_ADDED = 0;
    private static final int FILE_ALREADY_IN_LIST = 1;
    private static final int FILE_NOT_FOUND = 2;

    private static Image addImage;
    private static Image removeImage;
    private static Image warningImage;

    /**
     * Controls
     */
    private Table fileListTable;
    private Button openButton;
    private Button deleteButton;
    private NdpiFileInfoGetter ndpiFileInfoGetter;

    /**
     * Construct the file selection page
     */
    FileSelectionPage(NdpiFileInfoGetter ndpiFileInfoGetter)
    {
        super("FileSelection");
        setTitle("Step 1");
        setDescription("Select files for conversion");
        setPageComplete(false);
        this.ndpiFileInfoGetter = ndpiFileInfoGetter;
    }

    /**
     * Add controls to the page
     * 
     * @param parent
     *            the control where to add elements
     */
    public void createControl(Composite parent)
    {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));

        addImage = new Image(getShell().getDisplay(), ClassLoader
                .getSystemResourceAsStream("images/image-add-icon.png"));
        removeImage = new Image(getShell().getDisplay(), ClassLoader
                .getSystemResourceAsStream("images/image-remove-icon.png"));
        warningImage = new Image(getShell().getDisplay(), ClassLoader
                .getSystemResourceAsStream("images/warning-icon.png"));

        GridLayout gridLayout = new GridLayout(1, false);
        composite.setLayout(gridLayout);

        addFileOpenAndDeleteButtons(composite);
        addFileTable(composite);
        Attribution.addAttribution(composite, getShell());
        setControl(composite);
    }

    private void addFileOpenAndDeleteButtons(Composite composite)
    {
        Composite attributionComposite = new Composite(composite, SWT.NONE);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.END;
        data.verticalAlignment = GridData.END;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        attributionComposite.setLayoutData(data);
        attributionComposite.setLayout(new RowLayout());
        addFileDeleteButton(attributionComposite);
        addFileOpenButton(attributionComposite);
    }

    /**
     * Create the file table
     * 
     * @param composite
     */
    private void addFileTable(Composite composite)
    {
        fileListTable = new Table(composite, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
        fileListTable.setLinesVisible(true);
        fileListTable.setHeaderVisible(true);

        TableColumn fileName = new TableColumn(fileListTable, SWT.LEFT);
        fileName.setText("NDPI file name");
        fileName.setWidth(350);

        TableColumn fileSize = new TableColumn(fileListTable, SWT.LEFT);
        fileSize.setText("File Size");
        fileSize.setWidth(100);

        TableColumn magnification = new TableColumn(fileListTable, SWT.LEFT);
        magnification.setText("Magnification");
        magnification.setWidth(100);

        TableColumn destinationDirectory = new TableColumn(fileListTable, SWT.LEFT);
        destinationDirectory.setText("Destination Directory");
        destinationDirectory.setWidth(300);

        GridData fileTableGridData = new GridData(GridData.FILL_BOTH);
        // fileTableGridData.widthHint = 950;
        fileTableGridData.heightHint = 300;
        fileListTable.setLayoutData(fileTableGridData);
        SelectionListener listener = getTableListener();
        fileListTable.addSelectionListener(listener);
        // fileListTable.setSize(900, 300);
    }

    /**
     * Create the 'file open' button
     * 
     * @param composite
     */
    private void addFileOpenButton(Composite composite)
    {
        openButton = new Button(composite, SWT.PUSH);
        openButton.setText("Browse");
        openButton.setImage(addImage);
        // GridData openButtonGridData = new GridData(SWT.END, SWT.CENTER, false, false);
        // openButton.setLayoutData(openButtonGridData);
        SelectionListener mouseListener = getOpenButtonMouseAdapter();
        openButton.addSelectionListener(mouseListener);
    }

    /**
     * Create the delete button
     * 
     * @param composite
     */
    private void addFileDeleteButton(Composite composite)
    {
        deleteButton = new Button(composite, SWT.PUSH);
        deleteButton.setText("Remove selected");
        deleteButton.setImage(removeImage);

        // GridData deleteButtonGridData = new GridData(SWT.END, SWT.CENTER, true, false);
        // deleteButton.setLayoutData(deleteButtonGridData);

        SelectionListener mouseListener = getDeleteButtonMouseAdapter();
        deleteButton.addSelectionListener(mouseListener);
        deleteButton.setEnabled(false);
    }

    /**
     * Table listener - enables/disables the delete button depending on whether the table has files selected or not
     * 
     * @return
     */
    private SelectionListener getTableListener()
    {
        return new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent arg0)
            {
                int[] selected = fileListTable.getSelectionIndices();
                deleteButton.setEnabled(selected.length > 0);
            }

        };
    }

    /**
     * Listener for the Browse button Opens the file dialog
     * 
     * @return
     */
    private SelectionListener getOpenButtonMouseAdapter()
    {
        SelectionAdapter mouseAdapter = new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                doFileOpenDialog();
            }
        };

        return mouseAdapter;
    }

    /**
     * Listener for the delete button
     * 
     * @return
     */
    private SelectionListener getDeleteButtonMouseAdapter()
    {
        SelectionAdapter mouseAdapter = new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                doFileDelete();
            }
        };

        return mouseAdapter;
    }

    /**
     * Perform file deletion function Removes all selected files
     */
    private final void doFileDelete()
    {
        boolean confirmed = MessageDialog.openConfirm(getShell(), "Confirm file deletion",
                "Are you sure you want to remove the selected files from the list?");

        if (!confirmed)
        {
            return;
        }

        TableItem[] fileEntries = fileListTable.getSelection();
        for (TableItem fileEntry : fileEntries)
        {
            String fileName = fileEntry.getText(0); // this is without path
            String outputDirectory = fileEntry.getText(3); // output directory helps us identify if there's two with the
                                                           // same name
            ((NdpiWizard) getWizard()).getTransformationData().deleteFile(fileName, outputDirectory);
        }

        int[] selected = fileListTable.getSelectionIndices();
        fileListTable.remove(selected);
        setPageCompleted();
        deleteButton.setEnabled(false);
    }

    /**
     * Performs file opening action Opens the file open window and populates the table with the selected files
     */
    private final void doFileOpenDialog()
    {
        FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI);
        fileDialog.setFilterNames(FILTER_NAMES);
        fileDialog.setFilterExtensions(FILTER_EXTS);
        if (fileDialog.open() != null)
        {
            // Append all the selected files. Since getFileNames()
            // returns only the names, and not the path, prepend the path,
            // normalizing if necessary
            String[] files = fileDialog.getFileNames();
            String selectedDirectory = fileDialog.getFilterPath();
            if (selectedDirectory.charAt(selectedDirectory.length() - 1) != File.separatorChar)
            {
                selectedDirectory += File.separatorChar;
            }

            boolean someFilesAlreadyInList = false;
            boolean someFilesDontExist = false;
            for (int i = 0; i < files.length; i++)
            {
                String fileName = files[i];
                // status tells us if the file was added, wasn't found, or was already in the list
                int status = addFileEntryToTable(selectedDirectory, fileName);
                if (status == FILE_ALREADY_IN_LIST)
                {
                    someFilesAlreadyInList = true;
                }
                if (status == FILE_NOT_FOUND)
                {
                    someFilesDontExist = true;
                }
            }

            displayWarningsIfNeeded(someFilesAlreadyInList, someFilesDontExist);

        }
        setPageCompleted();
    }

    private void displayWarningsIfNeeded(boolean someFilesAlreadyInList, boolean filesDontExist)
    {
        if (!someFilesAlreadyInList && !filesDontExist)
        {
            // nothing to display
            return;
        }

        String message = "Not all selected files were added to the list since";
        if (someFilesAlreadyInList && !filesDontExist)
        {
            message += " some of the files were already included.";
        }
        else if (someFilesAlreadyInList && filesDontExist)
        {
            message += " some of the files were either already included or could not be found.";
        }
        else
        {
            message += " some of the files could not be found.";
        }
        MessageDialog.openInformation(getShell(), PARTIALLY_COMPLETED_DIALOG_TITLE, message);
    }

    /**
     * Adds an entry to the table for one of the selected files
     * 
     * @param selectedDirectory
     *            directory where the file is located
     * @param fileName
     *            selected file
     */
    private int addFileEntryToTable(String selectedDirectory, String fileName)
    {
        String fullFileName = selectedDirectory + fileName;

        File originFile = new File(fullFileName);
        if (!originFile.exists())
        {
            // If the file doesn't exist, we just ignore it and continue on. This is to cater partially for an issue in
            // the windows file selection dialog where you can get a non-existent file by selecting a file and changing
            // directories.
            return FILE_NOT_FOUND;
        }

        String outputDirectoryName = selectedDirectory;
        if (fileName.contains("."))
        {
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            outputDirectoryName += fileNameWithoutExtension + "_tiles";
        }
        else
        {
            outputDirectoryName += fileName + "_tiles";
        }

        File destinationDirectory = new File(outputDirectoryName);

        // success tells us whether the file was already added
        boolean success = ((NdpiWizard) getWizard()).getTransformationData().addFile(originFile, destinationDirectory);
        if (!success)
        {
            return FILE_ALREADY_IN_LIST;
        }

        TableItem fileEntry = new TableItem(fileListTable, SWT.NONE);

        fileEntry.setText(0, fileName);
        fileEntry.setText(1, longToByteRepresentation(originFile.length()));
        fileEntry.setText(2, getMagnification(originFile));

        if (destinationDirectory.exists())
        {
            fileEntry.setImage(3, warningImage);
        }
        fileEntry.setText(3, destinationDirectory.getAbsolutePath());

        return FILE_ADDED;
    }

    private String getMagnification(File originFile)
    {
        try
        {
            float magnification = ndpiFileInfoGetter.getMagnification(originFile.getAbsolutePath());
            int mag = Math.round(magnification);
            return Integer.toString(mag) + "x";
        }
        catch (Exception e)
        {
            // we don't really want to handle errors at this stage, so we just display "unknown"
            return "unknown";
        }
    }

    /**
     * Enables/Disables the 'Next' button in the wizard
     */
    private void setPageCompleted()
    {
        setPageComplete(fileListTable.getItems().length > 0);
    }

    /**
     * Returns a string with the size of the file
     * 
     * @param size
     * @return
     */
    private String longToByteRepresentation(long size)
    {
        if ((size / GIGABYTE) > 0)
        {
            return divideAndOneDecimal(size, GIGABYTE) + " Gbytes";
        }
        else if ((size / MEGABYTE) > 0)
        {
            return divideAndOneDecimal(size, MEGABYTE) + " Mbytes";
        }
        else if ((size / KILOBYTE) > 0)
        {
            return divideAndOneDecimal(size, KILOBYTE) + " Kbytes";
        }

        return new Long(size).toString() + " bytes";
    }

    /**
     * Return the result of the division If the integer value is less than 10, return the first decimal
     * 
     * @param dividend
     * @param divisor
     * @return the result of the division
     */
    private String divideAndOneDecimal(long dividend, long divisor)
    {
        Long quotient = dividend / divisor;
        if (quotient > 9)
        {
            return quotient.toString();
        }

        // Get the first decimal by
        // 1. Getting the remaining of the division, multiplying by 10
        // and dividing again
        Long remainder = ((dividend % divisor) * 10) / divisor;
        if (remainder > 0)
        {
            return quotient.toString() + "." + remainder;
        }

        return quotient.toString();
    }

    /**
     * @return
     */
    public IPageChangingListener getPageChangingListener()
    {
        return new IPageChangingListener()
        {
            @Override
            public void handlePageChanging(PageChangingEvent event)
            {
                boolean inFileSelectionPage = event.getCurrentPage() == FileSelectionPage.this;
                if (inFileSelectionPage)
                {
                    TransformationData transformationData = ((NdpiWizard) getWizard()).getTransformationData();
                    if (transformationData.shouldWarnAboutExistingDirectories())
                    {
                        boolean confirmCancel = MessageDialog.openConfirm(getShell(), "Confirm Replace Existing Files",
                                CONFIRM_OVERWRITE_DIALOG_MESSAGE);
                        event.doit = confirmCancel;
                    }
                    else
                    {
                        event.doit = true;
                    }
                }
            }
        };
    }
}
