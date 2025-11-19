package com.cognizant.demo.service;

import com.cognizant.demo.entity.*;
import com.cognizant.demo.exception.PdfGenerationException;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.time.format.DateTimeFormatter; // For better date formatting

@Service
public class PdfService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public byte[] createBookingPdf(Booking booking) {
        
        // --- Expanded Input Validation ---
        if (booking == null) {
            throw new PdfGenerationException("Booking object is null.");
        }
        if (booking.getTravellers() == null || booking.getTravellers().isEmpty()) {
            throw new PdfGenerationException("Traveller data is missing or empty.");
        }
        if (booking.getContactInfo() == null || booking.getContactInfo().getEmail() == null) {
            throw new PdfGenerationException("Contact email is missing.");
        }
        if (booking.getBookingId() == null) {
            throw new PdfGenerationException("Booking ID is missing.");
        }
        if (booking.getBookingDate() == null) {
            throw new PdfGenerationException("Booking date is missing.");
        }
         if (booking.getStatus() == null) {
            throw new PdfGenerationException("Booking status is missing.");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // === Title ===
            document.add(new Paragraph("Booking Confirmation")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold().setFontSize(20).setMarginBottom(20));

            // === Customer Greeting (Now Null-Safe) ===
            String customerName = "Valued Customer";
            List<Traveller> travellers = booking.getTravellers();
            // Validation already confirmed travellers list is not empty
            Traveller firstTraveller = travellers.get(0);
            if (firstTraveller.getFirstName() != null) {
                 customerName = firstTraveller.getFirstName() + " " + safeString(firstTraveller.getLastName());
            }
            
            document.add(new Paragraph("Dear " + customerName + ",")
                    .setMarginBottom(10));
            document.add(new Paragraph(
                    "Thank you for your booking. Please find your confirmation details below."));

            // === Booking-Specific Details ===
            document.add(new Paragraph("Reservation Details")
                    .setBold().setFontSize(14).setMarginTop(20).setMarginBottom(10));

            Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
            detailsTable.setWidth(UnitValue.createPercentValue(100));

            detailsTable.addCell(createCell("Booking ID:", true));
            detailsTable.addCell(createCell(safeString(booking.getBookingId()), false)); // Use safeString
            detailsTable.addCell(createCell("Booking Status:", true));
            detailsTable.addCell(createCell(safeString(booking.getStatus()), false)); // Use safeString
            
            // Format the date safely
            String formattedDate = booking.getBookingDate().toLocalDate().format(DATE_FORMATTER);
            detailsTable.addCell(createCell("Booking Date:", true));
            detailsTable.addCell(createCell(formattedDate, false));

            // Add fields based on the *type* of booking (Now Null-Safe)
            if (booking instanceof FlightBooking fb) {
                detailsTable.addCell(createCell("Airline:", true));
                detailsTable.addCell(createCell(safeString(fb.getAirline()), false));
                detailsTable.addCell(createCell("From:", true));
                detailsTable.addCell(createCell(safeString(fb.getDeparture()), false));
                detailsTable.addCell(createCell("To:", true));
                detailsTable.addCell(createCell(safeString(fb.getArrival()), false));
                detailsTable.addCell(createCell("Departure:", true));
                detailsTable.addCell(createCell(safeString(fb.getDepartureDate()), false));
                detailsTable.addCell(createCell("Arrival:", true));
                detailsTable.addCell(createCell(safeString(fb.getArrivalDate()), false));
            } else if (booking instanceof HotelBooking hb) {
                detailsTable.addCell(createCell("Hotel Name:", true));
                detailsTable.addCell(createCell(safeString(hb.getName()), false));
                detailsTable.addCell(createCell("Check-in:", true));
                detailsTable.addCell(createCell(safeString(hb.getCheckInDate()), false));
                detailsTable.addCell(createCell("Check-out:", true));
                detailsTable.addCell(createCell(safeString(hb.getCheckOutDate()), false));
            } else if (booking instanceof PackageBooking pb) {
                detailsTable.addCell(createCell("Package Title:", true));
                detailsTable.addCell(createCell(safeString(pb.getPackageTitle()), false));
                detailsTable.addCell(createCell("Location:", true));
                detailsTable.addCell(createCell(safeString(pb.getLocation()), false));
                detailsTable.addCell(createCell("Duration:", true));
                detailsTable.addCell(createCell(safeString(pb.getDuration()), false));
            }
            document.add(detailsTable);


            // === Travellers Details (Now Null-Safe) ===
            document.add(new Paragraph("Traveller Details")
                    .setBold().setFontSize(14).setMarginTop(20).setMarginBottom(10));
            
            Table travellerTable = new Table(UnitValue.createPercentArray(new float[]{2, 2, 1, 1}));
            travellerTable.setWidth(UnitValue.createPercentValue(100));
            // Headers
            travellerTable.addHeaderCell(createCell("First Name", true));
            travellerTable.addHeaderCell(createCell("Last Name", true));
            travellerTable.addHeaderCell(createCell("Age", true));
            travellerTable.addHeaderCell(createCell("Gender", true));
            // Data
            for (Traveller t : travellers) {
                travellerTable.addCell(createCell(safeString(t.getFirstName()), false));
                travellerTable.addCell(createCell(safeString(t.getLastName()), false));
                travellerTable.addCell(createCell(safeString(t.getAge()), false)); // Handles null Integer
                travellerTable.addCell(createCell(safeString(t.getGender()), false)); 
            }
            document.add(travellerTable);

            // === Payment Details (Now Null-Safe) ===
            Payment payment = booking.getPayment();
            if (payment != null) {
                document.add(new Paragraph("Payment Details")
                        .setBold().setFontSize(14).setMarginTop(20).setMarginBottom(10));

                Table paymentTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}));
                paymentTable.setWidth(UnitValue.createPercentValue(100));
                paymentTable.addCell(createCell("Payment ID:", true));
                paymentTable.addCell(createCell(safeString(payment.getPaymentId()), false));
                
                String amountStr = "Rs " + safeString(payment.getAmount()); // Handles null BigDecimal/Double
                paymentTable.addCell(createCell("Amount:", true));
                paymentTable.addCell(createCell(amountStr, false));
                
                paymentTable.addCell(createCell("Method:", true));
                paymentTable.addCell(createCell(safeString(payment.getMethod()), false));
                paymentTable.addCell(createCell("Status:", true));
                paymentTable.addCell(createCell(safeString(payment.getStatus()), false));
                document.add(paymentTable);
            }

            // === Footer ===
            document.add(new Paragraph("We wish you a pleasant journey!")
                    .setTextAlignment(TextAlignment.CENTER).setMarginTop(30));

            document.close();

        } catch (Exception e) {
            // Re-throw any technical error
            throw new PdfGenerationException("Technical error during PDF rendering.", e);
        }

        return baos.toByteArray();
    }

    /**
     * Helper method to safely convert any object to a String for the PDF.
     * Returns "N/A" if the object is null.
     */
    private String safeString(Object obj) {
        if (obj == null) {
            return "N/A";
        }
        return obj.toString();
    }

    /**
     * Helper method to create styled table cells.
     * It now safely handles null content by default.
     */
    private Cell createCell(String content, boolean isHeader) {
        // Use safeString to ensure content is never null
        String cellContent = safeString(content);
        
        Paragraph p = new Paragraph(cellContent).setPadding(5);
        Cell cell = new Cell().add(p);
        if (isHeader) {
            cell.setBold();
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }
        return cell;
    }
}