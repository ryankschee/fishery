package com.ryanworks.fishery.client.print;

import com.ryanworks.fishery.client.delegate.SupplierDelegate;
import com.ryanworks.fishery.shared.bean.*;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.util.DateTimeFormatter;
import com.ryanworks.fishery.shared.util.LogBook;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
public class ControlPDFBoxPrinter 
    extends RootPDFBoxPrinter
{ 
    private static final ControlPDFBoxPrinter instance = new ControlPDFBoxPrinter();
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    
    private ControlPDFBoxPrinter() { super(); }    
    public static ControlPDFBoxPrinter getInstance() { return instance; }
    
    //--------------------------------------------------------------------------
        
    public void printReportCustomerMonthlyReport(String saveTo, long startDate, long endDate, CustomerBean customer, List<CustomerReportLineBean> reportLineList)        
    {
        try 
        {
            String[][] reportArrayData = this.getReportCustomerMonthlyArray( reportLineList );    

            PDDocument document = new PDDocument();
            
            int totalPageNumber = 0;
            for (int i = 0 ; i < reportArrayData.length ; i+=30) {
                totalPageNumber++;
            }
            
            int startIndex = 0, endIndex = 29;
            for (int pageNumber = 1 ; pageNumber <= totalPageNumber ; pageNumber++) {
            
                PDPage page = new PDPage();
                page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
                document.addPage( page );

                // Create a new font object selecting one of the PDF base fonts
                PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

                // Start a new content stream which will "hold" the to be created content
                PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

                printPlainText(contentStream, simkaiFont, 245, 660, 18, "客户月结单"); 
                printPlainText(contentStream, simkaiFont, 176, 640, 18, "期限"); 
                printPlainText(contentStream, simkaiFont, 220, 640, 18, MyDatePicker.SIMPLE_DATE_FORMAT.format( startDate ) + " - " + MyDatePicker.SIMPLE_DATE_FORMAT.format( endDate )); 
                printPlainText(contentStream, simkaiFont,  10, 615, 18, "客户："); 
                printPlainText(contentStream, simkaiFont,  60, 615, 18, customer.getName()); 
                printPlainText(contentStream, simkaiFont, 360,  10, 14, "打印日期："); 
                printPlainText(contentStream, simkaiFont, 440,  10, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime())); 
                printPlainText(contentStream, simkaiFont, 525,  10, 14, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime()));
                printPlainText(contentStream, simkaiFont,  10,  10, 14, "Page " + pageNumber + " of " + totalPageNumber);

                boolean showHeaderLine = false, showFooterLine = false;
                if (pageNumber == 1)
                    showHeaderLine = true;
                if (pageNumber == totalPageNumber)
                    showFooterLine = true;
                // Draw table on page #1        
                boolean[] chineseColumn1 = {false, false, false, false, false};
                int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(
                        document, 
                        page, 
                        contentStream, 
                        simkaiFont, 
                        590, 
                        10, 
                        trim2dArray(reportArrayData, startIndex, endIndex), 
                        false, 
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
    
    public void printReportSingleSupplier(String saveTo, long startDate, long endDate, SupplierBean supplier,
                                             List<InTransactionBean> inTransactionList, List<SupplierCashBean> cashList, List<SupplierChequeBean> chequeList, 
                                             List<SupplierFuelBean> fuelList, List<SupplierMiscBean> miscList)
    {
        try
        {
            String[][] inTransactionArrayData = this.getReportSingleSupplierInTransactionArray( inTransactionList );
            String[][] cashArrayData = this.getReportSingleSupplierCashArray( cashList );
            String[][] chequeArrayData = this.getReportSingleSupplierChequeArray( chequeList );
            String[][] fuelArrayData = this.getReportSingleSupplierFuelArray( fuelList );
            String[][] miscArrayData = this.getReportSingleSupplierMiscArray( miscList );

            double totalDebit = 0.0d;
            double totalCredit = 0.0d;
            for (InTransactionBean bean : inTransactionList)
                totalDebit += bean.getTotalPrice();
            for (SupplierCashBean bean : cashList)
                totalDebit += bean.getCashAmount();
            for (SupplierChequeBean bean : chequeList)
                totalCredit += bean.getChequeAmount();
            for (SupplierFuelBean bean : fuelList)
                totalCredit += bean.getFuelTotalPrice();
            for (SupplierMiscBean bean : miscList)
                totalCredit += bean.getMiscAmount();

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

            printPlainText(contentStream, simkaiFont, 245, 660, 16, "船户月结单"); 
            printPlainText(contentStream, simkaiFont, 196, 640, 13, "期限："); 
            printPlainText(contentStream, simkaiFont, 240, 640, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format( startDate ) + " - " + MyDatePicker.SIMPLE_DATE_FORMAT.format( endDate )); 
            printPlainText(contentStream, simkaiFont, 30, 620, 14, "船户"); 
            printPlainText(contentStream, simkaiFont, 80, 620, 12, supplier.getName()); 
            printPlainText(contentStream, simkaiFont, 30, 600, 14, "船号"); 
            printPlainText(contentStream, simkaiFont, 80, 600, 12, supplier.getShipNumber());   

            // Draw table on page #1      
            int y_offset = 580;
            int tableCount = 0;
            int tableRowCount = 0;
            int tableRowHeight = 16;
            int tableSpace = 16;

            if (inTransactionList.size() > 0)
            {
                boolean[] chineseColumn1 = {false, false, false, false, false};
                int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset, 10, trim2dArray(inTransactionArrayData,0,19), false, chineseColumn1, textAlignment1, true, true, false);   
                tableRowCount = inTransactionList.size();
                tableCount++;
            }

            if (cashList.size() > 0)
            {
                y_offset = y_offset - (tableRowCount*tableRowHeight) - tableSpace;        
                boolean[] chineseColumn2 = {false, false, false, false, false};
                int[] textAlignment2 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset, 10, trim2dArray(cashArrayData,0,19), false, chineseColumn2, textAlignment2, true, false, false);     
                tableRowCount = cashList.size();
                tableCount++;
            }

            if (chequeList.size() > 0)
            {
                y_offset = y_offset - (tableRowCount*tableRowHeight) - tableSpace;       
                boolean[] chineseColumn3 = {false, false, false, false, false};
                int[] textAlignment3 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset-tableSpace, 10, trim2dArray(chequeArrayData,0,19), false, chineseColumn3, textAlignment3, true, false, false);      
                tableRowCount = chequeList.size();
                tableCount++;  
            }

            if (fuelList.size() > 0)
            {
                y_offset = y_offset - (tableRowCount*tableRowHeight) - tableSpace;       
                boolean[] chineseColumn4 = {false, false, false, false, false};
                int[] textAlignment4 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset-tableSpace, 10, trim2dArray(fuelArrayData,0,19), false, chineseColumn4, textAlignment4, true, false, false);  
                tableRowCount = fuelList.size();
                tableCount++; 
            }

            if (miscList.size() > 0)
            {
                y_offset = y_offset - (tableRowCount*tableRowHeight) - tableSpace;          
                boolean[] chineseColumn5 = {false, false, false, false, false};
                int[] textAlignment5 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset-tableSpace, 10, trim2dArray(miscArrayData,0,19), false, chineseColumn5, textAlignment5, true, false, false);
                tableRowCount = miscList.size() + 2;   
                tableCount++;
            }

            Calendar summary_startDay = Calendar.getInstance();
            summary_startDay.setTimeInMillis(startDate);
            summary_startDay.set(Calendar.DATE, summary_startDay.get(Calendar.DATE)-2);

            SupplierSummaryBean summary = SupplierDelegate.getInstance().getSummaryBySupplierAndDate(supplier.getId(), summary_startDay.getTimeInMillis());

            Date date = new Date();
            date.setTime(startDate);
            
            if (tableRowCount > 0)
            {
                String[][] summaryData = new String[6][5];

                summaryData[0][3] = "去项"; 
                summaryData[0][4] = "来项"; 

                summaryData[1][2] = "总额：";            
                summaryData[1][3] = currencyFormatter.format( totalCredit );
                summaryData[1][4] = currencyFormatter.format( totalDebit );

                double thisMonthBalance = totalDebit - totalCredit;
                summaryData[2][2] = "存余：";
                summaryData[2][4] = currencyFormatter.format( thisMonthBalance );

                summaryData[3][2] = "红利：";
                summaryData[3][4] = currencyFormatter.format( 0 );

                if (summary.getBalance() < 0)
                    summaryData[4][2] = "上欠：";
                else
                    summaryData[4][2] = "上存：";
                summaryData[4][1] = MyDatePicker.SIMPLE_DATE_FORMAT.format( summary.getEndDay());
                summaryData[4][4] = currencyFormatter.format( summary.getBalance() );

                if ( (totalDebit - totalCredit + summary.getBalance()) < 0 )
                    summaryData[5][2] = "尚欠：";
                else
                    summaryData[5][2] = "尚存：";
                summaryData[5][1] = MyDatePicker.SIMPLE_DATE_FORMAT.format( endDate );
                summaryData[5][4] = currencyFormatter.format( thisMonthBalance + summary.getBalance() );

                y_offset = y_offset - ((tableRowCount+1)*20);
                boolean[] chineseColumn6 = {false, false, true, false, false};
                int[] textAlignment6 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset-(tableCount*10), 10, summaryData, chineseColumn6, textAlignment6);   
            }
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
    
    public void printReportFullSuppliers(String saveTo, long startDate, long endDate, Object[][] tableData)
    {
        try
        {
            String[][] fullSuppliersArrayData = this.getReportFullSuppliersArray( tableData );    

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

            printPlainText(contentStream, simkaiFont, 245, 660, 18, "全船半月结单"); 
            printPlainText(contentStream, simkaiFont, 196, 640, 14, "期限"); 
            printPlainText(contentStream, simkaiFont, 240, 640, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format( startDate ) + " - " + MyDatePicker.SIMPLE_DATE_FORMAT.format( endDate )); 

            if (fullSuppliersArrayData.length > 1)
            {
                // Draw table on page #1        
                boolean[] chineseColumn1 = {true, false, false, false, false, false, false, false};
                int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_CENTER, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(
                    document, 
                    page, 
                    contentStream, 
                    simkaiFont, 
                    620, 
                    30, 
                    trim2dArray(fullSuppliersArrayData,0,19), 
                    chineseColumn1, 
                    textAlignment1);
            }

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
    
    public void printReportStockInOut(String saveTo, List<ItemBean> itemList, long dateInMillis, List<ReportStockInBean> stockInList, List<ReportStockOutBean> stockOutList) 
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

            printPlainText(contentStream, simkaiFont, 260, 660, 18, "存货报表"); 
            printPlainText(contentStream, simkaiFont,  30, 640, 10, "报表日期："); 
            printPlainText(contentStream, simkaiFont,  80, 640, 10, MyDatePicker.SIMPLE_DATE_FORMAT.format( dateInMillis )); 
            printPlainText(contentStream, simkaiFont, 455, 640, 10, "打印日期："); 
            printPlainText(contentStream, simkaiFont, 505, 640, 10, MyDatePicker.SIMPLE_DATE_FORMAT.format( Calendar.getInstance().getTimeInMillis() )); 

            int y_offset = 590;

            for (ItemBean item : itemList)
            {   
                String[][] stockInArrayData = this.getReportStockInArray( item, stockInList );

                // if the array has the data (more than 2, one is header, one is footer)
                if (stockInArrayData.length > 2)
                {                 
                    printPlainText(contentStream, simkaiFont, 30, y_offset, 14, "进货报表"); 
                    
                    // Draw table on page #1
                    boolean[] chineseColumn1 = {true, false, false, false, false, false};
                    int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT};
                    drawTable(document, page, contentStream, simkaiFont, y_offset-5, 10, trim2dArray(stockInArrayData,0,19), chineseColumn1, textAlignment1);

                    y_offset = y_offset - 20 - (stockInArrayData.length*16);
                }

                String[][] stockOutArrayData = this.getReportStockOutArray( item, stockOutList );

                if (stockOutArrayData.length > 2)
                {
                    printPlainText(contentStream, simkaiFont, 30, y_offset-20, 14, "出货报表");

                    boolean[] chineseColumn2 = {true, false, false, false};
                    int[] textAlignment2 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT};
                    drawTable(document, page, contentStream, simkaiFont, y_offset-25, 10, trim2dArray(stockOutArrayData,0,19), chineseColumn2, textAlignment2);

                    y_offset = y_offset - 20 - (stockOutArrayData.length*16);
                }
            }

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
    
    public void printReportSavingSupplier(
            String saveTo, 
            long startDate, 
            long endDate, 
            SupplierBean supplier,
            List<SupplierCashBean> cashList, 
            List<SupplierChequeBean> chequeList, 
            List<SupplierFuelBean> fuelList,
            List<SupplierMiscBean> miscList, 
            List<InTransactionBean> savingList, 
            List<SupplierWithdrawalBean> withdrawalList)
    {
        try
        {
            String[][] savingArrayData = this.getReportSavingSupplierInTransactionArray( savingList );
            String[][] withdrawalArrayData = this.getReportSingleSupplierWithdrawalArray( withdrawalList );

            double totalSaving = 0.0d;
            double totalWithdrawal = 0.0d;
            for (InTransactionBean bean : savingList)
                totalSaving += bean.getTotalSaving();
            for (SupplierWithdrawalBean bean : withdrawalList)
                totalWithdrawal += bean.getCashAmount();
            
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

            printPlainText(contentStream, simkaiFont, 235, 660, 18, "船户月结单：储蓄"); 
            printPlainText(contentStream, simkaiFont, 196, 640, 14, "期限"); 
            printPlainText(contentStream, simkaiFont, 240, 640, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format( startDate ) + " - " + MyDatePicker.SIMPLE_DATE_FORMAT.format( endDate )); 
            //printPlainText(contentStream, simkaiFont,  30, 620, 12, supplier.getId()); 
            printPlainText(contentStream, simkaiFont, 30, 620, 14, "船户："); 
            printPlainText(contentStream, simkaiFont, 80, 620, 12, supplier.getName()); 
            printPlainText(contentStream, simkaiFont, 30, 600, 14, "船号"); 
            printPlainText(contentStream, simkaiFont, 80, 600, 12, supplier.getShipNumber());   

            // Draw table on page #1      
            int y_offset = 580;
            int tableCount = 0;
            int tableRowCount = 0;

            if (savingList.size() > 0)
            {
                boolean[] chineseColumn1 = {false, false, false, false, false};
                int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset, 10, trim2dArray(savingArrayData,0,19), chineseColumn1, textAlignment1);   
                tableRowCount = savingList.size() + 2;
                tableCount++;
            }

            if (withdrawalList.size() > 0)
            {
                y_offset = y_offset - (tableRowCount*20);        
                boolean[] chineseColumn2 = {false, false, false, false, false};
                int[] textAlignment2 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset-(tableCount*8), 10, trim2dArray(withdrawalArrayData,0,19), false, chineseColumn2, textAlignment2, true, false, false);     
                tableRowCount = withdrawalList.size() + 2;
                tableCount++;
            }
            
            Calendar startDay = Calendar.getInstance();
            startDay.setTimeInMillis(startDate);
            startDay.set(Calendar.DATE, startDay.get(Calendar.DATE)-2);

            SupplierSummarySavingBean summarySaving = SupplierDelegate.getInstance().getSummarySavingBySupplierAndDate(supplier.getId(), startDay.getTimeInMillis());

            if (tableRowCount > 0)
            {
                String[][] summaryData = new String[6][5];

                double totalBalance = totalSaving - totalWithdrawal;
                summaryData[1][2] = "这半月";
                summaryData[1][3] = "总额：";            
                summaryData[1][4] = currencyFormatter.format( totalBalance );
                
                summaryData[2][1] = MyDatePicker.SIMPLE_DATE_FORMAT.format( summarySaving.getEndDay());
                summaryData[2][2] = "上半月";
                if ( summarySaving.getBalance() < 0 )
                    summaryData[2][3] = "尚欠：";
                else
                    summaryData[2][3] = "尚存：";                
                summaryData[2][4] = currencyFormatter.format( summarySaving.getBalance() );

                summaryData[3][1] = MyDatePicker.SIMPLE_DATE_FORMAT.format( endDate );
                summaryData[3][2] = "这半月";
                if ( (totalSaving - totalWithdrawal + summarySaving.getBalance()) < 0 )
                    summaryData[3][3] = "尚欠：";
                else
                    summaryData[3][3] = "尚存：";
                summaryData[3][4] = currencyFormatter.format( totalSaving - totalWithdrawal + summarySaving.getBalance() );

                y_offset = y_offset - ((tableRowCount+1)*18);
                boolean[] chineseColumn6 = {false, false, true, false, false};
                int[] textAlignment6 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                drawTable(document, page, contentStream, simkaiFont, y_offset-(tableCount*10), 10, summaryData, chineseColumn6, textAlignment6);   
            }
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
    
    private String[] getReportStockInTableHeader() 
    {
        return new String[] {"鱼类","船户","单号","客户","桶号","重量"};
    }
    
    private String[][] getReportStockInArray(ItemBean item, List<ReportStockInBean> list) 
    {        
        String[] header = getReportStockInTableHeader();
        String[][] array = new String[list.size()+2][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalWeight = 0.0d;
        int iIndex = 1;
        for (ReportStockInBean bean : list) 
        {
            if (item.getName().equalsIgnoreCase(bean.getItemName()))
            {
                array[iIndex][0] = bean.getItemNewName();
                array[iIndex][1] = bean.getSupplierId();
                array[iIndex][2] = bean.getInvoiceNo();
                array[iIndex][3] = bean.getCustomerId();
                array[iIndex][4] = bean.getBucketNo();
                array[iIndex][5] = String.valueOf(bean.getWeight());

                totalWeight += bean.getWeight();
                iIndex++;
            }
        }
                
        array[iIndex][4] = "Total";
        array[iIndex][5] = String.valueOf(totalWeight);      
        
        String[][] filteredArray = new String[(iIndex-1)+2][header.length];
        for (int i=0 ; i<filteredArray.length ; i++)
        {
            filteredArray[i][0] = array[i][0];
            filteredArray[i][1] = array[i][1];
            filteredArray[i][2] = array[i][2];
            filteredArray[i][3] = array[i][3];
            filteredArray[i][4] = array[i][4];
            filteredArray[i][5] = array[i][5];
        }        
        
        return filteredArray;
    }
    
    private String[] getReportStockOutTableHeader() 
    {
        return new String[] {"鱼类","客户","桶号","重量"};
    }
    
    private String[][] getReportStockOutArray(ItemBean item, List<ReportStockOutBean> list) 
    {        
        String[] header = getReportStockOutTableHeader();
        String[][] array = new String[list.size()+2][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalWeight = 0.0d;
        int iIndex = 1;
        for (ReportStockOutBean bean : list) 
        {
            if (item.getName().equalsIgnoreCase(bean.getItemName()))
            {
                array[iIndex][0] = bean.getItemNewName();
                array[iIndex][1] = bean.getCustomerId();
                array[iIndex][2] = bean.getBucketNo();
                array[iIndex][3] = String.valueOf(bean.getWeight());

                totalWeight += bean.getWeight();
                iIndex++;
            }
        }
                
        array[iIndex][2] = "Total";
        array[iIndex][3] = String.valueOf(totalWeight);        
        
        String[][] filteredArray = new String[(iIndex-1)+2][header.length];
        for (int i=0 ; i<filteredArray.length ; i++)
        {
            filteredArray[i][0] = array[i][0];
            filteredArray[i][1] = array[i][1];
            filteredArray[i][2] = array[i][2];
            filteredArray[i][3] = array[i][3];
        }   
        
        return filteredArray;
    }
    
    private String[] getReportFullSuppliersTableHeader() 
    {
        return new String[] {"船户","单次","来货","来银","柴油","支银","什费","存／欠"};
    }
    
    private String[][] getReportFullSuppliersArray(Object[][] tableData) 
    {        
        String[] header = getReportFullSuppliersTableHeader();
        String[][] array = new String[tableData.length+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalWeight = 0.0d;
        int iIndex = 1;
        for (Object[] bean : tableData) 
        {
            array[iIndex][0] = bean[0]==null ? "-" : bean[0].toString();
            array[iIndex][1] = bean[1]==null ? "-" : bean[1].toString();
            array[iIndex][2] = bean[2]==null ? "-" : bean[2].toString();
            array[iIndex][3] = bean[3]==null ? "-" : bean[3].toString();
            array[iIndex][4] = bean[4]==null ? "-" : bean[4].toString();
            array[iIndex][5] = bean[5]==null ? "-" : bean[5].toString();
            array[iIndex][6] = bean[6]==null ? "-" : bean[6].toString();
            array[iIndex][7] = bean[7]==null ? "-" : bean[7].toString();
                        
            iIndex++;
        }                
        
        return array;
    }
    
    private String[] getReportSingleSupplierInTransactionTableHeader() 
    {
        return new String[] {"日期","摘要","数额","去项","来项"};
    }
    
    private String[][] getReportSingleSupplierInTransactionArray(List<InTransactionBean> list) 
    {        
        String[] header = getReportSingleSupplierInTransactionTableHeader();
        String[][] array = new String[list.size()+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalAmount = 0.0d;
        int iIndex = 1;
        for (InTransactionBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getDateTime());
            array[iIndex][1] = "来货： " + (bean.getTransactionNo()==null || bean.getTransactionNo().length()==0 ? " - " : bean.getTransactionNo());
            array[iIndex][2] = currencyFormatter.format(bean.getTotalPrice());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getTotalPrice();
            iIndex++;
        }
                
        if (iIndex > 0)
            array[iIndex-1][4] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportSavingSupplierInTransactionTableHeader() 
    {
        return new String[] {"日期","单号","数额","去项","来项"};
    }
    
    private String[][] getReportSavingSupplierInTransactionArray(List<InTransactionBean> list) 
    {        
        String[] header = getReportSavingSupplierInTransactionTableHeader();
        String[][] array = new String[list.size()+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalAmount = 0.0d;
        int iIndex = 1;
        for (InTransactionBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getDateTime());
            array[iIndex][1] = "储蓄：　" + (bean.getTransactionNo()==null || bean.getTransactionNo().length()==0 ? " - " : bean.getTransactionNo());
            array[iIndex][2] = currencyFormatter.format(bean.getTotalSaving());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getTotalSaving();
            iIndex++;
        }
               
        if (iIndex > 0) 
            array[iIndex-1][4] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportSingleSupplierCashTableHeader() 
    {
        return new String[] {"来银","细节","数额","",""};
    }
    
    private String[][] getReportSingleSupplierCashArray(List<SupplierCashBean> list) 
    {        
        String[] header = getReportSingleSupplierCashTableHeader();
        String[][] array = new String[list.size()][header.length];
        
        //for (int i=0 ; i<header.length ; i++) {
        //    array[0][i] = header[i];
        //}
        
        double totalAmount = 0.0d;
        int iIndex = 0;
        for (SupplierCashBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getCashDate());
            array[iIndex][1] = "来银： " + (bean.getCashDesc()==null || bean.getCashDesc().length()==0 ? "-" : bean.getCashDesc());
            array[iIndex][2] = currencyFormatter.format(bean.getCashAmount());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getCashAmount();
            iIndex++;
        }
        
        if (iIndex > 0)
            array[iIndex-1][4] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportSingleSupplierChequeTableHeader() 
    {
        return new String[] {"支银","银号","数额","",""};
    }
    
    private String[][] getReportSingleSupplierChequeArray(List<SupplierChequeBean> list) 
    {        
        String[] header = getReportSingleSupplierChequeTableHeader();
        String[][] array = new String[list.size()][header.length];
                
        //for (int i=0 ; i<header.length ; i++) {
        //    array[0][i] = header[i];
        //}
        
        double totalAmount = 0.0d;
        int iIndex = 0;
        for (SupplierChequeBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getChequeDate());
            array[iIndex][1] = "支银： " + (bean.getChequeNo()==null || bean.getChequeNo().length()==0 ? "-" : bean.getChequeNo());
            array[iIndex][2] =  currencyFormatter.format(bean.getChequeAmount());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getChequeAmount();
            iIndex++;
        }
                
        if (iIndex > 0)
            array[iIndex-1][3] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportSingleSupplierFuelTableHeader() 
    {
        return new String[] {"柴油","公升","数额","",""};
    }
    
    private String[][] getReportSingleSupplierFuelArray(List<SupplierFuelBean> list) 
    {        
        String[] header = getReportSingleSupplierFuelTableHeader();
        String[][] array = new String[list.size()][header.length];
        
        //for (int i=0 ; i<header.length ; i++) {
        //   array[0][i] = header[i];
        //}
        
        double totalAmount = 0.0d;
        int iIndex = 0;
        for (SupplierFuelBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getFuelDate());
            array[iIndex][1] = "柴油： " + currencyFormatter.format(bean.getFuelQuantity()) + "L";
            array[iIndex][2] = currencyFormatter.format(bean.getFuelTotalPrice());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getFuelTotalPrice();
            iIndex++;
        }
            
        if (iIndex > 0)    
            array[iIndex-1][3] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportSingleSupplierMiscTableHeader() 
    {
        return new String[] {"什费","细节","数额","",""};
    }
    
    private String[][] getReportSingleSupplierMiscArray(List<SupplierMiscBean> list) 
    {        
        String[] header = getReportSingleSupplierMiscTableHeader();
        String[][] array = new String[list.size()][header.length];
        
        //for (int i=0 ; i<header.length ; i++) {
        //    array[0][i] = header[i];
        //}
        
        double totalAmount = 0.0d;
        int iIndex = 0;
        for (SupplierMiscBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getMiscDate());
            array[iIndex][1] = "什费： " + (bean.getMiscDesc()==null || bean.getMiscDesc().length()==0 ? "-" : bean.getMiscDesc());
            array[iIndex][2] = currencyFormatter.format(bean.getMiscAmount());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getMiscAmount();
            iIndex++;
        }
            
        if (iIndex > 0)    
            array[iIndex-1][3] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportSingleSupplierWithdrawalTableHeader() 
    {
        return new String[] {"什费","细节","数额","",""};
    }
    
    private String[][] getReportSingleSupplierWithdrawalArray(List<SupplierWithdrawalBean> list) 
    {        
        String[] header = getReportSingleSupplierWithdrawalTableHeader();
        String[][] array = new String[list.size()][header.length];
        
        //for (int i=0 ; i<header.length ; i++) {
        //    array[0][i] = header[i];
        //}
        
        double totalAmount = 0.0d;
        int iIndex = 0;
        for (SupplierWithdrawalBean bean : list) 
        {
            array[iIndex][0] = MyDatePicker.SIMPLE_DATE_FORMAT.format(bean.getCashDate());
            array[iIndex][1] = "提款： " + (bean.getCashDesc()==null || bean.getCashDesc().length()==0 ? "-" : bean.getCashDesc());
            array[iIndex][2] = currencyFormatter.format(bean.getCashAmount());
            array[iIndex][3] = "";
            array[iIndex][4] = "";
            
            totalAmount += bean.getCashAmount();
            iIndex++;
        }
            
        if (iIndex > 0)    
            array[iIndex-1][3] = currencyFormatter.format(totalAmount);        
        
        return array;
    }
    
    private String[] getReportCustomerMonthlyTableHeader() 
    {
        return new String[] {"日期","项目","去项","来项","结存"};
    }
    
    private String[][] getReportCustomerMonthlyArray(List<CustomerReportLineBean> list) 
    {        
        String[] header = getReportCustomerMonthlyTableHeader();
        String[][] array = new String[list.size()+2][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        double totalCredit = 0.0d;
        double totalDebit = 0.0d;
        double totalBalance = 0.0d;
        int iIndex = 1;
        for (CustomerReportLineBean bean : list) 
        {
            if (bean.getLineObject() instanceof CustomerSummaryBean)
            {
                totalBalance = ((CustomerSummaryBean)bean.getLineObject()).getBalance();
                
                Calendar dateTime = bean.getDateTime();
                dateTime.set(Calendar.MONTH, dateTime.get(Calendar.MONTH));
                
                array[1][0] = DateTimeFormatter.formatDateTime_Day( dateTime.getTimeInMillis() );
                array[1][1] = "承接上月"; // 承接上月
                array[1][2] = "";
                array[1][3] = "";
                array[1][4] = currencyFormatter.format( totalBalance );
            }
        }
        
        iIndex = 2;
        for (CustomerReportLineBean bean : list) 
        {
            boolean recordAdded = false;
                        
            if (bean.getLineObject() instanceof SalesBean)
            {
                if ("".equalsIgnoreCase( ((SalesBean)bean.getLineObject()).getInvoiceNo() ))
                {
                    System.err.println("Skip Sales Record: " + ((SalesBean)bean.getLineObject()).getId() + ", due to empty invoice number.");
                    //continue;
                }
                
                totalBalance = totalBalance - ((SalesBean)bean.getLineObject()).getTotalPrice();
                
                array[iIndex][0] = DateTimeFormatter.formatDateTime_Day( bean.getDateTime().getTime() );
                array[iIndex][1] = ((SalesBean)bean.getLineObject()).getInvoiceNo();
                array[iIndex][2] = currencyFormatter.format( ((SalesBean)bean.getLineObject()).getTotalPrice() );
                array[iIndex][3] = "";
                array[iIndex][4] = currencyFormatter.format( totalBalance );

                totalCredit += ((SalesBean)bean.getLineObject()).getTotalPrice();
                recordAdded = true;
            }
            else
            if (bean.getLineObject() instanceof CustomerPaymentBean)
            {
                totalBalance = totalBalance + ((CustomerPaymentBean)bean.getLineObject()).getAmount();
                
                array[iIndex][0] = DateTimeFormatter.formatDateTime_Day( bean.getDateTime().getTime() );
                array[iIndex][1] = ((CustomerPaymentBean)bean.getLineObject()).getTerm() + "付款";
                array[iIndex][2] = "";
                array[iIndex][3] = currencyFormatter.format( ((CustomerPaymentBean)bean.getLineObject()).getAmount() );
                array[iIndex][4] = currencyFormatter.format( totalBalance );

                totalDebit += ((CustomerPaymentBean)bean.getLineObject()).getAmount();
                recordAdded = true;
            }
            
            if (recordAdded)
                iIndex++;
        }
                
        array[iIndex][0] = "总额";
        array[iIndex][2] = currencyFormatter.format(totalCredit);   
        array[iIndex][3] = currencyFormatter.format(totalDebit);   
        array[iIndex][4] = currencyFormatter.format(totalBalance);        
        
        return array;
    }
}
