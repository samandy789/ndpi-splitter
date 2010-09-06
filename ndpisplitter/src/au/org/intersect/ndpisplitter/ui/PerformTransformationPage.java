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
import java.util.Map;

import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Last page of the wizard, that runs the image splitting itself
 * 
 * @version $Rev$
 */
public class PerformTransformationPage extends WizardPage
{
    private static final int PROGRESSBAR_COLUMN_WIDTH = 350;
    private static final int FILENAME_COLUMN_WIDTH = 350;

    private Table table;
    private ProgressBar bar;
    private Button cancelTransformationButton;

    public PerformTransformationPage()
    {
        super("performTransformation");
        setTitle("Step 3");
        setDescription("Confirm conversion");
        setPageComplete(false);
    }

    public Table getTable()
    {
        return table;
    }

    public ProgressBar getBar()
    {
        return bar;
    }
    
    public Button getCancelButton()
    {
        return cancelTransformationButton;
    }

    @Override
    public void createControl(Composite parent)
    {
        final Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        createTableControl(composite);
        createProgressBarControl(composite);
        addCancelButton(composite);
        Attribution.addAttribution(composite, getShell());
        setControl(composite);
    }

    private void addCancelButton(Composite composite)
    {
        cancelTransformationButton = new Button(composite, SWT.PUSH);
        cancelTransformationButton.setText("Stop Conversion");
        GridData openButtonGridData = new GridData(SWT.END, SWT.CENTER, false, false);
        cancelTransformationButton.setLayoutData(openButtonGridData);
        SelectionListener mouseListener = getCancelButtonMouseAdapter();
        cancelTransformationButton.addSelectionListener(mouseListener);
        cancelTransformationButton.setEnabled(false);
    }

    /**
     * Listener for the Browse button Opens the file dialog
     * 
     * @return
     */
    private SelectionListener getCancelButtonMouseAdapter()
    {
        final Shell shell = getShell();
        SelectionAdapter mouseAdapter = new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                MessageDialog.openInformation(shell, "Stop Conversion", 
                        "Stopping the conversion process. This will exit this application.");
                shell.close();                             
            }
        };

        return mouseAdapter;
    }
    
    private void createProgressBarControl(final Composite composite)
    {
        final Composite barContainer = new Composite(composite, SWT.NONE);
        barContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        barContainer.setLayout(new GridLayout(2, false));

        Label l = new Label(barContainer, SWT.NONE);
        l.setText("Converting images");

        bar = new ProgressBar(barContainer, SWT.NONE);
        bar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    private void createTableControl(final Composite composite)
    {
        table = new Table(composite, SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(GridData.FILL_BOTH));

        TableColumn fileName = new TableColumn(table, SWT.LEFT);
        fileName.setText("NDPI file name");
        fileName.setWidth(FILENAME_COLUMN_WIDTH);

        TableColumn progressBar = new TableColumn(table, SWT.LEFT);
        progressBar.setText("File Size");
        progressBar.setWidth(PROGRESSBAR_COLUMN_WIDTH);
    }

    private void populateTable()
    {
        table.removeAll();
        Map<File, File> fileMap = ((NdpiWizard) getWizard()).getTransformationData().getOriginAndDestinationFiles();
        for (File fileToProcess : fileMap.keySet())
        {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, fileToProcess.getName());
        }
    }

    IPageChangingListener getPageChangingListener()
    {
        return new IPageChangingListener()
        {
            @Override
            public void handlePageChanging(PageChangingEvent event)
            {
                event.doit = true;

                boolean inPerformTransformationPage = event.getTargetPage() == PerformTransformationPage.this;

                setPageComplete(inPerformTransformationPage);

                if (inPerformTransformationPage)
                {
                    populateTable();
                }

            }

        };
    }

}
