/**
 * Copyright (C) Intersect 2010.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @version $Rev$
 *
 */
public class Attribution
{
    private static Image attributionLogo;

    private static synchronized Image getAttributionLogo(Shell shell)
    {
        if (attributionLogo == null)
        {
            attributionLogo = new Image(shell.getDisplay(), ClassLoader
                    .getSystemResourceAsStream("images/intersect_logo.png"));
        }
        return attributionLogo;
    }

    public static void addAttribution(Composite composite, Shell shell)
    {
        Composite attributionComposite = new Composite(composite, SWT.NONE);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.END;
        data.verticalAlignment = GridData.END;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        attributionComposite.setLayoutData(data);
        attributionComposite.setLayout(new RowLayout());
        Image attributionLogo = getAttributionLogo(shell);
        Label imageLabel = new Label(attributionComposite, SWT.NONE);
        Label textLabel = new Label(attributionComposite, SWT.NONE);
        imageLabel.setImage(attributionLogo);
        imageLabel.setToolTipText("http://www.intersect.org.au");
        textLabel.setText("Developed by Intersect Australia Ltd");
        textLabel.setToolTipText("http://www.intersect.org.au");
        attributionComposite.pack();
    }
}
