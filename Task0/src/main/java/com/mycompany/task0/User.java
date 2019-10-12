/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.task0;

import java.sql.*;

/**
 *
 * @author Zaccaria
 */
public class User {
    
    protected String name;
    protected String surname;
    protected String role;
    protected int idCode;
    
    public User(String name,String surname, String role)throws SQLException{
        if(name.compareTo("") == 0)
            name = "NaN";
         if(surname.compareTo("") == 0)
            surname = "NaN";
        this.name=name;
        this.surname=surname;
        switch(role){
            case "d":
                this.role="doctor";
                break;
            case "p":
                this.role="patient";
                break;
            case "e":
                this.role="employee";
                break;
        }
        signIn();
    }
     protected void signIn() throws SQLException{
        CallableStatement cs = Interface.connection.prepareCall("{CALL get_code(?,?,?)}");
        cs.setString(1, name);
        cs.setString(2, surname);
        cs.setString(3,role);
        ResultSet rs = cs.executeQuery();
        if(rs.next())
            this.idCode = rs.getInt(1);
        else 
            idCode=0;
    }
    
    public boolean signUp() throws SQLException{
        if(idCode != 0)
            return false;
        PreparedStatement ps = Interface.connection.prepareStatement(
                        " insert into ?"
                      + " values(?,?);");
        ps.setString(1, role);
        ps.setString(2, name);
        ps.setString(3, surname);
        int rs = ps.executeUpdate();
        if(rs > 0) {
            return true;
        }
        return false;
    }
    public int getIdCode(){
        return idCode;
    }
}
