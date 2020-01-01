package com.ryanworks.fishery.client.print;

import com.ryanworks.fishery.shared.bean.InTransactionBean;
import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.SupplierBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.util.LogBook;
import java.text.DecimalFormat;
import java.util.Calendar;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author Ryan C
 */
public class InboundPDFBoxPrinter 
    extends RootPDFBoxPrinter
{ 
    private static final InboundPDFBoxPrinter instance = new InboundPDFBoxPrinter();
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    
    private InboundPDFBoxPrinter() 
    { 
        super(); 
    }    
    public static InboundPDFBoxPrinter getInstance() { return instance; }
    
    public void init() {        
        // Init
        getFontFileSimkai();
        getFontFileSimhei();
        getFontPlain();
        getFontBold();
        getFontOblique();        
    }
    
    //--------------------------------------------------------------------------
        
    public void printReportSupplierTransactionReportChinese(String saveTo, SupplierBean supplier, InTransactionBean transactionBean, boolean showPrice)        
    {        
        try 
        {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );
            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai());
            
            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);
                        
            printPlainText(contentStream, simkaiFont,  10, 660, 14, "船户：");            
            printPlainText(contentStream, simkaiFont,  60, 660, 16, supplier.getShipNumber());  
            printPlainText(contentStream, simkaiFont, 160, 660, 16, supplier.getName());
            printPlainText(contentStream, simkaiFont,  10, 635, 14, "单号：");
            printPlainText(contentStream, simkaiFont,  60, 635, 14, transactionBean.getTransactionNo());
            
            printPlainText(contentStream, simkaiFont, 440, 660, 14, "单据类别：");  
            printPlainText(contentStream, simkaiFont, 520, 660, 14, "进货");             
            printPlainText(contentStream, simkaiFont, 440, 635, 14, "单据日期：");
            printPlainText(contentStream, simkaiFont, 520, 635, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(transactionBean.getDateTime()));
            //printPlainText(contentStream, simkaiFont, 320, 645, 12, "打印日期：");
            //printPlainText(contentStream, simkaiFont, 400, 645, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime()));
            //printPlainText(contentStream, simkaiFont, 485, 645, 12, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime()));

            printPlainText(contentStream, simkaiFont, 10, 20, 14, "打印日期："); 
            printPlainText(contentStream, simkaiFont, 90, 20, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime())); 
            printPlainText(contentStream, simkaiFont, 180, 20, 14, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime())); 
                
            if (showPrice)
            {     
                printPlainText(contentStream, simkaiFont, 410,  20, 16, "总额：");
                printPlainText(contentStream, simkaiFont, 460,  20, 16, "RM");
                printPlainText(contentStream, simkaiFont, 490,  20, 16, currencyFormatter.format(transactionBean.getTotalPrice()));
            }        

            int middle_index = 0;
            if ( (transactionBean.getLineList().size()%2)==1 )
                middle_index = (int) (transactionBean.getLineList().size()+1) / 2;
            else
                middle_index = (int) transactionBean.getLineList().size() / 2;

            String[][] reportArrayData1 = this.getReportSupplierTransactionArray( transactionBean, 0, middle_index, showPrice);    
            String[][] reportArrayData2 = this.getReportSupplierTransactionArray( transactionBean, middle_index, transactionBean.getLineList().size(), showPrice);    
            
            // Draw table on page #1        
            boolean[] chineseColumn1 = {false, true, false, false, false};
            int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
            drawTwoColumnTable(document, page, contentStream, simkaiFont, 610, 10, trim2dArray(reportArrayData1,0,19), chineseColumn1, textAlignment1);
            drawTwoColumnTable(document, page, contentStream, simkaiFont, 610, 320, trim2dArray(reportArrayData2,0,19), chineseColumn1, textAlignment1);
            
            // Make sure that the content stream is closed:
            contentStream.close();

            // Save the results and ensure that the document is properly closed:
            document.save( saveTo );
            document.close();
            
            LogBook.Log("PDF created");
        }
        catch (Exception e)
        {
            e.printStackTrace();            
            LogBook.Log(e.getMessage());
        }
    }
    
    public void printReportSupplierTransactionReportMalay(String saveTo, SupplierBean supplier, InTransactionBean transactionBean, boolean showPrice)        
    {        
        try 
        {                
            PDDocument document = new PDDocument();
            String[][] reportArrayData = this.getReportSupplierTransactionMalayArray( transactionBean, 0, transactionBean.getLineList().size(), showPrice);  
            
            int totalPageNumber = 0;
            for (int i = 0 ; i < reportArrayData.length ; i+=30) {
                totalPageNumber++;
            }
                        
            int startIndex = 0, endIndex = 24;
            for (int pageNumber = 1 ; pageNumber <= totalPageNumber ; pageNumber++) {
                PDPage page = new PDPage();
                page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
                document.addPage( page );

                // Create a new font object selecting one of the PDF base fonts
                PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

                // Start a new content stream which will "hold" the to be created content
                PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

                printPlainText(contentStream, simkaiFont,  10, 660, 12, "Pembekal");
                printPlainText(contentStream, simkaiFont, 120, 660, 12, supplier.getShipNumber());
                printPlainText(contentStream, simkaiFont, 200, 660, 12, supplier.getName());
                printPlainText(contentStream, simkaiFont,  10, 645, 12, "No. Penerimaan:");
                printPlainText(contentStream, simkaiFont, 120, 645, 12, transactionBean.getTransactionNo());
                printPlainText(contentStream, simkaiFont, 320, 660, 12, "Tarikh:");
                printPlainText(contentStream, simkaiFont, 420, 660, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(transactionBean.getDateTime()));
                printPlainText(contentStream, simkaiFont, 320, 645, 12, "Tarikh Cetak");
                printPlainText(contentStream, simkaiFont, 420, 645, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime()));
                printPlainText(contentStream, simkaiFont, 495, 645, 12, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime()));
                printPlainText(contentStream, simkaiFont, 10, 10, 14, "Page " + pageNumber + " of " + totalPageNumber);

                if (showPrice) {
                    printPlainText(contentStream, simkaiFont, 400,  10, 16, "Jumlah: RM");
                    printPlainText(contentStream, simkaiFont, 520,  10, 16, currencyFormatter.format(transactionBean.getTotalPrice()));
                }        

                boolean showHeaderLine = false, showFooterLine = false;
                if (pageNumber == 1)
                    showHeaderLine = true;
                //if (pageNumber == totalPageNumber)
                //    showFooterLine = true;
                
                boolean[] chineseColumn1 = {false, false, false, false, false};
                int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(
                    document, 
                    page, 
                    contentStream, 
                    simkaiFont, 
                    610, 
                    10, 
                    trim2dArray(reportArrayData, startIndex, endIndex), 
                    true, 
                    chineseColumn1, 
                    textAlignment1, 
                    true, 
                    showHeaderLine, 
                    showFooterLine);

                // Make sure that the content stream is closed:
                contentStream.close();
                
                startIndex = endIndex + 1;
                endIndex = startIndex + 29;
            }

            // Save the results and ensure that the document is properly closed:
            document.save( saveTo );
            document.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogBook.Log("exception: " + e.getMessage());
        }
    }
    
    public void printReportSupplierTransactionReportSaving(String saveTo, SupplierBean supplier, InTransactionBean transactionBean, boolean showPrice)
    {     
        try
        {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);
            
            printPlainText(contentStream, simkaiFont,  10, 660, 14, "船户：");            
            printPlainText(contentStream, simkaiFont,  60, 660, 16, supplier.getShipNumber());  
            printPlainText(contentStream, simkaiFont, 160, 660, 16, supplier.getName());
            printPlainText(contentStream, simkaiFont,  10, 635, 14, "单号：");
            printPlainText(contentStream, simkaiFont,  60, 635, 14, transactionBean.getTransactionNo());
            
            printPlainText(contentStream, simkaiFont, 440, 660, 14, "单据类别：");  
            printPlainText(contentStream, simkaiFont, 520, 660, 14, "储蓄");     
            printPlainText(contentStream, simkaiFont, 440, 635, 14, "单据日期：");
            printPlainText(contentStream, simkaiFont, 520, 635, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(transactionBean.getDateTime()));
            //printPlainText(contentStream, simkaiFont, 320, 635, 12, "打印日期：");
            //printPlainText(contentStream, simkaiFont, 400, 635, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime()));
            //printPlainText(contentStream, simkaiFont, 485, 635, 12, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime()));

            printPlainText(contentStream, simkaiFont, 10, 20, 14, "打印日期："); 
            printPlainText(contentStream, simkaiFont, 90, 20, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime())); 
            printPlainText(contentStream, simkaiFont, 180, 20, 14, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime())); 
                
            if (showPrice)
            {
                printPlainText(contentStream, simkaiFont, 410,  20, 16, "总额：");
                printPlainText(contentStream, simkaiFont, 460,  20, 16, "RM");
                
                double totalSaving = 0.0d;
                for (InTransactionLineBean lineBean : transactionBean.getLineList())
                    totalSaving = totalSaving + (lineBean.getSaving() * lineBean.getWeight());
                
                printPlainText(contentStream, simkaiFont, 490,  20, 16, currencyFormatter.format(totalSaving));
            }       

            int middle_index = 0;
            if ( (transactionBean.getLineList().size()%2)==1 )
                middle_index = (int) (transactionBean.getLineList().size()+1) / 2;
            else
                middle_index = (int) transactionBean.getLineList().size() / 2;

            String[][] reportArrayData1 = this.getReportSupplierTransactionSavingArray( transactionBean, 0, middle_index, showPrice);    
            String[][] reportArrayData2 = this.getReportSupplierTransactionSavingArray( transactionBean, middle_index, transactionBean.getLineList().size(), showPrice);    

            // Draw table on page #1        
            boolean[] chineseColumn1 = {false, true, false, false, false};
            int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
            drawTwoColumnTable(document, page, contentStream, simkaiFont, 610, 10, trim2dArray(reportArrayData1,0,19), chineseColumn1, textAlignment1);
            drawTwoColumnTable(document, page, contentStream, simkaiFont, 610, 320, trim2dArray(reportArrayData2,0,19), chineseColumn1, textAlignment1);

            // Make sure that the content stream is closed:
            contentStream.close();

            // Save the results and ensure that the document is properly closed:
            document.save( saveTo );
            document.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogBook.Log("exception: " + e.getMessage());
        }
    }
    
    public void printReportSupplierTransactionReportSavingMalay(String saveTo, SupplierBean supplier, InTransactionBean transactionBean, boolean showPrice)
    {     
        try
        {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);
            
            printPlainText(contentStream, simkaiFont,  10, 660, 12, "Pembekal:");            
            printPlainText(contentStream, simkaiFont, 120, 660, 12, supplier.getShipNumber());  
            printPlainText(contentStream, simkaiFont, 200, 660, 12, supplier.getName());
            printPlainText(contentStream, simkaiFont,  10, 645, 12, "No. Penerimaan:");
            printPlainText(contentStream, simkaiFont, 120, 645, 12, transactionBean.getTransactionNo());
            printPlainText(contentStream, simkaiFont, 320, 660, 12, "Tarikh Penerimaan:");
            printPlainText(contentStream, simkaiFont, 440, 660, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(transactionBean.getDateTime()));
            printPlainText(contentStream, simkaiFont, 320, 645, 12, "Tarikh Cetakan:");
            printPlainText(contentStream, simkaiFont, 440, 645, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime()));
            printPlainText(contentStream, simkaiFont, 525, 645, 12, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime()));

            if (showPrice)
            {
                printPlainText(contentStream, simkaiFont, 400,  20, 16, "Jumlah: RM");
                
                double totalSaving = 0.0d;
                for (InTransactionLineBean lineBean : transactionBean.getLineList())
                    totalSaving = totalSaving + (lineBean.getSaving() * lineBean.getWeight());
                
                printPlainText(contentStream, simkaiFont, 500,  20, 16, currencyFormatter.format(totalSaving));
            }       

            String[][] reportArrayData = this.getReportSupplierTransactionSavingMalayArray( transactionBean, 0, transactionBean.getLineList().size(), showPrice);     

            // Draw table on page #1        
            boolean[] chineseColumn1 = {false, false, false, false, false};
            int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
            drawTable(document, page, contentStream, simkaiFont, 610, 10, trim2dArray(reportArrayData,0,19), chineseColumn1, textAlignment1);

            // Make sure that the content stream is closed:
            contentStream.close();

            // Save the results and ensure that the document is properly closed:
            document.save( saveTo );
            document.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogBook.Log("exception: " + e.getMessage());
        }
    }
    
    private String[] getReportSupplierTransactionTableHeader() 
    {
        return new String[] {"鱼类", "价格", "重量", "银额"};
    }
    
    private String[][] getReportSupplierTransactionArray(InTransactionBean transaction, int startIndex, int endIndex, boolean showPrice) 
    {        
        //System.err.println("Two Column: Index from " + startIndex + " to " + endIndex);
        
        String[] header = getReportSupplierTransactionTableHeader();
        String[][] array = new String[endIndex-startIndex+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalAmount = 0.0d;
        int iIndex = 1;
        for (int i=startIndex ; i<endIndex ; i++)
        {
            InTransactionLineBean bean = transaction.getLineList().get(i);
            
            //array[iIndex][0] = bean.getCustomerId();
            array[iIndex][0] = bean.getItemNewName();
            array[iIndex][1] = showPrice ? currencyFormatter.format(bean.getUnitPrice()) : "";
            array[iIndex][2] = currencyFormatter.format(bean.getWeight());
            array[iIndex][3] = showPrice ? currencyFormatter.format(bean.getUnitPrice()*bean.getWeight()) : "";
            
            iIndex++;
        }     
        
        return array;
    }
    
    private String[] getReportSupplierTransactionMalayTableHeader() 
    {
        return new String[] {"Ikan", "Harga", "Berat", "Jumlah"};
    }
    
    private String[][] getReportSupplierTransactionMalayArray(InTransactionBean transaction, int startIndex, int endIndex, boolean showPrice) 
    {        
        //System.err.println("Two Column: Index from " + startIndex + " to " + endIndex);
        
        String[] header = getReportSupplierTransactionMalayTableHeader();
        String[][] array = new String[endIndex-startIndex+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalAmount = 0.0d;
        int iIndex = 1;
        for (int i=startIndex ; i<endIndex ; i++)
        {
            InTransactionLineBean bean = transaction.getLineList().get(i);
            
            //array[iIndex][0] = bean.getCustomerId();
            array[iIndex][0] = bean.getItemNewName() + " - " + bean.getItemNameBm();
            array[iIndex][1] = showPrice ? currencyFormatter.format(bean.getUnitPrice()) : "";
            array[iIndex][2] = currencyFormatter.format(bean.getWeight());
            array[iIndex][3] = showPrice ? currencyFormatter.format(bean.getUnitPrice()*bean.getWeight()) : "";
            
            iIndex++;
        }     
        
        return array;
    }
    
    private String[] getReportSupplierTransactionSavingTableHeader() 
    {
        return new String[] {"鱼类", "储蓄", "重量", "总额"};
    }
    
    private String[][] getReportSupplierTransactionSavingArray(InTransactionBean transaction, int startIndex, int endIndex, boolean showPrice) 
    {        
        //System.err.println("Two Column: Index from " + startIndex + " to " + endIndex);
        
        String[] header = getReportSupplierTransactionSavingTableHeader();
        String[][] array = new String[endIndex-startIndex+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalAmount = 0.0d;
        int iIndex = 1;
        for (int i=startIndex ; i<endIndex ; i++)
        {
            InTransactionLineBean bean = transaction.getLineList().get(i);
            
            //array[iIndex][0] = bean.getCustomerId();
            array[iIndex][0] = bean.getItemNewName();
            array[iIndex][1] = showPrice ? currencyFormatter.format(bean.getSaving()) : "";
            array[iIndex][2] = currencyFormatter.format(bean.getWeight());
            array[iIndex][3] = showPrice ? currencyFormatter.format(bean.getSaving()*bean.getWeight()) : "";
            
            iIndex++;
        }     
        
        return array;
    }
    
    private String[] getReportSupplierTransactionSavingMalayTableHeader() 
    {
        return new String[] {"Ikan", "Simpanan", "Berat", "Jumlah"};
    }
    
    private String[][] getReportSupplierTransactionSavingMalayArray(InTransactionBean transaction, int startIndex, int endIndex, boolean showPrice) 
    {        
        //System.err.println("Two Column: Index from " + startIndex + " to " + endIndex);
        
        String[] header = getReportSupplierTransactionSavingMalayTableHeader();
        String[][] array = new String[endIndex-startIndex+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalAmount = 0.0d;
        int iIndex = 1;
        for (int i=startIndex ; i<endIndex ; i++)
        {
            InTransactionLineBean bean = transaction.getLineList().get(i);
            
            //array[iIndex][0] = bean.getCustomerId();
            array[iIndex][0] = bean.getItemNewName() + " - " + bean.getItemNameBm();
            array[iIndex][1] = showPrice ? currencyFormatter.format(bean.getSaving()) : "";
            array[iIndex][2] = currencyFormatter.format(bean.getWeight());
            array[iIndex][3] = showPrice ? currencyFormatter.format(bean.getSaving()*bean.getWeight()) : "";
            
            iIndex++;
        }     
        
        return array;
    }
}
