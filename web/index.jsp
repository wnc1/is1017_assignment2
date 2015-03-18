<%-- 
    Document   : index
    Created on : Feb 19, 2015, 7:04:36 PM
    Author     : bconner
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="edu.pitt.bank.Security"%>
<%@page import="edu.pitt.bank.Customer"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="edu.pitt.utilities.MySqlUtilities"%>
<%@page import="edu.pitt.utilities.DbUtilities"%>
<%@page import="edu.pitt.utilities.ErrorLogger"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Bank Account: Login</title>
        <link rel="stylesheet" href="css/bootstrap.css" type="text/css" media="screen" title="no title" charset="utf-8">

    </head>
    <body>
        <div class="container">
            <h1>WNC Bank</h1>
            <form id="frmLogin" name="frmLogin" action="index.jsp" method="POST">
                <div class="form-group">
                    <label for="txtLogin">Login name:</label>
                    <input type="text" class="form-control" id="txtLogin" name="txtLogin" value="" placeholder="Enter username" required/>        
                </div>
                <div class="form-group">
                    <label for="txtPassword">Pin number:</label>
                    <input type="password" class="form-control" id="txtPassword" name="txtPassword" value="" placeholder="Enter pin number" required/>
                </div>
                <p><input type="submit" class="btn btn-primary" id="btnSubmit" name="btnSubmit" value="Submit" /></p>
            </form>
       
        <%
            //check to see if there is a session expired message
            if(session.getAttribute("serverMessage")!= null){              
                String message = (String) session.getAttribute("serverMessage");
                out.print("<p class='bg-warning'>" + message +" </p>");
            }
            //clear message
            request.getSession().removeAttribute("serverMessage");
            
            String txtLogin, txtPassword;
            txtLogin = request.getParameter("txtLogin");
            txtPassword = request.getParameter("txtPassword");
            
            //validate input
            if(txtLogin !=null && txtPassword != null && !(txtLogin.equals("")) && !(txtPassword.equals(""))){
                
                try{
                    Security security = new Security();
                    Customer cust = security.validateLogin(txtLogin, Integer.parseInt(txtPassword));
                    
                    if(cust!=null){
                        ArrayList<String> permissions = security.listUserGroups(cust.getCustomerID());//get permissions
                        session.setAttribute("authenticatedUser", cust); //places the customer in session
                        session.setAttribute("userPermissions", permissions);//place permissions in session
                        response.sendRedirect("accountdetailview.jsp");//redirect to new page
                    }else{
                        out.print("<p class='bg-danger'>Invalid login information.</p>");
                    }
                }catch(NumberFormatException ex){
                    out.print("<p class='bg-danger'>Pin number must be numeric.</p>");
                }           
            }
        %>
        </div>
    </body>
</html>
