package assisgnment1;

/**
 * This class
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class Record {
    protected static int baseID = 10000;
    private String firstName;
    private String lastName;
    protected String recordId;

    public Record(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRecordId(){
        if(this instanceof StudentRecord){
            ((StudentRecord)this).getRecordId();
        }
        if (this instanceof TeacherRecord){
            ((TeacherRecord)this).getRecordId();
        }
        return "";
    }
}
