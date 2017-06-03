package assisgnment1;

/**
 * This class
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class TeacherRecord extends Record {

    private String address;
    private String phone;
    private PublicParameters.Specialization specialization;
    private PublicParameters.Location location;

    public TeacherRecord(String firstName, String lastName) {
        super(firstName, lastName);
        this.recordId = "TR" + baseID++;
    }

    public TeacherRecord(String firstName, String lastName, String address, String phone, PublicParameters.Specialization specialization, PublicParameters.Location location) {
        super(firstName, lastName);
        this.address = address;
        this.phone = phone;
        this.specialization = specialization;
        this.location = location;
        this.recordId = "TR" + baseID++;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public PublicParameters.Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(PublicParameters.Specialization specialization) {
        this.specialization = specialization;
    }

    public PublicParameters.Location getLocation() {
        return location;
    }

    public void setLocation(PublicParameters.Location location) {
        this.location = location;
    }

    public void setLocation(String location){
        this.location = PublicParameters.Location.valueOf(location);
    }
    @Override
    public String getRecordID (){
        return this.recordId;
    }

    public void setRecordID(String recordID){
        this.recordId = recordID;
    }
}
