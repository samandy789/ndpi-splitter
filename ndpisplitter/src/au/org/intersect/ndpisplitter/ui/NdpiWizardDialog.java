/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @version $Rev$
 *
 */
public class NdpiWizardDialog extends WizardDialog
{

    private static final int WINDOW_HEIGHT = 640;
    private static final int WINDOW_WIDTH = 1000;


    /**
     * @param parentShell
     * @param newWizard
     */
    public NdpiWizardDialog(Shell parentShell, IWizard newWizard)
    {
        super(parentShell, newWizard);
    }

    @Override
    protected void configureShell(Shell newShell)
    {
        super.configureShell(newShell);
        newShell.setMinimumSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        newShell.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
    
    @Override
    protected Control createContents(Composite parent)
    {
        Control control = super.createContents(parent);
        getButton(IDialogConstants.FINISH_ID).setText("Start");
        
        PerformTransformationPage transformationPage = 
            (PerformTransformationPage) getWizard().getPage("performTransformation");
        addPageChangingListener(transformationPage.getPageChangingListener());

        FileSelectionPage fileSelectionPage = 
            (FileSelectionPage) getWizard().getPage("FileSelection");
        addPageChangingListener(fileSelectionPage.getPageChangingListener());
        return control;
    }
    
    @Override
    protected void finishPressed()
    {
        getButton(IDialogConstants.FINISH_ID).setEnabled(false);
        getButton(IDialogConstants.BACK_ID).setEnabled(false);
        getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
        super.finishPressed();
    }
        
}
