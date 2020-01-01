package com.ryanworks.fishery.client.print;

import com.ryanworks.fishery.shared.util.AppConstants;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author Ryan C
 */
public class RootPDFBoxPrinter 
{
    public final static int TEXT_ALIGN_RIGHT = 1;
    public final static int TEXT_ALIGN_CENTER = 2;
    public final static int TEXT_ALIGN_LEFT = 3;
    
    private static File fontFileSimkai;
    private static File fontFileSimhei;
    private static PDFont plainFont;
    private static PDFont boldFont;
    private static PDFont obliqueFont;
    
    public final static boolean OPEN_PDF_ON_FLY = false;
    //public final static boolean OPEN_PDF_ON_FLY = true;
    
    public final static String PDF_PRINTOUT_FOLDER = AppConstants.PROJECT_PATH + "pdf/";
    public final static int DIMENSION_WIDTH_IN_POINTS = 625; //612
    public final static int DIMENSION_HEIGHT_IN_POINTS = 792;
    // A4: 595x842
    // Letter: 612x792
    protected void printPlainText(PDPageContentStream contentStream, PDFont chineseFont, float positionX, float positionY, int fontSize, String text)
            throws IOException
    {        
        if (text==null)
            text = "";
        
        contentStream.beginText();        
        if (this.containsHanScript(text))
            contentStream.setFont( chineseFont, fontSize );        
        else
            contentStream.setFont( getFontPlain(), fontSize );
        contentStream.moveTextPositionByAmount( positionX, positionY );
        contentStream.drawString( text );
        contentStream.endText();
    }
    
    protected File getFontFileSimkai()
    {
        try 
        {
            URL url = this.getClass().getResource( "/com/ryanworks/fishery/resources/" );
            
            if (fontFileSimkai == null)
                fontFileSimkai = new File( url.getPath() + "simkai.ttf");
            
            if (fontFileSimkai.exists()==false)
                fontFileSimkai = new File(AppConstants.PROJECT_PATH + "font/simkai.ttf");
            System.err.println("File exist? : " + fontFileSimkai.exists());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {            
            System.err.println("Font: " + fontFileSimkai);
            return fontFileSimkai;
        }
    }
    
    protected File getFontFileSimhei()
    {
        try 
        {
            URL url = this.getClass().getResource( "/com/ryanworks/fishery/resources/" );
            
            if (fontFileSimhei == null)
                fontFileSimhei = new File( url.getPath() + "simhei.ttf");
                        
            if (fontFileSimhei.exists()==false)
                fontFileSimhei = new File(AppConstants.PROJECT_PATH + "font/simhei.ttf");
            System.err.println("File exist? : " + fontFileSimhei.exists());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            System.err.println("Font: " + fontFileSimhei);
            return fontFileSimhei;
        }
    }
    
    protected static PDFont getFontPlain()
    {
        if (plainFont == null)
            plainFont = PDType1Font.HELVETICA;
        return plainFont;
    }
    
    protected static PDFont getFontBold()
    {
        if (boldFont == null)
            boldFont = PDType1Font.HELVETICA_BOLD;
        return boldFont;
    }
    
    protected static PDFont getFontOblique()
    {
        if (obliqueFont == null)
            obliqueFont = PDType1Font.HELVETICA_OBLIQUE;
        return obliqueFont;
    }
    
    public RootPDFBoxPrinter()
    {
        //fontFileSimkai = new File("C:/fishery/simkai.ttf");
        //fontFileSimhei = new File("C:/fishery/simhei.ttf");
        plainFont = PDType1Font.HELVETICA;
        boldFont = PDType1Font.HELVETICA_BOLD;
        obliqueFont = PDType1Font.HELVETICA_OBLIQUE;
    }
    
    protected PDPageContentStream printHeader(PDDocument document, PDPage page, PDFont simkaiFont)
    {        
        try 
        {
            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            int y_position = DIMENSION_HEIGHT_IN_POINTS-20;
            
            contentStream.beginText(); 
            contentStream.setFont( simkaiFont, 18 ); 
            contentStream.moveTextPositionByAmount( xPositionAfterJustifyToCenter("海春鱼行", DIMENSION_WIDTH_IN_POINTS, simkaiFont, 18), y_position-10 );
            contentStream.drawString( "海春鱼行" );
            contentStream.endText();
                        
            contentStream.beginText(); 
            contentStream.setFont( getFontPlain(), 16 ); 
            contentStream.moveTextPositionByAmount( xPositionAfterJustifyToCenter("HAI CHUN FISHERY", DIMENSION_WIDTH_IN_POINTS, simkaiFont, 16), y_position-30 );
            contentStream.drawString( "HAI CHUN FISHERY" );
            contentStream.endText();
            
            contentStream.beginText(); 
            contentStream.setFont( getFontPlain(), 9 ); 
            contentStream.moveTextPositionByAmount( xPositionAfterJustifyToCenter("Lot 0258, Bagan Sungai Buloh", DIMENSION_WIDTH_IN_POINTS, simkaiFont, 9), y_position-45 );
            contentStream.drawString( "Lot 0258, Bagan Sungai Buloh" );
            contentStream.endText();
            
            contentStream.beginText(); 
            contentStream.setFont( getFontPlain(), 9 ); 
            contentStream.moveTextPositionByAmount( xPositionAfterJustifyToCenter("Jeram, 45800, Selangor.", DIMENSION_WIDTH_IN_POINTS, simkaiFont, 9), y_position-60 );
            contentStream.drawString( "Jeram, 45800, Selangor." );
            contentStream.endText();
            
            contentStream.beginText(); 
            contentStream.setFont( getFontPlain(), 9 ); 
            contentStream.moveTextPositionByAmount( xPositionAfterJustifyToCenter("(O) 03-8747726, (R) 03-8747353", DIMENSION_WIDTH_IN_POINTS, simkaiFont, 9), y_position-75 );
            contentStream.drawString( "(O) 03-8747726, (R) 03-8747353" );
            contentStream.endText();
            
            contentStream.drawLine(10, y_position-90, DIMENSION_WIDTH_IN_POINTS-10, y_position-90);
            
            return contentStream;
        }   
        catch (Exception e)
        {
            e.printStackTrace();
            return null;            
        }
    }
    
    /**
     * @param document
     * @param page
     * @param contentStream
     * @param simkaiFont
     * @param y the y-coordinate of the first row
     * @param margin the padding on left and right of table
     * @param content a 2d array containing the table data
     * @param chineseColumn
     * @param textAlignment
     * @throws IOException
     */
    protected void drawTwoColumnTable(PDDocument document, 
                                        PDPage page, 
                                        PDPageContentStream contentStream, 
                                        PDFont simkaiFont,
                                        float y, 
                                        float margin, 
                                        String[][] content,
                                        boolean[] chineseColumn,
                                        int[] textAlignment) 
            throws IOException 
    {  
        this.drawTwoColumnTable(document, page, contentStream, simkaiFont, y, margin, content, false, chineseColumn, textAlignment, true, true);
    }
    
    /**
     * @param document
     * @param page
     * @param contentStream
     * @param simkaiFont
     * @param y the y-coordinate of the first row
     * @param margin the padding on left and right of table
     * @param content a 2d array containing the table data
     * @param chineseColumn
     * @param textAlignment
     * @throws IOException
     */
    protected void drawTwoColumnTable(PDDocument document, 
                                        PDPage page, 
                                        PDPageContentStream contentStream, 
                                        PDFont chineseFont,
                                        float y, 
                                        float margin, 
                                        String[][] content,
                                        boolean englishHeader,
                                        boolean[] chineseColumn,
                                        int[] textAlignment,
                                        boolean showHeader,
                                        boolean showHeaderLine) 
            throws IOException 
    {        
        final int rows = content.length;
        final int cols = content[0].length;
        final float rowHeight = 20f;
        final float tableWidth = 265;  // one-column: 535
        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth/(float)cols;
        final float cellMargin=5f;
        
        if (content.length < 2)
            return;
        
        if (showHeaderLine==true && content.length > 1)
        {
            // Draw header row line
            float nexty = y - 1;
            nexty-= rowHeight;
            contentStream.drawLine(margin+5, nexty, margin+15+tableWidth, nexty);
        }
        
        List<String> textList;
        
        float textx = margin+cellMargin;
        float texty = y-13;        
        for(int row = 0; row < content.length; row++)
        {
            if (row==0 && showHeader==false)
            {
                contentStream.beginText();
                contentStream.moveTextPositionByAmount( (float)(textx), texty );
                contentStream.drawString("");
                contentStream.endText();    
            }
            else
            {
                for(int col = 0 ; col < content[row].length; col++)
                {                
                    String text = content[row][col]==null ? "" : content[row][col];
                    textList = this.tokenizeText(text);
                    double increment_x = 0.0d;

                    int hanScriptCount = 0;
                    int asciiScriptCount = 0;
                    for (String subText : textList) {
                        if (this.containsHanScript(subText))
                            hanScriptCount++;
                        else
                            asciiScriptCount++;
                    }
                    
                    for (String subText : textList)
                    {
                        double textWidth = 0.0d;
                        contentStream.beginText();

                        if (this.containsHanScript(subText))
                        {                   
                            contentStream.setFont(chineseFont, 14);  
                            textWidth = chineseFont.getStringWidth( subText )/1000 * 10;
                            if (hanScriptCount > 0 && asciiScriptCount > 0)
                                textWidth = 18;
                            System.out.println( "[" + row + "," + col + "]: " + subText + " is HanScript. > width:" + textWidth ); 
                        }
                        else
                        {
                            contentStream.setFont(getFontPlain(), 14);
                            textWidth = getFontPlain().getStringWidth( subText )/1000 * 10;
                            if (hanScriptCount > 0 && asciiScriptCount > 0)
                                textWidth = 10;
                            
                            System.out.println( "[" + row + "," + col + "]: " + subText + " is not HanScript. > width:" + textWidth );
                        }

                        double adjustment_x = 0.0d;              
                        if (textAlignment[col]==TEXT_ALIGN_RIGHT)
                            adjustment_x = colWidth - textWidth;
                        else
                        if (textAlignment[col]==TEXT_ALIGN_CENTER)
                            adjustment_x = (colWidth - textWidth) / 2;
                        else
                        if (textAlignment[col]==TEXT_ALIGN_LEFT)
                            adjustment_x = 0.0d;

                        contentStream.moveTextPositionByAmount( (float)(textx + adjustment_x + increment_x), texty );
                        contentStream.drawString(subText);
                        contentStream.endText();                        

                        increment_x = increment_x + textWidth;
                    }

                    textx += colWidth;     
                }
            }
            texty-=rowHeight;
            textx = margin+cellMargin;
        }
    }
    
    /**
     * @param document
     * @param page
     * @param contentStream
     * @param simkaiFont
     * @param y the y-coordinate of the first row
     * @param margin the padding on left and right of table
     * @param content a 2d array containing the table data
     * @param chineseColumn
     * @param textAlignment
     * @throws IOException
     */
    protected void drawTable(PDDocument document, 
                            PDPage page, 
                            PDPageContentStream contentStream, 
                            PDFont simkaiFont,
                            float y, 
                            float margin, 
                            String[][] content,
                            boolean[] chineseColumn, 
                            int[] textAlignment) 
            throws IOException 
    {  
        this.drawTable(document, page, contentStream, simkaiFont, y, margin, content, false, chineseColumn, textAlignment, true, true, false);
    }
    
    /**
     * @param document
     * @param page
     * @param contentStream
     * @param simkaiFont
     * @param y the y-coordinate of the first row
     * @param margin the padding on left and right of table
     * @param content a 2d array containing the table data
     * @param chineseColumn
     * @param textAlignment
     * @throws IOException
     */
    protected void drawTable(PDDocument document, 
                            PDPage page, 
                            PDPageContentStream contentStream, 
                            PDFont simkaiFont,
                            float y, 
                            float margin, 
                            String[][] content,
                            boolean englishHeader,
                            boolean[] chineseColumn, 
                            int[] textAlignment,
                            boolean header,
                            boolean headerLine,
                            boolean footerLine) 
            throws IOException 
    {                
        final int rows = content.length;
        final int cols = content[0].length;
        final float rowHeight = 16f; // default 20f
        final float tableWidth = 575;
        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth/(float)cols;
        final float cellMargin=5f;
        
        if (headerLine==true && content.length > 1)
        {
            float nexty = y - 1;
            nexty-= rowHeight;
            contentStream.drawLine(margin+5, nexty, margin+15+tableWidth, nexty);
        }
        /*
        //draw the rows
        float nexty = y ;
        for (int i = 0; i <= rows; i++) 
        {
            contentStream.drawLine(margin, nexty, margin+tableWidth, nexty);
            nexty-= rowHeight;
        }

        //draw the columns
        float nextx = margin;
        for (int i = 0; i <= cols; i++) 
        {
            contentStream.drawLine(nextx, y, nextx, y-tableHeight);
            nextx += colWidth;
        }*/
        
        List<String> textList;
        
        float textx = margin+cellMargin;
        float texty = y-15;        
        for(int row = 0; row < content.length; row++)
        {
            if (row==0 && header==false)
            {                 
                contentStream.beginText();
                contentStream.moveTextPositionByAmount( (float)(textx), texty );
                contentStream.drawString("");
                contentStream.endText();  
            }
            else
            {
                for(int col = 0 ; col < content[row].length; col++)
                {                
                    String text = content[row][col]==null ? "" : content[row][col];
                    textList = this.tokenizeText(text);
                    double increment_x = 0.0d;

                    for (String subText : textList)
                    {
                        double textWidth;                        
                        contentStream.beginText();

                        if (this.containsHanScript(subText))
                        {
                            contentStream.setFont(simkaiFont, 12);  
                            textWidth = simkaiFont.getStringWidth( subText )/1000 * 10;
                        }
                        else
                        {
                            contentStream.setFont(getFontPlain(), 12);
                            textWidth = getFontPlain().getStringWidth( subText )/1000 * 10;
                        }


                        double adjustment_x = 0.0d;              
                        if (textAlignment[col]==TEXT_ALIGN_RIGHT)
                            adjustment_x = colWidth - textWidth;
                        else
                        if (textAlignment[col]==TEXT_ALIGN_CENTER)
                            adjustment_x = (colWidth - textWidth) / 2;
                        else
                        if (textAlignment[col]==TEXT_ALIGN_LEFT)
                            adjustment_x = 0.0d;

                        contentStream.moveTextPositionByAmount( (float)(textx + adjustment_x + increment_x), texty );
                        contentStream.drawString(subText);
                        contentStream.endText();                   

                        increment_x = increment_x + textWidth;
                    }

                    textx += colWidth;                
                }
            }
            
            texty-=rowHeight;
            textx = margin+cellMargin;
            
        }
        
        if (footerLine==true)
            contentStream.drawLine(margin+5, texty+rowHeight*2-5, margin+15+tableWidth, texty+rowHeight*2-5);
    }
    
    protected String trimString(String original, int charSize) 
    {        
        if (original == null)
            return "null";
        
        if (original.length() <= charSize)
            return original;
        else
            return original.substring(0, charSize) + "...";
    }
    
    protected String[][] trim2dArray(String[][] original, int startIndex, int endIndex) 
    {        
        if (original.length <= (endIndex-startIndex))
            return original;
        else 
        {
            if (endIndex > original.length-1)
                endIndex = original.length-1;
            
            int length = endIndex - startIndex + 1;
            String[][] trimmed = new String[length][original[0].length];
            
            for (int x=startIndex, index=0 ; x<=endIndex ; x++, index++) 
            {                
                for (int y=0 ; y<original[x].length ; y++) 
                {                                     
                    trimmed[index][y] = original[x][y];    
                }
            }
            
            return trimmed;
        }
    }
    
    public void openPDFonFly(String filePath) 
        throws IOException 
    {
        if (Desktop.isDesktopSupported()) 
        {
            try 
            {
                File myFile = new File(filePath);
                Desktop.getDesktop().open(myFile);
            } 
            catch (IOException ex) 
            {
                // no application registered for PDFs
                throw new IOException("Exception: No application registered for PDF.");
            }
        }
    }
    /*
    public static PrintService choosePrinter() 
    {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        if(printJob.printDialog()) 
        {
            return printJob.getPrintService();          
        }
        else 
        {
            return null;
        }
    }
    
    public static void printPDF(String fileName, PrintService printer)
        throws IOException, PrinterException 
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintService(printer);
        PDDocument doc = PDDocument.load(new File(fileName));
        doc.silentPrint(job);
    }*/
    
    public void printFile(String filename) 
    {
        PrintRequestAttributeSet pras;
        DocFlavor flavor;
        
        try
        {                  
            // Locate the default print service for this environment.
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            
            pras = new HashPrintRequestAttributeSet();
            flavor = DocFlavor.INPUT_STREAM.PDF;
            if (service != null) 
            {
                DocPrintJob job = service.createPrintJob();
                FileInputStream fis = new FileInputStream(filename);
                DocAttributeSet das = new HashDocAttributeSet();
                Doc doc = new SimpleDoc(fis, flavor, das);
                job.print(doc, pras);
                Thread.sleep(5000L);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            pras = null;
            flavor = null;
        }
    }
       
    
    public void printPDFtoPrinter(String filePath)
    {
        try 
        {
            InputStream is = new BufferedInputStream(new FileInputStream(filePath));
            
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            
            // Locate the default print service for this environment.
            PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            
            // Create and return a PrintJob capable of handling data from
            // any of the supported document flavors.
            DocPrintJob printJob = service.createPrintJob();
            
            // register a listener to get notified when the job is complete
            printJob.addPrintJobListener(new JobCompleteMonitor());
            
            // Construct a SimpleDoc with the specified
            // print data, doc flavor and doc attribute set.
            Doc doc = new SimpleDoc(is, flavor, null);
            
            // Print a document with the specified job attributes.
            printJob.print(doc, null);
            /*
            while (jobRunning) 
            {
                Thread.sleep(1000);
            }*/
            
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static boolean jobRunning = true;
    private static class JobCompleteMonitor 
        extends PrintJobAdapter
    {
        @Override
        public void printJobCompleted(PrintJobEvent jobEvent)
        {
            System.out.println( "Job Completed" );
            jobRunning = false;
        }
    }
    
    protected int xPositionAfterJustifyToCenter(String text, int totalWidth, PDFont simkaiFont, int fontSize)
    {        
        double textWidth = getTextWidth( text, simkaiFont, fontSize ); 
        int result = (int) ((totalWidth/2) - (textWidth/2));
               
        return result;
    }
    
    protected double getTextWidth(String text, PDFont simkaiFont, int fontSize)
    {
        try
        {
            if (containsHanScript(text))
                return simkaiFont.getStringWidth( text )/1000 * fontSize;
            else
                return getFontPlain().getStringWidth( text )/1000 * fontSize;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return 0.0d;
        }
    }
    
    protected boolean containsHanScript(String s) 
    {       
        if (s==null)
            return false;
        
        for (int i = 0; i < s.length(); ) 
        {
            int codepoint = s.codePointAt(i);
            //System.out.println("\t[" + i + "] - " + codepoint);
            
            i += Character.charCount(codepoint);
            if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) 
            {
                return true;
            }
        }
        return false;
    }
    
    protected List<String> tokenizeText(String text)
    {
        boolean[] hanScript = new boolean[text.length()];
        
        for (int i=0 ; i < text.length() ; )
        {
            int codepoint = text.codePointAt(i);
            
            if (codepoint > 127)
                hanScript[i] = true;
            else
                hanScript[i] = false;
            
            i += Character.charCount(codepoint);
        }
        
        List<String> stringList = new ArrayList();
        
        StringBuffer buffer = new StringBuffer();
        for (int i=0 ; i < hanScript.length ; i++)
        {
            if (i==0)
                buffer.append( text.charAt(i) );
            if (i!=0 && hanScript[i-1]==hanScript[i])
                buffer.append( text.charAt(i) );
            
            if (i!=0 && hanScript[i-1]!=hanScript[i])
            {
                stringList.add(buffer.toString());
                
                buffer = new StringBuffer();
                
                buffer.append( text.charAt(i) );
            }
        }
        
        stringList.add( buffer.toString() );
        
        return stringList;
    }
}
