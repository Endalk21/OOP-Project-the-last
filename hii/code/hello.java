import java.util.*;
public class Appointment
{ 
    private String appointmentId;
    private Date startTime;
    private Date endTime; 
    private String reason; 
    private Status status;
public Appointment(String appointmentId, Date startTime, Date endTime, String reason, Status status)
    {
        this.appointmentId = appointmentId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reason = reason;
        this.status = status;
    } 
public String getAppointmentId()
    {
         return appointmentId;
    }
public void setAppointmentId(String appointmentId)
    { 
         this.appointmentId = appointmentId;
    } 
public Date getStartTime() 
    { 
         return startTime; 
    }
public void setStartTime(Date startTime) 
    { 
         this.startTime = startTime;
    }
public Date getEndTime() 
     { 
         return endTime; 
     }
public void setEndTime(Date endTime)
     { 
         this.endTime = endTime;
     } 
public String getReason()
     {
         return reason;
     }
public void setReason(String reason) 
     {
         this.reason = reason;
     }
public Status getStatus()
     {
         return status;
     }
public void setStatus(Status status) 
     { 
         this.status = status;
     }
public boolean reschedule(Date newStart)
     {
         this.startTime = newStart;
         return true;
     }
public void cancel()
     { 
         this.status = Status.Cancelled;
     } 
}

