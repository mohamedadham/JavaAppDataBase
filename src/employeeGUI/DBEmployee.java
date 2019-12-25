/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employeeGUI;

import java.util.Vector;
import java.sql.*;

public class DBEmployee {
    public Vector<Employee> emp;
    Connection conn;
    int index;
    int size;
    DBEmployee(){
        emp = new Vector<Employee>();
        index = 0;
        size = 0;
    }
    //start new Connection to the database for every query
    public void startConnection(){
        if (conn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee","root","mohamedshafik");
                System.out.println("Connection Started...");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //close the current opened Connection to the database for every query
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connection Closed...");
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public void selectAll() throws SQLException{
        startConnection();
        Employee tmp;
        Statement stmt = conn.createStatement() ;
        String queryString = "select * from emp";
        ResultSet rs = stmt.executeQuery(queryString) ;
           while(rs.next()){
               tmp = new Employee();
               tmp.setId(rs.getInt("id"));
               tmp.setfName(rs.getString("FirstName"));
               tmp.setmName(rs.getString("MiddleName"));
               tmp.setlName(rs.getString("LastName"));
               tmp.setEmail(rs.getString("Email"));
               tmp.setPhoneNum(rs.getString("PhoneNumber"));
               emp.add(tmp);
           }
           updateSize();
           rs.close();
        closeConnection();
        
    }
    public Employee selectNext(){
        return emp.elementAt(++index);
    }
    public Employee selectPrev(){
        if(index==size){
            index = size-2;
        }
        return emp.elementAt(--index);
    }
    public Employee selectFirst(){
        return emp.firstElement();
    }
    public Employee selectLast(){
        return emp.lastElement();
    }
    public int  insert(Employee employee) throws SQLException{
        startConnection();                
        String query="insert into employee.emp(FirstName,MiddleName,Lastname,Email, PhoneNumber)values(?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1,employee.getfName());
        pst.setString(2,employee.getmName());
        pst.setString(3,employee.getlName());
        pst.setString(4,employee.getEmail());
        pst.setString(5,employee.getPhoneNum());       
        pst.execute();
        query="select * from employee.emp ORDER BY id DESC LIMIT 1";
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(query);
        rs.next();
        
        emp.add(employee);        
        updateSize();
        index=size-1;
        return rs.getInt("id");
        //closeConnection();
        
    }
    public void delete() throws SQLException{
        startConnection();
        String query = "delete from emp where id = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setInt(1, emp.elementAt(index).getId());
        // execute the preparedstatement
        preparedStmt.execute();
        closeConnection();
        emp.remove(index--);
        if(index < 0){
            index = 0;
        }
    }
    public void update(Employee e) throws SQLException{
        startConnection();
        String query = "update emp set FirstName = ? , MiddleName = ?, LastName = ?, Email = ?, PhoneNumber = ? where id = ?";
        PreparedStatement preparedStmt = conn.prepareStatement(query);
        preparedStmt.setString(1, e.getfName());
        preparedStmt.setString(2, e.getmName());
        preparedStmt.setString(3, e.getlName());
        preparedStmt.setString(4, e.getEmail());
        preparedStmt.setString(5, e.getPhoneNum());
        preparedStmt.setInt(6, e.getId());
        
        // execute the java preparedstatement
        preparedStmt.executeUpdate();
        closeConnection();
        emp.elementAt(index).setfName(e.getfName());
        emp.elementAt(index).setlName(e.getlName());
        emp.elementAt(index).setmName(e.getmName());
        emp.elementAt(index).setEmail(e.getEmail());
        emp.elementAt(index).setPhoneNum(e.getPhoneNum());
    }
    private void updateSize() {
        size = emp.size();
    }
    public int getSize(){
        return size;
    }
    public int getIndex(){
        return index;
    }
    public void setIndex(int ind){
        index=ind;
    }
 
}

