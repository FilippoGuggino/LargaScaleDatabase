/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.task0;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author baccio
 */
public class Employee extends User{
   /**
    * constructor which takes and initializes all attributes of the class using
    * the constructor of superclass User.
    * @param name first name of the employee
    * @param surname last name of the employee
    * @throws SQLException if an SQL exception occurred retrieving the related
    *                      record in the database
    */
    public Employee(String name, String surname) throws SQLException{
        super(name,surname,"e");
    }
    
    /**
     * takes a patient, a doctor and a date and adds a new medical in the database 
     * with their values.
     * @param patient a Patient instance to identify the patient of the new medical.
     * @param doctor a Doctor instance to identify the doctor of the new medical.
     * @param date the date of the new medical, in the format "YYYY-MM-DD"
     * @throws SQLException if an SQL exception occurred retrieving related
    *                      records in the database
    *  @return      true if medical was correctly added, false if something went wrong.
    */
    public boolean addMedical(Patient patient,Doctor doctor, String date) throws SQLException {
        int patCode=patient.getIdCode();
        int docCode=doctor.getIdCode();
        if(patCode == 0 || docCode == 0)
            return false;
        PreparedStatement ps = Interface.connection.prepareStatement(
                           " Insert into medical" 
                         + " values(?,?,?,true);");
        ps.setInt(1, docCode);
        ps.setInt(2, patCode);
        ps.setString(3, date);
        int rs = ps.executeUpdate();
        return rs == 1;
    }
    
    /**
     * takes a patient, a doctor and a date and deletes the related medical in the database 
     * @param patient a Patient instance used to retrieve the target medical.
     * @param doctor a Doctor instance used to retrieve the target medical.
     * @param date the date of the medical to be dropped, in the format "YYYY-MM-DD"
     * @throws SQLException if an SQL exception occurred retrieving related
    *                      records in the database
    *  @return      true if medical was correctly dropped, false if something went wrong.
    */
    public boolean dropMedical(Patient patient,Doctor doctor, String date) throws SQLException {
        int patCode=patient.getIdCode();
        int docCode=doctor.getIdCode();
        if(patCode == 0 || docCode == 0)
            return false;
        PreparedStatement ps = Interface.connection.prepareStatement(
                           " delete from medical" 
                         + " where fk_doctor = ? and fk_patient = ? and medical_date = ?;");
        ps.setInt(1, docCode);
        ps.setInt(2, patCode);
        ps.setString(3, date);
        int rs = ps.executeUpdate();
        return rs == 1;
    }
    
    /**
     * accepts or rejects a patient's request for a new medical depending on the value of approved:
     * if approved = 1 request is accepted, otherwise it's rejected; returns true if request
     * was correctly handled, false if something went wrong.
     * @param patient a Patient instance used to retrieve the medical request.
     * @param doctor a Doctor instance used to retrieve the medical request.
     * @param date the date of the new medical, in the format "YYYY-MM-DD"
     * @param approved if its value is 1 then request is accepted, otherwise it's rejected.
     * @throws SQLException if an SQL exception occurred retrieving related
    *                      records in the database
    *  @return      true if request was correctly handled, false if something went wrong.
    */
    public boolean handleMedicalRequest(Patient patient,Doctor doctor, String date, int approved) throws SQLException{
        int patCode=patient.getIdCode();
        int docCode=doctor.getIdCode();
        
        if(patCode == 0 || docCode == 0)
            return false;
       
        
        CallableStatement cs = Interface.connection.prepareCall("{CALL handle_medical_request(?,?,?,?)}");
        cs.setInt(1, patCode);
        cs.setInt(2, docCode);
        cs.setString(3,date);
        cs.setInt(4,approved);
        ResultSet rs = cs.executeQuery();
        if(rs.next()){
            if(rs.getInt(1) == 1)
               return true;
        }
        return false;   
    }
    
    /**
     * accepts or rejects a patient's request to delete a medical depending on the value of approved:
     * if approved = 1 request is accepted, otherwise it's rejected; returns true if request
     * was correctly handled, false if something went wrong.
     * @param patient a Patient instance used to retrieve the medical delete request.
     * @param doctor a Doctor instance used to retrieve the medical delete request.
     * @param date the date of the new medical, in the format "YYYY-MM-DD"
     * @param approved if its value is 1 then delete request is accepted, otherwise it's rejected.
     * @throws SQLException if an SQL exception occurred retrieving related
    *                      records in the database
    *  @return      true if delete request was correctly handled, false if something went wrong.
    */
    public boolean handleDeleteRequest(Patient patient,Doctor doctor, String date, int approved) throws SQLException{
        int patCode=patient.getIdCode();
        int docCode=doctor.getIdCode();

        if(patCode == 0 || docCode == 0)
            return false;
       
       
        CallableStatement cs = Interface.connection.prepareCall("{CALL handle_delete_request(?,?,?,?)}");
        cs.setInt(1, patCode);
        cs.setInt(2, docCode);
        cs.setString(3,date);
        cs.setInt(4,approved);
        ResultSet rs = cs.executeQuery();
        if(rs.next()){
            if(rs.getInt(1) == 1)
               return true;
        }
        return false;   
    }
    
    /**
     * accepts or rejects a patient's request to move the date of a medical depending on the value of approved:
     * if approved = 1 request is accepted, otherwise it's rejected; returns true if request
     * was correctly handled, false if something went wrong; doesn't need the new date of the medical
     * because it's implicit given the database structure.
     * @param patient a Patient instance used to retrieve the medical request.
     * @param doctor a Doctor instance used to retrieve the medical request.
     * @param old_date the old date of the medical, in the format "YYYY-MM-DD, 
     *                 to retrieve the medical record in the database"
     * @param approved if its value is 1 then request is accepted, otherwise it's rejected.
     * @throws SQLException if an SQL exception occurred retrieving related
    *                      records in the database
    *  @return      true if request was correctly handled, false if something went wrong.
    */
    public boolean handleMoveRequest(Patient patient,Doctor doctor, String old_date, int approved) throws SQLException{
        int patCode=patient.getIdCode();
        int docCode=doctor.getIdCode();
        if(patCode == 0 || docCode == 0)
            return false;
        
        CallableStatement cs = Interface.connection.prepareCall("{CALL handle_move_request(?,?,?,?)}");
        cs.setInt(1, patCode);
        cs.setInt(2, docCode);
        cs.setString(3,old_date);
        cs.setInt(4,approved);
        ResultSet rs = cs.executeQuery();
        if(rs.next()){
            if(rs.getInt(1) == 1)
               return true;
        }
        return false;   
    }
    
    /**
     * prints all requests for new medicals made by patients in the following format (1 per line):
     * <code>Doctor: "doctorName", Patient: "patientName", Date: "YYYY/MM/DD"</code>
     * @throws SQLException if an SQL exception occurred while preparing or executing the query
     */
    public void printMedicalRequests() throws SQLException{
        PreparedStatement ps = Interface.connection.prepareStatement(

                           " select d.name, d.surname, p.name, p.surname, m.medical_date"
                         + " medical m inner join doctor d on m.fk_doctor = d.IDCode inner join patient p on m.fk_patient = p.IDCode"
                         + " where approved=false"
                         + " order by m.medical_date;");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            System.out.println("Doctor " + rs.getString(1) + " " + rs.getString(2) + ", Patient " + rs.getString(3) + " " + rs.getString(4) + ", Date: " + rs.getString(5));
        }
    }
    
    /**
     * prints all requests to delete medicals made by patients in the following format (1 per line):
     * <code>Doctor: "doctorName", Patient: "patientName", Date: "YYYY/MM/DD"</code>
     * @throws SQLException if an SQL exception occurred while preparing or executing the query
     */
    public void printDeleteRequests() throws SQLException{
        PreparedStatement ps = Interface.connection.prepareStatement(
                           " select d.name, d.surname, p.name, p.surname, m.medical_date"
                         + " (delete_request dq inner join medical m on dq.fk_medical = m.code) inner join doctor d on m.fk_doctor = d.IDCode inner join patient p on m.fk_patient = p.IDCode"                  
                         + " order by m.medical_date;");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            System.out.println("Doctor " + rs.getString(1) + " " + rs.getString(2) + ", Patient " + rs.getString(3) + " " + rs.getString(4) + ", Date: " + rs.getString(5));
        }
    }
    
    /**
     * prints all requests to move a medical made by patients in the following format (1 per line):
     * <code>Doctor: "doctorName", Patient: "patientName", Date: "YYYY/MM/DD"</code>
     * @throws SQLException if an SQL exception occurred while preparing or executing the query
     */
    public void printMoveRequests() throws SQLException{
        PreparedStatement ps = Interface.connection.prepareStatement(
                           " select d.name, d.surname, p.name, p.surname, m.medical_date, mq.new_date"
                         + " (move_request mq inner join medical m on mq.fk_medical = m.code) inner join doctor d on m.fk_doctor = d.IDCode inner join patient p on m.fk_patient = p.IDCode"                  
                         + " order by m.medical_date;");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            System.out.println("Doctor " + rs.getString(1) + " " + rs.getString(2) + ", Patient " + rs.getString(3) + " " + rs.getString(4) + ", old date: " + rs.getString(5) + "new date: " + rs.getString(6));
        }
    }
    
    /**
     * prints the schedule of next medicals, filtering results by a patient, a doctor, a date
     * or any combination of these three paramethers; patient and doctor aren't used to filter
     * the result set if their idCode attribute is 0 (they don't have a related record in the database),
     * whilst byDate isn't used to filter the result set if its value is an empty string.
     * @param patient a Patient instance, if its idCode is not 0 it has a related record in the
     *                database and only medicals involving this patient will be shown
     * @param doctor  a Doctor instance, if its idCode is not 0 it has a related record in the
     *                database and only medicals involving this doctor will be shown
     * @param byDate  a String that may contain either an empty string or a date in "YYYY-MM-DD" format:
     *                in the first case this paramether isn't used to filter the result set,
     *                in the second case only medicals scheduled in this date will be shown
     * @throws SQLException 
     */
    public void printSchedule(Patient patient, Doctor doctor, String byDate) throws SQLException{
        int patCode=patient.getIdCode();
        int docCode=doctor.getIdCode();
        
        String query = " select d.name, d.surname, p.name, p.surname, m.medical_date"
                     + " from medical m inner join doctor d on m.fk_doctor = d.IDCode inner join patient p on m.fk_patient = p.IDCode"
                     + " where ";
        boolean previous = false;
        if(patCode != 0) {
            query += "m.fk_patient = ?";
            previous = true;
        }
        if(docCode != 0) {
            if(previous)
                query += " and ";
            query += " m.fk_doctor = ?";
            previous = true;
        }
        if(byDate.compareTo("") != 0) {
            if(previous)
                query += " and ";
            query += " m.medical_date = ?";
        }
        query += ";";
        
        PreparedStatement ps = Interface.connection.prepareStatement(query);
        int counter = 1;
        if(patCode != 0) {
            ps.setInt(counter++, patCode);
        }
        if(docCode != 0) {
            ps.setInt(counter++, docCode);
        }
        if(byDate.compareTo("") != 0) {
            ps.setString(counter++, byDate);
        }
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            System.out.println("Doctor " + rs.getString(1) + " " + rs.getString(2) + ", Patient " + rs.getString(3) + " " + rs.getString(4) + ", date: " + rs.getString(5));
        }
    }
    
    
}
