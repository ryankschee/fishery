package com.ryanworks.fishery.client.print;

import com.ryanworks.fishery.client.delegate.CustomerDelegate;
import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.CustomerPaymentBean;
import com.ryanworks.fishery.shared.bean.SalesBean;
import com.ryanworks.fishery.shared.bean.SalesBucketBean;
import com.ryanworks.fishery.shared.bean.SalesLineBean;
import com.ryanworks.fishery.shared.swing.date.MyDatePicker;
import com.ryanworks.fishery.shared.util.LogBook;
import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class OutboundPDFBoxPrinter 
    extends RootPDFBoxPrinter
{ 
    private static final OutboundPDFBoxPrinter instance = new OutboundPDFBoxPrinter();
    private final DecimalFormat currencyFormatter = new DecimalFormat("0.00");
    
    private OutboundPDFBoxPrinter() { 
        super(); 
    }    
    public static OutboundPDFBoxPrinter getInstance() { return instance; }
    
    public void init() {        
        // Init
        getFontFileSimkai();
        getFontFileSimhei();
        getFontPlain();
        getFontBold();
        getFontOblique();        
    }
    
    //--------------------------------------------------------------------------
        
    public void printReportCustomerSalesReportChinese(String saveTo, CustomerBean customer, SalesBean salesBean, boolean showPrice)
    {        
        try 
        {
            double totalPrice = 0.0d;
            for (SalesLineBean lineBean : salesBean.getLineList())
            {
                totalPrice -= (lineBean.getUnitPrice() * lineBean.getWeight());
            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );
            PDFont simheiFont = PDType0Font.load(document, this.getFontFileSimhei() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

            printPlainText(contentStream, simkaiFont,  10, 660, 16, "客户："); 
            printPlainText(contentStream, simkaiFont,  60, 660, 16, customer.getId()); 
            printPlainText(contentStream, simkaiFont, 120, 660, 16, customer.getName()); 
            printPlainText(contentStream, simkaiFont,  10, 635, 14, "单号：");  
            printPlainText(contentStream, simkaiFont,  60, 635, 14, salesBean.getInvoiceNo()); 
            printPlainText(contentStream, simkaiFont, 440, 660, 14, "单据类别：");  
            printPlainText(contentStream, simkaiFont, 520, 660, 14, "出货"); 
            printPlainText(contentStream, simkaiFont, 440, 635, 14, "单据日期：");  
            printPlainText(contentStream, simkaiFont, 520, 635, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(salesBean.getDateTime())); 
            //printPlainText(contentStream, simkaiFont, 320, 645, 14, "打印日期："); 
            //printPlainText(contentStream, simkaiFont, 400, 645, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime())); 
            //printPlainText(contentStream, simkaiFont, 470, 645, 14, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime())); 

            if (showPrice)
            {
                Calendar startTime = Calendar.getInstance(); 
                startTime.setTimeInMillis( salesBean.getDateTime() );
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);

                Calendar endTime = Calendar.getInstance();
                endTime.setTimeInMillis( salesBean.getDateTime() );
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                endTime.set(Calendar.MINUTE, 59);
                endTime.set(Calendar.SECOND, 59);

                List<CustomerPaymentBean> paymentList = 
                        CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(
                                customer.getId(), startTime.getTimeInMillis(), endTime.getTimeInMillis());

                double totalPaymentOfTheDay = 0.0d;
                for (CustomerPaymentBean paymentBean : paymentList)
                {
                    totalPaymentOfTheDay += paymentBean.getAmount();
                }
                
                printPlainText(contentStream, simkaiFont, 10, 20, 14, "打印日期："); 
                printPlainText(contentStream, simkaiFont, 90, 20, 14, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime())); 
                printPlainText(contentStream, simkaiFont, 180, 20, 14, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime())); 
                
                printPlainText(contentStream, simkaiFont, 440, 20, 14, "总额：");
                printPlainText(contentStream, simkaiFont, 480, 20, 14, "RM");

                double textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(totalPrice)) )/1000 * 10;
                double adjustment_x = 70 - textWidth;            
                
                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 20, 14, currencyFormatter.format(Math.abs(totalPrice)));
                
                /*
                double balance0 = customer.getBalance() - totalPrice - totalPaymentOfTheDay;
                if (balance0 >= 0)
                {
                    printPlainText(contentStream, simkaiFont, 440,  80, 12, "剩余：");
                    printPlainText(contentStream, simkaiFont, 480,  80, 12, "+RM");
                }
                else
                {
                    printPlainText(contentStream, simkaiFont, 440,  80, 12, "尚欠：");
                    printPlainText(contentStream, simkaiFont, 480,  80, 12, "-RM");
                }

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(balance0)) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 80, 12, currencyFormatter.format(Math.abs(balance0)));

                contentStream.drawLine(440, 75, 575, 75);

                double balance1 = totalPrice + balance0;

                if (balance1 >= 0)
                {
                    printPlainText(contentStream, simkaiFont, 440,  60, 12, "总余：");
                    printPlainText(contentStream, simkaiFont, 480,  60, 12, "+RM");
                }
                else
                {
                    printPlainText(contentStream, simkaiFont, 440,  60, 12, "总欠：");
                    printPlainText(contentStream, simkaiFont, 480,  60, 12, "-RM");
                }

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(balance1)) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 60, 12, currencyFormatter.format(Math.abs(balance1)));

                printPlainText(contentStream, simkaiFont, 440, 40, 12, "来银：");
                printPlainText(contentStream, simkaiFont, 480, 40, 12, "+RM");

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(totalPaymentOfTheDay) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 40, 12, currencyFormatter.format(Math.abs(totalPaymentOfTheDay)));
                
                contentStream.drawLine(440, 35, 575, 35);   

                double balance2 = balance1 + totalPaymentOfTheDay;
                        
                if (balance2 >= 0)
                {
                    printPlainText(contentStream, simkaiFont, 440,  20, 12, "剩余：");
                    printPlainText(contentStream, simkaiFont, 480,  20, 12, "+RM");
                }
                else
                {
                    printPlainText(contentStream, simkaiFont, 440,  20, 12, "尚欠：");
                    printPlainText(contentStream, simkaiFont, 480,  20, 12, "-RM");
                }

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(balance2)) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 20, 12, currencyFormatter.format(Math.abs(balance2)));
                        */
            }      

            int y_coordinate = 610;
            int rowHeightPixel = 22;
            int rowCount = 0;
            int rowSpace = 16;
            int visibleTableCount = 0;
            for (SalesBucketBean bucketObj : salesBean.getBucketList())
            {
                List<SalesLineBean> salesLineList = new ArrayList<SalesLineBean>();
                for (SalesLineBean salesLineBean : salesBean.getLineList())
                {
                    if (bucketObj.getBucketNo().equals(salesLineBean.getBucketNo()))
                        salesLineList.add(salesLineBean);
                }
                
                if (salesLineList.size() > 0)
                {      
                    visibleTableCount++;
                    rowCount = (int) Math.ceil( salesLineList.size() / 2);
                            
                    int middle_index = 0;
                    if ( (salesLineList.size()%2)==1 )
                        middle_index = (int) (salesLineList.size()+1) / 2;
                    else
                        middle_index = (int) salesLineList.size() / 2;

                    String[][] reportArrayData1 = this.getReportCustomerSalesChineseArray( salesLineList, 0, middle_index, showPrice);    
                    String[][] reportArrayData2 = this.getReportCustomerSalesChineseArray( salesLineList, middle_index, salesLineList.size(), showPrice);    

                    boolean showHeader = true;
                    if (visibleTableCount >= 2) 
                        showHeader = false;
                    
                    // Draw table on page #1        
                    boolean[] chineseColumn1 = {false, true, false, false, false};
                    int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                    drawTwoColumnTable(document, page, contentStream, simkaiFont, y_coordinate, 10, trim2dArray(reportArrayData1,0,19), false, chineseColumn1, textAlignment1, showHeader, true);
                    drawTwoColumnTable(document, page, contentStream, simkaiFont, y_coordinate, 320, trim2dArray(reportArrayData2,0,19), false, chineseColumn1, textAlignment1, showHeader, true);
                    
                    y_coordinate = y_coordinate - (rowHeightPixel * rowCount) - rowSpace;
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
            
            try {
                PrintWriter pw = new PrintWriter(new File("file_" + Calendar.getInstance().getTimeInMillis() + ".txt"));
                e.printStackTrace(pw);
                pw.close();
            } catch (Exception ioe) {
                ioe.printStackTrace();
                LogBook.Log("exception: " + ioe.getMessage());
            }
        }
    }    
    
    public void printReportCustomerSalesReportMalay(String saveTo, CustomerBean customer, SalesBean salesBean, boolean showPrice)
    {        
        try
        {
            double totalPrice = 0.0d;
            for (SalesLineBean lineBean : salesBean.getLineList())
            {
                totalPrice -= (lineBean.getUnitPrice() * lineBean.getWeight());
            }

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setMediaBox(new PDRectangle(DIMENSION_WIDTH_IN_POINTS, DIMENSION_HEIGHT_IN_POINTS));
            document.addPage( page );

            // Create a new font object selecting one of the PDF base fonts
            PDFont simkaiFont = PDType0Font.load(document, getFontFileSimkai() );

            // Start a new content stream which will "hold" the to be created content
            PDPageContentStream contentStream = printHeader(document, page, simkaiFont);

            printPlainText(contentStream, simkaiFont,  10, 660, 12, "Pelanggan:"); 
            printPlainText(contentStream, simkaiFont, 120, 660, 12, customer.getId()); 
            printPlainText(contentStream, simkaiFont, 180, 660, 12, customer.getName()); 
            printPlainText(contentStream, simkaiFont,  10, 645, 12, "No.Penerimaan:");  
            printPlainText(contentStream, simkaiFont, 120, 645, 12, salesBean.getInvoiceNo()); 
            printPlainText(contentStream, simkaiFont, 350, 660, 12, "Tarikh:");  
            printPlainText(contentStream, simkaiFont, 470, 660, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(salesBean.getDateTime())); 
            printPlainText(contentStream, simkaiFont, 350, 645, 12, "Tarikh Percetakan:"); 
            printPlainText(contentStream, simkaiFont, 470, 645, 12, MyDatePicker.SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime())); 
            printPlainText(contentStream, simkaiFont, 540, 645, 12, MyDatePicker.TIME_HHMMSS_FORMAT.format(Calendar.getInstance().getTime())); 

            if (showPrice)
            {
                Calendar startTime = Calendar.getInstance(); 
                startTime.setTimeInMillis( salesBean.getDateTime() );
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);

                Calendar endTime = Calendar.getInstance();
                endTime.setTimeInMillis( salesBean.getDateTime() );
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                endTime.set(Calendar.MINUTE, 59);
                endTime.set(Calendar.SECOND, 59);

                List<CustomerPaymentBean> paymentList = 
                        CustomerDelegate.getInstance().getPaymentsByCustomerAndDateRange(
                                customer.getId(), startTime.getTimeInMillis(), endTime.getTimeInMillis());

                double totalPaymentOfTheDay = 0.0d;
                for (CustomerPaymentBean paymentBean : paymentList)
                {
                    totalPaymentOfTheDay += paymentBean.getAmount();
                }

                printPlainText(contentStream, simkaiFont, 380, 20, 12, "Jumlah:");
                printPlainText(contentStream, simkaiFont, 470, 20, 12, "-RM");

                double textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(totalPrice)) )/1000 * 10;
                double adjustment_x = 70 - textWidth;            

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 20, 12, currencyFormatter.format(Math.abs(totalPrice)));
                /*
                double balance0 = customer.getBalance() - totalPrice - totalPaymentOfTheDay;
                if (balance0 >= 0)
                {
                    printPlainText(contentStream, simkaiFont, 380,  80, 12, "Baki:");
                    printPlainText(contentStream, simkaiFont, 470,  80, 12, "+RM");
                }
                else
                {
                    printPlainText(contentStream, simkaiFont, 380,  80, 12, "Hutang:");
                    printPlainText(contentStream, simkaiFont, 470,  80, 12, "-RM");
                }

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(balance0)) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 80, 12, currencyFormatter.format(Math.abs(balance0)));

                contentStream.drawLine(440, 75, 575, 75);

                double balance1 = totalPrice + balance0;

                if (balance1 >= 0)
                {
                    printPlainText(contentStream, simkaiFont, 380,  60, 12, "Jumlah Baki:");
                    printPlainText(contentStream, simkaiFont, 470,  60, 12, "+RM");
                }
                else
                {
                    printPlainText(contentStream, simkaiFont, 380,  60, 12, "Jumlah Hutang:");
                    printPlainText(contentStream, simkaiFont, 470,  60, 12, "-RM");
                }

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(balance1)) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 60, 12, currencyFormatter.format(Math.abs(balance1)));

                printPlainText(contentStream, simkaiFont, 380, 40, 12, "Bayaran:");
                printPlainText(contentStream, simkaiFont, 470, 40, 12, "+RM");

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(totalPaymentOfTheDay) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 40, 12, currencyFormatter.format(Math.abs(totalPaymentOfTheDay)));

                contentStream.drawLine(440, 35, 575, 35);   

                double balance2 = balance1 + totalPaymentOfTheDay;

                if (balance2 >= 0)
                {
                    printPlainText(contentStream, simkaiFont, 380,  20, 12, "Baki:");
                    printPlainText(contentStream, simkaiFont, 470,  20, 12, "+RM");
                }
                else
                {
                    printPlainText(contentStream, simkaiFont, 380,  20, 12, "Hutang:");
                    printPlainText(contentStream, simkaiFont, 470,  20, 12, "-RM");
                }

                textWidth = getFontPlain().getStringWidth( currencyFormatter.format(Math.abs(balance2)) )/1000 * 10;
                adjustment_x = 70 - textWidth; 

                printPlainText(contentStream, simkaiFont, (float)(490+adjustment_x), 20, 12, currencyFormatter.format(Math.abs(balance2)));  
                        */
            }      

            
            int y_coordinate = 610;
            int rowHeightPixel = 20;
            int rowCount = 0;
            int rowSpace = 16;
            int visibleTableCount = 0;
            for (SalesBucketBean bucketObj : salesBean.getBucketList())
            {
                List<SalesLineBean> salesLineList = new ArrayList<SalesLineBean>();
                for (SalesLineBean salesLineBean : salesBean.getLineList())
                {
                    if (bucketObj.getBucketNo().equals(salesLineBean.getBucketNo()))
                        salesLineList.add(salesLineBean);
                }
                
                if (salesLineList.size() > 0)
                {      
                    visibleTableCount++;
                    rowCount = salesLineList.size();
                            
                    boolean showHeader = true;
                    if (visibleTableCount >= 2)
                        showHeader = false;
                    
                    String[][] reportArrayData = this.getReportCustomerSalesMalayArray( salesLineList, 0, salesLineList.size(), showPrice);
                    boolean[] chineseColumn1 = {false, false, false, false, false};
                    int[] textAlignment1 = {TEXT_ALIGN_LEFT, TEXT_ALIGN_LEFT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT, TEXT_ALIGN_RIGHT};
                    drawTable(document, page, contentStream, simkaiFont, y_coordinate, 10, trim2dArray(reportArrayData,0,19), true, chineseColumn1, textAlignment1, showHeader, true, false);

                    y_coordinate = y_coordinate - (rowHeightPixel * rowCount) - rowSpace;
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
    
    private String[] getReportCustomerSalesChineseTableHeader() 
    {
        return new String[] {"桶号", "鱼类", "价格", "重量", "银额"};
    }
    
    private String[][] getReportCustomerSalesChineseArray(List<SalesLineBean> salesLineList, int startIndex, int endIndex, boolean showPrice) 
    {        
        String[] header = getReportCustomerSalesChineseTableHeader();
        String[][] array = new String[endIndex-startIndex+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        int iIndex = 1;
        for (int i=startIndex ; i<endIndex ; i++)
        {
            SalesLineBean bean = salesLineList.get(i);
            
            array[iIndex][0] = bean.getBucketNo();
            array[iIndex][1] = bean.getItemNewName();
            array[iIndex][2] = showPrice ? currencyFormatter.format(bean.getUnitPrice()) : "";
            array[iIndex][3] = currencyFormatter.format(bean.getWeight());
            array[iIndex][4] = showPrice ? currencyFormatter.format(bean.getUnitPrice()*bean.getWeight()) : "";
            
            iIndex++;
        }     
        
        return array;
    }
    
    private String[] getReportCustomerSalesMalayTableHeader() 
    {
        return new String[] {"No.Baldi", "Ikan", "Harga", "Berat", "Jumlah"};
    }
    
    private String[][] getReportCustomerSalesMalayArray(List<SalesLineBean> salesLineList, int startIndex, int endIndex, boolean showPrice) 
    {        
        String[] header = getReportCustomerSalesMalayTableHeader();
        String[][] array = new String[endIndex-startIndex+1][header.length];
        
        for (int i=0 ; i<header.length ; i++) {
            array[0][i] = header[i];
        }
        
        int iIndex = 1;
        for (int i=startIndex ; i<endIndex ; i++)
        {
            SalesLineBean bean = salesLineList.get(i);
            
            array[iIndex][0] = bean.getBucketNo();
            array[iIndex][1] = bean.getItemNewName() + "    - " + bean.getItemNameBm();
            array[iIndex][2] = showPrice ? currencyFormatter.format(bean.getUnitPrice()) : "";
            array[iIndex][3] = currencyFormatter.format(bean.getWeight());
            array[iIndex][4] = showPrice ? currencyFormatter.format(bean.getUnitPrice()*bean.getWeight()) : "";
            
            iIndex++;
        }     
        
        return array;
    }
}
