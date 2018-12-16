package packMongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;


import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class teammate extends Document {
    private String name, surname, username, password;
    private String[] projects;
    private int salary, assignment;
    private long id_number;
    private Date birthdate;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public teammate(){

    }

    public teammate( Document person, MongoDatabase database){
        this.name = (String) person.get("name");
        this.surname = (String)person.get("surname");
        this.username = (String)person.get("username");
        this.birthdate = (Date) person.get("birthdate");
        this.assignment = (int)person.get("assignment");
        this.projects = getProjectsAsArray(database, this.username);
        this.salary = person.getInteger("salary");
        this.id_number = person.getInteger("id_number");
        this.password=(String)person.get("password");
    }

    public String getAssignmentAsString(MongoDatabase database, String assignmentAsInt){
        MongoCollection<Document> collection = database.getCollection("roles");
        Document assignmentDoc = collection.find(eq("role_id",assignmentAsInt)).first();
        return (String)assignmentDoc.get("role_name");
    }

    public String[] getProjectsAsArray(MongoDatabase database, String username){
        MongoCollection<Document> collection = database.getCollection("teammates");
        Document person = collection.find(eq("username",username)).first();
        if(person.get("projects")!=null){
            return (String[]) person.get("projects");
        }
        else{
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public int getAssignment() {
        return assignment;
    }

    public void setAssignment(int assignment) {
        this.assignment = assignment;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public long getId_number() {
        return id_number;
    }

    public void setId_number(long id_number) {
        this.id_number = id_number;
    }
}
