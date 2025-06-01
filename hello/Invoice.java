package hello;

public public class Invoice {
    private static int counter = 0;
    private String invoiceId;
    private double totalAmount;
    private Status status;

    public Invoice(double totalAmount) {
        this.invoiceId = "INV" + (++counter);
        this.totalAmount = totalAmount;
        this.status = Status.Pending;
    }

    public String getInvoiceId() { return invoiceId; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public void generatePDF() {
        System.out.println("PDF for invoice " + invoiceId + " generated.");
    }
}
 {
    
}
