package assisgnment1;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class StudentRecord extends Record {
    private PublicParameters.CoursesRegistered coursesRegistered;
    private PublicParameters.Status status;
    private Date statusDate;

    public StudentRecord(String firstName, String lastName) {
        super(firstName, lastName);
        this.recordId = "SD" + baseID++;
    }

    public StudentRecord(String firstName, String lastName, PublicParameters.CoursesRegistered coursesRegistered, PublicParameters.Status status, Date statusDate) {
        super(firstName, lastName);
        this.coursesRegistered = coursesRegistered;
        this.status = status;
        this.statusDate = statusDate;
        this.recordId = "SD" + baseID++;
    }

    public PublicParameters.CoursesRegistered getCoursesRegistered() {
        return coursesRegistered;
    }

    public void setCoursesRegistered(PublicParameters.CoursesRegistered coursesRegistered) {
        this.coursesRegistered = coursesRegistered;
    }

    public PublicParameters.Status getStatus() {
        return status;
    }

    public void setStatus(PublicParameters.Status status) {
        this.status = status;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getRecordID (){
        return this.recordId;
    }

    public void setRecordID(String recordID){
        this.recordId = recordID;
    }
}
