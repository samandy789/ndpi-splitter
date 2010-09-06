/**
 * Copyright (C) Intersect 2010.
 * 
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 *
 * $Id$
 */
package au.org.intersect.ndpisplitter.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import au.org.intersect.ndpisplitter.util.NdpiSplitterProperties;

/**
 * This is the wizard page that presents the dimensions of the image tiles and the image magnification
 * 
 * @version $Rev$
 * 
 */
class ImageSizePage extends WizardPage
{
    private static final int MIN_PIXEL_SIZE = 100;
    private static final int MAX_PIXEL_DIMENSION = 22500000;

    /*
     * Window dimension definitions
     */
    private static final int TILE_SIZE_DIGIT_LIMIT = 4;
    private static final int GRID_MARGIN_LEFT = 20;
    private static final int GRID_MARGIN_RIGHT = 20;
    private static final int GRID_MARGIN_TOP = 30;
    private static final int GRID_MARGIN_BOTTOM = 30;
    private static final int GRID_SPACING = 10;
    private static final int WIDGET_SEPARATION = 100;

    /**
     * Controls
     */
    private Group imageSizeGroup;
    private Label widthLabel;
    private Text widthText;
    private Label widthPixels;

    private Label heightLabel;
    private Text heightText;
    private Label heightPixels;

    private Group imageMagnificationGroup;
    private Label imageMagnificationLabel;
    private Combo imageMagnificationDropdown;
    private Group emptyTilesGroup;
    private Label emptyTilesLabel;
    private Combo emptyTilesDropdown;
    private final NdpiSplitterProperties properties;

    /**
     * Construct the image size page
     */
    ImageSizePage(NdpiSplitterProperties properties)
    {
        super("ImageSizeSelection");
        this.properties = properties;
        setTitle("Step 2");
        setDescription("File conversion specification");
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
        composite.setLayout(new GridLayout(1, false));

        addImageSizeGroup(composite);
        addImageMagnificationGroup(composite);
        addEmptyTilesAlgorithmGroup(composite);
        Attribution.addAttribution(composite, getShell());
        setPageComplete(false);
        setControl(composite);
    }

    private void addImageMagnificationGroup(Composite composite)
    {
        imageMagnificationGroup = new Group(composite, SWT.NONE);
        RowLayout rowLayout = new RowLayout();
        rowLayout.marginLeft = GRID_MARGIN_LEFT;
        rowLayout.marginRight = GRID_MARGIN_RIGHT;
        rowLayout.marginTop = GRID_MARGIN_TOP;
        rowLayout.marginBottom = GRID_MARGIN_BOTTOM;
        rowLayout.spacing = GRID_SPACING;
        imageMagnificationGroup.setLayout(rowLayout);
        imageMagnificationGroup.setText("Image Magnification");
        GridData layout2 = new GridData(SWT.FILL, SWT.CENTER, true, false);
        imageMagnificationGroup.setLayoutData(layout2);

        imageMagnificationLabel = new Label(imageMagnificationGroup, SWT.NONE);
        imageMagnificationLabel.setText("Select:");

        imageMagnificationDropdown = new Combo(imageMagnificationGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        imageMagnificationDropdown.setItems(properties.getMagnificationList());
        imageMagnificationDropdown.addModifyListener(checkPageValidityListener());
    }

    private void addEmptyTilesAlgorithmGroup(Composite composite)
    {
        emptyTilesGroup = new Group(composite, SWT.NONE);
        RowLayout rowLayout = new RowLayout();
        rowLayout.marginLeft = GRID_MARGIN_LEFT;
        rowLayout.marginRight = GRID_MARGIN_RIGHT;
        rowLayout.marginTop = GRID_MARGIN_TOP;
        rowLayout.marginBottom = GRID_MARGIN_BOTTOM;
        rowLayout.spacing = GRID_SPACING;
        emptyTilesGroup.setLayout(rowLayout);
        emptyTilesGroup.setText("Empty Tiles");
        GridData layout = new GridData(SWT.FILL, SWT.CENTER, true, false);
        emptyTilesGroup.setLayoutData(layout);

        emptyTilesLabel = new Label(emptyTilesGroup, SWT.NONE);
        emptyTilesLabel.setText("Select strategy:");

        emptyTilesDropdown = new Combo(emptyTilesGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        emptyTilesDropdown.setItems(properties.getEmptyTileAlgorithmList());
        emptyTilesDropdown.setText(properties.getDefaultEmptyTileAlgorithm());
        emptyTilesDropdown.addModifyListener(checkPageValidityListener());
    }

    private ModifyListener checkPageValidityListener()
    {
        return new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent arg0)
            {
                setPageCompleted();
            }
        };
    }

    private void addImageSizeGroup(Composite composite)
    {
        imageSizeGroup = new Group(composite, SWT.NONE);
        RowLayout rowLayout = new RowLayout();
        rowLayout.marginLeft = GRID_MARGIN_LEFT;
        rowLayout.marginRight = GRID_MARGIN_RIGHT;
        rowLayout.marginTop = GRID_MARGIN_TOP;
        rowLayout.marginBottom = GRID_MARGIN_BOTTOM;
        rowLayout.spacing = GRID_SPACING;
        imageSizeGroup.setLayout(rowLayout);
        imageSizeGroup.setText("Image Size");
        GridData layout = new GridData(SWT.FILL, SWT.CENTER, true, false);
        imageSizeGroup.setLayoutData(layout);

        createWidthControl();
        createHeightControl();
    }

    private void createHeightControl()
    {
        heightLabel = new Label(imageSizeGroup, SWT.NONE);
        heightLabel.setText("Height:");

        heightText = new Text(imageSizeGroup, SWT.BORDER);
        heightText.setText(properties.getDefaultHeight());
        heightText.setTextLimit(TILE_SIZE_DIGIT_LIMIT);
        heightText.addVerifyListener(numbersOnlyListener());
        heightText.addModifyListener(checkPageValidityListener());

        heightPixels = new Label(imageSizeGroup, SWT.NONE);
        heightPixels.setText("px                                                                                   ");
    }

    private void createWidthControl()
    {
        widthLabel = new Label(imageSizeGroup, SWT.NONE);
        widthLabel.setText("Width:");

        widthText = new Text(imageSizeGroup, SWT.BORDER);
        widthText.setText(properties.getDefaultWidth());
        widthText.setTextLimit(TILE_SIZE_DIGIT_LIMIT);
        widthText.addVerifyListener(numbersOnlyListener());
        widthText.addModifyListener(checkPageValidityListener());

        widthPixels = new Label(imageSizeGroup, SWT.LEFT);
        widthPixels.setText("px");

        RowData rowData = new RowData(WIDGET_SEPARATION, SWT.DEFAULT);
        widthPixels.setLayoutData(rowData);
    }

    /**
     * Enables/Disables the 'Next' button in the wizard
     */
    private void setPageCompleted()
    {
        String width = widthText.getText();
        String height = heightText.getText();
        String magnification = imageMagnificationDropdown.getText();
        String emptyTilesAlgorithm = emptyTilesDropdown.getText();

        // The four fields should have values
        boolean complete = areFieldsValid(width, height, magnification, emptyTilesAlgorithm);
        setPageComplete(complete);

        if (!complete)
        {
            return;
        }

        // drop the "x" off the end if its there
        String intMagnification;
        if (magnification.endsWith("x"))
        {
            intMagnification = magnification.substring(0, magnification.length() - 1);
        }
        else
        {
            intMagnification = magnification;
        }

        ((NdpiWizard) getWizard()).getTransformationData().setImageMagnification(Integer.parseInt(intMagnification));
        ((NdpiWizard) getWizard()).getTransformationData().setEmptyTilesAlgorithm(emptyTilesAlgorithm);
        ((NdpiWizard) getWizard()).getTransformationData().setTileHeight(Integer.parseInt(height));
        ((NdpiWizard) getWizard()).getTransformationData().setTileWidth(Integer.parseInt(width));
    }

    private boolean areFieldsValid(String width, String height, String magnification, String emptyTilesAlgorithm)
    {
        boolean magnificationValid = magnification.length() > 0;
        boolean emptyTilesAlgorithmValid = emptyTilesAlgorithm.length() > 0;
        int pixelWidth = getAsInt(width);
        int pixelHeight = getAsInt(height);
        boolean pixelsMeetsMin = pixelSizeMeetsMinimum(pixelWidth) && pixelSizeMeetsMinimum(pixelHeight);
        boolean pixelsMeetsMax = pixelSizeMeetsMaximum(pixelWidth, pixelHeight);
        if (pixelsMeetsMin && pixelsMeetsMax)
        {
            heightPixels.setText("px");
        }
        else
        {
            if (!pixelsMeetsMin)
            {
                heightPixels.setText("px        dimensions must be at least 100 x 100 pixels");
            }
            if (!pixelsMeetsMax)
            {
                heightPixels.setText("px        dimensions must be less than 5000 x 4500 pixels");
            }
        }
        return magnificationValid && emptyTilesAlgorithmValid && pixelsMeetsMin && pixelsMeetsMax;
    }

    private int getAsInt(String string)
    {
        if (string.length() <= 0)
        {
            return 0;
        }
        return Integer.parseInt(string);
    }

    private boolean pixelSizeMeetsMinimum(int width)
    {
        return width >= MIN_PIXEL_SIZE;
    }

    private boolean pixelSizeMeetsMaximum(int width, int height)
    {
        return width * height <= MAX_PIXEL_DIMENSION;
    }

    private VerifyListener numbersOnlyListener()
    {
        return new VerifyListener()
        {

            @Override
            public void verifyText(VerifyEvent event)
            {
                String string = event.text;
                char[] chars = new char[string.length()];
                string.getChars(0, chars.length, chars, 0);
                for (int i = 0; i < chars.length; i++)
                {
                    if (!('0' <= chars[i] && chars[i] <= '9'))
                    {
                        event.doit = false;
                        return;
                    }
                }

            }
        };
    }
}
